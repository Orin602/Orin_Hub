// 질문 작성
function qna_write() {
	if($("#title").val() == "") {
		swal.fire({
			title: '제목을 입력해주세요.',
			text: '제목은 필수 입력 항목입니다.',
			icon: 'warning',
			confirmButtonText: '확인'
		});
		$("#title").focus();
		return false;
	} else if($("#content").val() == "") {
		swal.fire({
			title: '내용을 입력해주세요.',
			text: '내용은 필수 입력 항목 입니다.',
			icon: 'warning',
			confirmButtonText: '확인'
		});
		$("#content").focus();
		return false;
	} else {
		swal.fire({
			title: '질문 작성 성공!',
			text: '질의응답 페이지로 이동합니다.',
			icon: 'success',
			confirmButtonText: '확인'
		}).then((result) => {
			if(result.isconfirmed) {
				$("#qna_write_form").attr("action", "/qna-write-action").submit();
			}
		});
	}
}

// 질문 수정
function qna_update() {
	if($("#title").val() == "") {
		swal.fire({
			title: '제목을 입력해주세요.',
			text: '제목은 필수 입력 항목입니다.',
			icon: 'warning',
			confirmButtonText: '확인'
		});
		$("#title").focus();
		return false;
	} else if($("#content").val() == "") {
		swal.fire({
			title: '내용을 입력해주세요.',
			text: '내용은 필수 입력 항목 입니다.',
			icon: 'warning',
			confirmButtonText: '확인'
		});
		$("#content").focus();
		return false;
	} else {
		swal.fire({
			title: '질문 수정 성공!',
			text: '질의응답 페이지로 이동합니다.',
			icon: 'success',
			confirmButtonText: '확인'
		}).then((result) => {
			if(result.isconfirmed) {
				$("#qna_update_form").attr("action", "/qna-update-action").submit();
			}
		});
	}
}