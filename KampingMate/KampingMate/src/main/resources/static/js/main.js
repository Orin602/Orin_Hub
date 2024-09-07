document.addEventListener('DOMContentLoaded', function() {
	// 위치정보
	function getLocation() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(processPosition, showError);
		} else {
			alert("현재 위치를 가져올 수 없습니다...");
		}
	}

	// 위치정보 가져오는 중 에러 처리
	function showError(error) {
		switch(error.code) {
        case error.PERMISSION_DENIED:
            alert("사용자가 위치 정보 접근을 거부했습니다.");
            break;
        case error.POSITION_UNAVAILABLE:
            alert("위치 정보를 사용할 수 없습니다.");
            break;
        case error.TIMEOUT:
            alert("위치 정보를 가져오는 시간이 초과되었습니다.");
            break;
        case error.UNKNOWN_ERROR:
            alert("알 수 없는 오류가 발생했습니다.");
            break;
    	}
	}

	// 위치정보 처리
	function processPosition(position) {
		var latitude = position.coords.latitude;
		var longitude = position.coords.longitude;
		console.log("위도: " + latitude + ", 경도: " + longitude);
		
		// 변환 좌표
		var NXNY = convertToKmaCoordinates(latitude, longitude);
		console.log("격자 좌표(nx: " + NXNY.nx + ", ny: " + NXNY.ny + ")");
		
		weatherData(NXNY.nx, NXNY.ny);
		
		// 초기 "오늘"의 기온 그래프를 그리기 위해 호출
    	weatherDay("today", NXNY.nx, NXNY.ny);
		
		// 클릭 이벤트 핸들러 내에서 함수 호출
		$("#today").click(function(){
			weatherDay("today", NXNY.nx, NXNY.ny);
		});
		$("#tomorrow").click(function(){
			weatherDay("tomorrow", NXNY.nx, NXNY.ny);
		});
		$("#dayAfterTomorrow").click(function(){
			weatherDay("dayAfterTomorrow", NXNY.nx, NXNY.ny);
		});
	}

    // 위치 정보 가져오기 함수 호출
    getLocation();
    // 유튜브 함수 호출
    fetchYouTubeVideos();
});

// 격자 좌표 구하기
// LCC DFS 좌표변환을 위한 기초 자료
var RE = 6371.00877; // 지구 반경(km)
var GRID = 5.0; // 격자 간격(km)
var SLAT1 = 30.0; // 투영 위도1(degree)
var SLAT2 = 60.0; // 투영 위도2(degree)
var OLON = 126.0; // 기준점 경도(degree)
var OLAT = 38.0; // 기준점 위도(degree)
var XO = 43; // 기준점 X좌표(GRID)
var YO = 136; // 기준점 Y좌표(GRID)

// 현재 위치 정보를 기상청 API에 쓸 격자 좌표로 변환
function convertToKmaCoordinates(latitude, longitude) {
	// LCC DFS 좌표 변환 수행
	var rs = dfs_xy_conv("toXY", latitude, longitude);
	// 반환
	return { nx: rs.x, ny: rs.y };
}

// 변환된 좌표값을 nx, ny 좌표로 변환
function dfs_xy_conv(code, v1, v2) {
	var DEGRAD = Math.PI / 180.0;
	var RADDEG = 180.0 / Math.PI;

	var re = RE / GRID;
	var slat1 = SLAT1 * DEGRAD;
	var slat2 = SLAT2 * DEGRAD;
	var olon = OLON * DEGRAD;
	var olat = OLAT * DEGRAD;

	var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
	var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
	var ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
	ro = re * sf / Math.pow(ro, sn);
	var rs = {};
	if (code == "toXY") {
		rs['lat'] = v1;
		rs['lng'] = v2;
		var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5);
		ra = re * sf / Math.pow(ra, sn);
		var theta = v2 * DEGRAD - olon;
		if (theta > Math.PI) theta -= 2.0 * Math.PI;
		if (theta < -Math.PI) theta += 2.0 * Math.PI;
		theta *= sn;
		rs['x'] = Math.floor(ra * Math.sin(theta) + XO + 0.5);
		rs['y'] = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
	}
	return rs;
}

// 유튜브 비디오 목록 가져오기
function fetchYouTubeVideos() {
    var api_key = "AIzaSyB9UyJogO_joBE7rl6iEajW5lY-fWn7WbM";
    var channel_id = "UCs1omgoHHPENxs4b-fwMpPQ";
    var max_result = 10;
    var url = `https://www.googleapis.com/youtube/v3/search?order=date&part=snippet&channelId=${channel_id}&maxResults=${max_result}&key=${api_key}`;
    var cacheKey = 'youtubeVideos';
    var cacheExpiryKey = 'youtubeVideosExpiry';
    var cacheExpiryTime = 14400000; // 4시간

    var cachedVideos = localStorage.getItem(cacheKey);
    var cacheExpiry = localStorage.getItem(cacheExpiryKey);

    // 현재 시간과 캐시 만료 시간을 비교하여 캐시가 유효한지 확인
    if (cachedVideos && cacheExpiry && new Date().getTime() < cacheExpiry) {
        console.log('Using cached data');
        displayVideos(JSON.parse(cachedVideos));
        return;
    }

    // API 요청 보내기
    fetch(url)
        .then(response => response.json())
        .then(data => {
            var videoList = data.items;

            // 데이터 캐싱
            localStorage.setItem(cacheKey, JSON.stringify(videoList));
            localStorage.setItem(cacheExpiryKey, new Date().getTime() + cacheExpiryTime);

            displayVideos(videoList);
        })
        .catch(error => console.error('Error fetching data:', error));
}

function displayVideos(videoList) {
    var output = '<ul>';

    videoList.forEach(video => {
        var videoId = video.id.videoId;
        var title = video.snippet.title;
        var thumbnail = video.snippet.thumbnails.default.url;

        output += `
            <li>
                <a href="https://www.youtube.com/watch?v=${videoId}" target="_blank">
                    <img src="${thumbnail}" alt="${title}">
                    <p>${truncateText(title, 30)}</p>
                </a>
            </li>
        `;
    });

    output += '</ul>';
    document.getElementById('video-container').innerHTML = output;
}

// 긴 제목을 일정 길이로 자르고 "..."을 추가하는 함수
function truncateText(text, maxLength) {
    return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
}
//날씨 함수
function weatherData(nx, ny) {
    var api_key = "WuMkHTh0aSvlWEtIHd7EkY%2B02m%2BOyVb6UcNDRYXc2kRCohnhAvj%2Ft11Zbjb8KuDwusQlhukBJWddx%2FsBexnBeQ%3D%3D";
    var baseDate = getBaseDate();
    var baseTime1 = getBaseTime1();
    var baseTime2 = "0500";
    
    console.log("날짜 : " + baseDate + ", 시간 : (초단기예보시간=" + baseTime1 + ", 단기예보시간=" + baseTime2 + ")");

    // 초단기 예보 fetch 요청
    fetch(`https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?serviceKey=${api_key}&pageNo=1&numOfRows=1000&dataType=json&base_date=${baseDate}&base_time=${baseTime1}&nx=${nx}&ny=${ny}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('초단기 예보 조회 api 반환 중 오류...');
            }
            return response.json();
        })
        .then(data => {
            var items = data.response.body.items.item;
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if (item.category == "T1H") {
                    document.getElementById("currentWeather").innerText = item.fcstValue + "°C";
                } else if (item.category == "SKY") {
                    var skyStatus = item.fcstValue;
                    var skyStatusText;

                    if (skyStatus == 1) {
                        skyStatusText = "맑음";
                    } else if (skyStatus == 3) {
                        skyStatusText = "구름 많음";
                    } else if (skyStatus == 4) {
                        skyStatusText = "흐림";
                    } else {
                        skyStatusText = "알 수 없음";
                    }
                    document.getElementById("skyStatus").innerText = skyStatusText;
                } else if(item.category == "REH") {
					document.getElementById("humidity").innerText = item.fcstValue + "%";
				}
            }
        })
        .catch(error => {
            console.error('초단기 예보 조회 api 반환 중 오류...', error);
        });

    // 단기 예보 fetch 요청
    fetch(`https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${api_key}&pageNo=1&numOfRows=1000&dataType=json&base_date=${baseDate}&base_time=${baseTime2}&nx=${nx}&ny=${ny}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('단기 예보 조회 api 반환 중 오류...');
            }
            return response.json();
        })
        .then(data => {
            var items = data.response.body.items.item;
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if (item.fcstDate === baseDate && item.category == "TMX") {
                    document.getElementById("maxTemperature").innerText = item.fcstValue + "°C";
                } else if (item.category == "TMN") {
                    document.getElementById("minTemperature").innerText = item.fcstValue + "°C";
                }
            }
        })
        .catch(error => {
            console.error('단기 예보 조회 api 반환 중 오류...', error);
        });
}
//기온 그래프 함수
function weatherDay(day, nx, ny) {
    var api_key = "WuMkHTh0aSvlWEtIHd7EkY%2B02m%2BOyVb6UcNDRYXc2kRCohnhAvj%2Ft11Zbjb8KuDwusQlhukBJWddx%2FsBexnBeQ%3D%3D";
    var baseDate = getBaseDate();
    var baseTime = getBaseTime2();

    var targetDate;
    if (day === "today") {
        targetDate = baseDate;
    } else if (day === "tomorrow") {
        targetDate = getTomorrowDate();
    } else if (day === "dayAfterTomorrow") {
        targetDate = getDayAfterTomorrowDate();
    }

    // fetch API를 사용한 요청
    fetch(`https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${api_key}&pageNo=1&numOfRows=1000&dataType=json&base_date=${baseDate}&base_time=${baseTime}&nx=${nx}&ny=${ny}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('날씨 데이터 가져오기 실패...');
            }
            return response.json();
        })
        .then(data => {
            var items = data.response.body.items.item;
            var temperatureData = [];
            var timeLabels = [];

            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if (item.fcstDate === targetDate && item.category === "TMP") {
                    temperatureData.push(item.fcstValue);
                    timeLabels.push(item.fcstTime.slice(0, 2) + "시");
                }
            }
            drawTemperatureGraph(temperatureData, timeLabels);
        })
        .catch(error => {
            console.error('날씨 데이터 가져오기 실패:', error);
        });
}
//기온 그래프 그리기
function drawTemperatureGraph(temperature, w_time) {
	var ctx = document.getElementById('temperatureChartCanvas').getContext('2d');

	var existingChart = Chart.getChart(ctx);
	if (existingChart) {
		existingChart.destroy();
	}

	var chartData = {
		labels: w_time,
		datasets: [{
			label: '기온(°C)',
			data: temperature,
			borderColor: 'rgba(255, 99, 132, 1)',
			borderWidth: 1,
			fill: false
		}]
	};

	var options = {
		responsive: true,
		scales: {
			x: {
				title: {
					display: true,
					text: '시간'
				}
			},
			y: {
				display: false // y 축 표시 비활성화
			}
		},
		plugins: {
			tooltip: {
				enabled: true,
				padding: 20, // 패딩 조절
				backgroundColor: 'rgba(0, 0, 0, 0.8)', // 배경색 설정
				titleColor: '#fff', // 제목 색상 설정
				bodyColor: '#fff', // 내용 색상 설정
			},
			scrollbar: {
				axis: 'x',
				limit: 6
			}
		}
	};

	new Chart(ctx, {
		type: 'line',
		data: chartData,
		options: options
	});
}


//한국 시간(KST)으로 현재 날짜를 얻는 함수
function getKSTDate(date) {
    // UTC 시간 기준으로 변환
    var utcDate = date.getTime() + (date.getTimezoneOffset() * 60000);
    // UTC+9 시간으로 변환
    var koreaTimeOffset = 9 * 60 * 60 * 1000;
    var koreaDate = new Date(utcDate + koreaTimeOffset);
    return koreaDate;
}
//기본 날짜 (한국 시간 기준)를 얻는 함수
function getBaseDate() {
    var date = new Date();
    var koreaDate = getKSTDate(date);

    var year = koreaDate.getFullYear();
    var month = ('0' + (koreaDate.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 +1
    var day = ('0' + koreaDate.getDate()).slice(-2);

    return year + month + day;
}
//내일 날짜 (한국 시간 기준)를 얻는 함수
function getTomorrowDate() {
    var date = getKSTDate(new Date());
    date.setDate(date.getDate() + 1);
    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    var day = ('0' + date.getDate()).slice(-2);
    return year + month + day;
}

// 모레 날짜 (한국 시간 기준)를 얻는 함수
function getDayAfterTomorrowDate() {
    var date = getKSTDate(new Date());
    date.setDate(date.getDate() + 2);
    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    var day = ('0' + date.getDate()).slice(-2);
    return year + month + day;
}
// 초단기 예보 시간
function getBaseTime1() {
    var date = new Date();
    var koreaDate = getKSTDate(date);

    var hours = koreaDate.getHours();
    var minutes = koreaDate.getMinutes();

    if (minutes < 30) {
        // 30분 이전이면 1시간을 감소시키고 30분으로 설정
        hours -= 1;
        minutes = 30;
    } else {
        // 30분 이후면 현재 시간의 30분으로 설정
        minutes = 30;
    }

    // 시간 보정: 자정 이전 시간 처리 (예: 00:20 -> 23:30)
    if (hours < 0) {
        hours = 23;
    }

    // HHMM 형식으로 맞추기
    var baseTime = ('0' + hours).slice(-2) + ('0' + minutes).slice(-2);

    return baseTime;
}
// 단기 예보 시간
function getBaseTime2() {
    var date = new Date();
    var koreaDate = getKSTDate(date);

    var hours = koreaDate.getHours();

    // 3시간 간격의 시간대 배열
    var timeSlots = [2, 5, 8, 11, 14, 17, 20, 23];
    var baseTime;

    // 현재 시간과 가장 가까운 이전 시간대 찾기
    for (var i = timeSlots.length - 1; i >= 0; i--) {
        if (hours >= timeSlots[i]) {
            baseTime = ('0' + timeSlots[i]).slice(-2) + '00';
            break;
        }
    }

    // 만약 현재 시간이 0시 ~ 1시 사이라면 2300을 반환
    if (baseTime === undefined) {
        baseTime = '2300';
    }

    return baseTime;
}