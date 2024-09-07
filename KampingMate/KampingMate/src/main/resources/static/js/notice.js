/**
 * 
 */

     function submitSearch() {
    var keyword = $("#searchKeyword").val();

    if (keyword === "") {
        alert("검색어를 입력하세요");
        $("#searchKeyword").focus();
        return false;
    } else {
        $("#searchForm").attr("action", "/Notice_search");
        $("#searchForm").submit();
    }
}

	
	//상세페이지로 이동		
	 function go_view(notice_seq) {
		var theForm = document.createElement('form');
		theForm.method = "get";
		theForm.action = "/Notice_detail";

		var hiddenSeq = document.createElement('input');
		hiddenSeq.type = "hidden";
		hiddenSeq.name = "notice_seq";
		hiddenSeq.value = notice_seq;

		theForm.appendChild(hiddenSeq);
		document.body.appendChild(theForm);
		theForm.submit();
	}	
	
	//페이징
	
window.onload = function() {
    var urlParams = new URLSearchParams(window.location.search);
    var category = urlParams.get('category');
    if (category) {
        document.getElementById('category').value = category;
    }
    renderPagination();
};

function renderPagination() {
    var paginationContainer = document.getElementById('pagination');
    if (!paginationContainer) {
        console.error('Pagination container not found');
        return;
    }

    var totalPages = parseInt(paginationContainer.getAttribute('data-total-pages'));
    var pageNumber = parseInt(paginationContainer.getAttribute('data-page-number'));

    if (isNaN(totalPages) || isNaN(pageNumber)) {
        console.error('Invalid totalPages or pageNumber');
        return;
    }

    paginationContainer.innerHTML = '';

    if (pageNumber > 1) {
        var prevPage = document.createElement('li');
        prevPage.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + (pageNumber - 1) + ')">&laquo; 이전</a>';
        paginationContainer.appendChild(prevPage);
    }

    for (var i = 1; i <= totalPages; i++) {
        var pageItem = document.createElement('li');
        pageItem.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + i + ')" class="' + (i == pageNumber ? 'active' : '') + '">' + i + '</a>';
        paginationContainer.appendChild(pageItem);
    }

    if (pageNumber < totalPages) {
        var nextPage = document.createElement('li');
        nextPage.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + (pageNumber + 1) + ')">다음 &raquo;</a>';
        paginationContainer.appendChild(nextPage);
    }
}

function keyClick(event) {
    var category = event.target.getAttribute('data-category');
    document.getElementById('category').value = category;
    document.getElementById('page').value = 1;

    console.log("카테고리키클릭 : " + category);

    var searchForm = document.getElementById('searchForm');
    searchForm.action = "/category";  // URL을 /category로 설정
    searchForm.submit();
}


function go_page(page) {
    var form = document.getElementById('searchForm');
    var categoryInput = document.getElementById('category');
    var category = categoryInput ? categoryInput.value : "";
    var searchType = document.getElementById('searchType').value;
    var searchKeyword = document.getElementById('searchKeyword').value;

    console.log("go_page - category input value: " + categoryInput.value);
    console.log("go_page - category: " + category);

    var params = new URLSearchParams();
    params.set('page', page);

    if (category) {
        params.set('category', category);
    }

    if (searchType) {
        params.set('searchType', searchType);
    }

    if (searchKeyword) {
        params.set('searchKeyword', searchKeyword);
    }

    var url = "/category?" + params.toString();
    if (!category) {
        url = "/notice?" + params.toString();
    }
    console.log("go_page - URL: " + url);

    window.location.href = url;
}

function go_list() {
    document.getElementById('category').value = "";  
    document.getElementById('page').value = 1;

    var searchForm = document.getElementById('searchForm');
    searchForm.action = "/notice";  // URL을 /notice로 설정
    searchForm.submit();
}





  
	
		//클립보드복사
	function copyboard() {
		var copy = document.createElement('input'), text = window.location.href;

		document.body.appendChild(copy);
		copy.value = text;
		copy.select();
		document.execCommand('copy');
		document.body.removeChild(copy);

		alert('복사되었습니다');
	}
	