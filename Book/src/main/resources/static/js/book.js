// 도서 검색
function search_book() {
	if($("#query").val() == "") {
		swal.fire({
			title: '검색어를 입력하세요.',
			icon: 'warning',
			confirmButtonText: '확인'
		});
		$("#query").focus();
		return false;
	} else {
		$("#search-form").attr("action", "/search-book").submit();
	}
}