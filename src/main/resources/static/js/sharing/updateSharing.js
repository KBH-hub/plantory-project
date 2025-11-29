const sharingId = Number(document.body.dataset.sharingId);
let existingImages = [];    // [{id, url}]
let selectedFiles = [];     // [File, File, ...]
let deleteImageIds = [];    // 기존 이미지 삭제할 ID 저장
const MAX_IMAGES = 5;

/****************************************************
 *  이미지 프리뷰 렌더링 (기존 + 새 파일)
 ****************************************************/
function renderImagePreview() {
    const previewList = document.querySelector("#previewList");
    const imgCount = document.querySelector("#imgCount");
    previewList.innerHTML = "";

    /* ---- 1) 기존 이미지 렌더링 ---- */
    existingImages.forEach((img, idx) => {
        const box = document.createElement("div");
        box.className = "position-relative";
        box.style.width = "120px";
        box.style.height = "120px";

        box.innerHTML = `
            <img src="${img.url}" class="rounded border"
                 style="width:120px;height:120px;object-fit:cover;">
            <button class="btn btn-sm btn-danger position-absolute top-0 end-0"
                    data-exist-idx="${idx}">
                <i class="bi bi-x-lg small"></i>
            </button>
        `;
        previewList.appendChild(box);
    });

    /* ---- 2) 새로 업로드된 파일 렌더링 ---- */
    selectedFiles.forEach((file, idx) => {
        const url = URL.createObjectURL(file);

        const box = document.createElement("div");
        box.className = "position-relative";
        box.style.width = "120px";
        box.style.height = "120px";

        box.innerHTML = `
            <img src="${url}" class="rounded border"
                 style="width:120px;height:120px;object-fit:cover;">
            <button class="btn btn-sm btn-danger position-absolute top-0 end-0"
                    data-new-idx="${idx}">
                <i class="bi bi-x-lg small"></i>
            </button>
        `;
        previewList.appendChild(box);
    });

    imgCount.textContent = existingImages.length + selectedFiles.length;
}

/****************************************************
 *  수정 화면 초기 로딩
 ****************************************************/
async function loadSharingForEdit() {
    const sharingId = document.body.dataset.sharingId;
    if (!sharingId) return;

    const res = await axios.get(`/api/sharing/${sharingId}`);
    const data = res.data;

    // 텍스트 필드
    document.querySelector("#plantNameInput").value = data.plantType;
    document.querySelector("#titleInput").value = data.title;
    document.querySelector("#contentInput").value = data.content;

    // 난이도 / 관리 요구도
    document.querySelector("#plantConditionInput").value = data.managementLevelLabel;
    document.querySelector("#plantDifficultyInput").value = data.managementNeedsLabel;

    document.querySelector("#plantConditionInput").dataset.enum = data.managementLevel;
    document.querySelector("#plantDifficultyInput").dataset.enum = data.managementNeeds;

    // 기존 이미지
    existingImages = data.images.map(img => ({
        id: img.imageId,
        url: img.fileUrl
    }));

    renderImagePreview();
}

/****************************************************
 *  이미지 업로더 + 삭제 기능
 ****************************************************/
function bindImageUploader() {
    const fileInput = document.querySelector("#plantImages");
    const addTile = document.querySelector("#addTile");
    const previewList = document.querySelector("#previewList");

    addTile.addEventListener("click", () => fileInput.click());

    // 새 파일 선택
    fileInput.addEventListener("change", (e) => {
        const files = Array.from(e.target.files);

        if (existingImages.length + selectedFiles.length + files.length > MAX_IMAGES) {
            showAlert("최대 5장까지만 업로드할 수 있습니다.");
            return;
        }

        selectedFiles = [...selectedFiles, ...files];
        renderImagePreview();
    });

    // 이미지 삭제(기존/새 구분)
    previewList.addEventListener("click", (e) => {
        const btn = e.target.closest("button");
        if (!btn) return;

        const existIdx = btn.dataset.existIdx;
        const newIdx = btn.dataset.newIdx;

        // 기존 이미지 삭제
        if (existIdx !== undefined) {
            const removed = existingImages.splice(parseInt(existIdx), 1)[0];
            deleteImageIds.push(removed.id); // 서버로 보낼 삭제 id
            renderImagePreview();
            return;
        }

        // 새 파일 삭제
        if (newIdx !== undefined) {
            selectedFiles.splice(parseInt(newIdx), 1);
            renderImagePreview();
            return;
        }
    });
}

/****************************************************
 *  식물 선택 모달 (enum + label)
 ****************************************************/
function bindPlantSelect() {
    document.addEventListener("click", (e) => {
        const btn = e.target.closest(".plant-select-btn");
        if (!btn) return;

        document.querySelector("#plantNameInput").value = btn.dataset.plantName;

        document.querySelector("#plantConditionInput").value = btn.dataset.levelLabel;
        document.querySelector("#plantDifficultyInput").value = btn.dataset.needsLabel;

        document.querySelector("#plantConditionInput").dataset.enum = btn.dataset.levelEnum;
        document.querySelector("#plantDifficultyInput").dataset.enum = btn.dataset.needsEnum;
    });
}

/****************************************************
 *  폼 제출 (PUT 요청)
 ****************************************************/
function bindSubmit() {
    const form = document.querySelector("form");
    const sharingId = document.body.dataset.sharingId;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const formData = new FormData();

        formData.append("memberId", document.body.dataset.memberId);
        formData.append("title", document.querySelector("#titleInput").value);
        formData.append("content", document.querySelector("#contentInput").value);
        formData.append("plantType", document.querySelector("#plantNameInput").value);

        // Enum
        formData.append("managementLevel", document.querySelector("#plantConditionInput").dataset.enum);
        formData.append("managementNeeds", document.querySelector("#plantDifficultyInput").dataset.enum);

        // 새 파일
        selectedFiles.forEach(f => formData.append("files", f));

        // 삭제된 기존 이미지 목록
        formData.append("deleteImageIds", JSON.stringify(deleteImageIds));

        try {
            const res = await axios.put(`/api/sharing/${sharingId}`, formData);

            showAlert("수정이 완료되었습니다.", () => {
                window.location.href = `/readSharing/${sharingId}`;
            });

        } catch (err) {
            console.error(err);
            showAlert("수정 중 오류가 발생했습니다.");
        }
    });
}

/****************************************************
 *  초기 실행
 ****************************************************/
async function initUpdateSharing() {
    await loadSharingForEdit();
    bindImageUploader();
    bindPlantSelect();
    bindSubmit();
}

document.addEventListener("DOMContentLoaded", initUpdateSharing);
