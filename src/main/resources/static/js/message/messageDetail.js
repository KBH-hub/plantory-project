// /js/message/messageDetail.js
(function () {
    const apiBase = '/api/message';

    // 쿼리 파라미터 가져오기
    const qs = (k) => {
        const v = new URLSearchParams(location.search).get(k);
        return v && v.trim() !== '' ? v : null;
    };

    // KST 포맷
    const fmtKST = (iso) => {
        if (!iso) return '';
        return new Intl.DateTimeFormat('ko-KR', {
            timeZone: 'Asia/Seoul',
            year: 'numeric', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit', second: '2-digit',
            hour12: false
        }).format(new Date(iso))
            .replace(/\./g, '-').replace(/-\s/g, '-').replace(/\s/g, ' ');
    };

    // XSS 방지 이스케이프
    const esc = (s) => String(s ?? '')
        .replaceAll('&', '&amp;').replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;').replaceAll('"', '&quot;')
        .replaceAll("'", '&#39;');

    // 관련 글 링크
    const buildTargetUrl = (type, id) => {
        if (!type || !id) return '';
        return `/readSharing`
        // if (type === 'SHARING') return `/sharing/${id}`;
        // if (type === 'QUESTION') return `/question/${id}`;
        return '';
    };

    // 상세 데이터 채우기
    async function loadDetail() {
        const messageId = Number(qs('messageId'));
        const viewerId  = Number(qs('viewerId'));

        if (!Number.isFinite(messageId)) {
            alert('유효하지 않은 messageId 입니다.');
            return;
        }
        if (!Number.isFinite(viewerId)) {
            alert('viewerId가 없습니다.');
            return;
        }

        try {
            const res = await fetch(`${apiBase}/${messageId}?viewerId=${encodeURIComponent(viewerId)}`);
            if (!res.ok) {
                if (res.status === 404) alert('쪽지를 찾을 수 없습니다.');
                else alert(`상세 조회 실패: ${res.status}`);
                return;
            }
            const data = await res.json();

            // 제목, 보낸 사람, 내용, 시간
            const titleEl   = document.getElementById('detailTitle');
            const senderEl  = document.getElementById('detailSender');
            const contentEl = document.getElementById('detailContent');
            const timeEl    = document.getElementById('detailCreatedAt');

            if (titleEl)   titleEl.textContent = data.title ?? '(제목 없음)';
            // senderNickname을 API가 안 주면 senderId로 대체
            if (senderEl)  senderEl.textContent = data.senderNickname ?? String(data.senderNickname ?? '');
            if (contentEl) contentEl.value      = data.content ?? '';
            if (timeEl)    timeEl.textContent   = fmtKST(data.createdAt);

            // 관련 글
            const relatedEl = document.getElementById('detailPost');
            const relatedUrl = buildTargetUrl(data.targetType, data.targetId);
            if (relatedEl) {
                if (relatedUrl) {
                    relatedEl.innerHTML = `<a href="${relatedUrl}" class="text-decoration-none">${esc(data.targetTitle ?? '관련 글 보기')}</a>`;
                } else {
                    relatedEl.textContent = '-';
                }
            }

            // 모달: 답장 프리셋
            presetReplyModal({
                to: data.senderNickname ?? String(data.senderId ?? ''),
                post: data.targetTitle ?? '',
                title: data.title ?? '',
                original: data.content ?? ''
            });

        } catch (e) {
            console.error(e);
            alert(`상세 조회 실패: ${e.message}`);
        }
    }

    // 답장 모달 데이터 채우기
    function presetReplyModal({ to, post, title, original }) {
        const modalEl = document.getElementById('messageModal');
        if (!modalEl) return;

        modalEl.addEventListener('show.bs.modal', () => {
            const toEl     = document.getElementById('msgTo');
            const postEl   = document.getElementById('msgPost');
            const titleEl  = document.getElementById('msgTitle');
            const contentEl= document.getElementById('msgContent');

            if (toEl)    toEl.value = to;
            if (postEl)  postEl.value = post;
            if (titleEl) titleEl.value = title?.startsWith('Re:') ? title : `Re: ${title}`.trim();

            if (contentEl) {
                contentEl.value = original ? `\n\n\n\n----- 원문 -----\n${original}` : '';
            }
        });

        // 전송은 실제 API 스펙에 맞춰 교체(예시 주석)
        const form = document.getElementById('messageForm');
        form?.addEventListener('submit', async (e) => {
            e.preventDefault();
            try {
                // TODO: 실제 엔드포인트/페이로드로 변경
                // await fetch('/api/message', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({...}) });
                const bs = bootstrap.Modal.getInstance(modalEl);
                bs && bs.hide();
                alert('쪽지를 보냈습니다. (데모)');
            } catch (err) {
                console.error(err);
                alert(`전송 실패: ${err.message}`);
            }
        });
    }

    document.addEventListener('DOMContentLoaded', loadDetail);
})();