  document.addEventListener("DOMContentLoaded", function () {

    const sidoSelect = document.getElementById("sido");
    const sigunguSelect = document.getElementById("sigungu");

    fetch("/data/korea.json")
      .then(response => response.json())
      .then(data => {
        Object.keys(data).forEach(sido => {
          let option = document.createElement("option");
          option.value = sido;
          option.textContent = sido;
          sidoSelect.appendChild(option);
        });
        document.dispatchEvent(new Event("koreaOptionsLoaded"));

        sidoSelect.addEventListener("change", function () {
          const selected = this.value;
          sigunguSelect.innerHTML = '<option value="">시/군/구</option>';

          if (!selected) return;

          data[selected].forEach(sigungu => {
            const option = document.createElement("option");
            option.value = sigungu;
            option.textContent = sigungu;
            sigunguSelect.appendChild(option);
          });
        });
      });
  });