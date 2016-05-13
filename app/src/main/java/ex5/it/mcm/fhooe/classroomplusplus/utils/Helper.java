package ex5.it.mcm.fhooe.classroomplusplus.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Tob0t on 04.05.2016.
 */
public class Helper {
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm", Locale.GERMAN);
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMAN);
    public static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.GERMAN);
}
