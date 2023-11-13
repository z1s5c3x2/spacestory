let receiver;
let search_mno;
const modal = document.getElementById("modal");
let modal_content;
// 게시물 모달 보이기
function showBoardModal() {
	
	modal.style.display = "flex";
	
	let html = '';
	
	html += `
		<div class="modal-window">
            <div class="modal_top">
                <h2 class="modal_title" style="border-bottom: solid 1px gray; padding-bottom: 10px;">게시물 작성</h2>
            </div>
            <div class="close-area btn-close"></div>
            <select class="select_list">
	`;
	
	
	
	
	$.ajax({
		url: '/jspteam4/mapBoardController',
		method: 'get',
		data: { type : 'category' },
		async: false,
		success: r => {
			console.log(r)
			r.forEach(re => {
				html += `<option value="${re.cno}">${re.cname}</option>`;			
			})
			html += `
				            </select>
            <div class="modal_bottom">
            	<input class="title" type="text" placeholder="제목" />
	            <textarea class="content" cols="32" rows="7" placeholder="내용"></textarea>
	            <button onclick="boardWrite()" class="modal_btn" type="button">작성 완료</button>
            </div>
        </div>
			`;
			modal.innerHTML = html;
			let closeBtn = modal.querySelector(".close-area")
			closeBtn.addEventListener("click", e => {
			    modal.style.display = "none"
			})
		},
		erroe: e => { console.log(e) }

	})
}


// 게시물 작성
function boardWrite() {
	
	var latlng = map.getCenter();
	
	let btitle = document.querySelector('.title');
	let bcontent = document.querySelector('.content');
	
	console.log('bocontent[' + bcontent.value + "]")
	
	if(btitle.value == '' || bcontent.value == '') {
		alert('내용을 모두 작성해주시길 바랍니다.')
		return;
	}
	let cno = document.querySelector('.select_list');
	let blat = latlng.getLat();
	let blng = latlng.getLng();
	console.log('cno : ' + cno)
	$.ajax({
		url: '/jspteam4/mapBoardController',
		method: 'post',
		data: { btitle : btitle.value, bcontent : bcontent.value, cno : cno.value, blat : blat, blng : blng, mno : loginInfo.mno },
		success: r => {
			 alert('등록 완료!') 
			btitle.value = '';
			bcontent.value = '';
			cno.value = '1';
			modal.style.display = "none"
		},
		erroe: e => { console.log(e) }

	})
}


// 리뷰 모달 보이기
function showReviewModal(r) {
	
	if(loginInfo.mno == r) {
		alert('본인에겐 리뷰 달기 불가능');
		return;
	}
	
	
	receiver = r;
	
	modal.style.display = "flex";
	
	let html = '';
	
	html += `
		<div class="modal-window">
            <div class="modal_top">
                <h2 class="modal_title" style="border-bottom: solid 1px gray; padding-bottom: 10px;">리뷰 작성</h2>
            </div>
            <div class="close-area btn-close"></div>
            <div> 평점: <select class="select_list">
	`;
	
	for(let i = 1; i <= 5; i++) {
		html += `<option value="${i}">${i}</option>`;	
	}
	
	html += `
				            </select></div>
            <div class="modal_bottom">
	            <textarea class="content" cols="32" rows="7" placeholder="내용"></textarea>
	            <button onclick="reviewWrite()" class="modal_btn" type="button">작성 완료</button>
            </div>
        </div>
			`;
	
	modal.innerHTML = html;
	
	let closeBtn = modal.querySelector(".close-area")
	closeBtn.addEventListener("click", e => {
	    modal.style.display = "none"
	})
	
	
	
}

// 리뷰 작성
function reviewWrite() {
	
	
	let rcontent = document.querySelector('.content');
	let rscore = document.querySelector('.select_list');

	if(rcontent.value == '') {
		alert('내용을 모두 작성해주시길 바랍니다.')
		return;
	}

	$.ajax({
		url: '/jspteam4/ReviewController',
		method: 'post',
		data: { rcontent : rcontent.value, rscore : rscore.value, rreceiver : receiver, rsender : loginInfo.mno },
		success: r => {
			alert('등록 완료!') 
			rcontent.value = '';
			rscore.value = '1';
			modal.style.display = "none"
		},
		erroe: e => { console.log(e) }

	})
}

// 회원 리뷰 read
function showUserReviewModal(mnickname){
	let mnicknamecheck = mnickname; console.log(mnicknamecheck);

	modal.innerHTML = `
		<div class="modal-content"> </div>
	`;
	
	modal_content = document.querySelector(".modal-content");
	
	let html = ``;
	
	console.log(search_mno)
	
	$.ajax({
		url : "/jspteam4/MemberInfoController" , 
		method : "get" , 
		data : { type : "search"  , mnickname: mnickname } ,
		async : false , /* ajax가 응답이 올때까지 대기상태 = 동기식 */
		success : r => { console.log(r);
			search_mno = r.mno;
			console.log(search_mno)
			html += `<div class="review_top_content">
						<div> 조회한 닉네임 : ${r.mnickname}</div>
						<div class="user_avgscore review_content_rscore"></div>
					</div>
					<div class="writer_view">
						<div class="written_post">
							<div class="written_contents">
							
							</div>
							
							<div class="page_box"> 
							
							</div>
						</div>
						<div class="review_box">
							<div class="review_contents">
							
							</div>
							
							<div class="review_pagebox"> 
							
							</div>
						</div>
					</div>`;
			console.log(html)
			modal_content.innerHTML = html;
			
			getLists(1);
			console.log(search_mno);
			getreviews(1);
		}  ,
		
		error: e =>{console.log(e)}
	});
	
	
} // f end 

let pageOjects = { listsize : 3 ,  page : 1  }

let written_post = ``; // 조회한 닉네임이 작성한 게시글 
function getLists(page){
	let	written_postbox = document.querySelector('.written_contents');
	pageOjects.page = page;
	// 클릭된 페이지번호 를 조건객체에 대입
	console.log("search_mno : " + search_mno);
	console.log(pageOjects.page); 
	console.log(pageOjects.listsize);
	$.ajax({
		url:"/jspteam4/BoardInfoController" ,
		method: "get" ,
		async: false,
		data : { type : "search" , mno : search_mno ,'pageOject.page' : pageOjects.page , 'pageOject.listsize' : pageOjects.listsize } ,
		
		success : r => { console.log(r )
			if(r.totalpage == 0) return;
		written_post =`<h2 class="modal_what_content">게시물</h2>`;
		// 가져온 객체 배열을 반복하여 출력
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
            
            //console.log('cno : ' + cno)
			
            // 각 필드를 활용하여 HTML에 추가
            written_post += `
                <div class="written_content">
                	<div class="written_content_btmn">
                    	<h4 class="written_content_btitle">${btitle}</h4>
                    	<div class="written_content_mnickname">작성자: ${mnickName}</div>
                    </div>
                    <div class="written_content_bcontent">${bcontent}</div>
                    <div class="written_content_bdcn">
                    	<div class="written_content_cname">카테고리: ${cname}</div>
                    	<div class="written_content_bdate">작성일: ${bdate}</div>
                    </div>
                </div>
            `;
        });
        // ------------------------------ 2. 페이지번호 출력  -------------------- // 
			
			 let html = ``; // 위에서 사용된 html 초기화
			
			// 페이지 개수만큼 페이징번호 구성 
				// page : 조회한 페이지번호 [ 현재 보고 있는 페이지번호 ]
				// 이전 버튼 [ page <= 1 ? page : page-1   만약에 1페이지에서 이전버튼 클릭시 1페이지로 고정 하고 아니면 1차감 ]
				html += `<button onclick="getLists(${ page <= 1 ? page : page-1 })" type="button"> < </button>`
				
				// 페이지번호 버튼 [ * 마지막 페이지 까지 반복 ]
				for( let i = r.startbtn ; i<= r.endbtn ; i++ ){
					// class="${ page == i ? 'selectpage' : '' }"
					// 만약에 현재페이지(page) 와 i 같으면 버튼태그에 class="selectpage" 추가  
					html += `<button class="${ page == i ? 'selectpage' : '' }" onclick="getLists(${i})" type="button"> ${i} </button>`
				}
				
				console.log(page >= r.totalpage ? r.totalpage : r.endbtn+1)
				console.log(r.totalpage)
				
				// 다음 버튼 [ page >= pageDto.totalpage ? page : page+1  만약에 현재페이지가 마지막페이지이면 고정 아니면 1증가 ]
				html += `<button onclick="getLists(${ page >= r.totalpage ? r.totalpage : r.endbtn+1 })" type="button"> > </button>`;
						
			// page_box 구역에 구성된 html 대입 
			written_postbox.innerHTML = written_post
			document.querySelector('.page_box').innerHTML = html;
		console.log(html);
        // 결과를 written_postbox에 추가
		} ,
		error : e => {} 
	});
}

let review_pageOject = { listsize : 3 ,  page : 1  }
let review_post = ``;
function getreviews(page){
	console.log(page);
	let review_postbox = document.querySelector('.review_contents');
	let user_avgscore = document.querySelector('.user_avgscore');
	review_pageOject.page = page;
	// * form 전송 ajax 
	console.log (search_mno);
	$.ajax({
		url : "/jspteam4/ReviewController",
		method : "get",
		async: false,
		data : { type : "search" , mno : search_mno ,'reviewpageOject.page' : review_pageOject.page , 'reviewpageOject.listsize' : review_pageOject.listsize} ,
		success : r => { console.log(r)
			if(r.totalpage != 0) {
				review_post=`<h2 class="modal_what_content">리뷰</h2>`;
				let total_score = 0;
				r.boardList.forEach(item => {
		            let rno = item.rno;
		            let rcontent = item.rcontent;
		            let rdate = item.rdate;
		            let rsender_mnickname = item.rsender_mnickname;
		            let rreceiver_mnickname = item.rreceiver_mnickname;
		            let rscore = item.rscore;
					
		            // 각 필드를 활용하여 HTML에 추가
		            review_post += `
		            	
		            	<div class="review_content">
		                	<div class="review_content_rsrs">
		                		<div class="review_content_rsender">작성자: ${rsender_mnickname}</div>
		                		<div class="review_content_rscore">평점: ${rscore}점</div>
		                	</div>
		                    <div class="review_content_rcontent">리뷰내용:${rcontent}</div>       
		                    <div class="review_content_rdate">작성일: ${rdate}</div>
		                </div>
		            
		            `;   
		            total_score += rscore;
		        }); 
		     	let avgscore = total_score / r.boardList.length;
		     	user_avgscore.innerHTML = `평균 점수 : ${Math.round((avgscore + Number.EPSILON) * 10) / 10}점`;
		        review_post += ``;
		        // ------------------------------ 2. 페이지번호 출력  -------------------- // 
				
				 let html = ``; // 위에서 사용된 html 초기화
				
				// 페이지 개수만큼 페이징번호 구성 
				// page : 조회한 페이지번호 [ 현재 보고 있는 페이지번호 ]
				// 이전 버튼 [ page <= 1 ? page : page-1   만약에 1페이지에서 이전버튼 클릭시 1페이지로 고정 하고 아니면 1차감 ]
				html += `<button onclick="getreviews(${ page <= 1 ? page : page-1 })" type="button"> < </button>`
				
				// 페이지번호 버튼 [ * 마지막 페이지 까지 반복 ]
				for( let i = r.startbtn ; i<= r.endbtn ; i++ ){
					// class="${ page == i ? 'selectpage' : '' }"
					// 만약에 현재페이지(page) 와 i 같으면 버튼태그에 class="selectpage" 추가  
					html += `<button class="${ page == i ? 'selectpage' : '' }" onclick="getreviews(${i})" type="button"> ${i} </button>`
				}
				
				// 다음 버튼 [ page >= pageDto.totalpage ? page : page+1  만약에 현재페이지가 마지막페이지이면 고정 아니면 1증가 ]
				html += `<button onclick="getreviews(${ page >= r.totalpage ? page : page+1 })" type="button"> > </button>`;
						
				console.log("review_post : " + review_post)
				// review_pagebox 구역에 구성된 html 대입 
				review_postbox.innerHTML = review_post
				document.querySelector('.review_pagebox').innerHTML = html;
				console.log(html);
				
			}
			modal.style.display = "flex";
       } ,
		error : e => { }
	});	
}




// 모달 ----------------------------------------

// 다른 구역 누르면 모달창 꺼짐
modal.addEventListener("click", e => {
    const evTarget = e.target
    if(evTarget.classList.contains("modal-overlay")) {
        modal.style.display = "none"
    }
})
 
// esc 버튼 누르면 모달창 꺼짐
window.addEventListener("keyup", e => {
    if(modal.style.display === "flex" && e.key === "Escape") {
        modal.style.display = "none"
    }
})