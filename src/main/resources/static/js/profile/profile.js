let currentTab = "profilePosts";
let currentPage = 1;
const rowsPerPage = 10;
const IS_ME = ProfileData.isMe;
const PROFILE_ID = ProfileData.profileId;

let posts = [];
let comments = [];


document.addEventListener("DOMContentLoaded", async () => {
    await initProfileInfo();
    initTabs();
    initSearchFilter();
    initButtons();
});


async function initProfileInfo() {
    try {
        let response;

        if (IS_ME) {
            response = await axios.get("/api/profile/me");
            renderMyProfile(response.data);
        } else {
            response = await axios.get(`/api/profile/publicProfile/${PROFILE_ID}`);
            renderPublicProfile(response.data);

            hideMyButtons();
        }

        await loadProfilePosts();
        initPagination();

    } catch (error) {
        console.error(error);
        showAlert("프로필 정보를 불러오지 못했습니다.");
    }
}

function renderMyProfile(data) {
    document.getElementById("profileNickname").textContent = data.nickname;
    document.getElementById("profileAddress").textContent = data.address;
    document.getElementById("sharingRate").textContent = "나눔지수: " + data.sharingRate + "%";
}

function renderPublicProfile(data) {
    document.getElementById("profileNickname").textContent = data.nickname;
    document.getElementById("profileAddress").textContent = data.address ?? "비공개";
    document.getElementById("sharingRate").textContent = "나눔지수: " + data.sharingRate + "%";
}

function initButtons() {
    const updateBtn = document.getElementById("updateMyInfoBtn");

    if (updateBtn) {
        updateBtn.addEventListener("click", () => {
            if (IS_ME) {
                window.location.href = `/profile/update/${PROFILE_ID}`;
            }
        });
    }
}

function hideMyButtons() {
    const btnBox = document.getElementById("myButtons") || null;
    if (btnBox) btnBox.classList.add("d-none");
}


async function loadProfilePosts() {
    try {
        const response = await axios.get(`/api/posts/profile/${PROFILE_ID}`);
        posts = response.data;
        renderTable();
    } catch (e) {
        console.error(e);
    }
}

async function loadProfileComments() {
    try {
        const response = await axios.get(`/api/comments/profile/${PROFILE_ID}`);
        comments = response.data;
        renderTable();
    } catch (e) {
        console.error(e);
    }
}


function initTabs() {
    const tabs = document.querySelectorAll("#tabs span");

    tabs.forEach(tab => {
        tab.addEventListener("click", () => {
            tabs.forEach(t => t.classList.remove("tab-active", "text-dark"));
            tabs.forEach(t => t.classList.add("text-secondary"));

            tab.classList.add("tab-active", "text-dark");
            tab.classList.remove("text-secondary");

            currentTab = tab.dataset.tab;
            currentPage = 1;

            if (currentTab === "profilePosts") {
                loadProfilePosts();
            } else {
                loadProfileComments();
            }
        });
    });
}


function initSearchFilter() {
    const searchInput = document.querySelector(".input-group input");
    const categorySelect = document.querySelector(".form-select");

    searchInput.addEventListener("input", () => {
        currentPage = 1;
        renderTable();
    });

    categorySelect.addEventListener("change", () => {
        currentPage = 1;
        renderTable();
    });
}

function getCurrentData() {
    return currentTab === "profilePosts" ? posts : comments;
}

function filterData() {
    const searchInput = document.querySelector(".input-group input");
    const categorySelect = document.querySelector(".form-select");

    const keyword = searchInput.value.trim().toLowerCase();
    const category = categorySelect.value;

    return getCurrentData().filter(p =>
        (category === "전체" || p.category === category) &&
        p.title.toLowerCase().includes(keyword)
    );
}

function renderTable() {
    const tableBody = document.querySelector("tbody");
    tableBody.innerHTML = "";

    const data = filterData();
    const start = (currentPage - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const sliced = data.slice(start, end);

    sliced.forEach(item => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${item.writer}</td>
            <td>${item.category}</td>
            <td class="text-start">${item.title}</td>
            <td>${item.date}</td>
        `;
        tableBody.appendChild(row);
    });

    renderPagination(data.length);
}



function initPagination() {
    document.querySelector(".pagination").addEventListener("click", (e) => {
        if (!e.target.closest(".page-item") || e.target.classList.contains("disabled")) return;

        const btn = e.target.closest(".page-item");

        if (btn.dataset.page === "prev" && currentPage > 1) currentPage--;
        if (btn.dataset.page === "next") currentPage++;

        renderTable();
    });
}

function renderPagination(totalCount) {
    const pagination = document.querySelector(".pagination");
    const totalPages = Math.ceil(totalCount / rowsPerPage);

    pagination.innerHTML = `
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}" data-page="prev">
            <a class="page-link">&laquo;</a>
        </li>
        <li class="page-item disabled">
            <a class="page-link">${currentPage} / ${totalPages || 1}</a>
        </li>
        <li class="page-item ${currentPage === totalPages ? "disabled" : ""}" data-page="next">
            <a class="page-link">&raquo;</a>
        </li>
    `;
}
