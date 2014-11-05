package com.ifeng.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil extends GregorianCalendar{
	private static final long serialVersionUID = 2027884641350729981L;
	
	public static long getCurrentMills(){
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        long between = 0;
        try {
            java.util.Date begin = dfs.parse("1970-01-01 00:00:00.0");
            java.util.Date end = dfs.parse(DateUtil.getNow("yyyy-MM-dd HH:mm:ss.SSS"));
            between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            
        }
        return between;
	}
	public static Calendar getNow() {
		return Calendar.getInstance();
	}
	
	public static String getNow(String format) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}
	
	public static int getNowYear() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return Integer.parseInt(sdf.format(calendar.getTime()));
	}
	
	public static Calendar addYear(Calendar date,int year) {
		@SuppressWarnings("static-access")
		GregorianCalendar gc = new GregorianCalendar(date.YEAR, date.MONTH, date.DAY_OF_MONTH);
		gc.add(Calendar.YEAR, year);
		return gc;
	}
	
	public static Calendar addMonth(Calendar date,int month) {
		@SuppressWarnings("static-access")
		GregorianCalendar gc = new GregorianCalendar(date.YEAR, date.MONTH, date.DAY_OF_MONTH);
		gc.add(Calendar.MONTH, month);
		return gc;
	}
	
	public static Calendar addDays(Calendar date,int days) {
		GregorianCalendar gc = new GregorianCalendar(date.get(1), date.get(2),date.get(5));
		gc.add(Calendar.DAY_OF_MONTH, days);
		return gc;
	}
	
	public static String convertToString(Calendar date,String format) {
		SimpleDateFormat sdf=new SimpleDateFormat(format);  
		return sdf.format(date.getTime());  
	}
	
	public String toString(String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format); 
		return sdf.format(getNow());  
	}
	
	public static String convertToString(String date,String format) throws ParseException{
		Date d = convertToDate(date);
		SimpleDateFormat sdf=new SimpleDateFormat(format);  
		
		try {
			return sdf.format(d); 
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return null;
	}
	
	public static Date convertToDate(String date) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
		return sdf.parse(date);
	}
}
