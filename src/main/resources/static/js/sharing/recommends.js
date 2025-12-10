export async function loadRecommendedSharings(containerId) {
    const sido = document.getElementById("sido").value;
    const sigungu = document.getElementById("sigungu").value;

    let userAddress = null;
    if (sido && sigungu) userAddress = `${sido} ${sigungu}`;
    else if (sido) userAddress = sido;

    try {
        // const res = await axios.get("/api/dashboard/recommendeds");

        const res = await axios.get("/api/sharing/popular", {
            params: { userAddress }
        });
        const list = res.data;

        const container = document.getElementById(containerId);
        if (!container) {
            console.log(`[loadRecommendedSharings] container ${containerId} 없음`);
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
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="badge ${item.status === 'true' ? 'bg-secondary' : 'bg-success'} small">
                                    ${item.status === 'true' ? '나눔완료' : '나눔 중'}
                                </span>
                            </div>

                            <div class="fw-semibold text-truncate">
                                ${item.title}
                            </div>

                            <div class="d-flex justify-content-between align-items-center mt-2">
                                <small class="text-muted">${timeAgo(item.createdAt)}</small>
                                <small class="text-muted">
                                    <i class="bi bi-chat me-1"></i>${item.commentCount}
                                    <i class="bi bi-heart me-1"></i>${item.interestNum}
                                </small>
                            </div>

                        </div>
                    </div>

                </a>
            `;
            container.insertAdjacentHTML("beforeend", card);
        });

    } catch (err) {
        console.error("Popular recommended load error:", err);
    }
}