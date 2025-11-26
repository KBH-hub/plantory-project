let isIdChecked = false;
let isNickNameChecked = false;


    const form = document.getElementById("signUpForm");
    const pw = document.querySelector('input[type="password"]');
    const pwCheck = document.getElementById("pwCheck");

document.getElementById("checkIdBtn")
    .addEventListener("click", () => checkDuplicate("membernameInput", "membernameMessage", "/api/members/exists"));

document.getElementById("checkNickNameBtn")
    .addEventListener("click", () => checkDuplicate("nicknameInput", "nicknameMessage", "/api/members/exists"));

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
    const messageElement = document.getElementById(messageId);

    let paramKey = inputId === "membernameInput" ? "membername" : "nickname";

    if (!value) {
        messageElement.textContent = `${paramKey === "membername" ? "아이디" : "닉네임"}를 입력해주세요.`;
        messageElement.style.color = "red";
        return;
    }

    axios.get(url, { params: { [paramKey]: value } })
        .then(res => {
            if (res.data.code === "DUPLICATE_MEMBERNAME") {
                isIdChecked = false;
                messageElement.innerHTML = res.data.message;
                messageElement.style.color = "red";
                return;
            }

            if (res.data.code === "DUPLICATE_NICKNAME") {
                isNickNameChecked = false;
                messageElement.innerHTML = res.data.message;
                messageElement.style.color = "red";
                return;
            }

            messageElement.innerHTML = res.data.message;
            messageElement.style.color = "green";

            if (inputId === "membernameInput") isIdChecked = true;
            if (inputId === "nicknameInput") isNickNameChecked = true;
        })
        .catch(err => console.error(err));
}
