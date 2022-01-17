$(document).ready(function(){
	$.ajax({
		type: 'GET',
		url : '/user',
		dataType : 'json',
		success : function(res){
			console.log(res)
			$('#signinBtn').css('display', 'none');
			location.href='/accounting'
		},
		error : function(res){
			$('#accountingBtn').css('display', 'none');
		}
	})
})