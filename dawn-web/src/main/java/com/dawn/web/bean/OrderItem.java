package com.dawn.web.bean;


public class OrderItem {
	/**商品id */
	private Long itemId;
	/**订单id */
	private String orderId;
	/** 商品购买数量*/
	private Integer num;
	/**商品标题 */
	private String title;
	/**商品单价 */
	private Long price;
	/**商品总价 */
	private Long totalFee;
	/**图片路径 */
	private String picPath;
	
	public Long getItemId() {
		return itemId;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Long getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Long totalFee) {
		this.totalFee = totalFee;
	}
	public Integer getNum() {
		return num;
	}
	
}
