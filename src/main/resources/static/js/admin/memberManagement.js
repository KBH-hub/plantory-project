let limit = 8;
let offset = 0;
let totalCount = 0;

document.addEventListener("DOMContentLoaded", () => {
    loadMemberList();

    document.getElementById("memberSearchBtn").addEventListener("click", onSearch);

    document.getElementById("memberSearchInput").addEventListener("keyup", (e) => {
        if (e.key === "Enter") onSearch();
    });

    document.getElementById("memberTableBody").addEventListener("click", (e) => {
        const row = e.target.closest(".user-row");
        if (!row) return;

        const memberId = row.children[0].innerText;
        window.location.href = `/admin/profile/${memberId}`;
    });
});



function onSearch() {
    const keyword = document.getElementById("memberSearchInput").value.trim();
    loadMemberList(1, keyword);
}



async function loadMemberList() {
    const keyword = document.getElementById("memberSearchInput").value.trim();

    try {
        const res = await axios.get("/api/memberManagement/members", {
            params: {
                offset,
                limit,
                keyword
            }
        });

        const members = res.data.list;
        totalCount = res.data.totalCount;

        renderTable(members);
        renderPagination();

    } catch (err) {
        console.error(err);
        showAlert("회원 데이터를 불러오지 못했습니다.");
    }
}

function getRemainDays(stopDay) {
    if (!stopDay) return "-";

    const target = new Date(stopDay.replace(" ", "T"));

    const now = new Date();

    const diffMs = target - now;
    const diffDays = Math.ceil(diffMs / (1000 * 60 * 60 * 24));

    return diffDays > 0 ? diffDays + "일" : "-";
}




function renderTable(members) {
    const tbody = document.getElementById("memberTableBody");
    tbody.innerHTML = "";

    members.forEach(m => {
        tbody.innerHTML += `
            <tr class="user-row">
                <td>${m.memberId}</td>
                <td>${m.membername}</td>
                <td>${m.nickname}</td>
                <td>${m.phone}</td>
                <td>${m.address}</td>
                <td>${m.skillRate}%</td>
                <td>${m.managementRate}%</td>
                <td>${m.shareRate}%</td>
                <td>${getRemainDays(m.stopDay)}</td>
                <td>${m.createdAt}</td>
            </tr>
        `;
    });
}



function renderPagination() {
    const pagination = document.getElementById("pagination");
    pagination.innerHTML = "";

    const totalPage = Math.ceil(totalCount / limit);
    const currentPage = Math.floor(offset / limit) + 1;

    if (totalPage <= 1) return;

    const pageGroupSize = 5;

    const currentGroup = Math.ceil(currentPage / pageGroupSize);
    const groupStart = (currentGroup - 1) * pageGroupSize + 1;
    const groupEnd = Math.min(currentGroup * pageGroupSize, totalPage);

    const firstLi = document.createElement("li");
    firstLi.className = "page-item " + (currentGroup === 1 ? "disabled" : "");
    firstLi.innerHTML = `<a class="page-link" href="#">&laquo;&laquo;</a>`;
    firstLi.addEventListener("click", () => {
        if (currentGroup > 1) {
            const newPage = groupStart - pageGroupSize;
            offset = (newPage - 1) * limit;
            loadMemberList();
        }
    });
    pagination.appendChild(firstLi);

    const prevLi = document.createElement("li");
    prevLi.className = "page-item " + (currentPage === 1 ? "disabled" : "");
    prevLi.innerHTML = `<a class="page-link" href="#">&laquo;</a>`;
    prevLi.addEventListener("click", () => {
        if (currentPage > 1) {
            offset = (currentPage - 2) * limit;
            loadMemberList();
        }
    });
    pagination.appendChild(prevLi);

    for (let p = groupStart; p <= groupEnd; p++) {
        const li = document.createElement("li");
        li.className = "page-item " + (p === currentPage ? "active" : "");
        li.innerHTML = `<a class="page-link" href="#">${p}</a>`;
        li.addEventListener("click", () => {
            offset = (p - 1) * limit;
            loadMemberList();
        });
        pagination.appendChild(li);
    }

    const nextLi = document.createElement("li");
    nextLi.className = "page-item " + (currentPage === totalPage ? "disabled" : "");
    nextLi.innerHTML = `<a class="page-link" href="#">&raquo;</a>`;
    nextLi.addEventListener("click", () => {
        if (currentPage < totalPage) {
            offset = currentPage * limit;
            loadMemberList();
        }
    });
    pagination.appendChild(nextLi);

    const lastLi = document.createElement("li");
    lastLi.className = "page-item " + (groupEnd === totalPage ? "disabled" : "");
    lastLi.innerHTML = `<a class="page-link" href="#">&raquo;&raquo;</a>`;
    lastLi.addEventListener("click", () => {
        if (groupEnd < totalPage) {
            const newPage = groupEnd + 1;
            offset = (newPage - 1) * limit;
            loadMemberList();
        }
    });
    pagination.appendChild(lastLi);
}
