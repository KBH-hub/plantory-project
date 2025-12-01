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
        alert("목록을 불러오는데 실패했습니다.");
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
        const html = `
            <div class="row mb-3 p-3 bg-white border rounded">

                <div class="col-1 d-flex justify-content-center">
                    <img src="https://via.placeholder.com/40"
                         class="rounded-circle" style="width:40px;height:40px;">
                </div>

                <div class="col-9">
                    <a href="/question/${item.questionId}" class="text-decoration-none text-dark">
                        <p class="fw-bold mb-1">${item.title}</p>
                        <small class="text-muted">${item.nickname} · ${timeAgo(item.createdAt)}</small>
                    </a>
                </div>

                <div class="col-2 d-flex flex-column align-items-end justify-content-center">
                    <img src="${item.imageUrl ?? 'https://via.placeholder.com/70'}"
                         class="border rounded mb-1" 
                         style="width:70px;height:70px;object-fit:cover;">
                    <span class="text-muted small">
                        <i class="bi bi-chat-left-text"></i> ${item.answerCount}
                    </span>
                </div>

            </div>
        `;

        container.insertAdjacentHTML("beforeend", html);
    });
}

function renderPagination(page, size, totalCount) {

    const pager = document.getElementById("pager");
    pager.innerHTML = "";

    const totalPage = Math.ceil(totalCount / size);
    if (totalPage === 0) return;

    let html = "";

    // 이전
    if (page > 1) {
        html += `
            <li class="page-item">
                <a class="page-link" href="#" onclick="loadQuestionList(${page - 1})">&laquo;</a>
            </li>
        `;
    }

    // 페이지 번호
    for (let p = 1; p <= totalPage; p++) {
        html += `
            <li class="page-item ${p === page ? 'active' : ''}">
                <a class="page-link" href="#" onclick="loadQuestionList(${p})">${p}</a>
            </li>
        `;
    }

    // 다음
    if (page < totalPage) {
        html += `
            <li class="page-item">
                <a class="page-link" href="#" onclick="loadQuestionList(${page + 1})">&raquo;</a>
            </li>
        `;
    }

    pager.innerHTML = html;
}
