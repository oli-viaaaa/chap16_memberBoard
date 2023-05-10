package com.javalab.servlet.board;

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
import com.javalab.vo.BoardVo;

/**
 * 게시판 수정폼, 수정처리 서블릿 클래스
 * @since 2020.02.05
 * @author javalab
 */
@WebServlet("/boardModify")
public class BoardModifyServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/** BOARD DAO */
	private BoardDao boardDao = BoardDao.getInstance();
    
    public BoardModifyServlet() {
        super();
    }

	/**
	 * GET 접근 시 (수정폼 접근 시)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		String url = "boardModifyForm.jsp";
		
		// 파라미터
		int no = 0;
		
		if(request.getParameter("no") != null) {
			no = Integer.parseInt(request.getParameter("no").toString());
		}

		// 수정할 게시물 한개 조회
		BoardVo board = boardDao.getBoardById(no);
		
		// request에 board 객체 저장(업데이트 폼에 보여주기 위해서)
		request.setAttribute("board", board);
		
		RequestDispatcher rds = request.getRequestDispatcher(url);
		rds.forward(request, response);
		
	}

	/**
	 * POST 접근 시 (수정처리 접근 시)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		System.out.println("BoardModifyServlet - doPost()");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset:utf-8");
		
		// 파라미터
		int no = 0;
		String title = "";
		String content = "";
		String id = "";
		
		if(request.getParameter("no") != null) {
			no = Integer.parseInt(request.getParameter("no").toString());
		}		
		if(request.getParameter("title") != null) {
			title = request.getParameter("title").toString();
		}		
		if(request.getParameter("content") != null) {
			content = request.getParameter("content").toString();
		}		
		if(request.getParameter("id") != null) {
			id = request.getParameter("id").toString();
		}		
		
		BoardVo vo = new BoardVo();
		vo.setNo(no);
		vo.setTitle(title);
		vo.setContent(content);
		vo.setId(id);
		
		// 게시물 목록을 조회
		int result = boardDao.modifyBoard(vo);
		if(result > 0) {
			System.out.println("게시물 수정 성공!");
		}else {
			System.out.println("게시물 수정 실패!");
		}
		
		// 저장후 게시물 목록 페이지로 이동
		// 컨텍스트 패스를 붙이지 않아도 이동은 하지만 일반적으로 붙여서 이동해야함.
		// 왜냐하면 다른 프로젝트(컨텍스트)로 이동할 수도 있기 때문에.
		String contextPath = request.getContextPath();		
		String url = contextPath + "/boardList";	// 게시물 목록 서블릿 호출		
		System.out.println(url);
		response.sendRedirect(url);
	}
}
