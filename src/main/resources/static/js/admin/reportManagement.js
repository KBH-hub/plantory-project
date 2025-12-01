async function initReport(){
    try {
        let res = await axios.get("/api/reportManagement/list",{
            params: {
                keyword: keyword,
                status: status,
            }
        })
        console.log(res.data)
    } catch (err) {
        console.log(err)
    }

}

const keyword = document.getElementById("keywordInput").value;
const status = document.getElementById("statusFilter").value;

document.addEventListener("DOMContentLoaded", async () => {
await initReport()
});

const modal = new bootstrap.Modal(document.getElementById("reportModal"));

document.querySelectorAll(".report-row").forEach(row => {
    row.addEventListener("click", e => {
        if (e.target.type === "checkbox") return;

        const cells = row.children;

        document.getElementById("reportedId").innerText = cells[1].innerText;
        document.getElementById("reporterId").innerText = cells[2].innerText;
        document.getElementById("reportContent").innerText = cells[4].innerText;
        document.getElementById("processOpinion").innerText = cells[5].innerText;

        modal.show();
    });
});