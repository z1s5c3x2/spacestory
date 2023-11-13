
//1. 현재 로그인된 회원정보 요청
let loginState =false;
let loginMid ='';
let 헤더변수 = "헤더변수데이터";

function login(){
	// 1. 입력받은 아이디, 비밀번호 가져온다.
	let mid = document.querySelector('.mid').value
	let mpwd = document.querySelector('.mpwd').value
	// 2. ajax에게 전달해서 회원이 존재하는지 반환 받는다.
		// 2-1 : 로그인성공시 index.jsp 이동 
		// 		: 로그인실패시 'logincheckbox' 실패 알림
	$.ajax({
		url : "/jspteam4/MemberFindController" ,
		method : "post", 
		// get메소드는 전송하는 data노출O(보안취약) post메소드는 전송하는 data노출X(보안)
		data : { mid : mid , mpwd : mpwd } ,
		async: false , 
		success : r => { 
			if( r ){ location.href="/jspteam4/jsp/boardmap/map.jsp";
				loginState=true ; loginMid=r.mid;}
			else{ 
					document.querySelector('.logincheckbox')
						.innerHTML = '동일한 회원정보가 없습니다.'; 
				loginState=false; loginMid='';}
		} ,
		error : e => { console.log(e); } 
	});
	
}; 

//2. 로그아웃 함수 [ 서블릿에 세션을 삭제 그리고 로그아웃 성공시 메인페이지로 이동]
function logout() {
	$.ajax({
      url : "/jspteam4/MemberInfoController",
      method : "get",
      data : {type : "logout"} ,
      success : r => {
		  location.href="/jspteam4/jsp/boardmap/index.jsp"
	  },
      error : e => {console.log(e)}
      })
	
}

getMemberInfo(); 
function getMemberInfo(){
	$.ajax({ // 1. ajax 이용한 서블릿세션 정보 가져오기
		url : "/jspteam4/MemberInfoController" ,
		method : "get" ,
		async : false , /* 비동기화(기본값=true) , 동기화(false) 설정하는 속성 ( 우선적인 ajax실행 응답이 필요할때. )*/
		data : { type : "info" } , 
		success : r => { console.log( r ); 
			let submenu = document.querySelector('.submenu')
			let html = ``; // - 로그인 상태에 따른 서로다른 html 구성  
			if( r == null ){ // 비로그인 
			
				html += ` <li> <a href="/jspteam4/jsp/boardmap/signup.jsp">회원가입</a> </li>
						<li> <a href="/jspteam4/jsp/boardmap/index.jsp">로그인</a> </li> `;
			}else{ // 로그인 	
				loginState = true; loginMid = r.mid;
				html += `
						<li> ${ r.mnickname } 님 </li>
						<li> <a href="/jspteam4/jsp/boardmap/info.jsp">내정보/유저검색</a></li>
						<li> <a onclick="logout()" href="#">로그아웃</a> </li> `
				if( r.mid == "admin"){ } // 로그인 했는데 관리자메뉴
			}
			// - 구성된 html 대입
			//submenu.innerHTML = html;
		} ,error : e => {} 
	})
} // f end 

