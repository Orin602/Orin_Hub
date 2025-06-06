$(document).ready(function() {
	var isIdChecked = false;
	var isEmailChecked = false;
    // 로그인 버튼 클릭 시
    $("#login-button").click(function() {
        login();
    });

	// 주소 검색 버튼 클릭 시 새 창 열기
	$("#address-search-button").click(function() {
	    var searchWindow = window.open("/address-search", "_blank", "width=600,height=400");
	    
	    // 주소 검색창에서 선택한 주소를 부모 창에 전달할 수 있도록 설정
	    searchWindow.onunload = function() {
	        var selectedAddress = searchWindow.document.getElementById('selected-address').value;
	        if (selectedAddress) {
	            $("#address").val(selectedAddress);  // 부모 창의 주소 입력란에 값 입력
	        }
	    };
	});
	
	// 회원가입 버튼 클릭 시
	$("#join-button").click(function() {
		
		var idRegex = /^[a-zA-Z0-9]{3,15}$/;
		var pwdRegex = /^(?=.*[!@#$%^&*()-_=+`~{}[\]|;:'",<.>/?])(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,}$/;
		
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
		} else if ($("#address").val() == "") { // 기본 주소 입력 확인
			swal.fire({
				title: '기본 주소를 입력해 주세요',
				icon: 'warning'
			});
			$("#address").focus();
			return false;
		} else if ($("#addressDetail").val() == "") { // 상세 주소 입력 확인
			swal.fire({
				title: '상세 주소를 입력해 주세요',
				icon: 'warning'
			});
			$("#addressDetail").focus();
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
		} else if (!isEmailChecked) {
			swal.fire({
				title: 'Email 중복 체크를 해주세요.',
				icon: 'error',
				confirmButtonText: '확인'
			});
			return false;
		} else if ($("#pwd").val() != $("#pwdcheck").val()) {
			swal.fire({
				title: '비밀번호가 일치하지 않습니다.',
				text: '비밀번호를 다시 입력해 주세요.',
				icon: 'error'
			});
			$("#pwd").focus();
			return false;
		} else if(!idRegex.test($("#id").val())) {
			swal.fire({
				title: '제약조건에 맞지 않습니다.',
				text: 'ID는 영문자와 숫자로만 구성되어야 하고, 3자 이상 15자 이하여야 합니다.',
				icon: 'warning'
			});
			$("#id").focus();
			return false;
		} else if(!pwdRegex.test($("#pwd").val())) {
			swal.fire({
				title: '제약조건에 맞지 않습니다.',
				text: '비밀번호는 최소 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다.',
				icon: 'warning'
			});
			$("#pwd").focus();
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
    
    // ID 중복 체크
    $("#id-check-button").click(function() {
		var idRegex = /^[a-zA-Z0-9]{3,15}$/;
		var id = $("#id").val();
		if(id == "") {
			swal.fire({
				title: 'ID를 입력하세요.',
				icon: 'error',
				confirmButtonText: '확인'
			});
			$("#id").focus();
			return false;
		} else if(!idRegex.test(id)) {
			swal.fire({
				title: '제약조건에 맞지 않습니다.',
				text: 'ID는 영문자와 숫자로만 구성되어야 하고, 3자 이상 15자 이하여야 합니다.',
				icon: 'warning'
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
						title: '이미 사용중인 ID입니다.',
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
	
	// email 중복 체크
	$("#email-check-button").click(function() {
		var email = $("#email").val();
		if(email == "") {
			swal.fire({
				title: 'Email을 입력하세요.',
				icon: 'error',
				confirmButtonText: '확인'
			});
			return false;
		}

		$.ajax({
			type: 'GET',
			url: "/email-check-form",
			data: { email : email },
			success: function(response) {
				if(response.result == 1) {
					swal.fire({
						title: '이미 사용중인 Email입니다.',
						icon: 'warning',
						confirmButtonText: '확인'
					});
					isEmailChecked = false;
				}else {
					swal.fire({
						title: '사용 가능한 Email입니다.',
						icon: 'success',
						confirmButtonText: '확인'
					});
					isEmailChecked = true;
				}
			},
			error: function() {
				swal.fire({
					title: '오류 발생',
					text: 'Email 중복 확인 중 오류가 발생했습니다.',
					icon: 'error',
					confirmButtonText: '확인'
				});
				isEmailChecked = false;
			}

		});
	});
});

// 로그인
function login() {
	// 1. ID 필드가 비어 있는지 확인
    if ($("#id").val() == "") {
        swal.fire({
            title: 'ID를 입력해 주세요',
            icon: 'warning'
        });
        return false;
    } 
    // 2. Password 필드가 비어 있는지 확인
    else if ($("#pwd").val() == "") {
        swal.fire({
            title: 'Password를 입력해 주세요',
            icon: 'warning'
        });
        return false;
    }
    // 3. 모든 필드가 채워졌다면 폼을 제출
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
