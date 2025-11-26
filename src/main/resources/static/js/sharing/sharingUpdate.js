document.addEventListener('DOMContentLoaded', function () {
    /* ====== 식물 정보 초기/선택 동기화 ====== */
    function setPlantInfo(name, condition, difficulty) {
        const nameInput = document.getElementById('plantNameInput');
        const condInput = document.getElementById('plantConditionInput');
        const diffInput = document.getElementById('plantDifficultyInput');
        const condHidden = document.getElementById('plantConditionHidden');
        const diffHidden = document.getElementById('plantDifficultyHidden');

        if (nameInput) nameInput.value = name || '';
        if (condInput) condInput.value = condition || '';
        if (diffInput) diffInput.value = difficulty || '';
        if (condHidden) condHidden.value = condition || '';
        if (diffHidden) diffHidden.value = difficulty || '';
    }

    document.querySelectorAll('.plant-select-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            setPlantInfo(
                this.getAttribute('data-plant-name') || '',
                this.getAttribute('data-plant-condition') || '',
                this.getAttribute('data-plant-difficulty') || ''
            );
        });
    });

    (function applyInitialPlantSelection(){
        const formEl = document.getElementById('editForm');
        if (!formEl) return;
        const initName = formEl.dataset.plantName || '';
        const initCond = formEl.dataset.plantCondition || '';
        const initDiff = formEl.dataset.plantDifficulty || '';
        if (initName) setPlantInfo(initName, initCond, initDiff);
    })();

    /* ====== 이미지 업로드 & 기존 이미지 주입 ====== */
    const input    = document.getElementById('plantImages');
    const thumbs   = document.getElementById('thumbs');
    const preview  = document.getElementById('previewList');
    const addTile  = document.getElementById('addTile');
    const countEl  = document.getElementById('imgCount');
    const delHidden= document.getElementById('deletedImageIds');
    const keptHidden= document.getElementById('keptImageIds');

    // 기존 이미지 모델: {id:number, url:string}
    let existingImages = [];
    // 삭제된 기존 이미지 id 목록
    let deletedExistingIds = [];
    // 새로 추가한 File 배열
    let filesState = [];

    // 페이지 로드시 기존 이미지 JSON 주입
    (function loadInitialImages(){
        const formEl = document.getElementById('editForm');
        if (!formEl) return;
        try {
            const raw = formEl.dataset.initialImages || '[]';
            const parsed = JSON.parse(raw);
            if (Array.isArray(parsed)) existingImages = parsed.filter(x => x && x.url);
        } catch(e) {
            existingImages = [];
        }
        syncHiddenFields();
        renderPreviews();
    })();

    input.addEventListener('change', function () {
        const incoming = Array.from(input.files || []).filter(f => f.type.startsWith('image/'));
        const room = 5 - getTotalCount();
        const accepted = incoming.slice(0, Math.max(0, room));
        if (accepted.length < incoming.length) alert('이미지는 최대 5장까지 등록 가능합니다.');
        filesState = filesState.concat(accepted);
        syncInputWithState();
        renderPreviews();
    });

    function getTotalCount() {
        return existingImages.length + filesState.length;
    }

    function syncInputWithState() {
        const dt = new DataTransfer();
        filesState.forEach(f => dt.items.add(f));
        input.files = dt.files;
        countEl.textContent = String(getTotalCount());
    }

    function syncHiddenFields() {
        // 삭제된 기존 이미지 id -> hidden
        if (delHidden) delHidden.value = deletedExistingIds.join(',');
        // 남아있는 기존 이미지 id -> hidden (서버가 "유지" 확인/정렬에 쓰고 싶을 때)
        if (keptHidden) keptHidden.value = existingImages.map(x => x.id).join(',');
        countEl.textContent = String(getTotalCount());
    }

    function renderPreviews() {
        // 기존 Blob URL 해제
        const oldUrls = Array.from(preview.querySelectorAll('img')).map(img => img.dataset.url);
        preview.innerHTML = '';

        // 1) 기존 이미지 먼저 렌더
        existingImages.forEach((item, idx) => {
            const card = document.createElement('div');
            card.className = 'position-relative';
            card.style.width = '120px';
            card.style.height = '120px';

            const img = document.createElement('img');
            img.src = item.url;
            img.alt = 'existing-' + item.id;
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
                // 삭제: 기존 배열에서 제거 + 삭제목록에 id 추가
                const removed = existingImages.splice(idx, 1)[0];
                if (removed && removed.id != null) {
                    deletedExistingIds.push(removed.id);
                }
                syncHiddenFields();
                renderPreviews();
            });

            // 기존 이미지 배지
            const badge = document.createElement('span');

            card.appendChild(img);
            card.appendChild(delBtn);
            card.appendChild(badge);
            preview.appendChild(card);
        });

        // 2) 새로 업로드된 파일 렌더
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

            const badge = document.createElement('span');

            card.appendChild(img);
            card.appendChild(delBtn);
            card.appendChild(badge);
            preview.appendChild(card);
        });

        // 업로드 타일을 항상 마지막으로 재배치
        thumbs.appendChild(addTile);

        // 메모리 해제
        oldUrls.forEach(u => { try { URL.revokeObjectURL(u); } catch (e) {} });

        syncHiddenFields();
    }
});