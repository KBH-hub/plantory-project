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