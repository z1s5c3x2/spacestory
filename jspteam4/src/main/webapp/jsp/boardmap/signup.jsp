<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="../../css/signup.css" rel="stylesheet">
<title>SPACE STORY 회원가입</title>
</head>
<body>

	<%@include file = "header.jsp" %>

	<div class="webcontainer"> <!-- 회원가입 전체 구역  -->
		<form class="signupForm"> <!-- 폼 전송시 각 input에 name속성 -->
			
			<div class="titlehead">
				<div>
					<img class="mainlogoimg" alt="" src="/jspteam4/img/Logo.png"/>
					<h2> SPACE STORY 회원가입</h2>
				</div>
				<p> 전국의 모든 일들을 한눈에! Space Story에 오신걸 환영합니다! </p>
			</div>
			<div class="intitle">아이디</div>
			<input maxlength="30" onkeyup="idcheck() " name="mid" class="mid"  type="text" /> 
			<div class="idcheckbox"></div>
			
			<div class="intitle">비밀번호</div>
			<input maxlength="20"  onkeyup="pwcheck()" name="mpwd" class="mpwd" type="password" />
			
			<div class="intitle">비밀번호 확인</div>
			<input maxlength="20" onkeyup="pwcheck()"  class="mpwdconfirm" type="password" />
			<div class="pwcheckbox"></div>
			
			<div class="intitle">이메일</div>
			<div class="emailbox">
				<input onkeyup="emailcheck()" name="memail" class="memail" type="text" /> 
				<button disabled class="authReqBtn" onclick="authReq()"  type="button">인증요청</button> 
			</div>
			<div class="authbox"> </div>
			<div class="emailcheckbox"></div>
			
			<div class="intitle">닉네임</div>
			<input maxlength="12" onkeyup="nicknamecheck() " name="mnickname" class="mnickname"  type="text" /> 
			<div class="nicknamecheckbox"></div>
			
			<button class="signupbtn" onclick="signup()" type="button">회원가입</button>
			
		</form>
	</div>
	
	<script src="../../js/boardmap/signup.js" type="text/javascript"> </script>
	

</body>
</html>