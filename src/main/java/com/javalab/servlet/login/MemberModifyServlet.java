package com.javalab.servlet.login;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javalab.dao.BoardDao;
import com.javalab.dao.MemberDao;
import com.javalab.vo.BoardVo;
import com.javalab.vo.MemberBean;

/**
 * 게시판 수정폼, 수정처리 서블릿 클래스
 * @since 2020.02.05
 * @author javalab
 */
@WebServlet("/memberModify")
public class MemberModifyServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/** 싱글톤으로 만들어진 MemberDao 인스턴스 받아옴 */
	private MemberDao memberDao = MemberDao.getInstance();
    
    public MemberModifyServlet() {
        super();
    }

	/**
	 * GET 접근 시 (수정폼 접근시)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		System.out.println("여기는 MemberModifyServlet doGet() 메소드");
		
		// 파라미터
		String id = "";
		
		if(request.getParameter("id") != null) {
			id = request.getParameter("id");
		}

		// 회원 목록 조회
		MemberBean member = memberDao.getMemberById(id);
		
		// request에 member 객체 저장
		request.setAttribute("member", member);
		
		//get방식이므로 회원 수정폼으로 이동
		String url = "member/memberUpdateForm.jsp";
		RequestDispatcher rds = request.getRequestDispatcher(url);
		rds.forward(request, response);
	}

	/**
	 * POST 접근시 (수정처리 접근시)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		System.out.println("MemberModifyServlet - doPost()");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset:utf-8");
		
		// 파라미터
		String id = "";
		String pwd = "";
		String name = "";
		String email = "";
		
		if(request.getParameter("id") != null) {
			id = request.getParameter("id");
		}		
		if(request.getParameter("pwd") != null) {
			pwd = request.getParameter("pwd");
		}		
		if(request.getParameter("name") != null) {
			name = request.getParameter("name");
		}		
		if(request.getParameter("email") != null) {
			email = request.getParameter("email");
		}		
		
		MemberBean member = new MemberBean();
		member.setId(id);
		member.setPwd(pwd);
		member.setName(name);
		member.setEmail(email);
		
		// 한명의 회원 정보  조회
		int result = memberDao.updateMember(member);
		
		if(result > 0) {
			System.out.println("회원정보 수정 성공!");
		}else {
			System.out.println("회원정보 수정 실패!");
		}
		
		// [중요] 회원정보 수정후 이동할 곳
		// 1. 톰캣 내부에서 이동하는 forward는  컨텍스트 패스 없어도 됨.
		// 2. 톰캣 밖에서 들어오는 sendRedirect는 컨텍스트 패스 있어야 됨.
		// 3. sendRedirect하게 되면 회원 목록 조회 서블릿을  get방식으로 호출하게 됨.
		// 4. RequestDispatcher를 이용해서 forward 방식으로 다음 서블릿을 호출하면
		//    post방식으로  서블릿이 호출됨  왜냐하면 현재 이 메소드가 post방식으로 호출되었기
		//    때문에 계속해서 post방식으로 진행하기 때문.
		// 5. 만약 다음 페이지를 위해서 정보를 저장하고 싶으면 session.setAttribute("",객체)하면 됨.
		// 	    리다이렉트는 더이상 request를 사용할 수 없기 때문. 단, 쿼리스트링에 달아서 보내도 됨.
		//session.setAttribute("result", "변경 성공");
		String contextPath = request.getContextPath();		
		String url = contextPath + "/memberList";	// 회원 목록 서블릿 호출		
		response.sendRedirect(url);		
	}
}
