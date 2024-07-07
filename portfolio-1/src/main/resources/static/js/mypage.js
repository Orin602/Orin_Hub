$(document).ready(function() {
    var isNicknameChecked = false;

    window.showAlert = function() {
        Swal.fire({
            title: '수정할 수 없는 항목입니다.',
            icon: 'info',
            confirmButtonText: '확인'
        });
    }

    $("#nick-check-button").click(function() {
        var nickname = $("#nickname").val();
        if (nickname == "") {
            Swal.fire({
                title: '닉네임을 입력하세요.',
                icon: 'error',
                confirmButtonText: '확인'
            });
            $("#nickname").focus();
            return false;
        }

        $.ajax({
            type: "GET",
            url: "/nick-check-form",
            data: { nickname: nickname },
            success: function(response) {
                if (response.result === 1) {
                    Swal.fire({
                        title: '중복된 닉네임입니다.',
                        icon: 'error',
                        confirmButtonText: '확인'
                    });
                    isNicknameChecked = false;
                } else {
                    Swal.fire({
                        title: '사용 가능한 닉네임입니다.',
                        icon: 'success',
                        confirmButtonText: '확인'
                    });
                    isNicknameChecked = true;
                }
            },
            error: function() {
                Swal.fire({
                    title: '오류 발생',
                    text: '닉네임 중복 확인 중 오류가 발생했습니다.',
                    icon: 'error',
                    confirmButtonText: '확인'
                });
                isNicknameChecked = false;
            }
        });
    });

    $("#info-update-button").click(function() {
        if ($("#pwd").val() == "") {
            Swal.fire({
                title: '비밀번호를 입력하세요.',
                icon: 'error',
                confirmButtonText: '확인'
            });
            $("#pwd").focus();
            return false;
        } else if ($("#pwd-check").val() == "") {
            Swal.fire({
                title: '비밀번호 확인을 입력하세요.',
                icon: 'error',
                confirmButtonText: '확인'
            });
            $("#pwd-check").focus();
            return false;
        } else if ($("#pwd").val() != $("#pwd-check").val()) {
            Swal.fire({
                title: '비밀번호가 일치하지 않습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
            $("#pwd-check").focus();
            return false;
        } else if (!isNicknameChecked) {
            Swal.fire({
                title: '닉네임 중복 체크를 해주세요.',
                icon: 'error',
                confirmButtonText: '확인'
            });
            return false;
        }

        $.ajax({
            type: "POST",
            url: "/updateInfo",
            data: $("#info-update-form").serialize(),
            success: function(response) {
                Swal.fire({
                    title: '수정 완료',
                    icon: 'success',
                    confirmButtonText: '확인'
                }).then((result) => {
                    window.location.href = '/mypage';
                });
            },
            error: function() {
                Swal.fire({
                    title: '오류 발생',
                    text: '정보 수정 중 오류가 발생했습니다.',
                    icon: 'error',
                    confirmButtonText: '확인'
                });
            }
        });
    });
});
