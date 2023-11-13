package jspteam4.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter // 새터
@Setter //새터
@ToString //문자열 출력
@Builder //빌더패턴
@NoArgsConstructor //빈생성자
@AllArgsConstructor //풀생성자

public class BoardDto {
	private int bno;
	private String btitle;
	private String bcontent;
	private String blat;
	private String blng;
	private String bdate;
	private int mno;
	private int cno;
	
	//작성자 닉네임
	private String mnickName;
	//작성자 아이디
	private String mid;
	//카테고리
	private String cname;


}
