package ICS.Utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import SSU_WHS.cPublicDefinitions;

public class cDateAndTime {
    public String mGetLogicalDateStr(Context context, String dateTimeStr) {
        String l_resultStr;
        l_resultStr = "";
        return l_resultStr;
    }
    public static String m_GetCurrentTime() {
        String l_resultStr;
        long millis = new Date().getTime();
        String timeStr = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, Locale.getDefault()).format(millis);
        l_resultStr = timeStr;
        return l_resultStr;
    }
    public static String m_GetCurrentLongDate() {
        String l_resultStr;
        Calendar calendar = new GregorianCalendar();
        String dayLongStr = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String dateStr = new SimpleDateFormat(cPublicDefinitions.DATEPATTERNSHOW,Locale.getDefault()).format(calendar.getTime());
        String showDateStr = dayLongStr + " " + dateStr;
        l_resultStr = showDateStr;
        return l_resultStr;
    }
    public static String m_GetCurrentDateTimeForWebservice() {
        String l_resultStr;
        Calendar calendar = new GregorianCalendar();
        String dateStr = new SimpleDateFormat(cPublicDefinitions.DATEPATTERNWEBSERVICE,Locale.getDefault()).format(calendar.getTime());
        l_resultStr = dateStr;
        return l_resultStr;
    }

    public static String m_getCurrentTimeZone() {
        String l_resultStr;
        l_resultStr = TimeZone.getDefault().getDisplayName(Locale.getDefault());

        return l_resultStr;
    }





}
