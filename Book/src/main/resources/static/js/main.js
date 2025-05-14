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

function goToReviewPage(bookTitle, bookAuthor, bookImageUrl) {
    // 책 정보를 URL 파라미터로 전달하거나 세션에 저장하고 리뷰 작성 페이지로 이동
    window.location.href = `/review-write-form2?bookTitle=${encodeURIComponent(bookTitle)}&bookAuthor=${encodeURIComponent(bookAuthor)}&bookImageUrl=${encodeURIComponent(bookImageUrl)}`;
}

// 도서 검색
function search_book() {
    if ($("#query").val() === "") {
        swal.fire({
            title: '검색어를 입력하세요.',
            icon: 'warning',
            confirmButtonText: '확인'
        });
        $("#query").focus();
        return false;
    } else {
        let srchTarget = $("#srchTarget").val();
        $("#search-form").attr("action", `/search-book?srchTarget=${srchTarget}`).submit();
    }
}

