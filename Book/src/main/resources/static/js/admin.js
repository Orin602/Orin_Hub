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
			title: '제목은 '
		})
	}
}