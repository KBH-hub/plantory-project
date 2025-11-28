// /js/message/messageDetail.js
(function () {
    const apiBase = '/api/message';

    document.addEventListener('DOMContentLoaded', () => {
        document.getElementById('closeBtn')?.addEventListener('click', (e) => {
            e.preventDefault();
            history.back();
        });
    });

    // 쿼리 파라미터
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

    // XSS 방지
    const esc = (s) => String(s ?? '')
        .replaceAll('&', '&amp;').replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;').replaceAll('"', '&quot;')
        .replaceAll("'", '&#39;');

    // 관련 글 링크
    const buildTargetUrl = (type, id) => {
        if (!type || !id) return '';
        if (type === 'SHARING')  return `/sharing/${id}`;
        if (type === 'QUESTION') return `/question/${id}`;
        return '';
    };

    // 상세 캐시(답장 전송시 사용)
    let detail = null;
    let viewerIdNum = null;

    // 상세 조회
    async function loadDetail() {
        const messageId = Number(qs('messageId'));
        const viewerId  = Number(qs('viewerId'));
        viewerIdNum = Number.isFinite(viewerId) ? viewerId : null;

        if (!Number.isFinite(messageId)) { alert('유효하지 않은 messageId 입니다.'); return; }
        if (!Number.isFinite(viewerId))  { alert('viewerId가 없습니다.'); return; }

        try {
            const res = await fetch(`${apiBase}/${messageId}?viewerId=${encodeURIComponent(viewerId)}`);
            if (!res.ok) {
                if (res.status === 404) alert('쪽지를 찾을 수 없습니다.');
                else alert(`상세 조회 실패: ${res.status}`);
                history.back();
                return;
            }
            const data = await res.json();
            detail = data; // 답장에 사용

            // 제목, 보낸 사람, 내용, 시간
            const titleEl   = document.getElementById('detailTitle');
            const senderEl  = document.getElementById('detailSender');
            const contentEl = document.getElementById('detailContent');
            const timeEl    = document.getElementById('detailCreatedAt');

            if (titleEl)   titleEl.textContent = data.title ?? '(제목 없음)';
            // senderNickname 없으면 senderId로 대체
            if (senderEl)  senderEl.textContent = data.senderNickname ?? String(data.senderId ?? '');
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

            // 답장 모달 프리셋 및 전송 바인딩
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

    // CSRF 헤더 추출(사용 중일 경우)
    function readCsrfHeaders() {
        const token = document.querySelector('meta[name="_csrf"]')?.content;
        const headerName = document.querySelector('meta[name="_csrf_header"]')?.content;
        return token && headerName ? { [headerName]: token } : {};
    }

    // 답장 모달 프리셋 + 전송
    function presetReplyModal({ to, post, title, original }) {
        const modalEl   = document.getElementById('messageModal');
        const form      = document.getElementById('messageForm');
        const toEl      = document.getElementById('msgTo');
        const postEl    = document.getElementById('msgPost');
        const titleEl   = document.getElementById('msgTitle');
        const contentEl = document.getElementById('msgContent');
        const btnSend   = document.getElementById('btnSend');

        if (!modalEl || !form) return;

        // 모달 열릴 때 프리셋
        modalEl.addEventListener('show.bs.modal', () => {
            if (toEl)      toEl.value      = to;
            if (postEl)    postEl.value    = post;
            if (titleEl)   titleEl.value   = title?.startsWith('Re:') ? title : (`Re: ${title}`).trim();
            if (contentEl) contentEl.value = original ? `\n\n----- 원문 -----\n${original}` : '';
        }, { once: true }); // 같은 페이지에서 한 번만 세팅

        // 폼 제출 핸들러(중복 방지 위해 once)
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            if (!detail) { alert('원문 정보를 불러오지 못했습니다.'); return; }
            if (viewerIdNum == null) { alert('viewerId가 없습니다.'); return; }

            const payload = {
                senderId:   viewerIdNum,           // 현재 열람자 = 발신자
                receiverId: detail.senderId,       // 원문 발신자 = 수신자
                title:      titleEl?.value?.trim()   ?? '',
                content:    contentEl?.value?.trim() ?? '',
                targetType: detail.targetType,     // 원글 맥락 유지
                targetId:   detail.targetId
            };

            // 기본 검증
            if (!payload.receiverId)        { alert('수신자 정보가 없습니다.'); return; }
            if (!payload.title)             { alert('제목을 입력하세요.'); return; }
            if (!payload.content)           { alert('내용을 입력하세요.'); return; }
            if (!['SHARING','QUESTION'].includes(payload.targetType)) {
                alert('유효하지 않은 대상 유형입니다.'); return;
            }

            try {
                btnSend?.setAttribute('disabled','true');
                await axios.post('/api/message/messageRegist', payload, {
                    headers: {
                        'Content-Type': 'application/json',
                        ...readCsrfHeaders()
                    }
                });

                const bs = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
                bs.hide();
                alert('쪽지를 보냈습니다.');

            } catch (err) {
                console.error(err);
                const msg = err?.response?.data?.message || err?.response?.statusText || err.message;
                alert(`전송 실패: ${msg}`);
            } finally {
                btnSend?.removeAttribute('disabled');
            }
        }, { once: true });
    }

    document.addEventListener('DOMContentLoaded', loadDetail);
})();
