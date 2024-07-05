$(document).ready(function() {
	var isIdChecked = false;
    // 로그인 버튼 클릭 시
    $("#login-button").click(function() {
        login();
    });

	// 회원가입 버튼 클릭 시
	$("#join-button").click(function() {
		if ($("#id").val() == "") {
			swal.fire({
				title: 'ID를 입력해 주세요',
				icon: 'warning'
			});
			$("#id").focus();
			return false;
		} else if ($("#email").val() == "") {
			swal.fire({
				title: 'Email을 입력해 주세요',
				icon: 'warning'
			});
			$("#email").focus();
			return false;
		} else if ($("#pwd").val() == "") {
			swal.fire({
				title: 'Password를 입력해 주세요',
				icon: 'warning'
			});
			$("#pwd").focus();
			return false;
		} else if ($("#name").val() == "") {
			swal.fire({
				title: 'Name을 입력해 주세요',
				icon: 'warning'
			});
			$("#name").focus();
			return false;
		} else if (!isIdChecked) {
			swal.fire({
				title: 'ID 중복 체크를 해주세요.',
				icon: 'error',
				confirmButtonText: '확인'
			});
			return false;
		}

		// AJAX를 사용하여 회원가입 폼 데이터를 서버로 전송
		$.ajax({
			type: "POST",
			url: "/join", // 서버 측의 컨트롤러 경로
			data: $("#join-form").serialize(), // 폼 데이터 직렬화하여 전송
			success: function(response) {
				Swal.fire({
					icon: 'success',
					title: '회원가입 성공',
					text: response, // 서버에서 전달한 메시지
					confirmButtonText: '확인'
				}).then((result) => {
					window.location.href = '/login'; // 로그인 페이지로 이동
				});
			},
			error: function(xhr, status, error) {
				Swal.fire({
					icon: 'error',
					title: '회원가입 실패',
					text: xhr.responseText, // 서버에서 전달한 에러 메시지
					confirmButtonText: '확인'
				});
			}
		});
    });

    // 아이디 찾기 버튼 클릭 시
    $("#find-id-button").click(function() {
        findId();
    });

    // 비밀번호 찾기 버튼 클릭 시
    $("#find-pwd-button").click(function() {
        findPwd();
    });
    $("#id-check-button").click(function() {
		var id = $("#id").val();
		if(id == "") {
			swal.fire({
				title: 'ID를 입력하세요.',
				icon: 'error',
				confirmButtonText: '확인'
			});
			$("#id").focus();
			return false;
		}
		
		$.ajax({
			type: "GET",
			url: "/id-check-form",
			data: { id : id },
			success: function(response) {
				if(response.result === 1) {
					swal.fire({
						title: '중복된 ID입니다.',
						icon: 'error',
						confirmButtonText: '확인'
					});
					isIdChecked = false;
				} else {
					swal.fire({
						title: '사용 가능한 ID입니다.',
						icon: 'success',
						confirmButtonText: '확인'
					});
					isIdChecked = true;
				}
			},
			error: function() {
				swal.fire({
					title: '오류 발생',
					text: '아이디 중복 확인 중 오류가 발생했습니다.',
					icon: 'error',
					confirmButtonText: '확인'
				});
				isIdChecked = false;
			}
		});
	});
});

// 로그인
function login() {
    if ($("#id").val() == "") {
        swal.fire({
            title: 'ID를 입력해 주세요',
            icon: 'warning'
        });
        return false;
    } else if ($("#pwd").val() == "") {
        swal.fire({
            title: 'Password를 입력해 주세요',
            icon: 'warning'
        });
        return false;
    }
    $("#login-form").attr("action", "/login").submit();
}



// 아이디 찾기 화면
function findId() {
    window.open('/find-id', '아이디찾기', 'width=400,height=300');
}

// 비밀번호 찾기 화면
function findPwd() {
    window.open('/find-pwd', '비밀번호찾기', 'width=400,height=350');
}

// 비밀번호 찾기 결과
function findPwdResult() {
    if ($("#id").val() == "") {
        swal.fire({
            title: 'ID를 입력해 주세요',
            icon: 'warning'
        });
        $("#id").focus();
        return false;
    } else if ($("#name").val() == "") {
        swal.fire({
            title: 'Name을 입력해 주세요',
            icon: 'warning'
        });
        $("#name").focus();
        return false;
    } else if ($("#email").val() == "") {
        swal.fire({
            title: 'Email을 입력해 주세요',
            icon: 'warning'
        });
        $("#email").focus();
        return false;
    }
    $("#find-pwd-form").attr("action", "/findpwdresult").submit();
}

// 아이디 찾기 결과
function findIdResult() {
    if ($("#name").val() == "") {
        swal.fire({
            title: 'Name을 입력해 주세요',
            icon: 'warning'
        });
        $("#name").focus();
        return false;
    } else if ($("#email").val() == "") {
        swal.fire({
            title: 'Email을 입력해 주세요',
            icon: 'warning'
        });
        $("#email").focus();
        return false;
    }
    $("#find-id-form").attr("action", "/findidresult").submit();
}

// 확인 == 닫기
function ok() {
    self.close();
}
