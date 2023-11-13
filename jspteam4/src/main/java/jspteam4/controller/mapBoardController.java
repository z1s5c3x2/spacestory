package jspteam4.controller;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import jspteam4.model.Dao.visibleboard.BoardDao;
import jspteam4.model.Dto.BoardDto;
import jspteam4.model.Dto.PageDto;
import jspteam4.service.UserLogWriter;

/**
 * Servlet implementation class mapBoardController
 */
@WebServlet("/mapBoardController")
public class mapBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mapBoardController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uploadpath = request.getServletContext().getRealPath("/UserLog");
		
		
		response.setContentType("application/json;charset=UTF-8");
		if(request.getParameter("type").equals("testadd"))
		{ //샘플 데이터 생성 Math.random() * (최댓값-최소값+1) + 최소값

		
			  
			  System.out.println("샘플 추가");
				Double randlat = Math.random()*(Double.parseDouble(request.getParameter("oa"))-Double.parseDouble(request.getParameter("ha")))+Double.parseDouble(request.getParameter("ha"));
				Double randlng = Math.random()*(Double.parseDouble(request.getParameter("pa"))-Double.parseDouble(request.getParameter("qa")))+Double.parseDouble(request.getParameter("qa"));
				
			//BoardDao.getInstance().boardWrite(null)
			BoardDto bdt = BoardDto.builder()
					.btitle("test")
					.bcontent("test2")
					.blat(randlng.toString())
					.blng(randlat.toString())
					.mno(1)
					.cno(1)
					.build();
			BoardDao.getInstance().boardWrite(bdt);
		}else if(request.getParameter("type").equals("info"))
		{//게시글 개별 조회
			response.setContentType("application/json;charset=UTF-8");
			//게시글 상세 조회
			BoardDto _bdt = BoardDao.getInstance()
					.boardInfo(Integer.parseInt(request.getParameter(("bno"))));
			
			//요청에 대한 답변 
			response.getWriter().print(new ObjectMapper()
					.writeValueAsString(_bdt));
			//로그 기록
			UserLogWriter.getInstance().BoardInfoViewLog(uploadpath, request.getParameter("mid"), _bdt);
			
	
			
		}else if(request.getParameter("type").equals("pageByLatLng"))
		{
			// 카테고리pno ,현재 페이지,페이지 당 출력 수,맵 사이즈로 페이지 계산
			PageDto res = BoardDao.getInstance().
					getPage(Integer.parseInt(request.getParameter("cno")),
							Integer.parseInt(request.getParameter("page")),
							Integer.parseInt(request.getParameter("viewByPage")),request.getParameter("pa"),
							request.getParameter("qa"),
							request.getParameter("ha"),
							request.getParameter("oa"));
			
			System.out.println(res.getTotalpage());

										// 총 페이지 수 보다 요청한 페이지가 더 클 경우 null 반환
			res.setBoardList( res.getTotalpage() >= Integer.parseInt(request.getParameter("page")) ?
					BoardDao.getInstance().boardGetList(res.getPa(),res.getQa(),res.getHa(),res.getOa(),res.getCno()
					,(res.getPage()-1)*res.getListsize(),res.getListsize(),request.getParameter("sSort"),Double.parseDouble(request.getParameter("clng")),Double.parseDouble(request.getParameter("clat")) ) : null);
			
			response.getWriter().print(new ObjectMapper().writeValueAsString(res));
		}
		else if(request.getParameter("type").equals("exist")) {
			int bno = Integer.parseInt(request.getParameter("bno"));
			System.out.println(bno);
			 Boolean result =   BoardDao.getInstance().exist(bno);
			 response.setContentType("application/json;charset=UTF-8");
			 response.getWriter().print(result);
			 System.out.println("방의 상태 : " +result);
			
		}else if(request.getParameter("type").equals("getCategoryList"))
		{
			//카테고리 가져오기
			 response.setContentType("application/json;charset=UTF-8");
			 response.getWriter().print(
					 new ObjectMapper().writeValueAsString(BoardDao.getInstance().getCateList()));
			
		}
		else if(request.getParameter("type").equals("State")) {
			int bno = Integer.parseInt(request.getParameter("bno"));
			System.out.println(bno);
			 Boolean result =   BoardDao.getInstance().state(bno);
			 response.setContentType("application/json;charset=UTF-8");
			 response.getWriter().print(result);
			 System.out.println(result);
			
		}
		else if(request.getParameter("type").equals("category")) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().print(new ObjectMapper()
					.writeValueAsString(BoardDao.getInstance()
							.getCategoryList()));
		}
	
		else if(request.getParameter("type").equals("manager")) {
			String mid = request.getParameter("loginMid");
			System.out.println("loginMid : " +mid);
			int bno = Integer.parseInt(request.getParameter("bno"));
			boolean result = BoardDao.getInstance().host(mid,bno);
			System.out.println("result : " +result);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().print(result);
		}
		else if(request.getParameter("type").equals("chart"))
		{
			ArrayList<String> res = BoardDao.getInstance().getPostChart(
							request.getParameter("pa"),
							request.getParameter("qa"),
							request.getParameter("ha"),
							request.getParameter("oa"),
							request.getParameter("standard"),
							Integer.parseInt(request.getParameter("cno"))
							);
			
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().print(new ObjectMapper().writeValueAsString(res));
			
		}else if(request.getParameter("type").equals("3Board"))
		{

			response.setContentType("application/json;charset=UTF-8");
			int _rank = UserLogWriter.getInstance().Get3RankBoard(uploadpath,
					request.getParameter("mid"),
					request.getParameter("nowlat"),
					request.getParameter("nowlng"));
			
			response.getWriter().print(new ObjectMapper().writeValueAsString(BoardDao.getInstance().boardInfo(_rank)));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		//글쓰기 마커의 경위도 받아서 해당 좌표에 생성
		BoardDao.getInstance().boardWrite(
				BoardDto.builder()
				.btitle(request.getParameter("btitle"))
				.bcontent(request.getParameter("bcontent"))
				.blat(request.getParameter("blat"))
				.blng(request.getParameter("blng"))
				.mno(Integer.parseInt(request.getParameter("mno")))
				.cno(Integer.parseInt(request.getParameter("cno")))
				.build()
		);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String btitle = request.getParameter("btitle");
		String bcontent = request.getParameter("bcontent");
		int cno = Integer.parseInt(request.getParameter("cno"));
										// servlet 제목이나 컨텐츠값이 null일경우 실패 반환
										// dao 업데이트 항목이 1이 아닐경우 실패 반환
		response.getWriter().print( (btitle != null && bcontent != null && cno != 0) ? 
				BoardDao.getInstance().boardUpdate(
						BoardDto.builder()
						.btitle(btitle)
						.bcontent(bcontent)
						.cno(cno)
						.build()
						, Integer.parseInt(request.getParameter("mno"))): false);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().print(
				BoardDao.getInstance().boardDelete(
						Integer.parseInt(request.getParameter("bno")),
						request.getParameter("mid")
						));
	}


}
