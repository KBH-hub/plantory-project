// 전체 선택
document.getElementById('checkAll').addEventListener('change', (e) => {
    document.querySelectorAll('.row-check').forEach(cb => cb.checked = e.target.checked);
});
// 삭제(데모)
document.getElementById('btnDelete').addEventListener('click', () => {
    const n = [...document.querySelectorAll('.row-check')].filter(c => c.checked).length;
    alert(n ? `${n}건 선택됨 (삭제 로직 연동)` : '선택된 쪽지가 없습니다.');
});