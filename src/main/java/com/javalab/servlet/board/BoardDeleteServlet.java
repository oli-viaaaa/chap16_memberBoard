package com.javalab.servlet.board;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javalab.dao.BoardDao;
import com.javalab.vo.BoardVo;


/**
 * 게시판 삭제 서블릿 클래스
 * @since 2019.05.10
 */
@WebServlet("/boardDelete")
public class BoardDeleteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private BoardDao boardDao = BoardDao.getInstance();
	
    public BoardDeleteServlet() {
        super();
    }

	/**
	 * GET 접근 시 (상세 조회 접근 시)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		// 파라미터
		int no = Integer.parseInt(request.getParameter("no"));
		
		// Dao 객체의 인스턴스 얻기
		//boardDao = BoardDao.getInstance();
		
		// 게시물 삭제
		int result = boardDao.deleteBoard(no);
		if(result > 0) {
			System.out.println("삭제 성공");
		}else {
			System.out.println("삭제 실패");
		}
		
		// 삭제후 페이지 이동
		response.sendRedirect("boardList");
	}

}
