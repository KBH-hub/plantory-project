let originalNickname = "";
let isNicknameChecked = false;

const phoneInput = document.getElementById("phoneInput");
const changeBtn = document.getElementById("changeProfileImgBtn");
const fileInput = document.getElementById("profileImgInput");
const previewImg = document.getElementById("profilePreview");

let selectedFile = null; // 서버로 보낼 최종 파일

changeBtn.addEventListener("click", () => {
    fileInput.click();
});

fileInput.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (!file) return;

    if (!file.type.startsWith("image/")) {
        showAlert("이미지 파일만 업로드 가능합니다.");
        fileInput.value = "";
        return;
    }

    const imgURL = URL.createObjectURL(file);

    // 미리보기 설정
    previewImg.src = imgURL;
    previewImg.style.display = "block";

    selectedFile = file;
});



document.addEventListener("DOMContentLoaded", async () => {
    const res = await axios.get("/api/profile/me");
    const profile = res.data;

    originalNickname = profile.nickname;

    await window.koreaDataLoaded;

    applyAddress(profile.address);

    await initUpdateProfileInfo();
    initEvents();
});

phoneInput.addEventListener("input", function (e) {
    let value = e.target.value.replace(/[^0-9]/g, "");

    if (value.length > 11) {
        value = value.slice(0, 11);
    }

    if (value.length <= 3) {
        e.target.value = value;
    } else if (value.length <= 7) {
        e.target.value = value.replace(/(\d{3})(\d{1,4})/, "$1-$2");
    } else {
        e.target.value = value.replace(/(\d{3})(\d{4})(\d{1,4})/, "$1-$2-$3");
    }
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
    const nickname = document.getElementById("nicknameInput").value.trim();
    if (!nickname) return showAlert("닉네임을 입력해주세요.");

    if (nickname === originalNickname) {
        isNicknameChecked = true;
        return showAlert("닉네임이 기존과 동일합니다.");
    }

    try {
        const res = await axios.get(`/api/members/checkNickname`, {
            params: { nickname }
        });

        if (res.data.exists) {
            isNicknameChecked = false;
            return showAlert("이미 사용 중인 닉네임입니다.");
        }

        isNicknameChecked = true;
        showAlert("사용 가능한 닉네임입니다.");

    } catch (err) {
        console.error(err);
        showAlert("닉네임 확인 중 오류가 발생했습니다.");
    }
}


async function submitProfile(event) {
    event.preventDefault();

    if (!isNicknameChecked) {
        return showAlert("닉네임 중복 확인을 먼저 해주세요.");
    }

    if (!selectedFile) {
        alert("프로필 사진을 선택해주세요.");
        return;
    }

    const formData = new FormData();
    formData.append("profileImage", selectedFile);

    const nickname = document.getElementById("nicknameInput").value.trim();
    const phone = document.getElementById("phoneInput").value.trim();
    const sido = document.getElementById("sido").value;
    const sigungu = document.getElementById("sigungu").value;
    const address = `${sido} ${sigungu}`;
    const noticeEnabled = document.getElementById("noticeToggle").checked ? 1 : 0;

    const req = {
        nickname,
        phone,
        address,
        noticeEnabled
    };

    try {
        await axios.put("/api/profile", req);
        await axios.post("/api/profile/picture", formData, {
            headers: { "Content-Type": "multipart/form-data" }
        });
        showAlert("프로필이 수정되었습니다.", () => {
            history.back();
        });
    } catch (err) {
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
        await axios.put("/api/profile/withdraw");
    } finally {
        window.location.href = "/logout";
    }
}



function goBack() {
    history.back();
}