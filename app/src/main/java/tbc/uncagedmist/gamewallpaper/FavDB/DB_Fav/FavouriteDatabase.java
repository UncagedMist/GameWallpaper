package tbc.uncagedmist.gamewallpaper.FavDB.DB_Fav;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import tbc.uncagedmist.gamewallpaper.FavDB.Favourites;

@Database(entities = Favourites.class,version = FavouriteDatabase.DATABASE_VERSION)
public abstract class FavouriteDatabase extends RoomDatabase {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Fav_DB";

    public abstract FavouritesDAO favouritesDAO();

    public static FavouriteDatabase instance;

    public static FavouriteDatabase getInstance(Context context)    {

        if (instance == null)   {
            instance = Room.databaseBuilder(
                    context,
                    FavouriteDatabase.class,
                    DATABASE_NAME).fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
