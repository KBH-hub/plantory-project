document.getElementById("pwConfirmBtn").addEventListener("click", function () {

    const oldPw = document.getElementById("oldPw").value.trim();
    const newPw = document.getElementById("newPw").value.trim();
    const newPwCheck = document.getElementById("newPwCheck").value.trim();

    const oldPwMsg = document.getElementById("oldPwMsg");
    const newPwMsg = document.getElementById("newPwMsg");
    const newPwCheckMsg = document.getElementById("newPwCheckMsg");

    const currentPassword = "1234";
    let valid = true;

    if (oldPw === "") {
        oldPwMsg.textContent = "*비밀번호를 입력해주세요.";
        oldPwMsg.classList.remove("d-none");
        valid = false;
    } else if (oldPw !== currentPassword) {
        oldPwMsg.textContent = "*일치하지 않습니다.";
        oldPwMsg.classList.remove("d-none");
        valid = false;
    } else {
        oldPwMsg.classList.add("d-none");
    }

    if (newPw === "") {
        newPwMsg.textContent = "*새 비밀번호를 입력해주세요.";
        newPwMsg.classList.remove("d-none");
        valid = false;
    } else if (newPw === currentPassword) {
        newPwMsg.textContent = "*기존 비밀번호와 같습니다.";
        newPwMsg.classList.remove("d-none");
        valid = false;
    } else {
        newPwMsg.classList.add("d-none");
    }

    if (newPwCheck === "") {
        newPwCheckMsg.textContent = "*비밀번호 확인을 입력해주세요.";
        newPwCheckMsg.classList.remove("d-none");
        valid = false;
    } else if (newPwCheck !== newPw) {
        newPwCheckMsg.textContent = "*일치하지 않습니다.";
        newPwCheckMsg.classList.remove("d-none");
        valid = false;
    } else {
        newPwCheckMsg.classList.add("d-none");
    }

    if (valid) {
        alert("비밀번호가 성공적으로 변경되었습니다!");

        const modal = bootstrap.Modal.getInstance(document.getElementById("changePwModal"));
        modal.hide();

        document.getElementById("oldPw").value = "";
        document.getElementById("newPw").value = "";
        document.getElementById("newPwCheck").value = "";

        oldPwMsg.classList.add("d-none");
        newPwMsg.classList.add("d-none");
        newPwCheckMsg.classList.add("d-none");
    }

    document.addEventListener("DOMContentLoaded", function () {

    const agreeChk = document.getElementById("agreeWithdraw");
    const withdrawBtn = document.getElementById("withdrawBtn");
    const withdrawMsg = document.getElementById("withdrawMsg");

    agreeChk.addEventListener("change", () => {
        withdrawBtn.disabled = !agreeChk.checked;
        withdrawMsg.classList.toggle("d-none", agreeChk.checked);
    });

    withdrawBtn.addEventListener("click", () => {
        alert("회원 탈퇴가 완료되었습니다.");

        agreeChk.checked = false;
        withdrawBtn.disabled = true;
        withdrawMsg.classList.add("d-none");

        const modal = bootstrap.Modal.getInstance(document.getElementById("withdrawModal"));
        modal.hide();
    });
});

});