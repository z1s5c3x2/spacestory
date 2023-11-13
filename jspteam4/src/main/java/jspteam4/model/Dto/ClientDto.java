package jspteam4.model.Dto;

import javax.websocket.Session;

public class ClientDto {
	
	
	private Session session;
	private String mid; // 접속한사람 아이디
	private int bno ; 

	
	public ClientDto() {
		// TODO Auto-generated constructor stub
	}

	
	
	
	public ClientDto(Session session, String mid, int bno) {
		super();
		this.session = session;
		this.mid = mid;
		this.bno = bno;
	}


	public ClientDto(Session session, String mid) {
		super();
		this.session = session;
		this.mid = mid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public int getBno() {
		return bno;
	}

	public void setBno(int bno) {
		this.bno = bno;
	}

	

	
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getmid() {
		return mid;
	}

	public void setmid(String mid) {
		this.mid = mid;
	}




	@Override
	public String toString() {
		return "ClientDto [session=" + session + ", mid=" + mid + ", bno=" + bno + "]";
	}

	

	
	
	
	
	
	
	
	
}