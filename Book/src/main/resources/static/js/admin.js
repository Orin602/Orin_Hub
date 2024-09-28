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