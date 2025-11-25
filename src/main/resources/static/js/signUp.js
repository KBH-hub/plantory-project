let isIdChecked = false;
let isNickNameChecked = false;

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signUpForm");
    const pw = document.querySelector('input[type="password"]');
    const pwCheck = document.getElementById("pwCheck");


    form.addEventListener("submit", (event) => {

        if (!isIdChecked || !isNickNameChecked) {
            alert("아이디와 닉네임 중복을 확인해주세요.");
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

    const phoneInput = document.getElementById("phoneNumberInput");

    phoneInput.addEventListener("keydown", function (e) {
        const allowed = [
            "Backspace", "Delete", "ArrowLeft", "ArrowRight", "Tab"
        ];

        const isNumberKey = /^[0-9]$/.test(e.key);

        if (!isNumberKey && !allowed.includes(e.key)) {
            e.preventDefault();
        }
    });

    phoneInput.addEventListener("input", function(e) {
        let value = e.target.value.replace(/[^0-9]/g, "");
        if (value.length > 11) value = value.slice(0, 11);

        if (value.length > 7) {
            value = value.replace(/(\d{3})(\d{4})(\d{0,4})/, "$1-$2-$3");
        } else if (value.length > 3) {
            value = value.replace(/(\d{3})(\d{0,4})/, "$1-$2");
        }

        e.target.value = value;
    });
});

document.getElementById("checkIdBtn")
    .addEventListener("click", () => checkDuplicate("아이디", "membernameInput", "membernameMessage", "/members/exists"));

document.getElementById("checkNickNameBtn")
    .addEventListener("click", () => checkDuplicate("닉네임", "nicknameInput", "nicknameMessage", "/members/exists"));

function checkDuplicate(type, inputId, messageId, url) {
    const value = document.getElementById(inputId).value.trim();
    const messageEl = document.getElementById(messageId);

    if (!value) {
        messageEl.textContent = `${type}를 입력해주세요.`;
        messageEl.style.color = "red";
        return;
    }

    axios.get(url, { params: { [type]: value } })
        .then(res => {
            if (res.data === true) {
                messageEl.innerHTML = `이미 존재하는 ${type}입니다.`;
                messageEl.style.color = "red";

                if(type === "아이디") isIdChecked = false;
                if(type === "닉네임") isNickNameChecked = false;
            } else {
                messageEl.innerHTML = `사용 가능한 ${type}입니다.`;
                messageEl.style.color = "green";
            }

            if (type === "아이디") isIdChecked = true;
            if (type === "닉네임") isNickNameChecked = true;
        })
        .catch(err => console.error(err));
}
