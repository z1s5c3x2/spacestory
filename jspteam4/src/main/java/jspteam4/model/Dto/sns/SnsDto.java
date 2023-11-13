package jspteam4.model.Dto.sns;

import java.util.ArrayList;


public class SnsDto {
	// 1. 필드
	private int sno;
	private String simg;
	private String sdate;
	private String scontent;
	private String spwd;
	private int searchsns;
	private int good;
	private int hate;
	
	// 댓글 리스트 생성자 
	ArrayList<CommentDto> commentList;


	// 2. 생성자
	public SnsDto() {
	}

	
	// 검색 횟수를 구하는 생성자
	public SnsDto(int searchsns) {
		super();
		this.searchsns = searchsns;
	}


	


	//개별출력(수정페이지)
	public SnsDto(int sno, String simg, String scontent) {
		super();
		this.sno = sno;
		this.simg = simg;
		this.scontent = scontent;
	}


	public SnsDto(int sno, String simg, String sdate, String scontent, String spwd, int searchsns, int good, int hate,
			ArrayList<CommentDto> commentList) {
		super();
		this.sno = sno;
		this.simg = simg;
		this.sdate = sdate;
		this.scontent = scontent;
		this.spwd = spwd;
		this.searchsns = searchsns;
		this.good = good;
		this.hate = hate;
		this.commentList = commentList;
	}


	public SnsDto(int sno, String simg, String sdate, String scontent, String spwd, ArrayList<CommentDto> commentList) {

		super();
		this.sno = sno;
		this.simg = simg;
		this.sdate = sdate;
		this.scontent = scontent;
		this.spwd = spwd;
		this.commentList = commentList;
	}
	// 등록시 사용되는 생성자
	public SnsDto(String simg, String scontent, String spwd) {
		super();
		this.simg = simg;
		this.scontent = scontent;
		this.spwd = spwd;
	}
	// 수정시 사용되는 생성자
	public SnsDto(String simg, int sno,  String spwd , String scontent) {
		super();
		this.sno = sno;
		this.simg = simg;
		this.scontent = scontent;
		this.spwd = spwd;
	}
	

	public SnsDto(int sno, String simg, String scontent, String sdate, int good, int hate) {
		super();
		this.sno = sno;
		this.simg = simg;
		this.sdate = sdate;
		this.scontent = scontent;
		this.good = good;
		this.hate = hate;
	}


	// 3. 메소드
	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getSimg() {
		return simg;
	}

	public void setSimg(String simg) {
		this.simg = simg;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getScontent() {
		return scontent;
	}

	public int getSearchsns() {
		return searchsns;
	}

	public void setSearchsns(int searchsns) {
		this.searchsns = searchsns;
	}

	public void setScontent(String scontent) {
		this.scontent = scontent;
	}

	public String getSpwd() {
		return spwd;
	}

	public void setSpwd(String spwd) {
		this.spwd = spwd;
	}

	public ArrayList<CommentDto> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<CommentDto> commentList) {
		this.commentList = commentList;
	}
	
	public int getGood() {
		return good;
	}


	public void setGood(int good) {
		this.good = good;
	}


	public int getHate() {
		return hate;
	}


	public void setHate(int hate) {
		this.hate = hate;
	}


	@Override
	public String toString() {
		return "SnsDto [sno=" + sno + ", simg=" + simg + ", sdate=" + sdate + ", scontent=" + scontent + ", spwd="
				+ spwd + ", commentList=" + commentList + "]";
	}
	
	
	
}
