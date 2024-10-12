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
