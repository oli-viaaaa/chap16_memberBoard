package com.javalab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.javalab.vo.BoardVo;


/**
 * 게시판 DAO 클래스 (데이터베이스 C/R/U/D)
 * @since 2020.02.05
 * @author javalab
 */
public class BoardDao {
	
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private static DataSource dataSource;	
	private static BoardDao instance;
	
	private BoardDao() {
		System.out.println("여기는 BoardDao 생성자");
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	// 싱글톤 팬턴으로 생성
	public static BoardDao getInstance() {
		if (instance == null)
			instance = new BoardDao();
		return instance;
	}
	
	// 게시물 목록 조회 메소드
	public ArrayList<BoardVo> getBoardList() {

		ArrayList<BoardVo> boardList = new ArrayList<BoardVo>();
		try {
			con = dataSource.getConnection();	//커넥션 객체 얻기

			StringBuffer query = new StringBuffer();
			query.append("select no, title, content, id, hit, regdate, reply_group, reply_order, reply_indent ");			
			query.append("from( ");			
			query.append("     select rownum rnum, no, title, content, id, hit, regdate, reply_group, reply_order, reply_indent");			
			query.append("     from (");			
			query.append("		select no, title, content, id, hit, regdate, reply_group, reply_order, reply_indent");			
			query.append("		from tbl_board");			
			query.append("		order by reply_group desc, reply_order, reply_indent asc");			
			query.append("     )");			
			query.append(")");			
			//sql.append("where rnum >= 1 and rnum <=10"); // 페이징에서 필요			
			
			System.out.println("SQL :  " + query.toString());
			
			pstmt = con.prepareStatement(query.toString());
			rs = pstmt.executeQuery();
			
			BoardVo board = null;
			while(rs.next()) {
				board = new BoardVo();
				
				board.setNo(rs.getInt("no"));
				board.setTitle(rs.getString("title"));
				board.setId(rs.getString("id"));
				board.setHit(rs.getInt("hit"));
				board.setRegdate(rs.getString("regdate"));
				board.setReply_group(rs.getInt("reply_group"));
				board.setReply_order(rs.getInt("reply_order"));
				board.setReply_indent(rs.getInt("reply_indent"));
				
				boardList.add(board);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();			
		}
		return boardList;
	}

	// 게시물 삭제 메소드
	public int deleteBoard(int no) {
		int result = 0;
		try {
			con = dataSource.getConnection();
			
			String query = "delete from tbl_board where no=?";
		
			System.out.println("SQL :  " + query);

			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, no);

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close();			
		}
		return result;		
	}

	// 한 명의 회원 조회 메소드
	public BoardVo getBoardById(int no) {
		BoardVo board = null;
		try {
			con = dataSource.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("select no, title, content, id, hit, regdate, reply_group, reply_order, reply_indent");			
			query.append(" from tbl_board ");			
			query.append(" where no = ?");			
			
			System.out.println("SQL :  " + query.toString());
			
			pstmt = con.prepareStatement(query.toString());
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				board = new BoardVo();
				board.setNo(rs.getInt("no"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setId(rs.getString("id"));
				board.setHit(rs.getInt("hit"));
				board.setRegdate(rs.getString("regdate"));
				board.setReply_group(rs.getInt("reply_group"));
				board.setReply_order(rs.getInt("reply_order"));
				board.setReply_indent(rs.getInt("reply_indent"));			
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();			
		}
		return board;
	}	
	
	// 회원 수정 메소드
	public int modifyBoard(BoardVo boardVo) {
		int result = 0;
		try {
			con = dataSource.getConnection();
			
			int no = boardVo.getNo();
			String title = boardVo.getTitle();
			String content = boardVo.getContent();
			String id = boardVo.getId();
			
			String query = "update tbl_board set title=?, content=?";
			query += " where no=?";
			
			System.out.println("SQL :  " + query);

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, no);

			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close();			
		}
		return result;
	}

	// 새로운 시퀀스를 조회해서 알려준다.
	public int getSeq()
	{
		int result = 1;		
		try {
			con = dataSource.getConnection();
			
			// 시퀀스 값을 가져온다. (dual : 시퀀스 값을 가져오기위한 임시 테이블)
			StringBuffer query = new StringBuffer();
			query.append("select seq_board_no.nextval from dual");
			
			pstmt = con.prepareStatement(query.toString());
			// 쿼리 실행
			rs = pstmt.executeQuery();
			
			if(rs.next()) {	
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}finally {
			// 여기서 close() 안함 호출한 곳에서 con을 계속 사용해야 하므로
			//close();			
		}
		
		return result;	
	} 	
	
	/**
	 * 새 게시물 저장(답글이 아닌) 메소드
	 * - 특이한 점은 sequence를 사용하지 않고 getSeq()라는 메소드를 이용.
	 * - 기존 방식대로 그냥 시퀀스를 사용해도 됨. 
	 * @param board
	 * @return
	 */
	public int insertNewBoard(BoardVo board) {
		int result = 0;
		try {
			con = dataSource.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("insert into tbl_board(");
			query.append(" no, title, content, id, hit, reply_group, reply_order, reply_indent)");
			query.append(" values ");
			query.append(" (?,?,?,?,?,?,?,?)");

			int no = getSeq();	// 시퀀스를 이용하여 글번호가 될 번호 얻음
			
			pstmt = con.prepareStatement(query.toString());
			
			System.out.println("no : " + no);
			
			pstmt.setInt(1, no);
			pstmt.setString(2, board.getTitle());	// 제목
			pstmt.setString(3, board.getContent());	// 내용
			pstmt.setString(4, board.getId());		// 작성자 ID
			pstmt.setInt(5, 0);				// 조회수
			pstmt.setInt(6, no);			// 그룹번호(새글의 글번호를 똑같이 넣어줌)
			pstmt.setInt(7, 0);				// 그룹내 번호
			pstmt.setInt(8, 0);				// 들여쓰기

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close();			
		}
		return result;		
	}
	
	// 답변 게시물 삽입 메소드
	public int insertReplyBoard(BoardVo board) {
		System.out.println("insertReplyBoard");

		int result = 0;
		try {
			con = dataSource.getConnection();
			
			StringBuffer query = new StringBuffer();
			query.append("insert into tbl_board(");
			query.append("no, title, content, id, reply_group, reply_order, reply_indent)");
			query.append(" values ");
			query.append(" (seq_board_no.nextval,?,?,?,?,?,?)");
			System.out.println("SQL :  " + query.toString());
			
			int no = board.getNo();
			System.out.println("insertReplyBoard no : " + no);
			pstmt = con.prepareStatement(query.toString());
			// 답변글 정보
			pstmt.setString(1, board.getTitle());	// 제목
			pstmt.setString(2, board.getContent());	// 내용
			pstmt.setString(3, board.getId());		// 작성자 ID
			
			// 원글의 그룹번호를 넣어줌(그래야 원글과 답글이 연결됨)
			pstmt.setInt(4, board.getReply_group());	// 답글의 그룹번호 = 원글의 그룹번호를 넣어줌
			pstmt.setInt(5, board.getReply_order());	// 그룹내 번호 = 원글의 order + 1
			pstmt.setInt(6, board.getReply_indent());	// indent번호 = 원글의 indent + 1
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close();			
		}
		return result;		
	}
	
	// 조회수 증가 메소드(게시물이 한 번 읽힐때마다 증가)
	public void updateHitCount(int no) {
		try {
			con = dataSource.getConnection();
			
			String query = "update tbl_board set hit = hit + 1";
			query += " where no = ?";
			System.out.println("SQL :  " + query);

			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, no);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close();			
		}
	}

	/**
	 * [답글 저장전 사전작업] commit & rollback 나눠서 처리함!
	 * 답글 저장 전에 현재 원글의 order 보다 큰 글들 즉, 기존 답글들의 order+1 해서 순번이 뒤로 밀리게 함
	 * 그리고 밀린 자리에 현재 답글이 위치하게 됨.(답글중 가장 작은 order를 갖게 되어 부모 바로 밑으로 오게됨)
	 */
	public boolean reqUpdate(int reply_group, int reply_order) {
		System.out.println("reqUpdate reply_group : " + reply_group +" reply_order : " + reply_order);
		boolean result = false;
		
		try {
			StringBuffer query = new StringBuffer();
			con = dataSource.getConnection();
			
			// group(그룹번호)와 order(답글순서)를 확인하여 원본 글에 다른 답변이 있으면
			// 답변글 중에서 답변 글보다 상위에 있는 즉 seq가 큰 답글들을 seq 값을 +1
			// 이전의 답글들이 뒤로 밀리고 그 자리에 현재 답변글이 들어가게 됨.
			query.append("update tbl_board set reply_order = reply_order + 1");
			query.append(" where reply_group = ? and reply_order > ?");
			
			pstmt = con.prepareStatement(query.toString());
			pstmt.setInt(1, reply_group);
			pstmt.setInt(2, reply_order);
			int flag = pstmt.executeUpdate();

			System.out.println("flag : " + flag);
			// 업데이트가 정상적으로 처리되었으면 
			if(flag > 0) {
				result = true;
				//con.commit();	// 커밋
			}
		}catch(Exception e) {
			try {
				con.rollback();	// 오류시 롤백(업뎃 원래대로 복원)
			}catch (SQLException sqe) {
				sqe.printStackTrace();
			}
			throw new RuntimeException(e.getMessage());	
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
				pstmt=null; 
			}
			if ( con != null ){ 
				con.close(); 
				con=null;	
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	} // end close()	
}
