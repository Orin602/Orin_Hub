// 로그인 폼 액션
document.addEventListener('DOMContentLoaded', function() {
	document.getElementById('login-btn').addEventListener('click', function() {
		document.getElementById('login-container').style.display = 'block';
		document.getElementById('join-container').style.display = 'none';
		document.getElementById('terms-container').style.display = 'none';
		document.getElementById('findId-container').style.display = 'none';
		document.getElementById('findPW-container').style.display = 'none';
		document.getElementById('login-tab').classList.add('active');
		document.getElementById('signup-tab').classList.remove('active');
	});

	document.getElementById('signup-btn').addEventListener('click', function() {
		document.getElementById('login-container').style.display = 'none';
		document.getElementById('join-container').style.display = 'none';
		document.getElementById('terms-container').style.display = 'block';
		document.getElementById('findId-container').style.display = 'none';
		document.getElementById('findPW-container').style.display = 'none';
		document.getElementById('login-tab').classList.remove('active');
		document.getElementById('signup-tab').classList.add('active');
	});

	document.getElementById('agree-btn').addEventListener('click', function() {
		document.getElementById('terms-container').style.display = 'none';
		document.getElementById('join-container').style.display = 'block';
	});

	document.getElementById('disagree-btn').addEventListener('click', function() {
		alert('동의하지 않으면 가입할 수 없습니다.');
	});

	document.getElementById('findId-btn').addEventListener('click', function() {
		document.getElementById('login-container').style.display = 'none';
		document.getElementById('join-container').style.display = 'none';
		document.getElementById('terms-container').style.display = 'none';
		document.getElementById('findId-container').style.display = 'block';
		document.getElementById('findPW-container').style.display = 'none';
	});

	document.getElementById('findPW-btn').addEventListener('click', function() {
		document.getElementById('login-container').style.display = 'none';
		document.getElementById('join-container').style.display = 'none';
		document.getElementById('terms-container').style.display = 'none';
		document.getElementById('findId-container').style.display = 'none';
		document.getElementById('findPW-container').style.display = 'block';
	});
});





// 회원가입 : 필수 입력 항목 확인
function go_savve() {
	// 아이디
	if($("#id2").val() == "") {
		alert("아이디를 입력해 주세요.");
		$("#id2").focus();
		return false;
	} 
	// 아이디 중복 체크 확인
	else if($("#id2").val() != $("#reid").val()) {
		alert("아이디 중복 체크를 해주세요.");
		return false;
	}
	// 비밀번호
	else if($("#pwd2").val() == "") {
		alert("비밀번호를 입력해 주세요.");
		$("#pwd2").focus();
		return false;
	}
	// 입력한 비밀번호가 서로 확인하는지
	else if($("#pwd2").val() != $("#pwd_confirm").val()) {
		alert("입력한 비밀번호가 일치하지 않습니다.");
		$("#pwd2").focus();
		return false;
	}
	// 이름
	else if($("#name2").val() == "") {
		alert("이름을 입력해 주세요.");
		$("#name2").focus();
		return false;
	}
	// 이메일
	else if($("#email2").vla() == "") {
		alert("이메일을 입력해 주세요.");
		$("#email2").focus();
		return false;
	}
	// 이메일 중복체크 확인
	else if($("#email2").val() != $("#reemail").val()) {
		alert("이메일 중복 체크를 해주세요.");
		return false;
	} else {
		// 필수항목 전부 입력 시 폼 전송
		$("#join").attr("action", "join").submit();
	}
}

// 회원가입 : 중복체크 확인
function idcheck() {
    if($("#id2").val() == "") {
        alert("아이디를 입력해 주세요.");
        return false;
    }
    // 중복확인 창에 값 전달.
    var url = "/id_check_form?id=" + $("#id2").val();
    // 중복확인 창 열기
    window.open(url, "_blank", "toolbar=no, menubar=no, scrollbars=yes,"
    			+ " resizable=no, width=500, height=450");
}
function emailcheck() {
    if($("#email2").val() == "") {
        alert("이메일을 입력해 주세요.");
        return false;
    }
    // 중복확인 창에 값 전달.
    var url = "/email_check_form?email=" + $("#email2").val();
    // 중복확인 창 열기
    window.open(url, "_blank", "toolbar=no, menubar=no, scrollbars=yes,"
    			+ " resizable=no, width=500, height=450");
}

// 아이디, 비밀번호 찾기
function findMemberId() {
	if($("#find-id-name").val() == "") {
		alert("이름을 입력해 주세요.");
		return false;
	} else if($("#find-id-email").val() == "") {
		alert("이메일을 입력해 주세요.");
		return false();
	} else {
		$("#findId").attr("action", "find_id").submit();
	}
}
function findPassword() {
	 if($("#find-pw-id").val() == "") {
		alert("아이디를 입력해 주세요.");
		return false;
	 } else if($("#find-pw-name").val() == "") {
		alert("이름을 입력해 주세요.");
		return false;
	} else if($("#find-pw-email").val() == "") {
		alert("이메일을 입력해 주세요.");
		return false();
	} else {
		$("#findPw").attr("action", "find_pwd").submit();
	}
}








