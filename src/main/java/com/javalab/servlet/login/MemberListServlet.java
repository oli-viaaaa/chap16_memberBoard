package com.javalab.servlet.login;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javalab.dao.BoardDao;
import com.javalab.dao.MemberDao;
import com.javalab.vo.BoardVo;
import com.javalab.vo.MemberBean;

@WebServlet("/memberList")
public class MemberListServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/** 싱글톤으로 만들어진 MemberDao 인스턴스 받아옴 */
	private MemberDao memberDao = MemberDao.getInstance();
       
    public MemberListServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("MemberListServlet - doGet()");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset:utf-8");

		
		// 게시물 목록을 조회
		ArrayList<MemberBean> memberList = memberDao.listMembers();
		
		// 디버깅 코드
		System.out.println("회원수 : " + memberList.size());
		
		// request에 회원 목록 객체 저장
		request.setAttribute("memberList", memberList);

		// 회원 목록을 보여줄 jsp 페이지
		// 톰캣 내부에서 접근하기 때문에 컨텍스트 패스 필요 없음
		String url = "/member/memberList.jsp";
		
		// Jsp 화면으로 이동
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
		requestDispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
