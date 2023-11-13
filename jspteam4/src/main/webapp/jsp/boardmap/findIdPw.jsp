<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SPACE STORY ID/PW 찾기</title>
    <link rel="stylesheet" type="text/css" href="../../css/findIdPw.css">
</head>
<body style="	background-color: #f2f2f2">

<%@include file = "header.jsp" %>
    <div class="container">
    	<div style="display:flex; align-items: center; flex-direction: column;">
			<img class="mainlogoimg" alt="" src="/jspteam4/img/Logo.png" style="width:150px;"/>
			<h2> SPACE STORY ID/PW 찾기</h2>
		</div>
    	<div class="idpw">
	        <div class="section">
	            <h2>ID 찾기</h2>
	            <input type="email" id="findIdEmail" placeholder="이메일">
	            <button id="findIdButton">ID 찾기</button>
	            <p id="foundId"></p>
	        </div>
	        <div class="section">
	            <h2>비밀번호 찾기</h2>
	            <input type="text" id="findPwMid" placeholder="아이디">
	            <input type="email" id="findPwEmail" placeholder="이메일">
	            <button id="findPwButton">비밀번호 찾기</button>
	            <p id="foundPwd"></p>
	        </div>
        </div>
        <div class="loginsign">
         	<button type="button"><a href="index.jsp" style="color:white">로그인</a></button>
         	<button type="button"><a href="signup.jsp" style="color:white">회원가입</a></button>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="../../js/boardmap/findIdPw.js"></script>
</body>
</html>
