// readSharing.js
import {
    renderComments,
    submitComment,
    submitEditComment,
    deleteComment
} from "/js/sharing/readSharing.comment.js";

const sharingId = Number(document.body.dataset.sharingId);

function setLoginUserNickname() {
    const nickname = document.body.dataset.memberNickname;
    const span = document.getElementById("loginUserNickname");

    if (span && nickname) {
        span.innerText = nickname;
    }
}

function renderDetail(detail) {
    document.getElementById("shareTitle").innerText = detail.title;

    document.getElementById("plantType").innerText = detail.plantType;
    document.getElementById("managementLevel").innerText = detail.managementLevel;
    document.getElementById("managementNeeds").innerText = detail.managementNeeds;

    document.getElementById("contentBox").innerHTML = detail.content;
    document.getElementById("writerNickname").innerText = detail.nickname;

    document.body.dataset.writerId = detail.memberId;
    document.body.dataset.sharingStatus = detail.status;

    document.getElementById("btnUpdate").href = `/updateSharing/${sharingId}`;
    document.getElementById("writerProfileLink").href = `/profile/${detail.memberId}`;

    document.getElementById("interestCount").innerText = `(${detail.interestNum})`;
    document.getElementById("sharingRate").innerHTML =  `🌿 나눔 지수 ${detail.sharingRate}% `;

    let timeText;

    if (detail.updatedAt) {
        timeText = timeAgo(detail.updatedAt) + " (수정됨)";
    } else {
        timeText = timeAgo(detail.createdAt);
    }
    document.getElementById("shareCreated").innerText = timeText;
}

function renderCarousel(images) {
    const inner = document.getElementById("shareCarouselInner");
    const indicators = document.getElementById("shareCarouselIndicators");

    inner.innerHTML = "";
    indicators.innerHTML = "";

    images.forEach((img, idx) => {
        inner.insertAdjacentHTML("beforeend", `
            <div class="carousel-item ${idx === 0 ? "active" : ""}">
                <img src="${img.fileUrl}" data-original="${img.fileUrl}"
                     class="d-block w-100 object-fit-cover share-image"
                     style="height:350px; cursor:pointer;">
            </div>
        `);

        indicators.insertAdjacentHTML("beforeend", `
            <button type="button" data-bs-target="#shareCarousel"
                    data-bs-slide-to="${idx}"
                    class="${idx === 0 ? "active" : ""}">
            </button>
        `);
    });
}

function updateActionButtons() {
    const loginId = Number(document.body.dataset.memberId);
    const writerId = Number(document.body.dataset.writerId);
    const status = document.body.dataset.sharingStatus;

    const myActions = document.getElementById("myActions");
    const otherActions = document.getElementById("otherActions");

    if (loginId === writerId) {
        myActions.style.display = "flex";
        document.getElementById("btnComplete").style.display =
            status === "false" ? "block" : "none";
    } else {
        otherActions.style.display = "flex";
    }
}

function getInterestCount() {
    return Number(
        document.getElementById("interestCount").textContent.replace(/[^0-9]/g, "")
    );
}

function updateInterestButton(active, count) {
    const btn = document.getElementById("btnInterest");
    const icon = document.getElementById("interestIcon");
    const countEl = document.getElementById("interestCount");

    if (active) {
        btn.classList.add("btn-danger");
        btn.classList.remove("btn-outline-secondary");
        icon.textContent = "관심❤";
    } else {
        btn.classList.remove("btn-danger");
        btn.classList.add("btn-outline-secondary");
        icon.textContent = "관심♡";
    }

    countEl.textContent = `(${count})`;
}


// sharing complete
function bindCompleteButton() {
    const btn = document.getElementById("btnComplete");
    if (!btn) return;

    btn.addEventListener("click", async () => {
        const sharingId = document.body.dataset.sharingId;
        const receiverId = document.body.dataset.memberId;

        try {
            const res = await axios.get(`/api/sharing/${sharingId}/partners`, {
                params: { receiverId }
            });

            renderCompleteTargetList(res.data);

            const modal = new bootstrap.Modal(document.getElementById("modalSelectCounterpart"));
            modal.show();

        } catch (err) {
            console.error(err);
            showAlert("대화 상대를 불러오지 못했습니다.");
        }
    });
}

document.addEventListener("change", (e) => {
    if (e.target.classList.contains("done-target-radio")) {
        document.getElementById("btnSelectComplete").disabled = false;
    }
});

document.getElementById("btnSelectComplete").addEventListener("click", () => {
    const selected = document.querySelector(".done-target-radio:checked");

    if (!selected) return;

    const nickname = selected.nextElementSibling.innerText;
    document.getElementById("confirmName").innerText = nickname;

    // 1) 모달1 닫기
    const modal1 = bootstrap.Modal.getInstance(document.getElementById("modalSelectCounterpart"));
    modal1.hide();

    // 2) 모달2 열기
    new bootstrap.Modal(document.getElementById("modalConfirmComplete")).show();

    // 모달2 - 취소하면 다시 모달1 열기
    document.getElementById("btnCompleteCancel")?.addEventListener("click", () => {

        // 1) 모달2 닫기
        const modal2 = bootstrap.Modal.getInstance(document.getElementById("modalConfirmComplete"));
        modal2.hide();

        // 2) 모달1 다시 열기
        const modal1 = new bootstrap.Modal(document.getElementById("modalSelectCounterpart"));
        modal1.show();
    });

});

// modal 3
document.getElementById("btnCompleteConfirm")?.addEventListener("click", async () => {
    const sharingId = Number(document.body.dataset.sharingId);
    const selected = document.querySelector(".done-target-radio:checked");

    if (!selected) {
        showAlert("대상을 선택하세요.");
        return;
    }

    const targetMemberId = selected.value;
    const nickname = selected.nextElementSibling.innerText;

    try {
        await axios.post(`/api/sharing/${sharingId}/complete`, null, {
            params: { targetMemberId }
        });

        // 모달2 닫기
        const modal2 = bootstrap.Modal.getInstance(document.getElementById("modalConfirmComplete"));
        modal2.hide();

        document.getElementById("resultName").innerText = nickname;
        document.getElementById("goReview").href = `/updateReview?sharingId=${sharingId}`;

        // 모달3 열기
        new bootstrap.Modal(document.getElementById("modalCompleteResult")).show();

    } catch (err) {
        console.error(err);
        showAlert("나눔 완료 처리 중 오류가 발생했습니다.");
    }
});



function renderCompleteTargetList(partners) {
    const modalEl = document.getElementById("modalSelectCounterpart");
    const body = modalEl.querySelector(".modal-body");

    body.innerHTML = `
        <div class="small text-muted mb-2">이 글에 대해 쪽지를 나눈 상대</div>
    `;

    if (partners.length === 0) {
        body.innerHTML += `<div class="text-muted small">쪽지 기록이 없습니다.</div>`;
        return;
    }

    partners.forEach((p, i) => {
        body.innerHTML += `
            <div class="form-check">
                <input class="form-check-input done-target-radio"
                       type="radio" name="doneTarget"
                       id="done${i}" value="${p.memberId}">
                <label class="form-check-label" for="done${i}">
                    ${p.nickname}
                </label>
            </div>
        `;
    });
}



async function toggleInterest() {
    const btn = document.getElementById("btnInterest");
    const countEl = document.getElementById("interestCount");
    if (!btn || !countEl) return;

    let currentCount = getInterestCount();
    const isCurrentlyInterested = btn.classList.contains("btn-danger");
    const memberId = Number(document.body.dataset.memberId);

    try {
        if (!isCurrentlyInterested) {
            const res = await axios.post(`/api/sharing/${sharingId}/interest`);
            const success = res.data === true;

            if (success) {
                updateInterestButton(true, currentCount + 1);
            } else {
                showAlert("이미 관심 등록된 글입니다.");
            }

        } else {
            const res = await axios.delete(`/api/sharing/${sharingId}/interest`);
            const success = res.data === true;

            if (success) {
                updateInterestButton(false, Math.max(0, currentCount - 1));
            } else {
                showAlert("이미 관심 해제된 글입니다.");
            }
        }

    } catch (err) {
        console.error("interest toggle error:", err);
        showAlert("관심 처리 중 오류가 발생했습니다.");
    }
}

function bindMessageButton() {
    document.getElementById("btnMessage").addEventListener("click", () => {
        document.getElementById("msgTo").value = document.getElementById("writerNickname").innerText;
        document.getElementById("msgPost").value = document.getElementById("shareTitle").innerText;

        new bootstrap.Modal(document.getElementById("messageModal")).show();
    });
}

document.getElementById("messageForm").addEventListener("submit", async e => {
    e.preventDefault();

    const senderId = Number(document.body.dataset.memberId);
    const receiverId = Number(document.body.dataset.writerId);
    const title = document.getElementById("msgTitle").value.trim();
    const content = document.getElementById("msgContent").value.trim();

    if (!title || !content) {
        showAlert("제목과 내용을 입력하세요.");
        return;
    }

    await axios.post("/api/message/messageRegist", {
        senderId,
        receiverId,
        title,
        content,
        targetType: "SHARING",
        targetId: sharingId
    });

    showAlert("쪽지를 보냈습니다!");

    bootstrap.Modal.getInstance(document.getElementById("messageModal")).hide();
});

async function deleteSharing() {
    showModal("정말 삭제하시겠습니까?", async confirm => {
        if (!confirm) return;

        await axios.delete(`/api/sharing/${sharingId}`);
        showAlert("삭제되었습니다.", () => (location.href = "/sharingList"));
    });
}

async function loadComments() {
    const res = await axios.get(`/api/sharing/${sharingId}/comments`);
    renderComments(res.data);
}

function bindCommentSubmitEvent() {
    document.getElementById("btnCommentSubmit").addEventListener("click", async () => {
        const ok = await submitComment(sharingId);
        if (ok) loadComments();
    });
}

async function loadSharingDetail() {
    const res = await axios.get(`/api/sharing/${sharingId}`);
    const data = res.data;

    renderDetail(data);
    renderCarousel(data.images);
    updateActionButtons();
}

function init() {
    setLoginUserNickname();
    document.getElementById("btnInterest").addEventListener("click", toggleInterest);
    document.getElementById("btnDelete").addEventListener("click", deleteSharing);
    // document.getElementById("btnComplete").addEventListener("click", completeSharing);
    bindMessageButton();
    bindCommentSubmitEvent();
    bindCompleteButton();

    document.addEventListener("comments:changed", loadComments);

    loadSharingDetail();
    loadComments();
}

document.addEventListener("DOMContentLoaded", init);
