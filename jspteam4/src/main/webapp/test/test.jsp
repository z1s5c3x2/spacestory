<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div>
         <h2> - 카카오지도  - </h2>
         <div id="clickLatlng"></div>
         <div class="detailbox">
         
         </div>
         <div id="map" style="width:100%;height:500px;"></div>
      </div>
      
	
	<!-- 카카오js -->
   <!-- services 라이브러리 불러오기 -->
   
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1ac4a57d8a5927d34020a891fcdbbcbd&libraries=services"></script>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1ac4a57d8a5927d34020a891fcdbbcbd&libraries=clusterer"></script>
	<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script  src="./test.js"></script>
</body>
</html>