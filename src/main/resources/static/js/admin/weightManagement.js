(function () {

    const state = {
        apiBase: '/api/weightManagement/list',
        keyword: '',
        offset: 0,
        limit: 10,
        range: 30,
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
        const s = state;
        const url = `${s.apiBase}`;

        const params = new URLSearchParams({
            keyword: s.keyword,
            range: s.range,
            offset: s.offset,
            limit: s.limit
        });

        const res = await axios.get(`${url}?${params.toString()}`);
        const { items, total } = normalizeResponse(res.data);

        s.items = items;
        s.total = total;
    }

    async function refresh() {
        await fetchData();
        const careCounts = await loadCareCounts();
        state.items = state.items.map(m => ({
            ...m,
            plantsNeedingAttention: careCounts[m.memberId] ?? 0
        }));
        renderList(state.items);
        renderPager(goPage);
    }

    function goPage(p) {
        if (p < 1) return;
        const max = Math.ceil(state.total / state.limit);
        if (p > max) return;

        state.offset = (p - 1) * state.limit;
        refresh();
    }

async function loadLatestWeights() {
    const res = await axios.get("/api/weightManagement/latest");
    return res.data;
}

async function loadCareCounts() {
        const res = await axios.get("/api/weightManagement/careCounts");
        return res.data;
    }

async function saveWeights() {
    const sw = Number(document.getElementById("searchWeightInput").value);
    const qw = Number(document.getElementById("questionWeightInput").value);

    const swReal = sw / 10;
    const qwReal = qw / 10;

    if (sw + qw !== 10) {
        showAlert("검색어 수 + 질문 수 합이 10이 되어야합니다.");
        return;
    }

    try {
        await axios.post("/api/weightManagement/save", {
            searchWeight: swReal,
            questionWeight: qwReal
        });

        showAlert("저장되었습니다!");
        refresh();

    } catch (err) {
        console.error(err);
        showAlert("저장 실패");
    }
}

function onSearch() {
    const input = document.getElementById("weightSearchInput");
    state.keyword = input.value;
    resetOffset();
    refresh();
}

function renderList(weights) {
    const tbody = document.getElementById("weightTableBody");
    if (!tbody) return;

    tbody.innerHTML = "";

    if (!weights || weights.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center py-4 text-muted">
                <i class="bi bi-box fs-2"></i>
                    데이터가 없습니다.
                </td>
            </tr>
        `;
        return;
    }

    weights.forEach(m => {
        tbody.innerHTML += `
            <tr class="user-row" data-member-id="${m.memberId}">
                <td>${m.membername}</td>
                <td>${m.nickname}</td>
                <td>${(m.searchWeight * 10).toFixed(0)}</td>
                <td>${(m.questionWeight * 10).toFixed(0)}</td>
                <td>${m.plantsNeedingAttention > 0 ? m.plantsNeedingAttention : '0'}</td>
            </tr>
        `;
    });
}

    function renderPager(goPage) {
        const ul = document.getElementById('pager');
        if (!ul) return;

        const { offset, limit, total } = state;
        const current = Math.floor(offset / limit) + 1;
        const totalPages = Math.max(1, Math.ceil(total / limit));

        function item(label, page, disabled, active, aria) {
            const li = document.createElement('li');
            li.className = `page-item${disabled ? ' disabled' : ''}${active ? ' active' : ''}`;

            const a = document.createElement('a');
            a.className = 'page-link';
            a.href = '#';
            if (aria) a.setAttribute('aria-label', aria);
            a.textContent = label;

            if (!disabled && !active) {
                a.addEventListener('click', e => {
                    e.preventDefault();
                    goPage(page);
                });
            } else {
                a.addEventListener('click', e => e.preventDefault());
            }

            li.appendChild(a);
            return li;
        }

        ul.innerHTML = '';

        ul.appendChild(item('«', 1, current === 1, false, '처음'));
        ul.appendChild(item('‹', current - 1, current === 1, false, '이전'));

        const windowSize = 5;
        const start = Math.floor((current - 1) / windowSize) * windowSize + 1;
        const end = Math.min(start + windowSize - 1, totalPages);

        for (let p = start; p <= end; p++) {
            ul.appendChild(item(p, p, false, p === current));
        }

        const isLast = current >= totalPages;
        ul.appendChild(item('›', current + 1, isLast, false, '다음'));
        ul.appendChild(item('»', totalPages, isLast, false, '마지막'));
    }

    document.addEventListener("DOMContentLoaded", async () => {
        try {
            const latest = await loadLatestWeights();
            if (latest) {
                const swInput = document.getElementById("searchWeightInput");
                const qwInput = document.getElementById("questionWeightInput");

                if (swInput) swInput.value = (latest.searchWeight * 10).toFixed(0);
                if (qwInput) qwInput.value = (latest.questionWeight * 10).toFixed(0);

            }
        } catch (e) {
            console.error("최신 추천 로딩 실패", e)
        }

        try {
            const res = await fetch(`/api/rate/`);
            const data = await res.json();

            if (!data) return;

            document.getElementById("initialSkill").value = data.initialSkillRate ?? "";
            document.getElementById("skill1").value = data.skillRateGrade1 ?? "";
            document.getElementById("skill2").value = data.skillRateGrade2 ?? "";
            document.getElementById("skill3").value = data.skillRateGrade3 ?? "";
            document.getElementById("skill4").value = data.skillRateGrade4 ?? "";

            document.getElementById("initialMng").value = data.initialManagementRate ?? "";
            document.getElementById("mng1").value = data.managementRateGrade1 ?? "";
            document.getElementById("mng2").value = data.managementRateGrade2 ?? "";
            document.getElementById("mng3").value = data.managementRateGrade3 ?? "";

        } catch (e) {
            console.log("불러오기 실패:", e);
        }

        const rangeSelect = document.getElementById("dateRangeSelect");
        if (rangeSelect) {
            state.range = rangeSelect.value;

            rangeSelect.addEventListener("change", () => {
                state.range = rangeSelect.value;
                resetOffset();
                refresh();
            });
        }

        const searchBtn = document.getElementById("weightSearchBtn");
        if (searchBtn) {
            searchBtn.addEventListener("click", onSearch);
        }

        const searchInput = document.getElementById("weightSearchInput");
        if (searchInput) {
            searchInput.addEventListener("keyup", (e) => {
                if (e.key === "Enter") onSearch();
            });
        }

        const saveBtn = document.getElementById("weightSaveBtn");
        if (saveBtn) {
            saveBtn.addEventListener("click", saveWeights);
        }

        refresh();
    });

})();