package com.example.demoShop.dto;

public class CategoryDTO {
	private int no;			//PK
	private int memberNo;	//Member FK
	private String id;
	private int groupNo;
	private int refNo;
	private int depth;
	private String title;
	private String state;
	private String regDate;
	private String uptDate;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}
	public int getRefNo() {
		return refNo;
	}
	public void setRefNo(int refNo) {
		this.refNo = refNo;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getUptDate() {
		return uptDate;
	}
	public void setUptDate(String uptDate) {
		this.uptDate = uptDate;
	}
}
