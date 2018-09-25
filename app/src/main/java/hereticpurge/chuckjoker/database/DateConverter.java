package hereticpurge.chuckjoker.database;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import java.util.Date;

// Class for converting Date objects into long values and vice versa to be stored in the database.

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
