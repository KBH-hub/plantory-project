const myPostsLink = document.getElementById('myPostsLink');
const receivedPostsLink = document.getElementById('receivedPostsLink');

const limit = 10;
const offset = 0;
let currentPage = 1;

document.addEventListener("DOMContentLoaded", () => {
    loadList("MY");
});

async function loadList(myType) {
    const keyword = document.getElementById("search").value.trim();
    const status = document.getElementById("statusFilter").value;

    const res = await axios.get(`/api/profileSharing/${myType === "MY" ? "my" : "received"}`, {
        params: {
            keyword: keyword,
            status: status,
            limit: limit,
            offset: (currentPage - 1) * limit
        }
    });

    console.res

    const totalCount = res.data.totalCount;
    const pageData = res.data.list;

    renderCards(pageData);
    renderPagination(totalCount);
}



document.getElementById("searchBtn").addEventListener("click", () => {
    currentPage = 1;
    applyCurrentTabLoad();
});
document.getElementById("statusFilter").addEventListener("change", () => {
    currentPage = 1;
    applyCurrentTabLoad();
});
document.getElementById("search").addEventListener("keyup", (e) => {
    if (e.key === "Enter") {
        currentPage = 1;
        applyCurrentTabLoad();
    }
});

function applyCurrentTabLoad() {
    if (myPostsLink.classList.contains("active")) {
        loadList("MY");
    } else {
        loadList("RECEIVED");
    }
}

myPostsLink.addEventListener('click', () => {
    myPostsLink.classList.add('active');
    receivedPostsLink.classList.remove('active');
    currentPage = 1;
    loadList("MY");
});

receivedPostsLink.addEventListener('click', () => {
    receivedPostsLink.classList.add('active');
    myPostsLink.classList.remove('active');
    currentPage = 1;
    loadList("RECEIVED");
});

function renderCards(posts) {
    const list = document.getElementById("post-list");
    list.innerHTML = posts.map(post => `

        <div class="col-auto">
            <div class="card post-card border-custom shadow-sm" data-id="${post.sharingId}">

                <div class="card-img-section">
            <img
            src="${post.thumbnail ? post.thumbnail : '/image/default.png'}"
            class="card-img-top card-img">


                    
                    <span class="badge badge-status position-absolute top-0 start-0 m-2">
                        ${post.status === "false" ? "나눔중" : "나눔완료"}
                    </span>
                </div>

                <div class="card-body text-section">
                    <h6 class="fw-bold text-truncate mb-2">${post.title}</h6>
                    <p class="text-muted small mb-2"><i class="bi bi-clock"></i> ${formatTime(post.createdAt)}</p>

                    <div class="d-flex justify-content-between small mb-3">
                        <span class="text-secondary"><i class="bi bi-chat-dots"></i> ${post.commentCount}</span>
                        <span class="text-danger"><i class="bi bi-heart-fill"></i> ${post.interestNum}</span>
                    </div>

                    ${post.reviewFlag ? `
                        <div class="mt-auto text-end">
                            <button class="btn btn-sm btn-outline-success review-badge" data-id="${post.sharingId}">
                                후기 작성
                            </button>
                        </div>
                    ` : ""}
                </div>

            </div>
        </div>

    `).join("");

}

document.addEventListener("click", e => {
    if (e.target.closest(".review-badge")) {
        e.stopPropagation();
    }
});


function formatTime(dateString) {
    return dateString.replace("T", " ").substring(0, 16);
}

function renderPagination(total) {
    const pagination = document.getElementById("pagination");
    const totalPages = Math.ceil(total / limit);

    pagination.innerHTML = `
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
            <a class="page-link" data-page="1">&laquo;</a>
        </li>
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
            <a class="page-link" data-page="${currentPage - 1}">&lsaquo;</a>
        </li>
    `;

    for (let i = 1; i <= totalPages; i++) {
        pagination.innerHTML += `
            <li class="page-item ${currentPage === i ? "active" : ""}">
                <a class="page-link" data-page="${i}">${i}</a>
            </li>
        `;
    }

    pagination.innerHTML += `
        <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
            <a class="page-link" data-page="${currentPage + 1}">&rsaquo;</a>
        </li>
        <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
            <a class="page-link" data-page="${totalPages}">&raquo;</a>
        </li>
    `;

    bindPaginationClick();
}


function bindPaginationClick() {
    document.querySelectorAll("#pagination .page-link").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();

            const page = e.currentTarget.dataset.page;

            if (!page || Number(page) === currentPage) return;

            currentPage = Number(page);
            applyCurrentTabLoad();
        });
    });
}

document.addEventListener("click", (e) => {
    const card = e.target.closest(".post-card");

    if (e.target.closest(".review-badge")) {
        return;
    }

    if (card) {
        const sharingId = card.dataset.id;
        window.location.href = `/readSharing/${sharingId}`;
    }
});

document.addEventListener("click", (e) => {
    const reviewBtn = e.target.closest(".review-badge");
    if (reviewBtn) {
        e.stopPropagation();
        const sharingId = reviewBtn.dataset.id;
        window.location.href = `/sharing/${sharingId}/review`;
        return;
    }
});
