(function () {

    const state = {
        apiBase: '/api/weightManagement/list',
        keyword: '',
        offset: 0,
        limit: 10,
        range: 30,
        total: 0,
        items: [],

        rate: {
            initialSkillRate: 0,
            skillRateGrade1: 0,
            skillRateGrade2: 0,
            skillRateGrade3: 0,
            skillRateGrade4: 0,

            initialManagementRate: 0,
            managementRateGrade1: 0,
            managementRateGrade2: 0,
            managementRateGrade3: 0
        }
    };

    function validateRates(rate) {
        const {
            initialSkillRate,
            skillRateGrade1: s1,
            skillRateGrade2: s2,
            skillRateGrade3: s3,
            skillRateGrade4: s4,

            initialManagementRate,
            managementRateGrade1: m1,
            managementRateGrade2: m2,
            managementRateGrade3: m3
        } = rate;

        const all = [
            initialSkillRate, s1, s2, s3, s4,
            initialManagementRate, m1, m2, m3
        ];

        if (all.some(v => Number.isNaN(v))) {
            showAlert("모든 값은 숫자여야 합니다.");
            return false;
        }

        if (!(s1 <= s2 && s2 <= s3 && s3 <= s4)) {
            showAlert("숙련도는 S1 ≤ S2 ≤ S3 ≤ S4 여야 합니다.");
            return false;
        }

        if (initialSkillRate < s1 || initialSkillRate > s4) {
            showAlert("숙련도 초기값은 S1 이상 S4 이하여야 합니다.");
            return false;
        }

        if (!(m1 <= m2 && m2 <= m3)) {
            showAlert("요구관리도는 M1 ≤ M2 ≤ M3 여야 합니다.");
            return false;
        }

        if (initialManagementRate < m1 || initialManagementRate > m3) {
            showAlert("요구관리도 초기값은 M1 이상 M3 이하여야 합니다.");
            return false;
        }

        return true;
    }



    async function rateSave() {

        state.rate = {
            initialSkillRate: parseFloat(document.getElementById("initialSkill").value),
            skillRateGrade1: parseFloat(document.getElementById("skill1").value),
            skillRateGrade2: parseFloat(document.getElementById("skill2").value),
            skillRateGrade3: parseFloat(document.getElementById("skill3").value),
            skillRateGrade4: parseFloat(document.getElementById("skill4").value),

            initialManagementRate: parseFloat(document.getElementById("initialMng").value),
            managementRateGrade1: parseFloat(document.getElementById("mng1").value),
            managementRateGrade2: parseFloat(document.getElementById("mng2").value),
            managementRateGrade3: parseFloat(document.getElementById("mng3").value)
        };

        if (!validateRates(state.rate)) return;

        try {
            console.log(state.rate);
            await axios.post('/api/weightManagement/rate', state.rate);
            showAlert("숙련도/요구관리도 값이 저장되었습니다.");

        } catch (error) {
            console.error(error);
            showAlert("저장 중 오류가 발생했습니다.");
        }
    }

    async function initRate() {
        try {
            const res = await axios.get(`/api/weightManagement/rate`);
            const data = await res.data;

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
        }
        catch (err) {
            console.log(err);
        }
    }

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
        try {
            await fetchData();
            const careCounts = await loadCareCounts();
            state.items = state.items.map(m => ({
                ...m,
                plantsNeedingAttention: careCounts[m.memberId] ?? 0
            }));
            renderList(state.items);
            renderPager(goPage);
        } catch (error) {
            console.log(error);
        }
    }

    function goPage(p) {
        if (p < 1) return;
        const max = Math.ceil(state.total / state.limit);
        if (p > max) return;

        state.offset = (p - 1) * state.limit;
        refresh();
    }

async function loadLatestWeights() {
        try {
            const res = await axios.get("/api/weightManagement/latest");
            return res.data;
        }
        catch (err) {
            console.log(err);
        }
}

async function loadCareCounts() {
        try {
        const res = await axios.get("/api/weightManagement/careCounts");
        return res.data;
        } catch (error) {
            console.log(error);
        }
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
        await axios.post("/api/weightManagement/list", {
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
        document.getElementById("rateSaveBtn").addEventListener("click", rateSave)
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

        initRate();

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