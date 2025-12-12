let currentTab = "profilePosts";
let currentPage = 1;
const rowsPerPage = 10;

const memberId = document.body.dataset.memberId;
const IS_ME = ProfileData?.isMe;
const PROFILE_ID = ProfileData?.profileId;

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

    if (currentTab === "profilePosts") writtenTbody.classList.remove("d-none");
    else commentTbody.classList.remove("d-none");

    const checkAll = document.getElementById("checkAll");
    if (checkAll) checkAll.checked = false;
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

document.addEventListener("change", (e) => {
    if (e.target?.id === "checkAll") {
        const checked = e.target.checked;
        document.querySelectorAll(".row-check").forEach(chk => chk.checked = checked);
    }
});

document.getElementById("interestArea")?.addEventListener("click", () => {
    window.location.href = `/profileInsert`;
});

document.getElementById("sharingHistoryArea")?.addEventListener("click", () => {
    window.location.href = `/profileSharingHistory?memberId=${memberId}`;
});

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

async function initProfileInfo() {
    const data = await fetchProfileData();
    if (!data) return;

    if (IS_ME) renderProfile(data);
    else {
        renderProfile(data);
        hideMyButtons();
    }

    await loadProfile();
    await loadProfileWritten();
    await loadProfileCounts();
}

function renderProfile(data) {
    document.getElementById("profileNickname").textContent = data.nickname ?? "";
    document.getElementById("profileAddress").textContent = data.address ?? "";
    // 숫자면 소수점 2자리로
    const rate = (typeof data.sharingRate === "number")
        ? data.sharingRate.toFixed(2)
        : (data.sharingRate ?? "0.00");
    document.getElementById("sharingRate").textContent = `나눔지수: ${rate}%`;
}

function hideMyButtons() {
    const btnBox = document.getElementById("myButtons");
    if (btnBox) btnBox.classList.add("d-none");
}

function initButtons() {
    const updateBtn = document.getElementById("updateMyInfoBtn");
    const deleteBtn = document.getElementById("deleteProfileWrittenBtn");

    updateBtn?.addEventListener("click", () => {
        if (IS_ME) window.location.href = `/profile/update/${PROFILE_ID}`;
    });

    deleteBtn?.addEventListener("click", handleDeleteWritten);
}

function getKeyword() {
    return document.getElementById("searchInput")?.value.trim() ?? "";
}

function getCategory() {
    return document.getElementById("categorySelect")?.value ?? "ALL";
}

async function loadProfile() {
    const res = await axios.get("/api/profile/picture", {
        params: { memberId: PROFILE_ID }
    });

    const profileImg = document.getElementById("profilePreview");
    const defaultIcon = document.getElementById("profileDefaultIcon");

    if (res.data?.imageUrl) {
        profileImg.src = res.data.imageUrl;
        profileImg.style.display = "block";
        defaultIcon.style.display = "none";
    } else {
        profileImg.style.display = "none";
        defaultIcon.style.display = "block";
    }
}

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
    renderPagination(total || 0);
}

function renderTable() {
    const tbody = (currentTab === "profilePosts")
        ? document.getElementById("profileWrittenTbody")
        : document.getElementById("profileCommentTbody");

    if (!tbody) return;
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
          ${currentTab === "profilePosts"
            ? `<input type="checkbox" class="row-check" data-id="${item.id}" data-category="${item.category}">`
            : ``}
        </td>
        <td>${item.nickname ?? ""}</td>
        <td>${categoryMap[item.category] || item.category}</td>
        <td>${item.title ?? ""}</td>
        <td>${fmtKST(item.createdAt)}</td>
      </tr>
    `;
    });

    bindRowClick();
}

function bindRowClick() {
    // 쓴 글 탭에서만 row click 이동
    if (currentTab !== "profilePosts") return;

    document.querySelectorAll("#profileWrittenTbody tr").forEach(row => {
        const checkbox = row.querySelector(".row-check");

        checkbox?.addEventListener("click", (e) => e.stopPropagation());

        row.addEventListener("click", () => {
            if (!checkbox) return;

            const id = checkbox.dataset.id;
            const category = checkbox.dataset.category;

            if (category === "SHARING") window.location.href = `/readSharing/${id}`;
            else if (category === "QUESTION") window.location.href = `/readSharing/${id}`;
        });
    });
}

function fmtKST(iso) {
    if (!iso) return "";
    return new Intl.DateTimeFormat("ko-KR", {
        timeZone: "Asia/Seoul",
        year: "numeric", month: "2-digit", day: "2-digit",
        hour: "2-digit", minute: "2-digit", second: "2-digit",
        hour12: false
    })
        .format(new Date(iso))
        .replace(/\./g, "-")
        .replace(/-\s/g, "-")
        .replace(/\s/g, " ");
}

function initTabs() {
    const tabs = document.querySelectorAll("[data-tab]");
    const categorySelect = document.getElementById("categorySelect");

    tabs.forEach(tab => {
        tab.addEventListener("click", async () => {
            tabs.forEach(t => {
                t.classList.remove("tab-active", "text-dark");
                t.classList.add("text-secondary");
            });

            tab.classList.add("tab-active", "text-dark");
            tab.classList.remove("text-secondary");

            currentTab = tab.dataset.tab;
            currentPage = 1;

            updateTableVisibility();

            if (currentTab === "profilePosts") {
                categorySelect.innerHTML = writtenOptions;
                categorySelect.value = "ALL";
                await loadProfileWritten("ALL");
            } else {
                categorySelect.innerHTML = commentOptions;
                categorySelect.value = "COMMENT_ALL";
                await loadProfileWritten("COMMENT_ALL");
            }
        });
    });
}

function initSearchFilter() {
    const searchInput = document.getElementById("searchInput");
    const categorySelect = document.getElementById("categorySelect");

    searchInput?.addEventListener("input", () => {
        currentPage = 1;
        loadProfileWritten(categorySelect.value);
    });

    categorySelect?.addEventListener("change", (e) => {
        currentPage = 1;
        loadProfileWritten(e.target.value);
    });
}

function initPagination() {
    const paginationContainer = document.getElementById("pagination");
    if (!paginationContainer) return;

    paginationContainer.addEventListener("click", (e) => {
        const link = e.target.closest(".page-link");
        if (!link) return;

        const page = Number(link.dataset.page);
        if (!page || Number.isNaN(page)) return;

        currentPage = page;
        loadProfileWritten();
    });
}

function renderPagination(totalCount) {
    const pagination = document.getElementById("pagination");
    if (!pagination) return;

    const totalPages = Math.max(1, Math.ceil(totalCount / rowsPerPage));
    pagination.innerHTML = "";

    pagination.innerHTML += `
    <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
      <a class="page-link" data-page="1">«</a>
    </li>
  `;

    pagination.innerHTML += `
    <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
      <a class="page-link" data-page="${Math.max(1, currentPage - 1)}">‹</a>
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
      <a class="page-link" data-page="${Math.min(totalPages, currentPage + 1)}">›</a>
    </li>
  `;

    pagination.innerHTML += `
    <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
      <a class="page-link" data-page="${totalPages}">»</a>
    </li>
  `;
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

    await axios.post("/api/profileWritten/softDelete", {
        memberId: PROFILE_ID,
        sharingIds,
        questionIds
    });

    showAlert("삭제되었습니다.");

    document.getElementById("checkAll").checked = false;
    document.querySelectorAll(".row-check").forEach(chk => chk.checked = false);

    loadProfileWritten();
}

async function loadProfileCounts() {
    try {
        const res = await axios.get("/api/profileSharing/counts");
        document.getElementById("interestCount").textContent = `${res.data.interestCount ?? 0}개`;
        document.getElementById("sharingHistoryCount").textContent = `${res.data.sharingCount ?? 0}개`;
    } catch (err) {
        console.error("카운트 로딩 실패", err);
    }
}
