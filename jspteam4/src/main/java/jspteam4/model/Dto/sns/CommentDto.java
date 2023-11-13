package jspteam4.model.Dto.sns;

public class CommentDto {
    private int cno ;
    private String ccontent ;
    private String cpwd ;
    private String cdate ;
    private int  sno ;
    
    public CommentDto() {
		// TODO Auto-generated constructor stub
	}

    //검색창 검색횟수  생성자
    
    //등록할때 쓰는 생성자
	public CommentDto(String ccontent, String cpwd, int sno) {
		super();
		this.ccontent = ccontent;
		this.cpwd = cpwd;
		this.sno = sno;
	}

	
	public CommentDto(int cno, String ccontent, String cpwd, String cdate, int sno) {
		super();
		this.cno = cno;
		this.ccontent = ccontent;
		this.cpwd = cpwd;
		this.cdate = cdate;
		this.sno = sno;
	}

	public int getCno() {
		return cno;
	}

	public void setCno(int cno) {
		this.cno = cno;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public String getCpwd() {
		return cpwd;
	}

	public void setCpwd(String cpwd) {
		this.cpwd = cpwd;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	@Override
	public String toString() {
		return "CommentDto [cno=" + cno + ", ccontent=" + ccontent + ", cpwd=" + cpwd + ", cdate=" + cdate + ", sno="
				+ sno + "]";
	}
    
    
    
    
    
    
    
    
}
