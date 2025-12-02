import { openMessageModal, bindMessageSubmit } from "/js/common/message.js";

const questionId = Number(document.body.dataset.questionId);

document.addEventListener("DOMContentLoaded", () => {
    bindMessageSubmit();
    setLoginUserNickname();
    loadQuestionDetail();
    loadAnswerList();
    bindAnswerSubmit();
    bindDeleteButton();
});

/* ---------------------------
    로그인 닉네임 표시
---------------------------- */
function setLoginUserNickname() {
    const nick = document.body.dataset.memberNickname;
    const span = document.getElementById("loginUserNickname");
    if (span && nick) span.innerText = nick;
}

/* ---------------------------
    질문 상세 불러오기
---------------------------- */
async function loadQuestionDetail() {
    try {
        const res = await axios.get(`/api/questions/${questionId}`);
        const detail = res.data;

        renderDetail(detail);
        renderCarousel(detail.images);
        updateActionButtons(detail);

    } catch (err) {
        console.error(err);
        showAlert("질문 상세 정보를 불러오는데 실패했습니다.");
    }
}

function renderDetail(detail) {
    document.getElementById("questionTitle").innerText = detail.title;
    document.getElementById("writerNickname").innerText = detail.nickname;
    document.getElementById("contentBox").innerHTML = detail.content;

    document.body.dataset.writerId = detail.memberId;

    document.getElementById("writerProfileLink").href = `/profile/${detail.memberId}`;
    document.getElementById("btnUpdate").href = `/updateQuestion/${questionId}`;

    let timeText;
    if (detail.updatedAt) {
        timeText = timeAgo(detail.updatedAt) + " (수정됨)";
    } else {
        timeText = timeAgo(detail.createdAt);
    }
    document.getElementById("questionCreated").innerText = timeText;
}

/* ---------------------------
    캐러셀 렌더링
---------------------------- */
function renderCarousel(images) {
    const inner = document.getElementById("questionCarouselInner");
    const indicators = document.getElementById("questionCarouselIndicators");

    inner.innerHTML = "";
    indicators.innerHTML = "";

    if (!images || images.length === 0) return;

    images.forEach((img, i) => {
        inner.insertAdjacentHTML("beforeend", `
            <div class="carousel-item ${i === 0 ? "active" : ""}">
                <img src="${img.fileUrl}" class="d-block w-100 object-fit-cover"
                     style="height:350px; cursor:pointer;">
            </div>
        `);

        indicators.insertAdjacentHTML("beforeend", `
            <button type="button" data-bs-target="#questionCarousel"
                    data-bs-slide-to="${i}"
                    class="${i === 0 ? "active" : ""}"></button>
        `);
    });
}

/* ---------------------------
    수정 / 삭제 버튼 노출
---------------------------- */
function updateActionButtons(detail) {
    const loginId = Number(document.body.dataset.memberId);
    const writerId = detail.memberId;

    const myActions = document.getElementById("myActions");
    const otherActions = document.getElementById("otherActions");

    if (loginId === writerId) {
        myActions.style.display = "flex";
    } else {
        otherActions.style.display = "flex";

        document.getElementById("btnMessage").addEventListener("click", () => {
            openMessageModal(
                writerId,
                detail.nickname,
                detail.title,
                "QUESTION",
                questionId
            );
        });
    }
}

/* ---------------------------
    질문 삭제
---------------------------- */
function bindDeleteButton() {
    const btn = document.getElementById("btnDelete");
    if (!btn) return;

    btn.addEventListener("click", async () => {
        showModal("정말 삭제하시겠습니까?", async confirm => {
            if (!confirm) return;

            try {
                await axios.delete(`/api/questions/${questionId}`);
                showAlert("삭제되었습니다.", () => (location.href = "/questionList"));
            } catch (err) {
                console.error(err);
                showAlert("삭제 실패!");
            }
        });
    });
}

/* ---------------------------
    댓글 목록 불러오기(Answers)
---------------------------- */
async function loadAnswerList() {
    try {
        const res = await axios.get(`/api/questions/${questionId}/answers`);
        renderAnswerList(res.data);
    } catch (err) {
        console.error(err);
    }
}

/* ---------------------------
    댓글 렌더링 + 수정·삭제 버튼 포함
---------------------------- */
function renderAnswerList(list) {
    const container = document.getElementById("answerList");
    const loginId = Number(document.body.dataset.memberId);
    container.innerHTML = "";

    if (list.length === 0) {
        container.innerHTML = `<div class="text-muted py-3">댓글이 없습니다.</div>`;
        return;
    }

    list.forEach(a => {
        const isMine = loginId === Number(a.writerId);

        container.insertAdjacentHTML("beforeend", `
            <div class="border rounded p-3 mb-2 bg-white position-relative"
                 data-answer-id="${a.answerId}">

                <div class="fw-semibold mb-1">${a.nickname}</div>

                <small class="text-muted">
                    ${a.updatedAt ? `${timeAgo(a.updatedAt)} (수정됨)` : timeAgo(a.createdAt)}
                </small>

                <div class="mt-2 answer-content">${a.content}</div>

                ${isMine ? `
                <div class="mt-2 text-end">
                    <button class="btn btn-sm btn-link text-muted p-0 me-2 btn-edit-answer">수정</button>
                    <button class="btn btn-sm btn-link text-muted p-0 btn-delete-answer">삭제</button>
                </div>
                ` : ""}
            </div>
        `);
    });

    bindAnswerEvents();
}

/* ---------------------------
    댓글 수정/삭제 버튼 이벤트
---------------------------- */
function bindAnswerEvents() {

    // 수정 클릭
    document.querySelectorAll(".btn-edit-answer").forEach(btn => {
        btn.addEventListener("click", e => {
            const box = e.target.closest("[data-answer-id]");
            startAnswerEdit(box);
        });
    });

    // 삭제 클릭
    document.querySelectorAll(".btn-delete-answer").forEach(btn => {
        btn.addEventListener("click", e => {
            const box = e.target.closest("[data-answer-id]");
            const answerId = Number(box.dataset.answerId);
            deleteAnswer(answerId);
        });
    });
}

/* ---------------------------
    댓글 inline 수정 UI
---------------------------- */
function startAnswerEdit(box) {
    const contentDiv = box.querySelector(".answer-content");
    const oldText = contentDiv.textContent.trim();

    contentDiv.innerHTML = `
        <input type="text" class="form-control form-control-sm edit-input"
               value="${oldText}">
    `;

    const btnArea = box.querySelector("div.text-end");
    btnArea.innerHTML = `
        <button class="btn btn-sm btn-link p-0 me-2 text-primary btn-save-answer">저장</button>
        <button class="btn btn-sm btn-link p-0 text-muted btn-cancel-answer">취소</button>
    `;

    // 저장
    btnArea.querySelector(".btn-save-answer").addEventListener("click", async () => {
        const newText = box.querySelector(".edit-input").value.trim();
        const answerId = Number(box.dataset.answerId);

        if (!newText) {
            showAlert("내용을 입력하세요.");
            return;
        }

        try {
            await axios.put(`/api/questions/answers/${answerId}`, {
                content: newText
            });

            loadAnswerList();
        } catch (err) {
            console.error(err);
            showAlert("수정 실패!");
        }
    });

    // 취소
    btnArea.querySelector(".btn-cancel-answer").addEventListener("click", () => {
        contentDiv.textContent = oldText;
        loadAnswerList();
    });
}

/* ---------------------------
    댓글 삭제
---------------------------- */
async function deleteAnswer(answerId) {
    showModal("삭제하시겠습니까?", async confirm => {
        if (!confirm) return;

        try {
            await axios.delete(`/api/questions/answers/${answerId}`);
            loadAnswerList();
        } catch (err) {
            console.error(err);
            showAlert("삭제 실패!");
        }
    });
}

/* ---------------------------
    댓글 등록
---------------------------- */
function bindAnswerSubmit() {
    const btn = document.getElementById("btnAnswerSubmit");
    const input = document.getElementById("answerInput");

    btn.addEventListener("click", async () => {
        const content = input.value.trim();
        if (!content) {
            showAlert("댓글 내용을 입력하세요.");
            return;
        }

        try {
            await axios.post(`/api/questions/${questionId}/answers`, null, {
                params: { content }
            });

            input.value = "";
            loadAnswerList();

        } catch (err) {
            console.error(err);
            showAlert("댓글 등록 실패!");
        }
    });
}
