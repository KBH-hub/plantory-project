document.getElementById("openAddModal").addEventListener("click", () => {
    new bootstrap.Modal(document.getElementById("addPlantModal")).show();
});

const dummyPlants = [
    { id: 1, name: "방토방토", img: "https://picsum.photos/id/111/400/300", days: 125 },
    { id: 2, name: "다육이", img: "https://picsum.photos/id/122/400/300", days: 25 },
    { id: 3, name: "몬stera", img: "", days: 125 },
    { id: 4, name: "행운목", img: "https://picsum.photos/id/133/400/300", days: 10 },
    { id: 5, name: "산세베리아", img: "https://picsum.photos/id/155/400/300", days: 45 },
    { id: 6, name: "칼라디움", img: "", days: 80 }
];

let plants = [...dummyPlants];
let filtered = [...plants];
let currentPage = 1;
const limit = 3;

document.addEventListener("DOMContentLoaded", () => loadPage(1));
document.getElementById("searchBtn").addEventListener("click", filterPlants);
document.getElementById("search").addEventListener("keyup", e => e.key === "Enter" && filterPlants());

function filterPlants() {
    const keyword = document.getElementById("search").value.toLowerCase();
    filtered = plants.filter(p => p.name.toLowerCase().includes(keyword));
    loadPage(1);
}

function loadPage(page) {
    currentPage = page;
    const start = (page - 1) * limit;
    renderCards(filtered.slice(start, start + limit));
    renderPagination();
}

function renderCards(items) {
    document.getElementById("plant-list").innerHTML = items.map(p => `
    <div class="col-12 col-md-4">
      <div class="card plant-card shadow-sm border-0 h-100" onclick='openDetailModal(${JSON.stringify(p)})'>
        ${p.img
        ? `<img src="${p.img}" class="card-img-top rounded-top" />`
        : `<div class="bg-light d-flex justify-content-center align-items-center" style="height:260px;">
               <i class="bi bi-image fs-1 text-muted"></i>
             </div>`}
        <div class="card-body text-center">
          <h6 class="fw-bold">${p.name}</h6>
          <p class="text-muted small mb-0">함께한지 +${p.days}일</p>
        </div>
      </div>
    </div>
  `).join("");
}

function renderPagination() {
    const totalPages = Math.ceil(filtered.length / limit);
    const pagination = document.getElementById("pagination");
    let html = "";

    const btn = (page, text, disabled, active = false) => `
    <li class="page-item ${disabled ? "disabled" : ""} ${active ? "active" : ""}">
      <button class="page-link" data-page="${page}">${text}</button>
    </li>`;

    html += btn(1, "&laquo;", currentPage === 1);
    html += btn(currentPage - 1, "&lsaquo;", currentPage === 1);
    for (let i = 1; i <= totalPages; i++) html += btn(i, i, false, i === currentPage);
    html += btn(currentPage + 1, "&rsaquo;", currentPage === totalPages);
    html += btn(totalPages, "&raquo;", currentPage === totalPages);

    pagination.innerHTML = html;

    pagination.addEventListener("click", e => {
        if (e.target.dataset.page) loadPage(Number(e.target.dataset.page));
    });
}

function openDetailModal(p) {
    document.getElementById("detailImg").src = p.img || "https://via.placeholder.com/160x200";
    document.getElementById("detailName").value = p.name;
    document.getElementById("detailWater").value = p.days + "일 경과";
    new bootstrap.Modal(document.getElementById("plantDetailModal")).show();
}

document.getElementById("saveWatering").addEventListener("click", () => {
    bootstrap.Modal.getInstance(document.getElementById("wateringModal")).hide();
    setTimeout(() => {
        new bootstrap.Modal(document.getElementById("addPlantModal")).show();
    }, 100);
});
