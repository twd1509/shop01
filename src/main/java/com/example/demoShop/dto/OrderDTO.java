package com.example.demoShop.dto;

public class OrderDTO {
	private int no;						//PK
    private int memberNo;				//Member FK
    private int productNo;				//Product FK
    private String title;
    private int count;
    private int productPrice;
    private int deliPrice;
    private int totalPrice;
    private String orderId;
    private String tid;					//카드사 주문 id
    private String payType;				//결제 타입(Card)
    private int payPrice;
    private String email;
    private String address1;
    private String address2;
    private String postNum;
    private String name;
    private String phone;
    private String memo;
    private String deliCompany;
    private String deliCode;
    private String state;				//주문상태(결제완료, 배송중, 배송완료, 취소요청, 취소완료, 반품요청, 반품완료)
    private String regDate;
    private String cancelReason;
    private String returnReason;
    private String updateDate;
    private String paymentDate;			//결제일
    private String deliDate;			//배송 시작일
    
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
	public int getProductNo() {
		return productNo;
	}
	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	public int getDeliPrice() {
		return deliPrice;
	}
	public void setDeliPrice(int deliPrice) {
		this.deliPrice = deliPrice;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public int getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(int payPrice) {
		this.payPrice = payPrice;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getPostNum() {
		return postNum;
	}
	public void setPostNum(String postNum) {
		this.postNum = postNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDeliCompany() {
		return deliCompany;
	}
	public void setDeliCompany(String deliCompany) {
		this.deliCompany = deliCompany;
	}
	public String getDeliCode() {
		return deliCode;
	}
	public void setDeliCode(String deliCode) {
		this.deliCode = deliCode;
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
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getDeliDate() {
		return deliDate;
	}
	public void setDeliDate(String deliDate) {
		this.deliDate = deliDate;
	}
}
