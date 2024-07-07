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