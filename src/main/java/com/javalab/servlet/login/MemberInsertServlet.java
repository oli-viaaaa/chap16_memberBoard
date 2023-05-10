package com.javalab.servlet.login;

import java.io.IOException;

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
 * 사용자 등록폼, 등록처리 서블릿 클래스
 * @since 2020.02.05
 * @author javalab
 */
@WebServlet("/insertMember")
public class MemberInsertServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/** 싱글톤으로 만들어진 MemberDao 인스턴스 받아옴 */
	private MemberDao memberDao = MemberDao.getInstance();
	
    
    public MemberInsertServlet() {
        super();
    }

	/**
	 * GET 접근 시 (회원물 입력 폼 요청시 응답 메소드)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		// 회원정보 입력폼으로 이동		
		String url = "/member/memberInsertForm.jsp";
		
		//response.sendRedirect(url);
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
		requestDispatcher.forward(request, response);
	}

	/**
	 * POST 접근시 (회원을 등록처리 메소드)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		// POST 한글 파라미터 깨짐 처리
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset:utf-8");
		
		// 파라미터 받기
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		
		// 전달받은 파라미터로 MemberBean 객체 생성
		MemberBean vo = new MemberBean(id, pwd, name, email);
		memberDao = MemberDao.getInstance();
		memberDao.addMember(vo);

		// 회원 삽입 후에 회원 목록 화면으로 이동(목록 서블릿 호출)	
		// sendRedirect는 외부에서 들어오기 때문에 컨텍스트 패스 걸어줘야 함
		String contextPath = request.getContextPath();
		String url = contextPath + "/memberList";	// 회원 목록 서블릿 호출
		response.sendRedirect(url);
		
	}

}
