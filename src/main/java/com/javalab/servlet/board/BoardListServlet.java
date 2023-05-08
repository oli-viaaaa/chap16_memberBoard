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

@WebServlet("/boardList")
public class BoardListServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private BoardDao boardDao = BoardDao.getInstance();
       
    public BoardListServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("doGet()");
		
		request.setCharacterEncoding("utf-8");
		
		// 게시물 목록을 보여줄 jsp 페이지
		// 톰캣 내부에서 접근하기 때문에 컨텍스트 패스 필요 없음
		String url = "/boardList.jsp";
		
		// 회원 목록 조회
		ArrayList<BoardVo> boardList = boardDao.getBoardList();
		
		// 디버깅 코드
		//System.out.println("게시물 건수 : " + boardList.size());
		
		// request에 목록 객체 저장
		request.setAttribute("boardList", boardList);
		
		// Jsp 화면으로 보내기
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
		requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
