package com.javalab.servlet.login;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javalab.dao.MemberDao;
import com.javalab.vo.MemberBean;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** 싱글톤으로 만들어진 MemberDao 인스턴스 받아옴 */
	private MemberDao memberDao = MemberDao.getInstance();

    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, 
					HttpServletResponse response) 
					throws ServletException, IOException {
		
		System.out.println("doGet 로그인 폼으로 이동");
		
		// 로그인 폼으로 이동		
		RequestDispatcher rds = request.getRequestDispatcher("/login/loginForm.jsp");
		rds.forward(request, response);		
	}

	// 로그인 페이지에서 아이디 비밀번호를 넣고 [로그인] 버튼 눌렀을 때
	protected void doPost(HttpServletRequest request, 
					HttpServletResponse response) 
					throws ServletException, IOException {
		
		// 1. 임시 변수 선언
		String id = "";
		String pwd = "";
		String contextPath = request.getContextPath();
		String url = "/login/loginForm.jsp";	//로그인 실패시 이동하는 페이지(다시 로그인 페이지)
		
		// 2. 넘어온 아이디 비번 추출
		id = request.getParameter("id");
		pwd = request.getParameter("pwd");
		
		// 3. 아이디 비밀번호 검증 실패시 오류 메시지 저장
		if (id == null || id.equals("")) {
			RequestDispatcher rds = request.getRequestDispatcher(url);
			rds.forward(request, response);
			return;
		}
		if (pwd == null || pwd.equals("")) {
			RequestDispatcher rds = request.getRequestDispatcher(url);
			rds.forward(request, response);
			return;
		}
		MemberBean mb = new MemberBean(id, pwd);	// Dao에 인자로 보낼 객체 생성
		
		// 4 사용자 아이디/비밀번호 확인
		MemberBean member = memberDao.loginCheck(mb);
		// 4.1 쿼리 결과로 사용자 객체를 받아옴. 객체가 널이면 회원이 없다는 의미
		if(member == null) {
			// 4.2 로그인 오류 세팅
			request.setAttribute("loginErrMsg", Boolean.TRUE);			
			RequestDispatcher rds = request.getRequestDispatcher(url);
			rds.forward(request, response);
			return;
		}else {		
			// 4.3 로그인 성공 -> 세션에 사용자 정보 기록
			HttpSession session = request.getSession();
			
			// 4.4 세션에 회원 객체 저장(Jsp에서 꺼내 쓸때는 ${sessionScope.user.userName})
			// 세선에 저장했기 때문에 어플리케이션 화면 어디서나 확인 가능(로그인 여부 확인할 때)
			session.setAttribute("member", member);
			
			// 4.5 세션의 유지 시간 설정
			session.setMaxInactiveInterval(3600);	// 세션 유지 시간 지정(옵션, 초단위)

			// 4.6 쿠키 생성(세션과 쿠키 병행할 경우에)
			// member - id 쿠키 객체 생성
			Cookie cookie = new Cookie("id", member.getId());
			response.addCookie(cookie);
			System.out.println("로그인 성공! 세션과 쿠키 생성 완료!");
			
			// 4.6 로그인 성공후 게시물 목록 페이지로 이동
			url = contextPath + "/boardList";
			response.sendRedirect(url);	
		}

	}
}
