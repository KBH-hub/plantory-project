document.addEventListener('DOMContentLoaded', function () {
    /* 모달 선택 → 입력값 채우기 */
    document.querySelectorAll('.plant-select-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            const name = this.getAttribute('data-plant-name') || '';
            const condition = this.getAttribute('data-plant-condition') || '';
            const difficulty = this.getAttribute('data-plant-difficulty') || '';
            const nameInput = document.getElementById('plantNameInput');
            const condInput = document.getElementById('plantConditionInput');
            const diffInput = document.getElementById('plantDifficultyInput');
            if (nameInput) nameInput.value = name;
            if (condInput) condInput.value = condition;
            if (diffInput) diffInput.value = difficulty;
        });
    });

    /* 이미지 업로드: 업로드 타일이 항상 마지막 칸으로 이동 */
    const input    = document.getElementById('plantImages');
    const thumbs   = document.getElementById('thumbs');
    const preview  = document.getElementById('previewList');
    const addTile  = document.getElementById('addTile');
    const countEl  = document.getElementById('imgCount');
    let filesState = [];

    input.addEventListener('change', function () {
        const incoming = Array.from(input.files || []).filter(f => f.type.startsWith('image/'));
        const room = 5 - filesState.length;
        const accepted = incoming.slice(0, Math.max(0, room));
        if (accepted.length < incoming.length) alert('이미지는 최대 5장까지 등록 가능합니다.');
        filesState = filesState.concat(accepted);
        syncInputWithState();
        renderPreviews();
    });

    function syncInputWithState() {
        const dt = new DataTransfer();
        filesState.forEach(f => dt.items.add(f));
        input.files = dt.files;
        countEl.textContent = String(filesState.length);
    }

    function renderPreviews() {
        // 기존 Blob URL 해제
        const oldUrls = Array.from(preview.querySelectorAll('img')).map(img => img.dataset.url);
        preview.innerHTML = '';

        filesState.forEach((file, idx) => {
            const url = URL.createObjectURL(file);

            const card = document.createElement('div');
            card.className = 'position-relative';
            card.style.width = '120px';
            card.style.height = '120px';

            const img = document.createElement('img');
            img.src = url;
            img.dataset.url = url; // revoke용 저장
            img.alt = file.name;
            img.className = 'rounded border';
            img.style.width = '120px';
            img.style.height = '120px';
            img.style.objectFit = 'cover';

            const delBtn = document.createElement('button');
            delBtn.type = 'button';
            delBtn.className = 'btn btn-sm btn-danger position-absolute top-0 end-0 m-1';
            delBtn.setAttribute('aria-label', '삭제');
            delBtn.innerHTML = '<i class="bi bi-x-lg"></i>';
            delBtn.addEventListener('click', function () {
                filesState.splice(idx, 1);
                syncInputWithState();
                renderPreviews();
            });

            card.appendChild(img);
            card.appendChild(delBtn);
            preview.appendChild(card);
        });

        // 업로드 타일을 항상 마지막으로 재배치
        thumbs.appendChild(addTile);

        // 메모리 해제
        oldUrls.forEach(u => { try { URL.revokeObjectURL(u); } catch (e) {} });
    }
});