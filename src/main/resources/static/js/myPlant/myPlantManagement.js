import {createPaginator} from '/js/common/pagination.js';

(function () {
    const state = {
        plants: [],
        filtered: [],
        currentPage: 1,
        limit: 8,
        totalCount: 0,
        keyword: ""
    };

    function toDate(val) {
        if (!val) return null;
        const d = new Date(val);
        return Number.isNaN(d.getTime()) ? null : d;
    }

    function daysBetween(from) {
        const d = toDate(from);
        if (!d) return 0;
        return Math.max(0, Math.floor((Date.now() - d.getTime()) / 86400000));
    }

    function addDays(dateVal, days) {
        const d = toDate(dateVal);
        if (!d || !Number.isFinite(days)) return null;
        const r = new Date(d);
        r.setDate(r.getDate() + days);
        return r;
    }

    function formatDate(val) {
        const d = toDate(val);
        if (!d) return "";
        const yyyy = d.getFullYear();
        const mm = String(d.getMonth() + 1).padStart(2, "0");
        const dd = String(d.getDate()).padStart(2, "0");
        return `${yyyy}-${mm}-${dd}`;
    }

    function isValidWaterDates(startStr, endStr) {
        const sStr = (startStr ?? "").trim();
        const eStr = (endStr ?? "").trim();

        const hasStart = sStr.length > 0;
        const hasEnd   = eStr.length > 0;

        if (!hasStart && !hasEnd) return true;
        if (hasStart !== hasEnd) return false;

        const s = toDate(sStr);
        const e = toDate(eStr);
        if (!s || !e) return false;
        return s.getTime() < e.getTime();
    }

    const rawMemberId = document.body.dataset.memberId;
    const memberId = Number(rawMemberId);

    let currentVM = null;
    let deletePhoto = false;
    let pager = null;

    const listEl = document.getElementById("plant-list");
    const pagEl = document.getElementById("myplant-pagination");
    const searchEl = document.getElementById("search");
    const searchBtn = document.getElementById("searchBtn");

    const detailEl = document.getElementById("plantDetailModal");
    const dImgEl = document.getElementById("detailImg");
    const dNameEl = document.getElementById("detailName");
    const dTypeEl = document.getElementById("detailType");
    const dSoilEl = document.getElementById("detailFertilizer");
    const dTempEl = document.getElementById("detailTemp");
    const dStartEl = document.getElementById("detailStartAt");
    const dEndEl = document.getElementById("detailEndDate");
    const dIntervalEl = document.getElementById("detailIntervalDays");
    const dNextEl = document.getElementById("detailNextWaterAt");

    function normalizePlant(r) {
        const createdAt = r.createdAt ?? null;
        const startAt = r.startAt ?? null;
        const endDate = r.endDate ?? null;
        const interval = Number(r.interval) || 0;
        const baseForNext = endDate || startAt;
        const nextWaterAt = interval > 0 ? addDays(baseForNext, interval) : null;

        return {
            id: r.myplantId ?? null,
            memberId: r.memberId ?? null,
            name: r.name ?? "",
            type: r.type || "",
            soil: r.soil || "",
            temperature: r.temperature || "",
            img: r.imageUrl || "",
            fileId: r.imageId ?? null,
            createdAt,
            startAt,
            endDate,
            interval,
            daysSinceCreated: daysBetween(createdAt),
            daysSinceLastWater: daysBetween(endDate),
            nextWaterAt
        };
    }

    async function fetchPlants(page = 1) {
        try {
            if (!Number.isFinite(memberId) || memberId <= 0) {
                console.warn("memberId가 유효하지 않습니다:", rawMemberId);
                return;
            }
            const offset = (page - 1) * state.limit;
            const name = state.keyword ?? "";

            const {data} = await axios.get("/api/myPlant/list", {
                params: {memberId, name, limit: state.limit, offset}
            });

            const rows = Array.isArray(data) ? data : [];
            state.totalCount = rows[0]?.totalCount > 0 ? rows[0].totalCount : 0;

            state.plants = rows.map(normalizePlant);
            state.filtered = state.plants.slice();
            state.currentPage = page;

            renderCards(state.filtered);

            pager?.update({
                current: state.currentPage,
                totalItems: state.totalCount,
                pageSize: state.limit
            });
        } catch (e) {
            console.error("목록 조회 실패:", e);
            state.plants = [];
            state.filtered = [];
            state.totalCount = 0;
            renderCards([]);

            pager?.update({
                current: 1,
                totalItems: 0,
                pageSize: state.limit
            });
        }
    }

    function renderCards(items) {
        if (!items.length) {
            listEl.innerHTML = `
      <div class="col-12">
        <div class="text-center text-muted py-5">
          <i class="bi bi-box"></i> 표시할 식물이 없습니다.
        </div>
      </div>`;
            return;
        }

        listEl.innerHTML = items.map((p, idx) => `
    <div class="col-12 col-sm-6 col-md-4 col-lg-3">
      <div class="card plant-card shadow-sm border-0 h-100" data-index="${idx}">
        ${p.img
            ? `<img src="${p.img}" class="card-img-top rounded-top" alt="plant">`
            : `<div class="bg-light d-flex justify-content-center align-items-center" style="height:260px;">
               <i class="bi bi-image fs-1 text-muted"></i>
             </div>`}
        <div class="card-body text-center">
          <h6 class="fw-bold mb-1">${p.name}</h6>
          <p class="text-muted small mb-0">함께한지 +${p.daysSinceCreated}일</p>
        </div>
      </div>
    </div>
  `).join("");
    }

    function openDetailModal(raw) {
        const vm = (raw && typeof raw === "object" && raw.id != null) ? raw : normalizePlant(raw);
        currentVM = vm;

        const FALLBACK = "/image/default.png";

        if (dImgEl) dImgEl.src = vm.img || FALLBACK;
        if (dNameEl) dNameEl.value = vm.name || "";
        if (dTypeEl) dTypeEl.value = vm.type || "";
        if (dSoilEl) dSoilEl.value = vm.soil || "";
        if (dTempEl) dTempEl.value = vm.temperature || "";

        if (dStartEl) dStartEl.value = vm.startAt ? formatDate(vm.startAt) : "";
        if (dEndEl) dEndEl.value = vm.endDate ? formatDate(vm.endDate) : "";
        if (dIntervalEl) dIntervalEl.value = vm.interval > 0 ? String(vm.interval) : "";

        if (detailEl) bootstrap.Modal.getOrCreateInstance(detailEl).show();

        const imgEl = document.getElementById("editImgPreview");
        imgEl.src = vm.img || FALLBACK;

        deletePhoto = false;

        const fileEl = document.getElementById("detailImgFile");
        if (fileEl) fileEl.value = "";

        refreshPhotoControls();
    }

    function refreshPhotoControls() {
        const fileEl = document.getElementById("detailImgFile");
        const btn = document.getElementById("photoDeleteBtn");

        if (!btn) return;

        const hasExisting = Number.isFinite(Number(currentVM?.fileId)) && Number(currentVM.fileId) > 0;
        const hasNew = !!(fileEl?.files && fileEl.files[0]);

        btn.disabled = !(hasExisting || hasNew);
        btn.textContent = hasNew ? "선택한 이미지 취소" : "사진 삭제";
    }

    document.addEventListener("DOMContentLoaded", () => {
        pager = createPaginator({
            container: pagEl,
            current: 1,
            totalItems: 0,
            pageSize: state.limit,
            windowSize: 5,
            modeWhenUnknown: 'hide-all',
            labels: {first: '«', prev: '‹', next: '›', last: '»'},
            onChange: (page) => {
                if (!Number.isFinite(page)) return;
                fetchPlants(page);
            }
        });

        const addBtn = document.getElementById("openAddModal");
        const addModalEl = document.getElementById("addPlantModal");
        if (addBtn && addModalEl) {
            addBtn.addEventListener("click", () => {
                bootstrap.Modal.getOrCreateInstance(addModalEl).show();
            });
        }

        function toLocalDateTimeStr(dateStr) {
            if (!dateStr) return "";
            return `${dateStr}T00:00:00`;
        }

        {
            const fileEl = document.getElementById("addImgFile");
            const imgEl = document.getElementById("addImgPreview");
            const FALLBACK = "https://via.placeholder.com/200x230?text=No+Image";

            if (fileEl && imgEl) {
                imgEl.addEventListener("error", () => {
                    imgEl.src = FALLBACK;
                });

                fileEl.addEventListener("change", () => {
                    const f = fileEl.files && fileEl.files[0];
                    if (!f) {
                        imgEl.src = FALLBACK;
                        return;
                    }
                    if (!f.type || !f.type.startsWith("image/")) {
                        showAlert("이미지 파일만 선택하세요.");
                        fileEl.value = "";
                        imgEl.src = FALLBACK;
                        return;
                    }

                    const url = URL.createObjectURL(f);
                    imgEl.onload = () => {
                        URL.revokeObjectURL(url);
                    };
                    imgEl.src = url;
                });
            }
        }

        (() => {
            const addBtnEl = document.getElementById("addSubmitBtn");
            const addNameEl = document.getElementById("addName");
            const addTypeEl = document.getElementById("addType");
            const addStartAtEl = document.getElementById("addStartAt");
            const addEndDateEl = document.getElementById("addEndDate");
            const addIntervalEl = document.getElementById("addIntervalDays");
            const addSoilEl = document.getElementById("addFertilizer");
            const addTempEl = document.getElementById("addTemp");
            const addFileEl = document.getElementById("addImgFile");
            const addModalEl = document.getElementById("addPlantModal");

            if (!addBtnEl) return;

            addBtnEl.addEventListener("click", async () => {
                try {
                    const name = (addNameEl?.value || "").trim();
                    if (!name) {
                        showAlert("식물 이름은 필수입니다.");
                        return;
                    }

                    const startStr = (addStartAtEl?.value || "").trim();
                    const endStr = (addEndDateEl?.value || "").trim();

                    if (!isValidWaterDates(startStr, endStr)) {
                        showAlert("최초 물 준일자는 마지막 물 준일자보다 같거나 늦을 수 없습니다.");
                        return;
                    }

                    const fd = new FormData();

                    fd.append("memberId", String(memberId));
                    fd.append("name", name);
                    fd.append("type", (addTypeEl?.value || "").trim());
                    fd.append("startAt", toLocalDateTimeStr(addStartAtEl?.value || ""));
                    fd.append("endDate", toLocalDateTimeStr(addEndDateEl?.value || ""));
                    fd.append("interval", String(addIntervalEl?.value || "0"));
                    fd.append("soil", (addSoilEl?.value || "").trim());
                    fd.append("temperature", (addTempEl?.value || "").trim());

                    const file = addFileEl?.files?.[0];
                    if (file) {
                        fd.append("file", file, file.name);
                    }

                    await axios.post("/api/myPlant", fd, {
                        headers: {"Content-Type": "multipart/form-data"}
                    });

                    await fetchPlants(1);

                    showAlert("등록되었습니다.");
                    if (addModalEl) {
                        bootstrap.Modal.getOrCreateInstance(addModalEl).hide();
                    }
                } catch (err) {
                    console.error(err);
                    showAlert("등록에 실패했습니다. 잠시 후 다시 시도해주세요.");
                }
            });
        })();

        document.getElementById("editSubmitBtn")?.addEventListener("click", async () => {
            if (!currentVM?.id) {
                showAlert("수정 대상 식물 정보를 찾지 못했습니다.");
                return;
            }

            const name = (document.getElementById("detailName")?.value || "").trim();
            const type = (document.getElementById("detailType")?.value || "").trim();
            const soil = (document.getElementById("detailFertilizer")?.value || "").trim();
            const temp = (document.getElementById("detailTemp")?.value || "").trim();
            const start = document.getElementById("detailStartAt")?.value || "";
            const end = document.getElementById("detailEndDate")?.value || "";
            const itv = document.getElementById("detailIntervalDays")?.value || "0";

            if (!name) {
                showAlert("식물 이름은 필수입니다.");
                return;
            }

            function toLocalDateTimeStr(dateStr) {
                if (!dateStr) return "";
                return `${dateStr}T00:00:00`;
            }

            if (!isValidWaterDates(start, end)) {
                showAlert("최초 물 준일자는 마지막 물 준일자보다 같거나 늦을 수 없습니다.");
                return;
            }

            const fd = new FormData();
            fd.append("memberId", String(memberId));
            fd.append("myplantId", String(currentVM.id));
            fd.append("name", name);
            fd.append("type", type);
            fd.append("startAt", toLocalDateTimeStr(start));
            fd.append("endDate", toLocalDateTimeStr(end));
            fd.append("interval", String(itv));
            fd.append("soil", soil);
            fd.append("temperature", temp);

            const fileEl = document.getElementById("detailImgFile");
            const newFile = fileEl?.files?.[0];

            if (newFile) {
                fd.append("file", newFile, newFile.name);
                if (Number.isFinite(Number(currentVM.fileId)) && Number(currentVM.fileId) > 0) {
                    fd.append("delFile", String(currentVM.fileId));
                }
            } else if (deletePhoto && Number.isFinite(Number(currentVM.fileId)) && Number(currentVM.fileId) > 0) {
                fd.append("delFile", String(currentVM.fileId));
            }

            try {
                await axios.put("/api/myPlant", fd, {
                    headers: {"Content-Type": "multipart/form-data"}
                });
                showAlert("수정되었습니다.");
                bootstrap.Modal.getOrCreateInstance(document.getElementById("plantDetailModal")).hide();
                await fetchPlants(1);
            } catch (err) {
                const msg = err?.response?.data?.message || "수정 중 오류가 발생했습니다.";
                showAlert(msg);
            }
        });

        document.getElementById("photoDeleteBtn")?.addEventListener("click", () => {
            if (!currentVM) return;

            const imgEl = document.getElementById("editImgPreview");
            const fileEl = document.getElementById("detailImgFile");
            const FALLBACK = "/image/default.png";

            const hasNew = !!(fileEl?.files && fileEl.files[0]);
            const hasExisting = Number.isFinite(Number(currentVM?.fileId)) && Number(currentVM.fileId) > 0;

            if (hasNew) {
                fileEl.value = "";
                imgEl.src = currentVM?.img || FALLBACK;
                deletePhoto = false;
            } else if (hasExisting) {
                imgEl.src = FALLBACK;
                deletePhoto = true;
            }

            refreshPhotoControls();
        });

        document.getElementById("deleteWateringBtn")?.addEventListener("click", () => {
            const btn = document.getElementById("deleteWateringBtn");
            btn?.setAttribute("disabled", "true");
            try {
                const params = {myplantId: currentVM.id, memberId: memberId};

                showModal("물주기를 삭제하시겠습니까?", async (result) => {
                    if (result) {
                        await axios.delete("/api/plantingCalender/watering", {params});
                        document.getElementById("detailStartAt").value = "";
                        document.getElementById("detailIntervalDays").value = "";
                        document.getElementById("detailEndDate").value = "";
                        showAlert("삭제되었습니다.");
                    }
                });

            } catch (err) {
                showAlert(err?.response?.data?.message || "삭제 중 오류가 발생했습니다.");
            } finally {
                btn?.removeAttribute("disabled");
            }
        });

        document.getElementById("deleteSubmitBtn")?.addEventListener("click", async () => {
            if (!Number.isInteger(Number(currentVM?.id))) {
                showAlert("삭제 대상 식물 정보를 찾지 못했습니다.");
                return;
            }

            const btn = document.getElementById("deleteSubmitBtn");
            btn?.setAttribute("disabled", "true");

            try {
                const params = {myplantId: currentVM.id};
                const fileIdNum = Number(currentVM.fileId);
                if (Number.isInteger(fileIdNum) && fileIdNum > 0) {
                    params.delFile = fileIdNum;
                }
                showModal("식물을 삭제하시겠습니까?", async (result) => {
                    if (!result) {
                        btn?.removeAttribute("disabled");
                        return;
                    }

                    try {
                        await axios.delete("/api/myPlant", {params});

                        bootstrap.Modal.getOrCreateInstance(
                            document.getElementById("plantDetailModal")
                        ).hide();

                        await fetchPlants(1);

                        showAlert("삭제되었습니다.");
                    } catch (err) {
                        showAlert(err?.response?.data?.message || "삭제 중 오류가 발생했습니다.");
                    } finally {
                        btn?.removeAttribute("disabled");
                    }
                });

            } catch (err) {
                showAlert("처리 중 오류가 발생했습니다.");
                btn?.removeAttribute("disabled");
            }
        });

        listEl.addEventListener("click", e => {
            const card = e.target.closest(".plant-card");
            if (!card) return;
            const idx = Number(card.dataset.index);
            const item = state.filtered[idx];
            if (item) openDetailModal(item);
        });

        function filterPlants() {
            state.keyword = searchEl.value.trim();
            fetchPlants(1);
        }

        searchBtn.addEventListener("click", filterPlants);
        searchEl.addEventListener("keyup", e => e.key === "Enter" && filterPlants());

        ["change", "input"].forEach(evt => {
            dStartEl?.addEventListener(evt, () => currentVM);
            dEndEl?.addEventListener(evt, () => currentVM);
            dIntervalEl?.addEventListener(evt, () => currentVM);
        });

        {
            const fileEl = document.getElementById("detailImgFile");
            const imgEl = document.getElementById("editImgPreview");
            if (fileEl && imgEl) {
                fileEl.addEventListener("change", () => {
                    const f = fileEl.files && fileEl.files[0];
                    if (!f) {
                        refreshPhotoControls();
                        return;
                    }
                    if (!f.type?.startsWith("image/")) {
                        showAlert("이미지 파일만 선택하세요.");
                        fileEl.value = "";
                        refreshPhotoControls();
                        return;
                    }

                    const url = URL.createObjectURL(f);
                    imgEl.onload = () => URL.revokeObjectURL(url);
                    imgEl.src = url;

                    deletePhoto = false;
                    refreshPhotoControls();
                });
            }
        }

        fetchPlants(1);
    });
})();