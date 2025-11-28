(() => {
    if (!document.getElementById("confirmModal")) {
        const modalHTML = `
            <div id="confirmModal" class="custom-modal" style="display:none; opacity:0;">
                <div class="custom-modal-content">
                    <p id="confirmMessage">정말 진행하시겠습니까?</p>
                    <div class="custom-modal-buttons">
                        <button id="confirmYes">예</button>
                        <button id="confirmNo">아니오</button>
                    </div>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML("beforeend", modalHTML);
    }

    const modal = document.getElementById("confirmModal");
    const messageElem = document.getElementById("confirmMessage");
    const btnYes = document.getElementById("confirmYes");
    const btnNo = document.getElementById("confirmNo");

    window.showModal = (message, callback) => {
        messageElem.textContent = message;

        modal.style.display = "flex";
        requestAnimationFrame(() => {
            modal.style.opacity = "1";
        });

        const handleYes = () => {
            closeModal();
            callback(true);
        };

        const handleNo = () => {
            closeModal();
            callback(false);
        };

        btnYes.addEventListener("click", handleYes, { once: true });
        btnNo.addEventListener("click", handleNo, { once: true });
    };

    function closeModal() {
        modal.style.opacity = "0";
        setTimeout(() => {
            modal.style.display = "none";
        }, 180);
    }
})();
document.querySelectorAll(".custom-alert-modal, .custom-alert-modal *")
    .forEach(el => el.setAttribute("draggable", "false"));
