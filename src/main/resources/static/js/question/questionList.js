const questionId = document.body.dataset.questionId;

document.addEventListener("DOMContentLoaded", () => {

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
        renderPagination(data.page, data.size, data.totalCount);

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

function renderPagination(page, size, totalCount) {
    const ul = document.getElementById('pager');
    if (!ul) return;

    ul.innerHTML = "";

    const totalPages = Math.max(1, Math.ceil(totalCount / size));
    const current = page;

    const goPage = (p) => loadQuestionList(p);

    const makeItem = (label, p, { disabled = false, active = false, aria = null } = {}) => {
        const li = document.createElement('li');
        li.className = `page-item${disabled ? ' disabled' : ''}${active ? ' active' : ''}`;

        const a = document.createElement('a');
        a.className = 'page-link';
        a.href = "#";
        if (aria) a.setAttribute('aria-label', aria);
        a.textContent = label;

        if (!disabled && !active) {
            a.addEventListener('click', (ev) => {
                ev.preventDefault();
                goPage(p);
            });
        } else {
            a.addEventListener('click', (ev) => ev.preventDefault());
        }

        li.appendChild(a);
        return li;
    };

    ul.appendChild(makeItem('«', 1, { disabled: current === 1, aria: "처음" }));

    ul.appendChild(makeItem('‹', current - 1, { disabled: current === 1, aria: "이전" }));

    const windowSize = 5;
    const blockStart = Math.floor((current - 1) / windowSize) * windowSize + 1;
    const blockEnd = Math.min(blockStart + windowSize - 1, totalPages);

    for (let p = blockStart; p <= blockEnd; p++) {
        ul.appendChild(makeItem(String(p), p, { active: p === current }));
    }

    const isLast = current >= totalPages;
    ul.appendChild(makeItem('›', current + 1, { disabled: isLast, aria: "다음" }));

    ul.appendChild(makeItem('»', totalPages, { disabled: isLast, aria: "마지막" }));
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
