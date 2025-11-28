(() => {
    if (!document.getElementById("alertModal")) {
        const modalHTML = `
            <div id="alertModal" class="modal" style="display:none; opacity:0;">
                <div class="modal-content">
                    <p id="alertMessage">알림 메시지 내용</p>
                    <div class="modal-buttons">
                        <button id="alertOk">확인</button>
                    </div>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML("beforeend", modalHTML);
    }

    const modal = document.getElementById("alertModal");
    const messageElem = document.getElementById("alertMessage");
    const btnOk = document.getElementById("alertOk");

    window.showAlert = (message, callback) => {
        messageElem.textContent = message;

        if (noOverlay) {
            modal.classList.add("no-overlay");
        } else {
            modal.classList.remove("no-overlay");
        }

        modal.style.display = "flex";
        requestAnimationFrame(() => {
            modal.style.opacity = "1";
        });

        const handleOk = () => {
            closeModal();
            if (callback) callback();
        };

        btnOk.addEventListener("click", handleOk, { once: true });
    };

    function closeModal() {
        modal.style.opacity = "0";
        setTimeout(() => {
            modal.style.display = "none";
        }, 180);
    }
})();
