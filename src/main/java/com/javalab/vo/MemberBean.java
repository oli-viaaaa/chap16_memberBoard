package com.javalab.vo;

public class MemberBean {
	// 멤버 변수
	private String id;
	private String pwd;
	private String name;
	private String email;
	private String joindate;
	
	// 기본 생성자
	public MemberBean() {		
	}
	
	
	public MemberBean(String id, String pwd) {
		super();
		this.id = id;
		this.pwd = pwd;
	}

	// 오버로딩 생성자
	public MemberBean(String id, String pwd, String name, String email, String joindate) {
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
		this.joindate = joindate;
	}

	public MemberBean(String id, String name, String email, String joindate) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.joindate = joindate;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPwd() {
		return pwd;
	}


	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getJoindate() {
		return joindate;
	}


	public void setJoindate(String joindate) {
		this.joindate = joindate;
	}
}
