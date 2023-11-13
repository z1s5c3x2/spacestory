package jspteam4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.protocol.x.SyncFlushDeflaterOutputStream;

import jspteam4.model.Dao.visibleboard.MemberDao;
import jspteam4.model.Dto.MemberDto;

/**
 * Servlet implementation class MemberFindController
 */
@WebServlet("/MemberFindController")
public class MemberFindController extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public MemberFindController() {
        super();
        // TODO Auto-generated constructor stub
    }
    // 하나의 메소드의 여러개 ajax 통신할때. type전송( 숫자 1:아이디중복검사 2.이메일중복검사 vs 필드명  mid : 아이디중복검사 , memail : 이메일중복검사 )  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 요청한다.
		String type = request.getParameter("type"); System.out.println("Type : " + type);
		String data = request.getParameter("data"); System.out.println("data : " + data);
		// 2. 객체화/유효성검사	 	
				 if (type.equals("mid")) {
		                // 아이디 유효성
					// 3. DAO 요청 결과 
						boolean result = MemberDao.getInstance().findIdOrEmail( type , data);
						// 4. 결과 응답한다. 
						response.setContentType("application/json;charset=UTF-8");
						response.getWriter().print(result);
	
		            }
				 if (type.equals("memail")) {
		                // 아이디 유효성
					// 3. DAO 요청 결과 
						boolean result = MemberDao.getInstance().findIdOrEmail( type , data);
						// 4. 결과 응답한다. 
						response.setContentType("application/json;charset=UTF-8");
						response.getWriter().print(result);
	
		            }
				 if (type.equals("mnickname")) {
		                // 아이디 유효성
					// 3. DAO 요청 결과 
						boolean result = MemberDao.getInstance().findIdOrEmail( type , data);
						// 4. 결과 응답한다. 
						response.setContentType("application/json;charset=UTF-8");
						response.getWriter().print(result);
	
		            }
				 
	            if (type.equals("findId")) {
	                // 아이디 찾기 로직
	                String memail = request.getParameter("data");				System.out.println("로직 안 memail : " + memail);
	                String foundId = MemberDao.getInstance().findId(memail);	System.out.println("로직 안 foundId : " + foundId);

	                JSONObject jsonResponse = new JSONObject();
	                try {
						jsonResponse.put("id", foundId);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                response.setContentType("application/json");
	                response.getWriter().write(jsonResponse.toString());

	            } else if (type.equals("findPw")) {
	                // 비밀번호 찾기 로직
	                String mid = request.getParameter("data1"); System.out.println("mid : " + mid);
	                String memail = request.getParameter("data2"); System.out.println("memail : " + memail);
	                String foundPwd = MemberDao.getInstance().findPw(mid, memail);

	                JSONObject jsonResponse = new JSONObject();
	                try {
						jsonResponse.put("pwd", foundPwd);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	                response.setContentType("application/json");
	                response.getWriter().write(jsonResponse.toString());
	            }
	}
	
	
	// 로그인 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 매개변수 요청 
		String mid = request.getParameter("mid");
		String mpwd = request.getParameter("mpwd");
		// 2. (객체화/유효성검사)
		// 3. DAO 에게 전달후 결과 받기 
		boolean result = MemberDao.getInstance().login(mid, mpwd);
		
		// - 만약에 로그인 성공하면 세션에 로그인한 정보담기
		if( result) {
			// 1. 세션에 저장할 데이터 만들기
			MemberDto loginDto = MemberDao.getInstance().info(mid);
			// 2. 세션에 저장한다.
			request.getSession().setAttribute( "loginDto" , loginDto );
			// * 세션 상태 확인
			MemberDto dto = (MemberDto)request.getSession().getAttribute("loginDto"); // 부모인 Object가 자식의 객체를 따라가려면 강제로 형변환을 해줘야함 (MemberDto)

			
			ObjectMapper objectMapper = new ObjectMapper();
			String json =  objectMapper.writeValueAsString( dto );
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().print(json); 
		}
		else {
		// 4. 결과를 응답한다.
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print(result); 
		}

	}
}