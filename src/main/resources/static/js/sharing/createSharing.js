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

async function loadInitialData() {
    // 필요 시 초기 데이터 불러오기
}

function bindImageUploader() {
    const fileInput = document.querySelector("#plantImages");
    const addTile = document.querySelector("#addTile");
    const previewList = document.querySelector("#previewList");

    addTile.addEventListener("click", () => fileInput.click());

    fileInput.addEventListener("change", (e) => {
        const files = Array.from(e.target.files);

        if (selectedFiles.length + files.length > MAX_IMAGES) {
            alert("최대 5장까지만 업로드할 수 있습니다.");
            return;
        }

        selectedFiles = [...selectedFiles, ...files];
        renderImagePreview();
    });

    previewList.addEventListener("click", (e) => {
        const idx = e.target.closest("button")?.dataset.idx;
        if (idx === undefined) return;

        selectedFiles.splice(parseInt(idx), 1);
        renderImagePreview();
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
        formData.append("content", document.querySelector("textarea").value);
        formData.append("plantType", document.querySelector("#plantNameInput").value);
        formData.append("managementLevel", document.querySelector("#plantConditionInput").value);
        formData.append("managementNeeds", document.querySelector("#plantDifficultyInput").value);
        formData.append("targetMemberId", null);


        selectedFiles.forEach(file => formData.append("files", file));

        try {
            const res = await axios.post("/api/sharing", formData, {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            });

            const sharingId = res.data;
            alert("등록 완료!");
            window.location.href = `/sharing/${sharingId}`;

        } catch (err) {
            console.error(err);
            alert("등록 중 오류가 발생했습니다.");
        }
    });
}

async function initCreateSharing() {
    renderImagePreview();
    bindImageUploader();
    bindSubmit();
    await loadInitialData();
}

document.addEventListener("DOMContentLoaded", initCreateSharing);
