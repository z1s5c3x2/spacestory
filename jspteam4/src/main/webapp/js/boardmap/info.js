let mnickname = "";  // 회원검색시 입력창에 입력한 닉네임값을 저장하는 변수

let searchresultbox = document.querySelector('.searchresultbox') // map.jsp에서 닉네임검색 후 사이드바로 출력해줄 구역
let writtenpostbox; // 회원검색하는 searchinfo() 안에 mnickname으로 찾은 mno로 작성된 게시글을 담는 변수

let searchmno; // 검색할 mno 
let reviewpostbox; // 회원검색하는 searchinfo() 안에 mnickname으로 찾은 mno로 작성된 리뷰를 담는 변수

// 1.로그인된 회원의정보 호출
getInfo();
function getInfo(){
	$.ajax({
		url : "/jspteam4/MemberInfoController" , 
		method : "get" , 
		data : { type : "info" } ,
		success : r => { 
			if( r == null ){ // 비로그인 상태 -> 페이지 차단
				alert('회원전용 페이지입니다.[로그인]페이지로 이동');
				location.href="/jspteam4/jsp/boardmap/index.jsp";
			}else{ // 로그인 상태 -> 마이페이지 구역에 정보 넣어주기 
				document.querySelector('.mid').innerHTML = r.mid;
				document.querySelector('.mnickname2').innerHTML =r.mnickname;
				document.querySelector('.memail').innerHTML = r.memail;
			}
		}
	});
} // f end 
// 2. 회원정보 수정( 닉네임 , 비밀번호 ) 
function mupdate(){
	
	let NewNickname = document.querySelector('.mnickname').value;  console.log('NewNickname : ' + NewNickname); // 새롭게 변경할 닉네임을 담은 변수
	let NewPassWord = document.querySelector('.mpwd').value;		console.log('NewPassWord : ' + NewPassWord); // 새롭게 변경할 비밀번호를 담은 변수
	console.log('mupdate 안 checkList 확인' + checkList)
	if( ( ( NewPassWord == '' ) && checkList [3] ) || ( checkList[1] && checkList[3]) || ( checkList[1] &&  NewNickname == '' )   ){ //signup.js에 있는 유효성검사 사용 ( 조건 충족시만 수정 가능)
		console.log('수정 진행가능');
		// * form 전송 ajax 
		$.ajax({
			url : "/jspteam4/MemberInfoController",
			method : "put",
			data : {mnickname : NewNickname , mpwd : NewPassWord} ,
			// form 전송타입 : 문자X jsonX , 첨부파일 [o]
			/*
				HTTP 전송타입
						1. text/html			: 문자타입 [ 기본값 ]
						2. application/json		: json타입
						3. multipart/form-data	: 대용량 form 전송 지원
			
			*/
			
			success : r => { 
				if(r){
					alert('수정 성공');
					logout();
				}else{
					alert('수정 실패');
				}
			} ,
			error : e => { }
		});		
	}else{ alert('수정 실패 : 입력 조건을 만족해주세요.')}
}
// 3. 회원탈퇴
function mdelete(){ 
	// 1. 탈퇴여부 확인    confirm() 확인true/취소false 버튼 알림창 
	let dconfirm = confirm('정말 탈퇴하시겠습니까?');
	// 2. 확인 버튼을 눌렀을때.
	if( dconfirm == true ){
		let mpwd = prompt('비밀번호 확인');
		// 3. ajax  [ 입력받은 패스워드 전송해서 로그인된회원(서블릿세션) 의 패스워드 와 입력받은 패스워드가 일치하면 탈퇴]
		$.ajax({
			url : "/jspteam4/MemberInfoController" , 
			method : "delete" , 
			data : { mpwd : mpwd } ,
			success : r => { 
				if(r){ alert('회원탈퇴 했습니다.'); logout(); }
				else{ alert('패스워드가 일치하지 않습니다'); } 
			}
		})
	}
}
	
// 4. 닉네임으로 유저( 작성한 게시글 , 해당 유저리뷰 )검색
function searchinfo(){
    mnickname = document.querySelector('.searchbar2').value;
    console.log(mnickname);

    $.ajax({
        url: "/jspteam4/MemberInfoController",
        method: "get",
        data: { type: "search", mnickname: mnickname },
        async: false, /* ajax가 응답이 올때까지 대기상태 = 동기식 */
        success: r => {
            console.log('result 객체1 : ' + r);
            if (!r || !r.mno) {
                // 응답이 없거나 mno가 비어있을 때 처리 , DB에 존재하지 않는 닉네임 조회 시도할때
                let errorMessage = `<div class="nicknameandbtn">
                					<div class="errorMessage">조회할 수 없는 닉네임입니다.</div>
               						<button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
               						</div>`;
                searchresultbox.innerHTML = `<div class="error-message">${errorMessage}</div>`;
            } else { // 정상적으로 DB에 있는 닉네임을 조회 성공했을때 
                searchmno = r.mno;
                getList(1);
                console.log(searchmno);
                getreview();

                let html = `
                    <div class="nicknamebox">
                        <div class="nicknameandbtn">
                            <div> <h2>조회한 닉네임 : ${r.mnickname}</h2></div>
                            <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                        </div>
                    </div>
                    <div class="writereview">
                        <div class="writtenpost"></div>
                        <div class="reviewbox"></div>
                    </div>
                `;

                searchresultbox.innerHTML = html;
                writtenpostbox = document.querySelector('.writtenpost');
                reviewpostbox = document.querySelector('.reviewbox');
            }
        },
        error: e => {
            // 에러 처리를 여기에 추가하세요.
        }
    });
} // f end


let pageOject = { listsize : 5 ,  page : 1  } // 게시물 페이징처리 할때 사용될 한 페이지게 출력할 게시글 수 , 페이지 기본값 

let writtenpost = ``; // 조회한 닉네임이 작성한 게시글 
function getList(page){ // 페이징 처리 후 각 페이지에 맞는 게시물 리스트 가져오는 함수

	pageOject.page = page;
	// 클릭된 페이지번호 를 조건객체에 대입
	console.log(searchmno);
	$.ajax({
		url:"/jspteam4/BoardInfoController" ,
		method: "get" ,
		data : { type : "search" , mno : searchmno ,'pageOject.page' : pageOject.page , 'pageOject.listsize' : pageOject.listsize } ,
		
		success : r => { console.log(r )
		writtenpost =``;
		// 가져온 객체 배열을 반복하여 출력
		writtenpost += `
        			<div class="writtenposttop">
        				<div> 조회한 유저가 작성한 게시글</div>
        			</div>`
        r.boardList.forEach(item => {
            let bcontent = item.bcontent;
            let bdate = item.bdate;
            let blat = item.blat;
            let blng = item.blng;
            let bno = item.bno;
            let btitle = item.btitle;
            let cname = item.cname;
            let cno = item.cno;
            let mnickName = item.mnickName;
            let mno = item.mno;
			
            // 각 필드를 활용하여 HTML에 추가
            writtenpost += `
                <div class="writtencontent">
                	<div class="writtencontentbtmn">
                    	<h4 class="writtencontentbtitle">${btitle}</h4>
                    	<div class="writtencontentmnickname">작성자: ${mnickName}</div>
                    </div>
                    <div class="writtencontentbcontent">${bcontent}</div>
                    <div class="writtencontentbdcn">
                    	<div class="writtencontentcname">카테고리: ${cname}</div>
                    	<div class="writtencontentbdate">작성일: ${bdate}</div>
                    </div>
                </div>
            `;
        });
        writtenpost += `<div class="pagebox"> </div>`;
       
        // ------------------------------ 2. 페이지번호 출력  -------------------- // 
			
			 let html = ``; // 위에서 사용된 html 초기화
			
			// 페이지 개수만큼 페이징번호 구성 
				// page : 조회한 페이지번호 [ 현재 보고 있는 페이지번호 ]
				// 이전 버튼 [ page <= 1 ? page : page-1   만약에 1페이지에서 이전버튼 클릭시 1페이지로 고정 하고 아니면 1차감 ]
				html += `<button onclick="getList(${ page <= 1 ? page : page-1 })" type="button"> < </button>`
				
				// 페이지번호 버튼 [ * 마지막 페이지 까지 반복 ]
				for( let i = r.startbtn ; i<= r.endbtn ; i++ ){
					// class="${ page == i ? 'selectpage' : '' }"
					// 만약에 현재페이지(page) 와 i 같으면 버튼태그에 class="selectpage" 추가  
					html += `<button class="${ page == i ? 'selectpage' : '' }" onclick="getList(${i})" type="button"> ${i} </button>`
				}
				
				// 다음 버튼 [ page >= pageDto.totalpage ? page : page+1  만약에 현재페이지가 마지막페이지이면 고정 아니면 1증가 ]
				html += `<button onclick="getList(${ page >= r.totalpage ? page : page+1 })" type="button"> > </button>`;
						
			// pagebox 구역에 구성된 html 대입 
			writtenpostbox.innerHTML = writtenpost
			document.querySelector('.pagebox').innerHTML = html;
		
        // 결과를 writtenpostbox에 추가
		} ,
		error : e => {} 
	});
}

let reviewpageOject = { listsize : 5 ,  page : 1  } // 리뷰 페이징처리 할때 사용될 한 페이지게 출력할 게시글 수 , 페이지 기본값 
let reviewpost = ``;
function getreview(page){ // 페이징 처리 후 각 페이지에 맞는 리뷰 리스트 가져오는 함수
	// * form 전송 ajax 
	console.log (searchmno);
	$.ajax({
		url : "/jspteam4/ReviewController",
		method : "get",
		data : { type : "search" , mno : searchmno ,'reviewpageOject.page' : reviewpageOject.page , 'reviewpageOject.listsize' : reviewpageOject.listsize} ,
		success : r => { console.log(r)
		reviewpost=``;
		reviewpost += `
        			<div class="reviewposttop">
        			</div>`
        	let totalScore = 0; // 평균 평점 계산시 필요한 전체 평점 합계 
        	let itemCount = r.boardList.length; // 평균 평점 계산시 필요한 전체 리뷰게시물 수 
		r.boardList.forEach(item => { // 반복문 , 전체 리뷰게시물을 출력  
            let rno = item.rno; // 리뷰 rno 식별 고유넘버
            let rcontent = item.rcontent; // 리뷰 내용
            let rdate = item.rdate; // 리뷰 작성일 
            let rsender = item.rsender; // 리뷰 작성자
            let rreceiver = item.rreceiver; // 리뷰 받는사람
            let rsender_mnickname = item.rsender_mnickname; // 리뷰 작성자 닉네임
            let rscore = item.rscore; // 리뷰 1개 평점
			let itemCount = r.boardList.length; // 평균 평점 계산시 필요한 전체 리뷰게시물 수  
            // 각 필드를 활용하여 HTML에 추가
            reviewpost += `
                <div class="reviewcontent">
                	<div class="reviewcontentrsrs">
                		<div class="reviewcontentrsender">작성자: ${rsender_mnickname}</div>
                		<div class="reviewcontentrscore">평점: ${rscore}점</div>
                	</div>
                    <div class="reviewcontentrcontent">${rcontent}</div>       
                    <div class="reviewcontentrdate">작성일: ${rdate}</div>
                </div>
            `;   
            totalScore += rscore; // 리뷰 1개씩의 평점을 누적 더하기 
        }); 
     	let averageScore = totalScore / itemCount; // 평균 평점 구하는 변수 식
       
        let reviewtop = `<div>조회한 유저평가</div>
        				<div class="reviewcontentrscore">평균평점: ${averageScore}점</div>`
        				
        
        reviewpost += `<div class="reviewpagebox"> </div>`;
        
        // ------------------------------ 2. 페이지번호 출력  -------------------- // 
			
			 let html = ``; // 위에서 사용된 html 초기화
			
			// 페이지 개수만큼 페이징번호 구성 
				// page : 조회한 페이지번호 [ 현재 보고 있는 페이지번호 ]
				// 이전 버튼 [ page <= 1 ? page : page-1   만약에 1페이지에서 이전버튼 클릭시 1페이지로 고정 하고 아니면 1차감 ]
				html += `<button onclick="getreview(${ page <= 1 ? page : page-1 })" type="button"> < </button>`
				
				// 페이지번호 버튼 [ * 마지막 페이지 까지 반복 ]
				for( let i = r.startbtn ; i<= r.endbtn ; i++ ){
					// class="${ page == i ? 'selectpage' : '' }"
					// 만약에 현재페이지(page) 와 i 같으면 버튼태그에 class="selectpage" 추가  
					html += `<button class="${ page == i ? 'selectpage' : '' }" onclick="getreview(${i})" type="button"> ${i} </button>`
				}
				
				// 다음 버튼 [ page >= pageDto.totalpage ? page : page+1  만약에 현재페이지가 마지막페이지이면 고정 아니면 1증가 ]
				html += `<button onclick="getreview(${ page >= r.totalpage ? page : page+1 })" type="button"> > </button>`;
						
			// pagebox 구역에 구성된 html 대입 
			reviewpostbox.innerHTML = reviewpost
			document.querySelector('.reviewposttop').innerHTML=reviewtop;
			document.querySelector('.reviewpagebox').innerHTML = html;
       } ,
		error : e => { }
	});	
}