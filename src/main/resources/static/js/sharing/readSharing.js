const sharingId = Number(document.body.dataset.sharingId);

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
    document.getElementById("shareTitle").innerText = detail.title;
    document.getElementById("shareCreated").innerText = timeAgo(detail.createdAt);

    document.getElementById("plantType").innerText = detail.plantType;
    document.getElementById("managementLevel").innerText = detail.managementLevel;
    document.getElementById("managementNeeds").innerText = detail.managementNeeds;

    document.getElementById("contentBox").innerHTML = detail.content;
    document.getElementById("writerNickname").innerText = detail.nickname;

    document.getElementById("btnUpdate").href = `/updateSharing/${sharingId}`;

    document.body.dataset.writerId = detail.memberId;
    document.body.dataset.sharingStatus = detail.status;

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


function bindCarouselEvents() {
    document.querySelectorAll(".share-image").forEach(imgEl => {
        imgEl.addEventListener("click", () => {
            const url = imgEl.dataset.original;
            const modalImage = document.getElementById("modalImage");
            modalImage.src = url;

            const modal = new bootstrap.Modal(document.getElementById("imageModal"));
            modal.show();
        });
    });
}

function bindActionButtons() {
    document.getElementById("btnCommentSubmit")
        .addEventListener("click", submitComment);

    document.getElementById("btnDelete")
        .addEventListener("click", deleteSharing);

    document.getElementById("btnComplete")
        .addEventListener("click", completeSharing);
}

function updateActionButtons() {
    const loginId = Number(document.body.dataset.memberId);
    const writerId = Number(document.body.dataset.writerId);
    const status = document.body.dataset.sharingStatus;

    const btnUpdate = document.getElementById("btnUpdate");
    const btnDelete = document.getElementById("btnDelete");
    const btnComplete = document.getElementById("btnComplete");

    btnUpdate.style.display = "none";
    btnDelete.style.display = "none";
    btnComplete.style.display = "none";

    if (loginId === writerId) {

        btnUpdate.style.display = "block";
        btnDelete.style.display = "block";

        if (status === "false") {
            btnComplete.style.display = "block";
        }
    }
}


function bindLoginUserNickname() {
    const nickname = document.body.dataset.memberNickname;
    document.getElementById("loginUserNickname").innerText = nickname
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
        alert("게시글 정보를 불러오지 못했습니다.");
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


async function submitComment() {
    const content = document.getElementById("commentInput").value.trim();
    const memberId = Number(document.body.dataset.memberId);

    if (!content) {
        alert("댓글을 입력하세요.");
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
        alert("댓글 등록에 실패했습니다.");
    }
}


async function deleteSharing() {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    try {
        await axios.delete(`/api/sharing/${sharingId}`);
        alert("삭제되었습니다.");
        location.href = "/sharingList";

    } catch (err) {
        console.error("delete error:", err);
        alert("삭제에 실패했습니다.");
    }
}


// --- 나눔 완료 ---
async function completeSharing() {
    if (!confirm("나눔을 완료 처리하시겠습니까?")) return;

    try {
        await axios.put(`/api/sharing/${sharingId}/complete`);
        alert("완료 처리되었습니다.");
        loadSharingDetail();  // UI 반영

    } catch (err) {
        console.error("complete error:", err);
        alert("나눔 완료 처리 실패");
    }
}


function init() {
    bindLoginUserNickname();
    bindActionButtons();

    loadSharingDetail();
    loadComments();
}

document.addEventListener("DOMContentLoaded", init);
