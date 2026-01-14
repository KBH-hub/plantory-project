export function createImageUploader({
                                        inputId,
                                        previewListId,
                                        countId,
                                        max = 5
                                    }) {
    let existingImages = [];
    let newImages = [];
    let deletedImageIds = [];

    const fileInput = document.getElementById(inputId);
    const previewList = document.getElementById(previewListId);
    const countBox = document.getElementById(countId);

    function uuid() {
        return 'tmp_' + Math.random().toString(36).substring(2, 11);
    }

    function updateCount() {
        if (countBox) {
            countBox.textContent = `${existingImages.length + newImages.length}/${max}`;
        }
    }

    function render() {
        previewList.innerHTML = "";

        existingImages.forEach(img => {
            previewList.insertAdjacentHTML(
                "beforeend",
                `
                <div class="position-relative" style="width:120px; height:120px;">
                    <img src="${img.fileUrl}" class="border rounded object-fit-cover"
                        style="width:100%; height:100%">
                    <button class="btn btn-sm btn-danger position-absolute top-0 end-0"
                        data-type="existing" data-id="${img.imageId}">
                        <i class="bi bi-x-lg small"></i>
                    </button>
                </div>
                `
            );
        });

        newImages.forEach(img => {
            const url = URL.createObjectURL(img.file);

            previewList.insertAdjacentHTML(
                "beforeend",
                `
                <div class="position-relative" style="width:120px; height:120px;">
                    <img src="${url}" class="border rounded object-fit-cover"
                        style="width:100%; height:100%">
                    <button class="btn btn-sm btn-danger position-absolute top-0 end-0"
                        data-type="new" data-id="${img.key}">
                        <i class="bi bi-x-lg small"></i>
                    </button>
                </div>
                `
            );
        });

        updateCount();
    }

    function bindEvents() {
        fileInput.addEventListener("change", (e) => {
            const files = Array.from(e.target.files);

            if (existingImages.length + newImages.length + files.length > max) {
                showAlert(`이미지는 최대 ${max}장까지 업로드 가능합니다.`);
                return;
            }

            files.forEach(file => {
                newImages.push({
                    key: uuid(),
                    file
                });
            });

            render();
            fileInput.value = "";
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

            render();
        });
    }

    bindEvents();

    return {
        setExistingImages(list) {
            existingImages = list;
            render();
        },
        getNewImages() {
            return newImages.map(img => img.file);
        },
        getDeletedIds() {
            return deletedImageIds;
        },
        getTotalCount() {
            return existingImages.length + newImages.length;
        }
    };
}
