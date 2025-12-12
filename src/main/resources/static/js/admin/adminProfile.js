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
    initPagination();

    updateTableVisibility();
});

document.addEventListener("change", e => {
    if (e.target.id === "checkAll") {
        document.querySelectorAll(".row-check").forEach(chk => chk.checked = e.target.checked);
    }
});

async function fetchProfileData() {
    const res = await axios.get(`/api/profile/publicProfile/${PROFILE_ID}`);
    return res.data;
}

async function initProfileInfo() {
    const data = await fetchProfileData();
    renderProfile(data);
    await loadProfileImage();
    await loadProfileCounts();
    await loadProfileWritten();
}

function renderProfile(data) {
    document.getElementById("profileNickname").textContent = data.nickname;
    document.getElementById("profileAddress").textContent = data.address;
    document.getElementById("sharingRate").textContent = `나눔지수: ${Number(data.sharingRate).toFixed(2)}%`;
}

async function loadProfileImage() {
    const res = await axios.get("/api/profile/picture", { params: { memberId: PROFILE_ID } });

    const img = document.getElementById("profilePreview");
    const icon = document.getElementById("profileDefaultIcon");

    if (res.data.imageUrl) {
        img.src = res.data.imageUrl;
        img.style.display = "block";
        icon.style.display = "none";
    } else {
        img.style.display = "none";
        icon.style.display = "block";
    }
}

function initButtons() {
    document.getElementById("deleteProfileWrittenBtn")
        ?.addEventListener("click", handleDeleteWritten);
}

function getKeyword() {
    return document.getElementById("searchInput").value.trim();
}

function getCategory() {
    return document.getElementById("categorySelect").value;
}

async function loadProfileWritten(category = getCategory()) {
    const res = await axios.get(`/api/profileWritten/${PROFILE_ID}`, {
        params: {
            keyword: getKeyword(),
            category,
            limit: rowsPerPage,
            offset: (currentPage - 1) * rowsPerPage
        }
    });

    content = res.data.list || [];
    renderTable();
    renderPagination(res.data.total || 0);
}

function renderTable() {
    const tbody = currentTab === "profilePosts"
        ? document.getElementById("profileWrittenTbody")
        : document.getElementById("profileCommentTbody");

    tbody.innerHTML = "";

    if (content.length === 0) {
        tbody.innerHTML = `
        <tr>
            <td colspan="5" class="text-center py-5 text-muted">표시할 데이터가 없습니다.</td>
        </tr>`;
        return;
    }

    content.forEach(item => {
        tbody.innerHTML += `
        <tr>
            <td><input type="checkbox" class="row-check" data-id="${item.id}" data-category="${item.category}"></td>
            <td>${item.nickname}</td>
            <td>${categoryMap[item.category] || item.category}</td>
            <td>${item.title}</td>
            <td>${fmtKST(item.createdAt)}</td>
        </tr>`;
    });

    bindRowClick();
}

function bindRowClick() {
    document.querySelectorAll("#profileWrittenTbody tr").forEach(row => {
        const chk = row.querySelector(".row-check");

        chk.addEventListener("click", e => e.stopPropagation());

        row.addEventListener("click", () => {
            window.location.href = `/admin/readSharing/${chk.dataset.id}`;
        });
    });
}

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

function initSearchFilter() {
    document.getElementById("searchInput").addEventListener("input", () => {
        currentPage = 1;
        loadProfileWritten();
    });

    document.getElementById("categorySelect").addEventListener("change", e => {
        currentPage = 1;
        loadProfileWritten(e.target.value);
    });
}

function initPagination() {
    document.getElementById("pagination").addEventListener("click", e => {
        const page = Number(e.target.dataset.page);
        if (!page) return;
        currentPage = page;
        loadProfileWritten();
    });
}

function renderPagination(total) {
    const totalPages = Math.ceil(total / rowsPerPage);
    const ul = document.getElementById("pagination");
    ul.innerHTML = "";

    for (let i = 1; i <= totalPages; i++) {
        ul.innerHTML += `
        <li class="page-item ${i === currentPage ? "active" : ""}">
            <a class="page-link" data-page="${i}">${i}</a>
        </li>`;
    }
}

async function handleDeleteWritten() {
    const checked = [...document.querySelectorAll(".row-check:checked")];

    const sharingIds = [];
    const questionIds = [];

    checked.forEach(c => {
        if (c.dataset.category === "SHARING") sharingIds.push(+c.dataset.id);
        if (c.dataset.category === "QUESTION") questionIds.push(+c.dataset.id);
    });

    if (!sharingIds.length && !questionIds.length) {
        showAlert("삭제할 글이 없습니다.");
        return;
    }

    await axios.post("/api/profileWritten/softDelete", {
        memberId: PROFILE_ID,
        sharingIds,
        questionIds
    });

    showAlert("삭제되었습니다.");
    loadProfileWritten();
}

async function loadProfileCounts() {
    const res = await axios.get("/api/profileSharing/counts");
    document.getElementById("interestCount").textContent = `${res.data.interestCount}개`;
    document.getElementById("sharingHistoryCount").textContent = `${res.data.sharingCount}개`;
}

function fmtKST(iso) {
    return new Intl.DateTimeFormat("ko-KR", {
        timeZone: "Asia/Seoul",
        year: "numeric", month: "2-digit", day: "2-digit",
        hour: "2-digit", minute: "2-digit", second: "2-digit",
        hour12: false
    }).format(new Date(iso)).replace(/\./g, "-").replace(/\s/g, " ");
}
