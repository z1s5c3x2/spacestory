package jspteam4.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import jspteam4.model.Dao.visibleboard.BoardDao;
import jspteam4.model.Dao.visibleboard.MemberDao;
import jspteam4.model.Dto.BoardDto;
import jspteam4.model.Dto.MemberDto;
import jspteam4.model.Dto.infoPageDto;

import jspteam4.service.FileService;




@WebServlet("/BoardInfoController")
public class BoardInfoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public BoardInfoController() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 요청한다.
		String type = request.getParameter("type"); System.out.println("Type : " + type);
		Integer mno = Integer.parseInt(request.getParameter("mno")); System.out.println("mno : " + mno);
		
		int listsize = Integer.parseInt( request.getParameter("pageOject.listsize") ); System.out.println("listsize : " + listsize);
		int page = Integer.parseInt( request.getParameter("pageOject.page") ); System.out.println("page : " + page);
		
		 if (type.equals("search")) {
             // 검색한 닉네임으로 작성된 게시물 가져오기
					// 1. 페이지별 레코드의 시작번호
				int startrow = ( page-1 )*listsize; // 페이지번호*최대게시물수
					// 1*10 => 10->0 // 2*10 => 20 -> 10 // 3*10 => 30->20
				// ----------------------- 4. 마지막 페이지번호 ---------------- // 
					// 1. 마지막페이지번호/총페이지수 = 전체게시물수 / 페이지별최대게시물수( listsize )
					// 2. 전체 게시물수
				int totalsize = BoardDao.getInstance().getTotalSize( mno ); System.out.println("totalsize : " +totalsize);
					// 3. 마지막페이지번호/총페이지수
				int totalpage = totalsize%listsize == 0 ? 
								totalsize/listsize : 	  
								totalsize/listsize+1 ;
				// 1. 페이지버튼 번호의 최대개수 
				int btnsize = 5;
					// 2. 페이지버튼 번호의 시작번호 
				int startbtn = ( (page-1) / btnsize ) * btnsize + 1 ;	
				System.out.println("startbtn : " + startbtn);
					// 3. 페이지버튼 번호의 마지막번호 
				int endbtn = startbtn+(btnsize-1);
				System.out.println("endbtn : " + endbtn);
						// * 단 마지막번호는 총페이지수 보다 커질수 없음 [
						// 만약에 마지막번호가 총 페이지수보다 크거나 같으면 총페이지 수로 제한두기 
				if( endbtn >= totalpage ) endbtn = totalpage;
				System.out.println("endbtn2 : " + endbtn);
				// ----------------------- 6. pageDto 구성  ---------------- // 
				ArrayList<BoardDto> boardDto = BoardDao.getInstance().searchboard( mno , listsize , startrow ); System.out.println(boardDto);
		
				infoPageDto pageDto = infoPageDto.builder().totalpage(totalpage).page(page).listsize(listsize).startrow(startrow).totalsize(totalsize).startbtn(startbtn).endbtn(endbtn).boardList(boardDto).build();
					
				System.out.println("pageDto.getEndbtn() : " + pageDto.getEndbtn());
				ObjectMapper objectMapper = new ObjectMapper();
				String json =  objectMapper.writeValueAsString( pageDto );
				System.out.println("서블릿 json : " + json);
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().print(json); 

         }
	}
	// 2. 쓰기 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}
	// 3. 수정 
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}
	
	// 4. 삭제 
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}
	
}