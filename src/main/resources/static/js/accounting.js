//init document
$(document).ready(function(){		
/*========================================================================================*/
/*                                                  초기화                                                        */
/*========================================================================================*/
	//날 짜 세팅
	$('#date').text(dateToStrShort(new Date()))
	
	//인사
	$.ajax({
		type: 'GET',
		url : '/user',
		dataType : 'json',
		success : function(res){
			$('#hello').text(res.name +"님 안녕하세요.")
		}
	})
	//가계부 초기화
	setReportArt(new Date(), true)
	
	//차트 모듈 로딩  
 	google.charts.load('current', {'packages':['corechart','bar']});
	

	
/*========================================================================================*/
/*                                              네비게이션 버튼                                                        */
/*========================================================================================*/
	//이동 버튼 시 활성화
	$('.move-btn').on('click',function(){
		moveSectnion($(this))
	})
	$('#analyze-btn').on('click',function(){
		google.charts.setOnLoadCallback(drawCostChart);
		//google.charts.setOnLoadCallback(drawCostBarChart);
	})
	
	// 휴지통 이동
	$('#trash-btn').on('click',function(){
		var dateStr = $('#date').text()
		var date  = strTodate(dateStr)
		setTrashArt(date)
	})

	// 가계부이동 이동
	$('#report-btn').on('click',function(){
		var dateStr = $('#date').text()
		var date  = strTodate(dateStr)
		setReportArt(date, true)
	})


	//로그아웃 버튼
	$('#signout-btn').on("click",function(){
		$.ajax({
			type : "GET",
			url:'/signout',
			async : false,
			success : function(res){

			},
			error : function(res){

			}
		})
		location.href = '/'
	})
	
/*========================================================================================*/
/*                                              가계부 이벤트                                                        */
/*========================================================================================*/
	//등록 활성화
	$('#plusBtn').on("click",function(){
		postFormPop()
	})
	//가계부 등록 프로세스
	$('#submitReport').on('click',function(){
		var amount = $('#amount').val()
		var mainCategory = $('#main_catagory option:selected').val()
		var subCategory = $('#sub_category').val()
		var reportingDate = $('#reporting_date').val()
		var memo = $('#memo').val()
		var dateStr = $('#date').text()
		var date  = strTodate(dateStr)
		if(amount == ''){
			alert("금액을 입력해 주세요.")
			$('#amount').val()
			$('#amount').focus()
			return
		}
		if(amount<=9){
			alert("10원 이상부터 입력이 가능합니다..")
			$('#amount').val()
			$('#amount').focus()
			return
		}
		if(subCategory==null||subCategory==''){
			alert("내역을 입력해 주세요/")
			$('#sub_category').focus()
			return
		}
		if(reportingDate == null||reportingDate == ''){
			reportingDate = new Date().toISOString().substring(0, 10);
		}
		var report = new Object()
		report.amount = amount
		report.mainCategory = mainCategory
		report.subCategory = subCategory
		report.reportingDate =reportingDate
		report.memo = memo
		postReport(report)
		setReportArt(date ,true)
	})
//paging
	$('#scroll-btn').on('click',function(){
		var dateStr = $('#date').text()
		var date  = strTodate(dateStr)
		setReportArt(date,false)
	})
//컨트롤러 로직
	//컨트롤러 +
	$('#plus').on('click', function(){
		var dateStr = $('#date').text()
		var date  = strTodate(dateStr)
		date = changeMonth(1,date)
		dateStr = dateToStrShort(date)
		$('#date').text(dateStr)
		setReportArt(date, true)
	})
	//컨트롤러 -
	$('#minus').on('click', function(){
		var dateStr = $('#date').text()
		var date  = strTodate(dateStr)
		date = changeMonth(-1,date)
		dateStr = dateToStrShort(date)
		$('#date').text(dateStr)
		setReportArt(date, true)
	})
/*---------------------------------------------------------------------------------------------*/
/*                              동적 엘레멘트 이벤트 for 가계부 페이지	                              */
//자세히 보기
	$(document).on('click', ".brief_info",function(){
		$(this).parent().find('.detail_info').css('display','block');
	})
//자세히 보기 닫기
	$(document).on('click', ".close-btn",function(){
		$(this).parent().parent().css('display','none');
	})
//삭제 버튼
	$(document).on('click',".delete-btn",function(){
		var report_box = $(this).parent().parent().parent()
		deleteReport(report_box,true)
	})
//수정 하기 활성화
	$(document).on('click',".edit-btn",function(){
		var ele = $(this).parent().parent().find('.edit-box')
		console.log(ele.css('display')=='none')
		
		if(ele.css('display') == 'none'){
			ele.css('display','block')
		}else{
			ele.css('display','none')
		}
		//$(this).parent().parent().find('.edit-box').css('display','block')
	})
//수정 등록하기
	$(document).on('click', '.submit-edit-btn', function(){
		var editBox = $(this).parent()
		putReport(editBox)
	})
/*---------------------------------------------------------------------------------------------*/
/*                              동적 엘레멘트 이벤트 for 휴지통 페이지                       */
//복구하기
	$(document).on('click', '.restore-btn',function(){
		var report_box = $(this).parent().parent()
		deleteReport(report_box,false)
	})
	
})
/*========================================================================================*/
/*                                                  도큐먼트 초기화 끝                                                        */
/*========================================================================================*/

/*========================================================================================*/
/*                                      static val                                                      */
/*========================================================================================*/
var start = 0;


/*========================================================================================*/
/*                                           기본 http 메서드                                                      */
/*========================================================================================*/
//post
function postReport(report){
	$.ajax({
		type : 'POST',
		url : '/accounting/report',
		async : false,
		data : JSON.stringify(report),
		contentType:'application/json;charset=utf-8',
		success : function(res){
			
			alert("등록이 완료 되었습니다.")
			$('#amount').val('')
			$('#memo').val('')
			postFormPop()
		},
		error : function(res){
			alert("등록할 수 없습니다.")
		}
	})
}
//get
function getReport(date, hidden, paging){
	var param = new Object();
	param.hidden = hidden;
	if(date !=null) param.reportingDate = date
	param.paging = paging;
	if(paging ==true){
		param.start = start;
		param.length = 20;
		
	}  
	var obj
	$.ajax({
		type : 'GET',
		url : '/accounting/report',
		async : false,
		data : param,
		success : function(res){
			obj = res
		}
	})
	return obj
}

//delete
function deleteReport(reportBox, hidden){
	$.ajax({
		type : 'DELETE',
		url : '/accounting/report',
		async : false,
		data : {
			id : reportBox.attr('id'),
			hidden : hidden
		},
		success : function(){
			alert('완료되었습니다.')
			reportBox.remove()
		}
	})
}
//put
function putReport(editBox){
	var breifInfo = editBox.parent().parent()
	var report = new Object()
	report.amount = editBox.find('#amount').val()
	report.subCategory = editBox.find('#sub_category').val()
	report.memo = editBox.find('#memo').val()
	report.mainCategory = breifInfo.find('.main_category').text()
	report.id = breifInfo.attr('id')
		$.ajax({
		type : 'PUT',
		url : '/accounting/report',
		async : false,
		data : JSON.stringify(report),
		contentType:'application/json;charset=utf-8',
		success : function(res){
			alert("등록이 완료 되었습니다.")
			var dateStr = $('#date').text()
			var date  = strTodate(dateStr)
			setReportArt(date, true)
			
		},
		error : function(res){
			
		}
	})
}
/*=====================================================================================================================*/
/*                    동적 엘레멘트 로직*/
/*=====================================================================================================================*/

//차트 만들기drawCostBarChart
function drawCostBarChart(){
	var dateStr = $('#date').text()
	var date  = strTodate(dateStr)
	var array
	$.ajax({
		type: 'GET',
		url : '/accounting/costBarChart',
		data : {
			reportingDate : date
		},
		success : function(res){
			array = res
			var data = new google.visualization.DataTable();
			data.addColumn('string', '월');
			data.addColumn('number', '수입');
   			data.addColumn('number', '지출');
			var len = array.length
			console.log(len)
			if(len == 0) {
				return
				$('#bar-chart').empty()
				$('#bar-chart').text('내역이 존재하지 않습니다.')
			}
			for(var i = 0 ; i < len ; i++){
				console.log(array[i])
				data.addRow(array[i])
			}
	
		  var options = {};
		
        var options = {
          chart: {
            'title':' 월별 소득/지출 그래프',
  			
	        	  },
          	//bars: 'horizontal' // Required for Material Bar Charts.
	        };
	      var chart = new google.charts.Bar(document.getElementById('bar-chart'));
	      chart.draw(data, google.charts.Bar.convertOptions(options));
		}
	})
}
//차트 만들기
function drawCostChart(){
	var dateStr = $('#date').text()
	var date  = strTodate(dateStr)
	var array
	$('#pie-chart').empty()
	$('#pie-chart').text('지출 내역이 존재하지 않습니다.')
	$.ajax({
		type: 'GET',
		url : '/accounting/costPieChart',
		data : {
			reportingDate : date
		},
		success : function(res){
			array = res
			var data = new google.visualization.DataTable();
			data.addColumn('string', '분류');
   			 data.addColumn('number', '금액');
				console.log(array==null)
			var len = array.length
			if(len == 0) {
				return
				$('#pie-chart').empty()
				$('#pie-chart').text('지출 내역이 존재하지 않습니다.')
			}
			for(var i = 0 ; i < len ; i++){
				console.log(array[i])
				data.addRow(array[i])
			}
	
		  var options = {'title':dateStr+' 지출 분석',
		             'width':400,
		             'height':300};
		
		      var chart = new google.visualization.PieChart(document.getElementById('pie-chart'));
		      chart.draw(data, options);
		}
	})
}

//가계부 화면 채우기
function setReportArt(date, init){
			if(init){
				$('.reports_list').empty()
				start = 0

			}
			var res = getReport(date, false, true)

			if(res == null) {
				return;	
			}
			var len = res.length
			start += len;

			if(len == 0 ){
				if(init) {
					$('.reports_list').text("항목이 없습니다.")
				}else{
							
				}
				return;
			} 
			for(var i = 0 ; i < len ; i++){
				var amount = res[i].amount
				var date = dateToStrLong(res[i].reportingDate)
				date = date.substr(0,date.length-5)
				
				var mainCategory = res[i].mainCategory
				var subCategory = res[i].subCategory
				var id = res[i].id
				var memo = res[i].memo

				
				var contents = '<li id ='+id+'>'
								+"<div class ='report_box' id ="+id+">"
									+"<div class = 'brief_info' id = '"+id+"' style='display:flex; justify-content : space-evenly;'>"
											+"<div class ='reporting_date'>"
											+"일자 :  "	+date
											+"</div>"
											+"<div class ='main_category'>"	
											+"구분 :  "		+mainCategory
											+"</div>"
											+"<div class ='sub_category'>"	
											+"내역 :  "		+subCategory
											+"</div>"
											+"<div class ='amount'>"
											+"금액 :  "	+amount+'원'
											+"</div>"
										+"</div>"
										+"<div class = 'detail_info' style='display:none'>"
											+memo
											+"<div class = 'btn-con'>"
												+"<button class = 'edit-btn'>수정</button><button class = 'delete-btn'>삭제</button><button class = 'close-btn'>닫기</button>"
											+"</div>"
											+"<div class = 'edit-box' style='display:none'>"
												+"금액 :  <input id = 'amount' type = 'number' min = '10' step = 'any' value = '"+Math.abs(amount)+"'><br>"
												+"중분류 : <input id = 'sub_category' name ='sub_category' type ='text' autocomplete ='on' value = '"+subCategory+"'><br>"
												+"메모 : <input id = 'memo' type = 'text' value ='"+memo+"'><br><br>"
												+"<button class = 'submit-edit-btn'>등록</button>"
											+"</div>"
										+"</div>"
									+"</div>"
								'</li>'
				
				$('.reports_list').append(contents)
			}
}
//휴지통 화면 채우기
function setTrashArt(){
	var res = getReport(null, true, false)
	if(res == null) {
				return;
			}
			$('.trash-list').empty()
			var len = res.length
			if(len == 0 ){
				$('.trash-list').text('내역이 없습니다.')
				return;
			} 
			for(var i = 0 ; i < len ; i++){
				var amount = res[i].amount
				
				var date = dateToStrLong(res[i].reportingDate)
				date = date.substr(0,date.length-5)
				
				var mainCategory = res[i].mainCategory
				var subCategory = res[i].subCategory
				var id = res[i].id
				var memo = res[i].memo

				
				var contents = '<li id ='+id+'>'
								+"<div class ='report_box' id ="+id+">"
									+"<div class = 'brief_info' id = '"+id+"' style='display:flex; justify-content : space-evenly;'>"
											+"<div class ='reporting_date'>"
											+"일자 :  "	+date
											+"</div>"
											+"<div class ='main_category'>"	
											+"구분  : "		+mainCategory
											+"</div>"
											+"<div class ='sub_category'>"	
											+"내역 :  "	+subCategory
											+"</div>"
											+"<div class ='amount'>"
											+"금액 :  "	+amount+'원'
											+"</div>"
											+"<button class = 'restore-btn'>복구하기</button>"
										+"</div>"
									+"</div>"
								'</li>'
				
				$('.trash-list').append(contents)
			}
}

/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*                      유틸                                             */
/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
//여닫기 
function postFormPop(){
		var status = $('#reportForm').css('display')
		if(status =='none'){
			$('#reportForm').css('display','block')
		}else{
			$('#reportForm').css('display','none')
		}
}

//날짜 변경
function changeMonth(n , date){
	date.setMonth(date.getMonth()+n)
	return date
}
//문자열 데이트화 (연도 / date => Date)
function strTodate(dateStr){
	var year = String(dateStr).substr(0,4)
	var month = String(dateStr).substr(5,7)
	var date = new Date(year, month)
	return date
}
//문자열 데이트화 (가계부 화면)
function dateToStrShort(date){
	dateStr =  date.toISOString().toString().substr(0,7)
	var year = dateStr.toString().substr(0,4)
	var month = dateStr.toString().substr(5,7)
	dateStr = year +'/' +month

	return dateStr
}
function dateToStrLong(date){
	var d = new Date(date)
	var dateStr = d.toLocaleDateString('en-Us', {timeZone: 'Asia/Seoul'})
	return dateStr
}

function moveSectnion(btn){
	var arr =[
		$('.analyze-art'),
		$('.trash-art'),
		$('.report-art'),
	]
	for(var i = 0 ; i <3;i++){
		if(btn.attr('id').substr(0,4)==arr[i].attr('class').substr(0,4)){
			arr[i].css('display','block')
		}else{
			arr[i].css('display','none')
		}
	}
	
}
