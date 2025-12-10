import { createPaginator } from '/js/common/pagination.js';

document.addEventListener('DOMContentLoaded', () => {
    let pageNo = 1;
    const numOfRows = 10;

    const api = axios.create({ baseURL: '/' });

    const state = { q: '', mode: 'server' };
    let allCache = [];

    let currentController = null;
    const CONCURRENCY = 10;

    const pager = createPaginator({
        container: document.getElementById('dryPlantDictionary-pagination'),
        current: pageNo,
        pageSize: numOfRows,
        onChange: (nextPage) => {
            pageNo = nextPage;
            if (state.mode === 'server') {
                loadList(pageNo, numOfRows);
            } else {
                const q = normalize(state.q);
                renderClientPage(allCache.filter(byQuery(q)));
            }
        },
    });

    loadList(pageNo, numOfRows);

    const $q = document.getElementById('qName');
    const $btnSearch = document.getElementById('btnSearch');
    $btnSearch && $btnSearch.addEventListener('click', onSearch);
    $q && $q.addEventListener('keydown', (e) => { if (e.key === 'Enter') onSearch(); });

    document.querySelector('button.btn.btn-secondary')?.addEventListener('click', () => {
        if ($q) $q.value = '';
        state.q = ''; state.mode = 'server'; pageNo = 1;
        loadList(pageNo, numOfRows);
    });

    async function loadList(p, rows) {
        try {
            const res = await api.get('/api/dictionary/dry', {
                params: { pageNo: String(p), numOfRows: String(rows) },
            });
            const body       = res.data?.body;
            const items      = body?.items?.item || [];
            const totalCount = Number(body?.items?.totalCount || 0);
            const current    = Number(body?.items?.pageNo || p);
            const size       = Number(body?.items?.numOfRows || rows);

            renderList(asArray(items));

            pager.update({
                current: current,
                pageSize: size,
                totalPages: Math.max(1, Math.ceil(totalCount / size)),
            });
        } catch (e) {
            console.error(e);
            renderList([]);
            pager.update({ current: 1, pageSize: numOfRows, totalPages: 1 });
        }
    }

    async function onSearch() {
        state.q = ($q?.value || '').trim();
        pageNo = 1;

        if (currentController) currentController.abort();
        currentController = new AbortController();

        if (!state.q) {
            state.mode = 'server';
            return loadList(pageNo, numOfRows);
        }

        state.mode = 'client';

        pager.update({ current: 1, pageSize: numOfRows, totalItems: 0, totalPages: null });

        const { totalPages, size, firstItems } = await collectFirstPage({ signal: currentController.signal });
        const q = normalize(state.q);

        const base = firstItems.map(withSearchKey);
        allCache = base;

        const filtered = base.filter(byQuery(q));
        renderClientPage(filtered);

        collectRestPages({
            totalPages,
            size,
            q,
            signal: currentController.signal,
            onBatch: (batch) => {
                allCache.push(...batch);
                if (state.mode === 'client') {
                    renderClientPage(allCache.filter(byQuery(q)));
                }
            },
            shouldStop: () => {
                const need = pageNo * numOfRows;
                return allCache.filter(byQuery(q)).length >= need + numOfRows;
            }
        }).catch((err) => {
            if (err.name !== 'AbortError') console.error(err);
        });
    }

    function withSearchKey(it) {
        const cntntsSj = normalize(it?.cntntsSj);
        return { ...it, _searchKey: `${cntntsSj}` };
    }
    function byQuery(q) { return (it) => it._searchKey?.includes(q); }

    function renderClientPage(filtered) {
        const totalCount = filtered.length;
        const size = numOfRows;
        const totalPages = Math.max(1, Math.ceil(totalCount / size));
        const current = Math.min(Math.max(1, pageNo), totalPages);
        const from = (current - 1) * size;
        const to = Math.min(from + size, totalCount);

        renderList(filtered.slice(from, to));

        pager.update({
            current,
            pageSize: size,
            totalItems: totalCount,
            totalPages: null,
        });
    }

    async function collectFirstPage({ signal }) {
        const res = await api.get('/api/dictionary/dry', {
            params: { pageNo: '1', numOfRows: String(numOfRows) },
            signal,
        });
        const body = res.data?.body;
        const firstItems = asArray(body?.items?.item || []);
        const totalCount = Number(body?.items?.totalCount || 0);
        const size = Number(body?.items?.numOfRows || numOfRows);
        const totalPages = Math.max(1, Math.ceil(totalCount / size));
        return { totalPages, size, firstItems };
    }

    async function collectRestPages({ totalPages, size, q, signal, onBatch, shouldStop }) {
        const pages = [];
        for (let p = 2; p <= totalPages; p++) pages.push(p);

        let idx = 0;
        async function worker() {
            while (idx < pages.length && !shouldStop()) {
                const p = pages[idx++];
                const res = await api.get('/api/dictionary/dry', {
                    params: { pageNo: String(p), numOfRows: String(size) },
                    signal,
                });
                const batch = asArray(res.data?.body?.items?.item || []).map(withSearchKey);
                onBatch?.(batch);
            }
        }
        const workers = Array.from({ length: CONCURRENCY }, () => worker());
        await Promise.allSettled(workers);
    }

    function renderList(items) {
        const container = document.getElementById('resultList');
        if (!container) return;
        if (!items.length) {
            container.innerHTML = '<div class="text-center py-5 text-muted"><i class="bi bi-box"></i> 검색 결과가 없습니다.</div>';
            return;
        }
        container.innerHTML = items.map(toCardHtml).join('');
    }

    function toCardHtml(item) {
        const title    = sanitize(item?.cntntsSj || '');

        const thumb = firstTruthy(item?.thumbImgUrl1, item?.thumbImgUrl2, item?.imgUrl1, item?.imgUrl2) || '';
        const full  = firstTruthy(item?.imgUrl1, item?.imgUrl2, item?.thumbImgUrl1, item?.thumbImgUrl2) || '';
        const hasImg = !!thumb;

        const cntntsNoRaw = String(item?.cntntsNo ?? '').trim();
        const detailUrl = `/readDryDictionary?cntntsNo=${encodeURIComponent(cntntsNoRaw)}`;

        return `
      <div class="row mb-3 p-3 bg-white border rounded align-items-center">
        <div class="col-auto pe-3">
          <div class="border rounded overflow-hidden" style="width:120px;height:90px;">
            ${
            hasImg
                ? `<a href="${full}" target="_blank" rel="noopener">
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

    function asArray(maybeArray) {
        if (Array.isArray(maybeArray)) return maybeArray;
        if (maybeArray == null) return [];
        return [maybeArray];
    }
    function firstTruthy(...args) {
        for (const x of args) {
            if (x != null && String(x).trim() !== '') return String(x).trim();
        }
        return '';
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
    function stripHtml(s) {
        if (s == null) return '';
        return String(s).replace(/<[^>]*>/g, '');
    }
    function normalize(s) {
        return stripHtml(s).toLowerCase().trim();
    }
});