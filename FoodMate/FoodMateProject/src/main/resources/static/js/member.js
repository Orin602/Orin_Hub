
/*
** 약관 동의 여부 확인
*/
$(document).ready(function(){
    // 다음 버튼이 클릭되었을 때
    $("#nextButton").click(function() {
        // 동의 체크가 되어 있고, 비동의 체크가 되어 있지 않은 경우
        if ($(".agree").prop("checked") && !$(".disagree").prop("checked")) {
            // 회원 가입 폼 제출
            $("#join").attr("action", "/join_form");
            $("#join").submit();
        } 
        // 비동의 체크가 되어 있고, 동의 체크가 되어 있지 않은 경우
        else if ($(".disagree").prop("checked") && !$(".agree").prop("checked")) {
            // 알림 표시
            alert("약관에 동의하셔야 가입할 수 있습니다."); 
        } 
        // 동의 또는 비동의를 선택하지 않은 경우
        else {
            // 알림 표시
            alert("동의 또는 비동의를 선택해주세요.");
        }
    });
});

/*
 * 회원 가입시, 필수 입력 항목 확인
 */
function go_save() {
    // 아이디가 비어있는 경우
    if ($("#id").val() == "") {
        alert("아이디를 입력해 주세요");
        $("#id").focus();
        return false;
    } 
    // 아이디 중복 체크가 필요한 경우
    else if ($("#id").val() != $("#reid").val()) {
        alert("아이디 중복 체크를 해주세요.");
        $("#id").focus();
        return false;
    } 
    // 비밀번호가 비어있는 경우
    else if ($("#password").val() == "") {
        alert("비밀번호를 입력해 주세요.");
        $("#password").focus();
        return false;
    } 
    // 비밀번호가 확인용 비밀번호와 일치하지 않는 경우
    else if ($("#password").val() != $("#password_confirm").val()) {
        alert("비밀번호가 일치하지 않습니다.");
        $("#password").focus();
        return false;
    } 
    // 이름이 비어있는 경우
    else if ($("#name").val() == "") {
        alert("이름을 입력해 주세요.");
        return false;
    } 
    // 이메일이 비어있는 경우
    else if ($("#email").val() == "") {
        alert("이메일을 입력해 주세요.");
        return false;
    } 
    // 모든 필수 입력 항목이 채워진 경우
    else {
        $("#join").attr("action", "join").submit();
    }
}

function go_save_naver() {
    // 아이디가 비어있는 경우
    if ($("#name").val() == "") {
        alert("이름을 입력해 주세요");
        $("#name").focus();
        return false;
    } 
   
    else if ($("#nickname").val() == "") {
        alert("닉네임을 입력해 주세요.");
        $("#nickname").focus();
        return false;
    } 
    // 모든 필수 입력 항목이 채워진 경우
    else {
        $("#join").attr("action", "join_naver").submit();
    }
}

// id 중복체크
function idcheck() {
	if($("#id").val() == ""){
		alert("아이디를 입력해 주세요!");
		$("#id").focus();
		return false;
	}
	// id 중복확인 창 오픈
	var url = "id_check_form?id="+$("#id").val();
	window.open(url, "_blank_", "toolbar=no, menubar=no, scrollbars=no, " +
			"resizable=yes, width=500, height=400");
}

// email 중복체크
function emailcheck() {
	if($("#email").val() == ""){
		alert("이메일을 입력해 주세요!");
		$("#email").focus();
		return false;
	}
	// id 중복확인 창 오픈
	var url = "email_check_form?email="+$("#email").val();
	window.open(url, "_blank_", "toolbar=no, menubar=no, scrollbars=no, " +
			"resizable=yes, width=500, height=400");
}

// 로그인 폼의 유효성 검사
function validateLoginForm() {
    var id = document.getElementById("id").value.trim();
    var password = document.getElementById("password").value.trim();
    
    // 아이디 또는 비밀번호가 비어있는 경우
    if (id === '' || password === '') {
        // 알림 표시
        alert("아이디와 비밀번호를 입력해주세요.");
        // 폼 제출 중지
        return false;
    }
    // 폼 제출 가능
    return true;
}




// 사용자명 입력 확인
$(document).ready(function() {
    // 이메일 필드가 비어있는 경우
    if ($('.username_input').val() == '') {
        // 알림 표시
        alert('이메일을 입력해주세요.');
    }
});


/*
** 아이디 찾기 요청
*/
function findMemberId() {
    // 이름이 비어있는 경우
    if ($("#name").val() == "") {
        // 알림 표시
        alert("이름을 입력해 주세요.");
        // 이름 필드에 포커스 설정
        $("#name").focus();
        // 폼 제출 중지
        return false;
    } 
    // 이메일이 비어있는 경우
    else if ($("#email").val() == "") {
        // 알림 표시
        alert("이메일을 입력해 주세요.");
        // 이메일 필드에 포커스 설정
        $("#email").focus();
        // 폼 제출 중지
        return false;
    } 
    // 모든 입력이 채워진 경우
    else {
        // 아이디 찾기 요청
        var form = $("#findId");
        form.action = "find_id";  // 컨트롤러 요청 URL
        form.submit();  // 컨트롤러로 전송
    }
}

/*
** 비밀번호 찾기 요청
*/
function findPassword() {
    // 아이디가 비어있는 경우
    if ($("#id2").val() == "") {
        // 알림 표시
        alert("아이디를 입력해 주세요.");
        // 폼 제출 중지
        return false;
    } 
    // 이름이 비어있는 경우
    else if ($("#name2").val() == "") {
        // 알림 표시
        alert("이름을 입력해 주세요.");
        // 폼 제출 중지
        return false;
    } 
    // 이메일이 비어있는 경우
    else if ($("#email2").val() == "") {
        // 알림 표시
        alert("이메일을 입력해 주세요.");
        // 폼 제출 중지
        return false;
    } 
    // 모든 입력이 채워진 경우
    else {
        // 비밀번호 찾기 요청
        $("#findPW").action = "find_pwd";  // 비밀번호 찾기 URL
        $("#findPW").submit();
    }
}

/*
** 비밀번호 변경
*/
function changePassword() {
    // 비밀번호가 비어있는 경우
    if ($("#pwd").val() == "") {
        // 알림 표시
        alert("비밀번호를 입력해 주세요.");
        // 비밀번호 필드에 포커스 설정
        $("#pwd").focus();
        // 폼 제출 중지
        return false;
    } 
    // 비밀번호가 확인용 비밀번호와 일치하지 않는 경우
    else if ($("#pwd").val() != $("#pwdcheck").val()) {
        // 알림 표시
        alert("비밀번호가 맞지 않습니다. 다시 입력해 주세요");
        // 비밀번호 필드에 포커스 설정
        $("#pwd").focus();
        // 폼 제출 중지
        return false;
    } 
    // 모든 조건이 충족된 경우
    else {
        // 비밀번호 변경 요청
        $("#pwd_form").action = "change_pwd";
        $("#pwd_form").submit();
    }
}

// 비밀번호 유효성 검사
function validatePassword(password) {
    // 비밀번호가 6자 이상인지 확인
    if (password.length < 6) {
        return false;
    }
    // 비밀번호에 알파벳 대소문자가 포함되어 있는지 확인
    var hasLowerCase = /[a-z]/.test(password);
    var hasUpperCase = /[A-Z]/.test(password);
    if (!(hasLowerCase && hasUpperCase)) {
        return false;
    }
    // 비밀번호에 숫자가 포함되어 있는지 확인
    var hasNumber = /\d/.test(password);
    if (!hasNumber) {
        return false;
    }
    // 모든 조건을 만족하면 유효한 비밀번호로 간주
    return true;
}

// 폼 유효성 검사
function validateForm() {
    // 비밀번호 필드의 값 가져오기
    var password = document.getElementById("password").value;
    // 비밀번호 유효성 검사 실행
    if (!validatePassword(password)) {
        // 비밀번호가 유효하지 않은 경우 알림 표시
        alert("비밀번호는 6자 이상이어야 하며, 대소문자와 숫자를 포함해야 합니다.");
        // 폼 제출 중지
        return false;
    }
    // 유효한 비밀번호인 경우 폼 제출 가능
    return true;
}


