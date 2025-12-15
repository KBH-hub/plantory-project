import { renderRecommendedCards } from "/js/sharing/recommends.js";

async function loadDashboard() {
    try {
        const res = await axios.get("/api/dashboard");
        const data = res.data;

        document.getElementById("countMyPlants").textContent = data.myPlantsCount ?? "-";

        document.getElementById("countWatering").textContent = data.todayWateringCount ?? "-";

        document.getElementById("countCareneeded").textContent = data.careNeededCount ?? "-";

        renderRecommendedCards(data.recommendeds ?? [], "recommendedContainer");

        renderTodayWatering(data.waterings ?? []);

        renderTodayDiary(data.diaries ?? []);

    } catch (err) {
        console.error("Dashboard load error:", err);
    }
}

function renderTodayWatering(list) {
    const container = document.getElementById("wateringListContainer");

    if (!list || list.length === 0) {
        container.innerHTML =
            `<div class="text-muted small">오늘 물 줄 식물이 없습니다.</div>`;
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
}

function renderTodayDiary(list) {
    const container = document.getElementById("diaryListContainer");

    if (!list || list.length === 0) {
        container.innerHTML =
            `<div class="text-muted small">최근 작성된 관찰일지가 없습니다.</div>`;
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
            <div class="d-flex align-items-center p-2 mb-2 border rounded"
                 style="background:#fff7e6;">
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
}

document.addEventListener("DOMContentLoaded", () => {
    loadDashboard();
});
