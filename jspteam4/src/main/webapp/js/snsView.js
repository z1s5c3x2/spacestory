
function onDelete(sno) {
	let spwd = prompt('비밀번호를 입력해주세요.');

	$.ajax({
		url: "/jspteam4/snsController",
		method: "delete",
		data: { sno: sno, spwd: spwd },
		success: r => {

			if (r) {
				alert('삭제가 완료되었습니다.');snsView();
				

			} else {
				alert('비밀번호가 다릅니다.')
			}
			snsView()
		},
		error: e => {
			console.log('onDelete 에러 : ' + e);
		}
	})
}

function commentDelete(cno)
{
	$.ajax({
		url: "/jspteam4/CommentController",
		method: "delete",
		data: { cno: cno, cpwd: prompt('비밀번호 입력') },
		//data: { cno: cno, cpwd: 'asd1234' },
		success: r => {
			console.log(r)
			if(r)
			{
				alert('삭제되었습니다')
				snsView()
			}else{
				alert('비밀번호가 틀렸습니다')
			}
			
			
		},
		error: e => {
			console.log('답글 삭제  에러 : ' + e);
		}
	})
}
function boardRender(r)
{
	console.log(r)
	
	let feedbox = document.querySelector('.feedbox')
	let html = ``;
	let	commenthtml = ``;
		r.forEach(r=>{
			
					// 서버에서 받아온 본문 내용
      				let postContentFromServer = r.scontent; 

       				// 줄 바꿈 문자(\r\n)를 <br> 태그로 변환하여 HTML에 추가
        			let scontent = postContentFromServer.replaceAll('\r\n', "<br>");
					console.log (scontent);
			
			if(r.commentList != null)
				{r.commentList.forEach(s => {
					
					
					
				commenthtml += `<div class="comment">
								<div class="ccon">${s.content}</div>
								<div class="comright">
									<div class="cdate">${s.cdate}</div>
									<button class="combtn" onclick="commentDelete(${s.cno})" type="button">X</button>
								</div>
						</div>`
				})}
				

				html += `<div class="feed feeditem${r.sno}">
							<div class="img">
								<img class ="img" src = "/jspteam4/upload/sns/${r.simg}"/>
							</div>
							<div class="content">
								<div class="scon">${r.sdate}</div>
								${scontent}
							</div>
							

				<div class="feed_bottom">
					<div class="btnbox">
						<button onclick="onUpdate(${r.sno})" type="button">수정</button>
						<button onclick="onDelete(${r.sno})" type="button">삭제</button>
						<button onclick="commentWrite(${r.sno})" type="button">답글</button>
						<button onClick="fileDownload('${r.simg}')" type="button"> 다운 로드 </button>
					</div>
					<div class="good_bad">
						<button onclick="goodClick(${r.sno})">👍</button> <span class="good"> ${r.good} </span> <button onclick="hateClick(${r.sno})">👎</button> <span class="hate"> ${r.hate} </span>
					</div>
				<div>
				
				<div class="commentbox">${commenthtml}</div>
			</div>`
			
			feedbox.innerHTML = html;
			commenthtml = ``
			})
}
function fileDownload(_f)
{
	location.href ='/jspteam4/FileService?imgName='+_f
	
	
}
snsView();
function snsView() {


	$.ajax({
		url: "/jspteam4/snsController",
		method: "get",
		data: { type: 1 },
		success: r => {
				console.log(r)
			document.querySelector('.viewSearch').innerHTML = `총 게시물 수 ${r.length}`
			console.log(r)
			boardRender(r)
			
			// ------------------------- 2. 답글 출력 -------------------------
			
		},
		error: e => { console.log(e) }


	})
}

 function onUpdate(sno)
 {
    location.href = `/jspteam4/jsp/snsUpdate.jsp?sno=${sno}`
 }

 
 // 답글 작성 
 function commentWrite(sno) {
	 let ccontent = prompt('댓글을 작성해주세요')
	 let cpwd = prompt('비밀번호를 작성해주세요')
	 console.log(ccontent, cpwd)
	 $.ajax({
		url: "/jspteam4/CommentController",
		method: "post",
		data: { sno: sno, cpwd: cpwd, ccontent : ccontent },
		success: r => {
			if (r) {
				alert('댓글 작성이 완료되었습니다.')
				// 보여주기 함수 
			} else {
				alert('댓글 작성에 실패하였습니다.')
			}
			snsView()
		},
		error: e => {
			console.log('onDelete 에러 : ' + e);
		}
	})
 }




function search() {
	let search = document.querySelector('.search').value;
	let html = ` `;
	$.ajax({
		url: "/jspteam4/CommentController",
		method: "get",
		data: { type: 2, search: search },
		success: r => {
			
			let feedbox = document.querySelector('.feedbox')
			if(search==' '){snsView();}
			else{
				boardRender(r)
				 viewSearch(search);
				
		
			}	
		}, //s end
		error: e => { console.log("통신오류 :" + e) }
	})
}



function viewSearch(search){
	let viewSearch = document.querySelector('.viewSearch');
	let html = ``;
	$.ajax({
		url : "/jspteam4/CommentController",
		method : "get",
		data : {type : 3, search:search},
		success : r=>{
			
			html += `<div class="viewSearch">검색된 피드 : ${r.searchsns} </div>`
			viewSearch.innerHTML=html;
		},
		error : e=>{console.log("통신오류 :"+e)}
	})
}


// ----------------------- 좋아요 표시 ----------------------------
function goodClick(sno) {
	let good = localStorage.getItem('good');
	
	if(good != null) {
		alert('이미 좋아요를 눌렀습니다.');
		return;
	}else{
		localStorage.setItem('good', true);
	}
	$.ajax({
		url : "/jspteam4/snsController",
		method : "put",
		data : {type : 'good', sno:sno},
		success : r=>{
			let good = document.querySelector('.feeditem'+sno).querySelector('.good').innerHTML;
			document.querySelector('.feeditem'+sno).querySelector('.good').innerHTML = parseInt(good)+1;
		},
		error : e=>{console.log("통신오류 :"+e)}
	})
}

function hateClick(sno){
	let hate = localStorage.getItem('hate');
	if(hate != null) {
		alert('이미 싫어요를 눌렀습니다')
		return;
	}else{
		localStorage.setItem('hate', true);
	}
	$.ajax({
		url : "/jspteam4/snsController",
		method : "put",
		data : {type : 'hate', sno : sno},
		success : r=>{
			let hate = document.querySelector('.feeditem'+sno).querySelector('.hate').innerHTML;
			document.querySelector('.feeditem'+sno).querySelector('.hate').innerHTML = parseInt(hate)+1;
		},
		error : e=>{console.log("통신오류 :"+e)}
	})
}

