package jspteam4.model.Dto;

import java.util.ArrayList;
import java.util.List;

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
public class infoPageDto {
	// 1. 
	private int page; 		// 현재 페이지번호 
	private int listsize; 	// 페이지당 최대게시물수 
	private int startrow;	// 현재 페이지에서 시작되는 레코드 번호 
	private int totalsize;	// 총 게시물 수 or 카테고리별 게시물수 
	private int totalpage;	// 총 페이지 수
	private int startbtn;	// 페이지번호버튼 시작번호 
	private int endbtn;		// 페이지번호버튼 끝번호 
	// * 게시물 리스트 [ 조회된 결과 ]
	ArrayList<BoardDto> boardList;

}