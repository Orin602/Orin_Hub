// 리뷰 검색
function search_review() {
	if($("#search").val() == "") {
		swal.fire({
			title: '검색어를 입력해주세요.',
			icon: 'warning',
			confirmButtonText: '확인'
		});
		$("#search").focus();
		return false;
	}
	$("#search_review").attr("action", "/search_review").submit();
}


// 리뷰 작성
function review_write() {
	if($("#title").val() == "") {
		swal.fire({
			title: '제목을 입력해주세요.',
			text: '제목은 필수 입력 항목 입니다.',
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
			title: '리뷰 작성 성공!',
			text: '리뷰 목록 페이지로 이동합니다.',
			icon: 'success',
			confirmButtonText: '확인'
		}).then((result) => {
            if (result.isConfirmed) {
                $("#review-write-form").attr("action", "/review-write-action").submit();
            }
        });
	}
}

// 이미지 삭제
function deleteImage(review_seq, imageIndex) {
	$.ajax({
		url: '/delete-image',
		type: 'GET',
		data: { 
            review_seq: review_seq,
            imageIndex: imageIndex 
        },
		success: function(response) {
			swal.fire({
				title: '삭제 성공',
				text: '이미지를 성공적으로 삭제하였습니다.',
				icon: 'success',
				confirmButtonText: '확인'
			}).then((result) => {
				if(result.isConfirmed) {
					location.reload();
				}
			});
		},
		error: function(xhr, status, error) {
			swal.fire({
				title: '삭제 실패',
				text: '이미지 삭제 중 오류가 발생했습니다.',
				icon: 'error',
				confirmButtonText: '확인'
			});
		}
	});
}

// 리뷰 수정
function update_review() {
	if($("#title").val() == "") {
		swal.fire({
			title: '제목을 입력해주세요.',
			icon: 'warning'
		});
		$("#title").focus();
		return false;
	} else if($("#content").val() == "") {
		swal.fire({
			title: '내용을 입력해주세요.',
			icon: 'warning'
		});
		$("#content").focus();
		return false;
	} else {
		swal.fire({
			title: '리뷰 수정 성공!',
			text: '리뷰 목록 페이지로 이동합니다.',
			icon: 'success',
			confirmButtonText: '확인'
		}).then((result) => {
            if (result.isConfirmed) {
                $("#review-edit-form").attr("action", "/update-review").submit();
            }
        });
	}
	
}
