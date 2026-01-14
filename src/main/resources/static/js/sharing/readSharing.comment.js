function formatDate(value) {
    if (!value) return "";
    if (typeof timeAgo === "function") {
        return timeAgo(value);
    }
    return value;
}

export function renderComments(list) {
    const container = document.getElementById("commentList");
    const loginUserId = Number(document.body.dataset.memberId);

    container.innerHTML = "";

    list.forEach((c) => {
        const isMine = loginUserId === Number(c.writerId);


        container.insertAdjacentHTML(
            "beforeend",
            `
            <li class="list-group-item d-flex justify-content-between align-items-start" data-comment-id="${c.commentId}">
                
                <!-- 왼쪽: 닉네임 + 내용 -->
                <div class="comment-left">
                    <div class="fw-semibold small">${c.nickname}</div>
                    <div class="small comment-content">${c.content}</div>
                </div>

                <div class="text-end ms-3">

            <div class="text-muted small">
                ${
                c.updatedAt
                    ? `${formatDate(c.updatedAt)} <span class="text-muted">(수정됨)</span>`
                    : formatDate(c.createdAt)
            }
            </div>

                    ${isMine ? `
                        <div class="mt-1 comment-buttons">
                            <button class="btn btn-sm btn-link text-muted p-0 me-2 comment-edit-btn">
                                수정
                            </button>
                            <button class="btn btn-sm btn-link text-muted p-0 comment-delete-btn">
                                삭제
                            </button>
                        </div>
                        `
                    : ""}
                </div>

            </li>
        `
        );
    });

    bindCommentEvents();
}

function bindCommentEvents() {
    document.querySelectorAll(".comment-edit-btn").forEach((btn) => {
        btn.addEventListener("click", (e) => {
            const li = e.target.closest("li");
            startInlineEdit(li);
        });
    });

    document.querySelectorAll(".comment-delete-btn").forEach((btn) => {
        btn.addEventListener("click", (e) => {
            const li = e.target.closest("li");
            const commentId = Number(li.dataset.commentId);
            deleteComment(commentId);
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
        <button class="btn btn-sm btn-link p-0 me-2 text-primary comment-save-btn">저장</button>
        <button class="btn btn-sm btn-link p-0 text-muted comment-cancel-btn">취소</button>
    `;

    buttonBox.querySelector(".comment-save-btn").addEventListener("click", async () => {
        const newText = li.querySelector(".edit-input").value.trim();
        const commentId = Number(li.dataset.commentId);

        if (!newText) {
            showAlert("내용을 입력하세요.");
            return;
        }

        const success = await submitEditComment(commentId, newText);
        if (success) {
            document.dispatchEvent(new CustomEvent("comments:changed"));
        }
    });

    buttonBox.querySelector(".comment-cancel-btn").addEventListener("click", () => {
        contentDiv.textContent = originText;
        restoreButtons(buttonBox);
    });
}

function restoreButtons(buttonBox) {
    buttonBox.innerHTML = `
        <button class="btn btn-sm btn-link text-muted p-0 me-2 comment-edit-btn">수정</button>
        <button class="btn btn-sm btn-link text-muted p-0 comment-delete-btn">삭제</button>
    `;

    bindCommentEvents();
}

export async function submitComment(sharingId) {
    const input = document.getElementById("commentInput");
    const content = input.value.trim();
    const memberId = Number(document.body.dataset.memberId);

    if (!content) {
        showAlert("댓글을 입력하세요.");
        return false;
    }

    await axios.post(`/api/sharing/${sharingId}/comments`, {
        content
    });


    input.value = "";
    return true;
}

export async function submitEditComment(commentId, newText) {
    await axios.put(`/api/sharing/comments/${commentId}`, {
        content: newText,
        sharingId: Number(document.body.dataset.sharingId)
    });


    return true;
}

export async function deleteComment(commentId) {
    const memberId = Number(document.body.dataset.memberId);
    const sharingId = Number(document.body.dataset.sharingId);

    showModal("삭제하시겠습니까?", async (confirm) => {
        if (!confirm) return;

        await axios.delete(`/api/sharing/comments/${commentId}`, {
            data: {
                sharingId: Number(document.body.dataset.sharingId)
            }
        });


        document.dispatchEvent(new CustomEvent("comments:changed"));
    });
}
