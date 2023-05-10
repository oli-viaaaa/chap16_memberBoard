package com.javalab.servlet.login;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javalab.dao.BoardDao;
import com.javalab.dao.MemberDao;
import com.javalab.vo.BoardVo;


/**
 * 회원 삭제 서블릿 클래스
 * @since 2019.05.10
 */
@WebServlet("/memberDelete")
public class MemberDeleteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/** 싱글톤으로 만들어진 MemberDao 인스턴스 받아옴 */
	private MemberDao memberDao = MemberDao.getInstance();	
	
    public MemberDeleteServlet() {
        super();
    }

	/**
	 * GET 접근시 (삭제) - 회원목록에서 <a href> 방식으로 오면 get방식
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		// 파라미터
		String id = "";
		
		if(request.getParameter("id") != null) {
			id = request.getParameter("id");
		}
		
		// 게시물 삭제
		int result = memberDao.deleteMember(id);
		if(result > 0) {
			System.out.println("삭제 성공");
		}else {
			System.out.println("삭제 실패");
		}
		
		// 삭제후 페이지 이동
		String contextPath = request.getContextPath();		
		String url = contextPath + "/memberList";	// 회원 목록 서블릿 호출		
		response.sendRedirect(url);
	}

}
