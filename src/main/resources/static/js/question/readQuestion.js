import { openMessageModal, bindMessageSubmit } from "/js/common/message.js";

const questionId = Number(document.body.dataset.questionId);

document.addEventListener("DOMContentLoaded", () => {
    bindMessageSubmit();
    loadQuestionDetail();
    loadAnswerList();
    setLoginUserNickname();
});

function setLoginUserNickname() {
    const nick = document.body.dataset.memberNickname;
    document.getElementById("loginUserNickname").innerText = nick;
}

async function loadQuestionDetail() {
    try {
        const res = await axios.get(`/api/questions/${questionId}`);
        renderDetail(res.data);
        renderCarousel(res.data.images);
        updateActionButtons(res.data);
    } catch (err) {
        console.error(err);
        alert("질문 상세 정보를 불러오는데 실패했습니다.");
    }
}

function renderDetail(detail) {
    document.getElementById("questionTitle").innerText = detail.title;
    document.getElementById("writerNickname").innerText = detail.nickname;
    document.getElementById("questionCreated").innerText = timeAgo(detail.createdAt);

    document.getElementById("contentBox").innerHTML = detail.content;

    document.body.dataset.writerId = detail.memberId;
    document.getElementById("writerProfileLink").href = `/profile/${detail.memberId}`;
    document.getElementById("btnUpdate").href = `/updateQuestion/${questionId}`;
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
                    class="${i === 0 ? "active" : ""}">
            </button>
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
