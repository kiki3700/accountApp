$(document).ready(function(){
	$("#signinBtn").on("click",function(){
		var regEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;
		var id = $('#id').val()
		var password = $("#password").val()
		var user = new Object()
		user.id = id
		user.password = password
		if(id.length==0){
			alert("아이디를 입력해주세요.")
			return
		}
		if(regEmail.test(id) == false){
			alert("아이디는 이메일 형식입니다..")
			$('#id').val("")
			return
		}
		if(password.length==0){
			alert('비밀번호를 입력해 주세요.')
			return
		}
		$.ajax({
			type : "POST",
			url : "/signin",
			async : false,
			data : JSON.stringify(user),
			contentType:'application/json;charset=utf-8',
			success : function(res){
				console.log("success")
				location.href = '/accounting'
			},
			error : function(res){
				alert("다시 시도해 주시기 바랍니다.")
			}
		})
	})
})

