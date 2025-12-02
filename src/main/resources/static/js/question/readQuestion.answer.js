function formatDate(value) {
    if (!value) return "";
    if (typeof timeAgo === "function") {
        return timeAgo(value);
    }
    return value;
}

export function renderAnswers(list) {
    const container = document.getElementById("answerList");
    const loginUserId = Number(document.body.dataset.memberId);

    container.innerHTML = "";

    list.forEach((a) => {
        const isMine = loginUserId === Number(a.writerId);

        container.insertAdjacentHTML(
            "beforeend",
            `
            <li class="list-group-item d-flex justify-content-between align-items-start" 
                data-answer-id="${a.answerId}">
                
                <!-- 왼쪽: 닉네임 + 내용 -->
                <div class="comment-left">
                    <div class="fw-semibold small">${a.nickname}</div>
                    <div class="small answer-content">${a.content}</div>
                </div>

                <!-- 오른쪽: 날짜 + 수정/삭제 버튼 -->
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
                        <div class="mt-1 answer-buttons">
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
    // 수정
    document.querySelectorAll(".answer-edit-btn").forEach((btn) => {
        btn.addEventListener("click", (e) => {
            const li = e.target.closest("li");
            startInlineEdit(li);
        });
    });

    // 삭제
    document.querySelectorAll(".answer-delete-btn").forEach((btn) => {
        btn.addEventListener("click", (e) => {
            const li = e.target.closest("li");
            const answerId = Number(li.dataset.answerId);
            deleteAnswer(answerId);
        });
    });
}

function startInlineEdit(li) {
    const contentDiv = li.querySelector(".answer-content");
    const buttonBox = li.querySelector(".answer-buttons");

    const original = contentDiv.textContent.trim();

    contentDiv.innerHTML = `
        <input type="text" class="form-control form-control-sm edit-input" value="${original}">
    `;

    buttonBox.innerHTML = `
        <button class="btn btn-sm btn-link p-0 me-2 text-primary answer-save-btn">저장</button>
        <button class="btn btn-sm btn-link p-0 text-muted answer-cancel-btn">취소</button>
    `;

    buttonBox.querySelector(".answer-save-btn").addEventListener("click", async () => {
        const newText = li.querySelector(".edit-input").value.trim();
        const answerId = Number(li.dataset.answerId);

        if (!newText) {
            showAlert("내용을 입력하세요.");
            return;
        }

        const ok = await submitEditAnswer(answerId, newText);
        if (ok) document.dispatchEvent(new CustomEvent("answers:changed"));
    });

    buttonBox.querySelector(".answer-cancel-btn").addEventListener("click", () => {
        contentDiv.textContent = original;
        restoreButtons(buttonBox);
    });
}

function restoreButtons(box) {
    box.innerHTML = `
        <button class="btn btn-sm btn-link text-muted p-0 me-2 answer-edit-btn">수정</button>
        <button class="btn btn-sm btn-link text-muted p-0 answer-delete-btn">삭제</button>
    `;
    bindAnswerEvents();
}

export async function submitAnswer(questionId) {
    const input = document.getElementById("answerInput");
    const content = input.value.trim();

    if (!content) {
        showAlert("댓글을 입력하세요.");
        return false;
    }

    await axios.post(`/api/questions/${questionId}/answers`, {
        content
    });

    input.value = "";
    return true;
}

export async function submitEditAnswer(answerId, newText) {
    await axios.put(`/api/questions/answers/${answerId}`, {
        content: newText,
        questionId: Number(document.body.dataset.questionId)
    });

    return true;
}

export async function deleteAnswer(answerId) {
    const questionId = Number(document.body.dataset.questionId);

    showModal("삭제하시겠습니까?", async (confirm) => {
        if (!confirm) return;

        await axios.delete(`/api/questions/answers/${answerId}`, {
            params: { questionId }
        });

        document.dispatchEvent(new CustomEvent("answers:changed"));
    });
}
