var map; 
var trafficOn = false; // 교통상황 표시
var overlayOn = false; // 로드뷰 오버레이 상태
var roadviewWindow = null; // 로드뷰 창
var userMarker = null; // 사용자 위치를 표시
var ps; // 장소 검색
var infowindow; // 인포윈도우
var markers = []; // 마커
var categoryMarkers = {}; // 카테고리별 마커
var currCategory = ''; // 현재 선택된 카테고리

/**
 * 지도 초기화
 */
var container = document.getElementById('container');
var mapWrapper = document.getElementById('mapWrapper');
var mapContainer = document.getElementById('map');

var mapCenter = new kakao.maps.LatLng(37.56381, 126.97836); // 지도의 중심좌표
var mapOption = {
    center: mapCenter, // 지도의 중심좌표
    level: 3, // 지도 확대 레벨
    mapTypeId: kakao.maps.MapTypeId.ROADMAP 
};

// 지도 생성
map = new kakao.maps.Map(mapContainer, mapOption); 

// 장소 검색 객체를 생성
ps = new kakao.maps.services.Places(); 

// 장소명을 표출할 인포윈도우를 생성
infowindow = new kakao.maps.InfoWindow({zIndex:1});

// 로드뷰 객체를 생성
var rv = new kakao.maps.Roadview(document.createElement('div')); 
var rvClient = new kakao.maps.RoadviewClient(); 

// 로드뷰 위치가 바뀔 때 발생하는 이벤트 등록
kakao.maps.event.addListener(rv, 'position_changed', function() {
    var rvPosition = rv.getPosition();
    map.setCenter(rvPosition);

    if (overlayOn) {
        marker.setPosition(rvPosition);
    }

    // 새 창에 로드뷰 위치를 업데이트
    if (roadviewWindow && !roadviewWindow.closed) {
        roadviewWindow.postMessage({
            type: 'updatePosition',
            position: {
                lat: rvPosition.getLat(),
                lng: rvPosition.getLng()
            }
        }, '*');
    }
});

// 마커 이미지를 생성
var markImage = new kakao.maps.MarkerImage(
    'https://t1.daumcdn.net/localimg/localimages/07/2018/pc/roadview_minimap_wk_2018.png',
    new kakao.maps.Size(26, 46),
    {
        spriteSize: new kakao.maps.Size(1666, 168),
        spriteOrigin: new kakao.maps.Point(705, 114),
        offset: new kakao.maps.Point(13, 46)
    }
);

// 드래그 가능한 마커를 생성(로드뷰용)
var marker = new kakao.maps.Marker({
    image: markImage,
    position: mapCenter,
    draggable: true
});

// 마커에 드래그이벤트 추가(로드뷰용)
kakao.maps.event.addListener(marker, 'dragend', function(mouseEvent) {
    var position = marker.getPosition();
    toggleRoadview(position);
});

// 지도에 클릭 이벤트를 등록
kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
    if (!overlayOn) {
        return;
    }

    var position = mouseEvent.latLng;
    marker.setPosition(position);
    toggleRoadview(position);
});

// 전달받은 좌표(position)에 가까운 로드뷰의 파노라마 ID를 추출하여 로드뷰를 설정
function toggleRoadview(position) {
    rvClient.getNearestPanoId(position, 50, function(panoId) {
        if (panoId === null) {
            toggleMapWrapper(true, position);
        } else {
            toggleMapWrapper(false, position);
            rv.setPanoId(panoId, position);
            openRoadviewWindow(panoId, position); // 로드뷰 새 창 열기

            // 로드뷰 창이 열려 있을 때 위치 업데이트 메시지 전송
            if (roadviewWindow && !roadviewWindow.closed) {
                roadviewWindow.postMessage({
                    type: 'updatePosition',
                    panoId: panoId,
                    position: {
                        lat: position.getLat(),
                        lng: position.getLng()
                    }
                }, '*');
            }
        }
    });
}

// 지도를 감싸고 있는 div의 크기를 조정
function toggleMapWrapper(active, position) {
    if (active) {
        container.className = '';
        map.relayout();
        map.setCenter(position);
    } else {
        if (container.className.indexOf('view_roadview') === -1) {
            container.className = 'view_roadview';
            map.relayout();
            map.setCenter(position);
        }
    }
}

// 지도 위의 로드뷰 도로 오버레이를 추가, 제거
function toggleOverlay(active) {
    if (active) {
        overlayOn = true;
        map.addOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);
        marker.setMap(map);
        marker.setPosition(map.getCenter());
        toggleRoadview(map.getCenter());
    } else {
        overlayOn = false;
        map.removeOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);
        marker.setMap(null);
    }
}

// 지도 위의 로드뷰 버튼을 눌렀을 때
function setRoadviewRoad() {
    var control = document.getElementById('roadviewControl');
    if (control.className.indexOf('active') === -1) {
        control.className = 'active';
        toggleOverlay(true);
    } else {
        control.className = '';
        toggleOverlay(false);
        if (roadviewWindow && !roadviewWindow.closed) {
            roadviewWindow.close();
        }
    }
}


// 실시간 교통상황
function traffic_check() {
    if (trafficOn) {
        map.removeOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC); // 교통 상황 레이어 제거
        trafficOn = false;
    } else {
        map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC); // 교통 상황 레이어 추가
        trafficOn = true;
    }
}

// 내 위치기준 검색
function mylocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var lat = position.coords.latitude,
                lon = position.coords.longitude;
            
            var locPosition = new kakao.maps.LatLng(lat, lon);
            
            map.setCenter(locPosition);
            map.setLevel(2);

            if (userMarker === null) {
                userMarker = new kakao.maps.Marker({
                    position: locPosition,
                    map: map
                });
            } else {
                userMarker.setPosition(locPosition);
            }
        });
    } else {
        alert('GPS를 지원하지 않습니다');
    }
}

// 로드뷰 새 창 열기
function openRoadviewWindow(panoId, position) {
    if (roadviewWindow && !roadviewWindow.closed) {
        roadviewWindow.focus();
        roadviewWindow.postMessage({
            type: 'updatePosition',
            panoId: panoId,
            position: {
                lat: position.getLat(),
                lng: position.getLng()
            }
        }, '*');
    } else {
        // URL과 위치 정보 확인
        var url = 'contain/roadview.html?panoId=' + panoId + '&lat=' + position.getLat() + '&lng=' + position.getLng();
        console.log('Opening URL: ' + url);

        // 새 창 열기
        roadviewWindow = window.open(url, '로드뷰', 'width=750,height=500');
        
        // 새 창이 정상적으로 열렸는지 확인
        if (roadviewWindow) {
            roadviewWindow.onbeforeunload = function() {
                window.opener.postMessage({
                    type: 'closeRoadview'
                }, '*');
            };
        } else {
            console.error('Failed to open the new window. Please check popup blocker settings.');
        }
    }
}

// 메시지 이벤트를 통해 로드뷰 위치를 업데이트
window.addEventListener('message', function(event) {
    if (event.data.type === 'updatePosition') {
        var position = new kakao.maps.LatLng(event.data.position.lat, event.data.position.lng);
        map.setCenter(position);
        marker.setPosition(position);

        // 로드뷰 위치도 업데이트
        rvClient.getNearestPanoId(position, 50, function(panoId) {
            if (panoId !== null) {
                rv.setPanoId(panoId, position);
            }
        });
    } else if (event.data.type === 'closeRoadview') {
        toggleOverlay(false);
        var control = document.getElementById('roadviewControl');
        control.className = '';
    }
});

// 카테고리 토글
function toggleCategory(category) {
    if (categoryMarkers[category] && categoryMarkers[category].length > 0) {
        removeCategoryMarkers(category);
    } else {
        searchCategory(category);
    }
}

// 카테고리별 장소 검색
function searchCategory(category) {
    ps.categorySearch(category, function(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
            displayCategoryPlaces(category, data);
        }
    }, {useMapBounds:true});
}

// 카테고리별 장소를 지도에 표시
function displayCategoryPlaces(category, places) {
    var order = document.getElementById(category).getAttribute('data-order');
    categoryMarkers[category] = [];

    for (var i = 0; i < places.length; i++) {
        var marker = addCategoryMarker(new kakao.maps.LatLng(places[i].y, places[i].x), order);
        categoryMarkers[category].push(marker);
    }
}

// 카테고리 마커를 생성하고 지도 위에 마커 표시
function addCategoryMarker(position, order) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png',
        imageSize = new kakao.maps.Size(27, 28),
        imgOptions =  {
            spriteSize: new kakao.maps.Size(72, 208),
            spriteOrigin: new kakao.maps.Point(46, (order * 36)),
            offset: new kakao.maps.Point(11, 28)
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
        marker = new kakao.maps.Marker({
            position: position,
            image: markerImage
        });

    marker.setMap(map);
    return marker;
}

// 카테고리 클릭이벤트
function addCategoryClickEvent() {
    var category = document.getElementById('category'),
        children = category.children;

    for (var i = 0; i < children.length; i++) {
        children[i].onclick = function() {
            var category = this.id;
            toggleCategory(category);
            this.classList.toggle('on');
        };
    }
}

addCategoryClickEvent();

// 카테고리 마커 제거
function removeCategoryMarkers(category) {
    if (categoryMarkers[category]) {
        for (var i = 0; i < categoryMarkers[category].length; i++) {
            categoryMarkers[category][i].setMap(null);
        }
        categoryMarkers[category] = [];
    }
}

// 장소 검색
function searchPlaces() {
    var keyword = document.getElementById('keyword').value + ' 캠핑장';

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    ps.keywordSearch(keyword, placesSearchCB); 
}

// 장소검색이 완료됐을 때 호출
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커표시
        displayPlaces(data);

        // 페이지 번호
        displayPagination(pagination);
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');
        return;
    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 결과 중 오류가 발생했습니다.');
        return;
    }
}

// 검색 결과 목록과 마커
function displayPlaces(places) {
    var listEl = document.getElementById('placesList'),
        menuEl = document.getElementById('menu_wrap'),
        fragment = document.createDocumentFragment(),
        bounds = new kakao.maps.LatLngBounds(),
        listStr = '';

    // 검색 결과 목록에 추가된 항목 제거
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 마커 제거
    removeMarker();

    for (var i = 0; i < places.length; i++) {
        // 마커를 생성하고 지도에 표시
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i),
            itemEl = getListItem(i, places[i]); // 검색 결과

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가
        bounds.extend(placePosition);

        (function (marker, itemEl) {
            itemEl.onclick = function () {
                itemClickEvent(marker, itemEl);
            };
        })(marker, itemEl);

        fragment.appendChild(itemEl);
    }

    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
    listEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);
}

// 검색결과 항목을 반환
function getListItem(index, places) {
    var el = document.createElement('li'),
        itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
            '<div class="info">' +
            '   <h5>' + places.place_name + '</h5>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
            '   <span class="jibun gray">' + places.address_name + '</span>';
    } else {
        itemStr += '    <span>' + places.address_name + '</span>';
    }

    itemStr += '  <span class="tel">' + places.phone + '</span>' +
        '</div>';

    el.innerHTML = itemStr;
    el.className = 'item';

    // 인포윈도우에 표시할 정보를 엘리먼트에 저장
    el.dataset.id = places.id;
    el.dataset.placeName = places.place_name;
    el.dataset.placeUrl = places.place_url;
    el.dataset.phone = places.phone || '정보 없음';
    el.dataset.address = places.road_address_name || places.address_name;

    return el;
}

// 마커를 생성하고 지도 위에 마커를 표시
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', 
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
        marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage 
        });

    marker.setMap(map); // 지도 위에 마커표시
    markers.push(marker);  // 배열에 생성된 마커추가

    return marker;
}

// 지도위 마커 모두 제거
function removeMarker() {
    for (var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }   
    markers = [];
}

function itemClickEvent(marker, itemEl) {
    map.setCenter(marker.getPosition());
    map.setLevel(3);

    var content = '<form name="frm" id="frm" method="get">' +
        '<input type="hidden" name="kakao_id" value="' + itemEl.dataset.id + '"/>' +
        '<input type="hidden" name="kakao_name" value="' + itemEl.dataset.placeName + '"/>' +
        '<h5>' + itemEl.dataset.placeName + '</h5>' + 
        '<a href="' + itemEl.dataset.placeUrl + '" target="_blank">카카오맵에서 보기</a><br>' +
        '<span>전화번호: ' + itemEl.dataset.phone + '</span><br>' +
        '<span>주소: ' + itemEl.dataset.address + '</span><br>' +
        '<button type="button" onclick="go_reviewpage(\'' + itemEl.dataset.id + '\')">리뷰페이지로</button><br>' +
        '<button type="button" onclick="go_book(\'' + itemEl.dataset.id + '\', \'' + itemEl.dataset.placeName + '\')">예약하러가기</button><br>' +
        '</form>';
    
    infowindow.setContent(content);
    infowindow.open(map, marker);
}


// 예약페이지로 이동
function go_book(kakao_id, kakao_name) {
    var theForm = document.createElement('form');
    theForm.method = "get";
    theForm.action = "/book";

    var idField = document.createElement('input');
    idField.type = "hidden";
    idField.name = "campingid";
    idField.value = kakao_id;

    var nameField = document.createElement('input');
    nameField.type = "hidden";
    nameField.name = "campingname";
    nameField.value = kakao_name;

    theForm.appendChild(idField);
    theForm.appendChild(nameField);
    document.body.appendChild(theForm);
    theForm.submit();
}



// 리뷰페이지로 이동
function go_reviewpage(kakao_id) {
    var theForm = document.createElement('form');
    theForm.method = "get";
    theForm.action = "/map_review_detail";

    var hiddenField = document.createElement('input');
    hiddenField.type = "hidden";
    hiddenField.name = "kakao_id";
    hiddenField.value = kakao_id;

    theForm.appendChild(hiddenField);
    document.body.appendChild(theForm);
    theForm.submit();
}

// 검색결과 페이징처리
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i; 

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild(paginationEl.lastChild);
    }

    for (i=1; i<=pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i===pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function(i) {
                return function() {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

// 검색결과 목록의 자식 Element를 제거
function removeAllChildNods(el) {   
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}

// 지역별 정보 
function area_check() {
    $('#areaSelection').load('contain/korea_area.html', function(response, status, xhr) {
        if (status == "error") {
            alert("An error occurred while loading the page: " + xhr.status + " " + xhr.statusText);
        } else {
            $('#areaSelection').show();
        }
    });
}

var mapContainer = document.getElementById('map'); 
    var mapOption = { 
        center: new kakao.maps.LatLng(37.56381, 126.97836), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨 
    };  

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성
    
var geocoder = new kakao.maps.services.Geocoder(); 

// Listen for messages from the iframe
window.addEventListener('message', function(event) {
    if (event.data && event.data.address) {
        var address = event.data.address;
        geocoder.addressSearch(address, function(result, status) {
            if (status === kakao.maps.services.Status.OK) {
                var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                map.setCenter(coords);
            } else {
                alert("주소를 찾을 수 없습니다.");
            }
        });
    }
});
