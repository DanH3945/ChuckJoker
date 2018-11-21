package hereticpurge.chuckjoker.database.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

// Class for converting Date objects into long values and vice versa to be stored in the database.

@Deprecated
public class DateConverter {

    @TypeConverter
    public static long getTimeStamp(Date date) {
        return date == null ? 0 : date.getTime();
    }

    @TypeConverter
    public static Date getDateFromTimeStamp(Long value) {
        return value == null ? null : new Date(value);
    }
}
