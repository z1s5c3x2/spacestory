package jspteam4.model.Dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.ToString;


@ToString
public class MessageDto {		// 주고받을 메시지 정보들을 설계한 클래스 
	
	private String frommid; // 보낸사람 
	private String msg;		//보낸 사람의 채팅내용
	private String date;	// 보낸시간 

	
	public MessageDto() { }
	
	
	public MessageDto(String frommid, String msg) {
		super();
		this.frommid = frommid;
		this.msg = msg;
		// 객체 생성시 추가코드 
			
			// 2. 보낸일시 구하기 
				// 1. new Date() : 현재시간/날짜 제공하는 클래스 [ import java.util.Date; ]
			Date date = new Date(); System.out.println( "현재날짜/시간 : " + date );
				// 2. SimpleDateFormat : 날짜 포멧(형식)
			SimpleDateFormat sdf = new SimpleDateFormat(" aa hh:mm ");
				// y연도 M월 d일 h시m분s초 aa오전오후 
			this.date = sdf.format( date ); // 현재시간을 정의한형식으로 변환 
	}
	
	


	

	public MessageDto(String msg) {
		super();
		this.msg = msg;
	}


	public MessageDto(String frommid, String msg,  String date) {
		super();
		this.frommid = frommid;
		this.msg = msg;
		this.date = date;
	}

	public String getFrommid() {
		return frommid;
	}

	public void setFrommid(String frommid) {
		this.frommid = frommid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	

	

	
	
}