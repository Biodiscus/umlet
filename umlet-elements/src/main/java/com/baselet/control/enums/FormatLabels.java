package com.baselet.control.enums;

public enum FormatLabels {
	UNDERLINE("_"),
	BOLD("*"),
	ITALIC("/");

	private String value;

	FormatLabels(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
