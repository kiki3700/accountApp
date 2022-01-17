$(document).ready(function(){
	$("#id").keyup(function(){
		idVerification = false;
		console.log("수정중"+idVerification)
	})
	
	$("#submitBtn").on("click",function(){
		var reg = /^[가-힣]{2,4}$/;
		var id = $("#id").val()
		var name = $("#name").val()
		var password = $("#password").val()
		if(idVerification==false){
			alert("아이디 중복 검사를 실시해 주세요")
			return
		}
		if(name.length==0){
			alert("이름을 입력해 주세요.")
			return
		}
		if(password.length==0){
			alert("패스워드를 입력해 주세요.")
			return
		}
		user = new Object();
		user.id = id
		user.name = name
		user.password = password	
		signup(user)
	})
		
		$("#verifyIdBtn").on("click",function(){
		var regEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;
		var id = $("#id").val()
		if(id.length == 0 ){
			alert("아이디를 입력해 주세요.")
			return
		}
		
		if(!regEmail.test(id)){
			$("#id").val('') 
			alert("이메일 형식으로 입력해 주세요.")
			return
		}
		param = new Object()
		param.id = id
		$.ajax({
			type : "GET",
			url:'/verifyUser',
			data : param,
			async : false,	
			success : function(res){
				if(res){
						alert("사용 가능한 아이디 입니다.")
						idVerification = true;
					}else{
						alert("중복된 아이디 입니다..")	
					}
			},
			error : function(res){
				alert("다시 시도해 주시길 바랍니다.")
				//location.reload();
			}	
		})
	})
});
var idVerification = false;
function signup(user){
	console.log(user)
	$.ajax({
			type : "POST",
			url:'/user',
			async : false,
			data : JSON.stringify(user),
			contentType:'application/json;charset=utf-8', 	
			success : function(res){
				if(res == true){
					alert("회원가입에 성공하였습니다..")
					location.href = '/signin'
				}else{
					alert("회원가입에 실패하였습니다. 다시 시도해 주시길 바랍니다...")
				}
			},
			error : function(res){
				alert("다시 시도해 주시기 바랍니다.")
			}
			
	})
} 




