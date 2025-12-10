let currentTab = "profilePosts";
let currentPage = 1;
const rowsPerPage = 10;

const PROFILE_ID = ProfileData.profileId;

let content = [];

const categoryMap = {
    SHARING: "나눔",
    QUESTION: "질문",
    COMMENT: "나눔댓글",
    ANSWER: "질문답글"
};

const writtenOptions = `
    <option value="ALL">전체</option>
    <option value="SHARING">나눔</option>
    <option value="QUESTION">질문</option>
`;

const commentOptions = `
    <option value="COMMENT_ALL">전체댓글</option>
    <option value="COMMENT">나눔댓글</option>
    <option value="ANSWER">질문답글</option>
`;

function updateTableVisibility() {
    document.getElementById("profileWrittenTbody").classList.toggle("d-none", currentTab !== "profilePosts");
    document.getElementById("profileCommentTbody").classList.toggle("d-none", currentTab !== "profileComments");
}

document.addEventListener("DOMContentLoaded", async () => {
    const categorySelect = document.getElementById("categorySelect");
    categorySelect.innerHTML = writtenOptions;
    categorySelect.value = "ALL";

    await initProfileInfo();
    initTabs();
    initSearchFilter();
    initButtons();

    updateTableVisibility();
});

/* 전체 선택 체크박스 */
document.addEventListener("change", (e) => {
    if (e.target.id === "checkAll") {
        document.querySelectorAll(".row-check").forEach(chk => {
            chk.checked = e.target.checked;
        });
    }
});

/* 프로필 이미지 로드 */
async function loadProfile() {
    const res = await axios.get("/api/profile/picture", {
        params: { memberId: PROFILE_ID }
    });

    const profileImg = document.getElementById("profilePreview");
    const defaultIcon = document.getElementById("profileDefaultIcon");

    if (res.data.imageUrl) {
        profileImg.src = res.data.imageUrl;
        profileImg.style.display = "block";
        defaultIcon.style.display = "none";
    } else {
        profileImg.style.display = "none";
        defaultIcon.style.display = "block";
    }
}

/* 글 삭제 */
async function handleDeleteWritten() {
    if (currentTab !== "profilePosts") {
        showAlert("댓글에서는 삭제할 수 없습니다.");
        return;
    }

    const selected = [...document.querySelectorAll(".row-check:checked")];

    const sharingIds = [];
    const questionIds = [];

    selected.forEach(chk => {
        const id = Number(chk.dataset.id);
        const category = chk.dataset.category;

        if (category === "SHARING") sharingIds.push(id);
        if (category === "QUESTION") questionIds.push(id);
    });

    if (sharingIds.length === 0 && questionIds.length === 0) {
        showAlert("삭제할 글이 없습니다.");
        return;
    }

    await axios.post("/api/profileWritten/softDelete", {
        memberId: PROFILE_ID,
        sharingIds,
        questionIds
    });

    showAlert("삭제되었습니다.");

    document.getElementById("checkAll").checked = false;

    loadProfileWritten();
}

/* 프로필 데이터 */
async function fetchProfileData() {
    try {
        const res = await axios.get(`/api/profile/publicProfile/${PROFILE_ID}`);
        return res.data;
    } catch (e) {
        console.error(e);
        showAlert("프로필 정보를 불러오지 못했습니다.");
        return null;
    }
}

async function initProfileInfo() {
    const data = await fetchProfileData();
    if (!data) return;

    renderPublicProfile(data);

    await loadProfileCounts();
    await loadProfileWritten();
}

function renderPublicProfile(data) {
    document.getElementById("profileNickname").textContent = data.nickname;
    document.getElementById("profileAddress").textContent = data.address;
    document.getElementById("sharingRate").textContent = `나눔지수: ${data.sharingRate}%`;
}

function initButtons() {
    const deleteBtn = document.getElementById("deleteProfileWrittenBtn");
    deleteBtn?.addEventListener("click", handleDeleteWritten);
}
function fmtKST(iso) {
    if (!iso) return '';
    return new Intl.DateTimeFormat('ko-KR', {
        timeZone: 'Asia/Seoul',
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit', second: '2-digit',
        hour12: false
    }).format(new Date(iso))
        .replace(/\./g, '-')
        .replace(/-\s/g, '-')
        .replace(/\s/g, ' ');
}

/* 검색 input selector 수정됨 */
function getKeyword() {
    return document.getElementById("searchInput").value.trim();
}

function getCategory() {
    return document.getElementById("categorySelect").value;
}

/* 글/댓글 로드 */
async function loadProfileWritten(category = getCategory()) {
    const response = await axios.get(`/api/profileWritten/${PROFILE_ID}`, {
        params: {
            keyword: getKeyword(),
            category,
            limit: rowsPerPage,
            offset: (currentPage - 1) * rowsPerPage
        }
    });

    const { total, list } = response.data;

    content = list || [];

    renderTable();
    renderPagination(total);
}

/* 테이블 렌더링 */
function renderTable() {
    const tbody = currentTab === "profilePosts"
        ? document.getElementById("profileWrittenTbody")
        : document.getElementById("profileCommentTbody");

    tbody.innerHTML = "";

    if (content.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5">
                    <div class="text-center text-muted py-5">
                        <i class="bi bi-box fs-3"></i><br>
                        표시할 데이터가 없습니다.
                    </div>
                </td>
            </tr>
        `;
        return;
    }

    content.forEach(item => {
        tbody.innerHTML += `
            <tr>
                <td>
                    <input type="checkbox" class="row-check" 
                           data-id="${item.id}" 
                           data-category="${item.category}">
                </td>
                <td>${item.nickname}</td>
                <td>${categoryMap[item.category] || item.category}</td>
                <td>${item.title}</td>
                <td>${fmtKST(item.createdAt)}</td>
            </tr>
        `;
    });

    bindRowClick();
}


async function loadProfileCounts() {
    try {
        const res = await axios.get("/api/profileSharing/counts");

        document.getElementById("interestCount").textContent =
            `${res.data.interestCount}개`;

        document.getElementById("sharingHistoryCount").textContent =
            `${res.data.sharingCount}개`;

    } catch (err) {
        console.error("카운트 로딩 실패", err);
    }
}


/* 글 클릭 이동 */
function bindRowClick() {
    document.querySelectorAll("#profileWrittenTbody tr").forEach(row => {

        row.addEventListener("click", () => {
            const checkbox = row.querySelector(".row-check");
            if (!checkbox) return;

            const id = checkbox.dataset.id;
            const category = checkbox.dataset.category;

            window.location.href = `/admin/readSharing/${id}`;
        });

        const checkbox = row.querySelector(".row-check");
        if (checkbox) {
            checkbox.addEventListener("click", (e) => e.stopPropagation());
        }
    });
}

/* 탭 이벤트 — 새 UI와 매칭 */
function initTabs() {
    const tabs = document.querySelectorAll("#profileTabs span[data-tab]");
    const categorySelect = document.getElementById("categorySelect");

    tabs.forEach(tab => {
        tab.addEventListener("click", () => {

            tabs.forEach(t => t.classList.remove("fw-semibold", "text-dark"));
            tabs.forEach(t => t.classList.add("text-secondary"));

            tab.classList.add("fw-semibold", "text-dark");
            tab.classList.remove("text-secondary");

            currentTab = tab.dataset.tab;
            currentPage = 1;

            updateTableVisibility();

            if (currentTab === "profilePosts") {
                categorySelect.innerHTML = writtenOptions;
                categorySelect.value = "ALL";
                loadProfileWritten("ALL");
            } else {
                categorySelect.innerHTML = commentOptions;
                categorySelect.value = "COMMENT_ALL";
                loadProfileWritten("COMMENT_ALL");
            }
        });
    });
}

/* 검색 필터 */
function initSearchFilter() {
    const searchInput = document.getElementById("searchInput");
    const categorySelect = document.getElementById("categorySelect");

    searchInput.addEventListener("input", () => {
        currentPage = 1;
        loadProfileWritten(categorySelect.value);
    });

    categorySelect.addEventListener("change", (e) => {
        currentPage = 1;
        loadProfileWritten(e.target.value);
    });
}

/* 페이징 렌더링 — 충돌 제거 후 최종 버전 */
function renderPagination(totalCount) {
    const pagination = document.getElementById("pagination");
    const totalPages = Math.ceil(totalCount / rowsPerPage);

    pagination.innerHTML = "";

    pagination.innerHTML += `
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
            <a class="page-link" data-page="1">«</a>
        </li>
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
            <a class="page-link" data-page="${currentPage - 1}">‹</a>
        </li>
    `;

    for (let i = 1; i <= totalPages; i++) {
        pagination.innerHTML += `
            <li class="page-item ${currentPage === i ? "active" : ""}">
                <a class="page-link" data-page="${i}">${i}</a>
            </li>
        `;
    }

    pagination.innerHTML += `
        <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
            <a class="page-link" data-page="${currentPage + 1}">›</a>
        </li>
        <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
            <a class="page-link" data-page="${totalPages}">»</a>
        </li>
    `;

    /* 단일 페이징 클릭 이벤트 */
    document.querySelectorAll("#pagination .page-link").forEach(btn => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            const page = Number(e.target.dataset.page);
            if (page < 1) return;

            currentPage = page;
            loadProfileWritten();
        });
    });
}
