


// 예약 정보 입력 필터
document.addEventListener("DOMContentLoaded", function() {
    let nowUtc = Date.now();
    let timeOff = new Date().getTimezoneOffset() * 60000;
    let today = new Date(nowUtc - timeOff).toISOString().split("T")[0];

    const bookdateSElement = document.getElementById("bookdateS");
    const bookdateEElement = document.getElementById("bookdateE");

    if (!bookdateSElement) {
        return; // 요소가 없으면 함수 종료
    }

    if (!bookdateEElement) {
        return; // 요소가 없으면 함수 종료
    }

    bookdateSElement.setAttribute("min", today);

    bookdateSElement.addEventListener("change", function() {
        const startDate = bookdateSElement.value;
        if (startDate) {
            bookdateEElement.setAttribute("min", startDate);
        }
    });

    bookdateEElement.addEventListener("change", function() {
        const startDate = bookdateSElement.value;
        const endDate = bookdateEElement.value;
        if (endDate && new Date(endDate) < new Date(startDate)) {
            alert("종료일은 시작일보다 이전일 수 없습니다");
            bookdateEElement.value = "";
        }
    });

    window.addEventListener('message', function(event) {
        if (event.data.type === 'selectedCampsite') {
            const campingnameElement = document.getElementById('campingname');
            const campingidElement = document.getElementById('campingid');
            if (campingnameElement && campingidElement) {
                campingnameElement.value = event.data.name;
                campingidElement.value = event.data.id; 
                console.log("캠핑아이디 : ", event.data);
            } else {
                console.error('Elements with id "campingname" or "campingid" not found');
            }
        }
    });
});


//시작날짜설정
function onStartDateChange() {
    const startDate = document.getElementById("bookdateS").value;
    const endDateElement = document.getElementById("bookdateE");
    if (startDate && endDateElement) {
        endDateElement.setAttribute("min", startDate);
    }
    calculateDays();
}

//종료날짜설정
function onEndDateChange() {
    const startDate = document.getElementById("bookdateS").value;
    const endDate = document.getElementById("bookdateE").value;
    if (!startDate) {
        alert("시작일자를 먼저 선택해 주세요.");
        const endDateElement = document.getElementById("bookdateE");
        if (endDateElement) {
            endDateElement.value = "";
        }
    } else {
        calculateDays();
    }
}

//날짜계산
function calculateDays() {
    const startDate = document.getElementById("bookdateS").value;
    const endDate = document.getElementById("bookdateE").value;
    if (startDate && endDate) {
        const start = new Date(startDate);
        const end = new Date(endDate);
        const diffTime = end - start;
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
        const periodElement = document.getElementById("period");
        if (periodElement) {
            periodElement.innerText = diffDays >= 0 ? diffDays : 0;
        }
    } else {
        const periodElement = document.getElementById("period");
        if (periodElement) {
            periodElement.innerText = 0;
        }
    }
}

//캠핑장찾기
function book_campingsearch() {
    window.open('/kakao-map', '카카오 지도', 'width=800,height=600');
}


//예약전송
async function gobook(event) {
    event.preventDefault(); // 이벤트 기본 동작 막기

    const bookdateS = document.getElementById('bookdateS').value;
    const bookdateE = document.getElementById('bookdateE').value;
    const campingname = document.getElementById('campingname').value;
    const campingid = document.getElementById('campingid').value;
    const headcount = document.getElementById('headcount').value;
    const phone = document.getElementById('phone').value;
    const bookname = document.getElementById('bookname').value;
    const message = document.getElementById('message').value;

    if (!bookdateS) {
        alert("캠핑시작일자를 지정해주세요");
        return false;
    } else if (!bookdateE) {
        alert("캠핑종료일자를 지정해주세요");
        return false;
    } else if (!campingname) {
        alert("캠핑장을 입력하세요.");
        document.getElementById("campingname").focus();
        return false;
    } else if (headcount === "0") {
        alert("인원수를 정해주세요.");
        document.getElementById("headcount").focus();
        return false;
    } else if (!phone) {
        alert("연락처를 입력해주세요");
        document.getElementById("phone").focus();
        return false;
    } else if (!bookname) {
        alert("예약자 성함을 입력해주세요");
        document.getElementById("bookname").focus();
        return false;
    }

    const bookData = {
        userId: document.getElementById('userId').value,
        campingname: campingname,
        campingid: campingid,
        headcount: headcount,
        phone: phone,
        bookname: bookname,
        message: message,
        bookdateS: bookdateS,
        bookdateE: bookdateE
    };

    console.log("Sending book data:", bookData); // 전송 데이터 확인

    try {
        const response = await fetch('/Bookwritesubmit', { 
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bookData)
        });

        if (response.ok) {
         	const responseData = await response.json();
            const bookseq = responseData.bookseq; // 예약 ID를 받아옴
            alert('캠핑장 예약이 성공적으로 신청되었습니다. 확정은 캠핑장 연락 후 예약확정됩니다.');

            // 동적으로 폼 생성 후 제출하여 POST 방식으로 리다이렉트
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '/Book_detail';

            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'bookseq';
            input.value = bookseq;
            form.appendChild(input);

            document.body.appendChild(form);
            form.submit();
        } else {
            const errorData = await response.json();
            alert(`캠핑장 예약이 실패하였습니다: ${errorData.message || 'Unknown error'}`);
            console.error('Error response:', response, errorData);
        }
    } catch (error) {
        alert('캠핑장 예약이 실패하였습니다');
        console.error('Fetch error:', error);
    }
}




/** BookList */
//예약 상세내용 보러가기
function go_view(bookseq) {
    var theForm = document.getElementById("frm");
    

    $('input[name="bookseq"]').remove();  
    $('<input>').attr({
        type: 'hidden',
        name: 'bookseq',
        value: bookseq
    }).appendTo(theForm);

    theForm.method = "post";
    theForm.action = "/Book_detail";
    theForm.submit();
}


function submitSearch() {	
	if ($("#searchKeyword").val() == "") {
		alert("검색어를 입력하세요");
		$("#searchKeyword").focus();
		return false;
	}  else{
		var theform = $("#searchForm");
		theform.attr("method", "get");
		theform.attr("action", "/Book_search");
		theform.submit();
	}
		}

//내 예약리스트 보러가기
function go_page(page) {
    var theForm = document.frm;
    $('<input>').attr({
        type: 'hidden',
        name: 'page',
        value: page
    }).appendTo(theForm);
    theForm.method = "post";
    theForm.action = "/Book_List";
    theForm.submit();
}


/** Bookupdate */

$(document).ready(function() {
    $('#toggleButton').click(function() {
        $('#dateFields').toggle();
    });
});

//예약삭제
function bookdelete() {
	var theForm = document.frm;
	var confirmCheck = confirm("정말로 취소하시겠습니까?")
	var condition = $("#condition").data("condition");
	
	if(confirmCheck){
	if(condition == 0){
		 theForm.method = "post";
         theForm.action = "/Book_delete";
         theForm.submit();
	} else {
		alert("대기중인 예약만 취소 가능합니다. 확정된 예약은 캠핑장에 직접 문의해주세요");
	}
}
}

//예약수정
async function bookupdatesub(event) {
    event.preventDefault(); // 이벤트 기본 동작 막기

    const bookdateS = document.getElementById('bookdateS').value;
    const bookdateE = document.getElementById('bookdateE').value;

    if (!bookdateS) {
        alert("캠핑시작일자를 지정해주세요");
        return false;
    } else if (!bookdateE) {
        alert("캠핑종료일자를 지정해주세요");
        return false;
    } else if (new Date(bookdateE) < new Date(bookdateS)) {
        alert("종료일은 시작일보다 이전일 수 없습니다");
        return false;
    } else if ($("#headcount").val() == "0") {
        alert("인원수를 정해주세요.");
        $("#headcount").focus();
        return false;
    } else if ($("#telephone").val() == "") {
        alert("연락처를 입력해주세요");
        $("#telephone").focus();
        return false;
    } else if ($("#bookname").val() == "") {
        alert("예약자 성함을 입력해주세요");
        $("#bookname").focus();
        return false;
    }

    const bookseq = document.getElementById('bookseq').value;
    const headcount = document.getElementById('headcount').value;
    const telephone = document.getElementById('telephone').value;
    const bookname = document.getElementById('bookname').value;
    const message = document.getElementById('message').value;

    const bookData = {
        bookseq: parseInt(bookseq, 10),
        headcount: headcount,
        phone: telephone, // 여기서 key는 "telephone" 이어야 합니다.
        bookname: bookname,
        message: message,
        bookdateS: bookdateS,
        bookdateE: bookdateE
    };

    const response = await fetch('/Bookupdatesubmit', { 
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bookData)
    });

    if (response.ok) {
        const responseData = await response.json();
        const bookseq = responseData.bookseq;
        alert('캠핑장 예약이 성공적으로 수정되었습니다.');
        var existingForm = document.getElementById('redirectForm');
        if (existingForm) {
            existingForm.remove();
        }

        // 새로운 폼 생성
        var form = document.createElement("form");
        form.id = "redirectForm";
        form.method = "post";
        form.action = responseData.redirectUrl;
        document.body.appendChild(form);
        form.submit();
    } else {
        alert('캠핑장 예약 수정이 실패하였습니다');
        console.error('Error response:', response);
        const errorData = await response.json();
        console.error('Error data:', errorData);
    }
    }



/** BookResult */
//예약 수정페이지로
function bookupdate(bookseq, condition) {
    if (condition != 0) {
        alert("현재는 예약을 변경할 수 없습니다");
    } else {
       var theForm = document.createElement('form');
        theForm.method = "get";
        theForm.action = "/Book_update";
        
        var input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'bookseq';
        input.value = bookseq;
        
        theForm.appendChild(input);
        document.body.appendChild(theForm);
        theForm.submit();
    }
}




//내 예약리스트 보기
function goBookList() {
    var theForm = document.forms['frm'];
    if (theForm) {
        theForm.method = "post";
        theForm.action = "Book_List";
        theForm.submit();
    } else {
        console.error('Form 을 찾을수없습니다');
    }
}