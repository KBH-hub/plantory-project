let existingImages = [];
let newImages = [];
let deletedImageIds = [];

const MAX_IMAGES = 5;
const questionId = document.body.dataset.questionId || null;

function uuid() {
    return "temp_" + Math.random().toString(36).substring(2, 11);
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
    const fileInput = document.getElementById("imageInput");
    const uploadBox = document.getElementById("imageUploadBox");
    const previewList = document.getElementById("previewList");

    uploadBox.addEventListener("click", () => fileInput.click());

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

function bindSubmit() {
    const submitBtn = document.getElementById("btnSubmit");
    let isSubmitting = false;

    submitBtn.addEventListener("click", async () => {
        if (isSubmitting) return;
        isSubmitting = true;
        submitBtn.disabled = true;
        submitBtn.innerText = "처리 중...";


    try{
        const title = document.getElementById("titleInput").value.trim();
        const content = document.getElementById("contentInput").value.trim();

        if (!title) {
            showAlert("제목을 입력하세요.");
            return;
        }

        if (!content) {
            showAlert("내용을 입력하세요.");
            return;
        }

        const formData = new FormData();
        formData.append("title", title);
        formData.append("content", content);
        formData.append("deletedImageIds", JSON.stringify(deletedImageIds));

        newImages.forEach(img => {
            formData.append("files", img.file);
        });

            if (questionId) {
                await axios.put(`/api/questions/${questionId}`, formData, {
                    headers: { "Content-Type": "multipart/form-data" }
                });

                showAlert("수정 완료되었습니다.", () => {
                    window.location.href = `/readQuestion/${questionId}`;
                });
                return;
            }

            const res = await axios.post("/api/questions", formData, {
                headers: { "Content-Type": "multipart/form-data" }
            });

            const savedId = res.data;
            showAlert("등록 완료되었습니다.", () => {
                window.location.href = `/readQuestion/${savedId}`;
            });

        } catch (err) {
            console.error(err);
            showAlert("저장 중 오류가 발생했습니다.");
        } finally {
            isSubmitting = false;
            submitBtn.disabled = false;
            submitBtn.innerText = questionId ? "수정" : "등록";
        }
    });
}

async function loadUpdateQuestion() {
    const res = await axios.get(`/api/questions/${questionId}`);
    const data = res.data;

    document.getElementById("titleInput").value = data.title;
    document.getElementById("contentInput").value = data.content;

    existingImages = data.images;
    renderImages();
}

async function initCreateQuestion() {
    bindImageUploader();
    bindSubmit();

    if (questionId) {
        document.getElementById("pageTitle").innerText = "질문글 수정";
        document.getElementById("btnSubmit").innerText = "수정";
        await loadUpdateQuestion();
    } else {
        document.getElementById("pageTitle").innerText = "질문글 등록";
        document.getElementById("btnSubmit").innerText = "등록";
    }
}

document.addEventListener("DOMContentLoaded", initCreateQuestion);
