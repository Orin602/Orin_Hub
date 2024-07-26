document.addEventListener("scroll", function() {
	var header = document.querySelector(".h-container");
	var fixedHeader = document.querySelector(".fixed-header");

	// header와 fixedHeader가 존재하는지 확인
	if (header && fixedHeader) {
		var headerBottom = header.getBoundingClientRect().bottom;

		// h-container가 화면 밖으로 벗어나면 보이도록 클래스 추가
		if (headerBottom <= 0) {
			fixedHeader.classList.add("visible");
		} else {
			fixedHeader.classList.remove("visible");
		}
	}
});

// 테마
function toggleTheme() {
	document.body.classList.toggle('dark-theme');
	const isDarkTheme = document.body.classList.contains('dark-theme');
	localStorage.setItem('darkTheme', isDarkTheme);

	// 아이콘 토글
	document.getElementById('themeToggleButtonLight').style.display = isDarkTheme ? 'none' : 'inline';
	document.getElementById('themeToggleButtonDark').style.display = isDarkTheme ? 'inline' : 'none';
}

// api
async function fetchLibraryData() {
    const apiKey = 'ecd06e994a7a5527438b5796093e64e3058be7a224082d4ed215e304e744966e';
    const url = `https://nl.go.kr/NL/search/openApi/saseoApi.do?key=${apiKey}`;

    try {
        const response = await fetch(url);
        const textData = await response.text();

        // XML 파싱
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(textData, "application/xml");

        // 필요한 데이터 추출
        const items = xmlDoc.getElementsByTagName("item");
        let resultHtml = '';

        for (let i = 0; i < items.length; i++) {
            const drCodeName = items[i].getElementsByTagName("drCodeName")[0].textContent;
            const recomtitle = items[i].getElementsByTagName("recomtitle")[0].textContent;
            const recomauthor = items[i].getElementsByTagName("recomauthor")[0].textContent;
            const recomcontens = items[i].getElementsByTagName("recomcontens")[0].textContent;
            const regdate = items[i].getElementsByTagName("regdate")[0].textContent;
            const mokchFilePath = items[i].getElementsByTagName("mokchFilePath")[0].textContent;

            resultHtml += `
                <div>
                    <h2>${recomtitle}</h2>
                    <p><strong>Category:</strong> ${drCodeName}</p>
                    <p><strong>Author:</strong> ${recomauthor}</p>
                    <p><strong>Content:</strong> ${recomcontens}</p>
                    <p><strong>Registration Date:</strong> ${regdate}</p>
                    <img src="${mokchFilePath}" alt="Thumbnail">
                </div>
                <hr>
            `;
        }

        document.getElementById('result').innerHTML = resultHtml;
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('result').textContent = 'Error fetching data';
    }
}