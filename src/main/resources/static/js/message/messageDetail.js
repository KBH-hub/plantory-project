// 모달 오픈 시: 내용 템플릿에 이전 내용 포함하여 편집 가능하게 채움
const messageModal = document.getElementById('messageModal');
messageModal.addEventListener('show.bs.modal', (ev) => {
    const btn  = ev.relatedTarget;
    const to   = btn?.getAttribute('data-to')    || document.getElementById('detailSender').textContent.trim();
    const post = btn?.getAttribute('data-post')  || document.getElementById('detailPost').textContent.trim();
    const tit  = btn?.getAttribute('data-title') || ('Re: ' + document.getElementById('detailTitle').textContent.trim());
    const prev = (document.getElementById('detailContent').value || '').trim();

    document.getElementById('msgTo').value    = to;
    document.getElementById('msgPost').value  = post;
    document.getElementById('msgTitle').value = tit;

    const template = `\n\n\n----------------\n${prev}`;
    const contentEl = document.getElementById('msgContent');
    contentEl.value = template;

    // 커서를 최상단(사용자 입력 부분)으로 이동
    requestAnimationFrame(() => {
        contentEl.focus();
        contentEl.setSelectionRange(0, 0);
    });
});

// 전송 데모
document.getElementById('messageForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const payload = {
        to: document.getElementById('msgTo').value,
        post: document.getElementById('msgPost').value,
        title: document.getElementById('msgTitle').value,
        content: document.getElementById('msgContent').value
    };
    console.log('SEND MESSAGE', payload);
    bootstrap.Modal.getInstance(messageModal).hide();
    alert('쪽지를 보냈습니다. (데모)');
});