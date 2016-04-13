package com.shouyang.syazs.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EntityUtils {
	public static void trimAllStrings(Object beanObject) throws Exception {
		Exception noSuchMethodException = null;
		boolean throwNoSuchMethodException = false;
		if (beanObject != null) {

			Method[] methods = null;

			try {
				methods = beanObject.getClass().getDeclaredMethods();
			} catch (SecurityException e) {
				throw new Exception(e);
			}

			if (methods != null) {

				for (Method method : methods) {

					String methodName = method.getName();

					if (!methodName.equals("getClass")) {

						String returnType = method.getReturnType().toString();
						String commonMethodName = null;

						if (methodName.startsWith("get")
								&& "class java.lang.String".equals(returnType)) {

							commonMethodName = methodName.replaceFirst("get",
									"");
							String returnedValue = null;

							try {
								returnedValue = (String) method
										.invoke(beanObject);
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
								throw e;
							} catch (IllegalAccessException e) {
								e.printStackTrace();
								throw e;
							} catch (InvocationTargetException e) {
								e.printStackTrace();
								throw e;
							}

							if (returnedValue != null) {
								StringBuilder setterMethodName = new StringBuilder();
								setterMethodName.append("set");
								setterMethodName.append(commonMethodName);
								Method setterMethod = null;

								try {
									setterMethod = beanObject
											.getClass()
											.getMethod(
													setterMethodName.toString(),
													String.class);

									if (setterMethod != null) {
										if (returnedValue.isEmpty()) {
											Object o = null;
											setterMethod.invoke(beanObject, o);
										} else {
											setterMethod.invoke(beanObject,
													(returnedValue.trim()));
										}
									}
								} catch (SecurityException e) {
									e.printStackTrace();
									throw e;
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
									if (!throwNoSuchMethodException) {
										noSuchMethodException = e;
									}
									throwNoSuchMethodException = true;
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
									throw e;
								} catch (IllegalAccessException e) {
									e.printStackTrace();
									throw e;
								} catch (InvocationTargetException e) {
									e.printStackTrace();
									throw e;
								}
							}
						}
					}
				}
			}
		}

		if (throwNoSuchMethodException && noSuchMethodException != null) {
			throw noSuchMethodException;
		}
	}

}
