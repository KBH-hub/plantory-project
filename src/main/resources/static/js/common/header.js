function openMemberSearchModal() {
    var el = document.getElementById("memberSearchModal");
    var modal = bootstrap.Modal.getInstance(el);
    if (!modal) {
        modal = new bootstrap.Modal(el);
    }
    modal.show();
}

function closeMemberSearchModal() {
    var el = document.getElementById("memberSearchModal");
    var modal = bootstrap.Modal.getInstance(el);
    if (modal) {
        modal.hide();
    }
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
    var nickname = input ? input.value : "";
    nickname = nickname.trim();

    if (nickname.length === 0) {
        renderRows([]);
        return;
    }

    var viewerIdRaw = document.body ? document.body.getAttribute("data-member-id") : null;
    var viewerIdNum = viewerIdRaw != null ? Number(viewerIdRaw) : null;

    try {
        var response = await axios.get("/api/report/users", {
            params: {nickname: nickname, viewerId: viewerIdNum}
        });

        var data = response && response.data ? response.data : [];
        if (Array.isArray(data)) {
            renderRows(data);
        } else {
            renderRows([data]);
        }
    } catch (e) {
        console.error("searchMember error:", e);
        renderRows([]);
    }
}

function selectMember(nickname, memberId) {
    var targetInput = document.getElementById("reportTargetInput");
    if (targetInput) {
        targetInput.value = nickname != null ? String(nickname) : "";
    }

    var targetId = document.getElementById("reportTargetId");
    if (targetId) {
        if (memberId == null) {
            targetId.setAttribute("value", "");
        } else {
            targetId.setAttribute("value", String(memberId));
        }
    }

    closeMemberSearchModal();
}

function renderRows(list) {
    var tbody = document.getElementById("searchResultBody");
    if (!tbody) {
        return;
    }

    if (!Array.isArray(list) || list.length === 0) {
        tbody.innerHTML = "<tr><td colspan=\"2\" class=\"text-center text-muted\">회원이 없습니다</td></tr>";
        return;
    }

    var rows = "";
    for (var i = 0; i < list.length; i++) {
        var item = list[i] || {};
        var nickSafeText = escapeHtml(item.nickname);
        var nickAttr = escapeAttr(item.nickname);
        var idAttr = escapeAttr(item.memberId);

        rows += ""
            + "<tr>"
            + "<td>" + nickSafeText + "</td>"
            + "<td>"
            + "<button type=\"button\""
            + " class=\"btn btn-success btn-sm js-select-member\""
            + " data-nickname=\"" + nickAttr + "\""
            + " data-member-id=\"" + idAttr + "\">"
            + "선택"
            + "</button>"
            + "</td>"
            + "</tr>";
    }
    tbody.innerHTML = rows;
}

async function submitReport() {
    const targetIdEl = document.getElementById("reportTargetId");
    const targetNickEl = document.getElementById("reportTargetInput");
    const contentEl = document.getElementById("reportContent");
    const fileEl = document.getElementById("reportImageInput");

    const targetMemberId = targetIdEl?.value || "";
    const content = contentEl?.value?.trim() || "";
    const files = fileEl?.files || [];

    if (!targetMemberId) {
        alert("피신고자 선택은 필수입니다.");
        return;
    }
    if (!content) {
        alert("신고 내용을 입력하세요.");
        return;
    }
    if (!files.length) {
        alert("근거 사진을 1장 이상 첨부하세요.");
        return;
    }

    // 로그인 사용자(신고자) 아이디
    const reporterIdRaw = document.body?.getAttribute("data-member-id");
    const reporterId = reporterIdRaw ? Number(reporterIdRaw) : null;

    const fd = new FormData();
    // 패턴 A(@ModelAttribute)라면 필드로 그대로 넣습니다.
    fd.append("targetMemberId", targetMemberId);
    if (reporterId != null) fd.append("reporterId", String(reporterId));
    fd.append("content", content);
    for (let i = 0; i < files.length; i++) {
        fd.append("files", files[i]); // 키 이름은 컨트롤러와 동일하게
    }

    try {
        const res = await axios.post("/api/report", fd, {
            // Content-Type은 생략해야 boundary가 자동 설정됩니다.
            // headers: { "Content-Type": "multipart/form-data" }
        });
        alert(res?.data?.message ?? "신고가 등록되었습니다.");
        // 모달 닫고 폼 초기화
        const reportModalEl = document.getElementById("reportModal");
        bootstrap.Modal.getInstance(reportModalEl)?.hide();
        targetNickEl.value = "";
        targetIdEl.value = "";
        contentEl.value = "";
        fileEl.value = "";
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
        alert(msg);
    }

    (function attachSelectHandler() {
        var tbody = document.getElementById("searchResultBody");
        if (!tbody) {
            return;
        }
        tbody.addEventListener("click", function (e) {
            var target = e.target;
            if (!target) {
                return;
            }
            var btn = target.closest ? target.closest(".js-select-member") : null;
            if (!btn) {
                return;
            }
            var nickname = btn.getAttribute("data-nickname");
            var memberId = btn.getAttribute("data-member-id");
            selectMember(nickname, memberId);
        });
    })();

    function closeAlarmDropdown() {
        var btn = document.getElementById("alarmDropdownBtn");
        var dropdown = btn ? bootstrap.Dropdown.getInstance(btn) : null;
        if (dropdown) {
            dropdown.hide();
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        var reportImageInput = document.getElementById("reportImageInput");
        if (reportImageInput) {
            reportImageInput.addEventListener("change", function (e) {
                var files = e && e.target ? e.target.files : null;
                var file = files && files.length > 0 ? files[0] : null;
                if (!file) {
                    return;
                }

                var reader = new FileReader();
                reader.onload = function (event) {
                    var result = event && event.target ? event.target.result : null;
                    var preview = document.getElementById("reportImagePreview");
                    var camera = document.getElementById("cameraIcon");
                    if (preview && result != null) {
                        preview.src = result;
                        preview.style.display = "block";
                    }
                    if (camera) {
                        camera.style.display = "none";
                    }
                };
                reader.readAsDataURL(file);
            });
        }
    });

    (function attachDropdownStopper() {
        var alarmDropdown = document.getElementById("alarmDropdown");
        if (alarmDropdown) {
            alarmDropdown.addEventListener("click", function (e) {
                e.stopPropagation();
            });
        }
    })();

    var deleteMode = false;

    (function attachDeleteToggle() {
        var btn = document.getElementById("alarmDeleteBtn");
        if (!btn) {
            return;
        }
        btn.addEventListener("click", function () {
            deleteMode = !deleteMode;

            var checkboxes = document.querySelectorAll(".alarm-check");
            var footer = document.getElementById("alarmFooter");

            if (deleteMode) {
                for (var i = 0; i < checkboxes.length; i++) {
                    checkboxes[i].classList.remove("d-none");
                }
                if (footer) {
                    footer.innerHTML = "<button class=\"btn btn-danger btn-sm w-100\" id=\"alarmDeleteConfirm\">삭제</button>";
                }
            } else {
                for (var j = 0; j < checkboxes.length; j++) {
                    checkboxes[j].checked = false;
                    checkboxes[j].classList.add("d-none");
                }
                if (footer) {
                    footer.innerHTML = ""
                        + "<button class=\"btn btn-light btn-sm\">닫기</button>"
                        + "<button class=\"btn btn-secondary btn-sm\">모두 읽음</button>";
                }
            }
        });
    })();
}
