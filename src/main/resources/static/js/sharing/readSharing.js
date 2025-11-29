const sharingId = Number(document.body.dataset.sharingId);
let shareTitle = "";

function renderCarousel(images) {
    const inner = document.getElementById("shareCarouselInner");
    const indicators = document.getElementById("shareCarouselIndicators");

    inner.innerHTML = "";
    indicators.innerHTML = "";

    images.forEach((img, idx) => {
        const activeClass = idx === 0 ? "active" : "";

        inner.insertAdjacentHTML("beforeend", `
            <div class="carousel-item ${activeClass}">
                <img src="${img.fileUrl}"
                     data-original="${img.fileUrl}"
                     class="d-block w-100 object-fit-cover share-image"
                     style="height:350px; cursor:pointer;">
            </div>
        `);

        indicators.insertAdjacentHTML("beforeend", `
            <button type="button" data-bs-target="#shareCarousel"
                    data-bs-slide-to="${idx}"
                    class="${activeClass}"></button>
        `);
    });
}

function renderDetail(detail) {
    shareTitle = detail.title;

    document.getElementById("shareTitle").innerText = detail.title;
    document.getElementById("shareCreated").innerText = timeAgo(detail.createdAt);

    document.getElementById("plantType").innerText = detail.plantType;
    document.getElementById("managementLevel").innerText = detail.managementLevel;
    document.getElementById("managementNeeds").innerText = detail.managementNeeds;

    document.getElementById("contentBox").innerHTML = detail.content;
    document.getElementById("writerNickname").innerText = detail.nickname;

    const btnUpdate = document.getElementById("btnUpdate");
    if (btnUpdate) {
        btnUpdate.href = `/updateSharing/${sharingId}`;
    }

    document.body.dataset.writerId = detail.memberId;
    document.body.dataset.sharingStatus = detail.status;

    const profileLink = document.getElementById("writerProfileLink");
    if (profileLink) {
        profileLink.href = `/profile/${detail.memberId}`;
    }

    const interestCountEl = document.getElementById("interestCount");
    if (interestCountEl) {
        interestCountEl.innerText = `(${detail.interestNum ?? 0})`;
    }
}

function renderComments(list) {
    const container = document.getElementById("commentList");
    container.innerHTML = "";

    list.forEach(c => {
        container.insertAdjacentHTML("beforeend", `
            <li class="list-group-item d-flex justify-content-between align-items-center">
                <div>
                    <div class="fw-semibold small">${c.nickname}</div>
                    <div class="small">${c.content}</div>
                </div>
                <div class="text-muted small">${formatDate(c.createdAt)}</div>
            </li>
        `);
    });
}

function getInterestCount() {
    const el = document.getElementById("interestCount");
    if (!el) return 0;

    const num = parseInt(el.textContent.replace(/[^0-9]/g, ""), 10);
    return isNaN(num) ? 0 : num;
}

function updateInterestButton(isInterested, currentCount) {
    const btn = document.getElementById("btnInterest");
    const icon = document.getElementById("interestIcon");
    const count = document.getElementById("interestCount");

    if (!btn || !icon || !count) return;

    if (isInterested) {
        btn.classList.remove("btn-outline-secondary");
        btn.classList.add("btn-danger");
        icon.textContent = "관심❤";
    } else {
        btn.classList.remove("btn-danger");
        btn.classList.add("btn-outline-secondary");
        icon.textContent = "관심♡";
    }

    count.textContent = `(${currentCount})`;
}


async function loadSharingDetail() {
    try {
        const res = await axios.get(`/api/sharing/${sharingId}`);
        const data = res.data;

        renderDetail(data);
        renderCarousel(data.images);
        bindCarouselEvents();
        updateActionButtons();

    } catch (err) {
        console.error("load detail error:", err);
        showAlert("게시글 정보를 불러오지 못했습니다.");
    }
}

async function loadComments() {
    try {
        const res = await axios.get(`/api/sharing/${sharingId}/comments`);
        renderComments(res.data);
    } catch (err) {
        console.error("load comments error:", err);
    }
}


function bindCarouselEvents() {
    document.querySelectorAll(".share-image").forEach(imgEl => {
        imgEl.addEventListener("click", () => {
            const url = imgEl.dataset.original;
            const modalImage = document.getElementById("modalImage");
            if (!modalImage) return;

            modalImage.src = url;
            const modal = new bootstrap.Modal(document.getElementById("imageModal"));
            modal.show();
        });
    });
}

function bindLoginUserNickname() {
    const nickname = document.body.dataset.memberNickname;
    const span = document.getElementById("loginUserNickname");
    if (span) {
        span.innerText = nickname;
    }
}

function updateActionButtons() {
    const loginId = Number(document.body.dataset.memberId);
    const writerId = Number(document.body.dataset.writerId);
    const status = document.body.dataset.sharingStatus;

    const myActions = document.getElementById("myActions");
    const otherActions = document.getElementById("otherActions");

    if (myActions) myActions.style.display = "none";
    if (otherActions) otherActions.style.display = "none";

    if (!loginId || !writerId) return;

    if (loginId === writerId) {
        if (myActions) {
            myActions.style.display = "flex";
        }

        const btnComplete = document.getElementById("btnComplete");
        if (btnComplete) {
            btnComplete.style.display = status === "false" ? "block" : "none";
        }

    } else {
        if (otherActions) {
            otherActions.style.display = "flex";
        }
    }
}

function bindActionButtons() {
    const btnComment = document.getElementById("btnCommentSubmit");
    const btnDelete = document.getElementById("btnDelete");
    const btnComplete = document.getElementById("btnComplete");
    const btnInterest = document.getElementById("btnInterest");
    const btnMessage = document.getElementById("btnMessage");

    if (btnComment) {
        btnComment.addEventListener("click", submitComment);
    }
    if (btnDelete) {
        btnDelete.addEventListener("click", deleteSharing);
    }
    if (btnComplete) {
        btnComplete.addEventListener("click", completeSharing);
    }
    if (btnInterest) {
        btnInterest.addEventListener("click", toggleInterest);
    }
    if (btnMessage) {
        btnMessage.addEventListener("click", openMessageModal);
    }
}

function bindMessageForm() {
    const form = document.getElementById("messageForm");
    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const senderId = Number(document.body.dataset.memberId);
        const receiverId = Number(document.body.dataset.writerId);
        const title = document.getElementById("msgTitle").value.trim();
        const content = document.getElementById("msgContent").value.trim();
        const targetId = Number(document.body.dataset.sharingId);

        if (!title || !content) {
            showAlert("제목과 내용을 모두 입력해주세요.");
            return;
        }

        try {
            await axios.post("/api/message/messageRegist", {
                senderId,
                receiverId,
                title,
                content,
                targetType: "SHARING",
                targetId
            });

            showAlert("쪽지를 보냈습니다!");

            const modalEl = document.getElementById("messageModal");
            const modalInstance = bootstrap.Modal.getInstance(modalEl);
            if (modalInstance) {
                modalInstance.hide();
            }

            document.getElementById("msgTitle").value = "";
            document.getElementById("msgContent").value = "";

        } catch (err) {
            console.error("쪽지 전송 오류:", err);
            showAlert("쪽지 전송에 실패했습니다. 다시 시도해주세요.");
        }
    });
}


// 댓글 등록
async function submitComment() {
    const content = document.getElementById("commentInput").value.trim();
    const memberId = Number(document.body.dataset.memberId);

    if (!content) {
        showAlert("댓글을 입력하세요.");
        return;
    }

    try {
        await axios.post("/api/sharing/comment", {
            sharingId,
            memberId,
            content
        });

        document.getElementById("commentInput").value = "";
        loadComments();

    } catch (err) {
        console.error("comment submit error:", err);
        showAlert("댓글 등록에 실패했습니다.");
    }
}

// 글 삭제
async function deleteSharing() {
    showModal("정말 삭제하시겠습니까?", async (confirm) => {
        if (!confirm) return;

        try {
            await axios.delete(`/api/sharing/${sharingId}`);

            showAlert("삭제되었습니다.", () => {
                window.location.href = "/sharingList";
            });

        } catch (err) {
            console.error("delete error:", err);
            showAlert("삭제에 실패했습니다.");
        }
    });
}

// 나눔 완료
async function completeSharing() {
    if (!confirm("나눔을 완료 처리하시겠습니까?")) return;

    try {
        await axios.put(`/api/sharing/${sharingId}/complete`);
        showAlert("완료 처리되었습니다.");
        loadSharingDetail();  // UI 갱신

    } catch (err) {
        console.error("complete error:", err);
        showAlert("나눔 완료 처리 실패");
    }
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

            if (!success) {
                showAlert("이미 관심 등록된 글입니다.");
                return;
            }

            currentCount += 1;
            updateInterestButton(true, currentCount);

        } else {
            const res = await axios.delete(`/api/sharing/${sharingId}/interest`, {
                params: { memberId }
            });
            const success = res.data === true;

            if (!success) {
                showAlert("이미 관심 해제된 글입니다.");
                return;
            }

            currentCount = Math.max(0, currentCount - 1);
            updateInterestButton(false, currentCount);
        }

    } catch (err) {
        console.error("interest toggle error:", err);
        showAlert("관심 처리 중 오류가 발생했습니다.");
    }
}

// 쪽지 모달 열기
function openMessageModal() {
    const writerNickname = document.getElementById("writerNickname").innerText;
    const msgTo = document.getElementById("msgTo");
    const msgPost = document.getElementById("msgPost");

    if (msgTo) msgTo.value = writerNickname;
    if (msgPost) msgPost.value = shareTitle || document.getElementById("shareTitle").innerText;

    const modal = new bootstrap.Modal(document.getElementById("messageModal"));
    modal.show();
}

function init() {
    bindLoginUserNickname();
    bindActionButtons();
    bindMessageForm();

    loadSharingDetail();
    loadComments();
}

document.addEventListener("DOMContentLoaded", init);
