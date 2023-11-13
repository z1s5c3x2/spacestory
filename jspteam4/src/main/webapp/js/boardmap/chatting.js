
let loginInfo = JSON.parse(sessionStorage.getItem('loginInfo'));

let userList = [];
let clientSocket = null;
loginMid=loginInfo.mid;

function host(bno){
	let chatting = document.querySelector('.chatting')
	let html = ``;
	$.ajax({
		url : "/jspteam4/mapBoardController",
		method:"get",
		data : {type:"exist",bno:bno},
		async: false,
		success: r=>{console.log(r)
			if(r){	html = `<p>
					<button class="btn btn-primary" type="button" 
					data-bs-toggle="collapse" data-bs-target="#collapseWidthExample"
					aria-expanded="false" aria-controls="collapseWidthExample" onclick="roomManager(${bno})">
					채팅방생성하기</button>
							</p>`
				chatting.innerHTML=html;}//if end
			else{html = `<p>
					<button class="btn btn-primary" type="button"
					data-bs-toggle="collapse" data-bs-target="#collapseWidthExample"
					aria-expanded="false" aria-controls="collapseWidthExample" onclick="Openchatting(${bno})">
					채팅방입장하기</button>
						</p>`
				chatting.innerHTML=html
		}//else end
		},//success end
		error : e=>{console.log("host error"+e)}
	})
}


function roomManager(bno){
	$.ajax({
			url : "/jspteam4/mapBoardController",
			method : "get",
			data : {type:"manager",loginMid:loginMid,bno:bno},
			async: false,
			success : r=>{console.log(r)
				if(r){Openchatting(bno);boardState(bno);
				alert("방장입니다."); }
				else{
					alert("방장만 방을 만들수있습니다."); }
			},
			error : e=>{console.log("roomManager오류 : "+e)}
		})

}
	
// 채팅방 생성시 db에서 상태 변경해주기
function boardState(bno){
		$.ajax({
			url : "/jspteam4/mapBoardController",
			method : "get",
			data : {type:"State",bno:bno},
			success : r=>{console.log(r)},
			error : e=>{console.log("boardState:"+e)}
		})

}

// 2. JS 클라이언트[유저] 소켓 만들기 (방회원)

function Openchatting(bno){
		OpenchattingHtml()

	if( clientSocket != null )clientSocket.close()
	clientSocket= null;
	clientSocket = new WebSocket(`ws://192.168.17.13/jspteam4/serversokcet/${loginMid}/${bno}`);
	clientSocket.onopen = e =>{
		let msgs ={ bno:bno,  type : "room" , content :`${bno}번방 입장했습니다.`}
		let msg ={ bno:bno,type : "alarm" , content :`${loginMid}님이 입장했습니다.`}
		clientSocket.send(JSON.stringify(msgs) );
		clientSocket.send(JSON.stringify(msg) );
	
	} ;

	clientSocket.onerror =  e =>{console.log('서버와 오류발생 :'+e)} ; 

	clientSocket.onclose = e =>{console.log('서버와 연결 끊김 :'+e)} ;
		
	clientSocket.onmessage = e => 
	onMsg( e,bno ) ;
}

//채팅방 js
function OpenchattingHtml(bno){
	
	let webcontainer2 = document.querySelector('.webcontainer2');
	let chatting = document.querySelector('.chatting');
	
	let chattingHtml=``;
	let webHtml = `
		<div class="topcontainer">
			<div class="manager">채팅방유저</div>
			<div class="chatcont"></div>
		</div>	
			<div class="chatbottom" >
				<textarea onkeyup="onEnterKey()" class="msg"> </textarea>
				<button onclick="onSend()" type="button">전송</button>
				<button onclick="Closechatting()" type="button">나가기</button>
			</div>
			<div class="dropdown ">
				<button class="emobtn" type="button" data-bs-toggle="dropdown"
					aria-expanded="false">
					<i class="far fa-smile-wink"></i>
				</button>
				<ul class="dropdown-menu emolistbox">
				</ul>
			</div>`;
	


    webcontainer2.innerHTML = webHtml;
    chatting.innerHTML = chattingHtml;
    
    // user 요소에 HTML을 설정
 
   
 		getEmo();
}


function Closechatting() {
	//나간 회원을 찾아서 배열에 삭제하기
	
	//user();
	closeSend()
	
	 let webcontainer2 = document.querySelector('.webcontainer2');
	 
	let  html =` <div class="chatting" >
			
				</div>`;
	 webcontainer2.innerHTML=html;

}

function closeSend(){
	let msg = {type:'close',content:loginInfo.mid , user:userList};
	clientSocket.send(JSON.stringify(msg) ); 
}


// 3. 서버에게 메시지 전송
function onSend(bno){
	//3-1 textarea 입력값 호출
	let msaVlaue = document.querySelector('.msg').value;
	
	if(msaVlaue==''||msaVlaue=='\n'){alert('내용을 입력해주세요.');
	document.querySelector('.msg').value=``;
	return;}
	
	let msg = {bno:bno, type : 'message', content : msaVlaue}
	
	
	//3-2 메시지 전송
	clientSocket.send(JSON.stringify(msg) ); // 클라이언트소켓과 연결된 서버소켓에게 메시지 전송 -->
					//JSON타입을 String 타입으로 변환해주는 함수 []
	//3-3 메시지 성공시 입력상자 초기화
	document.querySelector('.msg').value=``;
	

}


//4. 메시지를 받았을때 추후 행동(메소드) 선언
function onMsg(e,bno){
	let msgBox = JSON.parse(e.data);
console.log(e.data);
	msgBox.msg = JSON.parse(msgBox.msg);
	
	msgBox.msg.content=msgBox.msg.content.replace(/\n/g,'<br>')
	
	let chatcont =document.querySelector('.chatcont')
	
	let html=``;
	
		if( msgBox.msg.type==`alarm`){
			html =`${typeHTML( msgBox.msg )}`;
			
		}
		else if(msgBox.msg.type==`close`){
			html =`${typeHTML( msgBox.msg )}`;
			
			}
		
		else if(msgBox.msg.type==`room`){
			html =`${typeHTML( msgBox.msg )}`;
		}
		
		else if(msgBox.msg.type==`roomUser`){
			userList = msgBox.msg.content.slice(1,-1).split(",");
			userListBox(userList)
		}
		
		
		else if(msgBox.frommid==loginMid){
			 html = `<div class="rcont"> 
					<div class="subcont">
						<div class="date"> ${msgBox.date} </div>
						${typeHTML( msgBox.msg )}
					</div>
				</div>`;
			
		}else { 
		
			 html = `<div class="lcont"> 
			 
					<div class="tocont">
						<div class="name">${msgBox.frommid}</div> 
						<div class="subcont">
							${typeHTML( msgBox.msg )}
							<div class="date"> ${msgBox.date} </div>
						</div>
					</div>
				</div>`;
				
		}
	 
	
	chatcont.innerHTML += html;
	

	chatcont.scrollTop = chatcont.scrollHeight;
}


function userListBox(userList) {
	let manager=document.querySelector('.manager');
	let html = `<div class="user">채팅방유저</div><br>`;
	for(let i = 0 ; i<userList.length ; i++){
		html+=`${userList[i]}<br>`
	}
	manager.innerHTML=html
	console.log(userList)
	
}
function onEnterKey () {
	if (window.event.keyCode==13 && window.event.ctrlKey){
	document.querySelector('.msg').value+=`\n`;return;}
	if(window.event.keyCode==13){onSend();return;}


}


function getEmo(){

	for (let i = 1 ; i<= 43 ; i++){
		document.querySelector('.emolistbox').innerHTML
		+=`	<img onclick ="onEmoSend(${i})"
		src="/jspteam4/img/imoji/emo${i}.gif" />`;
	}
}


function onEmoSend(i){
	let msg ={type : 'emo', content : i +"" };
	clientSocket.send(JSON.stringify(msg));
}
	


function typeHTML( msg ){
	let html =``;
	
	
	// 1. 메시지 타입 일때는 <div> 반환
	if(msg.type =='message'){
		html +=`<div class="content"> ${msg.content} </div>`;
	}
	
	// 2. 이모티콘 타입 일때는 <img> 반환
	else if(msg.type== 'emo'){
		html +=`<img src = "/jspteam4/img/imoji/emo${msg.content}.gif"`;
	}
	else if(msg.type=='alarm'){
		html +=`<div class="alarm"> ${msg.content} </div>`;
	}
	else if(msg.type=='room'){
		html +=`<div class="alarm"> ${msg.content} </div>`;
	}
	else if(msg.type=="close"){
		html +=`<div class="alarm"> ${msg.content}님이 퇴장했습니다. </div>`;
	}
	
	// 3. 만약에 알림 타입일때는 <div>를 반환해준다.
	return html;
}
	
