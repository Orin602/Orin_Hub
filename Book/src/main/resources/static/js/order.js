function order_book() {
	if($("#query").val() == "") {
		swal.fire({
			title : '검색어를 입력하세요.',
			text : '구매할 도서를 입력해주세요.',
			icon : 'warning'
		});
		$("#query").focus();
		return false;
	} else {
		let searchTarget = $("#searchTarget").val();
		$("#search-form").attr("action", `/order-search?query=${$("#query").val()}&searchTarget=${searchTarget}`).submit();
	}
}

function goToOrder(button) {
	const title = button.getAttribute('data-title');
    const author = button.getAttribute('data-author');
    const cover = button.getAttribute('data-cover');
    const pricesales = button.getAttribute('data-pricesales');
    const pricestandard = button.getAttribute('data-pricestandard');
	
	swal.fire({
		icon : 'info',
		title : '도서 구매',
		text : '구매 페이지로 이동하시겠습니까?',
		showCancelButton: true,
		confirmedButtonText : '확인',
		cancelButtonText: '취소'
	}).then((result) => {
		if(result.isConfirmed) {
			const url = `/order-write-form2?title=${encodeURIComponent(title)}
				&author=${encodeURIComponent(author)}&cover=${encodeURIComponent(cover)}
				&priceSales=${pricesales}&priceStandard=${pricestandard}`;
			window.location.href = url;
		} else {
			swal.fire('취소되었습니다!', '', 'info');
		}
	});
}

function selectorder() {
	// 책 정보
	var title = document.getElementById("hidden-title").value;
    var author = document.getElementById("hidden-author").value;
    var imageUrl = document.getElementById("hidden-imageUrl").value;
	
	var totalPriceText = document.getElementById("totalPrice").textContent;
    var totalPrice = totalPriceText.replace(" 원", "").trim();  // '원' 제거하고 숫자만 추출
    totalPrice = parseInt(totalPrice);  // 숫자로 변환
	
	// 배송지 정보
    var useDefaultAddress = document.getElementById("useDefaultAddress").checked;
    var address = "";
	
	if (useDefaultAddress) {
		// 기본 배송지 사용 시
		address = document.getElementById("defaultAddress").value;
	} else {
		// 새 배송지 입력 시
		address = document.getElementById("address").value + " " + document.getElementById("newAddressDetail").value;
	}
	var ea = document.getElementById("ea").value;
	
	swal.fire({
        title: '주문을 확인하시겠습니까?',
        text: '주문 정보를 확인하고, 구매를 완료합니다.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: '확인',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            // 폼 데이터를 생성하여 전송
			document.getElementById("hidden-title").value = title;
            document.getElementById("hidden-author").value = author;
            document.getElementById("hidden-imageUrl").value = imageUrl;
            document.getElementById("hidden-totalPrice").value = totalPrice;
            document.getElementById("hidden-address").value = address;
            document.getElementById("hidden-ea").value = ea;

            // 폼을 전송
            var form = document.getElementById("order-write-form");
            form.action = "/order/submit";  // 폼 액션을 설정
            form.method = "POST";
            form.submit();  // 폼 전송
        }
    });
}
