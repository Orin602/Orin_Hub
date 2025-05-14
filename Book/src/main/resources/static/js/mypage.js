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

// 탈퇴 요청
function confirmDelete() {
    Swal.fire({
        title: '회원 탈퇴 요청',
        html: `
            <input id="userId" class="swal2-input" placeholder="아이디" required>
            <input id="userPwd" type="password" class="swal2-input" placeholder="비밀번호" required>
        `,
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        confirmButtonText: '네, 탈퇴합니다!',
        cancelButtonText: '아니요',
        preConfirm: () => {
            const userId = document.getElementById('userId').value;
            const userPwd = document.getElementById('userPwd').value;
            if (!userId || !userPwd) {
                Swal.showValidationMessage('아이디와 비밀번호를 입력하세요.');
            }
            return { userId: userId, userPwd: userPwd };
        }
    }).then((result) => {
        if (result.isConfirmed) {
            // AJAX 요청
            $.ajax({
                url: '/deleteMember',
                type: 'POST', // GET에서 POST로 변경
                contentType: 'application/json',
                data: JSON.stringify({
                    id: result.value.userId,
                    pwd: result.value.userPwd
                }),
                success: function(response) {
                    Swal.fire(
                        '탈퇴 요청 성공!', 
                        '회원 탈퇴 처리까지 하루정도의 시간이 소요됩니다.',
                        'success'
                    ).then(() => {
                        window.location.href = '/logout'; // 로그아웃 페이지로 이동
                    });
                },
                error: function(xhr, status, error) {
                    Swal.fire(
                        '탈퇴 요청 실패',
                        '탈퇴 요청 중 문제가 발생했습니다. 다시 시도해주세요.',
                        'error'
                    );
                }
            });
        }
    });
}

// 주문내역 수정
function edit_order(button) {
	const storeSeq = button.getAttribute("data-storeseq");
	const popupUrl = '/edit_order?storeSeq=' + storeSeq;  // 팝업 창에서 열 URL
    const popupOptions = 'width=600,height=400,resizable=yes,scrollbars=yes';  // 팝업 창의 크기 및 옵션

    window.open(popupUrl, 'orderEditPopup', popupOptions);  // 팝업 창 열기
	
}
// 주문내역 삭제
function delete_order(button) {
	const storeSeq = button.getAttribute("data-storeseq");
	$.ajax({
        url: '/delete_order',
        type: 'GET',
        data: { storeSeq: storeSeq },
        success: function(response) {
            // 삭제 성공 시 해당 주문 내역을 화면에서 제거
            button.closest('.order-box').remove();  // 해당 주문 내역을 DOM에서 삭제
            Swal.fire('성공', '주문이 삭제되었습니다.', 'success');
        },
        error: function(error) {
            Swal.fire('실패', '주문 삭제 중 오류가 발생했습니다.', 'error');
        }
    });
}