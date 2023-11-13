package jspteam4.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import jspteam4.model.Dao.sns.CommentDao;
import jspteam4.model.Dto.sns.SnsDto;


@WebServlet("/CommentController")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public CommentController() {
        super();
     
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String search = " ";
		
		String type = request.getParameter("type");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json="";
		if(type.equals("1")) {//입력창
			
		}
		else if (type.equals("2")) {
			search =request.getParameter("search");
			ArrayList<SnsDto> result = CommentDao.getInstance().search(search);
			for ( int i=0; i<result.size(); i++) {
				int sno = result.get(i).getSno();
				result.get(i).setCommentList(CommentDao.getInstance().getList(sno));
			}
			json = objectMapper.writeValueAsString(result);
			
		}
		else if (type.equals("3")) {
			search =request.getParameter("search");
			int result = CommentDao.getInstance().viewSearch(search);
			
			SnsDto snsDto = new SnsDto(result);
		
			json = objectMapper.writeValueAsString(snsDto);
		}
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print( json );
		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int sno = Integer.parseInt(request.getParameter("sno"));
		String ccontent = request.getParameter("ccontent");
		String cpwd = request.getParameter("cpwd");
		
		boolean result = CommentDao.getInstance().commnetWrite(sno, cpwd, ccontent);
		
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print( result );
	}

	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print(CommentDao.getInstance().commentDelete(Integer.parseInt(request.getParameter("cno")), request.getParameter("cpwd")));
	}

}
