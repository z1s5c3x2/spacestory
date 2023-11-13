<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="../css/snsView.css" rel="stylesheet" />
</head>
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
<body>
	<%@include file="../jsp/header.jsp"%>
	
	<div class="snscontainer">
	<input onkeyup="search()" class="search"  type="text" placeholder="search"/>
	<div class="viewSearch">총 게시물 : 15 </div>


		<div class="feedbox">
		
		</div>
	</div>


	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script src="/jspteam4/js/snsView.js" type="text/javascript"></script>

</body>
</html>