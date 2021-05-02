package tbc.uncagedmist.gamewallpaper.Database.LocalDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import tbc.uncagedmist.gamewallpaper.Database.Recents;

@Database(entities = Recents.class,version = LocalDatabase.DATABASE_VERSION)
public abstract class LocalDatabase extends RoomDatabase {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RECENT_DB";

    public abstract RecentDAO recentDAO();

    public static LocalDatabase instance;

    public static LocalDatabase getInstance(Context context)    {

        if (instance == null)   {
            instance = Room.databaseBuilder(context,LocalDatabase.class,DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return instance;
    }
}
