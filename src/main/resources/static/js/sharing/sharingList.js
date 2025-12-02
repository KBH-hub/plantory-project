let offset = 0;
const limit = 12;
let isLastPage = false;

function renderSharingList(list) {
    const container = document.getElementById("sharingListContainer");

    list.forEach(item => {
        const isEdited = item.updatedAt && item.updatedAt !== item.createdAt;
        const editedTag = isEdited ? " <span class='text-muted'>(수정됨)</span>" : "";

        const displayTime = isEdited ? timeAgo(item.updatedAt) : timeAgo(item.createdAt);

        const card = `
        <div class="col-12 col-sm-6 col-md-4"> 
            <a href="/readSharing/${item.sharingId}"
               class="card border-1 rounded-1 text-decoration-none"
               style="color:inherit;">

                <img src="${item.fileUrl}"
                     class="card-img-top object-fit-cover"
                     style="height:350px;"
                     alt="thumbnail">

                <div class="card-body px-2 py-2">

                    <div class="d-flex justify-content-between">
                        <span class="badge ${item.status === 'true' ? 'bg-secondary' : 'bg-success'} small">
                            ${item.status === 'true' ? '나눔완료' : '나눔 중'}
                        </span>
                    </div>

                    <div class="mt-1 mb-1 text-truncate small">
                        ${item.title}
                    </div>

                    <div class="d-flex justify-content-between small text-muted">
                        <span>${displayTime} ${editedTag}</span>
                        <span>
                            <i class="bi bi-chat"></i> ${item.commentCount}
                            <span class="ms-1">
                                <i class="bi bi-heart"></i> ${item.interestNum}
                            </span>
                        </span>
                    </div>

                </div>
            </a>
        </div>
        `;
        container.insertAdjacentHTML("beforeend", card);
    });
}

import { loadRecommendedSharings } from "/js/sharing/recommends.js";


async function loadSharingList(append = false) {
    const keyword = document.getElementById("keyword").value;
    const sido = document.getElementById("sido").value;
    const sigungu = document.getElementById("sigungu").value;

    let userAddress = null;
    if (sido && sigungu) userAddress = `${sido} ${sigungu}`;
    else if (sido) userAddress = sido;

    try {
        const res = await axios.get("/api/sharing", {
            params: { keyword, userAddress, limit, offset }
        });

        const list = res.data;

        if (!append) {
            document.getElementById("sharingListContainer").innerHTML = "";
        }

        renderSharingList(list);

        if (list.length < limit) {
            isLastPage = true;
            document.getElementById("btnLoadMore").style.display = "none";
        } else {
            isLastPage = false;
            document.getElementById("btnLoadMore").style.display = "block";
        }

    } catch (err) {
        console.error("Sharing list load error:", err);
    }
}


function renderPopularList(list) {
    const container = document.getElementById("popularListContainer");
    container.innerHTML = "";

    list.forEach(item => {
        const html = `
        <a href="/readSharing/${item.sharingId}" 
           class="d-flex justify-content-between align-items-center py-3 border-bottom text-decoration-none"
           style="color: inherit;">
           
            <span class="text-truncate" style="max-width: 160px;">
                ${item.title}
            </span>

            <span class="text-muted">
                <i class="bi bi-heart"></i> ${item.interestNum}
            </span>

        </a>`;

        container.insertAdjacentHTML("beforeend", html);
    });
}


async function loadPopularList() {
    try {
        const res = await axios.get("/api/sharing/popular");
        renderPopularList(res.data);
    } catch (err) {
        console.error("Popular list load error:", err);
    }
}

async function loadMyInterestCount() {
    const memberId = Number(document.body.dataset.memberId);

    try {
        const res = await axios.get("/api/sharing/countInterest", {
            params: { memberId }
        });

        document.getElementById("myInterestCount").innerHTML =
            `<i class="bi bi-heart"></i> ${res.data}`;

    } catch (err) {
        console.error("Interest count load error:", err);
    }
}

document.addEventListener("DOMContentLoaded", () => {

    loadSharingList(false);
    loadPopularList();
    loadMyInterestCount();
    loadRecommendedSharings("recommendedContainer");

    document.getElementById("btnSearch").addEventListener("click", () => {
        offset = 0;
        loadSharingList(false);
    });

    document.getElementById("keyword").addEventListener("keypress", e => {
        if (e.key === "Enter") {
            offset = 0;
            loadSharingList(false);
        }
    });

    document.getElementById("sido").addEventListener("change", () => {
        offset = 0;
        loadSharingList(false);
    });

    document.getElementById("sigungu").addEventListener("change", () => {
        offset = 0;
        loadSharingList(false);
    });

    document.getElementById("btnLoadMore").addEventListener("click", () => {
        if (isLastPage) return;

        offset += limit;
        loadSharingList(true);
    });
});
