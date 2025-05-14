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
			if(result.isConfirmed) {
				$("#qna-write-form").attr("action", "/qna-write-action").submit();
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
			if(result.isConfirmed) {
				$("#qna_edit_form").attr("action", "/qna-edit-action").submit();
			}
		});
	}
}

// 좋아요 버튼
function like_btn(notce_seq) {
	const noticeSeq = document.getElementById('notice-seq').value;
	
	fetch('/customer_notice_like', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ notice_seq: noticeSeq })
    })
    .then(response => {
        if (!response.ok) throw new Error('서버 오류');
        return response.json();
    })
	.then(data => {
        if (data.status === 'fail') {
            Swal.fire('실패', data.message, 'warning');
        } else {
            location.reload();  // 성공 시 페이지 새로고침
        }
    })
    .catch(error => {
        console.error('좋아요 요청 실패:', error);
        Swal.fire('에러', '좋아요 처리 중 오류가 발생했습니다.', 'error');
    });
}