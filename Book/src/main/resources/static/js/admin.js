// 관리자 로그인
function admin_login() {
	if($("#id").val() == "") {
		swal.fire({
			title: 'ID를 입력해 주세요.',
			icon: 'warning'
		});
		return false;
	} else if($("#pwd").val() == "") {
		swal.fire({
            title: 'Password를 입력해 주세요',
            icon: 'warning'
        });
        return false;
	}
	$("#admin-login-form").attr("action", "/admin_ok").submit();
}

// 로그아웃
function logout() {
	swal.fire({
		icon: 'info',
		title: '로그아웃',
		text: '정말록 로그아웃 하시겠습니까?',
		showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니요'
	}).then((result) => {
        if (result.isConfirmed) {
            // 로그아웃 요청
            fetch('/admin-logout', {
                method: 'GET', // GET 요청
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.ok) {
                    // 성공적으로 로그아웃된 경우 메인 페이지로 이동
                    window.location.href = '/main'; // 또는 사용하고자 하는 메인 페이지 URL
                } else {
                    Swal.fire('오류', '로그아웃에 실패했습니다.', 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire('오류', '로그아웃 중 오류가 발생했습니다.', 'error');
            });
        }
    });
	
}

// 공지사항 작성
function notice_write() {
	if($("#title").val() == "") {
		swal.fire({
			title: '제목은을 입력해주세요.',
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
			title: '공지사항 작성 성공!',
			text: '공지사항 목록 페이지로 이동합니다.',
			icon: 'success',
			confirmButtonText: '확인'
		}).then((result) => {
			if(result.isConfirmed) {
				$("#notice-write-form").attr("action", "/notice-write-action").submit();
			}
		});
	}
	
}

// 공지사항 삭제
function noticedelete() {
	const noticeSeq = document.getElementById("notice-seq").value; // 공지사항의 ID를 가져오는 부분
    const noticeTitle = document.getElementById("notice-title").innerText; // 제목을 가져오는 부분

    Swal.fire({
        title: '삭제 확인',
        text: `${noticeTitle} 공지사항을 삭제하시겠습니까?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '삭제',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            // AJAX 요청을 통해 공지사항 삭제
            $.ajax({
                url: '/notice/delete', // 삭제 요청 URL
                type: 'DELETE',
                data: { notice_seq: noticeSeq }, // 삭제할 공지사항의 ID
                success: function(response) {
                    // 삭제 성공 시
                    Swal.fire({
                        title: '삭제 완료',
                        text: '공지사항이 삭제되었습니다.',
                        icon: 'success'
                    }).then(() => {
                        location.href = '/admin-notice-list';
                    });
                },
                error: function(xhr, status, error) {
                    // 삭제 실패 시
                    Swal.fire({
                        title: '삭제 실패',
                        text: '공지사항 삭제에 실패했습니다.',
                        icon: 'error'
                    });
                }
            });
        }
    });
}

// 이미지 삭제
function deleteImage(buttonElement) {
	const notice_seq = buttonElement.getAttribute('data-notice-seq');
	const imageIndex = buttonElement.getAttribute('data-index');
	const remainingImages = [];
	$(".uploaded-image").each(function(index, element) {
		if(index !== imageIndex) {
			remainingImages.push($(element).find("img").attr("src"));
		}
	});
	// 삭제된 이미지를 제외한 나머지 이미지를 서버로 전송
    $("#notice-edit-form").append(
        $("<input>", {
            type: "hidden",
            name: "uploadedImages",
            value: remainingImages.join(",")
        })
    );
    $.ajax({
        url: '/delete-image-notice',
        type: 'GET',
        data: { 
            notice_seq: notice_seq,
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

// 공지사항 수정
function update_notice() {
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
			title: '리뷰 수정 성공',
			icon: 'success',
			confirmButtonText: '확인'
		}).then((result) => {
			if(result.isConfirmed) {
				$("#notice-edit-form").attr("action", "/update-notice").submit();
			}
		});
	}
}

// 회원관리
// 검색 입력에 따른 실시간 필터링 기능
$(document).ready(function() {
    $('#searchMember').on('input', function() {
        const searchValue = $(this).val().toLowerCase();
        $('#memberTableBody tr').filter(function() {
            $(this).toggle($(this).find('td:first').text().
            	toLowerCase().indexOf(searchValue) > -1);
        });
    });
});
// 회원코드 수정 함수
function updateMemberCode(button) {
	const memberId = button.getAttribute('data-memberid'); 
    const currentMemberCode = button.getAttribute('th:alt'); 

    Swal.fire({
        title: '회원코드를 수정합니다.',
        input: 'number',
        inputLabel: '새로운 회원코드를 입력하세요.',
        inputPlaceholder: '0 또는 1을 입력하세요.',
        showCancelButton: true,
        confirmButtonText: '수정',
        cancelButtonText: '취소',
        preConfirm: (newMemberCode) => {
            if (!newMemberCode || newMemberCode < 0 || newMemberCode > 1) {
                Swal.showValidationMessage('올바른 회원코드를 입력하세요. (0 또는 1)');
                return false; // 유효성 검사 실패 시 false 반환
            }
            return newMemberCode; // 유효성 검사 통과 시 새로운 회원코드 반환
        }
    }).then((result) => {
        if (result.isConfirmed) {
            // AJAX 요청을 통해 서버에 업데이트 요청을 보냄
            $.ajax({
                url: `/update-membercode`,
                type: 'post',
                data: { 
                    id: memberId, // 회원 ID 추가
                    newMemberCode: result.value // 새로운 회원코드를 포함
                },
                success: function(response) {
                    Swal.fire({
	                    title: '성공',
	                    text: response,
	                    icon: 'success'
	                }).then(() => {
	                    location.reload(); // 확인 버튼 클릭 시 페이지 새로고침
	                });
                },
                error: function(xhr) {
                    // 오류 발생 시 처리
	                console.error('Error details:', xhr.status, xhr.statusText); // 오류 로그
	                if (xhr.status === 401) {
	                    Swal.fire({
	                        title: '권한 오류',
	                        text: '로그인 후 다시 시도하세요.',
	                        icon: 'warning'
	                    }).then(() => {
	                        window.location.href = '/admin/admin_login'; // 관리자 로그인 페이지로 리다이렉트
	                    });
	                } else {
	                    Swal.fire('오류', '회원코드 수정에 실패했습니다. 다시 시도해주세요.', 'error');
	                }
                }
            });
        }
    });
}






