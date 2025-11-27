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

async function loadRecommendedSharings() {
    const template = document.getElementById("recommendedCardTemplate");
    const container = document.getElementById("recommendedContainer");

    try {
        const res = await axios.get("/api/dashboard/recommended");
        const list = res.data;

        container.innerHTML = "";

        list.forEach(item => {
            const clone = template.content.cloneNode(true);

            clone.querySelector("a").href = `/readSharing/${item.sharingId}`;
            clone.querySelector(".thumbnail").src = item.fileUrl;
            clone.querySelector(".title").textContent = item.title;
            clone.querySelector(".time").textContent = timeAgo(item.createdAt);

            clone.querySelector(".comment").textContent = item.commentCount;
            clone.querySelector(".interest").textContent = item.interestNum;

            container.appendChild(clone);
        });

    } catch (err) {
        console.error("Recommended sharing load error:", err);
    }
}



async function loadTodayWatering() {
    const memberId = Number(document.body.dataset.memberId);

    const container = document.getElementById("wateringListContainer");

    try {
        const res = await axios.get("/api/dashboard/watering", {
            params: { memberId }
        });

        const list = res.data;

        if (list.length === 0) {
            container.innerHTML = `<div class="text-muted small">오늘 물 줄 식물이 없습니다.</div>`;
            return;
        }

        container.innerHTML = "";

        list.forEach(item => {
            const card = `
                <div class="p-2 mb-2 border rounded position-relative" style="background:#f5faff;">
                    <span class="badge bg-dark position-absolute top-0 end-0 mt-2 me-2">오늘</span>
        
                    <div class="fw-bold">${item.name}</div>
                    <div class="text-muted small">${item.interval}일마다</div>
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
            const imagePart = item.fileUrl
                ? `<img src="${item.fileUrl}" 
                style="width:60px; height:60px; object-fit:cover;" 
                class="rounded">`
                : `<div style="width:60px; height:60px; background:#eee;" class="rounded"></div>`;

            const card = `
            <div class="d-flex align-items-center p-2 mb-2 border rounded position-relative"
                 style="background:#fff7e6;">
                 
              <!--  <span class="badge bg-dark position-absolute top-0 end-0 mt-2 me-2">오늘</span>-->
    
                <div style="flex-grow:1;">
                    <div class="fw-bold">${item.myplantName}</div>
                    <div class="text-muted small text-truncate" style="max-width:200px;">
                        ${item.memo ?? ""}
                    </div>
                </div>
    
                <div class="ms-2">${imagePart}</div>
            </div>
        `;

            container.insertAdjacentHTML("beforeend", card);
        });


    } catch (err) {
        console.error("Diary load error:", err);
        container.innerHTML = `<div class="text-danger small">불러오기 실패</div>`;
    }
}


document.addEventListener("DOMContentLoaded", () => {
    loadDashboardCounts();
    loadRecommendedSharings();
    loadTodayWatering();
    loadTodayDiary();
});
