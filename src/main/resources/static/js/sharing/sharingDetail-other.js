// 모달 열릴 때 받는사람/관련글 세팅
const messageModal = document.getElementById('messageModal');
messageModal.addEventListener('show.bs.modal', function (event) {
    const btn = event.relatedTarget;
    const toName = btn?.getAttribute('data-to-name') || '';
    const postTitle = btn?.getAttribute('data-post-title') || '';
    document.getElementById('msgTo').value = toName;
    document.getElementById('msgPost').value = postTitle;
    // 이전 입력 유지 원치 않으면 아래 두 줄 주석 해제
    // document.getElementById('msgTitle').value = '';
    // document.getElementById('msgContent').value = '';
    setTimeout(() => document.getElementById('msgTitle').focus(), 100);
});

// 제출 훅(백엔드 연결 지점)
document.getElementById('messageForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    const payload = {
        toName: document.getElementById('msgTo').value,
        postTitle: document.getElementById('msgPost').value,
        title: document.getElementById('msgTitle').value.trim(),
        content: document.getElementById('msgContent').value.trim()
    };
    if (!payload.title || !payload.content) return;

    // TODO: 실제 API로 교체
    // const res = await fetch('/api/messages', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(payload) });
    // if(res.ok){ bootstrap.Modal.getInstance(messageModal).hide(); }

    // 데모: 전송 후 닫기
    bootstrap.Modal.getInstance(messageModal).hide();
});