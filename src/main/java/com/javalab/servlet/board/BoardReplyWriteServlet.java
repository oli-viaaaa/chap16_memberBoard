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
@WebServlet("/boardReplyWrite")
public class BoardReplyWriteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	/** BOARD DAO */
	private BoardDao boardDao = BoardDao.getInstance();
    
    public BoardReplyWriteServlet() {
        super();
    }

	/**
	 * GET 접근시 (답글 입력폼 요청시 응답 메소드)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		//세션에 로그인 정보 유무 확인
		HttpSession session = request.getSession();
		MemberBean mb = (MemberBean)session.getAttribute("member");

		if(mb != null) {	// 로그인한 회원
			int no = 0;
			// 파라미터 추출
			if(request.getParameter("no") != null) {
				no = Integer.parseInt(request.getParameter("no"));
			}
			
			// 원글 조회
			BoardVo board = boardDao.getBoardById(no);

			// 답글 입력폼으로 이동, 원글에 대한 정보(글번호)와 답글 작성자 갖고 이동		
			request.setAttribute("board", board); 	// 원글 객체
			request.setAttribute("member", mb); 	// 답글 작성자 ID 표시용
			
			String url = "/boardReplyWriteForm.jsp";
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
			requestDispatcher.forward(request, response);
		}else {
			// 로그인 폼으로 이동		
			String contextPath = request.getContextPath();
			String url = contextPath + "/login/loginForm.jsp";
			response.sendRedirect(url);
		}
	}

	/**
	 * POST 접근시 (답글을 입력하고 저장 버튼 클릭시 처리 메소드)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		// POST 한글 파라미터 깨짐 처리
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset:utf-8");
		
		// 파라미터 받기
		int no = 0;			// 원글 게시물 번호
		String title = "";	// 답글 제목
		String content = "";// 답글 내용
		String id = "";		// 답글 작성자
		int reply_group = 0;	// 원글  그룹번호
		int reply_order = 0;	// 원글 순서
		int reply_indent = 0;	// 원글 들여쓰기

		if(request.getParameter("no") != null) {
			no = Integer.parseInt(request.getParameter("no"));
		}
		if(request.getParameter("title") != null) {
			title = request.getParameter("title");
		}
		if(request.getParameter("content") != null) {
			content = request.getParameter("content");
		}
		if(request.getParameter("id") != null) {
			id = request.getParameter("id");
		}
		if(request.getParameter("reply_group") != null) {
			reply_group = Integer.parseInt(request.getParameter("reply_group"));
		}
		if(request.getParameter("reply_order") != null) {
			reply_order = Integer.parseInt(request.getParameter("reply_order"));
		}
		if(request.getParameter("reply_indent") != null) {
			reply_indent = Integer.parseInt(request.getParameter("reply_indent"));
		}
		
		// 전달받은 파라미터로 BoardVo 객체 생성
		BoardVo board = new BoardVo();
		board.setNo(no);
		board.setContent(content);
		board.setTitle(title);
		board.setId(id);
		board.setReply_group(reply_group);
		board.setReply_order(reply_order + 1);	// 답글이니까 원글의 순번+1
		board.setReply_indent(reply_indent + 1);// 답글이니까 원글의 들여쓰기+1
		
		// [디버깅]전달할 객체가 제대로 생성되었지 검증
		System.out.println("reply_group : " + reply_group + " reply_order : " + reply_order);
		
		// 답글 저장전 사전 작업(기존 답글들 뒤로 밀어냄)이 성공하면
		boardDao.reqUpdate(reply_group, reply_order);
		// 답글 저장 
		boardDao.insertReplyBoard(board);		
		
		// 저장후 게시물 목록 페이지로 이동
		String contextPath = request.getContextPath();		
		String url = contextPath + "/boardList";	// 게시물 목록 서블릿 호출		
		response.sendRedirect(url);
	}

}
