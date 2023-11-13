/**
 * 
 */
function preimg( object ){
   console.log('사진 선택 변경');
   console.log( object );
      // 이벤트 실행시킨 태그의 DOM객체를 인수로 받음
   console.log( document.querySelector('.simg') );
   // 1. input태그의 속성 [ type, class, onchange, name 등등 ]
   // 1. input태그 이면서 type="file" 이면 추가적인 속성, 
      // .files : input type="file" 에 선택한 파일 정보를 리스트로 받음
   console.log( object.files );
      // 리스트 중에서 하나의 파일만 가져오기
   console.log( object.files[0] );   
   
   // --- 해당 파일을 바이트코드 변환
   // 2. JS 파일클래스 선언
      // 파일 읽기 클래스를 이용한 파일읽기객체 선언
   let file = new FileReader();   
   
   // 3. 파일 읽어오기 함수 제공
      // input에 등록된 파일리스트 붕 1개를 파일객체로 읽어오기
   file.readAsDataURL( object.files[0] );   
   console.log( file );
   
   // 4. 읽어온 파일을 해당 html img태그에 load
   file.onload = e => {
      console.log(e);      // onload() 실행한 FileReader 객체
      console.log( e.target.result );   // 읽어온 파일의 바이트코드
      document.querySelector('.preimg').src = e.target.result;   // img src 속성에 대입
   }
}

 
function onWrite()
{
	let form = document.querySelectorAll('.writeForm')[0];
	// 2. form 객체화 하기 
	let formData = new FormData( form );
	// 3. ajax로 대용량 form 전송하기
	
	// 본문 내용에서 줄 바꿈 처리 후 FormData에 추가
     let scontentTextarea = document.getElementById("scontent");
     let scontent = scontentTextarea.value.replace(/\n/g, "<br>"); // \n을 <br> 태그로 변경
     formData.set("scontent", scontent); // FormData에 추가
	

	
	if(formData.get("scontent").length == 0 || document.querySelector(".simg").files.length == 0)
	{
		alert('내용 또은 첨부파일을 넣어주세요')
		return
	}
	let getpwd = prompt("비밀번호 입력 8자리 이상")
	let type = 1
	 if(getpwd.length < 8){
		 alert("비밀번호 조건 에러")
		 return
	 }
	 formData.set("spwd",getpwd)
	 formData.set("type", type)
	 console.log(formData.get("type"))
	$.ajax({
		url : "/jspteam4/snsController" , 
		method: "post" , 
		data : formData ,
		contentType : false , 
		processData : false ,
		success : r => {
			if( r ){
				alert('글등록 성공');
				location.href="/jspteam4/jsp/snsView.jsp";
			}else{
				alert('글등록 실패 ');
			}
			
		} , 
		error : e => {console.log(e) } 
	})

}