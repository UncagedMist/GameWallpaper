package tbc.uncagedmist.gamewallpaper.Database.LocalDatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import tbc.uncagedmist.gamewallpaper.Database.Recents;

@Dao
public interface RecentDAO {

    @Query("SELECT * FROM recents ORDER BY saveTime DESC LIMIT 10")
    Flowable<List<Recents>> getAllRecents();

    @Insert
    void insertRecents(Recents...recents);

    @Update
    void updateRecents(Recents...recents);

    @Delete
    void deleteRecents(Recents...recents);

    @Query("DELETE FROM recents")
    void deleteAllRecents();
}
