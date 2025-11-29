document.addEventListener('DOMContentLoaded', () => {
    let pageNo = 1;
    const numOfRows = 10;

    const api = axios.create({ baseURL: '/' });

    const state = { q: '', mode: 'server' };
    let allCache = [];

    // 초기 렌더
    loadList(pageNo, numOfRows);

    // 검색 UI
    const $q = document.getElementById('qName');
    const $btnSearch = document.getElementById('btnSearch');
    $btnSearch && $btnSearch.addEventListener('click', onSearch);
    $q && $q.addEventListener('keydown', (e) => { if (e.key === 'Enter') onSearch(); });

    // 초기화(전체보기)
    document.querySelector('button.btn.btn-secondary')?.addEventListener('click', () => {
        if ($q) $q.value = '';
        state.q = ''; state.mode = 'server'; pageNo = 1;
        loadList(pageNo, numOfRows);
    });

    // 서버 페이징 목록
    async function loadList(p, rows) {
        try {
            const res = await api.get('/api/dictionary/garden', {
                params: { pageNo: String(p), numOfRows: String(rows) },
            });
            const body       = res.data?.body;
            const items      = body?.items?.item || [];
            const totalCount = Number(body?.items?.totalCount || 0);
            const current    = Number(body?.items?.pageNo || p);
            const size       = Number(body?.items?.numOfRows || rows);

            renderList(asArray(items));
            renderPager({
                current,
                totalPages: Math.max(1, Math.ceil(totalCount / size)),
                onMove: (next) => { pageNo = next; loadList(pageNo, size); },
            });
        } catch (e) {
            console.error(e);
            renderList([]);
            renderPager({ current: 1, totalPages: 1, onMove: () => {} });
        }
    }

    // 검색(프론트 필터)
    async function onSearch() {
        state.q = ($q?.value || '').trim();
        pageNo = 1;
        if (!state.q) {
            state.mode = 'server';
            return loadList(pageNo, numOfRows);
        }
        state.mode = 'client';
        await collectAll(); // 단순 전페이지 수집
        renderClientSearch();
    }

    async function collectAll() {
        allCache = [];
        // 1페이지로 전체 페이지 계산
        const first = await api.get('/api/dictionary/garden', {
            params: { pageNo: '1', numOfRows: String(numOfRows) },
        });
        const firstBody  = first.data?.body;
        const firstItems = asArray(firstBody?.items?.item || []);
        const totalCount = Number(firstBody?.items?.totalCount || 0);
        const size       = Number(firstBody?.items?.numOfRows || numOfRows);
        const totalPages = Math.max(1, Math.ceil(totalCount / size));
        allCache.push(...firstItems);

        // 나머지 페이지 수집(상한 없음: 필요하면 제한 추가)
        for (let p = 2; p <= totalPages; p++) {
            const res = await api.get('/api/dictionary/garden', {
                params: { pageNo: String(p), numOfRows: String(size) },
            });
            const pageItems = asArray(res.data?.body?.items?.item || []);
            allCache.push(...pageItems);
        }
    }

    function renderClientSearch() {
        const q = state.q.toLowerCase();
        const filtered = allCache.filter((it) => {
            const a = String(it?.cntntsSj || '').toLowerCase();
            const b = String(it?.distbNm  || '').toLowerCase();
            return a.includes(q) || b.includes(q);
        });

        const totalCount = filtered.length;
        const size = numOfRows;
        const totalPages = Math.max(1, Math.ceil(totalCount / size));
        const current = Math.min(Math.max(1, pageNo), totalPages);
        const from = (current - 1) * size;
        const to   = Math.min(from + size, totalCount);

        renderList(filtered.slice(from, to));
        renderPager({
            current,
            totalPages,
            onMove: (next) => { pageNo = next; renderClientSearch(); },
        });
    }

    // 렌더
    function renderList(items) {
        const container = document.getElementById('resultList');
        if (!container) return;
        if (!items.length) {
            container.innerHTML = '<div class="text-center py-5 text-muted">검색 결과가 없습니다.</div>';
            return;
        }
        container.innerHTML = items.map(toCardHtml).join('');
    }

    function toCardHtml(item) {
        const title  = sanitize(item.cntntsSj || item.distbNm);
        const thumbs = splitPipes(item.rtnThumbFileUrl);
        const files  = splitPipes(item.rtnFileUrl);

        const thumb = item.thumbUrl || thumbs[0] || '';
        const full  = item.imageUrl || files[0] || '';
        const hasImg = !!thumb;

        const cntntsNoRaw = String(item.cntntsNo ?? '').trim();
        const detailUrl = `/readDictionary?cntntsNo=${encodeURIComponent(cntntsNoRaw)}`;

        return `
      <div class="row mb-3 p-3 bg-white border rounded align-items-center">
        <div class="col-auto pe-3">
          <div class="border rounded overflow-hidden" style="width:120px;height:90px;">
            ${
            hasImg
                ? `<a href="${full || thumb}" target="_blank">
                     <img src="${thumb}" alt="${title}" loading="lazy" style="width:100%;height:100%;object-fit:cover;">
                   </a>`
                : `<div class="bg-light d-flex align-items-center justify-content-center text-secondary" style="width:100%;height:100%;">🖼</div>`
        }
          </div>
        </div>
        <div class="col">
          <a href="${detailUrl}" class="fw-bold text-dark text-decoration-none">${title}</a>
        </div>
      </div>
    `;
    }

    // 단일 페이저
    function renderPager({ current, totalPages, onMove }) {
        const ul = document.getElementById('paging');
        if (!ul) return;

        const makeItem = (label, target, { disabled = false, active = false } = {}) => {
            const li = document.createElement('li');
            li.className = `page-item${disabled ? ' disabled' : ''}${active ? ' active' : ''}`;
            const a = document.createElement('a');
            a.className = 'page-link';
            a.href = '#';
            a.textContent = label;
            a.addEventListener('click', (ev) => { ev.preventDefault(); if (!disabled && !active) onMove(target); });
            li.appendChild(a);
            return li;
        };

        const win = 5;
        const blockStart = Math.floor((current - 1) / win) * win + 1;
        const blockEnd   = Math.min(blockStart + win - 1, totalPages);

        ul.innerHTML = '';
        ul.appendChild(makeItem('«', 1,           { disabled: current === 1 }));
        ul.appendChild(makeItem('‹', current - 1, { disabled: current === 1 }));
        for (let p = blockStart; p <= blockEnd; p++) {
            ul.appendChild(makeItem(String(p), p,   { active: p === current }));
        }
        const isLast = current >= totalPages;
        ul.appendChild(makeItem('›', current + 1, { disabled: isLast }));
        ul.appendChild(makeItem('»', totalPages,  { disabled: isLast }));
    }

    // 유틸
    function splitPipes(s) {
        return !s || typeof s !== 'string' ? [] : s.split('|').map((x) => x.trim()).filter(Boolean);
    }
    function sanitize(s) {
        s = s == null ? '' : String(s);
        return s
            .replaceAll('&', '&amp;')
            .replaceAll('<', '&lt;')
            .replaceAll('>', '&gt;')
            .replaceAll('"', '&quot;')
            .replaceAll("'", '&#39;');
    }
    function asArray(maybeArray) {
        if (Array.isArray(maybeArray)) return maybeArray;
        if (maybeArray == null) return [];
        return [maybeArray];
    }
});
