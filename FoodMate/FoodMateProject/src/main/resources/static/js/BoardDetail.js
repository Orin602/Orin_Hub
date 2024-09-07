/**
 * 
 */
 //BoardDetail
	function go_list() {
		var theForm = document.frm;
		theForm.method = "get";
		theForm.action = "board_list";
		theForm.submit();
	}

	function board_update() {
		var theForm = document.frm;
		theForm.method = "get";
		theForm.action = "board_update";
		theForm.submit();
	}

	function board_delete() {
		var theForm = document.frm;
		var confirmCheck = confirm("정말로 삭제하시겠습니까?")
		
		if(confirmCheck){
		theForm.method = "get";
		theForm.action = "board_delete";
		theForm.submit();
		} else {
			
		}

	}

	//댓글등록	
	function reply_save() {
		if ($("#reply_content").val() == "") {
		alert("댓글 내용을 입력하세요.");
		$("#reply_content").focus();
		return false;
	}{
		var theForm = document.replyfrm;
		theForm.method = "post";
		theForm.action = "reply_save";
		theForm.submit();
		}
	}

//댓글출력
function reply_list() {
	var seq = $('#seq').val();

	$.ajax({
		url : '/reply_list',
		type : 'post',
		data : {
			seq : seq
		},
		success : function(response) {
			var updatedContent = $(response).find('#reply_list').html(); // 응답에서 댓글 목록 부분만 추출
			$('#reply_list').html(updatedContent); // 페이지의 댓글 목록 부분만 업데이트
		},
		error : function(error) {
			console.error("댓글 로드 중 오류 발생: ", error);
		}
	});
}

	// 페이지 로드 완료 후 댓글 목록 로드
	$(document).ready(function() {
		 if ($('#replyjs').length) {
        reply_list();
    }
	});

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
		theForm.action = "goodpoint";
		theForm.submit();
		
		 if (element.classList.contains('red')) {
            element.classList.remove('red');
        } else {
            element.classList.add('red');
        }
	}

	

function deleteReply(replynum, event) {
    event.preventDefault(); // 기본 이벤트 방지

    if (confirm('이 댓글을 삭제하시겠습니까?')) {
        fetch(`/reply_delete?replynum=${replynum}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ replynum: replynum })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`네트워크 응답이 올바르지 않습니다. 상태 코드: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                alert('댓글이 삭제되었습니다.');
                reply_list(); // 댓글 목록을 새로 고침
            } else {
                throw new Error(data.message); // 서버에서 처리 실패 시
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert(`댓글 삭제 중 오류가 발생하였습니다: ${error.message}`);
        });
    }
}


// 댓글 수정 모드 전환
function toggleEditMode(button, event) {
    event.preventDefault();

    const replynum = button.getAttribute('data-replynum');
    const replyElement = button.closest('tr');
    if (!replyElement) {
        console.error('Error: Unable to find the reply element.');
        return;
    }
    const contentView = replyElement.querySelector(`span[data-content-view='${replynum}']`);
    const contentEdit = replyElement.querySelector(`textarea[data-content-edit='${replynum}']`);
    const saveButton = replyElement.querySelector(`button[data-save-button='${replynum}']`);

    if (!contentView || !contentEdit || !saveButton) {
        console.error('Error: Unable to find the necessary HTML elements.');
        return;
    }

    if (contentView.style.display === 'none') {
        contentView.style.display = 'block';
        contentEdit.style.display = 'none';
        button.style.display = 'inline'; // 수정 버튼 표시
        saveButton.style.display = 'none'; // 저장 버튼 숨김
    } else {
        contentView.style.display = 'none';
        contentEdit.style.display = 'block';
        button.style.display = 'none'; // 수정 버튼 숨김
        saveButton.style.display = 'inline'; // 저장 버튼 표시
    }
}

// 댓글 내용 저장
function saveChanges(button, event) {
    event.preventDefault(); // 기본 이벤트 방지
    
    // 댓글 번호
    const replynum = button.getAttribute('data-replynum');
    const replyElement = button.closest('tr');
    if (!replyElement) {
        console.error('Error: Unable to find the reply element.');
        return;
    }
    const contentEdit = replyElement.querySelector(`textarea[data-content-edit='${replynum}']`);
    if (!contentEdit) {
        console.error('Error: Unable to find the edit textarea.');
        return;
    }
    const content = contentEdit.value.trim();
    if (content === "") {
        alert("댓글 내용을 입력하세요.");
        contentEdit.focus();
        return false;
    }

    // AJAX 댓글 내용을 업데이트
    fetch('/reply_update', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ replynum: replynum, content: content })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('댓글이 수정되었습니다.');
            replyElement.querySelector(`span[data-content-view='${replynum}']`).textContent = content;

            // 저장 후
            const editButton = replyElement.querySelector(`button[data-replynum='${replynum}']:not([data-save-button])`);
            editButton.style.display = 'inline'; // 수정 버튼 표시
            button.style.display = 'none'; // 저장 버튼 숨김

            const contentView = replyElement.querySelector(`span[data-content-view='${replynum}']`);
            contentView.style.display = 'block'; // 내용 보기 표시
            contentEdit.style.display = 'none'; // 내용 편집 숨김
        } else {
            throw new Error(data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('댓글 수정 중 오류가 발생했습니다: ' + error.message);
    });
}


//Boardupdate
function go_mov() {
		var theForm = document.frm
		theForm.method = "get";
		theForm.action = "board_list";
		theForm.submit();
	}

	function go_mod_save() {
		var theForm = document.getElementById('update_form');
		if (!theForm.seq.value) {
			alert('문서 번호가 없습니다. 다시 시도해 주세요.');
			return false; // 폼 제출 방지
		}
		theForm.method = "post";
		theForm.action = "/board_update_t";
		theForm.submit();
	}
	
	//BoardWrite
	
	function go_save() {	
	if ($("#title").val() == "") {
		alert("요리명을 입력하세요.");
		$("#title").focus();
		return false;
	} else if ($("#gredient").val() == "") {
		alert("재료를 입력하세요.");
		$("#gredient").focus();
		return false;
	} else if ($("#manual01").val() == "") {
		alert("조리법을 입력하세요.");
		$("#manual01").focus();
		return false;
	} else{
		var theform = $("#write_form");
		theform.attr("method", "post");
		theform.attr("enctype", "multipart/form-data");
		theform.attr("action", "/board_write_t");
		theform.submit();
	}
		}


function openInPopup(url) {
        window.open(url, '_blank', 'width=800,height=600,scrollbars=yes,resizable=yes');
    }
    
