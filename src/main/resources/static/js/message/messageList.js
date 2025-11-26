(function () {
    const Model = {
        state: {
            apiBase: window.__API_BASE__ || '/api/message',
            memberId: null,
            boxType: 'RECEIVED',
            targetType: '',
            title: '',
            offset: 0,
            limit: 10,
            total: null,
            items: []
        },
        resolveMemberId() {
            const memberId = Number(document.body.dataset.memberId);
            if (!Number.isFinite(memberId) || memberId <= 0) {
                throw new Error('유효한 memberId가 없습니다. <body data-member-id="...">를 주입하세요.');
            }
            return memberId;
        },
        normalizeResponse(data, headers) {
            if (Array.isArray(data)) {
                const items = data;
                const hdr = headers && (headers['x-total-count'] ?? headers['X-Total-Count']);
                const headerTotal = Number(hdr);
                if (Number.isFinite(headerTotal)) return { items, total: headerTotal };
                const first = items[0];
                const tc = Number(first?.totalCount ?? first?.total_count);
                return { items, total: Number.isFinite(tc) ? tc : null };
            }
            return { items: [], total: null };
        },
        async fetchMessages() {
            const s = this.state;
            const url = `${s.apiBase}/${s.memberId}/${s.boxType}`;
            const params = new URLSearchParams({ offset: String(s.offset), limit: String(s.limit) });
            if (s.targetType) params.set('targetType', s.targetType);
            if (s.title) params.set('title', s.title);
            const res = await axios.get(`${url}?${params.toString()}`);
            const { items, total } = this.normalizeResponse(res.data, res.headers);
            s.items = items;
            s.total = total;
        }
    };

    const View = {
        fmtKST(iso) {
            if (!iso) return '';
            return new Intl.DateTimeFormat('ko-KR', {
                timeZone: 'Asia/Seoul',
                year: 'numeric', month: '2-digit', day: '2-digit',
                hour: '2-digit', minute: '2-digit', second: '2-digit',
                hour12: false
            }).format(new Date(iso)).replace(/\./g, '-').replace(/-\s/g, '-').replace(/\s/g, ' ');
        },
        labelTargetType(tt) {
            return ({ QUESTION: '질문', SHARING: '나눔' }[tt] || '');
        },
        esc(s) {
            return String(s ?? '').replaceAll('&','&amp;').replaceAll('<','&lt;')
                .replaceAll('>','&gt;').replaceAll('"','&quot;').replaceAll("'",'&#39;');
        },
        buildTargetUrl(type, id) {
            if (!type || !id) return '';
            if (type === 'SHARING') return `/sharing/${id}`;
            if (type === 'QUESTION') return `/question/${id}`;
            return '';
        },
        renderRows(list) {
            const tbody = document.getElementById('messageTbody');
            if (!tbody) return;
            if (!Array.isArray(list) || list.length === 0) {
                tbody.innerHTML = `<tr><td class="text-center" colspan="8" style="height:120px">데이터가 없습니다.</td></tr>`;
                const checkAll = document.getElementById('checkAll');
                if (checkAll) { checkAll.checked = false; checkAll.indeterminate = false; }
                return;
            }
            tbody.innerHTML = list.map(item => {
                const readText = item.readFlag ? '읽음' : '안읽음';
                const category = View.labelTargetType(item.targetType);
                const relatedUrl = View.buildTargetUrl(item.targetType, item.targetId);
                const relatedText = View.esc(item.content || '(관련 글 보기)');
                return `
          <tr data-id="${item.messageId}">
            <td class="text-center" style="width:44px;">
              <input type="checkbox" class="form-check-input row-check" value="${item.messageId}">
            </td>
            <td style="width:96px;">${readText}</td>
            <td style="width:120px;">${View.esc(item.senderNickname ?? '')}</td>
            <td style="width:120px;">${View.esc(item.receiverNickname ?? '')}</td>
            <td style="width:90px;">${category}</td>
            <td class="text-truncate" style="max-width:320px;">${View.esc(item.title || '')}</td>
            <td class="text-truncate">
              ${relatedUrl ? `<a href="${relatedUrl}" class="text-decoration-none">${relatedText}</a>` : '-'}
            </td>
            <td class="text-nowrap" style="width:170px;">${View.fmtKST(item.createdAt)}</td>
          </tr>
        `;
            }).join('');
            wireCheckEvents();
        },
        renderPager(state, goPage) {
            const ul = document.getElementById('pager');
            if (!ul) return;
            const { offset, limit, total, items } = state;
            let totalPages = null;
            if (typeof total === 'number' && total >= 0) totalPages = Math.max(1, Math.ceil(total / limit));
            const current = Math.floor(offset / limit) + 1;
            const li = (disabledOrActive, label, clickHandler, aria, isNumber = false) => {
                const liEl = document.createElement('li');
                liEl.className = `page-item${disabledOrActive ? ' disabled' : ''}${isNumber && disabledOrActive ? ' active' : ''}`;
                const a = document.createElement('a');
                a.className = 'page-link';
                a.href = '#';
                if (aria) a.setAttribute('aria-label', aria);
                a.textContent = label;
                if (!disabledOrActive) a.addEventListener('click', ev => { ev.preventDefault(); clickHandler && clickHandler(); });
                else if (isNumber && disabledOrActive) a.addEventListener('click', ev => ev.preventDefault());
                liEl.appendChild(a);
                return liEl;
            };
            const isLast = totalPages === null ? (items.length < limit) : (current >= totalPages);
            let start = 1, end = 1;
            if (totalPages !== null) {
                const windowSize = 5;
                start = Math.max(1, current - Math.floor(windowSize / 2));
                end = Math.min(totalPages, start + windowSize - 1);
                start = Math.max(1, end - windowSize + 1);
            } else { start = current; end = current; }
            ul.innerHTML = '';
            ul.appendChild(li(current === 1, '«', () => goPage(1), '처음'));
            ul.appendChild(li(current === 1, '‹', () => goPage(current - 1), '이전'));
            for (let p = start; p <= end; p++) {
                const isActive = p === current;
                ul.appendChild(li(isActive, String(p), () => goPage(p), null, true));
            }
            ul.appendChild(li(isLast, '›', () => goPage(current + 1), '다음'));
            if (totalPages !== null) ul.appendChild(li(isLast, '»', () => goPage(totalPages), '마지막'));
            else ul.appendChild(li(true, '»', null, '마지막'));
        }
    };

    function wireCheckEvents() {
        const checkAll = document.getElementById('checkAll');
        const rowChecks = Array.from(document.querySelectorAll('.row-check'));
        if (!checkAll) return;
        checkAll.checked = false;
        checkAll.indeterminate = false;
        checkAll.onchange = () => { rowChecks.forEach(chk => { chk.checked = checkAll.checked; }); };
        rowChecks.forEach(chk => {
            chk.onchange = () => {
                const total = rowChecks.length;
                const checked = rowChecks.filter(c => c.checked).length;
                checkAll.checked = checked === total;
                checkAll.indeterminate = checked > 0 && checked < total;
            };
        });
    }

    function getSelectedMessageIds() {
        return Array.from(document.querySelectorAll('.row-check:checked'))
            .map(chk => Number(chk.value))
            .filter(n => Number.isFinite(n));
    }

    async function afterDeleteRefresh(deletedCount) {
        const s = Model.state;
        if (typeof s.total === 'number' && s.total >= 0) {
            const remaining = s.total - deletedCount;
            const maxPage = Math.max(1, Math.ceil(Math.max(0, remaining) / s.limit));
            const currentPage = Math.floor(s.offset / s.limit) + 1;
            if (currentPage > maxPage) s.offset = (maxPage - 1) * s.limit;
        }
        await Model.fetchMessages();
        View.renderRows(s.items);
        View.renderPager(s, goPage);
    }

    async function deleteSelected() {
        const ids = getSelectedMessageIds();
        if (ids.length === 0) { alert('삭제할 쪽지를 선택하세요.'); return; }
        const removerId = Number(document.body.dataset.memberId);
        if (!Number.isFinite(removerId) || removerId <= 0) { alert('removerId가 유효하지 않습니다.'); return; }
        const endpoint = (Model.state.boxType === 'SENT') ? '/api/message/deleteSenderMessages' : '/api/message/deleteMessages';
        showModal(`선택한 ${ids.length}건을 삭제하시겠습니까?`, async (result) => {
            if (result) {
                try {
                    await axios.delete(endpoint, {params: {removerId}, data: ids});
                    await afterDeleteRefresh(ids.length);
                } catch (e) {
                    console.error(e);
                    alert(`삭제 실패: ${e?.response?.status || e.message}`);
                }
            } else {
                return;
            }
        });
    }

    async function refresh() {
        await Model.fetchMessages();
        View.renderRows(Model.state.items);
        View.renderPager(Model.state, goPage);
    }

    function goPage(p) {
        const s = Model.state;
        if (p < 1) return;
        if (typeof s.total === 'number' && s.total >= 0) {
            const maxPage = Math.max(1, Math.ceil(s.total / s.limit));
            if (p > maxPage) return;
        }
        s.offset = (p - 1) * s.limit;
        refresh().catch(console.error);
    }

    function bindEvents() {
        document.getElementById('tabReceived')?.addEventListener('click', (ev) => {
            ev.preventDefault();
            const s = Model.state;
            if (s.boxType === 'RECEIVED') return;
            s.boxType = 'RECEIVED';
            s.offset = 0;
            document.getElementById('tabReceived')?.classList.add('fw-semibold');
            document.getElementById('tabSent')?.classList.remove('fw-semibold');
            refresh().catch(console.error);
        });
        document.getElementById('tabSent')?.addEventListener('click', (ev) => {
            ev.preventDefault();
            const s = Model.state;
            if (s.boxType === 'SENT') return;
            s.boxType = 'SENT';
            s.offset = 0;
            document.getElementById('tabSent')?.classList.add('fw-semibold');
            document.getElementById('tabReceived')?.classList.remove('fw-semibold');
            refresh().catch(console.error);
        });
        const form = document.getElementById('searchForm');
        const selTarget = document.getElementById('selectTargetType');
        const inputTitle = document.getElementById('inputTitle');
        form?.addEventListener('submit', (ev) => {
            ev.preventDefault();
            const s = Model.state;
            s.targetType = selTarget?.value || '';
            s.title = (inputTitle?.value || '').trim();
            s.offset = 0;
            refresh().catch(console.error);
        });
        document.getElementById('btnDelete')?.addEventListener('click', (e) => {
            e.preventDefault();
            deleteSelected();
        });
    }

    function init() {
        try {
            Model.state.memberId = Model.resolveMemberId();
        } catch (e) {
            console.error(e);
            alert(e.message);
            return;
        }
        bindEvents();
        refresh().catch(e => {
            console.error(e);
            View.renderRows([]);
            View.renderPager(Model.state, goPage);
            alert(`목록 요청 실패: ${e?.response?.status || e.message}`);
        });
    }

    document.addEventListener('DOMContentLoaded', init);
})();
