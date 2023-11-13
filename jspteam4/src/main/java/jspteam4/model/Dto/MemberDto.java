package jspteam4.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter // 개터
@Setter	// 새터
@Builder //빌드업
@ToString	//문자열 출력
@NoArgsConstructor	//빈생성자
@AllArgsConstructor	//풀생성자
public class MemberDto {
	
		private int mno;
		private String mid;
		private String mpwd;
		private String memail;
		private String mnickname;
		
}
