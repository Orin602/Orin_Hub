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



// 댓글 작성
function reply_write() {
    var content = $("#content").val();
    var reviewSeq = $("input[name='review_seq']").val();
    var memberId = $("input[name='member_id']").val();
    
    if (content.trim() === "") {
        swal.fire({
            title: '댓글 작성 실패',
            text: '댓글을 입력해 주세요.',
            icon: 'error',
            confirmButtonText: '확인'
        });
        $("#content").focus();
        return;
    }

    var reviewSeq = $("input[name='review_seq']").val();

    // 댓글 작성 요청
    $.ajax({
        url: '/write-reply',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            review_seq: reviewSeq,
            member_id: memberId,
            content: content
        }),
        success: function(response) {
            swal.fire({
                title: '댓글 작성 성공',
                text: response,
                icon: 'success',
                confirmButtonText: '확인'
            }).then(() => {
                location.reload(); // 댓글 작성 후 페이지 새로고침
            });
        },
        error: function(xhr, status, error) {
            swal.fire({
                title: '댓글 작성 실패',
                text: '서버에서 오류가 발생했습니다. 다시 시도해 주세요.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        }
    });
}

// 댓글 좋아요 처리
function replyLike(replySeq) {
	console.log("replySeq:", replySeq); // 확인용 로그
    $.ajax({
        url: `/replies/${replySeq}/like`,
        method: 'POST',
        success: function(response) {
            swal.fire({
                title: '좋아요 성공',
                text: '좋아요가 성공적으로 추가되었습니다.',
                icon: 'success',
                confirmButtonText: '확인'
            }).then(() => {
                location.reload(); // 페이지 새로 고침
            });
        },
        error: function(xhr, status, error) {
            swal.fire({
                title: '에러',
                text: '좋아요 처리 중 오류가 발생했습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        }
    });
}

// 댓글 삭제 처리
function replyDelete(replySeq) {
	console.log("replySeq:", replySeq); // 확인용 로그
    swal.fire({
        title: '댓글 삭제 확인',
        text: '정말로 이 댓글을 삭제하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `/replies/${replySeq}`,
                method: 'DELETE',
                success: function(response) {
                    swal.fire({
                        title: '댓글 삭제 성공',
                        text: '정상적으로 댓글을 삭제했습니다.',
                        icon: 'success',
                        confirmButtonText: '확인'
                    }).then(() => {
                        location.reload(); // 페이지 새로 고침
                    });
                },
                error: function(xhr, status, error) {
                    swal.fire({
                        title: '댓글 삭제 실패',
                        text: '댓글 삭제에 실패했습니다.',
                        icon: 'error',
                        confirmButtonText: '확인'
                    });
                }
            });
        }
    });
}




