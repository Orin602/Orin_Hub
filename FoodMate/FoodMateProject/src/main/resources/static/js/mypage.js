/**
 * 마이페이지 스크립트
*/

/**
 * 개인정보 수정 (닉네임, 비밀번호, 이메일) 
 */
var originalNickname = $("#nickname").val();

function change_info() {
    if ($("#password").val() == "") {
        alert("변경할 비밀번호를 입력하세요.");
        $("#password").focus();
        return false;
    } else if ($("#password").val() != $("#pwdcheck").val()) {
        alert("비밀번호가 일치하지 않습니다.");
        $("#password").focus();
        return false;
    } else if ($("#nickname").val() == "") {
        alert("변경할 닉네임을 입력하세요.");
        $("#nickname").focus();
        return false;
    } else if ($("#email").val() == "") {
        alert("변경할 이메일을 입력하세요.");
        $("#email").focus();
        return false;
    } else {
        // 기존 닉네임이 없는 경우에만 중복 확인
        if (originalNickname == "") {
            // 닉네임 중복 확인이 필요한 경우
            if (!isNicknameChecked) {
                alert("닉네임 중복 체크를 해주세요.");
                $("#nickname").focus();
                return false;
            }
        }

        // 폼 제출
        $("#update_info").attr("action", "update_info").submit();
    }
}

/**
 * 닉네임 중복확인 화면 출력요청
 */
function nickcheck() {
    if ($("#nickname").val() == "") {
        alert("닉네임을 입력해 주세요!");
        $("#nickname").focus();
        return false;
    } else if ($("#nickname").val() == " ") {
        alert("닉네임을 입력해 주세요!");
        $("#nickname").focus();
        return false;
    }

    // 닉네임 중복 확인 팝업 열기
    var url = "nickname_check_form?nickname=" + $("#nickname").val();
    window.open(url, "_blank_", "toolbar=no, menubar=no, scrollbars=no, " +
        "resizable=yes, width=600, height=400");
}

// 닉네임 입력 필드의 값이 변경될 때 중복 체크 상태를 초기화합니다.
$("#nickname").on('input', function() {
	isNicknameChecked = false;
	$("#renickname").val('');
});

// 중복 체크 완료 후 닉네임을 설정하는 함수
function setNicknameChecked() {
	isNicknameChecked = true;
	$("#renickname").val($("#nickname").val());
}
/**
 * 바디데이터 수정
 */
function change_bodydata() {
	if ($("#age").val() == "") {
		alert("나이를 입력해 주세요.");
		$("#age").focus();
		return false;
	} else if ($("#height").val() == "") {
		alert("키를 입력해 주세요.");
		$("#height").focus();
		return false;
	} else if ($("#weight").val() == "") {
		alert("몸무게를 입력해 주세요.");
		$("#weight").focus();
		return false;
	} else if ($("#bmi").val() == "") {
		alert("BMI를 계산해 주세요.");
		$("#bmi").focus();
		return false;
	} else if ($("#goal").val() == "") {
		alert("목표를 입력해 주세요.");
		$("#goal").focus();
		return false;
	} else {
		$("#update_body").attr("action", "update_body").submit();
	}
}

/*
** bmi 계산 버튼
*/
function my_bmi() {
	var height = document.getElementById('height').value;
	var weight = document.getElementById('weight').value;
	var age = document.getElementById('age').value;
	var gender = document.getElementById('gender').value;

	if (height > 0 && weight > 0 && age > 0 && (gender === 'Male' || gender === 'Female')) {
		var height_m = height / 100;
		var weight_kg = weight;
		var bmi = weight_kg / (height_m ** 2);

		var bmr;
		if (gender === 'Male') {
			bmr = 88.362 + (13.397 * weight_kg) + (4.799 * height_m * 100) - (5.677 * age);
		} else {
			bmr = 447.593 + (9.247 * weight_kg) + (3.098 * height_m * 100) - (4.330 * age);
		}

		var calories_threshold = bmr * 1.2;

		document.getElementById('bmi').value = bmi.toFixed(2);
		document.getElementById('calories_threshold').value = calories_threshold.toFixed(2);
	} else {
		alert('키와 몸무게, 나이, 성별을 올바르게 입력하세요.');
	}
}

// 체중변화 입력값 받기
function weight_record() {
	
	 var weight = parseFloat($("#re_weight").val());

    // 체중이 0 또는 0.0이면 입력하지 않도록 검증
    if (weight === 0 || weight === 0.0) {
        alert("체중은 0이나 0.0을 입력할 수 없습니다.");
        $("#re_weight").focus();
        return false;
    }

	
    if ($("#re_date").val() === "") {
        alert("날짜를 제대로 입력해 주세요.");
        $("#re_date").focus();
        return false;
    } else if ($("#re_weight").val() === "") {
        alert("몸무게를 제대로 입력해 주세요.");
        $("#re_weight").focus();
        return false;
    } else {
        // 폼 데이터를 직렬화하여 전송
        $.post("/weight_record", $("#weight_chart").serialize())
            .done(function(response) {
                alert("저장 성공!!");
                // 그래프 업데이트
                updateCharts();
            })
            .fail(function() {
                alert("저장 실패. 다시 시도해 주세요.");
            });
    }
    window.location.reload();
}
// 체중변화 차트 그리기
$(document).ready(function() {
            // input 요소 참조
            var weightInput = document.getElementById('re_weight');
            // kg를 표시할 span 요소 참조
            var kgSpan = document.getElementById('kg');

            // input 값이 변경될 때마다 이벤트 처리
            if (weightInput) {
                weightInput.addEventListener('input', function() {
                    // input 요소의 값 뒤에 'kg' 추가하여 span 요소에 표시
                    kgSpan.textContent = 'kg';
                });
            }

            if ($('#weeklyChartCanvas').length && $('#monthlyChartCanvas').length) {
                // 서버에서 데이터 가져오기
                $.ajax({
                    url: '/getRecordChart', // 데이터를 가져올 서버 URL
                    method: 'GET', // HTTP GET 메서드 사용
                    headers: {
                        Accept: 'application/json' // 서버 응답을 JSON으로 기대
                    },
                    success: function(response) {
                        // 서버가 리디렉션 URL을 반환하면 해당 URL로 이동
                        if (response.redirect) {
                            window.location.href = response.redirect;
                            return;
                        }

                        // 서버 응답 데이터 처리
                        var weeklyData = response.weeklyData.reverse();	// 최근 데이터를 뒤로 보내기 위해 역순으로 배열을 뒤집음
                        var monthlyData = response.monthlyData.reverse(); // 최근 데이터를 뒤로 보내기 위해 역순으로 배열을 뒤집음

                        // Chart.js를 사용하여 차트 그리기
                        drawWeightChart(weeklyData, '주간 체중 기록', 'weeklyChartCanvas');
                        drawWeightChart(monthlyData, '월간 체중 기록', 'monthlyChartCanvas');
                    },
                    error: function(err) {
                        console.error('차트 데이터를 가져오는 중 오류 발생', err);
                    }
                });
            }
        });

        /**
         * Chart.js를 사용하여 체중 변화를 시각화하는 함수
         * @param {Array} data - 서버에서 가져온 체중 데이터 배열
         * @param {String} title - 차트 제목
         * @param {String} chartElementId - 차트를 렌더링할 캔버스 요소의 ID
         */
        function drawWeightChart(data, title, chartElementId) {
			// 이전에 그려진 차트를 파괴
		    var existingChart = Chart.getChart(chartElementId);
		    if (existingChart) {
		        existingChart.destroy();
		    }
		    
            // 데이터 레이블(날짜)과 데이터 값(체중) 추출
            var labels = data.map(record => new Date(record.re_date).toLocaleDateString('ko-KR'));
            var values = data.map(record => record.re_weight);

            // 차트 데이터 및 옵션 설정
            var chartData = {
                labels: labels,
                datasets: [{
                    label: title,
                    data: values,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                    fill: false
                }]
            };

            var options = {
                responsive: true,
                scales: {
                    x: {
                        beginAtZero: true
                    },
                    y: {
                        beginAtZero: true
                    }
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'bottom'
                    }
                }
            };

            // Chart.js를 사용하여 라인 차트 생성
            var ctx = document.getElementById(chartElementId).getContext('2d');
            new Chart(ctx, {
                type: 'line',
                data: chartData,
                options: options
            });
        }


   
        

function delHistory() {
	var checkboxes = document
		.querySelectorAll('input[name="delete"]:checked');
	if (checkboxes.length === 0) {
		alert("삭제할 항목을 선택하세요.");
		return;
	}

	var confirmation = confirm("정말로 삭제하시겠습니까?");
	if (confirmation) {
		var deleteIdxArray = [];
		checkboxes.forEach(function(checkbox) {
			deleteIdxArray.push(Number(checkbox.value));
		});

		$.ajax({
			type: "POST",
			url: "/deleteHistory",
			data: JSON.stringify(deleteIdxArray),
			contentType: "application/json; charset=utf-8",
			success: function(response) {
				if (response === "success") {
					location.reload();
				} else {
					console.error("로그인이 필요합니다.");
				}
			},
			error: function(xhr, status, error) {
				console.error("오류가 발생했습니다:", error);
			}
		});
	}
}

function openChart(idx) {
	window.open('chart.html?idx=' + idx, '_blank', 'width=400, height=600');
}





        
$(document).ready(function() {
    $('.menu-link').on('click', function(event) {
        event.preventDefault();
        var url = $(this).data('url');
        $('#content').load(url);
    });
});