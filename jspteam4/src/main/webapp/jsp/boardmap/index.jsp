<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SPACE STORY 로그인</title>

	<link href="../../css/index.css" rel="stylesheet">

</head> 
<body>

	<%@include file = "header.jsp" %>
	
	<!-- 1.헤더에서 로그인메뉴를 클릭했을때 2.회원가입 성공하면 -->
	
	<div class="webcontainer"> <!-- 로그인 전체 구역  -->
	<img class="mainlogoimg" alt="" src="/jspteam4/img/Logo.png" style="width: 350px;
    position: relative;
    left: 350px;"/>
		<form class="signupForm"> <!-- 폼 전송시 각 input에 name속성 -->
			<div style="position: relative;bottom: 65px; right: 290px;">
				<div class="logintitle">
					<h2> SPACE STORY </h2>
					<p> 전국의 모든 일들을 한눈에! SPACE STORY에 오신걸 환영합니다! </p>
				</div >
				<div class="loginbox">
					<div class="intitle">아이디</div>
					<input maxlength="30" name="mid" class="mid"  type="text" /> 
					
					<div class="intitle">비밀번호</div>
					<input maxlength="20"  name="mpwd" class="mpwd" type="password" />
					
					<button class="signupbtn" onclick="login()" type="button" style="position: relative; left: 330px;">로그인</button>
				</div>
				<div class="logincheckbox" style="width: 300px; bottom: 30px; position: relative; left: 650px;"></div> <!-- 로그인 유효성검사 구역 -->
			</div>
			<div class="findbtnbox" style="width: 300px; position: relative; bottom: 86px; left: 361px;"> <!-- 아이디/비밀번호찾기 구역  -->
				<a href="findIdPw.jsp">IP/PW찾기</a>
				<a href="signup.jsp">회원가입</a>
			</div>
			
		</form>
	</div>
	
	<script src="../../js/boardmap/login.js" type="text/javascript"> </script>
	

</body>
</html>