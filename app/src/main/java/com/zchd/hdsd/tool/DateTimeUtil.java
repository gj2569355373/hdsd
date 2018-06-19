package com.zchd.hdsd.tool;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间处理工具类
 * 
 * @author laichendong
 * @since 2010年2月22日23:09:09
 *
 */
public class DateTimeUtil {

	private long lNow = System.currentTimeMillis();
	private Calendar cNow = Calendar.getInstance();
	private Date dNow = new Date(lNow);
	private Timestamp tNow = new Timestamp(lNow);
	private java.sql.Date today = new java.sql.Date(lNow);
	private java.sql.Time now = new java.sql.Time(lNow);

	/**
	 * 默认构造方法
	 */
	public DateTimeUtil() {

	}

	/*
	 * private DateTimeUtil(long lNow, Calendar cNow, Date dNow, Timestamp tNow,
	 * java.sql.Date today, Time now) { this.lNow = lNow; this.cNow = cNow;
	 * this.dNow = dNow; this.tNow = tNow; this.today = today; this.now = now; }
	 */

	/**
	 * 得到年
	 * 
	 * @param c
	 * @return
	 */
	public static int getYear(Calendar c) {
		if (c != null) {
			return c.get(Calendar.YEAR);
		} else {
			return Calendar.getInstance().get(Calendar.YEAR);
		}
	}

	/**
	 * 得到月份 注意，这里的月份依然是从0开始的
	 * 
	 * @param c
	 * @return
	 */
	public static int getMonth(Calendar c) {
		if (c != null) {
			return c.get(Calendar.MONTH);
		} else {
			return Calendar.getInstance().get(Calendar.MONTH);
		}
	}

	/**
	 * 得到日期
	 * 
	 * @param c
	 * @return
	 */
	public static int getDate(Calendar c) {
		if (c != null) {
			return c.get(Calendar.DATE);
		} else {
			return Calendar.getInstance().get(Calendar.DATE);
		}
	}

	/**
	 * 得到星期
	 * 
	 * @param c
	 * @return
	 */
	public static int getDay(Calendar c) {
		if (c != null) {
			return c.get(Calendar.DAY_OF_WEEK);
		} else {
			return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		}
	}

	/**
	 * 得到小时
	 * 
	 * @param c
	 * @return
	 */
	public static int getHour(Calendar c) {
		if (c != null) {
			return c.get(Calendar.HOUR);
		} else {
			return Calendar.getInstance().get(Calendar.HOUR);
		}
	}

	/**
	 * 得到分钟
	 * 
	 * @param c
	 * @return
	 */
	public static int getMinute(Calendar c) {
		if (c != null) {
			return c.get(Calendar.MINUTE);
		} else {
			return Calendar.getInstance().get(Calendar.MINUTE);
		}
	}

	/**
	 * 得到秒
	 * 
	 * @param c
	 * @return
	 */
	public static int getSecond(Calendar c) {
		if (c != null) {
			return c.get(Calendar.SECOND);
		} else {
			return Calendar.getInstance().get(Calendar.SECOND);
		}
	}

	/**
	 * 得到指定或者当前时间前n天的Calendar
	 * 
	 * @param c
	 * @param n
	 * @return
	 */
	public static Calendar beforeNDays(Calendar c, int n) {
		// 偏移量，给定n天的毫秒数
		long offset = n * 24 * 60 * 60 * 1000;
		Calendar calendar = null;
		if (c != null) {
			calendar = c;
		} else {
			calendar = Calendar.getInstance();
		}

		calendar.setTimeInMillis(calendar.getTimeInMillis() - offset);
		return calendar;
	}

	/**
	 * 得到指定或者当前时间后n天的Calendar
	 * 
	 * @param c
	 * @param n
	 * @return
	 */
	public static Calendar afterNDays(Calendar c, int n) {
		// 偏移量，给定n天的毫秒数
		long offset = n * 24 * 60 * 60 * 1000;
		Calendar calendar = null;
		if (c != null) {
			calendar = c;
		} else {
			calendar = Calendar.getInstance();
		}

		calendar.setTimeInMillis(calendar.getTimeInMillis() + offset);
		return calendar;
	}

	/**
	 * 昨天
	 * 
	 * @param c
	 * @return
	 */
	public static Calendar yesterday(Calendar c) {
		long offset = 1 * 24 * 60 * 60 * 1000;
		Calendar calendar = null;
		if (c != null) {
			calendar = c;
		} else {
			calendar = Calendar.getInstance();
		}

		calendar.setTimeInMillis(calendar.getTimeInMillis() - offset);
		return calendar;
	}

	/**
	 * 明天
	 * 
	 * @param c
	 * @return
	 */
	public static Calendar tomorrow(Calendar c) {
		long offset = 1 * 24 * 60 * 60 * 1000;
		Calendar calendar = null;
		if (c != null) {
			calendar = c;
		} else {
			calendar = Calendar.getInstance();
		}

		calendar.setTimeInMillis(calendar.getTimeInMillis() + offset);
		return calendar;
	}

	/**
	 * 得到指定或者当前时间前offset毫秒的Calendar
	 * 
	 * @param c
	 * @param offset
	 * @return
	 */
	public static Calendar before(Calendar c, long offset) {
		Calendar calendar = null;
		if (c != null) {
			calendar = c;
		} else {
			calendar = Calendar.getInstance();
		}

		calendar.setTimeInMillis(calendar.getTimeInMillis() - offset);
		return calendar;
	}

	/**
	 * 得到指定或者当前时间前offset毫秒的Calendar
	 * 
	 * @param c
	 * @param offset
	 * @return
	 */
	public static Calendar after(Calendar c, long offset) {
		Calendar calendar = null;
		if (c != null) {
			calendar = c;
		} else {
			calendar = Calendar.getInstance();
		}

		calendar.setTimeInMillis(calendar.getTimeInMillis() - offset);
		return calendar;
	}

	/**
	 * 日期格式化
	 * 
	 * @param c
	 * @param pattern
	 * @return
	 */
	public static String format(Calendar c, String pattern) {
		Calendar calendar = null;
		if (c != null) {
			calendar = c;
		} else {
			calendar = Calendar.getInstance();
		}
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyy年MM月dd日 HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		return sdf.format(calendar.getTime());
	}

	/**
	 * Date类型转换到Calendar类型
	 * 
	 * @param d
	 * @return
	 */
	public static Calendar Date2Calendar(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}

	/**
	 * Calendar类型转换到Date类型
	 * 
	 * @param c
	 * @return
	 */
	public static Date Calendar2Date(Calendar c) {
		return c.getTime();
	}

	/**
	 * Date类型转换到Timestamp类型
	 * 
	 * @param d
	 * @return
	 */
	public static Timestamp Date2Timestamp(Date d) {
		return new Timestamp(d.getTime());
	}

	/**
	 * Calendar类型转换到Timestamp类型
	 * 
	 * @param c
	 * @return
	 */
	public static Timestamp Calendar2Timestamp(Calendar c) {
		return new Timestamp(c.getTimeInMillis());
	}

	/**
	 * Timestamp类型转换到Calendar类型
	 * 
	 * @param ts
	 * @return
	 */
	public static Calendar Timestamp2Calendar(Timestamp ts) {
		Calendar c = Calendar.getInstance();
		c.setTime(ts);
		return c;
	}

	/**
	 * 得到当前时间的字符串表示 格式2010-02-02 12:12:12
	 * 
	 * @return
	 */
	public static String getTimeString() {
		return format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 标准日期格式字符串解析成Calendar对象
	 * 
	 * @param s
	 * @return
	 */
	public static Calendar pars2Calender(String s) {
		Timestamp ts = Timestamp.valueOf(s);
		return Timestamp2Calendar(ts);
	}

	/** 获取两个时间的时间查 如1天2小时30分钟 */
	public static String getDatePoor(Date endDate, Date nowDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		long sec = diff % nd % nh % nm / ns;
		if (day > 0) {
			return day + "天前";
		} else if (hour > 0) {
			return hour + "小时前";
		} else if (min > 0) {
			return min + "分钟前";
		} else {
			return sec + "秒前";
		}
	}
	public static String longTimeToStringTime(long time){
		String timeSize="0";
		if (time>0){
			long h=time/3600;
			long m=(time%3600)/60;
			long s=(time%3600)%60;
			if (h!=0) {
				if (m==0&&s==0)
					timeSize = h + "时";
				else if (s==0)
					timeSize = h + "时" + m + "分";
				else
					timeSize = h + "时" + m + "分" + s + "秒";
			}
			else if (m!=0){
				if (s==0)
					timeSize =  m + "分";
				else
					timeSize=m+"分"+s+"秒";
			}
			else {
				timeSize = s + "秒";
			}
		}
		return timeSize;
	}

	public static String longTimeToStringTime_MH(long time){
		String timeSize="0";
		if (time>0){
			long h=time/3600;
			long m=(time%3600)/60;
			long s=(time%3600)%60;
			if (h>0)
				timeSize=h+":"+m+":"+s;
			else if (m>0)
				timeSize=m+":"+s;
			else
				timeSize=s+"s";
		}
		return timeSize;
	}

	// ================以下是get和set方法=========================//

	public long getLNow() {
		return lNow;
	}

	public void setLNow(long now) {
		lNow = now;
	}

	public Calendar getCNow() {
		return cNow;
	}

	public void setCNow(Calendar now) {
		cNow = now;
	}

	public Date getDNow() {
		return dNow;
	}

	public void setDNow(Date now) {
		dNow = now;
	}

	public Timestamp getTNow() {
		return tNow;
	}

	public void setTNow(Timestamp now) {
		tNow = now;
	}

	public java.sql.Date getToday() {
		return today;
	}

	public void setToday(java.sql.Date today) {
		this.today = today;
	}

	public java.sql.Time getNow() {
		return now;
	}

	public void setNow(java.sql.Time now) {
		this.now = now;
	}

}
