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


public class CommentDto {
	private int cmno;
	private String cmcontent;
	private String cmdate;
	private int mno;
	private int bno;

}
