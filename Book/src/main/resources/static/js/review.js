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
function deleteImage(buttonElement) {
	// html에서 버튼을 data- 속성을 이용해서 전달된 값을 가져옴
	const reviewSeq = buttonElement.getAttribute('data-review-seq');
	const imageIndex = buttonElement.getAttribute('data-index');
	const remainingImages = [];
    $(".uploaded-image").each(function(index, element) {
        if (index !== imageIndex) {
            remainingImages.push($(element).find("img").attr("src"));
        }
    });
    
    // 삭제된 이미지를 제외한 나머지 이미지를 서버로 전송
    $("#review-edit-form").append(
        $("<input>", {
            type: "hidden",
            name: "uploadedImages",
            value: remainingImages.join(",")
        })
    );
	$.ajax({
		url: '/delete-image',
		type: 'GET',
		data: { 
            reviewSeq: reviewSeq,
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

// 리뷰 추천
function reviewReco(buttonElement) {
	var review_seq = $(buttonElement).data('review-seq');
	$.ajax({
		url: `/review/${review_seq}/recommend`,
		method: 'POST',
		success: function(response) {
			if (response.includes("성공적으로 처리되었습니다.")) {
                swal.fire({
                    title: '리뷰 추천!',
                    text: response,
                    icon: 'success',
                    confirmButtonText: '확인'
                }).then(() => {
                    location.reload();
                });
            } else {
                swal.fire({
                    title: '추천 삭제!',
                    text: response,
                    icon: 'warning',
                    confirmButtonText: '확인'
                }).then(() => {
                    location.reload();
                });
            }
		},
		error: function(xhr, status, error) {
			swal.fire({
				title: '리뷰 추천 실패!',
				text: '추천 처리 중 오류가 발생했습니다.',
				icon: 'error',
				confirmButtonText: '확인'
			});
		}
	});
}

// 리뷰 즐겨찾기
function reviewCheck(buttonElement) {
    var review_seq = $(buttonElement).data('review-seq');
    $.ajax({
        url: `/review/${review_seq}/bookmark`, // 서버의 즐겨찾기 처리 URL
        method: 'POST',
        success: function(response, status, xhr) {
            // 상태 코드가 200(성공)일 때
            if (xhr.status === 200 && response.includes("추가")) { // 즐겨찾기 추가 성공
                swal.fire({
                    title: '즐겨찾기 성공', // 즐겨찾기 추가 성공 시
                    text: response,
                    icon: 'success',
                    confirmButtonText: '확인'
                }).then(() => {
                    location.reload();
                });
            } else if (xhr.status === 200 && response.includes("취소")) { // 즐겨찾기 삭제 성공
                swal.fire({
                    title: '즐겨찾기 삭제!', // 즐겨찾기 삭제 시
                    text: response,
                    icon: 'warning',
                    confirmButtonText: '확인'
                }).then(() => {
                    location.reload();
                });
            }
        },
        error: function(xhr, status, error) {
            // 에러가 발생했을 때
            swal.fire({
                title: '즐겨찾기 실패',
                text: '즐겨찾기 처리 중 오류가 발생했습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        }
    });
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
function replyLike(buttonElement) {
    var replySeq = $(buttonElement).data('reply-seq');
    console.log("replySeq:", replySeq); // 확인용 로그
    $.ajax({
        url: `/replies/${replySeq}/like`,
        method: 'POST',
        success: function(response) {
            // 서버 응답에 따라 메시지 설정
            var title = '좋아요 성공';
            var text = '좋아요가 성공적으로 추가되었습니다.';

            if (response.includes("취소되었습니다")) {
                title = '좋아요 취소';
                text = '좋아요가 취소되었습니다.';
            }

            swal.fire({
                title: title,
                text: text,
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
function replyDelete(buttonElement) {
    var replySeq = $(buttonElement).data('reply-seq');
    
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
                url: `/replies/${replySeq}/delete`,
                method: 'POST',
                success: function(response) {
                    // 성공 메시지 표시
                    swal.fire({
                        title: '삭제 성공',
                        text: '댓글이 성공적으로 삭제되었습니다.',
                        icon: 'success',
                        confirmButtonText: '확인'
                    }).then(() => {
                        location.reload(); // 페이지 새로 고침
                    });
                },
                error: function(xhr, status, error) {
                    // 서버 오류 처리
                    let errorMessage = '댓글 삭제에 실패했습니다.';
                    
                    // 서버에서 반환한 에러 메시지에 따라 다르게 처리
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    
                    swal.fire({
                        title: '삭제 실패',
                        text: errorMessage,
                        icon: 'error',
                        confirmButtonText: '확인'
                    });
                }
            });
        }
    });
}





