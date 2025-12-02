
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
            console.log("[searchMember] sample:", data?.[0]); // 여기에 id 키가 뭔지 찍힙니다.
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

        // 로그인 사용자(신고자) 아이디
        const reporterIdRaw = document.body?.getAttribute("data-member-id");
        const reporterId = reporterIdRaw ? Number(reporterIdRaw) : null;

        const fd = new FormData();
        fd.append("targetMemberId", targetMemberId);
        if (reporterId != null) fd.append("reporterId", String(reporterId));
        fd.append("content", content);
        for (let i = 0; i < files.length; i++) {
            fd.append("files", files[i]); // 컨트롤러와 동일 키
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

    document.addEventListener("DOMContentLoaded", function () {
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

        // var deleteMode = false;
        // var alarmDeleteBtn = document.getElementById("alarmDeleteBtn");
        // if (alarmDeleteBtn) {
        //     alarmDeleteBtn.addEventListener("click", function () {
        //         deleteMode = !deleteMode;
        //
        //         var checkboxes = document.querySelectorAll(".alarm-check");
        //         var footer = document.getElementById("alarmFooter") || document.querySelector(".ph-alarm-footer");
        //
        //         if (deleteMode) {
        //             checkboxes.forEach(function (cb) { cb.classList.remove("d-none"); });
        //             if (footer) {
        //                 footer.innerHTML = '<button class="btn btn-danger btn-sm w-100" id="alarmDeleteConfirm">삭제</button>';
        //             }
        //         } else {
        //             checkboxes.forEach(function (cb) {
        //                 cb.checked = false;
        //                 cb.classList.add("d-none");
        //             });
        //             if (footer) {
        //                 footer.innerHTML =
        //                     '<button class="btn btn-light btn-sm">닫기</button>' +
        //                     '<button class="btn btn-secondary btn-sm">모두 읽음</button>';
        //             }
        //         }
        //     });
        // }

        var memberSearchInput = document.getElementById("memberSearchInput");
        if (memberSearchInput) {
            memberSearchInput.addEventListener("keypress", function (e) {
                if (e.key === "Enter") {
                    e.preventDefault();
                    searchMember();
                }
            });
        }
    });
})();
