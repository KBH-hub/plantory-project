
    const radios = document.querySelectorAll('input[type="checkbox"]');
    const submitBtn = document.getElementById('submitBtn');

    function checkAllSelected() {
    const agree1 = document.querySelector('input[name="agree1"]:checked');
    const agree2 = document.querySelector('input[name="agree2"]:checked');
    const agree3 = document.querySelector('input[name="agree3"]:checked');

    if (agree1 && agree2 && agree3) {
    submitBtn.disabled = false;
} else {
    submitBtn.disabled = true;
}
}

    radios.forEach(radio => {
    radio.addEventListener('change', checkAllSelected);
});