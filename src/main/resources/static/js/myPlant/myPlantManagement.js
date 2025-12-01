// ========== 유틸 ==========
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

// ========== 상태 ==========
const rawMemberId = document.body.dataset.memberId;
const memberId = Number(rawMemberId);

const state = {
    plants: [],
    filtered: [],
    currentPage: 1,
    limit: 8,
    totalCount: 0,   // 서버에서 유효 totalCount 받으면 사용
    keyword: ""
};

// 현재 상세 VM(모달용)
let currentVM = null;

// ========== 엘리먼트 ==========
const listEl = document.getElementById("plant-list");
const pagEl = document.getElementById("pagination");
const searchEl = document.getElementById("search");
const searchBtn = document.getElementById("searchBtn");

// 상세 모달 요소
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

// ========== 응답 → 뷰모델 ==========
function normalizePlant(r) {
    const createdAt = r.createdAt ?? null;     // 카드 “함께한지” 기준
    const startAt = r.startAt ?? null;       // 최초 물 준 일자
    const endDate = r.endDate ?? null;       // 마지막 물 준 일자
    const interval = Number(r.interval) || 0; // 간격(일)
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

// ========== 서버 호출 ==========
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
        renderPagination();
    } catch (e) {
        console.error("목록 조회 실패:", e);
        state.plants = [];
        state.filtered = [];
        state.totalCount = 0;
        renderCards([]);
        renderPagination();
    }
}

// ========== 렌더 ==========
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

function renderPagination() {
    const totalPages = state.totalCount > 0
        ? Math.max(1, Math.ceil(state.totalCount / state.limit))
        : 1;

    const cur = state.currentPage;

    const btn = (page, text, disabled, active = false) => `
    <li class="page-item ${disabled ? "disabled" : ""} ${active ? "active" : ""}">
      <button class="page-link" data-page="${page}">${text}</button>
    </li>`;

    let html = "";
    html += btn(1, "&laquo;", cur === 1);
    html += btn(cur - 1, "&lsaquo;", cur === 1);
    for (let i = 1; i <= totalPages; i++) html += btn(i, i, false, i === cur);
    html += btn(cur + 1, "&rsaquo;", cur === totalPages);
    html += btn(totalPages, "&raquo;", cur === totalPages);

    pagEl.innerHTML = html;
}

// ========== 상세 모달 ==========
function updateNextWaterAndBind(vm) {
    // 입력 → VM 반영
    const sVal = dStartEl?.value || "";
    const eVal = dEndEl?.value || "";
    const iVal = Number(dIntervalEl?.value);

    vm.startAt = sVal ? new Date(sVal).toISOString() : null;
    vm.endDate = eVal ? new Date(eVal).toISOString() : null;
    vm.interval = Number.isFinite(iVal) && iVal > 0 ? iVal : 0;

    // 다음 물 줄 날짜 계산
    const base = vm.endDate || vm.startAt;
    vm.nextWaterAt = vm.interval > 0 ? addDays(base, vm.interval) : null;

    if (dNextEl) dNextEl.value = vm.nextWaterAt ? formatDate(vm.nextWaterAt) : "";
}

function openDetailModal(raw) {
    const vm = (raw && typeof raw === "object" && raw.id != null) ? raw : normalizePlant(raw);
    currentVM = vm;

    if (dImgEl) dImgEl.src = raw.img;
    if (dNameEl) dNameEl.value = vm.name || "";
    if (dTypeEl) dTypeEl.value = vm.type || "";
    if (dSoilEl) dSoilEl.value = vm.soil || "";
    if (dTempEl) dTempEl.value = vm.temperature || "";

    if (dStartEl) dStartEl.value = vm.startAt ? formatDate(vm.startAt) : "";
    if (dEndEl) dEndEl.value = vm.endDate ? formatDate(vm.endDate) : "";
    if (dIntervalEl) dIntervalEl.value = vm.interval > 0 ? String(vm.interval) : "";

    updateNextWaterAndBind(vm);

    if (detailEl) bootstrap.Modal.getOrCreateInstance(detailEl).show();

    const imgEl = document.getElementById("editImgPreview");
    const FALLBACK = "/image/default.png"; // 프로젝트 기본 이미지 경로
    imgEl.src = raw.imageUrl || raw.img || FALLBACK;

    // 삭제 플래그 초기화
    deletePhoto = false;

    // 사진삭제 버튼 상태
    const photoDelBtn = document.getElementById("photoDeleteBtn");
    if (photoDelBtn) {
        const hasFile = Number.isFinite(Number(vm.fileId)) && Number(vm.fileId) > 0;
        photoDelBtn.disabled = !hasFile; // 기존 파일 없으면 비활성화
    }
}


// ========== 이벤트 바인딩 ==========
document.addEventListener("DOMContentLoaded", () => {
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
                    alert("이미지 파일만 선택하세요.");
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
        const addSoilEl = document.getElementById("addFertilizer"); // HTML id는 그대로, 서버 필드명은 'soil'
        const addTempEl = document.getElementById("addTemp");
        const addFileEl = document.getElementById("addImgFile");
        const addModalEl = document.getElementById("addPlantModal");

        if (!addBtnEl) return;

        addBtnEl.addEventListener("click", async () => {
            try {
                // 최소 검증: 이름
                const name = (addNameEl?.value || "").trim();
                if (!name) {
                    alert("식물 이름은 필수입니다.");
                    return;
                }

                const fd = new FormData();

                // 테스트 코드와 동일한 필드명으로 전송
                fd.append("memberId", String(memberId)); // body data-member-id에서 읽은 값
                fd.append("name", name);
                fd.append("type", (addTypeEl?.value || "").trim());
                fd.append("startAt", toLocalDateTimeStr(addStartAtEl?.value || ""));
                fd.append("endDate", toLocalDateTimeStr(addEndDateEl?.value || ""));
                fd.append("interval", String(addIntervalEl?.value || "0"));
                fd.append("soil", (addSoilEl?.value || "").trim());          // 서버 DTO의 'soil'
                fd.append("temperature", (addTempEl?.value || "").trim());   // 서버 DTO의 'temperature'

                // 파일 파트명은 'file'이어야 테스트와 1:1 매칭됨
                const file = addFileEl?.files?.[0];
                if (file) {
                    fd.append("file", file, file.name);
                }

                await axios.post("/api/myPlant", fd, {
                    headers: {"Content-Type": "multipart/form-data"}
                });

                await fetchPlants(1);

                if (addModalEl) {
                    bootstrap.Modal.getOrCreateInstance(addModalEl).hide();
                }
            } catch (err) {
                console.error(err);
                alert("등록에 실패했습니다. 잠시 후 다시 시도해주세요.");
            }
        });
    })();

    document.getElementById("editSubmitBtn")?.addEventListener("click", async () => {
        if (!currentVM?.id) {
            alert("수정 대상 식물 정보를 찾지 못했습니다.");
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
            alert("식물 이름은 필수입니다.");
            return;
        }

        function toLocalDateTimeStr(dateStr) {
            if (!dateStr) return "";
            return `${dateStr}T00:00:00`;
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

        // === 사진 교체/삭제 처리 (이 부분이 핵심) ===
        const fileEl = document.getElementById("detailImgFile");
        const newFile = fileEl?.files?.[0];

        if (newFile) {
            // 새 파일 업로드
            fd.append("file", newFile, newFile.name);
            // 기존 파일이 있었다면 동시에 삭제 지시(교체 시 둘 다 보냄)
            if (Number.isFinite(Number(currentVM.fileId)) && Number(currentVM.fileId) > 0) {
                fd.append("delFile", String(currentVM.fileId));
            }
        } else if (deletePhoto && Number.isFinite(Number(currentVM.fileId)) && Number(currentVM.fileId) > 0) {
            // 새 파일은 없고, 사진삭제만 선택한 경우
            fd.append("delFile", String(currentVM.fileId));
        }
        // 새 파일도 없고 삭제도 아니면 이미지 관련 전송 없음(유지)

        try {
            await axios.put("/api/myPlant", fd, {
                headers: {"Content-Type": "multipart/form-data"}
            });
            alert("수정되었습니다.");
            bootstrap.Modal.getOrCreateInstance(document.getElementById("plantDetailModal")).hide();
            await fetchPlants(1);
        } catch (err) {
            const msg = err?.response?.data?.message || "수정 중 오류가 발생했습니다.";
            alert(msg);
        }
    });


    document.getElementById("photoDeleteBtn")?.addEventListener("click", () => {
        if (!currentVM) return;

        const imgEl = document.getElementById("editImgPreview");
        const fileEl = document.getElementById("detailImgFile");
        const FALLBACK = "/image/default.png";

        // 미리보기 제거(플레이스홀더로 대체) + 파일 선택값 초기화
        if (imgEl) imgEl.src = FALLBACK;
        if (fileEl) fileEl.value = "";

        // 삭제 플래그 설정
        deletePhoto = true;

        // 한 번 더 누를 필요 없도록 버튼 잠금
        const photoDelBtn = document.getElementById("photoDeleteBtn");
        if (photoDelBtn) photoDelBtn.disabled = true;
    });

    document.getElementById("deleteSubmitBtn")?.addEventListener("click", async () => {
        if (!Number.isInteger(Number(currentVM?.id))) {
            alert("삭제 대상 식물 정보를 찾지 못했습니다.");
            return;
        }
        if (!confirm("정말 삭제하시겠습니까?")) return;

        const btn = document.getElementById("deleteSubmitBtn");
        btn?.setAttribute("disabled", "true");

        try {
            const params = {myplantId: currentVM.id};
            const fileIdNum = Number(currentVM.fileId);
            if (Number.isInteger(fileIdNum) && fileIdNum > 0) {
                params.delFile = fileIdNum;
            }
            await axios.delete("/api/myPlant", {
                params
            });

            alert("삭제되었습니다.");
            bootstrap.Modal.getOrCreateInstance(document.getElementById("plantDetailModal")).hide();
            await fetchPlants(1);
        } catch (err) {
            alert(err?.response?.data?.message || "삭제 중 오류가 발생했습니다.");
        } finally {
            btn?.removeAttribute("disabled");
        }
    });

    // 카드 클릭 위임
    listEl.addEventListener("click", e => {
        const card = e.target.closest(".plant-card");
        if (!card) return;
        const idx = Number(card.dataset.index);
        const item = state.filtered[idx];
        if (item) openDetailModal(item);
    });

    // 페이지네이션
    pagEl.addEventListener("click", e => {
        const btn = e.target.closest("[data-page]");
        if (!btn) return;
        const page = Number(btn.dataset.page);
        if (!Number.isFinite(page)) return;
        fetchPlants(page);
    });

    // 검색
    function filterPlants() {
        state.keyword = searchEl.value.trim();
        fetchPlants(1);
    }

    searchBtn.addEventListener("click", filterPlants);
    searchEl.addEventListener("keyup", e => e.key === "Enter" && filterPlants());

    // 상세 모달 내 물주기 3칸: 실시간 갱신(리스너는 한 번만 부착)
    ["change", "input"].forEach(evt => {
        dStartEl?.addEventListener(evt, () => currentVM && updateNextWaterAndBind(currentVM));
        dEndEl?.addEventListener(evt, () => currentVM && updateNextWaterAndBind(currentVM));
        dIntervalEl?.addEventListener(evt, () => currentVM && updateNextWaterAndBind(currentVM));
    });

    {
        const fileEl = document.getElementById("detailImgFile");
        const imgEl = document.getElementById("editImgPreview");
        if (fileEl && imgEl) {
            fileEl.addEventListener("change", () => {
                const f = fileEl.files && fileEl.files[0];
                if (!f) return;
                if (!f.type?.startsWith("image/")) {
                    alert("이미지 파일만 선택하세요.");
                    fileEl.value = "";
                    return;
                }

                const url = URL.createObjectURL(f);
                imgEl.onload = () => URL.revokeObjectURL(url);
                imgEl.src = url;

                // 새 파일을 올렸다면 삭제 플래그는 해제
                deletePhoto = false;

                // 사용자가 새 파일을 올린 경우에도 굳이 삭제 버튼을 누를 필요가 없으므로 버튼은 그대로 두어도 무방
            });
        }
    }


    // 초기 로드
    fetchPlants(1);

});
