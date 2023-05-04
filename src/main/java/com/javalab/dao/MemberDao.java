package com.javalab.dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.javalab.vo.BoardVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;


/**
 * C/R/U/D 입력/조회/삭제 기능 - 실제 oracle DB에 접속하는 역할을 하며 각 Servlet에서 사용
 */

public class MemberDao {
  // 멤버 변수
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
   // 데이터 베이스에 대한 소스 정보 객체
   private DataSource dataSource;
   
   // 자신의 참조 변수를 private static으로 선언
   private static MemberDao instance;
  
	/*
	 * [싱글턴 패턴 생성자] 
	 * 생성자를 private으로 선언 
	 * - 외부에서는 이 생성자를 부를 수 없음 
	 * - getInstance() 메소드에서 최초로 한번만 객체로 생성됨.
	 */
   private MemberDao() {
	   System.out.println("여기는 MemberDao 생성자");
	   try {
		   Context ctx = new InitialContext();
		   Context envContext = (Context) ctx.lookup("java:/comp/env");
		   dataSource = (DataSource) envContext.lookup("jdbc/oracle");
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
   }
   
   /*
    * 현재 객체의 참조 변수를 반환해주는 매소드
    *  - 이 메소드가 최초로 호출 될 때 단 한번만 자신이 속한 클래스의 객체를 생성 
	 * - 다음부터는 최초에 생성된 그 객체의 주소만 반환하게 됨. 
	 * - 현재 클래스의 객체가 있는지 확인해서 없으면 객체 생성(최초 호출 될 때)
    */
   public static MemberDao getInstance() {
	   if (instance == null) 
		   instance = new MemberDao();
		return instance;
   }

   // 게시물 목록 조회 : BoardListServlet의 doGet()에서 호풀
	public List<BoardVo> selectList() {
		String sql_query = " select * from tbl_board";
		List<BoardVo> list = null;
		
		try {
			con = dataSource.getConnection(); // 커넥션 객체 얻기
			pstmt = con.prepareStatement(sql_query);
			rs = pstmt.executeQuery();
			
			list = new ArrayList<BoardVo>();
			BoardVo vo = null;
			
			while (rs.next()) {
				vo = new BoardVo();
				vo.setNo(rs.getInt("no"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setId(rs.getString("id"));
				vo.setHit(rs.getInt("hit"));
				vo.setRegdate(rs.getString("regdate"));
				vo.setReply_group(rs.getInt("reply_group"));
				vo.setReply_order(rs.getInt("reply_order"));
				vo.setReply_indent(rs.getInt("reply_indent"));
				list.add(vo);
				vo = null;
			}
		} catch (SQLException e) {
			System.out.println("selectList() ERR => " +e.getMessage());
		} finally {
			close();
		}
		return list;
	}

	// 사용이 끝난 자원 해제
	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			System.out.println("CLOSED ERR : " + e.getMessage());
		}
	}
 
   
}
