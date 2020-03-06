package ai.turbochain.ipex.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.util.Assert;

public class DateUtil {
	public static final DateFormat YYYY_MM_DD_MM_HH_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat HHMMSS = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

	public static final DateFormat YYYYMMDDMMHHSSSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static final DateFormat YYYY_MM_DD_MM_HH_SS_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static final DateFormat YYYYMMDDMMHHSSSS = new SimpleDateFormat("yyyyMMddHHmmssSS");

	public static final DateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

	public static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	public static String dateToString(Date date) {
		return YYYY_MM_DD_MM_HH_SS.format(date);
	}

	public static String dateToStringDate(Date date) {
		return YYYY_MM_DD.format(date);
	}

	public static String dateToStringLengthIs17(Date date) {
		return YYYYMMDDMMHHSSSSS.format(date);
	}

	public static String dateTo3S(Date date) {
		return YYYY_MM_DD_MM_HH_SS_SSS.format(date);
	}

	/**
	 * 开始时间 结束时间 是否合法 // 判断是否开始时间小于今天并且开始时间小于结束时间
	 *
	 * @param startDate
	 * @param endDate
	 */
	public static void validateDate(Date startDate, Date endDate) {
		Date currentDate = DateUtil.getCurrentDate();
		int compare = compare(startDate, currentDate);
		int compare2 = compare(startDate, endDate);
		Assert.isTrue(compare != -1, "startDate cannot be less than currentDate!");
		Assert.isTrue(compare2 != 1, "startDate must be less than endDate!");
	}

	public static void validateEndDate(Date endDate) {
		Date currentDate = DateUtil.getCurrentDate();
		int compare = compare(currentDate, endDate);
		Assert.isTrue(compare != 1, "currentDate must be less than endDate!");
	}

	/**
	 * @param date1
	 * @param date2
	 * @return 1 大于 -1 小于 0 相等
	 */
	public static int compare(Date date1, Date date2) {
		try {
			if (date1.getTime() > date2.getTime()) {
				return 1;
			} else if (date1.getTime() < date2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当时日期时间串 格式 yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String getDateTime() {
		return YYYY_MM_DD_MM_HH_SS.format(new Date());
	}

	public static Date getStringToDate3S(String dateString) {
		try {
			return YYYYMMDDMMHHSSSSS.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getStringToDate2S(String dateString) {
		try {
			return YYYYMMDDMMHHSSSS.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当时日期串 格式 yyyy-MM-dd
	 *
	 * @return
	 */
	public static String getDate() {
		return YYYY_MM_DD.format(new Date());
	}

	public static String getDateYMD() {
		return YYYYMMDD.format(new Date());
	}

	public static String getDateYMD(Date date) {
		return YYYYMMDD.format(date);
	}

	public static Date strToDate(String dateString) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date strToYYMMDDDate(String dateString) {
		Date date = null;
		try {
			date = YYYY_MM_DD.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static long diffDays(Date startDate, Date endDate) {
		long days = 0L;
		long start = startDate.getTime();
		long end = endDate.getTime();
		days = (end - start) / 86400000L;
		return days;
	}
	
	
	/**
	 * 两个日期相隔天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long daysApart(Date startDate, Date endDate) {
		long days = 0L;
		long start = startDate.getTime();
		long end = endDate.getTime();
		days = (end - start) / 86400000L;
		if((end-start)%86400000L!=0) {
			days = days +1;
		}
		return days;
	}

	public static Date dateAddMonth(Date date, int month) {
		return add(date, 2, month);
	}

	public static Date dateAddDay(Date date, int day) {
		return add(date, 6, day);
	}

	public static Date dateAddYear(Date date, int year) {
		return add(date, 1, year);
	}

	public static String dateAddDay(String dateString, int day) {
		Date date = strToYYMMDDDate(dateString);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(6, day);
		return YYYY_MM_DD.format(calendar.getTime());
	}

	public static String dateAddDay(int day) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(6, day);
		return YYYY_MM_DD.format(calendar.getTime());
	}

	public static String dateAddMonth(int month) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(2, month);
		return YYYY_MM_DD.format(calendar.getTime());
	}

	public static String remainDateToString(Date startDate, Date endDate) {
		StringBuilder result = new StringBuilder();
		if (endDate == null) {
			return "过期";
		}
		long times = endDate.getTime() - startDate.getTime();
		if (times < -1L) {
			result.append("过期");
		} else {
			long temp = 86400000L;

			long d = times / temp;

			times %= temp;
			temp /= 24L;
			long m = times / temp;

			times %= temp;
			temp /= 60L;
			long s = times / temp;

			result.append(d);
			result.append("天");
			result.append(m);
			result.append("小时");
			result.append(s);
			result.append("分");
		}
		return result.toString();
	}

	private static Date add(Date date, int type, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, value);
		return calendar.getTime();
	}

	public static String getLinkUrl(boolean flag, String content, String id) {
		if (flag) {
			content = "<a href='finance.do?id=" + id + "'>" + content + "</a>";
		}
		return content;
	}

	public static long getTimeCur(String format, String date) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.parse(sf.format(date)).getTime();
	}

	public static long getTimeCur(String format, Date date) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.parse(sf.format(date)).getTime();
	}

	public static String getStrTime(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		long lcc_time = Long.valueOf(cc_time).longValue();
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	public static Date getCurrentDate() {
		return new Date();
	}

	public static String getFormatTime(DateFormat format, Date date) throws ParseException {
		return format.format(date);
	}

	/**
	 * 获取时间戳
	 *
	 * @return
	 */
	public static long getTimeMillis() {
		return System.currentTimeMillis();
	}

	public static String getWeekDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
		case 1:
			return "周日";
		case 2:
			return "周一";
		case 3:
			return "周二";
		case 4:
			return "周三";
		case 5:
			return "周四";
		case 6:
			return "周五";
		case 7:
			return "周六";
		default:
			return "";
		}
	}

	public static String toGMTString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
		df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
		return df.format(date);
	}

	/**
	 * 得到当前时间与某个时间的差的分钟数
	 *
	 * @param date
	 * @return
	 */
	public static BigDecimal diffMinute(Date date) {
		return BigDecimalUtils.div(new BigDecimal(System.currentTimeMillis() - date.getTime()),
				new BigDecimal("60000"));
	}

	/**
	 * 获取过去第几天的日期
	 *
	 * @param past
	 * @return
	 */
	public static String getPastDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
	}

	/**
	 * 获取未来 第 past 天的日期
	 *
	 * @param past
	 * @return
	 */
	public static String getFetureDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
	}

	public static int getDatePart(Date date, int part) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(part);
	}

	public static Date getDate(Date date, int day) {

		synchronized (YYYY_MM_DD) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, -day);
			date = calendar.getTime();
			try {
				return YYYY_MM_DD.parse(YYYY_MM_DD.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static String getDateRandom() {
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + (int) ((Math.random() * 9 + 1) * 10000);
	}

	public static Date getDateNoTime(Date curDate, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		cal.add(Calendar.DAY_OF_MONTH, amount);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm:ss" 如果获取失败，返回null
	 * 
	 * @return
	 */
	public static String getUTCTimeStr() {
		StringBuffer UTCTimeBuffer = new StringBuffer();
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		// 2、取得时间偏移量：
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

		Date date = cal.getTime();

		return YYYY_MM_DD_MM_HH_SS.format(date);
	}

	public static Map<String, Long> getMonthRange() {
		Calendar calendar = Calendar.getInstance();

		// 将秒、微秒字段置为0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		long time = calendar.getTimeInMillis();
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		long endTick = calendar.getTimeInMillis();
		String endTime = DateUtil.dateToString(calendar.getTime());

		// 获取上个月的开始时间
		calendar.add(Calendar.MONTH, -1);

		String fromTime = DateUtil.dateToString(calendar.getTime());

		long startTick = calendar.getTimeInMillis();

		System.out.println("统计时间区间为：" + fromTime + "至" + endTime);

		Map<String, Long> result = new HashMap<String, Long>();

		result.put("startTime", startTick);
		result.put("endTime", endTick);

		return result;
	}

	// 1、获取当月第一天
	public static long getFirstDateOfMonth() {
		Calendar calendar = Calendar.getInstance();

		// 将秒、微秒字段置为0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天

		String first = DateUtil.dateToString(calendar.getTime());

		System.out.println("===============本月first day:" + first);

		return calendar.getTimeInMillis();
	}

	// 5、获取上个月的第一天
	public static long getBeforeFirstMonthDate() {
		Calendar calendar = Calendar.getInstance();
		// 将秒、微秒字段置为0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println("上个月第一天：" + DateUtil.dateToString(calendar.getTime()));
		return calendar.getTimeInMillis();
	}

	// 5、获取上个月的最后一天
	public static long getBeforeLastMonthdate() throws Exception {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		System.out.println("上个月最后一天：" + DateUtil.dateToString(calendar.getTime()));
		return calendar.getTimeInMillis();
	}

	// 5、获取上个周的第一天
	public static long getBeforeFirstWeekDate() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		calendar.add(Calendar.DATE, 2 - dayofweek - 7);

		System.out.println("上周第一天：" + DateUtil.dateToString(calendar.getTime()));

		return calendar.getTimeInMillis();
	}

	// 获取本周的开始时间
	public static long getBeginDayOfWeek() {

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);

		if (dayofweek == 1) {
			dayofweek += 7;
		}
		calendar.add(Calendar.DATE, 2 - dayofweek);

		System.out.println("本周第一天：" + DateUtil.dateToString(calendar.getTime()));

		return calendar.getTimeInMillis();
	}

	public static long getYestDayBeginTime() {
		Calendar calendar = Calendar.getInstance();

		// 将秒、微秒字段置为0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.DATE, -1);

		System.out.println("昨天开始时间为：" + DateUtil.dateToString(calendar.getTime()));

		return calendar.getTimeInMillis();
	}

	public static long getTodayBeginTime() {
		Calendar calendar = Calendar.getInstance();

		// 将秒、微秒字段置为0
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		System.out.println("今天开始时间为：" + DateUtil.dateToString(calendar.getTime()));

		return calendar.getTimeInMillis();
	}

	/**
	 * LocalDateTime转换为Date
	 * 
	 * @param localDateTime
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);// Combines this date-time with a time-zone to create a //
															// ZonedDateTime.
		Date date = Date.from(zdt.toInstant());
		return date;
		// System.out.println(date.toString());// Tue Mar 27 14:17:17 CST 2018
	}
	
	/**
	 * 两个日期相隔天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long daysApartOfLocalDateTime(LocalDateTime startDate, LocalDateTime endDate) {
		long days = 0L;
		Duration duration  = Duration.between(startDate,  endDate);
		days = duration.toMillis() / 86400000L;
		if(duration.toMillis()%86400000L!=0) {
			days = days +1;
		}
		return days;
	}

	public static void main(String[] args) {
		long startTick = 0;
		long endTick = 0;
		try {
			// startTick = getYestDayBeginTime();
			// endTick = getTodayBeginTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long from = 1413078466000l;
		long to = 1568598466563l;

		// Calendar calendar = Calendar.getInstance();
		long s = getFirstDateOfMonth();
		System.out.println(s);
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.getTimeInMillis());
		// System.out.println(DateUtil.dateToString(calendar.getTime()));
		// System.out.println(DateUtil.dateToString(new Date(from)) + "========" +
		// DateUtil.dateToString(new Date(to)));
		// System.out.println(startTick + "========" + endTick);
	}
}