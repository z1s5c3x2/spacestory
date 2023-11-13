package jspteam4.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

import jspteam4.model.Dao.sns.CommentDao;
import jspteam4.model.Dao.sns.SnsDao;
import jspteam4.model.Dto.sns.SnsDto;





@WebServlet("/snsController")
public class SnsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

  
    public SnsController() {
      
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 요청 
				String type = request.getParameter("type");
				
				ObjectMapper objectMapper = new ObjectMapper();
				String json = "";

				if( type.equals("1") ) { // 전체 조회 로직 
					
					ArrayList<SnsDto> result = SnsDao.getInstance().getList();
					System.out.println(result.size());
					for ( int i=0; i<result.size(); i++) {
						int sno = result.get(i).getSno();
						result.get(i).setCommentList(CommentDao.getInstance().getList(sno));
					}
					
					json = objectMapper.writeValueAsString( result );

				}else if( type.equals("2") ) {// 개별 조회 로직 
					//1.매개변수 요청 
					int sno = Integer.parseInt( request.getParameter("sno") ) ;
				
					//2. DAO 처리 
					SnsDto result = SnsDao.getInstance().getBoard( sno );
					json = objectMapper.writeValueAsString( result );
				}
				
				// 공통 로직 // 1. 전체조회 , 개별조회 하던 응답 로직 공통
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().print( json );
				
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



		MultipartRequest multi = new MultipartRequest(
					request,			 
					request.getServletContext().getRealPath("/upload/sns"), 
					1024*1024*1024,  
					"UTF-8",		 
					new DefaultFileRenamePolicy() 
					
					);
	
		System.out.println(request.getServletContext().getRealPath("/upload/sns") );
			//  
			String spwd = multi.getParameter("spwd");
			String scontent = multi.getParameter("scontent");
			String simg = multi.getFilesystemName("simg"); 

			response.setContentType("application/json; charset=UTF-8");
			boolean result = SnsDao.getInstance().snsWrite(new SnsDto(simg,scontent,spwd));
			response.getWriter().print(result);

}


	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String type = request.getParameter("type");
		boolean result;
		if(type == null) {
			// 1.  수정할 첨부파일 업로드 [ cos.jar -> MultipartRequest 클래스  ]
			MultipartRequest multi = new MultipartRequest(
					request,			// 요청방식 
					request.getServletContext().getRealPath("/upload/sns"),	//저장경로				// 저장경로 
					1024*1024*1024, // 업로드허용용량[바이트] 1GB 
					"UTF-8",		// 인코딩타입 
					new DefaultFileRenamePolicy() // 만약에 업로드파일명이 서버내 존재하면(중복) 자동으로 파일명뒤에 숫자 붙이기
					);
	
			// 2. 수정할 데이터 요청
				String spwd = multi.getParameter("spwd");
				String simg = multi.getFilesystemName("simg");
				String scontent = multi.getParameter("scontent");
				
				// 2* 수정할 게시물 식별키
				int sno = Integer.parseInt( multi.getParameter("sno"));
				SnsDto updateDto = new SnsDto( simg,sno, spwd , scontent);
				// * 만약에 새로운 첨부파일 없으면 기존 첨부파일() 그대로 사용
				if ( updateDto.getSimg() == null) {
					updateDto.setSimg( SnsDao.getInstance().getBoard(sno).getSimg());
				}
			// 3. Dao 처리 
				result = SnsDao.getInstance().onUpdate(updateDto);
			// 4. (Dao 결과) 응답 
		
		}else{
			int sno = Integer.parseInt(request.getParameter("sno"));
			result = SnsDao.getInstance().updateGoodHate(sno, type);
			
	 	}
		
		response.setContentType("application/json; charset=UTF-8"); 
		response.getWriter().print(result);
	}
		

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int sno = Integer.parseInt(request.getParameter("sno"));
		String filename = SnsDao.getInstance().fileDelete(sno);
		// 2. 첨부파일 폴더 의 경로
		String uploadpath = request.getServletContext().getRealPath("/upload/sns");
		
		// 3. 다운로드 할 파일의 전체 경로 
		String filepath = uploadpath+"/"+filename;
		File file = new File(filepath); // 해당 경로의 파일 객체화 
		file.delete();
	
		
		
		boolean result = SnsDao.getInstance().onDelete(sno, request.getParameter("spwd"));
		
		
		
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(result);
	}

}
