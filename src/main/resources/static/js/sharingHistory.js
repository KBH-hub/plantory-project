const myPostsLink = document.getElementById('myPostsLink');
const receivedPostsLink = document.getElementById('receivedPostsLink');

const dummyData = [
    { id: 1, title: "이사가서 나눔해요", status: "나눔중", img: "https://picsum.photos/id/101/300/250", time: "3시간 전", comment: 0, like: 3, reviewable: false },
    { id: 2, title: "이쁜 식물 가져가세요", status: "나눔중", img: "https://picsum.photos/id/102/300/250", time: "6시간 전", comment: 2, like: 5, reviewable: true },
    { id: 3, title: "이쁜 몬스테라 나눔합니다", status: "나눔완료", img: "https://picsum.photos/id/103/300/250", time: "3일 전", comment: 2, like: 4, reviewable: true },
    { id: 4, title: "나쁜 방울토마토 나눔", status: "나눔완료", img: "https://picsum.photos/id/104/300/250", time: "10일 전", comment: 2, like: 4, reviewable: true },
    { id: 5, title: "양상추팝니다", status: "나눔완료", img: "https://picsum.photos/id/105/300/250", time: "31일 전", comment: 2, like: 4, reviewable: true },
];

let filteredData = [...dummyData];
let currentPage = 1;
const limit = 3;

document.addEventListener("DOMContentLoaded", () => loadPage(1));

document.getElementById("searchBtn").addEventListener("click", filterData);
document.getElementById("statusFilter").addEventListener("change", filterData);
document.getElementById("search").addEventListener("keyup", e => { if (e.key === "Enter") filterData(); });

myPostsLink.addEventListener('click', () => {
    myPostsLink.classList.add('active');
    receivedPostsLink.classList.remove('active');

    filteredData = [
        { id: 101, title: "내가 올린 글 1", status: "나눔중", img: "https://picsum.photos/id/201/300/250", time: "1시간 전", comment: 0, like: 0, reviewable: false },
        { id: 102, title: "내가 올린 글 2", status: "나눔중", img: "https://picsum.photos/id/202/300/250", time: "2시간 전", comment: 1, like: 2, reviewable: true },
        { id: 103, title: "내가 올린 글 3", status: "나눔완료", img: "https://picsum.photos/id/203/300/250", time: "3시간 전", comment: 0, like: 1, reviewable: false },
    ];

    loadPage(1);
});

receivedPostsLink.addEventListener('click', () => {
    receivedPostsLink.classList.add('active');
    myPostsLink.classList.remove('active');

    filteredData = [...dummyData];
    loadPage(1);
});

function filterData() {
    const keyword = search.value.trim().toLowerCase();
    const status = statusFilter.value;

    filteredData = dummyData.filter(post =>
        post.title.toLowerCase().includes(keyword) &&
        (status === "전체" || post.status === status)
    );

    loadPage(1);
}

function loadPage(page) {
    currentPage = page;
    const start = (page - 1) * limit;
    renderCards(filteredData.slice(start, start + limit));
    renderPagination(filteredData.length);
}

function renderCards(posts) {
    const list = document.getElementById("post-list");
    list.innerHTML = posts.map(post => `
        <div class="col-12 col-md-4">
            <div class="card border-0 shadow-sm h-100">
                <span class="badge badge-status position-absolute top-0 start-0 m-2">${post.status}</span>
                <img src="${post.img}" class="card-img-top rounded-top">
                <div class="card-body">
                    <h6 class="fw-bold text-truncate">${post.title}</h6>
                    <p class="text-muted small mb-2">${post.time}</p>

                    <div class="d-flex justify-content-between small mb-2">
                        <span><i class="bi bi-chat-dots"></i> ${post.comment}</span>
                        <span><i class="bi bi-heart"></i> ${post.like}</span>
                    </div>

                    ${post.reviewable ? `<div class="text-end"><button class="btn btn-sm btn-outline-secondary review-badge" data-id="${post.id}">후기 작성</button></div>` : ""}
                </div>
            </div>
        </div>
    `).join("");
}

function renderPagination(total) {
    const pagination = document.getElementById("pagination");
    const totalPages = Math.ceil(total / limit);

    pagination.innerHTML = `
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
            <a class="page-link" href="#" onclick="loadPage(1)">&laquo;</a>
        </li>
        ${Array.from({ length: totalPages }, (_, i) =>
            `<li class="page-item ${i + 1 === currentPage ? "active" : ""}">
                <a class="page-link" href="#" onclick="loadPage(${i + 1})">${i + 1}</a>
            </li>`
        ).join("")}
        <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
            <a class="page-link" href="#" onclick="loadPage(${totalPages})">&raquo;</a>
        </li>
    `;
}
