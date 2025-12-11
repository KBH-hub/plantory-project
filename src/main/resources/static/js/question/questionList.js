import { createPaginator } from '/js/common/pagination.js';

const questionId = document.body.dataset.questionId;

let pager = null;

document.addEventListener("DOMContentLoaded", () => {

    const pagerEl = document.getElementById('pager');

    pager = createPaginator({
        container: pagerEl,
        current: 1,
        pageSize: 10,
        totalItems: 0,
        labels: { first:'«', prev:'‹', next:'›', last:'»' },
        onChange: (page) => loadQuestionList(page)
    });

    loadQuestionList(1);

    document.getElementById("btnSearch").addEventListener("click", () => {
        loadQuestionList(1);
    });
});

async function loadQuestionList(page) {

    const keyword = document.getElementById("keywordInput").value;

    try {
        const res = await axios.get("/api/questions", {
            params: {
                page,
                size: 10,
                keyword
            }
        });

        const data = res.data;
        renderList(data.list);

        if (pager) {
            pager.update({
                current: data.page,
                pageSize: data.size,
                totalItems: data.totalCount,
            });
        }

    } catch (err) {
        console.error(err);
        showAlert("목록을 불러오는데 실패했습니다.");
    }
}

function renderList(list) {
    const container = document.getElementById("questionList");

    container.innerHTML = "";

    if (list.length === 0) {
        container.innerHTML = "<div class='text-center text-muted py-4'>게시글이 없습니다.</div>";
        return;
    }

    list.forEach(item => {

        const isEdited = item.updatedAt && item.updatedAt !== item.createdAt;
        const editedTag = isEdited ? " <span class='text-muted'>(수정됨)</span>" : "";

        const displayTime = isEdited ? timeAgo(item.updatedAt) : timeAgo(item.createdAt);

        const html = `
        <a href="/readQuestion/${item.questionId}" class="row mb-3 p-3 bg-white border rounded text-decoration-none text-dark">
            <div class="col-1 d-flex justify-content-center">
                <div id="profile-${item.memberId}">
                    <img src="https://via.placeholder.com/40"
                         class="rounded-circle" style="width:40px;height:40px;">
                     </div>
            </div>

            <div class="col-9">
                    <p class="fw-bold mb-1">${item.title}</p>
                    <small class="text-muted">${item.nickname} · ${displayTime} ${editedTag}</small>
            </div>

            <div class="col-2 d-flex flex-column align-items-end justify-content-center">
                <img src="${item.imageUrl}"
                     class="border rounded mb-1" 
                     style="width:100px;height:100px;object-fit:cover;">
                <span class="text-muted small">
                    <i class="bi bi-chat-left-text"></i> ${item.answerCount}
                </span>
            </div>
        </a>
    `;

        container.insertAdjacentHTML("beforeend", html);

        loadProfileImage(item.memberId);
    });

}

async function loadProfileImage(memberId) {
    if (!memberId) return;
    memberId = Number(memberId);

    const box = document.getElementById(`profile-${memberId}`);
    if (!box) return;

    try {
        const res = await axios.get(`/api/profile/picture`, {
            params: { memberId }
        });
        const url = res.data.imageUrl;

        box.innerHTML = url
            ? `<img src="${url}" class="rounded-circle" style="width:40px;height:40px;">`
            : `<div class="bg-secondary rounded-circle" style="width:40px;height:40px;"></div>`;

    } catch (err) {
        console.error("프사 불러오기 실패", err);
    }
}
