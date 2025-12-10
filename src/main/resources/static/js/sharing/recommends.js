export function renderRecommendedCards(list, containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = "";

    list.forEach(item => {
        const stateBadge =
            item.status === "true"
                ? "<span class='badge bg-secondary small'>나눔완료</span>"
                : "<span class='badge bg-success small'>나눔 중</span>";

        const card = `
            <a href="/readSharing/${item.sharingId}"
               class="text-decoration-none text-reset"
               style="width:350px;">

                <div class="card shadow-sm h-100">
                    <img src="${item.fileUrl}"
                         class="w-100"
                         style="height:375px; object-fit:cover;">

                    <div class="card-body p-3">
                        <div class="fw-semibold text-truncate">${item.title}</div>

                        <div class="mt-1">${stateBadge}</div>

                        <div class="d-flex justify-content-between align-items-center mt-2">
                            <small class="text-muted">${window.timeAgo(item.createdAt)}</small> 
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
}