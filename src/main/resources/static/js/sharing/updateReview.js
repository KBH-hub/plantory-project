document.addEventListener("DOMContentLoaded", loadReviewInfo);


async function loadReviewInfo() {
    const sharingId = document.body.dataset.sharingId;

    try {
        const res = await axios.get(`/api/sharing/giver`, {
            params: { sharingId }
        });

        const data = res.data;

        document.getElementById("partnerNickname").innerText = data.partnerNickname;
        document.getElementById("postTitle").innerText = data.title;
        document.getElementById("createdAt").innerText = timeAgo(data.createdAt);

        document.body.dataset.partnerId = data.partnerId;

    } catch (err) {
        console.error(err);
        showAlert("후기 정보를 불러오지 못했습니다.");
    }
}
