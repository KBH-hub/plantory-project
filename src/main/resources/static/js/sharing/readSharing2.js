(function () {
    const cache = new Map();
    const getModal = (sel) => {
        if (!cache.has(sel)) {
            cache.set(sel, new bootstrap.Modal(document.querySelector(sel), {
                backdrop: 'static',
                keyboard: false
            }));
        }
        return cache.get(sel);
    };

    const goNext = (currentSel, nextSel) => {
        const currEl = document.querySelector(currentSel);
        const next = getModal(nextSel);
        const handler = () => {
            currEl.removeEventListener('hidden.bs.modal', handler);
            next.show();
        };
        currEl.addEventListener('hidden.bs.modal', handler, { once: true });
        getModal(currentSel).hide();
    };

    const goBack = (currentSel, prevSel) => {
        const currEl = document.querySelector(currentSel);
        const prev = getModal(prevSel);
        const handler = () => {
            currEl.removeEventListener('hidden.bs.modal', handler);
            prev.show();
        };
        currEl.addEventListener('hidden.bs.modal', handler, { once: true });
        getModal(currentSel).hide();
    };

    /* ▶ 여기 추가: '나눔 완료' 버튼 → 1번 모달 오픈 */
    document.getElementById('btnComplete')
        .addEventListener('click', () => getModal('#modalSelectCounterpart').show());

    // 라디오 선택 -> "선택 완료" 버튼 활성화
    const radios = document.querySelectorAll('input[name="doneTarget"]');
    const btnSelectDone = document.getElementById('btnSelectDone');
    let selectedName = '';
    radios.forEach(r => r.addEventListener('change', () => {
        selectedName = r.value;
        btnSelectDone.disabled = !selectedName;
    }));

    // 전환 제어(+ 유효성 검사 및 이름 전달)
    document.addEventListener('click', (e) => {
        const btn = e.target.closest('[data-next],[data-back]');
        if (!btn) return;

        const current = btn.getAttribute('data-current');
        const next = btn.getAttribute('data-next');
        const back = btn.getAttribute('data-back');

        if (next) {
            if (current === '#modalSelectCounterpart' && next === '#modalConfirmDone') {
                if (!selectedName) return;
                document.getElementById('confirmName').textContent = `‘${selectedName}’`;
            }
            if (current === '#modalConfirmDone' && next === '#modalDoneResult') {
                document.getElementById('resultName').textContent = `‘${selectedName}’`;
            }
            goNext(current, next);
        } else if (back) {
            goBack(current, back);
        }
    });

    // 마지막 모달 닫힐 때 초기화
    document.getElementById('modalDoneResult').addEventListener('hidden.bs.modal', () => {
        ['modalConfirmDone', 'modalSelectCounterpart'].forEach(id => {
            const inst = bootstrap.Modal.getInstance(document.getElementById(id));
            inst && inst.hide();
        });
        radios.forEach(r => r.checked = false);
        btnSelectDone.disabled = true;
        selectedName = '';
    });
})();