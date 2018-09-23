package hereticpurge.chuckjoker.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static long getTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date getDateFromTimeStamp(Long value) {
        return value == null ? null : new Date(value);
    }
}
