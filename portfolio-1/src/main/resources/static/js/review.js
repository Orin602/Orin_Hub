function ReviewWriteForm() {
	$.ajax({
        url: '/checkLogin',
        type: 'GET',
        success: function(response) {
            if (response.loggedIn) {
                window.location.href = '/reviewWriteForm'; // 로그인 상태라면 글쓰기 페이지로 이동
            } else {
                // SweetAlert2로 메시지 표시
                Swal.fire({
                    icon: 'info',
                    title: '로그인 후 이용 가능합니다.',
                    showConfirmButton: true,
                    confirmButtonText: '확인',
                    allowOutsideClick: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = '/login'; // 로그인 페이지로 이동
                    }
                });
            }
        },
        error: function(xhr, status, error) {
            console.error('Error checking login status:', error);
            // 에러 발생 시 기본적으로 로그인 페이지로 이동
            window.location.href = '/login';
        }
    });
}

// 리뷰 작성
function review_write() {
	if($("#title").val() == "") {
		swal.fire({
			title: '제목을 입력하세요.',
			icon: 'warning',
			confirmButtonText: '확인'
		});
		$("#title").focus();
		return false;
	} else if($("#content").val() == "") {
		swal.fire({
			title: '리뷰 내용을 입력하세요.',
			icon: 'warning',
			confirmButtonText: '확인'
		});
		$("#content").focus();
		return false;
	}
	$("#review-write-form").attr("action", "review-write").submit();
}



// 리뷰 수정 저장
function saveEdit() {
    var title = $("#title").val();
    var content = $("#content").val();
    
    if (title.trim() === "") {
        Swal.fire({
            title: '제목을 입력하세요.',
            icon: 'warning',
            confirmButtonText: '확인'
        });
        return;
    }
    
    if (content.trim() === "") {
        Swal.fire({
            title: '리뷰 내용을 입력하세요.',
            icon: 'warning',
            confirmButtonText: '확인'
        });
        return;
    }
    
    $.ajax({
        url: '/review/edit',
        type: 'POST',
        data: {
            reviewSeq: reviewSeq,
            title: title,
            content: content
        },
        success: function(response) {
            Swal.fire({
                title: '수정 완료',
                icon: 'success',
                confirmButtonText: '확인'
            }).then(() => {
                window.location.href = '/reviewDetail?review_seq=' + reviewSeq;
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: '수정 실패',
                text: '오류가 발생했습니다. 다시 시도해 주세요.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        }
    });
}



