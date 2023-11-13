package jspteam4.model.Dto;

import java.util.ArrayList;

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

public class PageDto {
	private int page; 		// 현재 페이지번호 
	private int listsize; 	// 페이지당 최대게시물수 
	private int totalsize;	// 총 게시물 수 or 카테고리별 게시물수 
	private int totalpage;	// 총 페이지 수
	private String pa;
	private String qa;
	private String ha;
	private String oa;
	private int cno;
	ArrayList<BoardDto> boardList; //page의 게시글 리스트
	//좌표 페이징 처리 , 글쓴이 정보 검색 페이징, 리뷰 페이징 ,댓글 페이징 , 채팅방 목록 페이징
	//최소 4개 ~ 최대 5개 -> 제네리
}
