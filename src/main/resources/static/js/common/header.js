(function () {
    function openMemberSearchModal() {
        var el = document.getElementById("memberSearchModal");
        if (!el) return;
        var modal = bootstrap.Modal.getInstance(el) || new bootstrap.Modal(el);
        el.addEventListener("shown.bs.modal", function onShown() {
            el.removeEventListener("shown.bs.modal", onShown);
            document.getElementById("memberSearchInput")?.focus();
        });
        modal.show();
    }

    function closeMemberSearchModal() {
        var el = document.getElementById("memberSearchModal");
        if (!el) return;
        var modal = bootstrap.Modal.getInstance(el);
        if (modal) modal.hide();
    }

    function escapeHtml(s) {
        s = String(s == null ? "" : s);
        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll('"', "&quot;");
        s = s.replaceAll("'", "&#39;");
        return s;
    }

    function escapeAttr(s) {
        s = String(s == null ? "" : s);
        s = escapeHtml(s);
        s = s.replace(/\r?\n/g, " ");
        s = s.replace(/\\/g, "\\\\");
        return s;
    }

    async function searchMember() {
        var input = document.getElementById("memberSearchInput");
        var nickname = input ? input.value.trim() : "";

        if (nickname.length === 0) {
            renderRows([]);
            return;
        }

        var viewerIdRaw = document.body?.getAttribute("data-member-id");
        var viewerIdNum = viewerIdRaw != null ? Number(viewerIdRaw) : null;

        try {
            var response = await axios.get("/api/report/users", {
                params: { nickname: nickname, viewerId: viewerIdNum },
            });
            var data = Array.isArray(response?.data) ? response.data : [response?.data ?? []];
            console.log("[searchMember] sample:", data?.[0]);
            renderRows(data);
        } catch (e) {
            console.error("searchMember error:", e);
            renderRows([]);
        }
    }

    function renderRows(list) {
        var tbody = document.getElementById("searchResultBody");
        if (!tbody) return;

        if (!Array.isArray(list) || list.length === 0) {
            tbody.innerHTML =
                '<tr><td colspan="2" class="text-center text-muted">회원이 없습니다</td></tr>';
            return;
        }

        var rows = "";
        for (var i = 0; i < list.length; i++) {
            var item = list[i] || {};
            var nickSafeText = escapeHtml(item.nickname);
            var nickAttr = escapeAttr(item.nickname);
            var rawId = item.memberId ?? item.id ?? item.userId ?? item.memberNo ?? item.userNo;
            var idAttr = escapeAttr(rawId);

            rows +=
                "<tr>" +
                "<td>" + nickSafeText + "</td>" +
                "<td>" +
                '<button type="button" class="btn btn-success btn-sm js-select-member" ' +
                'data-nickname="' + nickAttr + '" ' +
                'data-member-id="' + idAttr + '">선택</button>' +
                "</td>" +
                "</tr>";
        }
        tbody.innerHTML = rows;
    }

    function selectMember(nickname, memberId) {
        var targetInput = document.getElementById("reportTargetInput");
        if (targetInput) {
            targetInput.value = nickname != null ? String(nickname) : "";
        }

        var targetId = document.getElementById("reportTargetId");
        if (targetId) {
            targetId.value = Number.isFinite(memberId) ? String(memberId) : "";
        }

        closeMemberSearchModal();
    }

    async function submitReport() {
        const targetIdEl = document.getElementById("reportTargetId");
        const targetNickEl = document.getElementById("reportTargetInput");
        const contentEl = document.getElementById("reportContent");
        const fileEl = document.getElementById("reportImageInput");

        const targetMemberId = targetIdEl?.value;
        const content = contentEl?.value?.trim() || "";
        const files = fileEl?.files || [];
        if (!targetMemberId) {
            showAlert("피신고자 선택은 필수입니다.");
            return;
        }
        if (!content) {
            showAlert("신고 내용을 입력하세요.");
            return;
        }
        if (!files.length) {
            showAlert("근거 사진을 1장 이상 첨부하세요.");
            return;
        }

        const reporterIdRaw = document.body?.getAttribute("data-member-id");
        const reporterId = reporterIdRaw ? Number(reporterIdRaw) : null;

        const fd = new FormData();
        fd.append("targetMemberId", targetMemberId);
        if (reporterId != null) fd.append("reporterId", String(reporterId));
        fd.append("content", content);
        for (let i = 0; i < files.length; i++) {
            fd.append("files", files[i]);
        }

        try {
            const res = await axios.post("/api/report", fd);
            showAlert(res?.data?.message ?? "신고가 등록되었습니다.");

            const reportModalEl = document.getElementById("reportModal");
            bootstrap.Modal.getInstance(reportModalEl)?.hide();
            if (targetNickEl) targetNickEl.value = "";
            if (targetIdEl) targetIdEl.value = "";
            if (contentEl) contentEl.value = "";
            if (fileEl) fileEl.value = "";

            const preview = document.getElementById("reportImagePreview");
            const camera = document.getElementById("cameraIcon");
            if (preview) {
                preview.src = "";
                preview.style.display = "none";
            }
            if (camera) {
                camera.style.display = "block";
            }
        } catch (err) {
            console.error(err);
            const msg = err?.response?.data?.message || "신고 등록에 실패했습니다.";
            showAlert(msg);
        }
    }

    function closeAlarmDropdown() {
        var btn = document.getElementById("alarmDropdownBtn");
        var dropdown = btn ? bootstrap.Dropdown.getInstance(btn) : null;
        if (dropdown) dropdown.hide();
    }

    /* =========================================================
       Notice 드롭다운 바인딩 (NoticeDTO 대응)
       ========================================================= */

    const noticeAPI = {
        list: (receiverId) => axios.get("/api/notice", { params: { receiverId } }),
        markRead: (noticeId) => axios.put("/api/notice", null, { params: { noticeId } }),
        clearAll: (receiverId) => axios.delete("/api/notice", { params: { receiverId } }),
    };

    function noticeFormatDate(isoOrEpoch) {
        try {
            const d = typeof isoOrEpoch === "number" ? new Date(isoOrEpoch) : new Date(String(isoOrEpoch));
            if (isNaN(d.getTime())) return "";
            const yy = d.getFullYear();
            const mm = String(d.getMonth() + 1).padStart(2, "0");
            const dd = String(d.getDate()).padStart(2, "0");
            const hh = String(d.getHours()).padStart(2, "0");
            const mi = String(d.getMinutes()).padStart(2, "0");
            return `${yy}-${mm}-${dd} ${hh}:${mi}`;
        } catch { return ""; }
    }

    // NoticeTargetType + targetId -> 링크 생성
    function noticeBuildLink(n) {
        const id = n?.targetId;
        switch (String(n?.targetType || "")) {
            case "SHARING":   return `/readSharing/${id}`;
            case "SHARING_REVIEW":   return `/sharing/${id}/review`;
            case "QUESTION":  return `/readQuestion/${id}`;
            case "MESSAGE":    return `/messageDetail?messageId=${id}`;
            case "WATERING":     return `/plantCalendar`;
            default:          return "#";
        }
    }

    function noticeUpdateBadge(count) {
        const badge = document.getElementById("alarmBadge"); // 있으면 갱신
        if (!badge) return;
        if (count > 0) {
            badge.textContent = count;
            badge.classList.remove("d-none");
        } else {
            badge.classList.add("d-none");
        }
    }

    let _noticeBadgeLoading = false;
    let _noticeBadgeLast = null;

    async function noticeInitBadge() {
        if (_noticeBadgeLoading) return;

        const viewerIdRaw = document.body?.getAttribute("data-member-id");
        const receiverId = viewerIdRaw ? Number(viewerIdRaw) : null;
        if (!Number.isFinite(receiverId)) return;

        _noticeBadgeLoading = true;
        try {
            const res = await noticeAPI.list(receiverId);
            const items = Array.isArray(res?.data) ? res.data : [];
            // 유효(delFlag == null) + 미읽음(readFlag == null)만 카운트
            const unread = items.filter(n => n?.delFlag == null && n?.readFlag == null).length;

            // 동일 값이면 DOM 업데이트 생략
            if (_noticeBadgeLast !== unread) {
                noticeUpdateBadge(unread);
                _noticeBadgeLast = unread;
            }
        } catch (err) {
            console.error("[notice] init badge load error", err);
            // 실패 시 강제 숨김은 하지 않음
        } finally {
            _noticeBadgeLoading = false;
        }
    }

    function noticeRenderEmpty() {
        const listEl = document.getElementById("alarmList");
        if (!listEl) return;
        listEl.innerHTML = '<div class="p-4 text-center text-muted">읽지 않은 알림이 없습니다</div>';
        noticeUpdateBadge(0);
    }

    function noticeRenderList(notices) {
        const listEl = document.getElementById("alarmList");
        if (!listEl) return;

        // delFlag가 null인 것만 표시
        const rows = (Array.isArray(notices) ? notices : []).filter(n => n?.delFlag == null);

        if (rows.length === 0) {
            noticeRenderEmpty();
            return;
        }

        const unread = rows.filter(n => n?.readFlag == null).length;
        noticeUpdateBadge(unread);

        let html = "";
        for (const n of rows) {
            const id = n.noticeId;
            const dt = noticeFormatDate(n.createdAt);
            const msg = escapeHtml(n.content ?? "");
            const link = noticeBuildLink(n);
            const isRead = n.readFlag != null;
            const readClass = isRead ? "opacity-75" : "fw-semibold";
            html += `
                <div class="p-3 border-bottom d-flex align-items-start ph-alarm-item" data-notice-id="${id}">
                  <div class="w-100">
                    <small class="text-secondary d-block">${dt}</small>
                    <a href="${link}" class="text-dark d-block text-truncate ${readClass} js-notice-link"
                       data-notice-id="${id}">${msg}</a>
                  </div>
                </div>
            `;
        }
        listEl.innerHTML = html;
    }

    async function noticeLoad() {
        const viewerIdRaw = document.body?.getAttribute("data-member-id");
        const receiverId = viewerIdRaw ? Number(viewerIdRaw) : null;
        if (!Number.isFinite(receiverId)) {
            noticeRenderEmpty();
            return;
        }
        try {
            const res = await noticeAPI.list(receiverId);
            noticeRenderList(res?.data);
        } catch (e) {
            console.error("[notice] load error", e);
            noticeRenderEmpty();
        }
    }

    async function noticeOnItemClick(e) {
        const a = e.target.closest(".js-notice-link");
        if (!a) return;
        const noticeId = Number(a.getAttribute("data-notice-id"));
        if (!Number.isFinite(noticeId)) return;

        try {
            await noticeAPI.markRead(noticeId);
            a.classList.add("opacity-75");
            a.classList.remove("fw-semibold");
            // 배지 낙관 갱신
            const badge = document.getElementById("alarmBadge");
            const cur = Number(badge?.textContent ?? 0);
            if (badge && cur > 0) noticeUpdateBadge(cur - 1);
        } catch (err) {
            console.error("[notice] markRead error", err);
        }
        // 링크 이동은 기본 동작
    }

    async function noticeOnClearAll() {
        const viewerIdRaw = document.body?.getAttribute("data-member-id");
        const receiverId = viewerIdRaw ? Number(viewerIdRaw) : null;
        if (!Number.isFinite(receiverId)) return;

        try {
            await noticeAPI.clearAll(receiverId);
            noticeRenderEmpty();
        } catch (err) {
            console.error("[notice] clearAll error", err);
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        // 전역 노출
        window.openMemberSearchModal = openMemberSearchModal;
        window.closeMemberSearchModal = closeMemberSearchModal;
        window.searchMember = searchMember;
        window.submitReport = submitReport;

        var tbody = document.getElementById("searchResultBody");
        if (tbody) {
            tbody.addEventListener("click", function (e) {
                var btn = e.target.closest(".js-select-member");
                if (!btn) return;
                var nickname = btn.getAttribute("data-nickname");
                var memberIdStr = btn.getAttribute("data-member-id");
                var memberId = Number(memberIdStr);
                if (!Number.isFinite(memberId)) {
                    console.warn("Invalid data-member-id:", memberIdStr);
                    showAlert("잘못된 회원 ID 입니다.");
                    return;
                }
                selectMember(nickname, memberId);
            });
        }

        var reportImageInput = document.getElementById("reportImageInput");
        if (reportImageInput) {
            reportImageInput.addEventListener("change", function (e) {
                var file = e?.target?.files?.[0] ?? null;
                if (!file) return;

                var reader = new FileReader();
                reader.onload = function (event) {
                    var result = event?.target?.result ?? null;
                    var preview = document.getElementById("reportImagePreview");
                    var camera = document.getElementById("cameraIcon");
                    if (preview && result != null) {
                        preview.src = result;
                        preview.style.display = "block";
                    }
                    if (camera) camera.style.display = "none";
                };
                reader.readAsDataURL(file);
            });
        }

        var alarmDropdown = document.getElementById("alarmDropdown");
        if (alarmDropdown) {
            alarmDropdown.addEventListener("click", function (e) {
                e.stopPropagation();
            });
        }

        var memberSearchInput = document.getElementById("memberSearchInput");
        if (memberSearchInput) {
            memberSearchInput.addEventListener("keypress", function (e) {
                if (e.key === "Enter") {
                    e.preventDefault();
                    searchMember();
                }
            });
        }

        noticeInitBadge();

        // 드롭다운이 펼쳐질 때 서버에서 알림 로드
        document.getElementById("alarmDropdownBtn")?.addEventListener("shown.bs.dropdown", noticeLoad);

        // 항목 클릭 위임(읽음 처리)
        document.getElementById("alarmList")?.addEventListener("click", noticeOnItemClick);

        // "비우기" 버튼 핸들링
        document.getElementById("removeAllAlarm")?.addEventListener("click", noticeOnClearAll);

        // 알림 드롭다운 닫히고 다시 계산
        document.getElementById("alarmDropdownBtn")
            ?.addEventListener("hidden.bs.dropdown", noticeInitBadge);
    });
})();
