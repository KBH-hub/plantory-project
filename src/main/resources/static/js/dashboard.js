document.addEventListener("DOMContentLoaded", loadDashboardCounts);

async function loadDashboardCounts() {
    const memberId = Number(document.body.dataset.memberId);

    try {
        const resMyPlants = await axios.get("/api/dashboard/countMyplants", {
            params: { memberId }
        });
        document.getElementById("countMyPlants").textContent = resMyPlants.data;

        const resWatering = await axios.get("/api/dashboard/countWatering", {
            params: { memberId }
        });
        document.getElementById("countWatering").textContent = resWatering.data;

        const resCareNeeded = await axios.get("/api/dashboard/countCareneeded", {
            params: { memberId }
        });
        document.getElementById("countCareneeded").textContent = resCareNeeded.data;

    } catch (err) {
        console.error("Dashboard count API error:", err);
    }
}


document.addEventListener("DOMContentLoaded", () => {
    loadDashboardCounts();
    loadRecommendedSharings();
});


async function loadRecommendedSharings() {
    try {
        const res = await axios.get("/api/dashboard/recommended");
        const list = res.data;

        const container = document.getElementById("recommendedContainer");
        container.innerHTML = ""; // 기존 정적 카드 제거

        list.forEach(item => {
            const card = `
                <a href="/readSharing/${item.sharingId}" 
                   class="text-decoration-none text-reset" 
                   style="width:350px;">
                    <div class="card shadow-sm h-100">
                    
                        <img src="${item.fileUrl}" 
                             alt="thumbnail" 
                             class="w-100"
                             style="height:375px; object-fit:cover;">
                             
                        <div class="card-body p-3">
                            <div class="fw-semibold text-truncate">${item.title}</div>
                            <div class="d-flex justify-content-between align-items-center mt-2">
                                <small class="text-muted">${timeAgo(item.createdAt)}</small>
                                <small class="text-muted">
                                    <i class="bi bi-chat me-1"></i>${item.commentCount}
                                    <i class="bi bi-heart ms-3 me-1"></i>${item.interestNum}
                                </small>
                            </div>
                        </div>
                    </div>
                </a>
            `;

            container.insertAdjacentHTML("beforeend", card);
        });

    } catch (err) {
        console.error("Recommended sharing load error:", err);
    }
}

function timeAgo(createdAt) {
    const now = new Date();
    const past = new Date(createdAt);
    const diffMs = now - past;

    const diffMinutes = Math.floor(diffMs / 60000);
    const diffHours   = Math.floor(diffMs / 3600000);
    const diffDays    = Math.floor(diffMs / 86400000);

    if (diffMinutes < 1) return "방금 전";
    if (diffMinutes < 60) return diffMinutes + "분 전";
    if (diffHours < 24)   return diffHours + "시간 전";
    if (diffDays < 7)     return diffDays + "일 전";

    const diffWeeks = Math.floor(diffDays / 7);
    return diffWeeks + "주 전";
}

document.addEventListener("DOMContentLoaded", () => {
    loadDashboardCounts();
    loadTodayWatering();
    loadTodayDiary();
});


// ====================================
//  오늘 물주기 리스트 렌더링
// ====================================
async function loadTodayWatering() {
    const memberId = Number(document.body.dataset.memberId);

    const container = document.getElementById("wateringListContainer");
    container.innerHTML = `<div class="text-muted small">불러오는 중...</div>`;

    try {
        const res = await axios.get("/api/dashboard/watering", {
            params: { memberId }
        });

        const list = res.data;

        if (list.length === 0) {
            container.innerHTML = `<div class="text-muted small">오늘 물 줄 식물이 없습니다.</div>`;
            return;
        }

        container.innerHTML = ""; // 초기화

        list.forEach(item => {
            const card = `
                <div class="p-2 mb-2 border rounded" style="background:#f5faff;">
                    <div class="fw-bold">${item.name}</div>
                    <div class="text-muted small">${item.cycleText}</div>
                    <span class="badge bg-dark float-end">오늘</span>
                </div>
            `;
            container.insertAdjacentHTML("beforeend", card);
        });

    } catch (err) {
        console.error("Watering load error:", err);
        container.innerHTML = `<div class="text-danger small">불러오기 실패</div>`;
    }
}


async function loadTodayDiary() {
    const memberId = Number(document.body.dataset.memberId);

    const container = document.getElementById("diaryListContainer");
    container.innerHTML = `<div class="text-muted small">불러오는 중...</div>`;

    try {
        const res = await axios.get("/api/dashboard/diary", {
            params: { memberId }
        });

        const list = res.data;

        if (list.length === 0) {
            container.innerHTML = `<div class="text-muted small">최근 작성된 관찰일지가 없습니다.</div>`;
            return;
        }

        container.innerHTML = "";

        list.forEach(item => {
            const imagePart = item.imageUrl
                ? `<img src="${item.imageUrl}" style="width:60px; height:60px; object-fit:cover;" class="rounded">`
                : `<div style="width:60px; height:60px; background:#eee;" class="rounded"></div>`;

            const card = `
                <div class="d-flex align-items-center p-2 mb-2 border rounded" style="background:#fff7e6;">
                    <div style="flex-grow:1;">
                        <div class="fw-bold">${item.myplantName}</div>
                        <div class="text-muted small text-truncate">${item.memo}</div>
                    </div>
                    <div class="ms-2">${imagePart}</div>
                    <span class="badge bg-dark ms-2">오늘</span>
                </div>
            `;

            container.insertAdjacentHTML("beforeend", card);
        });

    } catch (err) {
        console.error("Diary load error:", err);
        container.innerHTML = `<div class="text-danger small">불러오기 실패</div>`;
    }
}
