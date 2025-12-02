export function openMessageModal(toMemberId, toNickname, postTitle, targetType, targetId) {

    document.getElementById("msgTo").value = toNickname;
    document.getElementById("msgPost").value = postTitle;

    const modal = document.getElementById("messageModal");
    modal.dataset.receiverId = toMemberId;
    modal.dataset.targetType = targetType;
    modal.dataset.targetId = targetId;

    new bootstrap.Modal(modal).show();
}

export function bindMessageSubmit() {
    const form = document.getElementById("messageForm");
    if (!form) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const modal = document.getElementById("messageModal");

        const senderId = Number(document.body.dataset.memberId);
        const receiverId = Number(modal.dataset.receiverId);
        const targetType = modal.dataset.targetType;
        const targetId = Number(modal.dataset.targetId);

        const title = document.getElementById("msgTitle").value.trim();
        const content = document.getElementById("msgContent").value.trim();

        if (!title || !content) {
            showAlert("제목과 내용을 입력하세요.");
            return;
        }

        try {
            await axios.post("/api/message/messageRegist", {
                senderId,
                receiverId,
                title,
                content,
                targetType,
                targetId
            });

            showAlert("쪽지를 보냈습니다!");

            bootstrap.Modal.getInstance(document.getElementById("messageModal")).hide();
            form.reset();

        } catch (err) {
            console.error(err);
            showAlert("쪽지 전송 실패!");
        }
    });
}
