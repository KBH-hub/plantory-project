let currentTab = "profilePosts";
let currentPage = 1;
const rowsPerPage = 10;

const IS_ME = ProfileData.isMe;
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
    const writtenTbody = document.getElementById("profileWrittenTbody");
    const commentTbody = document.getElementById("profileCommentTbody");

    if (!writtenTbody || !commentTbody) return;

    writtenTbody.classList.add("d-none");
    commentTbody.classList.add("d-none");

    if (currentTab === "profilePosts") {
        writtenTbody.classList.remove("d-none");
    } else {
        commentTbody.classList.remove("d-none");
    }
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

document.addEventListener("change", (e) => {
    if (e.target.id === "checkAll") {
        const checked = e.target.checked;
        document.querySelectorAll(".row-check").forEach(chk => {
            chk.checked = checked;
        });
    }
});

async function loadProfile() {
    const res = await axios.get("/api/profile/picture");

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


async function handleDeleteWritten() {
    if (currentTab !== "profilePosts") {
        showAlert("댓글에서는 삭제할 수 없습니다.");
        return;
    }

    const selected = Array.from(document.querySelectorAll(".row-check:checked"));

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

    const payload = {
        memberId: PROFILE_ID,
        sharingIds,
        questionIds
    };

    console.log("삭제 요청:", payload);

    await axios.post("/api/profileWritten/softDelete", payload);

    showAlert("삭제되었습니다.");
    loadProfileWritten();
}



async function fetchProfileData() {
    try {
        if (IS_ME) {
            const res = await axios.get("/api/profile/me");
            return res.data;
        } else {
            const res = await axios.get(`/api/profile/publicProfile/${PROFILE_ID}`);
            return res.data;
        }
    } catch (e) {
        console.error(e);
        showAlert("프로필 정보를 불러오지 못했습니다.");
        return null;
    }
}

window.fetchProfileData = fetchProfileData;

async function initProfileInfo() {
    const data = await fetchProfileData();
    if (!data) return;

    if (IS_ME) {
        renderMyProfile(data);
    } else {
        renderPublicProfile(data);
        hideMyButtons();
    }

    await loadProfileWritten();
    initPagination();
}

function renderMyProfile(data) {
    document.getElementById("profileNickname").textContent = data.nickname;
    document.getElementById("profileAddress").textContent = data.address;
    document.getElementById("sharingRate").textContent = `나눔지수: ${data.sharingRate}%`;
}

function renderPublicProfile(data) {
    document.getElementById("profileNickname").textContent = data.nickname;
    document.getElementById("profileAddress").textContent = data.address ?? "비공개";
    document.getElementById("sharingRate").textContent = `나눔지수: ${data.sharingRate}%`;
}

function hideMyButtons() {
    const btnBox = document.getElementById("myButtons");
    if (btnBox) btnBox.classList.add("d-none");
}

function initButtons() {
    const updateBtn = document.getElementById("updateMyInfoBtn");
    const deleteBtn = document.getElementById("deleteProfileWrittenBtn");

    if (updateBtn) {
        updateBtn.addEventListener("click", () => {
            if (IS_ME) {
                window.location.href = `/profile/update/${PROFILE_ID}`;
            }
        });
    }

    if (deleteBtn) {
        deleteBtn.addEventListener("click", handleDeleteWritten);
    }
}

function getKeyword() {
    return document.querySelector(".input-group input").value.trim();
}

function getCategory() {
    return document.querySelector(".form-select").value;
}

async function loadProfileWritten(category = getCategory()) {
    const response = await axios.get(`/api/profileWritten/${PROFILE_ID}`, {
        params: {
            keyword: getKeyword(),
            category: category,
            limit: rowsPerPage,
            offset: (currentPage - 1) * rowsPerPage
        }
    });

    const { total, list } = response.data;

    content = list || [];

    renderTable();
    renderPagination(total);
}

function renderTable() {
    const tbody =
        currentTab === "profilePosts"
            ? document.getElementById("profileWrittenTbody")
            : document.getElementById("profileCommentTbody");

    if (!tbody) return;

    tbody.innerHTML = "";

    content.forEach(item => {
        tbody.innerHTML += `
            <tr>
                <td>
                       ${currentTab === "profilePosts" ? `<input type="checkbox" class="row-check" data-id="${item.id}" data-category="${item.category}">` : ``}
                </td>
                <td>${item.nickname}</td>
                <td>${categoryMap[item.category] || item.category}</td>
                <td>${item.title}</td>
                <td>${item.createdAt}</td>
            </tr>
        `;
        // console.log(item.id)
        console.log(item.category)
        bindRowClick();
    });
}
function bindRowClick() {
    document.querySelectorAll("#profileWrittenTbody tr").forEach(row => {
        row.addEventListener("click", (e) => {
            const checkbox = row.querySelector(".row-check");
            if (!checkbox) return;

            const id = checkbox.dataset.id;
            const category = checkbox.dataset.category;

            if (category === "SHARING") {
                window.location.href = `/readSharing/${id}`;
            } else if (category === "QUESTION") {
                window.location.href = `/readSharing/${id}`;
            }
        });
    });
}

function initTabs() {
    const tabs = document.querySelectorAll("#tabs span");
    const categorySelect = document.getElementById("categorySelect");

    tabs.forEach(tab => {
        tab.addEventListener("click", () => {
            tabs.forEach(t => t.classList.remove("tab-active", "text-dark"));
            tabs.forEach(t => t.classList.add("text-secondary"));

            tab.classList.add("tab-active", "text-dark");
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
    const searchInput = document.querySelector(".input-group input");
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

function initPagination() {
    const paginationContainer = document.querySelector(".pagination");
    if (!paginationContainer) return;

    paginationContainer.addEventListener("click", (e) => {
        const pageItem = e.target.closest(".page-item");
        if (!pageItem) return;

        const page = pageItem.dataset.page;
        if (!page) return;

        if (page === "prev" && currentPage > 1) {
            currentPage--;
        } else if (page === "next") {
            currentPage++;
        } else {
            currentPage = Number(page);
        }

        loadProfileWritten();
    });
}

function bindPaginationClick() {
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

function renderPagination(totalCount) {
    const pagination = document.getElementById("pagination");
    if (!pagination) return;

    const totalPages = Math.ceil(totalCount / rowsPerPage);

    pagination.innerHTML = "";

    pagination.innerHTML += `
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
            <a class="page-link" data-page="1">«</a>
        </li>
    `;

    pagination.innerHTML += `
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
    `;

    pagination.innerHTML += `
        <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
            <a class="page-link" data-page="${totalPages}">»</a>
        </li>
    `;

    bindPaginationClick();
    loadProfile()
}
