package jspteam4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import jspteam4.model.Dao.visibleboard.MemberDao;
import jspteam4.model.Dto.MemberDto;


/**
 * Servlet implementation class MemberInfoController
 */
@WebServlet("/MemberInfoController")
public class MemberInfoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberInfoController() {
        super();
        // TODO Auto-generated constructor stub
    }
 // 1. [C] (첨부파일 없을때)회원가입
 	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 		
 		// 1. AJAX 통신받은 data객체의 '속성명' 요청한다. [ request ] 
 		String mid =  request.getParameter("mid");					System.out.println("mid : "  + mid);
 		String mpwd =  request.getParameter("mpwd");				System.out.println("mpwd : "  + mpwd);
 		String memail =  request.getParameter("memail");			System.out.println("memail : "  + memail);
 		String mnickname =  request.getParameter("mnickname");		System.out.println("mnickname : "  + mnickname);
 		
 		// 2. (선택) 객체화.
 		MemberDto memberDto = MemberDto.builder().mid(mid).mpwd(mpwd).memail(memail).mnickname(mnickname).build();
 		// 2. (선택) 유효성검사.
 		
 		// 3. Dao 에게 전달하고 결과 받는다.
 		boolean result = MemberDao.getInstance().signup(memberDto);
 		
 		// 4. AJAX 통신으로 결과 데이터를 응답을 보낸다. [ response ]
 		response.setContentType("application/json;charset=UTF-8");
 		response.getWriter().print(result);
 	}
	
	// 2. 회원정보(세션호출) / 로그아웃(세션초기화) 호출 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 요청한다. [ 기능 구분을 위한 요청 ]
		String type = request.getParameter("type");
			// * 만약에 type 이 info이면 
			if( type.equals("info") ){
				// * 세션에 저장된 로그인객체를 꺼내기 
				//1. 세션호출한다. [ 세션타입은 Object ]
				Object session = request.getSession().getAttribute("loginDto");
						//2. 타입변환한다. [ 부 -> 자  (캐스팅/강제타입변환) ]
				MemberDto loginDto = (MemberDto)session;
				
					// * DTO는 JS가 이해할수 없는 언어 이므로 JS가 이해할수 있게 JS 언어로 변환 [jackson 라이브러리 ]
				ObjectMapper objectMapper = new ObjectMapper();
				String json =  objectMapper.writeValueAsString( loginDto );
				// 4. 응답한다.
				response.setContentType("application/json; charset=utf-8");
				response.getWriter().print(  json );
			
			}else if( type.equals("logout") ) {
				// * 세션에 저장된 로그인객체를 없애기/초기화/지우기/삭제 
				// 방법1 : (세션의 모든 속성) 초기화하는 함수 
					// request.getSession().invalidate(); // 로그인 정보 뿐만 아니라 모두 삭제..
				// 방법2 : (세션의 특정 속성) 초기화하는 방법 JVM GC(쓰레기수집기= 해당 객체를 아무도 참조하고 있으지 않으면 삭제 ) 
					// 삭제할 세션속성명과 동일하게 null 대입
				request.getSession().setAttribute("loginDto", null);
			}else if( type.equals("search") ) {
				
			String mnickname = request.getParameter("mnickname");
			System.out.println("서블릿에 들어온 닉네임 :" + mnickname);
						MemberDto searchDto = MemberDao.getInstance().searchinfo(mnickname);
						System.out.println("서블릿 searchDto: " + searchDto);
						ObjectMapper objectMapper = new ObjectMapper();
						String json =  objectMapper.writeValueAsString( searchDto );
						System.out.println("서블릿 json : " + json);
						response.setContentType("application/json;charset=UTF-8");
						response.getWriter().print(json); 
			}
	}
	// 3. 회원수정 
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String mnickname =  request.getParameter("mnickname");		System.out.println("mnickname : "  + mnickname);
		String mpwd = request.getParameter("mpwd"); 				System.out.println("mpwd : " + mpwd);
		// Dao [ 로그인된 회원번호 , 수정할 값 ]
		Object object = request.getSession().getAttribute("loginDto");  System.out.println(object);
		MemberDto memberDto = (MemberDto)object; 
		int loginMno = memberDto.getMno();
		
		// 만약에 수정할 닉네임이 없으면 
		if ( mnickname.equals("") ) { // 기존 닉네임 그대로 사용
			// mnickname = memberDto.getMnickname(); // 세션에 있던 닉네임 그대로 대입 
			MemberDto memberDto2 = MemberDao.getInstance().mnofindnicknamepw(loginMno);
			String mnickname2 = memberDto2.getMnickname(); System.out.println("mickname2 : " + mnickname2);
			mnickname = mnickname2;
			System.out.println("닉네임 null 들어왔으니 기존닉네임 " + mnickname + " 대입");
		}
		// 만약에 수정할 비밀번호가 없으면
		if ( mpwd.equals("") ) { // 기존 비밀번호 그대로 사용
			// mpwd = memberDto.getMpwd(); // 세션에 있던 비밀번호 그대로 대입
			MemberDto memberDto2 = MemberDao.getInstance().mnofindnicknamepw(loginMno);
			String mpwd2 = memberDto2.getMpwd(); System.out.println("mpwd2 : " + mpwd2);
			mpwd = mpwd2;
			System.out.println("비밀번호 null 들어왔으니 기존비밀번호 " + mpwd + " 대입");
		}
		
		boolean result = MemberDao.getInstance().mupdate(loginMno, mnickname, mpwd);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(result);
		
	}
	// 4. 회원삭제 
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mpwd = request.getParameter("mpwd"); // 1. 요청한다.
		// 2. 유효성검사/객체화 
		// 3. DAO 처리 [ 현재로그인된 회원번호[pk] , 입력받은 패스워드[mpwd] ]
			// 1. 현재로그인된 회원정보 => 세션 
				// int loginMno = ( (MemberDto)request.getSession().getAttribute("loginDto") ).getMno();
			Object object = request.getSession().getAttribute("loginDto");	// 1. 로그인 세션 호출 한다.
			MemberDto memberDto = (MemberDto)object; // 2. 타입 변환한다.
			int loginMno = memberDto.getMno(); // 3. 로그인객체에 회원번호만 호출한다.
			
			boolean result = MemberDao.getInstance().mdelete(loginMno, mpwd); // 2. Dao 전달해서 결과 받기 
			response.setContentType("application/json;charset=utf-8"); response.getWriter().print(result); // 4. 응답한다.
	}

}

