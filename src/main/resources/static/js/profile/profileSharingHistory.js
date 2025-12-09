(function () {

    const myId = document.body.dataset.memberId;

    const state = {
        apiBase: '/api/profileSharing',
        tab: 'MY',
        keyword: '',
        targetMemberId: '',
        targetMemberReviewFlag: '',
        status: '',
        offset: 0,
        limit: 10,
        total: 0,
        items: []
    };


    function normalizeResponse(items) {
        return {
            items,
            total: items[0]?.totalCount ?? 0
        };
    }

    function resetOffset() {
        state.offset = 0;
    }


    async function fetchSharing() {
        const s = state;
        const url = `${s.apiBase}/${s.tab === 'MY' ? 'my' : 'received'}`;

        const params = new URLSearchParams({
            keyword: s.keyword,
            status: s.status,
            offset: s.offset,
            limit: s.limit
        });

        const res = await axios.get(`${url}?${params.toString()}`);
        const { items, total } = normalizeResponse(res.data);

        s.items = items;
        s.total = total;
    }


    async function refresh() {
        await fetchSharing();
        renderCards(state.items);
        renderPager(goPage);
    }


    function goPage(p) {
        if (p < 1) return;
        const max = Math.ceil(state.total / state.limit);
        if (p > max) return;

        state.offset = (p - 1) * state.limit;
        refresh();
    }


    function activateTab(tab) {
        const myBtn = document.getElementById('myPostsLink');
        const receivedBtn = document.getElementById('receivedPostsLink');

        myBtn.classList.toggle('active', tab === 'MY');
        receivedBtn.classList.toggle('active', tab === 'RECEIVED');
    }


    function bindEvents() {

        document.getElementById('myPostsLink').addEventListener('click', () => {
            state.tab = 'MY';
            activateTab('MY');
            resetOffset();
            refresh();
        });

        document.getElementById('receivedPostsLink').addEventListener('click', () => {
            state.tab = 'RECEIVED';
            activateTab('RECEIVED');
            resetOffset();
            refresh();
        });

        document.getElementById('searchBtn').addEventListener('click', () => {
            state.keyword = document.getElementById("search").value.trim();
            resetOffset();
            refresh();
        });

        document.getElementById('search').addEventListener("keyup", (e) => {
            if (e.key === "Enter") {
                state.keyword = e.target.value.trim();
                resetOffset();
                refresh();
            }
        });

        document.getElementById('statusFilter').addEventListener('change', (e) => {
            state.status = e.target.value;
            resetOffset();
            refresh();
        });

        document.addEventListener("click", (e) => {
            const card = e.target.closest(".post-card");
            if (!card) return;

            if (e.target.closest(".review-badge")) {
                return;
            }

            const sharingId = card.dataset.id;
            window.location.href = `/readSharing/${sharingId}`;
        });

        document.addEventListener("click", (e) => {
            const reviewBtn = e.target.closest(".review-badge");
            if (!reviewBtn) return;

            e.stopPropagation();
            const sharingId = reviewBtn.dataset.id;
            window.location.href = `/sharing/${sharingId}/review`;
        });
    }


    function renderPager(goPage) {
        const ul = document.getElementById('pager');
        if (!ul) return;

        const { offset, limit, total } = state;
        const current = Math.floor(offset / limit) + 1;
        const totalPages = Math.max(1, Math.ceil(total / limit));

        function item(label, page, disabled, active, aria) {
            const li = document.createElement('li');
            li.className = `page-item${disabled ? ' disabled' : ''}${active ? ' active' : ''}`;

            const a = document.createElement('a');
            a.className = 'page-link';
            a.href = '#';
            if (aria) a.setAttribute('aria-label', aria);
            a.textContent = label;

            if (!disabled && !active) {
                a.addEventListener('click', e => {
                    e.preventDefault();
                    goPage(page);
                });
            } else {
                a.addEventListener('click', e => e.preventDefault());
            }

            li.appendChild(a);
            return li;
        }

        ul.innerHTML = '';

        ul.appendChild(item('«', 1, current === 1, false, '처음'));
        ul.appendChild(item('‹', current - 1, current === 1, false, '이전'));

        const windowSize = 5;
        const start = Math.floor((current - 1) / windowSize) * windowSize + 1;
        const end = Math.min(start + windowSize - 1, totalPages);

        for (let p = start; p <= end; p++) {
            ul.appendChild(item(p, p, false, p === current));
        }

        const isLast = current >= totalPages;
        ul.appendChild(item('›', current + 1, isLast, false, '다음'));
        ul.appendChild(item('»', totalPages, isLast, false, '마지막'));
    }


    function renderCards(posts) {
        const list = document.getElementById("post-list");
        if (!list) return;

        list.classList.remove("d-flex", "justify-content-center", "align-items-center");
        list.style.height = "";

        if (posts.length === 0) {
            list.classList.add("d-flex", "justify-content-center", "align-items-center");
            list.style.height = "250px";

            list.innerHTML = `
            <div class="text-center text-muted">
                <i class="bi bi-box fs-2"></i>
                <div class="mt-2">표시할 데이터가 없습니다.</div>
            </div>
        `;
            return;
        }

        list.innerHTML = posts.map(post => {
            let showReviewButton = false;

            if (state.tab === 'MY') {
                showReviewButton =
                    (post.status === "true" && post.reviewFlag == null && post.targetMemberId != null);

            } else if (state.tab === 'RECEIVED') {
                showReviewButton =
                    (post.status === "true" && post.targetMemberReviewFlag == null && String(post.targetMemberId) === String(myId));
            }

            return `
        <div class="col-auto">
            <div class="card post-card border-custom shadow-sm" data-id="${post.sharingId}">
                <div class="card-img-section">
                    <img src="${post.thumbnail || '/image/default.png'}" class="card-img-top card-img">
                    <span class="bg-dark badge badge-status position-absolute top-0 start-0 m-2">
                        ${post.status === "false" ? "나눔중" : "나눔완료"}
                    </span>
                </div>

                <div class="card-body">
                    <h6 class="fw-bold text-truncate mb-2">${post.title}</h6>
                    <p class="text-muted small mb-2">
                        <i class="bi bi-clock"></i> ${formatTime(post.createdAt)}
                    </p>

                    <div class="d-flex justify-content-between small mb-3">
                        <span class="text-secondary">
                            <i class="bi bi-chat-dots"></i> ${post.commentCount}
                        </span>
                        <span class="text-danger">
                            <i class="bi bi-heart-fill"></i> ${post.interestNum}
                        </span>
                    </div>

                    ${ showReviewButton ? `
                        <div class="text-end">
                            <button class="btn btn-sm btn-outline-success review-badge"
                                    data-id="${post.sharingId}">
                                후기 작성
                            </button>
                        </div>
                    ` : ""}

                </div>
            </div>
        </div>
    `;
        }).join("");


    }


    function formatTime(dateString) {
        return dateString.replace("T", " ").substring(0, 16);
    }


    document.addEventListener("DOMContentLoaded", () => {
        activateTab('MY');
        bindEvents();
        refresh();
    });

})();
