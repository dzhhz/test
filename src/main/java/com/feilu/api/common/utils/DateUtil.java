package com.feilu.api.common.utils;

import java.time.LocalDateTime;
import java.util.Date;

/**
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2020</p>  
* @Company: Hangzhou FeiLu
* @author qijunhui
* @date 2020年6月1日
*/
public class DateUtil {
	public static int getLeftSeconds(String siteCode,Date endDate) {
		Date nowDate = new Date();
		if(nowDate.after(endDate)) {
			return 0;
		}
		long now = nowDate.getTime();
		long end = endDate.getTime();
        return (int)((end - now) / 1000);
	}

	
	public static int getLeftSeconds(Date startDate,long flashTime) {
		Date nowDate = new Date();
		if(nowDate.before(startDate)) {
			return 0;
		}
		long now = nowDate.getTime();
		long end = startDate.getTime() + flashTime;
        return (int)((end - now) / 1000);
	}

	public static int getLeftSeconds(LocalDateTime startDate, long flashTime) {
		LocalDateTime nowDate = LocalDateTime.now();
		if(nowDate.isBefore(startDate)) {
			return 0;
		}
		long now = nowDate.getSecond();
		long end = startDate.getSecond() + flashTime / 1000;
		return (int)((end - now));
	}
}
