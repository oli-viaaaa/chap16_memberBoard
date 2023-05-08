package com.javalab.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.javalab.vo.BoardVo;
import com.javalab.vo.MemberBean;

public class MemberDao {
	
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private DataSource dataSource;
	private static MemberDao instance;

	// 기본 생성자에서 환경변수를 통한 데이터베이스 관련 DataSource얻어옴
	// Server / contex.xml에 Resource로 세팅해놓은 정보
	public MemberDao() {
		System.out.println("여기는 MemberDao 생성자");
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 싱글톤 팬턴으로 생성
	public static MemberDao getInstance() {
		if (instance == null)
			instance = new MemberDao();
		return instance;
	}	

	// 회원 목록 조회 메소드
	public ArrayList<MemberBean> listMembers() {
		ArrayList<MemberBean> list = new ArrayList<>();
		try {
			con = dataSource.getConnection();
			
			String query = "select * from tbl_member order by joindate desc";
			System.out.println("SQL :  " + query);
			
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String joindate = rs.getString("joindate");
				
				MemberBean vo = new MemberBean();
				vo.setId(id);
				vo.setPwd(pwd);
				vo.setName(name);
				vo.setEmail(email);
				vo.setJoindate(joindate);
				list.add(vo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close();			
		}
		return list;
	}

	// 회원 저장 메소드
	public void addMember(MemberBean memberBean) {
		try {
			
			Connection con = dataSource.getConnection();
			
			String id = memberBean.getId();
			String pwd = memberBean.getPwd();
			String name = memberBean.getName();
			String email = memberBean.getEmail();
			
			String query = "insert into tbl_member";			
			query += " (id,pwd,name,email)";
			query += " values(?,?,?,?)";
			
			System.out.println("SQL :  " + query);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close();			
		}
	}

	// 한 명의 회원 조회 메소드(상세정보 보기 위해서)
	public MemberBean getMemberById(String id) {
		MemberBean member = null;
		try {
			con = dataSource.getConnection();
			
			String query = "select id, pwd, name, email, to_char(joindate, 'yyyy-mm-dd') as joindate "
					+ "from tbl_member "
					+ " where id = ?";
			
			System.out.println("SQL :  " + query);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				member = new MemberBean();
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setEmail(rs.getString("email"));
				member.setJoindate(rs.getString("joindate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();			
		}
		return member;
	}	

	// 사용자가 데이터베이스에 있는지 조회
	public MemberBean loginCheck(MemberBean mb) {
		MemberBean memberBean = null;	
		try {
			// 1. 데이터소스에서 커넥션 객체 얻음
			Connection con = dataSource.getConnection();
			// 2. SQL쿼리문장 생성
			String sql = "select id, name, email, joindate from tbl_member where id = ? and pwd = ?";
			// 3. 쿼리문 실행
			pstmt = con.prepareStatement(sql);
			// 4. 인자 전달
			pstmt.setString(1, mb.getId());
			pstmt.setString(2, mb.getPwd());
			// 5. 결과 반환
			rs = pstmt.executeQuery();

			if(rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String joindate = rs.getString("joindate");
				memberBean = new MemberBean(id, name, email, joindate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();			
		}
		return memberBean;
	}		
	
	// 회원 수정 메소드
	public int updateMember(MemberBean member) {
		int result = 0;
		try {
			Connection con = dataSource.getConnection();
			
			String id = member.getId();
			String pwd = member.getPwd();
			String name = member.getName();
			String email = member.getEmail();
			
			String query = "update tbl_member set pwd=?, name=?, email=?";
			query += " where id=?";
			
			System.out.println("SQL :  " + query);

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, pwd);
			pstmt.setString(2, name);
			pstmt.setString(3, email);
			pstmt.setString(4, id);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close();			
		}
		return result;
	}	
	
	// 회원 삭제 메소드
	public int deleteMember(String id) {
		int result = 0;
		try {
			Connection con = dataSource.getConnection();
			
			String query = "delete from tbl_member where id=?";
			System.out.println("SQL :  " + query);

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close();			
		}
		return result;
	}
	
	// DB 자원해제
	private void close()
	{
		try {
			if ( pstmt != null ){ 
				pstmt.close(); 
			}
			if ( con != null ){ 
				con.close(); 
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	} // end close()	
		
}
