import { openMessageModal, bindMessageSubmit } from "/js/common/message.js";
import { loadAnswerList, bindAnswerSubmit } from "/js/question/readQuestion.answer.js";

const questionId = Number(document.body.dataset.questionId);

document.addEventListener("DOMContentLoaded", () => {
    bindMessageSubmit();
    setLoginUserNickname();
    loadQuestionDetail();
    loadAnswerList(questionId);
    bindAnswerSubmit(questionId);
    bindDeleteButton();
    document.addEventListener("answers:changed", () => {
        loadAnswerList(questionId);
    });
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

    const timeText = detail.updatedAt
        ? `${timeAgo(detail.updatedAt)} (수정됨)`
        : timeAgo(detail.createdAt);

    document.getElementById("questionCreated").innerText = timeText;
    renderProfileImage(detail.memberId);

}

document.addEventListener("click", function (e) {
    if (e.target.classList.contains("question-image")) {
        const originalUrl = e.target.dataset.original;
        const zoomImg = document.getElementById("zoomImg");
        zoomImg.src = originalUrl;

        const modal = new bootstrap.Modal(document.getElementById("imgZoomModal"));
        modal.show();
    }
});

function renderCarousel(images) {
    const inner = document.getElementById("questionCarouselInner");
    const indicators = document.getElementById("questionCarouselIndicators");

    inner.innerHTML = "";
    indicators.innerHTML = "";

    if (!images || images.length === 0) return;

    images.forEach((img, i) => {
        inner.insertAdjacentHTML("beforeend", `
            <div class="carousel-item ${i === 0 ? "active" : ""}">
                 <img src="${img.fileUrl}" 
                     data-original="${img.fileUrl}"
                     class="d-block w-100 object-fit-cover question-image"
                     style="height:450px; cursor:pointer;">
            </div>
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

function bindDeleteButton() {
    const btn = document.getElementById("btnDelete");
    if (!btn) return;

    btn.addEventListener("click", () => {
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

async function renderProfileImage(memberId) {
    const box = document.getElementById("writerProfileImage");
    if (!box) return;

    try {
        const res = await axios.get(`/api/profile/picture?memberId=${memberId}`);
        const imageUrl = res.data.imageUrl;

        if (imageUrl) {
            box.innerHTML = `
                <img src="${imageUrl}" 
                     class="rounded-circle"
                     style="width:48px; height:48px; object-fit:cover;">
            `;
        } else {
            box.innerHTML = `
                <div class="bg-secondary rounded-circle"
                     style="width:48px;height:48px;"></div>
            `;
        }

    } catch (e) {
        console.error("프로필 이미지 불러오기 실패", e);
    }
}
