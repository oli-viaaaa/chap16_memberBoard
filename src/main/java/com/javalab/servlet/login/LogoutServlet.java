package com.javalab.servlet.login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LogoutServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1. 세션 무효화
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
			System.out.println("세션을 무효화 하였습니다.");
		}
		
		// 2. 쿠키 만료
		Cookie cookie = new Cookie("id", "");
		if(cookie != null) {
			cookie.setMaxAge(0); // 쿠키 만료
			response.addCookie(cookie);
			System.out.println("쿠키를 삭제하였습니다.");			
		}
		
		String url = "/boardList";	//로그아웃 후에 다시 게시물 목록 페이지 재호출
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
		requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
