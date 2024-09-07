/**
 * 
 */

 // reviewWrite
 
 
function gocamping_search() {
    window.open('/gocampreview', '고캠핑 검색', 'width=800,height=600');
}

window.addEventListener('message', function(event) {
    if (event.data.type === 'selectedCampsite') {
        document.getElementById('kakao_name').value = event.data.name;
        document.getElementById('kakao_id').value = event.data.id;
    }
});


    let selectedRating = 0;

       $(document).ready(function() { // jQuery를 사용한 별점 클릭 이벤트 등록
    $('.star_rating .star').click(function() {
        $(this).siblings().removeClass('on');
        $(this).addClass('on').prevAll().addClass('on');
        selectedRating = $(this).data('value');
    });
});

        async function wsubmitReview() {
            if ($("#title").val() == "") {
                alert("제목을 입력하세요.");
                $("#title").focus();
                return false;
            } else if ($("#kakao_name").val() == "") {
                alert("캠핑장을 입력하세요.");
                $("#kakao_name").focus();
                return false;
            } else if (editor.getMarkdown().trim() == "") {
                alert("내용을 입력하세요.");
                $("#content").focus();
                return false;
            }
            const title = document.getElementById('title').value;
            const kakaoName = document.getElementById('kakao_name').value;
            const kakaoId = document.getElementById('kakao_id').value;
            const content = editor.getHTML();
            
            const reviewData = {
                title: title,
                kakao_name: kakaoName,
                kakao_id: kakaoId,
                content: content,
                reviewrate: selectedRating
            };


            const response = await fetch('/reviewwritesubmit', { 
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(reviewData)
            });
            if (response.ok) {
                alert('리뷰가 성공적으로 저장되었습니다.');
                window.location.href = '/review_list';
            } else {
                alert('리뷰 저장에 실패했습니다.');
            }
        }
 
    function review_campingsearch() {
        window.open('/kakao-map', '카카오 지도', 'width=800,height=600');
    }

    window.addEventListener('message', function(event) {
        if (event.data.type === 'selectedCampsite') {
            document.getElementById('kakao_name').value = event.data.name;
            document.getElementById('kakao_id').value = event.data.id; 
            console.log("캠핑아이디 : ", event.data.id);
            console.log("캠핑이름 : ", event.data.name);
        }
    });
    
    
    //reviewUpdate
    
    
    async function usubmitReview() {
    	const review_seq = document.getElementById('review_seq').value;
        const title = document.getElementById('title').value;
        const kakaoName = document.getElementById('kakao_name').value;
        const kakaoId = document.getElementById('kakao_id').value;
        const content = editor.getHTML();
        const reviewData = {
            title: title,
            kakao_name: kakaoName,
            kakao_id: kakaoId,
            content: content,
            review_seq: review_seq,
            reviewrate: selectedRating
        };
        const response = await fetch('/reviewupdatesubmit', { // URL 수정
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reviewData)
        });
        if (response.ok) {
            alert('리뷰가 성공적으로 수정되었습니다.');
            window.location.href = '/review_detail?review_seq=' + review_seq;
        } else {
            alert('리뷰 수정에 실패했습니다.');
        }
    }
 