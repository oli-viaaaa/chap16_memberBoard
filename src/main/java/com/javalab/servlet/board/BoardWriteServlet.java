package com.javalab.servlet.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javalab.dao.BoardDao;
import com.javalab.vo.BoardVo;
import com.javalab.vo.MemberBean;


/**
 * 게시판 등록폼, 등록처리 서블릿 클래스
 * @since 2020.02.05
 * @author javalab
 */
@WebServlet("/boardWrite")
public class BoardWriteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private BoardDao boardDao = BoardDao.getInstance();
    
	/**
	 * GET 접근 시 (게시물 입력폼 요청시 응답 메소드)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		//세션에 로그인 정보 유무 확인
		HttpSession session = request.getSession();
		MemberBean mb = (MemberBean)session.getAttribute("member");
		
		// 로그인한 회원
		if(mb != null) {	
			// 게시물 입력폼으로 이동		
			String url = "/boardWriteForm.jsp";
			request.setAttribute("member", mb); // 게시물 입력폼에서 작성자 부분에 id표시
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
			requestDispatcher.forward(request, response);
		}else {
			// 로그인 안한 사용자는 로그인 폼으로 이동		
			String contextPath = request.getContextPath();
			String url = contextPath + "/login/loginForm.jsp";
			response.sendRedirect(url);
		}
	}

	/**
	 * POST 접근시 (게시물을 입력하고 저장 버튼 클릭시 처리 메소드)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		// POST 한글 파라미터 깨짐 처리
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset:utf-8");
		
		// 파라미터 받기
		String title = "";
		String content = "";
		String id = "";
		
		if(request.getParameter("title") != null) {
			title = request.getParameter("title");
		}
		if(request.getParameter("content") != null) {
			content = request.getParameter("content");
		}
		if(request.getParameter("id") != null) {
			id = request.getParameter("id");
		}
		
		// 파리미터가 제대로 전달되는지 검증
		//System.out.println(title + "  " + content + " " + id);
		
		// 전달받은 파라미터로 BoardVo 객체 생성
		BoardVo vo = new BoardVo(title, content, id);
		boardDao.insertNewBoard(vo);
		
		// 저장후 게시물 목록 페이지로 이동
		// 컨텍스트 패스를 붙이지 않아도 이동은 하지만 일반적으로 붙여서 이동해야함.
		// 왜냐하면 다른 프로젝트(컨텍스트)로 이동할 수도 있기 때문에.
		String contextPath = request.getContextPath();		
		String url = contextPath + "/boardList";	// 게시물 목록 서블릿 호출		
		response.sendRedirect(url);
	}

}
