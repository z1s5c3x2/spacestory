



// 1. 로그인 함수 

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
		success : r => { 
			if( r!=false ){
				let loginInfo = {
					mno : r.mno,
					mid : r.mid,
					memail : r.memail,
					mnickname : r.mnickname
				};
				
				sessionStorage.setItem('loginInfo', JSON.stringify(loginInfo));
				location.href="/jspteam4/jsp/boardmap/map.jsp";
				

				
			}
			else{ 
					document.querySelector('.logincheckbox')
						.innerHTML = '동일한 회원정보가 없습니다.'
						
						; 
				}
		} ,
		error : e => { console.log(e); } 
	});

}
	