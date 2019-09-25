package ICS.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import SSU_WHS.General.cPublicDefinitions;

public class cDateAndTime {

    public static String pGetCurrentTimeStr() {
        String resultStr;
        long milliSecondsLng = new Date().getTime();
        resultStr = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, Locale.getDefault()).format(milliSecondsLng);
        return resultStr;
    }

    public static String pGetCurrentLongDateStr() {
        String resultStr;
        Calendar calendar = new GregorianCalendar();
        String dayLongStr = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String dateStr = new SimpleDateFormat(cPublicDefinitions.DATEPATTERNSHOW,Locale.getDefault()).format(calendar.getTime());
        resultStr = dayLongStr + " " + dateStr;
        return resultStr;
    }

    public static String pGetCurrentDateTimeForWebserviceStr() {
        String resultStr;
        Calendar calendar = new GregorianCalendar();
        resultStr = new SimpleDateFormat(cPublicDefinitions.DATEPATTERNWEBSERVICE,Locale.getDefault()).format(calendar.getTime());
        return resultStr;
    }

    public static String pGetCurrentTimeZoneStr() {
        String resultStr;
        resultStr = TimeZone.getDefault().getDisplayName(Locale.getDefault());
        return resultStr;
    }

}
