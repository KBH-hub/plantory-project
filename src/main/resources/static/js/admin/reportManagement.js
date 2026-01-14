import { createPaginator } from '/js/common/pagination.js';

let limit = 8;
let offset = 0;
let totalCount = 0;

let currentReportId = null;
let currentTargetMemberId = null;
let paginator = null;

document.addEventListener('DOMContentLoaded', () => {

    const pagerEl = document.getElementById('pagination');
    paginator = createPaginator({
        container: pagerEl,
        current: 1,
        pageSize: limit,
        totalItems: 0,
        labels: { first:'«', prev:'‹', next:'›', last:'»' },
        onChange: (page) => {
            offset = (page - 1) * limit;
            loadReportList();
        }
    });

    loadReportList();
    document.getElementById("reportSubmitBtn").addEventListener("click", submitReportProcess);

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


        const data = res.data;

        totalCount = data.totalCount;
        renderTable(data.list);
        const currentPage = Math.floor(offset / limit) + 1;
        const totalPages = Math.max(1, Math.ceil(totalCount / limit));
        if (currentPage > totalPages) {
            offset = (totalPages - 1) * limit;
            await loadReportList();
            return;
        }
        if (paginator) {
            paginator.update({
                current: currentPage,
                pageSize: limit,
                totalItems: totalCount
            });
        }

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
    <td>
        <input type="checkbox" class="form-check-input report-check" data-id="${item.reportId}">
    </td>

    <td>${item.targetMemberId ?? "-"}</td>
    <td>${item.reporterId ?? "-"}</td>
    <td>${item.adminId ?? "-"}</td>

    <td class="text-truncate report-detail" 
        data-report-id="${item.reportId}"
        style="max-width:200px; cursor:pointer;">
        ${item.content}
    </td>

    <td>
        ${item.status === "true"
            ? `<span class="badge bg-success">처리완료</span>`
            : `<span class="badge bg-secondary">처리전</span>`}
    </td>

<td>
    <button class="btn btn-sm ${item.status === "true" ? "btn-secondary" : "btn-danger"} report-process-btn"
            data-report-id="${item.reportId}"
            data-target-id="${item.targetMemberId}"
            ${item.status === "true" ? "disabled" : ""}>
        신고처리
    </button>
</td>

    <td>${formatDate(item.createdAt)}</td>
`;


        tbody.appendChild(tr);
    });
}

document.addEventListener("click", (e) => {

    if (e.target.closest(".report-detail")) {
        const id = e.target.closest(".report-detail").dataset.reportId;
        openDetail(id);
    }

    if (e.target.closest(".report-process-btn")) {
        const btn = e.target.closest(".report-process-btn");
        openReport(btn.dataset.reportId, btn.dataset.targetId);
    }
});


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

const reportProcessModal = new bootstrap.Modal(document.getElementById("reportProcessModal"), {
    backdrop: "static"
});

const reportProcessModalElement = document.getElementById("reportProcessModal");

reportProcessModalElement.addEventListener("hidden.bs.modal", () => {
    document.activeElement.blur();
});

reportProcessModalElement.addEventListener("shown.bs.modal", () => {
    reportProcessModalElement.focus();
});


document.getElementById("modalCloseBtn").addEventListener("click", e => {
    e.target.blur();
});

async function openReport(reportId, targetMemberId) {
    currentReportId = reportId;
    currentTargetMemberId = targetMemberId;

    reportProcessModal.show();
}

async function submitReportProcess() {
    const adminMemo = document.getElementById("reportMemo").value.trim();
    const stopDay = document.getElementById("selectStopDay").value;


    if (!adminMemo) {
        showAlert("처리 의견을 입력해주세요.");
        return;
    }

    try {
        const res = await axios.post(`/api/reportManagement/${currentReportId}`, {
            targetMemberId: currentTargetMemberId,
            adminMemo: adminMemo,
            stopDays: stopDay
        });

        const modalEl = document.getElementById("reportProcessModal");
        const modal = bootstrap.Modal.getInstance(modalEl);
        modal.hide();

        await loadReportList();

        showAlert("신고가 정상 처리되었습니다.");

    } catch (err) {
        console.error(err);
        showAlert("신고 처리 중 오류가 발생했습니다.");
    }
}

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
    try {
        const res = await axios.put("/api/reportManagement/softDelete", { ids });
        showAlert("삭제되었습니다.");

        const topCheckbox = document.getElementById("reportCheckbox");
        if (topCheckbox) topCheckbox.checked = false;

        loadReportList();

    } catch (error) {
        console.error(error);
    }
}

