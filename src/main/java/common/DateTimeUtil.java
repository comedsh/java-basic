package common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
	
	public static String format( Date date ){
		
		return df.format(date);
		
	}
	
}
