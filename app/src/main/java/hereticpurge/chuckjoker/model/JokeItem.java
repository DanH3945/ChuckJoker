package hereticpurge.chuckjoker.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import hereticpurge.chuckjoker.database.converters.DateConverter;

@Deprecated
@Entity
@TypeConverters({DateConverter.class})
public class JokeItem {

    @PrimaryKey()
    private int id;

    @ColumnInfo(name = "jokeBody")
    private String jokeBody;

    @ColumnInfo(name = "dateAdded")
    private Date dateAdded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJokeBody() {
        return jokeBody;
    }

    public void setJokeBody(String jokeBody) {
        this.jokeBody = jokeBody;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
