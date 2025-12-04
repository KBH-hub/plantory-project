// plantSearchModal.js

const searchInput = document.querySelector("#plantSearchModal input[type='text']");
const searchBtn = document.querySelector("#plantSearchModal .input-group-text");
const resultList = document.querySelector("#plantSearchResultList");
// console.log(resultList);
// 모달 DOM
const plantSearchModalDom = document.getElementById("plantSearchModal");
let plantSearchModal = null;

// DOMContentLoaded 되면 modal instance 생성
document.addEventListener("DOMContentLoaded", () => {
    plantSearchModal = bootstrap.Modal.getOrCreateInstance(plantSearchModalDom);
    bindSearchEvent();
    bindSelectEvent();
});


function bindSearchEvent() {
    // 검색 버튼 클릭
    searchBtn.addEventListener("click", () => {
        const word = searchInput.value.trim();
        if (!word) {
            showAlert("검색어를 입력해주세요.");
            return;
        }
        searchPlants(word);
    });

    // 엔터로 검색
    searchInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            const word = searchInput.value.trim();
            if (!word) {
                showAlert("검색어를 입력해주세요.");
                return;
            }
            searchPlants(word);
        }
    });
}


async function searchPlants(word) {
    try {
        const res = await axios.get(`/api/dictionaryModal/search?word=${word}`);
        renderSearchList(res.data);
    } catch (err) {
        console.error(err);
        showAlert("검색 중 오류가 발생했습니다.");
    }
}


function renderSearchList(list) {
    resultList.innerHTML = ""; // 초기화

    if (list.length === 0) {
        resultList.innerHTML = `<div class="text-center text-muted small py-2">검색 결과가 없습니다.</div>`;
        return;
    }

    list.forEach(item => {
        const html = `
            <div class="border rounded d-flex align-items-center p-2">
                <img src="${item.fileUrl}"
                     class="me-2"
                     style="width:50px;height:50px;object-fit:cover;">
                
                <div class="flex-grow-1 small">${item.plantName}</div>

                <button type="button"
                        class="btn btn-success btn-sm px-3 text-nowrap plant-select-btn"
                        data-id="${item.id}"
                        data-type="${item.type}">
                    선택
                </button>
            </div>
        `;

        resultList.insertAdjacentHTML("beforeend", html);
    });
}


function bindSelectEvent() {
    document.addEventListener("click", async (e) => {
        const btn = e.target.closest(".plant-select-btn");
        if (!btn) return;

        const id = btn.dataset.id;
        const type = btn.dataset.type;

        let url =
            type === "garden"
                ? `/api/dictionaryModal/garden/${id}`
                : `/api/dictionaryModal/dry/${id}`;

        try {
            const res = await axios.get(url);
            const data = res.data;

            // 값 세팅할 input DOM 가져오기
            const plantNameInput = document.querySelector("#plantNameInput");
            const managementLevel = document.querySelector("#managementLevel");
            const managementNeeds = document.querySelector("#managementNeeds");

            // 식물명
            plantNameInput.value = data.plantName;

            // 관리 수준 + ENUM
            managementLevel.value = data.levelLabel;
            managementLevel.dataset.enum = data.manageLevel;

            // 관리 요구도 + ENUM
            managementNeeds.value = data.demandLabel;
            managementNeeds.dataset.enum = data.manageDemand;

            // 모달 닫기
            plantSearchModal.hide();

        } catch (err) {
            console.error(err);
            showAlert("식물 정보를 불러오지 못했습니다.");
        }
    });
}
