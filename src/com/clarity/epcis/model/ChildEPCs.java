package com.clarity.epcis.model;

public class ChildEPCs {

	private String gsPrefix;
	private String itemRef;
	private SubProduct[] subProducts;

	public String getGsPrefix() {
		return gsPrefix;
	}

	public void setGsPrefix(String gsPrefix) {
		this.gsPrefix = gsPrefix;
	}

	public String getItemRef() {
		return itemRef;
	}

	public void setItemRef(String itemRef) {
		this.itemRef = itemRef;
	}

	public SubProduct[] getSubProducts() {
		return subProducts;
	}

	public void setSubProducts(SubProduct[] subProducts) {
		this.subProducts = subProducts;
	}
}
