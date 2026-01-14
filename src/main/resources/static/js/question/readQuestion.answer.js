function formatDate(value) {
    if (!value) return "";
    if (typeof timeAgo === "function") return timeAgo(value);
    return value;
}

export async function loadAnswerList(questionId) {
    try {
        const res = await axios.get(`/api/questions/${questionId}/answers`);
        renderAnswerList(res.data);
    } catch (err) {
        console.error(err);
        showAlert("댓글 목록을 불러오지 못했습니다.");
    }
}

function renderAnswerList(list) {
    const container = document.getElementById("answerList");
    const loginUserId = Number(document.body.dataset.memberId);
    container.innerHTML = "";

    if (!list || list.length === 0) {
        container.innerHTML = `<div class="text-muted small py-3"></div>`;
        return;
    }

    list.forEach(a => {
        const isMine = loginUserId === Number(a.writerId);

        container.insertAdjacentHTML(
            "beforeend",
            `
            <li class="list-group-item d-flex justify-content-between align-items-start"
                data-answer-id="${a.answerId}">
                
                <div class="comment-left">
                    <div class="fw-semibold small">${a.nickname}</div>
                    <div class="small comment-content">${a.content}</div>
                </div>

                <div class="text-end ms-3">
                    <div class="text-muted small">
                        ${
                a.updatedAt
                    ? `${formatDate(a.updatedAt)} <span class="text-muted">(수정됨)</span>`
                    : formatDate(a.createdAt)
            }
                    </div>

                    ${
                isMine
                    ? `
                                <div class="mt-1 comment-buttons">
                                    <button class="btn btn-sm btn-link text-muted p-0 me-2 answer-edit-btn">수정</button>
                                    <button class="btn btn-sm btn-link text-muted p-0 answer-delete-btn">삭제</button>
                                </div>
                            `
                    : ""
            }
                </div>

            </li>
            `
        );
    });

    bindAnswerEvents();
}

function bindAnswerEvents() {
    document.querySelectorAll(".answer-edit-btn").forEach(btn => {
        btn.addEventListener("click", e => {
            const li = e.target.closest("li");
            startInlineEdit(li);
        });
    });

    document.querySelectorAll(".answer-delete-btn").forEach(btn => {
        btn.addEventListener("click", e => {
            const li = e.target.closest("li");
            const answerId = Number(li.dataset.answerId);
            deleteAnswer(answerId);
        });
    });
}

function startInlineEdit(li) {
    const contentDiv = li.querySelector(".comment-content");
    const buttonBox = li.querySelector(".comment-buttons");

    const originText = contentDiv.textContent.trim();

    contentDiv.innerHTML = `
        <input type="text" class="form-control form-control-sm edit-input" value="${originText}">
    `;

    buttonBox.innerHTML = `
        <button class="btn btn-sm btn-link p-0 me-2 text-primary answer-save-btn">저장</button>
        <button class="btn btn-sm btn-link p-0 text-muted answer-cancel-btn">취소</button>
    `;

    buttonBox.querySelector(".answer-save-btn").addEventListener("click", async () => {
        const newText = li.querySelector(".edit-input").value.trim();
        const answerId = Number(li.dataset.answerId);
        const questionId = Number(document.body.dataset.questionId);

        if (!newText) {
            showAlert("내용을 입력하세요.");
            return;
        }

        await axios.put(`/api/questions/answers/${answerId}`, {
            content: newText,
            questionId
        });

        document.dispatchEvent(new CustomEvent("answers:changed"));
    });

    buttonBox.querySelector(".answer-cancel-btn").addEventListener("click", () => {
        contentDiv.textContent = originText;
        restoreButtons(buttonBox);
    });
}

function restoreButtons(buttonBox) {
    buttonBox.innerHTML = `
        <button class="btn btn-sm btn-link text-muted p-0 me-2 answer-edit-btn">수정</button>
        <button class="btn btn-sm btn-link text-muted p-0 answer-delete-btn">삭제</button>
    `;
    bindAnswerEvents();
}

async function deleteAnswer(answerId) {
    const questionId = Number(document.body.dataset.questionId);

    showModal("삭제하시겠습니까?", async confirm => {
        if (!confirm) return;

        await axios.delete(`/api/questions/answers/${answerId}`, {
            data: { questionId }
        });

        document.dispatchEvent(new CustomEvent("answers:changed"));
    });
}

export function bindAnswerSubmit(questionId) {
    const btn = document.getElementById("btnAnswerSubmit");
    const input = document.getElementById("answerInput");

    btn.addEventListener("click", async () => {
        const content = input.value.trim();

        if (!content) {
            showAlert("댓글 내용을 입력하세요.");
            return;
        }

        await axios.post(`/api/questions/${questionId}/answers`, { content });

        input.value = "";

        document.dispatchEvent(new CustomEvent("answers:changed"));
    });
}
