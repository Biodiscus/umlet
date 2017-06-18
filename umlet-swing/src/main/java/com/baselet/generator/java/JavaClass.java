package com.baselet.generator.java;

public interface JavaClass {

	String getName();

	Field[] getFields();

	Method[] getMethods();

	ClassRole getRole();

	enum ClassRole {
		ABSTRACT, CLASS, INTERFACE;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	String getPackage();
}
