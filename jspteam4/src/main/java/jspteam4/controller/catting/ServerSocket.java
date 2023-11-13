package jspteam4.controller.catting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import jspteam4.model.Dao.visibleboard.BoardDao;
import jspteam4.model.Dto.ClientDto;
import jspteam4.model.Dto.MessageDto;

@ServerEndpoint("/serversokcet/{mid}/{bno}") // 서버소켓 URL
public class ServerSocket {
	// 0. 접속된 클라이언트소켓들의 저장소 [ 세션 , 아이디 저장 => ClientDto ]
	public static List<ClientDto> clientList = new Vector<>();
	// clientDto = session , mid , bno
	// 0-1. 방 구별하고 방장을 찾기 위한 Map !
	public static Map<Integer, String> boardList = new HashMap<>();
	// 0-2 방 리스트
	

	// integer=bno string=mid
	// 1. 클라이언트소켓으로부터 접속받았을때.
	@OnOpen // 매개변수 : 1. 접속된 클라이언소켓(세션) 2.접속된 회원아이디
	public void onOpen(Session session, @PathParam("mid") String mid, @PathParam("bno") int bno) throws Exception {
		 List<String> roomHost=new ArrayList<>();
		// 1-1 : 접속된 클라이언소켓을 리스트에 저장하자.
		ClientDto clientDto = new ClientDto(session, mid, bno);
		clientList.add(clientDto);
		// 방에 사람들 추가하기
		/* bno 랑 clientList 비교후 같을때 들어가있는 mid를 roomHost */
		if (boardList.containsKey(bno)) {
			
			for(ClientDto dtos : clientList) {
				if(dtos.getBno()==bno) {
					roomHost.add(dtos.getmid());
				String msg = "{\"type\":\"roomUser\",\"content\":\"" + roomHost + "\"}";
				onMessage(session, msg,bno);
				
				}
			}
		} else {
			for(ClientDto dtos : clientList) {
			if(dtos.getBno()==bno) {roomHost.add(dtos.getmid());
			String msg = "{\"type\":\"roomUser\",\"content\":\"" + roomHost + "\"}";
			onMessage(session, msg,bno);
			}
		}
			boardList.put(bno, mid);
		}
	}

	// 2.
	@OnError
	public void onError(Session session, Throwable throwable) {
	}

	// 3. 클라이언트소켓 과 서버소켓이 연결이 끊겼을때.
	@OnClose
	public void onClose(Session session, @PathParam("bno") int bno) throws Exception {
		// * 접속목록에서 세션 제거
		for (ClientDto clientDto : clientList) { // 접속목록에서 연결이 끊긴 세션 찾기
			if (clientDto.getSession() == session) {
				// 클라이언트소켓의 세션과 연결이 끊긴 세션과 같으면 해당 dto를 제거하기.
				clientList.remove(clientDto);
				String msg = "{\"type\":\"close\",\"msgbox\":\"" + clientDto.getmid() + "님이 채팅방에 나갔습니다.\"}";
				onMessage(session, msg,bno);
				break;
			}

		}

	}

	// 4.
	@OnMessage // 매개변수 : 1. 메시지를 보낸 클라이언트소켓(세션) 2.메시지 내용 [문자열]
	public void onMessage(Session session, String msg, @PathParam("bno") int bno) throws Exception {
		// 2-2 메시지를 보낼 내용 구성. [ 보낸사람 , 보낸내용 ]
		
		
		
		JSONObject jsonObject = new JSONObject(msg);
		String type = jsonObject.getString("type");
		String mid = jsonObject.getString("content");
		
		MessageDto MessageDto = null;
		
		// 회원목록중에 보낸세션과 일치하면 보낸사람mid와 내용으로 dto 구성
		for (ClientDto clientDto : clientList) {
			if (clientDto.getSession() == session) {
				MessageDto = new MessageDto(clientDto.getmid(), msg);
			}
		}
		for (ClientDto clientDto : clientList) {
			if (bno == clientDto.getBno()) {
				if (type.equals("alarm") || type.equals("message") || type.equals("emo") || type.equals("room")) {
					// - 보낸사람 찾기 [ 보낸 세션 을 이용한 보낸 mid 찾기 ]

					ObjectMapper objectMapper = new ObjectMapper();
					String jsonMsg = objectMapper.writeValueAsString(MessageDto);
					// 2-1 받은 메시지를 접속된 회원들에게 모두 전송
					clientDto.getSession().getBasicRemote().sendText(jsonMsg);

				} // 알람과 메시지 타입 일때 if

				else if (type.equals("close")) {// 회원이 나갈시 모든 리스트에서 회원을 삭제

					ObjectMapper objectMapper = new ObjectMapper();
					String jsonMsg = objectMapper.writeValueAsString(MessageDto);
					clientDto.getSession().getBasicRemote().sendText(jsonMsg);

					// 회원목록중에 보낸세션과 일치하면 보낸사람mid와 내용으로 dto 구성
					if (boardList.containsValue(mid)) {
						boolean result = BoardDao.getInstance().close(bno);
						if (result) {
							 objectMapper = new ObjectMapper();
							 jsonMsg = objectMapper.writeValueAsString(MessageDto);
							clientDto.getSession().getBasicRemote().sendText(jsonMsg);

							boardList.clear();
							Iterator<ClientDto> iterator = clientList.iterator();
							while (iterator.hasNext()) {
								ClientDto dto = iterator.next();
								if (dto.getBno() == bno) {
									iterator.remove();
									  session = dto.getSession(); // getSession 메서드를 사용하여 세션을 가져옵니다.
						                if (session != null && session.isOpen()) {
						                   
						                	String msgs = "{\"type\":\"noexist\",\"content\":\"" + "방장나감" + "\"}";
						                        session.getBasicRemote().sendText(msgs);
						                   
						                        // 예외 처리: 메시지 전송 중 오류 발생 시 처리
						                }
						                    
								}
							}
						
						}
					}
						String user = jsonObject.getString("user");
						user = user.replaceAll("[\\[\\]\"]", "");
						String[] userArray = user.split(", ");
						List<String> roomHost = new ArrayList<>();
						for (int i = 0; i < userArray.length; i++) {
							if (!mid.equals(userArray[i])) {
								roomHost.add(userArray[i]);
							}
						}
						System.out.println("roomHost : " + roomHost);
						String msgs = "{\"type\":\"roomUser\",\"content\":\"" + roomHost + "\"}";
						MessageDto dto = new MessageDto(clientDto.getmid(), msgs);

						objectMapper = new ObjectMapper();
						jsonMsg = objectMapper.writeValueAsString(dto);

						clientDto.getSession().getBasicRemote().sendText(jsonMsg);
					

				} 
				else if (type.equals("roomUser")) {
					System.out.println("방 에 있는사람");
					if (bno == clientDto.getBno()) {
						// - 보낸사람 찾기 [ 보낸 세션 을 이용한 보낸 mid 찾기 ]
						MessageDto MessageDtos = new MessageDto(msg);
						System.out.println("MessageDtos : " + MessageDtos);
						ObjectMapper objectMapper = new ObjectMapper();
						String jsonMsg = objectMapper.writeValueAsString(MessageDtos);
						// 2-1 받은 메시지를 접속된 회원들에게 모두 전송
						clientDto.getSession().getBasicRemote().sendText(jsonMsg);
						System.out.println("jsonMsg : " + jsonMsg);

					} // 알람과 메시지 타입 일때 if

				}

			} // f end
		} // p end

}
}
