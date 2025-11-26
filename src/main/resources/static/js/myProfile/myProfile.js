document.addEventListener("DOMContentLoaded", function () {

    let posts = [
        { writer: "장명훈", category: "나눔", title: "다육이 나눕니다.", date: "2025-11-08" },
        { writer: "장명훈", category: "질문", title: "물주는 주기 어떻게 할까요?", date: "2025-11-07" },
        { writer: "장명훈", category: "나눔", title: "선인장 드려요", date: "2025-11-05" },
        { writer: "장명훈", category: "질문", title: "흙 어떤 거 쓰세요?", date: "2025-11-04" },
        { writer: "장명훈", category: "나눔", title: "몬스테라 드립니다", date: "2025-11-03" },
        { writer: "장명훈", category: "질문", title: "햇빛 어느정도 필요한지", date: "2025-11-02" },
        { writer: "장명훈", category: "나눔", title: "다육이 나눕니다.", date: "2025-11-08" },
        { writer: "장명훈", category: "질문", title: "물주는 주기 어떻게 할까요?", date: "2025-11-07" },
        { writer: "장명훈", category: "나눔", title: "선인장 드려요", date: "2025-11-05" },
        { writer: "장명훈", category: "질문", title: "흙 어떤 거 쓰세요?", date: "2025-11-04" },
        { writer: "장명훈", category: "나눔", title: "몬스테라 드립니다", date: "2025-11-03" },
        { writer: "장명훈", category: "질문", title: "햇빛 어느정도 필요한지", date: "2025-11-02" },
        { writer: "장명훈", category: "나눔", title: "다육이 나눕니다.", date: "2025-11-08" },
        { writer: "장명훈", category: "질문", title: "물주는 주기 어떻게 할까요?", date: "2025-11-07" },
        { writer: "장명훈", category: "나눔", title: "선인장 드려요", date: "2025-11-05" },
        { writer: "장명훈", category: "질문", title: "흙 어떤 거 쓰세요?", date: "2025-11-04" },
        { writer: "장명훈", category: "나눔", title: "몬스테라 드립니다", date: "2025-11-03" },
        { writer: "장명훈", category: "질문", title: "햇빛 어느정도 필요한지", date: "2025-11-02" },
        { writer: "장명훈", category: "나눔", title: "다육이 나눕니다.", date: "2025-11-08" },
        { writer: "장명훈", category: "질문", title: "물주는 주기 어떻게 할까요?", date: "2025-11-07" },
        { writer: "장명훈", category: "나눔", title: "선인장 드려요", date: "2025-11-05" },
        { writer: "장명훈", category: "질문", title: "흙 어떤 거 쓰세요?", date: "2025-11-04" },
        { writer: "장명훈", category: "나눔", title: "몬스테라 드립니다", date: "2025-11-03" },
        { writer: "장명훈", category: "질문", title: "햇빛 어느정도 필요한지", date: "2025-11-02" },
        { writer: "장명훈", category: "나눔", title: "다육이 나눕니다.", date: "2025-11-08" },
        { writer: "장명훈", category: "질문", title: "물주는 주기 어떻게 할까요?", date: "2025-11-07" },
        { writer: "장명훈", category: "나눔", title: "선인장 드려요", date: "2025-11-05" },
        { writer: "장명훈", category: "질문", title: "흙 어떤 거 쓰세요?", date: "2025-11-04" },
        { writer: "장명훈", category: "나눔", title: "몬스테라 드립니다", date: "2025-11-03" },
        { writer: "장명훈", category: "질문", title: "햇빛 어느정도 필요한지", date: "2025-11-02" }
    ];

    const comments = [
        { writer: "강철수", category: "질문", title: "토분이 더 좋은가요?", date: "2025-11-08" },
        { writer: "김민정", category: "나눔", title: "화분 교환해요", date: "2025-11-07" },
        { writer: "홍길동", category: "질문", title: "다육이 건강상태?", date: "2025-11-05" },
        { writer: "이영희", category: "나눔", title: "모종 나눔합니다", date: "2025-11-04" },
        { writer: "강철수", category: "질문", title: "토분이 더 좋은가요?", date: "2025-11-08" },
        { writer: "김민정", category: "나눔", title: "화분 교환해요", date: "2025-11-07" },
        { writer: "홍길동", category: "질문", title: "다육이 건강상태?", date: "2025-11-05" },
        { writer: "이영희", category: "나눔", title: "모종 나눔합니다", date: "2025-11-04" },
        { writer: "강철수", category: "질문", title: "토분이 더 좋은가요?", date: "2025-11-08" },
        { writer: "김민정", category: "나눔", title: "화분 교환해요", date: "2025-11-07" },
        { writer: "홍길동", category: "질문", title: "다육이 건강상태?", date: "2025-11-05" },
        { writer: "이영희", category: "나눔", title: "모종 나눔합니다", date: "2025-11-04" },
        { writer: "강철수", category: "질문", title: "토분이 더 좋은가요?", date: "2025-11-08" },
        { writer: "김민정", category: "나눔", title: "화분 교환해요", date: "2025-11-07" },
        { writer: "홍길동", category: "질문", title: "다육이 건강상태?", date: "2025-11-05" },
        { writer: "이영희", category: "나눔", title: "모종 나눔합니다", date: "2025-11-04" },
        { writer: "강철수", category: "질문", title: "토분이 더 좋은가요?", date: "2025-11-08" },
        { writer: "김민정", category: "나눔", title: "화분 교환해요", date: "2025-11-07" },
        { writer: "홍길동", category: "질문", title: "다육이 건강상태?", date: "2025-11-05" },
        { writer: "이영희", category: "나눔", title: "모종 나눔합니다", date: "2025-11-04" }
    ];

    let currentTab = "myPosts";
    let currentPage = 1;
    const rowsPerPage = 10;

    const tableBody = document.querySelector("tbody");
    const pagination = document.querySelector(".pagination");
    const searchInput = document.querySelector(".form-control");
    const categorySelect = document.querySelector(".form-select");

    function getCurrentData() {
        return currentTab === "myPosts" ? posts : comments;
    }

    function filterData() {
        let keyword = searchInput.value.trim().toLowerCase();
        let category = categorySelect.value;

        return getCurrentData().filter(p =>
            (category === "전체" || p.category === category) &&
            (p.title.toLowerCase().includes(keyword))
        );
    }


    function renderTable() {
        let filtered = filterData();
        let start = (currentPage - 1) * rowsPerPage;
        let end = start + rowsPerPage;
        let sliced = filtered.slice(start, end);

        tableBody.innerHTML = sliced.map(p => `
        <tr>
            <td>
                <input type="checkbox" class="form-check-input row-check">
            </td>
                <td>${p.writer}</td>
                <td>${p.category}</td>
                <td class="text-start ps-4">${p.title}</td>
                <td>${p.date}</td>
        </tr>
        `).join("");

        renderPagination(filtered.length);
    }

    function renderPagination(total) {
        let pageCount = Math.ceil(total / rowsPerPage);
        pagination.innerHTML = "";

        let prevDisabled = (currentPage === 1) ? "disabled" : "";
        let nextDisabled = (currentPage === pageCount) ? "disabled" : "";

        pagination.innerHTML += `<li class="page-item ${prevDisabled}">
            <a class="page-link" data-page="prev">&laquo;</a></li>`;

        for (let i = 1; i <= pageCount; i++) {
            pagination.innerHTML += `
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" data-page="${i}">${i}</a>
                </li>`;
        }

        pagination.innerHTML += `<li class="page-item ${nextDisabled}">
            <a class="page-link" data-page="next">&raquo;</a></li>`;
    }

    pagination.addEventListener("click", function (e) {
        if (!e.target.dataset.page) return;

        if (e.target.dataset.page === "prev" && currentPage > 1) currentPage--;
        else if (e.target.dataset.page === "next") currentPage++;
        else currentPage = Number(e.target.dataset.page);

        renderTable();
    });

    searchInput.addEventListener("input", () => {
        currentPage = 1;
        renderTable();
    });

    categorySelect.addEventListener("change", () => {
        currentPage = 1;
        renderTable();
    });

    document.querySelectorAll('#tabs span').forEach(tab => {
        tab.addEventListener("click", function () {
            document.querySelectorAll('#tabs span').forEach(t => {
                t.classList.remove("tab-active");
                t.classList.add("text-secondary");
            });

            this.classList.add("tab-active");
            this.classList.remove("text-secondary");

            currentTab = this.getAttribute("data-tab");
            currentPage = 1;
            renderTable();
        });
    });

    searchInput.addEventListener("input", () => { currentPage = 1; renderTable(); });
    categorySelect.addEventListener("change", () => { currentPage = 1; renderTable(); });

    renderTable();

});