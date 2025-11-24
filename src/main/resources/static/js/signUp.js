document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signUpForm");
    const pw = document.querySelector('input[type="password"]');
    const pwCheck = document.getElementById("pwCheck");

    form.addEventListener("submit", (event) => {
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
});

document.getElementById("checkIdBtn").addEventListener("click", function () {
    const membernameInput = document.getElementById("membernameInput").value.trim();
    const membernameMessage = document.getElementById("membernameMessage");

    if (!membernameInput) {
        membernameMessage.textContent = "아이디를 입력해주세요.";
        membernameMessage.style.color = "red";
        return;
    }

    axios.get(`/members/check-membername`, {
        params: {
            membername: document.getElementById("membernameInput").value
        }
    })
        .then(res => {
            if(res.data === true){
                membernameMessage.innerHTML = "이미 존재하는 아이디입니다.";
                membernameMessage.style.color = "red";
            } else {
                membernameMessage.innerHTML = "사용 가능한 아이디입니다."
                membernameMessage.style.color = "green";
            }
        })
        .catch(err => console.error(err));
})


document.getElementById("checkNickNameBtn").addEventListener("click", function () {
    const nicknameInput = document.getElementById("nicknameInput").value.trim();
    const nicknameMessage = document.getElementById("nicknameMessage");

    if (!nicknameInput) {
        nicknameMessage.textContent = "아이디를 입력해주세요.";
        nicknameMessage.style.color = "red";
        return;
    }

    axios.get(`/members/check-nickname`, {
        params: {
            membername: document.getElementById("nicknameInput").value
        }
    })
        .then(res => {
            if(res.data === true){
                nicknameMessage.innerHTML = "이미 존재하는 닉네임입니다.";
                nicknameMessage.style.color = "red";
            } else {
                nicknameMessage.innerHTML = "사용 가능한 닉네임입니다."
                nicknameMessage.style.color = "green";
            }
        })
        .catch(err => console.error(err));
})