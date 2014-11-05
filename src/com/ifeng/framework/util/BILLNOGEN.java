package com.ifeng.framework.util;

public class BILLNOGEN {
	public static String random(int orderCount) throws Exception {
		int r1 = (int) (Math.random() * (10));// 产生2个0-9的随机数
		int r2 = (int) (Math.random() * (10));
		String now = DateUtil.getNow("yyyyMM");
		return new StringBuilder().append("FH").append(now).append(r1)
				.append(StringUtil.leftPad(String.valueOf(orderCount), 5, "0")).append(r2)
				.toString();// 订单ID

	}
}
