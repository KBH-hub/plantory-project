let selectedFiles = [];
const MAX_IMAGES = 5;

function renderImagePreview() {
    const previewList = document.querySelector("#previewList");
    const imgCount = document.querySelector("#imgCount");

    previewList.innerHTML = "";

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
                    data-idx="${idx}">
                <i class="bi bi-x-lg small"></i>
            </button>
        `;
        previewList.appendChild(box);
    });

    imgCount.textContent = selectedFiles.length;
}


function bindImageUploader() {
    const fileInput = document.querySelector("#plantImages");
    const addTile = document.querySelector("#addTile");
    const previewList = document.querySelector("#previewList");

    addTile.addEventListener("click", () => fileInput.click());

    fileInput.addEventListener("change", (e) => {
        const files = Array.from(e.target.files);

        if (selectedFiles.length + files.length > MAX_IMAGES) {
            showAlert("최대 5장까지만 업로드할 수 있습니다.");
            return;
        }

        selectedFiles = [...selectedFiles, ...files];
        renderImagePreview();
    });

    previewList.addEventListener("click", (e) => {
        const idx = e.target.closest("button")?.dataset.idx;
        if (!idx) return;

        selectedFiles.splice(parseInt(idx), 1);
        renderImagePreview();
    });
}

function bindPlantSelect() {
    document.addEventListener("click", (e) => {
        const btn = e.target.closest(".plant-select-btn");
        if (!btn) return;

        // 1) 사용자 보여줄 값
        document.querySelector("#plantNameInput").value = btn.dataset.plantName;
        document.querySelector("#managementLevel").value = btn.dataset.levelLabel;
        document.querySelector("#managementNeeds").value = btn.dataset.needsLabel;

        // 2) 서버로 전송할 ENUM NAME 저장 (dataset.enum)
        document.querySelector("#managementLevel").dataset.enum = btn.dataset.levelEnum;
        document.querySelector("#managementNeeds").dataset.enum = btn.dataset.needsEnum;
    });
}


function bindSubmit() {
    const form = document.querySelector("form");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const formData = new FormData();
        const memberId = document.body.dataset.memberId;

        formData.append("memberId", memberId);
        formData.append("title", document.querySelector("input[placeholder='제목을 입력해 주세요.']").value);
        formData.append("content", document.querySelector("#contentInput").value);
        formData.append("plantType", document.querySelector("#plantNameInput").value);

        const managementLevel = document.querySelector("#managementLevel").dataset.enum;
        const managementNeeds = document.querySelector("#managementNeeds").dataset.enum;

        formData.append("managementLevel", managementLevel);
        formData.append("managementNeeds", managementNeeds);

        selectedFiles.forEach(file => formData.append("files", file));

        try {
            const res = await axios.post("/api/sharing", formData, {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            });

            const sharingId = res.data;
            showAlert("등록 완료되었습니다.");
            window.location.href = `/readSharing/${sharingId}`;

        } catch (err) {
            console.error(err);
            showAlert("등록 중 오류가 발생했습니다.");
        }
    });
}

async function loadUpdateSharing() {
    const sharingId = extractFromURL();
    const res = await axios.get(`/api/sharing/${sharingId}`);
    const data = res.data;

    document.querySelector("#plantNameInput").value = data.plantType;
    document.querySelector("#titleInput").value = data.title;
    document.querySelector("#contentInput").value = data.content;

    document.querySelector("#managementLevel").value = data.managementLevelLabel;
    document.querySelector("#managementNeeds").value = data.managementNeedsLabel;

    document.querySelector("#managementLevel").dataset.enum = data.managementLevel;
    document.querySelector("#managementNeeds").dataset.enum = data.managementNeeds;

    renderExistingImages(data.images);
}


async function initCreateSharing() {
    renderImagePreview();
    bindImageUploader();
    bindPlantSelect();
    bindSubmit();
    await loadInitialData();
}

document.addEventListener("DOMContentLoaded", initCreateSharing);
