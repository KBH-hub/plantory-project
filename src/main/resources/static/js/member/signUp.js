let isIdChecked = false;
let isNickNameChecked = false;

const form = document.getElementById("signUpForm");
const pw = document.querySelector('input[type="password"]');
const pwCheck = document.getElementById("pwCheck");
const phone = document.getElementById("phoneInput");

phone.addEventListener("input", (e) => {
    e.target.value = e.target.value
        .replace(/[^0-9-]/g, "")
        .replace(/-{2,}/g, "-")
        .replace(/^([0-9]{3})([0-9]{4})([0-9]{4})$/, "$1-$2-$3");
});

document.getElementById("checkIdBtn")
    .addEventListener("click", () =>
        checkDuplicate("membernameInput", "membernameMessage", "/api/members/checkMembername")
    );

document.getElementById("checkNickNameBtn")
    .addEventListener("click", () =>
        checkDuplicate("nicknameInput", "nicknameMessage", "/api/members/checkNickname")
    );

document.getElementById("membernameInput").addEventListener("input", () => {
    isIdChecked = false;
});

document.getElementById("nicknameInput").addEventListener("input", () => {
    isNickNameChecked = false;
});

form.addEventListener("submit", (event) => {

    const sido = document.getElementById("sido").value;
    const sigungu = document.getElementById("sigungu").value;
    document.getElementById("addressInput").value = `${sido} ${sigungu}`;

    const phone = document.getElementById("phoneInput");
    const valid = /^010-[0-9]{4}-[0-9]{4}$/.test(phone.value);

    if (!valid) {
        phone.setCustomValidity("전화번호 형식이 올바르지 않습니다.");
    } else {
        phone.setCustomValidity("");
    }
    if (!isIdChecked || !isNickNameChecked) {
        showAlert("아이디와 닉네임 중복을 확인해주세요.");
        event.preventDefault();
        return;
    }

    if (pw.value !== pwCheck.value) {
        pwCheck.setCustomValidity("비밀번호가 일치하지 않습니다");
    } else {
        pwCheck.setCustomValidity("");
    }

    if (!form.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
    }

    form.classList.add("was-validated");
});

function checkDuplicate(inputId, messageId, url) {
    const value = document.getElementById(inputId).value.trim();
    const msgBox = document.getElementById(messageId);

    if (!value) {
        msgBox.textContent = (inputId === "membernameInput" ? "아이디" : "닉네임") + "를 입력해주세요.";
        msgBox.style.color = "red";
        return;
    }

    axios
        .get(url, { params: {
                membername: inputId === "membernameInput" ? value : undefined,
                nickname: inputId === "nicknameInput" ? value : undefined
            }})
        .then(res => {
            const exists = res.data.exists;

            if (exists) {
                msgBox.textContent =
                    (inputId === "membernameInput" ? "이미 사용 중인 아이디입니다." : "이미 사용 중인 닉네임입니다.");
                msgBox.style.color = "red";

                if (inputId === "membernameInput") isIdChecked = false;
                if (inputId === "nicknameInput") isNickNameChecked = false;
            } else {
                msgBox.textContent = "사용 가능합니다.";
                msgBox.style.color = "green";

                if (inputId === "membernameInput") isIdChecked = true;
                if (inputId === "nicknameInput") isNickNameChecked = true;
            }
        })
        .catch(err => console.error(err));
}
