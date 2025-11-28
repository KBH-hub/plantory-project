document.addEventListener("DOMContentLoaded", () => {
    initMyInfo();
    initMyWrittenPage();
});

function renderMyInfo(resMyInfoData) {
    const profileNicknameTag = document.getElementById("profileNickname");
    const profileAddress = document.getElementById("profileAddress");
    const sharingRate = document.getElementById("sharingRate");
    profileNicknameTag.textContent = resMyInfoData.nickname;
    profileAddress.textContent = resMyInfoData.address;
    sharingRate.textContent = "나눔지수: " + resMyInfoData.sharingRate;
}

async function initMyInfo() {
    try {
        const resMyInfo = await axios.get("/api/myProfile");
        renderMyInfo(resMyInfo.data);
    } catch (error) {
        console.log(error);
        showAlert("내 정보를 불러오지 못했습니다.");
    }
}

async function initMyWrittenPage() {
    try {
        const resMyInfo = await axios.get("/api/");
        renderMyInfo(resMyInfo.data);
    } catch (error) {
        console.log(error);
        // showAlert("내 정보를 불러오지 못했습니다.");
    }
}