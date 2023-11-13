var markers = []; // 마커 객체 배열  
	// 1.현재 접속한 클라이언트[브라우저]의 위치 좌표 구하기 

var map;
var customOverlay= null
var clusterer;
let cateList = [];
let pageInfo = {page : 0 ,viewByPage : 5,cno : 0,totalPage:0,totalBoard:0}
//마커 표시 시 모든 마커들을 깔끔하게 표시하기 위한 범위 객체
var focusBounds = new kakao.maps.LatLngBounds();
const markerDefaultSize = new kakao.maps.Size(24, 35);
let focusCenter;
const ctlStr = {
	'%Y': '년',
	'%m' : '월',
	'%d' : '일',
	'%H': '시'
}
let ctx = null;
function get3rankBoard() {
	$.ajax({
		url: '/jspteam4/mapBoardController',
		method: 'get',
		data: { mid: loginMid, type: "3Board", nowlat: loginlat, nowlng: loginlng },
		success: s => {
			let mpos = new kakao.maps.LatLng(s.blat, s.blng)
			var imageSrc = `/jspteam4/img/markers/${s.cno}.png`
	    			var markerImage = new kakao.maps.MarkerImage(imageSrc, markerDefaultSize);
					
					let _m = new kakao.maps.Marker({
						position: mpos,
						clickable: true,
						image : markerImage
					});
					_m.setMap(map)
			markers.push(_m)
			viewOverlay(s)
			
		

			
		},
		error: e => {
			console.log(e)
			console.log('왜')
		}
	})
}

function chartCancle()
{
	if (ctx != null) {
		ctx.destroy()
	}
	document.getElementById('chatWrap').style.display = 'none';

}
	
function chartView()
{
	
	if (ctx != null) {
		ctx.destroy()
	}


	$.ajax({
		url: "/jspteam4/mapBoardController",
		method: "get",
		async: false,
		data: { 
			
			type: "chart",
			ha: focusBounds.ha, oa: focusBounds.oa, pa: focusBounds.pa, qa: focusBounds.qa,
			standard:document.querySelector('.chartTimeList').value,cno:document.querySelector('.chartCategoryList').value
		},
		success: r => {
			if (r.length == 0) {
				alert('범위 내 게시글이 없습니다')
				if (ctx != null) {
					ctx.destroy()
				}
				return
			}
			let data = {
				datasets: [{
					data: [],
					 label: '작성수',
				}],

				// 이 라벨들은 다른 호들을 호버했을 때 툴팁과 범례 안에서 표현됩니다
				labels: [
				]
			};
			r.forEach(tmp =>{
				let lt = tmp.split(',')
				data.datasets[0].data.push(Math.round(lt[1]))
				
				data.labels.push(lt[0]+ctlStr[document.querySelector('.chartTimeList').value])
			})

			var options = {
				plugins: {

					title: {
						display: true,
						text: '게시글 통계'
					},
					legend: {
						display: true,
						position: 'bottom',
						labels: {
							boxWidth: 20,
							fontColor: '#111',
							padding: 15
						}
					},
					tooltips: {
						enabled: false
					},

					datalabels: {
						color: '#111',
						textAlign: 'center',
						font: {
							lineHeight: 1.6
						}
					}

				}
			};

			ctx = new Chart(document.querySelector(".postChart"), {
				type: 'line',
				options: options,
				data: data
			});
				document.getElementById('chatWrap').style.display = 'block';
		 },
		error: e=>{console.log(e)}
	})


}
function pageChagne(_num)
{
	
	//  좌표 to 주소 변환 객체
	//페이지 이동시(검색) 활성된 오버레이 비활성화
	if(customOverlay != null)
	{
		customOverlay.setMap(null)
	}
	if(_num == -1)
	{
		alert("첫페이지 입니다")
		return
	}
	else if(pageInfo.totalPage != 0)
	{
		if(pageInfo.totalPage < _num)
		{
			alert("마지막 페이지 입니다")
			return
		}
	}
	pageInfo.page = _num

	let bounds = new kakao.maps.LatLngBounds(); // 검색된 마커들을 화면안에 출력해주기 위한 객체 생성 
	$.ajax({
		url: "/jspteam4/mapBoardController",
		method: "get",
		async: false,
		data: { // 페이지 검색 맵의 범위,
			type: "pageByLatLng",
			ha: focusBounds.ha, oa: focusBounds.oa, pa: focusBounds.pa, qa: focusBounds.qa
			, page: pageInfo.page, viewByPage: pageInfo.viewByPage, cno: pageInfo.cno,clat:focusCenter.Ma,clng:focusCenter.La,sSort:document.querySelector('.sortList').value
		},
		success: r => {
			console.log(r) // 결과 출력
			clusterer.clear()
			if(r.boardList == null)
			{
				markers.forEach(_m =>{
						_m.setMap(null)
					})
				markers = []
				document.getElementById('postWrap').style.display = 'none';
				document.getElementById('chatWrap').style.display = 'none';
				return
			}
			document.getElementById('postWrap').style.display = 'block';
		

			//마커와 게시글 출력
				
				pageInfo = {page : r.page ,viewByPage : r.listsize,cno : r.cno,totalPage:r.totalpage,totalBoard:r.totalsize}
				let html = ``
				if(markers.length > 0)
				{
					markers.forEach(_m =>{
						_m.setMap(null)
					})
				}
				markers = r.boardList.map((b) => {
					let mpos = new kakao.maps.LatLng(b.blat, b.blng)
					
					bounds.extend(mpos)
					// 기본 크기 24 , 35
					 
					var imageSrc = `/jspteam4/img/markers/${b.cno}.png`
	    			var markerImage = new kakao.maps.MarkerImage(imageSrc, markerDefaultSize);
					
					let _m = new kakao.maps.Marker({
						position: mpos,
						clickable: true,
						image : markerImage
					});
					_m.setMap(map)
					kakao.maps.event.addListener(_m, 'click', function() {
    					selectPost(b.bno)
					}); 
					//게시글 컨텐츠 html에 누적
					html += ` <div class="post" onclick="selectPost(${b.bno})">
								<h2>${b.btitle}</h2> 
								<div class="postheadder">
									<div class="writer" onclick="showUserReviewModal('${b.mnickName}')">작성자: ${b.mnickName}</div>
									<div style="font-size:12px;">카테고리: ${b.cname}</div>
								</div>
								
                   		 		<div style="max-height:55px; margin: 10px 0px 10px 0px; overflow:hidden;">${b.bcontent}</div>
                    			<div style="font-size:12px;">작성일: ${b.bdate}</div>
                    			<div class="postbtn"> 
                    				<button class="postbtnbtn"  onclick="host(${b.bno})"type="button">채팅</button>
                    				${loginMid === b.mid ? `<button class="postbtnbtn" onclick="postDelete(${b.bno})"> 삭제 </button>` : ""}
                    			</div>
                    		</div>`
					return _m
					 
				});
				console.log(markers.length)
				//clusterer.addMarkers(markers)
				
				document.querySelector('.postBox').innerHTML = html;

			pageBtnPrinter()
			map.setBounds(bounds,100,100,100,350)
			
			console.log(map.getLevel())

		},
         error:e=>{console.log(e)}
      })
      
}
function pageBtnPrinter()
{
	// 페이지 시작 번호 기준 
	// 5    0 1 2 3 4 / 5 
	let pagePer = Math.floor((pageInfo.page -1)/5)
	// 페이지 시작 번호 기준 에 리스트 번호만큼 계산
	let start = pagePer*5
	let end = start + 5 <= pageInfo.totalPage ? start +5 : pageInfo.totalPage
	let html = ``
	//이전 버튼
	html += `<li class="page-item" > 
		<a onclick = "pageChagne(${start-1})" > < </a> </li>`
	for(let num = start+1; num  <= end ; num++)
	{	
		html += `<li class="page-item" > 
		<a onclick = "pageChagne(${num})" > ${num}</a> </li>`
	}
	html += `<li class="page-item" > 
		<a onclick = "pageChagne(${end+1})" >  > </a> </li>`
	document.querySelector('.pageBtnList').innerHTML = html
}

function latlngSearchBoard()
{
	focusBounds = map.getBounds()
	focusCenter = map.getCenter()
	pageChagne(1)
	document.querySelector('.setFocus').style.display="none"
	document.getElementById('chatWrap').style.display = 'none';
}
let bno = 0;
function selectPost(bno) { //게시물 선택

	
	$.ajax({
		url: '/jspteam4/mapBoardController',
		method: 'get',
		async: false,
		data: { type: 'info', bno: bno, mid:loginMid },
		success: s => {viewOverlay(s) ;}, 
		error: e => {
			console.log(e)
		}

	})
}
function postDelete(bno)
{
		$.ajax({
		url: '/jspteam4/mapBoardController',
		method: 'delete',
		data: { mid:loginMid , bno: bno },
		success: s => {pageChagne(1)}, 
		error: e => {
			console.log(e)
		}

	})
}
function viewOverlay(s)
{
	let html = `<div class="postInfo">
					<div class="postinfoheadder">
						<h3>${s.btitle}</h3>
						<div class="writer" onclick="showUserReviewModal(${s.mnickName})">작성자: ${s.mnickName}</div>
					</div>
					<div class="">
						<div class="postinfocontent">
							<div style="font-size:12px;">카테고리: ${s.cname}</div>
							<div class="s.bcontent">${s.bcontent}</div>
							<div class="postinfobtn">
								<button onclick="host(${s.bno})" type="button">채팅</button>
								${loginMid === s.mid ? `<button onclick="postDelete(${s.bno})"> 삭제 </button>` : ""}
								<button onclick="showReviewModal(${s.mno})" type="button">리뷰 작성</button>
							</div>
						</div>
						<div class="postinfobottom">
							<div>날짜: ${s.bdate}</div>
						</div>
					</div>
					
				</div>`
			//다른 게시글의 상세정보 오버레이가 표시중일경우 꺼주고 다시 켜준다
			if (customOverlay != null) {
				customOverlay.setMap(null)
			}
			customOverlay = new kakao.maps.CustomOverlay({
				map: map,
				clickable: true,
				content: html,
				position: new kakao.maps.LatLng(s.blat, s.blng),
				xAnchor: 0.3, //오버레이창 축 변경
				yAnchor: 1.3,
				zIndex: 3, 
				
			});
			map.setCenter(new kakao.maps.LatLng(s.blat, s.blng))
			
			customOverlay.setMap(map);
}
let loginlat,loginlng

navigator.geolocation.getCurrentPosition( e => {
	
	loginlat = e.coords.latitude  // 위도 
	loginlng = e.coords.longitude  // 경도 
	
	
	let currentlat = e.coords.latitude;
	let currentlng = e.coords.longitude;
	
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = { 
	        center: new kakao.maps.LatLng( currentlat , currentlng ), // 지도의 중심좌표
	        level: 3 // 지도의 확대 레벨
		};
	    
	map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
	
	      	// 마커 클러스터러를 생성합니다 
	clusterer = new kakao.maps.MarkerClusterer({
	    map: null, // 마커들을 클러스터로 관리하고 표시할 지도 객체 
	    averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정 
	    minLevel: 7 // 클러스터 할 최소 지도 레벨 
	});
	 kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {

        // 현재 지도 레벨에서 1레벨 확대한 레벨
        var level = clusterer.minLevel;

        // 지도를 클릭된 클러스터의 마커의 위치를 기준으로 확대합니다
        map.setLevel(level, {anchor: cluster.getCenter()});
    });
	//addMarker(new kakao.maps.LatLng(currentlat,currentlng));
	//지도 확대or축소하면 리스트 새로 불러오기
		/*
			let 동 = map.getBounds().oa; console.log( "동:"+동 )	 
		let 서 = map.getBounds().ha; console.log( "서:"+서 ) 
    	let 남 = map.getBounds().qa; console.log( "남:"+남 )
    	let 북 = map.getBounds().pa; console.log( "북:"+북 )
 	 
	 */
		//지도 범위 변경시 재검색 버튼 띄워주기
	  	kakao.maps.event.addListener(map, 'zoom_changed', function() {document.querySelector('.setFocus').style.display="block"});
		kakao.maps.event.addListener(map, 'dragend', function() { document.querySelector('.setFocus').style.display="block" });
	//test1();
	//clusterer.addMarkers(markers);
	// 지도에 클릭 이벤트를 등록합니다
	// 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
	kakao.maps.event.addListener(map, 'click', function(mouseEvent) {     
	    // 클릭한 위도, 경도 정보를 가져옵니다 
	    var latlng = mouseEvent.latLng; 
	    console.log(map.getBounds())
	    // 마커 위치를 클릭한 위치로 옮깁니다
	    addMarker(latlng, latlng.getLat(), latlng.getLng())
	    var message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, ';
	    message += '경도는 ' + latlng.getLng() + ' 입니다';
	    
	    var resultDiv = document.getElementById('clickLatlng'); 
	    resultDiv.innerHTML = message;
	    
	    plat = latlng.getLat(); // 위도와 경도를 전역변수로 이동후 제품등록시 사용.
	    plng = latlng.getLng();
	    
	    map.setCenter(new kakao.maps.LatLng(latlng.getLat(),  latlng.getLng()));
	});
	pageInfo.page = 0
	loadCategory()

	focusCenter = map.getCenter()
	focusBounds = map.getBounds()
	pageChagne(1)

}); // getCurrentPosition end 
function settingListSize(_num)
{
	let ls = document.querySelector('.listsize')
	var defaultoption= document.createElement("option");
	defaultoption.text ='페이지 수'
	defaultoption.value = "none"
	ls.appendChild(defaultoption)
	for(let i=5;i<=15;i++)
	{
		var option = document.createElement("option"); // option 요소 생성
		option.text = i // 옵션 텍스트 설정
		option.value = i; // 옵션 값 설정
		ls.appendChild(option)
	}
	ls.addEventListener('change',e=>{
		pageInfo.viewByPage = document.querySelector(".listsize").value
		pageChagne(1)		
	})
}
settingListSize()


function loadCategory()
{
	//html = ``
	$.ajax({
		url:"/jspteam4/mapBoardController",
		method:'get',
		data : {type:'getCategoryList'},
		async:false,
		success : r =>{
			let cl = document.querySelector('.catelist')
			let cl2 = document.querySelector('.chartCategoryList')
			
			r.forEach(c => {
				//html += `<option value=${c.cno}>${c.cname}</option>`
				var option = document.createElement("option"); // option 요소 생성
				
				option.text = c.cname; // 옵션 텍스트 설정
				option.value = c.cno; // 옵션 값 설정
				cateList.push({cno:c.cno,cname:c.cname})
				cl.appendChild(option); // option을 select에 추가
				
				
				var option2 = document.createElement("option");
				option2.text = c.cname;
				option2.value = c.cno;
				cl2.appendChild(option2);
				
				
			})
			
			
		},
		erroe : e =>{console.log(e)}
		
	})
	document.querySelector(".catelist").addEventListener('change',e=>{
		
		pageInfo.cno = document.querySelector(".catelist").value
		pageChagne(1)
	})
	document.querySelector(".sortList").addEventListener('change',e=>{
		pageChagne(1)
	})
	
	document.querySelector('.close-chart').addEventListener('click',e=>{
		chartCancle()
	})
}
// 마커를 생성하고 지도위에 표시하는 함수입니다
function addMarker(position, plat, plng) {
	
	clusterer.clear();
	
	setMarkers(null)
    if(5 < markers.length)
	{
		markers.pop()

	}
    // 마커를 생성합니다
    console.log(position)
    var marker = new kakao.maps.Marker({
        position: position 
    });
    

    
    if(customOverlay != null)
	{
		customOverlay.setMap(null)
	}
	markers.push(marker)
	
    // 마커가 지도 위에 표시되도록 설정합니다
    setMarkers(map)
    
    // 생성된 마커를 배열에 추가합니다
	
	
	
	createInfoWindow(marker, plat, plng)
	
	clusterer.addMarkers(markers);
}

// 초기 마커 생성
function testaddMarker(position, title, image) {
	// 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        position: position,
        title: title, 
        image: image
    });

    // 마커가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);
    
    // 생성된 마커를 배열에 추가합니다
    markers.push(marker);
}

// 멥 랜더링
function setMarkers(map) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }            
}

// 초기 마커 
function test1() {
	// 마커를 표시할 위치와 title 객체 배열입니다 
	var positions = [
	    {
	        title: '카카오', 
	        latlng: new kakao.maps.LatLng(37.296154, 126.856618)
	    },
	    {
	        title: '생태연못', 
	        latlng: new kakao.maps.LatLng(37.294876, 126.857128)
	    },
	    {
	        title: '텃밭', 
	        latlng: new kakao.maps.LatLng(37.295491, 126.859111)
	    },
	    {
	        title: '근린공원',
	        latlng: new kakao.maps.LatLng(37.296913, 126.857823)
	    }
	];
	/*
		
	 */
	// 마커 이미지의 이미지 주소입니다
	var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
	    
	for (var i = 0; i < positions.length; i ++) {
	    // 마커 이미지의 이미지 크기 입니다
	    var imageSize = new kakao.maps.Size(24, 35); 
	    
	    // 마커 이미지를 생성합니다    
	    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
	     
	    testaddMarker(positions[i].latlng, positions[i].title, markerImage);

	}
	console.log(markers)

}
var infowindow = null
// 인포윈도우 생성
function createInfoWindow(marker, plat, plng) {
	console.log('asd')
	if (infowindow != null) infowindow.close()
	var iwContent = `
	

	<div class="infowindowbox">
		<button class="infowindowboxbtn" id="btn-modal" onclick="showBoardModal()" type="button">글 작성</button>
	</div>`, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
    iwPosition = new kakao.maps.LatLng(plat, plng); //인포윈도우 표시 위치입니다
	//채팅방 온클릭 이벤트 넣어주기
	// 인포윈도우를 생성합니다
	infowindow = new kakao.maps.InfoWindow({
	    position : iwPosition, 
	    content : iwContent ,
	    removeable:true
	});
	  
	// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
	infowindow.open(map, marker); 
}
 

function rangePostRecommendations()
{
	if(map.getLevel() > 6)
	{
		alert("해당 범위에선 추천순을 지원하지 않습니다.")
	}
}






