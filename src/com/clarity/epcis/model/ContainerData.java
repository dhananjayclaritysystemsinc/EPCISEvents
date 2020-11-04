package com.clarity.epcis.model;

import java.util.List;

public class ContainerData {

	private String recordId;
	
	private List<SubProduct> subProducts;

	private List<CheckIn> checkin;

	private List<CheckOut> checkout;

	private String ProductCode;

	private String recordType;

	private long statusUpdatedAt;

	private String status;
	
	private String producttype;

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public List<SubProduct> getSubProducts() {
		return subProducts;
	}

	public void setSubProducts(List<SubProduct> subProducts) {
		this.subProducts = subProducts;
	}

	public List<CheckIn> getCheckin() {
		return checkin;
	}

	public void setCheckin(List<CheckIn> checkin) {
		this.checkin = checkin;
	}

	public List<CheckOut> getCheckout() {
		return checkout;
	}

	public void setCheckout(List<CheckOut> checkout) {
		this.checkout = checkout;
	}

	public String getProductCode() {
		return ProductCode;
	}

	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public long getStatusUpdatedAt() {
		return statusUpdatedAt;
	}

	public void setStatusUpdatedAt(long statusUpdatedAt) {
		this.statusUpdatedAt = statusUpdatedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProducttype() {
		return producttype;
	}

	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}
	
}
