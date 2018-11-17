//设置一个全局的变量，便于保存验证码
var code;
/*验证码生成*/
function createCode(){
	//首先默认code为空字符串
	code = '';
	//设置长度，这里看需求，我这里设置了4
	var codeLength = 4;
	var codeV = document.getElementById('verify_code');
	//设置随机字符
	var random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R', 'S','T','U','V','W','X','Y','Z');
	//循环codeLength 我设置的4就是循环4次
	for(var i = 0; i < codeLength; i++){
		//设置随机数范围,这设置为0 ~ 36
		 var index = Math.floor(Math.random()*36);
		 //字符串拼接 将每次随机的字符 进行拼接
		 code += random[index]; 
	}
	//将拼接好的字符串赋值给展示的Value
	codeV.value = code;
}

/*验证码校验*/
function validate(){
	var oValue = document.getElementById('verify_input').value.toUpperCase();
	if(oValue ==0){
		alert('请输入验证码');
		return false;
	}else if(oValue != code){
		alert('验证码不正确，请重新输入');
		$("#verify_input").val('').focus();
		oValue = ' ';
		createCode();
		return false;
	}else{
		submitLoginForm();
	}
}

//设置此处的原因是每次进入界面展示一个随机的验证码，不设置则为空
window.onload = function (){
	createCode();
}

function submitLoginForm(){
	var uid = $('#uid').val();
	var pwd = $('#pwd').val();
//	alert(uid);
//	alert(pwd);
	$.ajax({
	  type: "get",
	  url: "./CXF/REST/Domain/doLogin/"+uid+"/"+pwd,
	  success: function (result, status, xhr) {
		  if (xhr.getResponseHeader("EntityClass") == "User"){
			  alert("登录成功！");
			  window.location.href = './index.html';
		  }else {
			  alert("用户名或密码错误!");
			  cleanForm();
		  }
	  },
	  error: function(response) {		  
		  	alert("登录失败!服务器内部错误");
			cleanForm();
	  }
	});
}


function cleanForm(){
	var verify_input = document.getElementById('verify_input').value.toUpperCase();
	var uid = document.getElementById('uid').value.toUpperCase();
	var pwd = document.getElementById('pwd').value.toUpperCase();
	verify_input="";
	uid = "";
	pwd = " ";
}




