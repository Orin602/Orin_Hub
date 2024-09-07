/**
 * 
 */
 
 //접기
$(document).ready(function() {
    $('#toggleButton').off('click').on('click', function(event) {
        event.preventDefault(); // 기본 동작 방지
        $('#main_head').toggleClass('hidden');
    });
});

//메뉴바
$(document).ready(function() {
    $('.menu-link').on('click', function(event) {
        event.preventDefault();
        var url = $(this).data('url');
        $('#content').load(url);
    });
});


        
 function go_view(community_seq) {
		var theForm = document.createElement('form');
		theForm.method = "get";
		theForm.action = "/community_detail";

		var hiddenSeq = document.createElement('input');
		hiddenSeq.type = "hidden";
		hiddenSeq.name = "community_seq";
		hiddenSeq.value = community_seq;

		theForm.appendChild(hiddenSeq);
		document.body.appendChild(theForm);
		theForm.submit();
	}

    
/**
 * 
 */
 //Detail
	function go_list() {
		var theForm = document.frm;
		theForm.method = "get";
		theForm.action = "communityboard_list";
		theForm.submit();
	}

	function board_update() {
		var theForm = document.frm;
		theForm.method = "get";
		theForm.action = "communityboard_update";
		theForm.submit();
	}

	function board_delete() {
		var theForm = document.frm;
		theForm.method = "get";
		theForm.action = "communityboard_delete";
		theForm.submit();

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

	//추천
	function goodpoint_plus(button) {
		var theForm = document.frm;
		theForm.method = "post";
		theForm.action = "community_goodpoint";
		theForm.submit();
	}



//Boardupdate
function go_mov() {
		var theForm = document.frm
		theForm.method = "get";
		theForm.action = "community_list";
		theForm.submit();
	}

	function go_mod_save() {
		var theForm = document.getElementById('update_form');
		if (!theForm.community_seq.value) {
			alert('문서 번호가 없습니다. 다시 시도해 주세요.');
			return false; // 폼 제출 방지
		}
		theForm.method = "post";
		theForm.action = "/communityboard_update_t";
		theForm.submit();
	}
	
	
		
function go_list()
{
	   var theForm = document.frm;
	   theForm.method = "get";
	   theForm.action="community_list";
	   theForm.submit();
}		

function openInPopup(url) {
        window.open(url, '_blank', 'width=800,height=600,scrollbars=yes,resizable=yes');
    }


function go_save() {	
	 event.preventDefault();
	 
	if ($("#title").val() == "") {
		alert("제목을 입력하세요.");
		$("#title").focus();
		return false;
	} else if ($("#content").val() == "") {
		alert("내용을 입력하세요.");
		$("#content").focus();
		return false;
	} else{
		var theform = $("#write_form");
		theform.attr("method", "post");
		theform.attr("action", "/communityboard_write_t");
		theform.submit();
	}
		}
      
   //검색
    	function submitSearch() {	
	if ($("#searchKeyword").val() == "") {
		alert("검색어를 입력하세요");
		$("#searchKeyword").focus();
		return false;
	}  else{
		var theform = $("#searchForm");
		theform.attr("method", "get");
		theform.attr("action", "/communityboard_search");
		theform.submit();
	}
		}      