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

public class ReviewDto {
	private int rno;
	private String rcontent;
	private String rdate;
	private int rsender;
	private int rreceiver;
	private int rscore;
	private String rsender_mnickname;
	private String rreceiver_mnickname;
}
