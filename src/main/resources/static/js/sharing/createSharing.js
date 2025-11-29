let existingImages = [];
let newImages = [];
let deletedImageIds = [];

const MAX_IMAGES = 5;
const sharingId = document.body.dataset.sharingId;

function uuid() {
    return 'temp_' + Math.random().toString(36).substring(2, 11);
}

function renderImages() {
    const previewList = document.getElementById("previewList");
    const imgCount = document.getElementById("imgCount");

    previewList.innerHTML = "";

    existingImages.forEach((img) => {
        const box = document.createElement("div");
        box.className = "position-relative";
        box.style.width = "120px";
        box.style.height = "120px";

        box.innerHTML = `
            <img src="${img.fileUrl}" class="rounded border"
                 style="width:120px;height:120px;object-fit:cover;">
            <button class="btn btn-sm btn-danger position-absolute top-0 end-0"
                    data-type="existing" data-id="${img.imageId}">
                <i class="bi bi-x-lg small"></i>
            </button>
        `;
        previewList.appendChild(box);
    });

    newImages.forEach((img) => {
        const url = URL.createObjectURL(img.file);

        const box = document.createElement("div");
        box.className = "position-relative";
        box.style.width = "120px";
        box.style.height = "120px";

        box.innerHTML = `
            <img src="${url}" class="rounded border"
                 style="width:120px;height:120px;object-fit:cover;">
            <button class="btn btn-sm btn-danger position-absolute top-0 end-0"
                    data-type="new" data-id="${img.key}">
                <i class="bi bi-x-lg small"></i>
            </button>
        `;
        previewList.appendChild(box);
    });

    imgCount.textContent = existingImages.length + newImages.length;
}

function bindImageUploader() {
    const fileInput = document.querySelector("#plantImages");
    const addTile = document.querySelector("#addTile");
    const previewList = document.getElementById("previewList");

    addTile.addEventListener("click", () => fileInput.click());

    fileInput.addEventListener("change", (e) => {
        const files = Array.from(e.target.files);

        if (existingImages.length + newImages.length + files.length > MAX_IMAGES) {
            showAlert("최대 5장까지만 업로드할 수 있습니다.");
            return;
        }

        files.forEach((file) => {
            newImages.push({
                key: uuid(),
                file: file
            });
        });

        renderImages();
    });

    previewList.addEventListener("click", (e) => {
        const btn = e.target.closest("button");
        if (!btn) return;

        const type = btn.dataset.type;
        const id = btn.dataset.id;

        if (type === "existing") {
            deletedImageIds.push(Number(id));
            existingImages = existingImages.filter(img => img.imageId !== Number(id));
        } else {
            newImages = newImages.filter(img => img.key !== id);
        }

        renderImages();
    });
}

function bindPlantSelect() {
    document.addEventListener("click", (e) => {
        const btn = e.target.closest(".plant-select-btn");
        if (!btn) return;

        document.querySelector("#plantNameInput").value = btn.dataset.plantName;
        document.querySelector("#managementLevel").value = btn.dataset.levelLabel;
        document.querySelector("#managementNeeds").value = btn.dataset.needsLabel;

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
        formData.append("title", document.querySelector("#titleInput").value);
        formData.append("content", document.querySelector("#contentInput").value);
        formData.append("plantType", document.querySelector("#plantNameInput").value);

        formData.append("managementLevel",
            document.querySelector("#managementLevel").dataset.enum);
        formData.append("managementNeeds",
            document.querySelector("#managementNeeds").dataset.enum);

        formData.append("deletedImageIds", JSON.stringify(deletedImageIds));

        newImages.forEach(img => {
            formData.append("files", img.file);
        });

        try {
            if (sharingId) {
                await axios.put(`/api/sharing/${sharingId}`, formData, {
                    headers: { "Content-Type": "multipart/form-data" },
                });

                showAlert("수정 완료되었습니다.");
                window.location.href = `/readSharing/${sharingId}`;
                return;
            }

            const res = await axios.post("/api/sharing", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });

            const savedId = res.data;
            showAlert("등록 완료되었습니다.");
            window.location.href = `/readSharing/${savedId}`;

        } catch (err) {
            console.error(err);
            showAlert("저장 중 오류가 발생했습니다.");
        }
    });
}

async function loadUpdateSharing() {
    const res = await axios.get(`/api/sharing/${sharingId}`);
    const data = res.data;

    document.querySelector("#plantNameInput").value = data.plantType;
    document.querySelector("#titleInput").value = data.title;
    document.querySelector("#contentInput").value = data.content;

    document.querySelector("#managementLevel").value = data.managementLevelLabel;
    document.querySelector("#managementNeeds").value = data.managementNeedsLabel;

    document.querySelector("#managementLevel").dataset.enum = data.managementLevel;
    document.querySelector("#managementNeeds").dataset.enum = data.managementNeeds;

    existingImages = data.images; // [{imageId, fileUrl}, ...]
    renderImages();
}

async function initCreateSharing() {
    // renderImages();
    bindImageUploader();
    bindPlantSelect();
    bindSubmit();

    if (sharingId) {
        document.getElementById("pageTitle").innerText = "나눔글 수정";
        document.getElementById("submitBtn").innerText = "수정";
        await loadUpdateSharing();
    } else {
        document.getElementById("pageTitle").innerText = "나눔글 등록";
        document.getElementById("submitBtn").innerText = "등록";
    }
}

document.addEventListener("DOMContentLoaded", initCreateSharing);
