/**
 * 
 */

 // BoardList
 
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

//글자수제한
function truncateText(selector, maxLength) {
    var elements = document.querySelectorAll(selector);
    elements.forEach(function(element) {
      var text = element.textContent;
      if (text.length > maxLength) {
        element.textContent = text.substring(0, maxLength) + '...';
      }
    });
  }

  document.addEventListener("DOMContentLoaded", function() {
    truncateText('.truncate', 9); // 9 글자로 제한
  });

        
 function go_view(seq) {
		var theForm = document.createElement('form');
		theForm.method = "get";
		theForm.action = "/com_board_detail";

		var hiddenSeq = document.createElement('input');
		hiddenSeq.type = "hidden";
		hiddenSeq.name = "seq";
		hiddenSeq.value = seq;

		theForm.appendChild(hiddenSeq);
		document.body.appendChild(theForm);
		theForm.submit();
	}

	function keyClick(event) {
		var category = event.target.getAttribute('data-category');
		var url = '/category';
		if (category === 'cnt_sort' || category === 'goodpoint_sort'
				|| category === 'date_sort') {
			url = '/sorted_board_list';
			url += '?sort=' + category;
		} else {
			url += '?category=' + encodeURIComponent(category);
		}
		window.location.href = url;
	}
	
	
	
// 그래프

	$(document).ready(function() {
    if ($('#boardlistbody').length) {
        google.charts.load('current', {packages: ['corechart'], language: 'ko'});
        google.charts.setOnLoadCallback(drawCharts);
    }
});

function drawCharts() {
    drawChart();
    drawAnthonyChart();
}

    function drawChart() {
    $.ajax({
        url: '/sorted_board_list',
        data: {
            seq: 1,
            page: 1,
            size: 10,
            sort: 'cnt_sort'
        },
        method: 'GET',
        headers: {
            Accept: 'application/json'
        },
        success: function(response) {
            var chartData = response;
            if (!Array.isArray(chartData)) {
                console.error('Unexpected response format:', response);
                return;
            }

            // 차트 데이터를 설정
            var dataArray = [['Recipe Name', 'Count', { role: 'style' }]];
            chartData.forEach(function(item) {
                dataArray.push([item.com_recipe.rcp_nm, item.cnt, '#8EC7D0']);
            });

            var data = google.visualization.arrayToDataTable(dataArray);

            // 차트 옵션
            var barchart_options = {
                title: '조회수 Top 3',
                width: 600 ,
                height: 300,
                legend: 'none',
                titleTextStyle: {
                    fontSize: 18 // 제목 글씨 크기
                },
                vAxis: {
                    minValue: 0,
                    viewWindow: {
                        min: 0
                    },
                    gridlines: {
                        count: 2 // 주 그리드라인 개수
                    },
                    minorGridlines: {
                        count: 1 // 소 그리드라인 간격
                    },
                   format: '0' // 
                }
            };


            // 차트 그리기
            var barchart = new google.visualization.ColumnChart(document.getElementById('visitChart'));
            barchart.draw(data, barchart_options);
        },
        error: function(err) {
            console.error('Error fetching chart data', err);
        }
    });
}
    
    
    
     function drawAnthonyChart() {
        $.ajax({
        url: '/sorted_board_list',
        data: {
            seq: 1,
            page: 1,
            size: 10,
            sort: 'goodpoint_sort'
        },
        method: 'GET',
        headers: {
            Accept: 'application/json'
        },
        success: function(response) {
            var chartData = response;
            if (!Array.isArray(chartData)) {
                console.error('Unexpected response format:', response);
                return;
            }

            // 차트 데이터를 설정
            var dataArray = [['Recipe Name', 'Count', { role: 'style' }]];
            chartData.forEach(function(item) {
                dataArray.push([item.com_recipe.rcp_nm, item.goodpoint, '#8EA8DB']); // 예: 색상은 임의로 지정
            });

            var data = google.visualization.arrayToDataTable(dataArray);

            // 차트 옵션
            var barchart_options = {
                title: '추천수 Top 3',
                width: 600 ,
                height: 300,
                legend: 'none',
                titleTextStyle: {
                    fontSize: 18 // 제목 글씨 크기
                },
                vAxis: {
                    minValue: 0,
                    viewWindow: {
                        min: 0
                    },
                    gridlines: {
                        count: 2 // 주 그리드라인 개수
                    },
                    minorGridlines: {
                        count: 1 // 소 그리드라인 간격
                    },
                    format: '0' 
                }
            };

            // 차트 그리기
            var barchart = new google.visualization.ColumnChart(document.getElementById('goodpointChart'));
            barchart.draw(data, barchart_options);
        },
        error: function(err) {
            console.error('Error fetching chart data', err);
        }
    });
    
}

function openInPopup(url) {
        window.open(url, '_blank', 'width=800,height=600,scrollbars=yes,resizable=yes');
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
		theform.attr("action", "/board_search");
		theform.submit();
	}
		}