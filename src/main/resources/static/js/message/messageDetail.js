(function () {
    'use strict';

    const apiBase = '/api/message';

    document.addEventListener('DOMContentLoaded', () => {
        document.getElementById('closeBtn')?.addEventListener('click', (e) => {
            e.preventDefault();
            history.back();
        });
    });

    const qs = (k) => {
        const v = new URLSearchParams(location.search).get(k);
        return v && v.trim() !== '' ? v : null;
    };

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
        if (type === 'SHARING') return `/readSharing/${id}`;
        if (type === 'QUESTION') return `/readQuestion/${id}`;
        return '';
    };

    // 상세 캐시(답장 전송시 사용)
    let detail = null;
    let viewerIdNum = null;

    // 상세 조회
    async function loadDetail() {
        const messageId = Number(qs('messageId'));
        const viewerId = Number(qs('viewerId'));
        viewerIdNum = Number(document.body.dataset.memberId);

        if (!Number.isFinite(messageId)) {
            showAlert('유효하지 않은 messageId 입니다.');
            return;
        }
        if (!Number.isFinite(viewerId)) {
            showAlert('viewerId가 없습니다.');
            return;
        }

        try {
            const res = await axios.get(`${apiBase}/${messageId}`, {params: {viewerId}});
            const data = res.data;
            detail = data;

            // 제목, 보낸 사람, 내용, 시간
            const titleEl = document.getElementById('detailTitle');
            const senderEl = document.getElementById('detailSender');
            const contentEl = document.getElementById('detailContent');
            const timeEl = document.getElementById('detailCreatedAt');

            if (titleEl) titleEl.textContent = data.title ?? '(제목 없음)';
            if (senderEl) senderEl.textContent = data.senderNickname ?? String(data.senderId ?? '');
            if (contentEl) contentEl.value = data.content ?? '';
            if (timeEl) timeEl.textContent = fmtKST(data.createdAt);

            const relatedEl = document.getElementById('detailPost');
            const relatedUrl = buildTargetUrl(data.targetType, data.targetId);
            if (relatedEl) {
                if (relatedUrl) {
                    relatedEl.innerHTML = `<a href="${relatedUrl}" class="text-decoration-none">${esc(data.targetTitle ?? '관련 글 보기')}</a>`;
                } else {
                    relatedEl.textContent = '-';
                }
            }

            presetReplyModal({
                to: data.senderNickname ?? String(data.senderId ?? ''),
                post: data.targetTitle ?? '',
                title: data.title ?? '',
                original: data.content ?? ''
            });

        } catch (e) {
            console.error(e);
            const status = e?.response?.status;
            if (status === 404) showAlert('쪽지를 찾을 수 없습니다.');
            else showAlert(`상세 조회 실패: ${e?.response?.data?.message || e.message}`);
            history.back();
        }
    }

    function readCsrfHeaders() {
        const token = document.querySelector('meta[name="_csrf"]')?.content;
        const headerName = document.querySelector('meta[name="_csrf_header"]')?.content;
        return token && headerName ? {[headerName]: token} : {};
    }

    function toReplyTitle(title, {stack = false} = {}) {
        const t = String(title ?? '').trim();
        if (stack) return `Re: ${t}`;
        return t.startsWith('Re:') ? t : `Re: ${t}`;
    }

    // 답장 모달 프리셋 + 전송
    function presetReplyModal({to, post, title, original}) {
        const modalEl = document.getElementById('messageModal');
        const form = document.getElementById('messageForm');
        const toEl = document.getElementById('msgTo');
        const postEl = document.getElementById('msgPost');
        const titleEl = document.getElementById('msgTitle');
        const contentEl = document.getElementById('msgContent');
        const btnSend = document.getElementById('btnSend');

        if (!modalEl || !form) return;

        // 모달 열릴 때 프리셋
        modalEl.addEventListener('show.bs.modal', () => {
            if (toEl) toEl.value = to;
            if (postEl) postEl.value = post;

            if (titleEl) titleEl.value = toReplyTitle(title);

            if (contentEl) contentEl.value = original ? `\n\n----- 원문 -----\n${original}` : '';
        }, {once: true});

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            if (!detail) {
                showAlert('원문 정보를 불러오지 못했습니다.');
                return;
            }
            if (viewerIdNum == null) {
                showAlert('viewerId가 없습니다.');
                return;
            }

            const payload = {
                senderId: viewerIdNum,
                receiverId: detail.senderId,
                title: titleEl?.value?.trim() ?? '',
                content: contentEl?.value?.trim() ?? '',
                targetType: detail.targetType,
                targetId: detail.targetId
            };

            if (!payload.receiverId) {
                showAlert('수신자 정보가 없습니다.');
                return;
            }
            if (!payload.title) {
                showAlert('제목을 입력하세요.');
                return;
            }
            if (!payload.content) {
                showAlert('내용을 입력하세요.');
                return;
            }
            if (!['SHARING', 'QUESTION'].includes(payload.targetType)) {
                showAlert('유효하지 않은 대상 유형입니다.');
                return;
            }

            try {
                btnSend?.setAttribute('disabled', 'true');

                await axios.post(`${apiBase}/messageRegist`, payload, {
                    headers: {
                        'Content-Type': 'application/json',
                        ...readCsrfHeaders()
                    }
                });

                const bs = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
                bs.hide();
                showAlert('쪽지를 보냈습니다.');

            } catch (err) {
                console.error(err);
                const msg = err?.response?.data?.message || err?.response?.statusText || err.message;
                showAlert(`전송 실패: ${msg}`);
            } finally {
                btnSend?.removeAttribute('disabled');
            }
        }, {once: true});
    }

    document.addEventListener('DOMContentLoaded', loadDetail);
})();