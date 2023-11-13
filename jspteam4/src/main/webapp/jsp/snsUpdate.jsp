<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="../css/snsUpdate.css" rel="stylesheet">

</head>
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
<body>

	<div class="snscontainer">
	<%@include file="../jsp/header.jsp"%>
		<h3> 게시물 수정 페이지 </h3><br/>
		
		<form class="writeForm"> <!-- 쓰기 입력 구역 -->
			
			<input onchange="preimg( this )" name="simg" class="simg" type="file" accept="image/*"/>
			<img class="preimg" alt="" src=""/> 
			<textarea name="scontent" class="scontent" placeholder="수정할 내용을 입력해주세요."></textarea> <br/>
			<button onclick="onUpdate()" type="button"> 글수정 </button> 
			<a href="snsView.jsp"><button type="button"> 목록보기 </button></a>
		</form>
		
	</div>
	
	<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script src="../js/snsUpdate.js" type="text/javascript"></script>


</body>
</html>