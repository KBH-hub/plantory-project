document.addEventListener("DOMContentLoaded", () => {
    initProfileInfo();
    initPublicProfilePage();
});

function renderMyInfo(resMyInfoData) {
    const profileNicknameTag = document.getElementById("profileNickname");
    const profileAddress = document.getElementById("profileAddress");
    const sharingRate = document.getElementById("sharingRate");
    profileNicknameTag.textContent = resMyInfoData.nickname;
    profileAddress.textContent = resMyInfoData.address;
    sharingRate.textContent = "나눔지수: " + resMyInfoData.sharingRate;
}

async function initProfileInfo() {
    try {
        const resProfileInfo = await axios.get(`/api/profile/me`);
        renderMyInfo(resProfileInfo.data);
    } catch (error) {
        console.log(error);
        showAlert("내 정보를 불러오지 못했습니다.");
    }
}

async function initPublicProfilePage() {
    const profileId = document.body.dataset.profileId;

    try {
        const resPublicProfileInfo = await axios.get(`/api/profile/publicProfile/${profileId}`);
        renderMyInfo(resPublicProfileInfo.data);
    } catch (error) {
        console.log(error);
        // showAlert("내 정보를 불러오지 못했습니다.");
    }
}