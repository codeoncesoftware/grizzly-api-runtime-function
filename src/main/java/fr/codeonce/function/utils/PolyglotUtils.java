package fr.codeonce.function.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.graalvm.polyglot.Value;


public class PolyglotUtils {

	public static Object checkReturnType(Value res) {

		if (res.isNumber()) {
			return res.asDouble();
		}

		if (res.isBoolean()) {
			return res.asBoolean();
		}
		if (res.isString()) {
			System.out.println(res);
			return res.asString();
		}
		if (res.hasArrayElements()) {
			return res.as(List.class);
		}
		return res.as(Object.class);
	}

	public static long getTimestampFromDate(String date, String format) {
		DateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
		// formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date d = null;
		try {
			d = formatter.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 1);
			return calendar.getTimeInMillis();
		} catch (Exception e) {
			e.printStackTrace();

		}
		// return the default value here
		// Can be System.currentTimeMillis();
		return 0;
	}

}
