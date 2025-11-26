/** 신고 모달 제어*/
function openMemberSearchModal() {
    const el = document.getElementById("memberSearchModal");
    let modal = bootstrap.Modal.getInstance(el) || new bootstrap.Modal(el);
    modal.show();
}

function closeMemberSearchModal() {
    const el = document.getElementById("memberSearchModal");
    let modal = bootstrap.Modal.getInstance(el);
    if (modal) modal.hide();
}

function selectMember(nickname) {
    document.getElementById("reportTargetInput").value = nickname;
    closeMemberSearchModal();
}

/** 알림 드롭다운 닫기*/
function closeAlarmDropdown() {
    const dropdown = bootstrap.Dropdown.getInstance(document.getElementById('alarmDropdownBtn'));
    if (dropdown) dropdown.hide();
}

/**  DOM 로드 후 이벤트*/
document.addEventListener("DOMContentLoaded", function () {
    const reportImageInput = document.getElementById("reportImageInput");

    if (reportImageInput) {
        reportImageInput.addEventListener("change", function(e) {
            const file = e.target.files[0];
            if (!file) return;

            let reader = new FileReader();
            reader.onload = function(event) {
                document.getElementById("reportImagePreview").src = event.target.result;
                document.getElementById("reportImagePreview").style.display = "block";
                document.getElementById("cameraIcon").style.display = "none";
            };
            reader.readAsDataURL(file);
        });
    }
});

// 드롭다운 자동 닫힘 방지
document.getElementById("alarmDropdown").addEventListener("click", function (e) {
    e.stopPropagation();
});

let deleteMode = false;

// 삭제모드 toggle
document.getElementById("alarmDeleteBtn").addEventListener("click", function () {

    deleteMode = !deleteMode;

    const checkboxes = document.querySelectorAll(".alarm-check");
    const footer = document.getElementById("alarmFooter");

    if (deleteMode) {
        // 체크박스 보이기
        checkboxes.forEach(cb => cb.classList.remove("d-none"));

        // 푸터 → 삭제 버튼으로 변경
        footer.innerHTML = `
            <button class="btn btn-danger btn-sm w-100" id="alarmDeleteConfirm">삭제</button>
        `;
    } else {
        // 체크박스 숨기기
        checkboxes.forEach(cb => {
            cb.checked = false;
            cb.classList.add("d-none");
        });

        // 푸터 복원
        footer.innerHTML = `
            <button class="btn btn-light btn-sm">닫기</button>
            <button class="btn btn-secondary btn-sm">모두 읽음</button>
        `;
    }
});
