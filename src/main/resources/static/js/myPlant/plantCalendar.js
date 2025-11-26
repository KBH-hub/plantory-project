document.addEventListener("DOMContentLoaded", function() {

    const calendarGrid = document.querySelector(".calendar-grid");
    const monthLabel = document.getElementById("monthLabel");
    const prevBtn = document.getElementById("prevMonth");
    const nextBtn = document.getElementById("nextMonth");

    let current = new Date();
    let currentYear = current.getFullYear();
    let currentMonth = current.getMonth() + 1;


    let diaryList = [];



    function renderCalendar(year, month) {
        calendarGrid.innerHTML = "";

        const firstDay = new Date(year, month - 1, 1).getDay();
        const lastDate = new Date(year, month, 0).getDate();

        monthLabel.innerText = `${year}년 ${month}월`;

        for (let i = 0; i < firstDay; i++) {
            calendarGrid.innerHTML += `<div class="calendar-cell empty"></div>`;
        }

        for (let day = 1; day <= lastDate; day++) {
            calendarGrid.innerHTML += `
        <div class="calendar-cell">
          <span>${day}</span>
        </div>
      `;
        }
    }

    prevBtn.addEventListener("click", function() {
        currentMonth--;
        if (currentMonth < 1) {
            currentMonth = 12;
            currentYear--;
        }
        renderCalendar(currentYear, currentMonth);
    });

    nextBtn.addEventListener("click", function() {
        currentMonth++;
        if (currentMonth > 12) {
            currentMonth = 1;
            currentYear++;
        }
        renderCalendar(currentYear, currentMonth);
    });

    let photoFiles = [];

    document.getElementById("addPhotoBtn").addEventListener("click", () => {
        if (photoFiles.length >= 5) {
            alert("사진은 최대 5장까지 업로드 가능합니다.");
            return;
        }

        document.getElementById("imageInput").value = "";
        document.getElementById("previewImage").classList.add("d-none");

        new bootstrap.Modal(document.getElementById("imageModal")).show();
    });

    document.getElementById("imageInput").addEventListener("change", e => {
        const file = e.target.files[0];
        if (!file) return;

        const img = document.getElementById("previewImage");
        img.src = URL.createObjectURL(file);
        img.classList.remove("d-none");
    });

    document.getElementById("saveImageBtn").addEventListener("click", () => {
        const file = document.getElementById("imageInput").files[0];
        if (!file) return;

        photoFiles.push(file);
        renderPhotoList();

        bootstrap.Modal.getInstance(document.getElementById("imageModal")).hide();
    });

    function renderPhotoList() {
        const list = document.getElementById("photoList");
        list.innerHTML = "";

        photoFiles.forEach((file, index) => {
            const url = URL.createObjectURL(file);

            const box = document.createElement("div");
            box.className = "position-relative";
            box.style = "width:110px;height:110px;";

            box.innerHTML = `
      <img src="${url}" class="w-100 h-100 rounded" style="object-fit:cover;">
      <button class="btn btn-danger btn-sm position-absolute top-0 end-0 p-0 px-1"
              onclick="removePhoto(${index})">×</button>
    `;

            list.appendChild(box);
        });

        const addBtn = document.createElement("div");
        addBtn.id = "addPhotoBtn";
        addBtn.className = "d-flex justify-content-center align-items-center border rounded";
        addBtn.style = "width:110px;height:110px;background:#efefef;cursor:pointer;";
        addBtn.innerHTML = `<span class="fs-1">+</span>`;

        addBtn.addEventListener("click", () => {
            if (photoFiles.length >= 5) {
                alert("사진은 최대 5장까지 업로드 가능합니다.");
                return;
            }
            new bootstrap.Modal(document.getElementById("imageModal")).show();
        });

        list.appendChild(addBtn);
    }

    window.removePhoto = function(idx) {
        photoFiles.splice(idx, 1);
        renderPhotoList();
    }


document.getElementById("saveDiaryBtn").addEventListener("click", () => {

    const data = {
        plant: document.getElementById("plantSelect").value,
        activity: document.getElementById("activityInput").value,
        condition: document.getElementById("conditionInput").value,
        memo: document.getElementById("memoInput").value,
        photos: [...photoFiles],
        date: new Date().toISOString().split("T")[0]
    };

    diaryList.push(data);


    appendDiaryCard(data);


    photoFiles = [];
    renderPhotoList();

    bootstrap.Modal.getInstance(document.getElementById("diaryModal")).hide();
});

function appendDiaryCard(diary) {
    const container = document.getElementById("diaryListContainer");

    let photoHtml = "";
    if (diary.photos.length > 0) {
        const photoUrl = URL.createObjectURL(diary.photos[0]);
        photoHtml = `<img src="${photoUrl}" width="60" height="60" class="rounded me-3">`;
    }

    const card = document.createElement("div");
    card.className = "d-flex flex-column bg-warning bg-opacity-10 rounded-2 border-start border-4 border-warning p-3 mb-3";

    card.innerHTML = `
        <div class="d-flex align-items-start">
            <div class="d-flex align-items-center">
                ${photoHtml}
                <div class="fw-bold">${diary.plant}</div>
            </div>

            <div class="ms-auto d-flex align-items-center gap-3">
                <span class="badge bg-warning text-dark">${diary.date}</span>
                <i class="fa-solid fa-xmark text-secondary fs-5 remove-btn"></i>
            </div>
        </div>

        <div class="mt-2 ps-1 bg-light rounded-1 p-1">
            <small class="text-muted">메모: ${diary.memo}</small>
        </div>
    `;

    container.prepend(card);
}




    renderCalendar(currentYear, currentMonth);

});