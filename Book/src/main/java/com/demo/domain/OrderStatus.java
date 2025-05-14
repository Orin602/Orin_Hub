package com.demo.domain;

public enum OrderStatus {

	COMPLETED("확정"), PENDING("대기"), CANCELED("취소"), PROCESSING("처리중"), SHIPPED("배송됨");

	private final String displayName;

	OrderStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
