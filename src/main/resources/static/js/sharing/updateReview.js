async function loadReviewInfo() {
    const sharingId = document.body.dataset.sharingId;

    try {
        const res = await axios.get(`/api/sharing/${sharingId}/reviewInfo`);

        const data = res.data;

        document.getElementById("partnerNickname").innerText = data.partnerNickname;
        document.getElementById("postTitle").innerText = data.title;
        document.getElementById("createdAt").innerText = formatDate(data.createdAt);

        document.body.dataset.partnerId = data.partnerId;

        if (data.reviewerType === "RECEIVER") {
            document.getElementById("receiverOnly").style.display = "block";
        } else {
            document.getElementById("receiverOnly").style.display = "none";
        }


    } catch (err) {
        console.error(err);
        showAlert("후기 정보를 불러오지 못했습니다.");
    }
}

// 2) 후기 제출하기
function bindSubmitReview() {
    document.getElementById("btnSubmitReview").addEventListener("click", async () => {

        const sharingId = document.body.dataset.sharingId;

        const manner = getSelectedValue("manner");
        const reShare = getSelectedValue("renanum");
        const satisfaction = getSelectedValue("satisfaction");

        if (!manner || !reShare) {
            showAlert("필수 항목을 선택해주세요.");
            return;
        }

        try {
            await axios.post(`/api/sharing/${sharingId}/review`, {
                manner: Number(manner),
                reShare: Number(reShare),
                satisfaction: satisfaction ? Number(satisfaction) : null
            });

            showAlert("후기가 등록되었습니다.");
            location.href = `/dashboard`;

        } catch (err) {
            console.error(err);
            showAlert("후기 등록 중 오류가 발생했습니다.");
        }
    });
}

function getSelectedValue(name) {
    const selected = document.querySelector(`input[name="${name}"]:checked`);
    return selected ? selected.id.replace(/\D/g, "") : null;
}


document.addEventListener("DOMContentLoaded", () => {
    loadReviewInfo();
    bindSubmitReview();
});
