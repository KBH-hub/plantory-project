(function () {

    const state = {
        apiBase: '/api/memberManagement/members',
        keyword: '',
        offset: 0,
        limit: 10,
        total: 0,
        items: []
    };

    function normalizeResponse(items) {
        return {
            items,
            total: items[0]?.totalCount ?? 0
        };
    }

    function resetOffset() {
        state.offset = 0;
    }

    async function fetchData() {
        const { apiBase, keyword, offset, limit } = state;
console.log(state);
        const res = await axios.get(apiBase, {
            params: { keyword, offset, limit }
        });

        const { items, total } = normalizeResponse(res.data);
        state.items = items;
        state.total = total;
    }

    async function refresh() {
        try {
            await fetchData();
            renderTable();
            renderPagination();
        } catch (err) {
            console.error(err);
            showAlert("회원 데이터를 불러오지 못했습니다.");
            state.items = [];
            state.total = 0;
            renderTable();
            renderPagination();
        }
    }

    function goPage(page) {
        const maxPage = Math.max(1, Math.ceil(state.total / state.limit));
        if (page < 1 || page > maxPage) return;

        state.offset = (page - 1) * state.limit;
        refresh();
    }

    function onSearch() {
        state.keyword = document.getElementById("memberManagementSearchInput").value.trim();
        resetOffset();
        refresh();
    }

    function getRemainDays(stopDay) {
        if (!stopDay) return "-";
        const target = new Date(String(stopDay).replace(" ", "T"));
        const diffDays = Math.ceil((target - new Date()) / (1000 * 60 * 60 * 24));
        return diffDays > 0 ? `${diffDays}일` : "-";
    }

    function renderTable() {
        const tbody = document.getElementById("memberManagementTableBody");
        if (!tbody) return;

        tbody.innerHTML = "";

        if (!Array.isArray(state.items) || state.items.length === 0) {
            tbody.innerHTML = `
        <tr>
          <td colspan="10" class="text-muted py-4">표시할 회원이 없습니다.</td>
        </tr>
      `;
            return;
        }

        state.items.forEach(m => {
            tbody.innerHTML += `
        <tr class="user-row" style="cursor:pointer">
          <td>${m.memberId ?? ""}</td>
          <td>${m.membername ?? ""}</td>
          <td>${m.nickname ?? ""}</td>
          <td>${m.phone ?? ""}</td>
          <td>${m.address ?? ""}</td>
          <td>${m.skillRate ?? 0}%</td>
          <td>${m.managementRate ?? 0}%</td>
          <td>${m.sharingRate ?? 0}%</td>
          <td>${getRemainDays(m.stopDay)}</td>
          <td>${m.createdAt ? String(m.createdAt).replace("T", " ").substring(0, 16) : ""}</td>
        </tr>
      `;
        });
    }

    function renderPagination() {
        const ul = document.getElementById("pagination");
        if (!ul) return;

        ul.innerHTML = "";

        const totalPages = Math.max(1, Math.ceil(state.total / state.limit));
        const currentPage = Math.floor(state.offset / state.limit) + 1;

        if (totalPages <= 1) return;

        const windowSize = 5;
        const start = Math.floor((currentPage - 1) / windowSize) * windowSize + 1;
        const end = Math.min(start + windowSize - 1, totalPages);

        const item = (label, page, disabled, active = false) => {
            const li = document.createElement("li");
            li.className = `page-item${disabled ? " disabled" : ""}${active ? " active" : ""}`;
            li.innerHTML = `<a class="page-link" href="#">${label}</a>`;
            li.addEventListener("click", (e) => {
                e.preventDefault();
                if (!disabled && !active) goPage(page);
            });
            return li;
        };

        ul.appendChild(item("«", 1, currentPage === 1));
        ul.appendChild(item("‹", currentPage - 1, currentPage === 1));

        for (let p = start; p <= end; p++) {
            ul.appendChild(item(String(p), p, false, p === currentPage));
        }

        ul.appendChild(item("›", currentPage + 1, currentPage === totalPages));
        ul.appendChild(item("»", totalPages, currentPage === totalPages));
    }

    document.addEventListener("DOMContentLoaded", () => {
        refresh();

        const searchBtn = document.getElementById("memberManagementSearchBtn");
        const searchInput = document.getElementById("memberManagementSearchInput");
        const tbody = document.getElementById("memberManagementTableBody");

        if (searchBtn) searchBtn.addEventListener("click", onSearch);
        if (searchInput) {
            searchInput.addEventListener("keydown", (e) => {
                if (e.key === "Enter") {
                    e.preventDefault();
                    onSearch();
                }
            });
        }


        if (tbody) {
            tbody.addEventListener("click", (e) => {
                const row = e.target.closest(".user-row");
                if (!row) return;

                const memberId = row.children[0]?.innerText;
                if (!memberId) return;

                window.location.href = `/admin/profile/${memberId}`;
            });
        }
    });

})();
