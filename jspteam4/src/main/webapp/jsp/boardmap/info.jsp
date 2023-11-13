<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

	<link href="../../css/info.css" rel="stylesheet">

</head> 
<body>

	<%@include file = "header.jsp" %> <!-- 헤더 호출 -->

	<div class="webcontainer"> <!-- 회원가입 전체 구역  -->
		<form class="signupForm"> <!-- 폼 전송시 각 input에 name속성 -->
			<div class="myinfo">
				<h2> 내 정보보기 </h2>
				
				<div class="intitle">내 아이디</div>
				<div class="mid"></div>
				
				<div class="intitle">내 이메일</div>
				<div class="memail"></div>
				
				<div class="intitle"> 변경할 닉네임</div>
				<input maxlength="12" onkeyup="nicknamecheck() " name="mnickname" class="mnickname"  type="text" /> 
				<div class="nicknamecheckbox"></div>
				
				<div class="intitle"> 새로운 비밀번호</div>
				<input maxlength="20"  onkeyup="pwcheck()" name="mpwd" class="mpwd" type="password" />
				
				<div class="intitle"> 새로운 비밀번호확인</div>
				<input maxlength="20" onkeyup="pwcheck()"  name="mpwdconfirm" class="mpwdconfirm" type="password" />
				<div class="pwcheckbox"></div>
				
				<button class="signupbtn" onclick="mupdate()" type="button"> 수정 </button>
				<button class="signupbtn" onclick="mdelete()" type="button"> 탈퇴 </button>
			</div>
			
			<div class ="searchbox">
				<h2> 유저검색 </h2>
				<div class="seachbar1"><input class="searchbar" name="searchbar" placeholder="닉네임을 입력해주세요." type="text"><button onclick="searchinfo()" type="button">검색</button></div>
				<div class="resultbox"></div> <!-- js에서 결과물 출력해줄 구역 -->
			</div>
			
			
		</form>
	</div>
	<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
	<!--  <script src="../../js/boardmap/signup.js" type="text/javascript"> </script>
	<script src="../../js/boardmap/info.js" type="text/javascript"> </script>-->
	

</body>
</html>