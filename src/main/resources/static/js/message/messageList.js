(function () {
    const state = {
        apiBase: window.__API_BASE__ || '/api/message',
        memberId: null,
        boxType: 'RECEIVED',
        targetType: '',
        title: '',
        offset: 0,
        limit: 10,
        total: null,
        items: []
    };

    function resolveMemberId() {
        const memberId = Number(document.body.dataset.memberId);
        if (!Number.isFinite(memberId) || memberId <= 0) {
            throw new Error('유효한 memberId가 없습니다. <body data-member-id="...">를 주입하세요.');
        }
        return memberId;
    }

    function normalizeResponse(data, headers) {
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
    }

    async function fetchMessages() {
        const s = state;
        const url = `${s.apiBase}/${s.memberId}/${s.boxType}`;
        const params = new URLSearchParams({ offset: String(s.offset), limit: String(s.limit) });
        if (s.targetType) params.set('targetType', s.targetType);
        if (s.title) params.set('title', s.title);
        const res = await axios.get(`${url}?${params.toString()}`);
        const { items, total } = normalizeResponse(res.data, res.headers);
        s.items = items;
        s.total = total;
    }

    // ---------- DOM helpers ----------
    function fmtKST(iso) {
        if (!iso) return '';
        return new Intl.DateTimeFormat('ko-KR', {
            timeZone: 'Asia/Seoul',
            year: 'numeric', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit', second: '2-digit',
            hour12: false
        }).format(new Date(iso)).replace(/\./g, '-').replace(/-\s/g, '-').replace(/\s/g, ' ');
    }

    function labelTargetType(tt) {
        return ({ QUESTION: '질문', SHARING: '나눔' }[tt] || '');
    }

    function esc(s) {
        return String(s ?? '')
            .replaceAll('&', '&amp;')
            .replaceAll('<', '&lt;')
            .replaceAll('>', '&gt;')
            .replaceAll('"', '&quot;')
            .replaceAll("'", '&#39;');
    }

    function buildTargetUrl(type, id) {
        if (!type || !id) return '';
        if (type === 'SHARING') return `/sharing/${id}`;
        if (type === 'QUESTION') return `/question/${id}`;
        return '';
    }

    function renderRows(list) {
        const tbody = document.getElementById('messageTbody');
        if (!tbody) return;

        if (!Array.isArray(list) || list.length === 0) {
            tbody.innerHTML = `<tr><td class="text-center" colspan="8" style="height:120px">데이터가 없습니다.</td></tr>`;
            const checkAll = document.getElementById('checkAll');
            if (checkAll) { checkAll.checked = false; checkAll.indeterminate = false; }
            return;
        }

        tbody.innerHTML = list.map(item => {
            const isUnread = !item.readFlag;
            const rowClass = `cursor-pointer${isUnread ? ' fw-semibold' : ''}`;
            const readText = item.readFlag ? '읽음' : '안읽음';
            const category = labelTargetType(item.targetType);
            const relatedUrl = buildTargetUrl(item.targetType, item.targetId);
            const relatedText = esc(item.targetTitle || '(삭제된 쪽지)');
            return `
        <tr data-id="${item.messageId}" class="${rowClass}">
          <td class="text-center" style="width:44px;">
            <input type="checkbox" class="form-check-input row-check" value="${item.messageId}">
          </td>
          <td style="width:96px;">${readText}</td>
          <td style="width:120px;">${esc(item.senderNickname ?? '')}</td>
          <td style="width:120px;">${esc(item.receiverNickname ?? '')}</td>
          <td style="width:90px;">${category}</td>
          <td class="text-truncate" style="max-width:180px;">${esc(item.title || '')}</td>
          <td class="text-truncate">${relatedText}</td>
          <td class="text-nowrap" style="width:170px;">${fmtKST(item.createdAt)}</td>
        </tr>
      `;
        }).join('');

        wireCheckEvents();
    }

    function renderPager(goPage) {
        const ul = document.getElementById('pager');
        if (!ul) return;

        const { offset, limit, total, items } = state;
        const current = Math.floor(offset / limit) + 1;
        const totalPages =
            (typeof total === 'number' && total >= 0)
                ? Math.max(1, Math.ceil(total / limit))
                : null; // total을 모르면 null

        const makeItem = (label, onClick, { disabled = false, active = false, aria = null } = {}) => {
            const li = document.createElement('li');
            li.className = `page-item${disabled ? ' disabled' : ''}${active ? ' active' : ''}`;
            const a = document.createElement('a');
            a.className = 'page-link';
            a.href = '#';
            if (aria) a.setAttribute('aria-label', aria);
            a.textContent = label;
            if (!disabled && !active && typeof onClick === 'function') {
                a.addEventListener('click', (ev) => { ev.preventDefault(); onClick(); });
            } else {
                a.addEventListener('click', (ev) => ev.preventDefault());
            }
            li.appendChild(a);
            return li;
        };

        ul.innerHTML = '';

        ul.appendChild(makeItem('«', () => goPage(1), { disabled: current === 1, aria: '처음' }));
        ul.appendChild(makeItem('‹', () => goPage(current - 1), { disabled: current === 1, aria: '이전' }));

        const windowSize = 5;
        const blockStart = Math.floor((current - 1) / windowSize) * windowSize + 1; // 1,6,11...
        const blockEnd = Math.min(blockStart + windowSize - 1, totalPages);        // 5,10,15...

        for (let p = blockStart; p <= blockEnd; p++) {
            ul.appendChild(makeItem(String(p), () => goPage(p), { active: p === current }));
        }

        const isLast = current >= totalPages;
        ul.appendChild(makeItem('›', () => goPage(current + 1), { disabled: isLast, aria: '다음' }));
        ul.appendChild(makeItem('»', () => goPage(totalPages), { disabled: isLast, aria: '마지막' }));
    }


    // ---------- selection & actions ----------
    function wireCheckEvents() {
        const checkAll = document.getElementById('checkAll');
        const rowChecks = Array.from(document.querySelectorAll('.row-check'));
        if (!checkAll) return;

        checkAll.checked = false;
        checkAll.indeterminate = false;

        checkAll.onchange = () => {
            rowChecks.forEach(chk => { chk.checked = checkAll.checked; });
        };

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
        if (typeof state.total === 'number' && state.total >= 0) {
            const remaining = state.total - deletedCount;
            const maxPage = Math.max(1, Math.ceil(Math.max(0, remaining) / state.limit));
            const currentPage = Math.floor(state.offset / state.limit) + 1;
            if (currentPage > maxPage) state.offset = (maxPage - 1) * state.limit;
        }
        await fetchMessages();
        renderRows(state.items);
        renderPager(goPage);
    }

    async function deleteSelected() {
        const ids = getSelectedMessageIds();
        if (ids.length === 0) { showAlert('삭제할 쪽지를 선택하세요.'); return; }
        const endpoint = (state.boxType === 'SENT') ? '/api/message/deleteSenderMessages' : '/api/message/deleteMessages';

        showModal(`선택한 ${ids.length}건을 삭제하시겠습니까?`, async (result) => {
            if (!result) return;
            try {
                await axios.delete(endpoint, { data: ids });
                await afterDeleteRefresh(ids.length);
            } catch (e) {
                console.error(e);
                showAlert(`삭제 실패: ${e?.response?.status || e.message}`);
            }
        });
    }

    // ---------- lifecycle & bindings ----------
    async function refresh() {
        await fetchMessages();
        renderRows(state.items);
        renderPager(goPage);
    }

    function goPage(p) {
        if (p < 1) return;
        if (typeof state.total === 'number' && state.total >= 0) {
            const maxPage = Math.max(1, Math.ceil(state.total / state.limit));
            if (p > maxPage) return;
        }
        state.offset = (p - 1) * state.limit;
        refresh().catch(console.error);
    }

    function bindEvents() {
        document.getElementById('tabReceived')?.addEventListener('click', (ev) => {
            ev.preventDefault();
            if (state.boxType === 'RECEIVED') return;
            state.boxType = 'RECEIVED';
            state.offset = 0;
            document.getElementById('tabReceived')?.classList.add('fw-semibold');
            document.getElementById('tabSent')?.classList.remove('fw-semibold');
            refresh().catch(console.error);
        });

        document.getElementById('tabSent')?.addEventListener('click', (ev) => {
            ev.preventDefault();
            if (state.boxType === 'SENT') return;
            state.boxType = 'SENT';
            state.offset = 0;
            document.getElementById('tabSent')?.classList.add('fw-semibold');
            document.getElementById('tabReceived')?.classList.remove('fw-semibold');
            refresh().catch(console.error);
        });

        const form = document.getElementById('searchForm');
        const selTarget = document.getElementById('selectTargetType');
        const inputTitle = document.getElementById('inputTitle');

        form?.addEventListener('submit', (ev) => {
            ev.preventDefault();
            state.targetType = selTarget?.value || '';
            state.title = (inputTitle?.value || '').trim();
            state.offset = 0;
            refresh().catch(console.error);
        });

        document.getElementById('btnDelete')?.addEventListener('click', (e) => {
            e.preventDefault();
            deleteSelected();
        });
    }

    function bindRowClick() {
        const tbody = document.getElementById('messageTbody');
        if (!tbody) return;

        function onRowClick(messageId) {
            const viewerId = state.memberId;
            window.location.href = `/messageDetail?messageId=${encodeURIComponent(messageId)}&viewerId=${encodeURIComponent(viewerId)}`;
        }


        tbody.addEventListener('click', (ev) => {
            if (ev.target.closest('input[type="checkbox"]')) return;
            if (ev.target.closest('a')) return;
            const tr = ev.target.closest('tr[data-id]');
            if (!tr) return;
            const messageId = Number(tr.dataset.id);
            if (!Number.isFinite(messageId)) return;
            onRowClick(messageId);
        });
    }

    function init() {
        try {
            state.memberId = resolveMemberId();
        } catch (e) {
            console.error(e);
            showAlert(e.message);
            return;
        }
        bindEvents();
        bindRowClick();
        refresh().catch(e => {
            console.error(e);
            renderRows([]);
            renderPager(goPage);
            showAlert(`목록 요청 실패: ${e?.response?.status || e.message}`);
        });
    }

    document.addEventListener('DOMContentLoaded', init);
})();