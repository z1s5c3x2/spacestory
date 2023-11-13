let sno = new URL(location.href).searchParams.get("sno"); 


// 1. 수정할 게시물의 기존 정보를 보여주기
getBoard()
function getBoard(){
	// 2. 쿼리스트링(URL주소상의변수) 의 변수 가져오기
		// 쿼리스트링 : URL?변수명=값&변수명=값&변수명=값
		// new URL(location.href).searchParams.get("변수명");
	
	// 3. ajax에게 bno 전달해서 게시물정보 가져오기
	$.ajax({
		url : "/jspteam4/snsController",
		method : "get" ,
		data : { type : 2 , sno : sno } , 
		success : r => {
			console.log(r);
			// 응답 결과를 html 대입
			document.querySelector('.preimg').src = `/jspteam4/upload/sns/${r.simg}`;
			document.querySelector('.scontent').value = `${r.scontent}`;
		}
	})
} // f end 

// 2. 수정하기
function onUpdate(){
	
	let getpwd = prompt("비밀번호 입력 8자리 이상")
	 if(getpwd.length < 8){
		 alert("비밀번호 조건 에러")}
		 
	// 1. 입력된(수정된) form 정보 한번에 가져오기
		// 1. form 가져오기
	let writeForm = document.querySelectorAll('.writeForm')[0];
		// 2. form 객체화 하기
	let formdata = new FormData( writeForm ); 
		// 3. *form 객체에 데이터 추가 [bno]
			// .set( 속성명 , 값 ) // form에 속성 추가
		formdata.set("sno" , sno );
		formdata.set("spwd" , getpwd );
	// 2. ajax로 대용량 form 전송하기
	$.ajax({
					url : "/jspteam4/snsController" , 
					method: "put" ,			// form 객체 [ 대용량 ] 전송은 무조건 post 방식 
					data : formdata ,			// FormData 객체를 전송 
					contentType : false ,		// form 객체 [ 대용량 ]  전송타입 		
					processData : false ,
					success : r => { 
						if( r ){ 
							alert('수정 성공');
							location.href = `/jspteam4/jsp/snsView.jsp?sno=${sno}`;
						}
						else{ 
							alert('비밀번호가 일치하지 않습니다.');
						}
					} ,
					error : e => { console.log(e) } ,
			})
}

// 7. 첨부파일에 등록된 사진을 HTML 표시하기 < 등록 사진을 미리보기 기능 >
function preimg( o ){ 
	
	let file = new FileReader(); 

	file.readAsDataURL( o.files[0] ); // input 에 등록된 파일리스트(o.files) 중 1개를 파일객체로 읽어오기 

	file.onload = e => { // onload() : 읽어온 파일의 바이트코드를 불러오는 함수 구현 
	
		document.querySelector('.preimg').src = e.target.result; // img src속성에 대입 
	} 
	
} // f end 
