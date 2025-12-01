let limit = 8;
let offset = 0;
let totalCount = 0;

let reportModal = null;

document.addEventListener('DOMContentLoaded', () => {
    reportModal = new bootstrap.Modal(document.getElementById("reportModal"));
    loadReportList();

    document.getElementById("statusFilter").addEventListener("change", () => {
        offset = 0;
        loadReportList();
    });

    document.getElementById("keywordInput").addEventListener("keyup", (e) => {
        if (e.key === "Enter") {
            offset = 0;
            loadReportList();
        }
    });

    document.getElementById("deleteBtn").addEventListener("click", deleteSelectedReports);
})

document.getElementById("searchBtn").addEventListener("click", async () => {
    offset = 0;
    await loadReportList();
});
async function loadReportList() {
    const status = document.getElementById("statusFilter").value;
    const keyword = document.getElementById("keywordInput").value;

    try {
        const res = await axios.get("/api/reportManagement/list", {
            params: {
                keyword,
                status,
                limit,
                offset
            }
        });

        // console.log(res.data);

        const data = res.data;

        totalCount = data.totalCount;
        renderTable(data.list);
        renderPagination();
    } catch (err) {
        console.log(err);
    }
}


function renderTable(list) {
    const tbody = document.getElementById("reportTableBody");
    tbody.innerHTML = "";

    if (!list || list.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" class="text-center py-4 text-muted">조회된 신고가 없습니다.</td>
            </tr>
        `;
        return;
    }

    list.forEach(item => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td><input type="checkbox" class="form-check-input report-check" data-id="${item.reportId}"></td>
            <td>${item.targetMemberId ?? "-"}</td>
            <td>${item.reporterId ?? "-"}</td>
            <td>${item.adminId ?? "-"}</td>
            <td class="text-truncate" style="max-width:200px;">${item.content}</td>
            <td>${item.status === "true" ? "처리완료" : "처리전"}</td>
            <td><button class="btn btn-sm btn-outline-secondary" onclick="openDetail(${item.reportId})">보기</button></td>
            <td>${formatDate(item.createdAt)}</td>
        `;

        tbody.appendChild(tr);
    });
}

document.getElementById("reportCheckbox").addEventListener("change", (e) => {
    const checked = e.target.checked;

    document.querySelectorAll(".report-check").forEach(cb => {
        cb.checked = checked;
    });
});


const reportDetailModal = new bootstrap.Modal(document.getElementById("reportDetailModal"), {
    backdrop: "static",
    keyboard: false
});

const reportDetailModalElement = document.getElementById("reportDetailModal");

reportDetailModalElement.addEventListener("hidden.bs.modal", () => {
    document.activeElement.blur();
});

reportDetailModalElement.addEventListener("shown.bs.modal", () => {
    reportDetailModalElement.focus();
});

document.getElementById("modalCloseBtn").addEventListener("click", e => {
    e.target.blur();
});

async function openDetail(reportId) {

    try {
        const res = await axios.get(`/api/reportManagement/detail/${reportId}`);
        const report = res.data;

        document.getElementById("rd-reporterName").textContent = report.reporterName;
        document.getElementById("rd-targetName").textContent = report.targetName;
        document.getElementById("rd-content").textContent = report.content;
        document.getElementById("rd-createdAt").textContent = formatDate(report.createdAt);
        document.getElementById("rd-adminMemo").textContent = report.adminMemo ?? "-";

        const imgRes = await axios.get("/api/reportManagement/images", {
            params: {
                targetType: "REPORT",
                targetId: reportId
            }
        });

        const images = imgRes.data;

        const img = document.getElementById("rd-evidenceImage");
        const noEvidence = document.getElementById("rd-noEvidence");

        if (images && images.length > 0 && images[0].fileUrl) {
            img.src = images[0].fileUrl;
            img.classList.remove("d-none");
            noEvidence.classList.add("d-none");
        } else {
            img.classList.add("d-none");
            noEvidence.classList.remove("d-none");
        }


        reportDetailModal.show();
    } catch (err) {
        console.error(err);
    }
}


function formatDate(isoString) {
    if (!isoString) return "-";

    const date = new Date(isoString);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} `
        + `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
}




async function deleteSelectedReports() {
    const checked = [...document.querySelectorAll(".report-check:checked")];

    if (checked.length === 0) {
        return;
    }

    const ids = checked.map(c => Number(c.dataset.id));
// console.log(ids);
    try {
        const res = await axios.put("/api/reportManagement/softDelete", { ids });
        console.log(res);
        showAlert("삭제되었습니다.");
        loadReportList();

    } catch (error) {
        console.error(error);
    }
}
function renderPagination() {
    const pagination = document.getElementById("pagination");
    pagination.innerHTML = "";

    const totalPage = Math.ceil(totalCount / limit);
    const currentPage = Math.floor(offset / limit) + 1;

    if (totalPage <= 1) return;

    const pageGroupSize = 5;

    const currentGroup = Math.ceil(currentPage / pageGroupSize);
    const groupStart = (currentGroup - 1) * pageGroupSize + 1;
    const groupEnd = Math.min(currentGroup * pageGroupSize, totalPage);


    const firstLi = document.createElement("li");
    firstLi.className = "page-item " + (currentGroup === 1 ? "disabled" : "");

    firstLi.innerHTML = `
        <a class="page-link" href="#" aria-label="First">
            &laquo;&laquo;
        </a>
    `;

    firstLi.addEventListener("click",  () => {
        if (currentGroup > 1) {
            const newPage = groupStart - pageGroupSize;
            offset = (newPage - 1) * limit;
             loadReportList();
        }
    });

    pagination.appendChild(firstLi);


    const prevLi = document.createElement("li");
    prevLi.className = "page-item " + (currentPage === 1 ? "disabled" : "");

    prevLi.innerHTML = `
        <a class="page-link" href="#" aria-label="Previous">
            &laquo;
        </a>
    `;

    prevLi.addEventListener("click",  () => {
        if (currentPage > 1) {
            offset = (currentPage - 2) * limit;
             loadReportList();
        }
    });

    pagination.appendChild(prevLi);


    for (let p = groupStart; p <= groupEnd; p++) {
        const li = document.createElement("li");
        li.className = "page-item " + (p === currentPage ? "active" : "");

        li.innerHTML = `<a class="page-link" href="#">${p}</a>`;

        li.addEventListener("click", () => {
            offset = (p - 1) * limit;
            loadReportList();
        });

        pagination.appendChild(li);
    }


    const nextLi = document.createElement("li");
    nextLi.className = "page-item " + (currentPage === totalPage ? "disabled" : "");

    nextLi.innerHTML = `
        <a class="page-link" href="#" aria-label="Next">
            &raquo;
        </a>
    `;

    nextLi.addEventListener("click",  () => {
        if (currentPage < totalPage) {
            offset = currentPage * limit;
             loadReportList();
        }
    });

    pagination.appendChild(nextLi);


    const lastLi = document.createElement("li");
    lastLi.className = "page-item " + (groupEnd === totalPage ? "disabled" : "");

    lastLi.innerHTML = `
        <a class="page-link" href="#" aria-label="Last">
            &raquo;&raquo;
        </a>
    `;

    lastLi.addEventListener("click",  () => {
        if (groupEnd < totalPage) {
            const newPage = groupEnd + 1;
            offset = (newPage - 1) * limit;
             loadReportList();
        }
    });

    pagination.appendChild(lastLi);
}

