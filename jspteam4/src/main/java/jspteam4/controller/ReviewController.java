package jspteam4.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import jspteam4.model.Dao.visibleboard.BoardDao;
import jspteam4.model.Dao.visibleboard.MemberDao;
import jspteam4.model.Dao.visibleboard.ReviewDao;
import jspteam4.model.Dto.ReviewDto;
import jspteam4.model.Dto.ReviewPageDto;
import jspteam4.model.Dto.infoPageDto;

/**
 * Servlet implementation class ReviewController
 */
@WebServlet("/ReviewController")
public class ReviewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReviewController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1. 요청
		String type = request.getParameter("type"); System.out.println("Type : " + type);
		
		
		
		int listsize = Integer.parseInt( request.getParameter("reviewpageOject.listsize") ); System.out.println("listsize : " + listsize);
		int page = Integer.parseInt( request.getParameter("reviewpageOject.page") ); System.out.println("page : " + page);
		
		if (type.equals("search")) {
			
			System.out.println("mno : " + request.getParameter("mno") );
			int mno = Integer.parseInt(request.getParameter("mno")); System.out.println("mno : " + mno); // mno 요청 후 변수에 담기
			
			// 페이징 처리시 사용될 변수들 
			int startrow = ( page-1 )*listsize;
		
			int totalsize = ReviewDao.getInstance().getTotalSize( mno ); System.out.println("totalsize : " +totalsize); // 전체 게시물수
				
			int totalpage = totalsize%listsize == 0 ? // 전체 페이지 수
							totalsize/listsize : 	  
							totalsize/listsize+1 ;
		
			int btnsize = 5; // 한 페이지에 출력할 버튼 수
	
			int startbtn = ( (page-1) / btnsize ) * btnsize + 1 ;	// 시작 버튼 값
			
			int endbtn = startbtn+(btnsize-1); // 끝나는 버튼 값
				
			if( endbtn >= totalpage ) endbtn = totalpage; // 전체 페이지수가 끝나는 버튼 값보다 크거나 같으면 마지막 버튼값 = 총 페이지 수	
				
				
			ArrayList<ReviewDto> reviewDto = ReviewDao.getInstance().reviewView(mno , listsize , startrow);
			
			for(int i = 0 ; i < reviewDto.size(); i++) {
				reviewDto.get(i).setRsender_mnickname(MemberDao.getInstance().mnoTomnickname(reviewDto.get(i).getRsender()));
				reviewDto.get(i).setRreceiver_mnickname(MemberDao.getInstance().mnoTomnickname(reviewDto.get(i).getRreceiver()));
			}
			
			ReviewPageDto reviewPageDto = ReviewPageDto.builder().totalpage(totalpage).page(page).listsize(listsize).startrow(startrow).totalsize(totalsize).startbtn(startbtn).endbtn(endbtn).boardList(reviewDto).build();
			
			ObjectMapper objectMapper = new ObjectMapper();
			String json =  objectMapper.writeValueAsString( reviewPageDto );
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().print(json); 
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 작성
		// 1. 요청 
		String rcontent = request.getParameter("rcontent");
		int rscore = Integer.parseInt(request.getParameter("rscore"));
		int rsender = Integer.parseInt(request.getParameter("rsender"));
		int rreceiver = Integer.parseInt(request.getParameter("rreceiver"));
		
		// 2. 응답
		boolean result = ReviewDao.getInstance().reviewWrite(rcontent, rsender, rreceiver, rscore);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(result); 		
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String rcontent = request.getParameter("rcontent");
		int rscore = Integer.parseInt(request.getParameter("rscore"));
		int rno = Integer.parseInt(request.getParameter("rno"));
		
		boolean result = ReviewDao.getInstance().reviewUpdate(rcontent, rscore, rno);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(result); 
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int rno = Integer.parseInt("rno");
		
		boolean result = ReviewDao.getInstance().reviewDelete(rno);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(result); 		
	}

}
