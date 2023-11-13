// 1. 아이디 유효성검사.
function idcheck(){ /* 실행조건 : 아이디 입력창에 입력할때마다 */
	// 1. 값 호출 
	let mid = document.querySelector('.mid').value; 
	let idcheckbox = document.querySelector('.idcheckbox');
	// 2. 유효성검사 
		// 제어문 이용한 검사 if( mid.length < 5 && mid.length >= 30  ) { }
		// 1. 아이디는 영문(소문자)+숫자 조합의 5~30글자 사이 이면
			// 1. 정규표현식 작성.
		let midj = /^[a-z0-9]{5,30}$/
			// 2. 정규표현식 검사. 
			console.log( midj.test( mid ) )
			console.log( mid )
		if( midj.test(mid) ){ // 입력한 값이 패턴과 일치하면
			// -- [ 아이디중복검사 ]입력한 아이디가 패턴과 일치하면
			$.ajax({
				url : "/jspteam4/MemberFindController" ,
				method : "get" ,
				data : { type : "mid" , data : mid },
				success : r => { 
					if( r ){  idcheckbox.innerHTML = '사용중인 아이디 입니다.'; checkList[0] = false; }
					else { idcheckbox.innerHTML = '사용가능한 아이디 입니다.'; checkList[0] = true; } 
				} ,
				error : e => { }
			})
		}else{ // 입력한 값이 패턴과 일치하지 않으면
			idcheckbox.innerHTML ='영문(소문자)+숫자 조합의 5~30글자 가능합니다.'; checkList[0] = false;
		}
	// 3. 결과 출력 
}

// 2. 비밀번호 유효성검사 [ 1.정규표현식 검사 2. 비밀번호 와 비밀번호 확인 일치여부 ]
function pwcheck(){	console.log('패스워드 입력중');
	let pwcheckbox = document.querySelector('.pwcheckbox')
	// 1. 입력 값 호출 
	let mpwd = document.querySelector('.mpwd').value; 					console.log('mpwd : ' + mpwd);	
	let mpwdconfirm = document.querySelector('.mpwdconfirm').value;		console.log('mpwdconfirm : ' + mpwdconfirm);	
	
	// 2. 유효성검사 
		// 1. 정규표현식 만들기 [ 영대소문자(1개필수) + 숫자(1개필수) 조합 5~20글자 사이 ]
		// let mpwdj = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{5,20}$/
		let mpwdj = /^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{5,20}$/
		
		if( mpwdj.test( mpwd ) ){  // 1.비밀번호 정규표현식 검사 
			// 2.비밀번호 확인 정규표현식 검사 
			if( mpwdj.test( mpwdconfirm) ){
				// 3. 비밀번호 와 비밀번호 확인 일치여부
				if( mpwd == mpwdconfirm ){ 
					pwcheckbox.innerHTML = `사용가능한 비밀번호`; checkList[1] = true;
				}else{
					pwcheckbox.innerHTML = `비밀번호가 일치하지 않습니다.`; checkList[1] = false;
				}
			}else{
				pwcheckbox.innerHTML = `영대소문자1개이상+숫자1개이상 조합 5~20글자 사이로 입력해주세요.`; checkList[1] = false;
			}
		}else{
			pwcheckbox.innerHTML = `영대소문자1개이상+숫자1개이상 조합 5~20글자 사이로 입력해주세요.`;checkList[1] = false;
		}
} // f end 

// 3. 이메일 유효성검사 [ 1. 정규표현식 2. 중복검사 ]
function emailcheck(){
	let emailcheckbox = document.querySelector('.emailcheckbox');
	let authReqBtn = document.querySelector('.authReqBtn');
	// 1. 입력된 값 호출 
	let memail = document.querySelector('.memail').value 
	// 2. 이메일 정규표현식 [  영대소문자,숫자,_-  @  ]
	let memailj = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\.[a-zA-Z]+$/
	// 3. 유효성검사 
	if( memailj.test( memail) ) {
		$.ajax({
			url : "/jspteam4/MemberFindController" , 
			method : "get" , 
			// *type 사용하는 이유 : 서로 다른 ajax가 동일한 서블릿과 동일한 get메소드 사용할때.
			data :  { type : "memail" , data : memail }, // : 이메일 중복검사
			// data :  { type : "mid" , data : mid }, : 아이디 중복검사  
			success : r => {  
				if( r ){
					emailcheckbox.innerHTML =`사용중인 이메일입니다.`;
					authReqBtn.disabled = true; // 해당 버튼의 disabled 속성 적용
					checkList[2] = false;
				}else{
					emailcheckbox.innerHTML =`사용가능한 이메일입니다.`;
					authReqBtn.disabled = false; // 해당 버튼의 disabled 속성 해제 
					checkList[2] = false;
				}
			} ,
			error : r => { console.log(r); } 
		})
	}else{
		emailcheckbox.innerHTML = `이메일형식에 맞게 입력해주세요.`;
		authReqBtn.disabled = true; // 해당 버튼의 disabled 속성 적용
		checkList[2] = false;
	}
} // f end 

// 4. 인증요청 버튼을 눌렀을때.
function authReq(){
	
	// ------------------------- 테스트용 ---------------- //
	// 1. 'authbox' div 호출 
	let authbox = document.querySelector('.authbox')
	
	// 2. auth html 구성 
	let html = `<span class="timebox">02:00</span>
				<input class="ecode" type="text" /> 
				<button onclick="auth()" type="button">인증</button> `
	// 3. auth html 대입 
	authbox.innerHTML = html;
	// 4. 타이머 실행
	authcode = "1234" ; 		 // [ 테스트용 ] 임의로 인증 코드를 '1234'
	timer = 120; 		// [ 테스트용 ] 인증 제한시간 10초 
	settimer();			// 타이머 실행 
	/*
	
	// ------------------------- 이메일 인증 보냈을때 ---------------- //
	// -- 인증요청시 서블릿 통신[ 인증코드 생성 , 이메일전송 ]
	$.ajax({
		url : "/jspweb/AuthSendEmailController" , 
		method : "get" ,
		data : { memail : document.querySelector('.memail').value } , 
		success : r => {  console.log( r );
			
			// 1. 'authbox' div 호출 
			let authbox = document.querySelector('.authbox')
			
			// 2. auth html 구성 
			let html = `<span class="timebox">02:00</span>
						<input class="ecode" type="text" /> 
						<button onclick="auth()" type="button">인증</button> `
			// 3. auth html 대입 
			authbox.innerHTML = html;
			// 4. 타이머 실행
			authcode = r ;				 // [ 이메일전송 ] Controller(서블릿) 에게 전달받은 값이 인증코드 
			timer = 120; 		// [ 테스트용 ] 인증 제한시간 10초 
			settimer();			// 타이머 실행 
		} ,
		error : e => { console.log(e); } 
	})
	*/

} // f end 

// 4번,5번,6번 함수에서 공통적으로 사용할 변수[전역변수]
let authcode = ''; 	// 인증코드 
let timer = 0; 		// 인증 시간(초) 변수 
let timerInter; 	// setInterval() 함수를 가지고 있는 변수 [ setInterval 종료시 필요. ]

// 5. 타이머 함수 만들기 
function settimer(){
	timerInter = setInterval( () => {
		// 시간 형식 만들기 
			// 1. 분 만들기 [ 초 / 60 ] => 분  /  [ 초 % 60 ] => 나머지 초
		let m = parseInt( timer / 60 ); // 분 계산 [ 몫 ] 
		let s = parseInt( timer % 60 ); ; // 초 계산 [ 나머지 ]
			// 2. 두자리수 맞춤  3 -> 03 
		m = m < 10 ? "0"+m : m; // 만약에 분 이 10보다 작으면 한자리수 이므로 0 붙이고 아니면 
		s = s < 10 ? "0"+s : s; 
		
		document.querySelector('.timebox').innerHTML = `${m}:${s}`; // 현재 인증 시간(초) HTML 대입
		timer--; // 1씩 차감
		
		// 만약에 타이머가 0 보다 작으면 [ 시간 끝 ]
		if( timer < 0 ){ 
			// 1. setInterval 종료 [ 누구를 종료할건지 식별자.. 변수 선언 = timerInter ]
			clearInterval( timerInter )
			// 2. 인증실패 알림
			document.querySelector('.emailcheckbox').innerHTML =`인증실패`;
			// 3. authbox 구역 HTML 초기화 
			document.querySelector('.authbox').innerHTML=``;
			checkList[2] = false;
		}
	} , 1000 ); // 1초에 한번씩 실행되는 함수
} // f end 

// 6. 인증요청후 인증코드를 입력하고 인증하는 함수
function auth(){ console.log('auth() open')
	// 1. 입력받은 인증코드
	let ecode = document.querySelector('.ecode').value;
	// 2. 비교[ 인증코드 와 입력받은 인증코드 ]
	if( authcode == ecode ){
		clearInterval( timerInter ); // 1. setInterval 종료
		document.querySelector('.emailcheckbox').innerHTML =`인증성공`; // 2. 인증성공 알림
		document.querySelector('.authbox').innerHTML=``; // 3. authbox 구역 HTML 초기화 
		checkList[2] = true;
	}else{
		// 1. 인증코드 불일치 알림
		document.querySelector('.emailcheckbox').innerHTML =`인증코드 불일치`; checkList[2] = false;
	}
} // f end 

// 7. 닉네임 중복검사
function nicknamecheck(){
	// 1. 값 호출 
	let mnickname = document.querySelector('.mnickname').value; 
	let nicknamecheckbox = document.querySelector('.nicknamecheckbox');
	// 2. 유효성검사 
		let mnickj = /^(?=.*[a-z0-9가-힣])(?![ㅏ-ㅣㄱ-ㅎ]+)[a-z0-9가-힣]{2,12}$/
			// 2. 정규표현식 검사. 
			console.log( mnickj.test( mnickname ) )
			console.log( mnickname )
		if( mnickj.test(mnickname) ){ // 입력한 값이 패턴과 일치하면
			// -- [ 중복검사 ]
			$.ajax({
				url : "/jspteam4/MemberFindController" ,
				method : "get" ,
				data : { type : "mnickname" , data : mnickname },
				success : r => { 
					if( r ){  nicknamecheckbox.innerHTML = '사용중인 닉네임 입니다.'; checkList[3] = false; }
					else { nicknamecheckbox.innerHTML = '사용가능한 닉네임 입니다.'; checkList[3] = true; } 
				} ,
				error : e => { }
			})
		}else{ // 입력한 값이 패턴과 일치하지 않으면
			nicknamecheckbox.innerHTML =' 2자 이상 12자 이하, 영어 또는 숫자 또는 한글로 구성해주세요. 한글 초성 및 모음은 불가합니다.'; checkList[3] = false;
		}
}


let checkList = [ false , false , false , false ] // [0] : 아이디통과여부 , [1] : 패스워드통과여부 , [2] : 이메일통과여부 [3] : 닉네임중복
	// true 통과 , false 비통과 
// 8. 회원가입 메소드 
function signup(){
	// 1. 아이디/비밀번호/이메일 유효성검사 통과 여부 체크 
		console.log( checkList )
	if( checkList[0] && checkList[1] && checkList[2] && checkList [3] ){ // checkList 에 저장된 논리가 모두 true 이면 
		console.log('회원가입 진행가능');
		
		let mid = document.querySelector('.mid').value;  				console.log('signupData에 담길 mid : ' + mid);
		let mpwd = document.querySelector('.mpwd').value;				console.log('signupData에 담길 mpwd : ' + mpwd);
		let memail = document.querySelector('.memail').value;			console.log('signupData에 담길 memail : ' + memail);
		let mnickname = document.querySelector('.mnickname').value; 	console.log('signupData에 담길 mnickname : ' + mnickname);
		
				$.ajax({
					url : "/jspteam4/MemberInfoController" , 
					method: "post" ,			// form 객체 [ 대용량 ] 전송은 무조건 post 방식 
					data : { mid : mid , mpwd : mpwd , memail : memail , mnickname : mnickname } ,	 
					success : r => { 
						if( r ){ // 회원가입성공 [ 1.알린다 2.페이지전환]
							alert('회원가입성공');
							location.href = '/jspteam4/jsp/boardmap/index.jsp';
						}
						else{ // 회원가입실패
							alert('회원가입실패[관리자문의]');
						}
					} ,
					error : e => { console.log(e) } ,
				})
	}else{
		alert('정상적으로 입력 안된 내용이 있습니다.');
	}
} // f end 