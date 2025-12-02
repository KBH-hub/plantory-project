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

function setLoginUserNickname() {
    const nick = document.body.dataset.memberNickname;
    const span = document.getElementById("loginUserNickname");
    if (span && nick) span.innerText = nick;
}

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

function updateActionButtons(detail) {
    const loginId = Number(document.body.dataset.memberId);
    const writerId = detail.memberId;

    const myActions = document.getElementById("myActions");
    const otherActions = document.getElementById("otherActions");

    if (loginId === writerId) {
        myActions.style.display = "flex";
    } else {
        otherActions.style.display = "flex";

        // 쪽지 보내기
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

async function loadAnswerList() {
    try {
        const res = await axios.get(`/api/questions/${questionId}/answers`);
        renderAnswerList(res.data);
    } catch (err) {
        console.error(err);
    }
}

function renderAnswerList(list) {
    const container = document.getElementById("answerList");
    container.innerHTML = "";

    if (list.length === 0) {
        container.innerHTML = `<div class="text-muted py-3">아직 댓글이 없습니다.</div>`;
        return;
    }

    list.forEach(a => {
        container.insertAdjacentHTML("beforeend", `
            <div class="border rounded p-3 mb-2 bg-white">
                <div class="fw-semibold mb-1">${a.nickname}</div>
                <small class="text-muted">${timeAgo(a.createdAt)}</small>
                <p class="mt-2 mb-0">${a.content}</p>
            </div>
        `);
    });
}

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
                params: {
                    content,
                }
            });

            input.value = "";
            loadAnswerList();

        } catch (err) {
            console.error(err);
            showAlert("댓글 등록 실패했습니다.");
        }
    });
}
