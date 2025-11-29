document.addEventListener('DOMContentLoaded', () => {
    let pageNo = 1;
    const numOfRows = 10;

    // 최초 로딩
    loadList(pageNo, numOfRows);

    // 검색 버튼 클릭시(필요 시 필터값 수집 후 전달)
    document.querySelector('button.btn.btn-secondary')?.addEventListener('click', () => {
        pageNo = 1;
        loadList(pageNo, numOfRows);
    });

    async function loadList(pageNo, numOfRows) {
        const url = new URL('/api/dictionary/list', window.location.origin);
        url.searchParams.set('pageNo', String(pageNo));
        url.searchParams.set('numOfRows', String(numOfRows));
        // 예: 채광(lightCode) 같은 필터를 지원한다면 여기서 추가
        // url.searchParams.set('lightCode', selectedLightCode || '');

        const res = await fetch(url, { headers: { 'Accept': 'application/json' }});
        if (!res.ok) {
            console.error('API error', res.status, await res.text());
            renderError('데이터를 불러오지 못했습니다.');
            return;
        }
        const data = await res.json();
        const body = data?.body;
        const items = body?.items?.item || [];
        const totalCount = Number(body?.items?.totalCount || 0);
        const current = Number(body?.items?.pageNo || pageNo);
        const rows = Number(body?.items?.numOfRows || numOfRows);

        renderList(items);
        renderPaging(current, rows, totalCount);
        attachPagingHandlers(rows, totalCount);
    }

    function renderList(items) {
        const container = document.getElementById('resultList');
        if (!container) return;

        if (!items.length) {
            container.innerHTML = `<div class="text-center py-5 text-muted">검색 결과가 없습니다.</div>`;
            return;
        }

        container.innerHTML = items.map(toCardHtml).join('');
    }

    function toCardHtml(item) {
        // pipe 필드를 배열로
        const thumbUrls = splitPipes(item.rtnThumbFileUrl);
        const imgCodes  = splitPipes(item.rtnImgSeCode);
        const imgUrls   = splitPipes(item.rtnFileUrl);

        // 대표 이미지 인덱스: 코드 209006(대표) 우선, 없으면 0
        let idx = imgCodes.findIndex(c => c === '209006');
        if (idx < 0) idx = 0;

        const thumb = thumbUrls[idx] || thumbUrls[0] || 'https://via.placeholder.com/150';
        const full  = imgUrls[idx]   || imgUrls[0]   || thumb;

        const title = sanitize(item.cntntsSj);
        const id    = sanitize(item.cntntsNo);

        return `
        <div class="row mb-4 p-3 bg-white border rounded">
          <div class="col-3">
            <a href="${full}" target="_blank" rel="noopener">
              <img src="${thumb}" class="img-fluid border" alt="${title}">
            </a>
          </div>
          <div class="col-9 d-flex flex-column justify-content-center">
            <a href="/plants/${id}" class="fw-bold fs-5 text-dark text-decoration-none">
              ${title}
            </a>
            <!-- 필요하면 부가 정보 필드 추가 표시 -->
          </div>
        </div>
      `;
    }

    function renderPaging(pageNo, numOfRows, totalCount) {
        const totalPages = Math.max(1, Math.ceil(totalCount / numOfRows));
        const ul = document.getElementById('paging');
        if (!ul) return;

        const max = totalPages;
        const curr = pageNo;

        // 부트스트랩 기본 형태
        const pages = [];
        pages.push(pageItem('이전', Math.max(1, curr - 1), curr === 1, 'prev'));
        // 간단히 1~N 전부, 필요 시 구간 페이지로 개선
        for (let p = 1; p <= max; p++) {
            pages.push(pageItem(String(p), p, false, 'page', p === curr));
        }
        pages.push(pageItem('다음', Math.min(max, curr + 1), curr === max, 'next'));

        ul.innerHTML = pages.join('');
    }

    function pageItem(label, go, disabled, role, active = false) {
        const disabledCls = disabled ? ' disabled' : '';
        const activeCls = active ? ' active' : '';
        return `
        <li class="page-item${disabledCls}${activeCls}">
          <a class="page-link" href="#" data-page="${go}" data-role="${role}">${label}</a>
        </li>
      `;
    }

    function attachPagingHandlers(numOfRows, totalCount) {
        const ul = document.getElementById('paging');
        if (!ul) return;
        ul.querySelectorAll('a.page-link').forEach(a => {
            a.addEventListener('click', e => {
                e.preventDefault();
                const p = Number(a.dataset.page || '1');
                loadList(p, numOfRows);
            });
        });
    }

    function splitPipes(s) {
        if (!s || typeof s !== 'string') return [];
        return s.split('|').map(x => x.trim()).filter(Boolean);
    }

    function sanitize(s) {
        s = s == null ? '' : String(s);
        return s.replaceAll('&','&amp;')
            .replaceAll('<','&lt;')
            .replaceAll('>','&gt;')
            .replaceAll('"','&quot;')
            .replaceAll("'",'&#39;');
    }

    function renderError(msg) {
        const container = document.getElementById('resultList');
        if (container) container.innerHTML = `<div class="alert alert-danger">${sanitize(msg)}</div>`;
    }
});