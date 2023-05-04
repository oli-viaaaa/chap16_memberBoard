package com.javalab.servlet.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javalab.dao.MemberDao;
import com.javalab.vo.BoardVo;

/**
 * Servlet implementation class BoardListServlet
 */
@WebServlet("/boardList")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// 싱글톤으로 만든 MemberDao 객체 얻어옴.
	private MemberDao memberDao = MemberDao.getInstance();
	
	protected void doGet(HttpServletRequest request, 
						HttpServletResponse response) 
						throws ServletException, IOException {
	
	// 1. 회원 목록 조회(Dao에 메서드 생성)
	List<BoardVo> list = memberDao.selectList();
	
	// 2. 조회한 회원 목록을 request 객체에 저장
	request.setAttribute("boardList", list);
	
	// 3. 프로그램 제어의 흐름을 boardList.jsp로 이동
	RequestDispatcher rd = request.getRequestDispatcher("boardList.jsp");
	rd.forward(request, response);
		
	}

}
