<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href='../css/snsWrite.css' rel='stylesheet'> 
</head>
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
<body>	
	<%@include file ="../jsp/header.jsp" %>
		<div class="webcontainer">
		
		<h3>글 작성</h3>
		<form class="writeForm">
		<img class ="preimg" src=""/>
			<textarea name="scontent" class="scontent"></textarea>
			
			<br /> <input onchange="preimg( this )" type="file" name="simg" class="simg">
			
			<br />
			<div class="btnBox">
			<button onclick="onWrite()" type="button">작성</button>
			<button type="reset">다시쓰기</button>
			</div>
		</form>
	</div>
	<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript" src="/jspteam4/js/snsWrite.js"></script>

</body>
</html>