package com.javalab.servlet.board;

import java.io.IOException;
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
 * 게시판 상세 조회 서블릿 클래스
 * @since 2020.02.05
 * @author javalab
 */
@WebServlet("/boardView")
public class BoardViewServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/** BOARD DAO */
	private BoardDao boardDao = BoardDao.getInstance();
	
    public BoardViewServlet() {
        super();
    }

	/**
	 * GET 접근 시 (상세 조회 접근 시)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		System.out.println("BoardViewServlet doGet()");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset:utf-8");
		
		String url = "/boardDetailView.jsp";
		
		// 파라미터
		int no = 0;		
		if(request.getParameter("no") != null) {
			no = Integer.parseInt(request.getParameter("no").toString());
		}

		// Dao 객체의 인스턴스 얻기
		//boardDao = new BoardDao(); //BoardDao.getInstance();
		//boardDao = BoardDao.getInstance();
		
		// 게시물 조회스 증가
		boardDao.updateHitCount(no);
		
		// 게시물 목록을 조회
		BoardVo board = boardDao.getBoardById(no);
		
		// request에 board 객체 저장
		request.setAttribute("board", board);
		
		RequestDispatcher rds = request.getRequestDispatcher(url);
		rds.forward(request, response);
	}

}
