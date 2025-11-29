// /js/myPlant/plantCalendar.js
document.addEventListener("DOMContentLoaded", function () {
    const calendarGrid = document.querySelector(".calendar-grid");
    const monthLabel = document.getElementById("monthLabel");
    const prevBtn = document.getElementById("prevMonth");
    const nextBtn = document.getElementById("nextMonth");

    const imageModalEl = document.getElementById("imageModal");
    const imageInputEl = document.getElementById("imageInput");
    const previewImageEl = document.getElementById("previewImage");
    const addPhotoBtnEl = document.getElementById("addPhotoBtn");
    const photoListEl = document.getElementById("photoList");
    const saveDiaryBtnEl = document.getElementById("saveDiaryBtn");
    const diaryListContainerEl = document.getElementById("diaryListContainer");

    let current = new Date();
    let currentYear = current.getFullYear();
    let currentMonth = current.getMonth() + 1;

    let diaryList = [];
    let photoFiles = [];
    let selectedDate = null;
    let lastRequestedYmd = null;
    let isRendering = false;

    calendarGrid.addEventListener("click", (e) => {
        const cell = e.target.closest(".calendar-cell[data-date]");
        if (!cell || !calendarGrid.contains(cell) || cell.classList.contains("empty")) return;
        const ymd = cell.dataset.date;
        selectedDate = ymd;
        calendarGrid.querySelectorAll(".calendar-cell.selected").forEach(el => el.classList.remove("selected"));
        cell.classList.add("selected");
        renderDayPanels(ymd);
    });

    const pad2 = (n) => String(n).padStart(2, "0");

    function ymdFromDate(d) {
        return `${d.getFullYear()}-${pad2(d.getMonth() + 1)}-${pad2(d.getDate())}`;
    }

    function selectDate(ymd, {focus = false, scroll = false} = {}) {
        calendarGrid.querySelectorAll(".calendar-cell.selected").forEach(el => el.classList.remove("selected"));
        const cell = calendarGrid.querySelector(`.calendar-cell[data-date="${ymd}"]`);
        if (!cell) return false;
        cell.classList.add("selected");
        if (focus) cell.focus?.();
        if (scroll) cell.scrollIntoView?.({block: "nearest"});
        selectedDate = ymd;
        return true;
    }

    function buildMonthRangeISO(year, month) {
        const start = `${year}-${pad2(month)}-01T00:00:00`;
        const nextMonth = month === 12 ? 1 : month + 1;
        const nextYear = month === 12 ? year + 1 : year;
        const end = `${nextYear}-${pad2(nextMonth)}-01T00:00:00`;
        return {start, end};
    }

    function buildDayRangeISO(ymd) {
        return {start: `${ymd}T00:00:00`, end: `${ymd}T23:59:59`};
    }

    function toLocalYmd(iso) {
        if (!iso) return null;
        const d = new Date(iso);
        if (isNaN(d)) return null;
        const y = d.getFullYear();
        const m = d.getMonth() + 1;
        const day = d.getDate();
        return `${y}-${pad2(m)}-${pad2(day)}`;
    }

    function toLocalHm(iso) {
        if (!iso) return "";
        const d = new Date(iso);
        if (isNaN(d)) return "";
        const hh = String(d.getHours()).padStart(2, "0");
        const mm = String(d.getMinutes()).padStart(2, "0");
        return `${hh}:${mm}`;
    }

    async function renderCalendar(year, month) {
        calendarGrid.innerHTML = "";
        const firstDay = new Date(year, month - 1, 1).getDay();
        const lastDate = new Date(year, month, 0).getDate();
        monthLabel.innerText = `${year}년 ${month}월`;

        for (let i = 0; i < firstDay; i++) {
            calendarGrid.insertAdjacentHTML("beforeend", `<div class="calendar-cell empty"></div>`);
        }

        for (let day = 1; day <= lastDate; day++) {
            const ymd = `${year}-${pad2(month)}-${pad2(day)}`;
            calendarGrid.insertAdjacentHTML(
                "beforeend",
                `<div class="calendar-cell" data-date="${ymd}" role="button" tabindex="0" aria-label="${ymd}">
           <span>${day}</span>
         </div>`
            );
        }

        await loadAndMark(year, month);
    }

    async function loadAndMark(year, month) {
        try {
            const {start, end} = buildMonthRangeISO(year, month);
            const memberId = Number(document.body.dataset.memberId || 0);
            if (!memberId) console.warn("memberId가 유효하지 않습니다:", document.body.dataset.memberId);

            const [resDiary, resWater] = await Promise.all([
                axios.get("/api/plantingCalender/diary", {
                    params: {memberId, startDate: start, endDate: end},
                    headers: {Accept: "application/json"}
                }),
                axios.get("/api/plantingCalender/watering", {
                    params: {memberId, startDate: start, endDate: end},
                    headers: {Accept: "application/json"}
                })
            ]);

            const diaryList = Array.isArray(resDiary.data) ? resDiary.data : [];
            const wateringList = Array.isArray(resWater.data) ? resWater.data : [];

            const diaryCountByYmd = diaryList.reduce((acc, item) => {
                const ymd = toLocalYmd(item?.createdAt);
                if (ymd) acc[ymd] = (acc[ymd] || 0) + 1;
                return acc;
            }, {});

            const wateringCountByYmd = wateringList.reduce((acc, item) => {
                const ymd = toLocalYmd(item?.dateAt);
                if (ymd) acc[ymd] = (acc[ymd] || 0) + 1;
                return acc;
            }, {});

            const dayCells = calendarGrid.querySelectorAll(".calendar-cell[data-date]");
            dayCells.forEach((cell) => {
                const ymd = cell.dataset.date;
                cell.classList.remove("has-diary", "has-watering");
                let wrap = cell.querySelector(".badge-wrap");
                if (!wrap) {
                    wrap = document.createElement("div");
                    wrap.className = "badge-wrap";
                    cell.appendChild(wrap);
                }
                wrap.innerHTML = "";
                const dCnt = diaryCountByYmd[ymd] || 0;
                const wCnt = wateringCountByYmd[ymd] || 0;
                if (dCnt > 0) {
                    cell.classList.add("has-diary");
                    wrap.insertAdjacentHTML(
                        "beforeend",
                        `<div class="badge-diary" title="관찰일지 ${dCnt}건"><b>관찰일지 ${dCnt}</b></div>`
                    );
                }
                if (wCnt > 0) {
                    cell.classList.add("has-watering");
                    wrap.insertAdjacentHTML(
                        "beforeend",
                        `<div class="badge-watering" title="물주기 ${wCnt}건"><b>물주기 ${wCnt}</b></div>`
                    );
                }
                if (dCnt === 0 && wCnt === 0) wrap.remove();
            });
        } catch (e) {
            console.error("달력 데이터 로딩 실패", e);
        }
    }

    async function fetchDayData(ymd) {
        const memberId = Number(document.body.dataset.memberId || 0);
        if (!memberId) throw new Error("memberId 누락");
        const {start, end} = buildDayRangeISO(ymd);
        const [resDiary, resWater] = await Promise.all([
            axios.get("/api/plantingCalender/diary", {
                params: {memberId, startDate: start, endDate: end},
                headers: {Accept: "application/json"}
            }),
            axios.get("/api/plantingCalender/watering", {
                params: {memberId, startDate: start, endDate: end},
                headers: {Accept: "application/json"}
            })
        ]);
        return {
            diaries: Array.isArray(resDiary.data) ? resDiary.data : [],
            waters: Array.isArray(resWater.data) ? resWater.data : []
        };
    }

    function clearDiaryList() {
        if (diaryListContainerEl) diaryListContainerEl.innerHTML = "";
        const diaryEmpty = document.getElementById("diaryEmpty");
        if (diaryEmpty) diaryEmpty.hidden = true;
    }

    function renderDiaryListFromAPI(diaries) {
        clearDiaryList();
        const diaryEmpty = document.getElementById("diaryEmpty");
        if (!diaries || diaries.length === 0) {
            if (diaryEmpty) diaryEmpty.hidden = false;
            return;
        }
        diaries.forEach(d => {
            const data = {
                diaryId: d.diaryId,
                plant: d.name || d.plant || "-",
                activity: d.activity || "",
                condition: d.condition || "",
                memo: d.memo || d.note || "",
                photos: d.photoUrls || [],
                date: toLocalYmd(d.createdAt) || ""
            };
            appendDiaryCard(data);
        });
    }

    function renderWateringList(items, dayYmd) {
        const wrap = document.getElementById("wateringListContainer");
        const empty = document.getElementById("wateringEmpty");
        if (!wrap) return;
        wrap.innerHTML = "";
        if (!items || items.length === 0) {
            if (empty) empty.hidden = false;
            return;
        }
        if (empty) empty.hidden = true;
        const isToday = dayYmd === ymdFromDate(new Date());

        items.forEach(item => {
            const displayName = item.name || item.plant || "-";
            const checkedAttr = item.checkFlag ? "checked" : "";
            const wateringId = item.wateringId ?? "";
            const disabledAttr = isToday ? "" : "disabled";
            const disableAttr = item.checkFlag ? "disabled" : "";

            const row = document.createElement("div");
            row.className = "d-flex align-items-center justify-content-between py-1 border-bottom";
            row.innerHTML = `
      <div class="d-flex align-items-center gap-2">
        <i class="bi bi-droplet"></i>
        <label class="m-0">${displayName}</label>
      </div>
      <input type="checkbox"
             class="form-check-input watering-check"
             data-watering-id="${wateringId}"
             aria-label="물주기 완료"
             ${checkedAttr} ${disabledAttr} ${disableAttr}>
    `;
            wrap.appendChild(row);
        });
    }

    async function renderDayPanels(ymd) {
        try {
            lastRequestedYmd = ymd;
            const {diaries, waters} = await fetchDayData(ymd);
            if (lastRequestedYmd !== ymd) return;
            renderDiaryListFromAPI(diaries);
            renderWateringList(waters, ymd);
        } catch (err) {
            console.error("일자 패널 렌더 실패:", err);
        }
    }

    function renderPhotoList() {
        if (!photoListEl) return;
        photoListEl.innerHTML = "";
        photoFiles.forEach((file, index) => {
            const url = URL.createObjectURL(file);
            const box = document.createElement("div");
            box.className = "position-relative";
            box.style = "width:110px;height:110px;";
            box.innerHTML = `
        <img src="${url}" class="w-100 h-100 rounded" style="object-fit:cover;">
        <button class="btn btn-danger btn-sm position-absolute top-0 end-0 p-0 px-1" data-remove-index="${index}">×</button>
      `;
            photoListEl.appendChild(box);
        });
        const addBtn = document.createElement("div");
        addBtn.id = "addPhotoBtn";
        addBtn.className = "d-flex justify-content-center align-items-center border rounded";
        addBtn.style = "width:110px;height:110px;background:#efefef;cursor:pointer;";
        addBtn.innerHTML = `<span class="fs-1">+</span>`;
        addBtn.addEventListener("click", () => {
            if (photoFiles.length >= 5) {
                showAlert("사진은 최대 5장까지 업로드 가능합니다.");
                return;
            }
            if (imageModalEl) new bootstrap.Modal(imageModalEl).show();
        });
        photoListEl.appendChild(addBtn);
    }

    photoListEl?.addEventListener("click", (e) => {
        const btn = e.target.closest("[data-remove-index]");
        if (!btn) return;
        const idx = Number(btn.getAttribute("data-remove-index"));
        photoFiles.splice(idx, 1);
        renderPhotoList();
    });

    addPhotoBtnEl?.addEventListener("click", () => {
        if (photoFiles.length >= 5) {
            showAlert("사진은 최대 5장까지 업로드 가능합니다.");
            return;
        }
        if (imageInputEl) imageInputEl.value = "";
        if (previewImageEl) previewImageEl.classList.add("d-none");
        if (imageModalEl) new bootstrap.Modal(imageModalEl).show();
    });

    imageInputEl?.addEventListener("change", (e) => {
        const file = e.target.files?.[0];
        if (!file || !previewImageEl) return;
        previewImageEl.src = URL.createObjectURL(file);
        previewImageEl.classList.remove("d-none");
    });

    document.getElementById("saveImageBtn")?.addEventListener("click", () => {
        const file = imageInputEl?.files?.[0];
        if (!file) return;
        photoFiles.push(file);
        renderPhotoList();
        if (imageModalEl) bootstrap.Modal.getInstance(imageModalEl)?.hide();
    });

    saveDiaryBtnEl?.addEventListener("click", () => {
        const plantSel = document.getElementById("plantSelect");
        const activityInput = document.getElementById("activityInput");
        const conditionInput = document.getElementById("conditionInput");
        const memoInput = document.getElementById("memoInput");
        const diaryModalEl = document.getElementById("diaryModal");

        const data = {
            plant: plantSel ? plantSel.value : "",
            activity: activityInput ? activityInput.value : "",
            condition: conditionInput ? conditionInput.value : "",
            memo: memoInput ? memoInput.value : "",
            photos: [...photoFiles],
            date: new Date().toISOString().split("T")[0]
        };

        appendDiaryCard(data);
        photoFiles = [];
        renderPhotoList();
        if (diaryModalEl) bootstrap.Modal.getInstance(diaryModalEl)?.hide();
    });

    function appendDiaryCard(diary) {
        if (!diaryListContainerEl) return;
        let photoHtml = "";
        if (diary.photos && diary.photos.length > 0) {
            const fileOrUrl = diary.photos[0];
            const url = fileOrUrl instanceof File ? URL.createObjectURL(fileOrUrl) : String(fileOrUrl);
            photoHtml = `<img src="${url}" width="60" height="60" class="rounded me-3">`;
        }
        const card = document.createElement("div");
        card.className = "d-flex flex-column bg-warning bg-opacity-10 rounded-2 border-start border-4 border-warning p-3 mb-3";
        card.innerHTML = `
      <div class="d-flex align-items-start diary-card" data-diary-id="${diary.diaryId}">
        <div class="d-flex align-items-center">
          ${photoHtml}
          <div class="fw-bold">${diary.plant || "-"}</div>
        </div>
        <div class="ms-auto d-flex align-items-center gap-3">
          <span class="badge bg-warning text-dark">${diary.date}</span>
          <button type="button"
                class="btn btn-link text-secondary p-0 remove-btn"
                data-diary-id="${diary.diaryId}"
                aria-label="이 관찰일지 삭제">
          <i class="fa-solid fa-xmark fs-5"></i>
        </button>
        </div>
      </div>
      <div class="mt-2 ps-1 bg-light rounded-1 p-1">
        <small class="text-muted">메모: ${diary.memo || "-"}</small>
      </div>
    `;
        diaryListContainerEl.prepend(card);
    }

    async function safeRender(year, month) {
        if (isRendering) return;
        isRendering = true;
        await renderCalendar(year, month);
        const today = new Date();
        const sameMonth = today.getFullYear() === year && (today.getMonth() + 1) === month;
        const defaultYmd = sameMonth ? ymdFromDate(today) : `${year}-${pad2(month)}-01`;
        selectDate(defaultYmd);
        renderDayPanels(defaultYmd);
        isRendering = false;
    }

    prevBtn?.addEventListener("click", async (e) => {
        e.preventDefault();
        if (isRendering) return;
        currentMonth--;
        if (currentMonth < 1) {
            currentMonth = 12;
            currentYear--;
        }
        await safeRender(currentYear, currentMonth);
    });

    nextBtn?.addEventListener("click", async (e) => {
        e.preventDefault();
        if (isRendering) return;
        currentMonth++;
        if (currentMonth > 12) {
            currentMonth = 1;
            currentYear++;
        }
        await safeRender(currentYear, currentMonth);
    });

    renderPhotoList();
    (async () => {
        await safeRender(currentYear, currentMonth);
    })();

    diaryListContainerEl?.addEventListener("click", async (e) => {
        const removeBtn = e.target.closest(".remove-btn");
        if (removeBtn) {
            e.stopPropagation(); // 상세 모달 열림 방지

            const card = removeBtn.closest(".diary-card[data-diary-id]");
            if (!card) return;
            const diaryId = card.dataset.diaryId;
            if (!diaryId) {
                showAlert("서버에 저장되지 않은 항목입니다.");
                // 로컬 카드만 제거하고 끝내려면 아래 주석 해제
                // card.parentElement?.remove();
                return;
            }

            showModal("이 관찰일지를 삭제할까요?", async (ok) => {
                if (!ok) return;

                const container = card.parentElement;
                const backupHTML = container.outerHTML;
                container.style.opacity = "0.5";

                try {
                    await axios.delete(`/api/plantingCalender/diary/${encodeURIComponent(diaryId)}`, {
                        headers: { Accept: "application/json" }
                    });

                    container.remove();

                    // 선택된 날짜 패널과 월 배지 갱신
                    const ymd = selectedDate || ymdFromDate(new Date());
                    await renderDayPanels(ymd);
                    const [yy, mm] = ymd.split("-").map(Number);
                    await loadAndMark(yy, mm);
                } catch (err) {
                    console.error("다이어리 삭제 실패:", err);
                    container.insertAdjacentHTML("afterend", backupHTML);
                    container.remove();
                    showAlert("삭제에 실패했습니다. 잠시 후 다시 시도해 주세요.");
                }
            });
            return;
        }
        const card = e.target.closest(".diary-card[data-diary-id]");
        if (!card || !diaryListContainerEl.contains(card)) return;

        const diaryId = card.dataset.diaryId;
        try {
            const {data} = await axios.get(`/api/plantingCalender/diaryInfo/${encodeURIComponent(diaryId)}`, {
                headers: {Accept: "application/json"}
            });

            function openDiaryUpdateModal(resp) {
                const el = document.getElementById("diaryDetailModal");
                if (!el) return showAlert("상세 모달이 없습니다.");

                // 부트스트랩 모달 인스턴스 확보
                const modal = bootstrap.Modal.getInstance(el) || new bootstrap.Modal(el);

                // 응답 정규화
                const d = resp?.diary ?? {};
                const photos = Array.isArray(resp?.images) ? resp.images.map(it => it.fileUrl).filter(Boolean) : [];

                // 필드 매핑: state -> condition, createdAt -> date
                document.getElementById("detailPlant").value = d.name || d.plant || "-"; // 서버에서 식물명이 없으면 "-"로
                document.getElementById("detailActivity").value = d.activity || "";
                document.getElementById("detailCondition").value = d.state || "";        // 여기!
                document.getElementById("detailMemo").value = d.memo || "";
                document.getElementById("detailDate").textContent = (d.createdAt || "").toString().slice(0, 10);

                // 사진 렌더
                const wrap = document.getElementById("detailPhotos");
                wrap.innerHTML = "";
                if (photos.length === 0) {
                    wrap.innerHTML = `<div class="text-muted small">사진이 없습니다.</div>`;
                } else {
                    photos.forEach(url => {
                        const box = document.createElement("div");
                        box.className = "rounded border";
                        box.style.cssText = "width:100%;aspect-ratio:1/1;overflow:hidden;";
                        box.innerHTML = `<img src="${url}" alt="관찰일지 사진" style="width:100%;height:100%;object-fit:cover;display:block;">`;
                        wrap.appendChild(box);
                    });
                }

                modal.show();
            }



            openDiaryUpdateModal(data);
        } catch (err) {
            console.error("다이어리 상세 조회 실패:", err);
        }
    });

    document.getElementById("wateringListContainer")?.addEventListener("change", (e) => {
        const cb = e.target.closest(".watering-check");
        if (!cb) return;

        const wateringId = cb.dataset.wateringId;
        const checked = cb.checked;

        console.log("물주기 체크 변경:", wateringId, checked);

        axios.put("/api/plantingCalender/watering", null,
            {
                params: {wateringId},
                headers: {Accept: "application/json"}
            });

        window.location.reload();
    });
});


