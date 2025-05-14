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
// 리뷰 작성
function search_review_write() {
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
                $("#review-write-form").attr("action", "/search-review-write-action").submit();
            }
        });
	}
}
// 리뷰 작성
function recommend_review_write() {
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
                $("#review-write-form").attr("action", "/recommend-review-write-action").submit();
            }
        });
	}
}
function yes24_review_write() {
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
                $("#review-write-form").attr("action", "/yes24-review-write-action").submit();
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
				if (result.isConfirmed) {
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
	if ($("#title").val() == "") {
		swal.fire({
			title: '제목을 입력해주세요.',
			icon: 'warning'
		});
		$("#title").focus();
		return false;
	} else if ($("#content").val() == "") {
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

// 댓글 수정
function replyEdit(buttonElement) {
	// 댓글 작성자 id와 현재 로그인한 사용자 id 가져오기
	var replySeq = $(buttonElement).attr("data-reply-seq"); // 댓글 seq
	var replyMemberId = $(buttonElement).attr("data-reply-member-id"); // 댓글 작성자 ID
	var loginUserId = $(buttonElement).attr("data-login-user-id"); // 로그인한 사용자 ID
	
	// 댓글 작성자와 로그인한 사용자가 일치하는지 확인
	if(replyMemberId === loginUserId) {
		swal.fire({
			title: '댓글 수정',
			input: 'text',
			inputLabel: '수정할 댓글을 입력하세요.',
			inputPlaceholder: '댓글 내용을 입력해 주세요.',
			showCancelButton: true,
			confirmButtonText: '수정하기',
			cancelButtonText: '취소',
			inputValidator: (value) => {
				if(!value) {
					return '수정할 댓글 내용을 입력해야 합니다.';
				}
			}
		}).then((result) => {
			if(result.isConfirmed) {
				var updatedReply = result.value;
				
				$.ajax({
					url: '/update-reply',	// controller 요청 url
					type: 'PUT',
					contentType: 'application/json',
					data: JSON.stringify({
						replySeq: replySeq,
						content: updatedReply
					}),
					success: function(response) {
						swal.fire({
							title: '수정 완료',
							text: '댓글을 성공적으로 수정하였습니다.',
							icon: 'success',
							confirmButtonText: '확인'
						}).then(() => {
							location.reload();
						});
					},
					error: function(xhr, status, error) {
						swal.fire({
							title: '수정 실패',
							text: '서버에서 오류가 발생했습니다. 다시 시도해 주세요.',
							icon: 'error',
							confirmButtonText: '확인'
						});
					}
				});
			}
		});
	} else { // 댓글 작성자와 수정버튼을 누른 사용자가 다를경우
		swal.fire({
			title: '수정 불가',
			text: '댓글 작성자만 수정이 가능합니다.',
			icon: 'warning',
			confirmButtonText: '확인'
		});
	}
}

// 댓글 삭제
function replyDelete(buttonElement) {
	// 버튼에서 댓글 정보와 사용자 ID 가져오기
	var replySeq = $(buttonElement).data('reply-seq'); // 댓글 시퀀스
	var replyMemberId = $(buttonElement).data('reply-member-id'); // 댓글 작성자 ID
	var loginUserId = $(buttonElement).data('login-user-id'); // 현재 로그인한 사용자 ID
	
	if(replyMemberId === loginUserId) { // 작성자와 로그인한 사용자가 일치할 경우
		swal.fire({
			title: '댓글 삭제 확인',
			text: '정말로 이 댓글을 삭제하시겠습니까?',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: '삭제',
			cancelButtonText: '취소',
			reverseButtons: true
		}).then((result) => {
			if(result.isConfirmed) {
				$.ajax({
					url: `/replies/${replySeq}/delete`,
					method: 'POST',
					success: function(response) {
						swal.fire({
							title: '삭제 성공',
							text: '댓글을 성공적으로 삭제하였습니다.',
							icon: 'success',
							confirmButtonText: '확인'
						}).then(() => {
							location.reload();
						});
					},
					error: function(xhr, status, error) {
						let errorMessage = '댓글 삭제에 실패했습니다.';
						
						// 서버에서 반환한 에러 메시지 처리
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
	} else {
		swal.fire({
		    title: '삭제 불가',
		    text: '댓글 작성자만 삭제할 수 있습니다.',
		    icon: 'warning',
		    confirmButtonText: '확인'
		});
	}
}

