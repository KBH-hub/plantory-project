

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
        renderPagination();
    } catch (err) {
        console.log(err);
    }
}