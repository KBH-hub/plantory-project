export async function loadRecommendedSharings(containerId) {
    try {
        const res = await axios.get("/api/dashboard/recommendeds");
        const list = res.data;

        const container = document.getElementById(containerId);
        if (!container) {
            console.warn(`[loadRecommendedSharings] container ${containerId} 없음`);
            return;
        }

        container.innerHTML = "";

        list.forEach(item => {
            const card = `
                <a href="/readSharing/${item.sharingId}"
                   class="text-decoration-none text-reset"
                   style="width:350px;">

                    <div class="card shadow-sm h-100">
                        <img src="${item.fileUrl}"
                             class="w-100"
                             style="height:375px; object-fit:cover;">

                        <div class="card-body p-3">

                            <div class="fw-semibold text-truncate">
                                ${item.title}
                            </div>

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
