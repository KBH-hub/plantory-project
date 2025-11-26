document.addEventListener("DOMContentLoaded", loadDashboardCounts);

async function loadDashboardCounts() {
    const memberId = Number(document.body.dataset.memberId);
    // 디버깅용 로그 추가
    console.log("Raw memberId:", document.body.dataset.memberId);
    console.log("Parsed memberId:", memberId);

    if (!memberId || isNaN(memberId)) {
        console.error("memberId를 찾을 수 없거나 유효하지 않습니다.");
        return;
    }

    try {
        // ... 나머지 코드
    } catch (err) {
        console.error("Dashboard count API error:", err);
    }

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
