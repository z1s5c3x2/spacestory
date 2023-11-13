<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>지도 생성하기</title>
	
	<!-- 폰트어썸 -->
	

   <link href="/jspteam4/css/modal.css" rel="stylesheet">
   <link href="/jspteam4/css/info.css" rel="stylesheet">
   <link href="/jspteam4/css/map.css" rel="stylesheet">
   <!-- 폰트어썸 -->
   <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.14.0/css/all.css">
   <!-- 부트스트랩 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://getbootstrap.com/docs/5.3/assets/css/docs.css" rel="stylesheet">
    <!--  통계용 chart js-->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
   
</head>
<body>

   <%@include file="header.jsp"%>

   <!-- 지도를 표시할 div 입니다 -->

   <div id="map" style="width: 100%; height: 900px; margin: 0 auto;">
      <!-- 차트 표시 공간 -->
      <div id="chatWrap">
         <div class="chartDropDownBox">
            <select class="chartTimeList chartSelect form-select form-select-sm"  onchange="chartView()">
               <option value="%Y">연도별</option>
               <option value="%m">월별</option>
               <option value="%d">일별</option>
               <option value="%H">시간별</option>
            </select> <select class="chartCategoryList chartSelect form-select form-select-sm" 
               onchange="chartView()">
               <option value="0">카테고리</option>
            </select>
         </div>
         <span class="close-chart btn-close"></span>
         <div class="chartCanvs">
            <canvas class="postChart"></canvas>
         </div>
         
      </div>
      <div id="postWrap">
         <h2 class="postWrapTitle">게시글 목록</h2>
         <div>
            <div class="posttop">
               <select class="listsize  postSelect form-select-sm" >

               </select> <select class="catelist  postSelect form-select-sm" >
                  <option value=0>카테고리</option>
               </select> <select class="sortList  postSelect form-select-sm">
                  <option value="최신순">최신순</option>
                  <option value="오래된순">오래된순</option>
                  <option value="거리순">거리순</option>
               </select>
            </div>

         </div>

         <div class="postBox"></div>
         
         <ul class="pageBtnList">

         </ul>
      </div>
      
       
         <!-- 내정보 콜랩스 -->
      <p class="d-inline-flex gap-1">
       </p>
       <div class="collapse" id="collapseExample" style="z-index:6; position: absolute;">
         <div class="card card-body" style="width:0px; padding:0px;">
         <div class="webcontainer"> <!-- 회원가입 전체 구역  -->
         <form class="signupForm"> <!-- 폼 전송시 각 input에 name속성 -->
            <div class="myinfo" style="position: relative; left: 350px;width: 700px; top: 0px;">
               <div>
               <h2> 내 정보보기 </h2>
               <a class="collapsed btn-close" data-bs-toggle="collapse" 
                href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample"
                 style="FONT-SIZE: 20PX; z-index: 2; position: absolute; top: 20px; right: 25px;"></a>
                  </div>
               <div class="intitle">현재 아이디</div>
               <div class="mid"></div>
               
               <div class="intitle">현재 이메일</div>
               <div class="memail"></div>
               
               <div class="intitle">현재 닉네임</div>
               <div class="mnickname2"></div>
               
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
            
         </form>
      </div></div>
       </div>
       
      <button onclick="latlngSearchBoard()" class="setFocus"> 정보 갱신</button>
       
       <a class="btn btn-primary collapsed" data-bs-toggle="collapse" 
       href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample"
        style="z-index: 2; position:absolute; top: 60px; right: 10px;">
        내정보
         </a>
         <button class="btn btn-primary" type="button" onclick="logout()" style="z-index: 2; position:absolute; top: 105px; right: 10px;">로그아웃</button>
         <button class="chartbtn btn-primary btn" onclick="chartView()">통계 보기</button>
            <div class="webcontainer2" >
           <div class="chatting" >
         
         </div>
   
      </div>
         <div class="nicknamesearchbar">
            <input class="searchbar2" name="searchbar" placeholder="조회할 닉네임을 입력해주세요." type="text" style="z-index: 2; position:absolute; top: 13px; right: 65px;">
          <button onclick="searchinfo()" class="btn btn-primary" type="button" data-bs-toggle="offcanvas" 
           data-bs-target="#offcanvasExample" aria-controls="offcanvasExample"
            style="z-index: 2; position:absolute; top: 8px; right: 10px;">
           검색</button>   
       </div>
   </div>
   <div id="clickLatlng"></div>

    <div id="modal" class="resultbox modal-overlay"> <!--카테고리  -->

    </div>
  
   
   <!-- 닉네임검색 사이드바 -->
   
    <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasExample" aria-labelledby="offcanvasExampleLabel">
      <div class="offcanvas-header">
        <div class="searchresultbox"></div>
      </div>
    </div>
    
 

   <!--마커 채팅입장하기,생성하기  -->

   
      



   <script type="text/javascript"
      src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1d55986e78fa9e3909689ada90eb437d&libraries=clusterer"></script>
   <script type="text/javascript"
      src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1d55986e78fa9e3909689ada90eb437d"></script>

   
   
   <div class="review_view">
   
   </div>

   <!-- 부트스트랩 -->
   <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
   <script src="/jspteam4/js/boardmap/signup.js" type="text/javascript"> </script>
   <script src="/jspteam4/js/boardmap/info.js" type="text/javascript"></script>
   <script src="/jspteam4/js/boardmap/chatting.js" type="text/javascript"></script>
   <script src="/jspteam4/js/boardmap/map.js" type="text/javascript"></script>
   <script src="/jspteam4/js/boardmap/modal.js" type="text/javascript"></script>





</body>