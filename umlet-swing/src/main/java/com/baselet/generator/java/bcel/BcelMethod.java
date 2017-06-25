package com.baselet.generator.java.bcel;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Type;

public class BcelMethod extends BcelAccessible implements com.baselet.generator.java.Method {

	private final Method method;
	private final String className;
	private boolean isConstructor;

	public BcelMethod(Method method, String className) {
		super(method);
		this.method = method;
		this.className = className;
		isConstructor = method.getName().equals("<init>") || method.getName().equals("<clinit>");
	}

	@Override
	public String getName() {
		if (isConstructor) {
			return className;
		}
		return method.getName();
	}

	@Override
	public String getReturnType() {
		if (isConstructor) {
			return "ctor";
		}
		return method.getReturnType().toString();
	}

	@Override
	public String getSignature() {
		StringBuilder sb = new StringBuilder("");
		Type[] arguments = method.getArgumentTypes();
		boolean first = true;
		for (Type argument : arguments) {
			if (first) {
				first = false;
				sb.append(argument);
			}
			else {
				sb.append(", ").append(argument);
			}
		}
		return sb.toString();
	}
}
