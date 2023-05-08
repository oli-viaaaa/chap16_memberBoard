package com.javalab.vo;


/**
 * 게시판 vo 클래스
 * @since 2020.02.05
 * @author javalab
 */
public class BoardVo {
	
	/** 번호 */
	private int no;
	/** 제목 */
	private String title;
	/** 내용 */
	private String content;
	/** 작성자ID */
	private String id;
	/** 조회수 */
	private int hit;
	/** 등록 일시 */
	private String regdate;
	/** 그룹번호 */
	private int reply_group;
	/** 그룹내순서 */
	private int reply_order;	
	/** 들여쓰기 */
	private int reply_indent;
	
	public BoardVo() {
		super();
	}

	public BoardVo(String title, String content, String id) {
		this.title = title;
		this.content = content;
		this.id = id;
	}

	public BoardVo(int no, String title, String content, String id) {
		super();
		this.no = no;
		this.title = title;
		this.content = content;
		this.id = id;
	}

	
	
	public int getReply_group() {
		return reply_group;
	}

	public void setReply_group(int reply_group) {
		this.reply_group = reply_group;
	}

	public int getReply_order() {
		return reply_order;
	}

	public void setReply_order(int reply_order) {
		this.reply_order = reply_order;
	}

	public int getReply_indent() {
		return reply_indent;
	}

	public void setReply_indent(int reply_indent) {
		this.reply_indent = reply_indent;
	}

	public int getNo() {
		return no;
	}



	public void setNo(int no) {
		this.no = no;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}




	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}


}
