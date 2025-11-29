let originalNickname = "";

document.addEventListener("DOMContentLoaded", async () => {
    const res = await axios.get("/api/profile/me");
    const profile = res.data;

    originalNickname = profile.nickname;

    await window.koreaDataLoaded;

    applyAddress(profile.address);
    initEvents();

    await initUpdateProfileInfo();
});

function applyAddress(address) {
    if (!address) return;

    const [sido, sigungu] = address.split(" ");

    const sidoSelect = document.getElementById("sido");
    const sigunguSelect = document.getElementById("sigungu");

    sidoSelect.value = sido;

    sidoSelect.dispatchEvent(new Event("change"));

    setTimeout(() => {
        sigunguSelect.value = sigungu;
    }, 10);
}




async function initUpdateProfileInfo() {
    try {
        const res = await axios.get("/api/profile/me");
        const data = res.data;

        console.log(data);

        document.getElementById("nicknameInput").value = data.nickname;
        document.getElementById("phoneInput").value = data.phone;
        document.getElementById("noticeToggle").checked = data.noticeEnabled === 1;

        applyAddress(data.address);

    } catch (e) {
        console.error(e);
        showAlert("프로필 정보를 불러오지 못했습니다.");
    }
}

function initEvents() {
    document.getElementById("checkNicknameBtn").addEventListener("click", checkNickname);
    document.getElementById("submitProfileBtn").addEventListener("click", submitProfile);
    document.getElementById("cancelBtn").addEventListener("click", goBack);

    document.getElementById("pwConfirmBtn").addEventListener("click", changePassword);
    document.getElementById("agreeWithdrawCheck").addEventListener("change", toggleWithdrawBtn);
    document.getElementById("withdrawBtn").addEventListener("click", withdrawMember);
}


async function checkNickname() {
    const nicknameInput = document.getElementById("nicknameInput").value.trim();

    if (!nicknameInput) return showAlert("닉네임을 입력해주세요.");

    if (nicknameInput === originalNickname) {
        return showAlert("닉네임이 기존과 동일합니다.");
    }

    try {
        await axios.get(`/api/members/exists?nickname=${nicknameInput}`);
        showAlert("사용 가능한 닉네임입니다.");
    } catch (err) {
        console.log(err);

        if (err.code === "DUPLICATE_NICKNAME") {
            return showAlert("이미 사용 중인 닉네임입니다.");
        }

        showAlert("닉네임 확인 중 오류가 발생했습니다.");
    }
}


async function submitProfile(event) {
    event.preventDefault();

    const nickname = document.getElementById("nicknameInput").value.trim();
    const phone = document.getElementById("phoneInput").value.trim();
    const sido = document.getElementById("sidoSelect").value;
    const sigungu = document.getElementById("sigunguSelect").value;
    const address = `${sido} ${sigungu}`;
    const noticeEnabled = document.getElementById("noticeToggle").checked ? 1 : 0;

    const req = {
        nickname,
        phone,
        address,
        noticeEnabled
    };

    try {
        const res = await axios.put("/api/profile/update", req);
        showSuccess("프로필이 수정되었습니다.");
    } catch (err) {
        console.log(err);
        showAlert("프로필 수정에 실패했습니다.");
    }
}

async function changePassword() {
    const oldPwInput = document.getElementById("oldPwInput").value.trim();
    const newPwInput = document.getElementById("newPwInput").value.trim();
    const newPwCheckInput = document.getElementById("newPwCheckInput").value.trim();

    hidePwErrorMsgs();

    if (!oldPwInput || !newPwInput || !newPwCheckInput) {
        return showAlert("모든 비밀번호를 입력해주세요.");
    }

    if (newPwInput !== newPwCheckInput) {
        document.getElementById("newPwCheckMsg").classList.remove("d-none");
        return;
    }

    try {
        const res = await axios.put("/api/profile/changePassword", {
            oldPassword: oldPwInput,
            newPassword: newPwInput
        });

        if (res.data.success) {
            showAlert("비밀번호가 변경되었습니다.");
            bootstrap.Modal.getInstance(document.getElementById("changePwModal")).hide();
        } else {
            document.getElementById("oldPwMsg").classList.remove("d-none");
        }
    } catch (err) {
        console.error(err);
        showAlert("비밀번호 변경 중 오류가 발생했습니다.");
    }
}


function hidePwErrorMsgs() {
    document.getElementById("oldPwMsg").classList.add("d-none");
    document.getElementById("newPwMsg").classList.add("d-none");
    document.getElementById("newPwCheckMsg").classList.add("d-none");
}

function toggleWithdrawBtn() {
    const checked = document.getElementById("agreeWithdrawCheck").checked;
    const btn = document.getElementById("withdrawBtn");
    const msg = document.getElementById("withdrawMsg");

    if (checked) {
        btn.disabled = false;
        msg.classList.add("d-none");
    } else {
        btn.disabled = true;
        msg.classList.remove("d-none");
    }
}

async function withdrawMember() {
    try {
        const res = await axios.delete("/api/profile/withdraw");

        if (res.data.success) {
            showSuccess("회원탈퇴가 완료되었습니다.");
            setTimeout(() => {
                window.location.href = "/logout";
            }, 1200);
        } else {
            showAlert("회원탈퇴에 실패했습니다.");
        }
    } catch (err) {
        console.log(err);
        showAlert("회원탈퇴 중 오류가 발생했습니다.");
    }
}

function goBack() {
    history.back();
}

function showAlert(message) {
    if (window.showAlertModal) {
        window.showAlertModal(message);
    } else {
        alert(message);
    }
}

function showSuccess(message) {
    if (window.showSuccessModal) {
        window.showSuccessModal(message);
    } else {
        alert(message);
    }
}