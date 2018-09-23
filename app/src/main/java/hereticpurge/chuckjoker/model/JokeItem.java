package hereticpurge.chuckjoker.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import hereticpurge.chuckjoker.database.DateConverter;

@Entity
@TypeConverters({DateConverter.class})
public class JokeItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "jokeBody")
    private String jokeBody;

    @ColumnInfo(name = "dateAdded")
    private Date dateAdded;
}
