(() => {
    if (!document.getElementById("alertModal")) {
        const modalHTML = `
            <div id="alertModal" class="custom-alert-modal" style="display:none; opacity:0;">
                <div class="custom-alert-modal-content">
                    <p id="alertMessage">알림 메시지 내용</p>
                    <div class="custom-alert-modal-buttons">
                        <button id="alertOk">확인</button>
                    </div>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML("beforeend", modalHTML);
    }

    const customAlertModal = document.getElementById("alertModal");
    const messageElem = document.getElementById("alertMessage");
    const btnOk = document.getElementById("alertOk");

    window.showAlert = (message, callback = null, noOverlay = false) => {
        messageElem.textContent = message;

        customAlertModal.style.display = "flex";
        requestAnimationFrame(() => {
            customAlertModal.style.opacity = "1";
        });

        const handleOk = () => {
            closeModal();
            if (callback) callback();
        };

        btnOk.addEventListener("click", handleOk, { once: true });
    };

    function closeModal() {
        customAlertModal.style.opacity = "0";
        setTimeout(() => {
            customAlertModal.style.display = "none";
        }, 180);
    }
})();
