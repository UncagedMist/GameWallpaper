package tbc.uncagedmist.gamewallpaper.RecentDB.DB_Recent;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import tbc.uncagedmist.gamewallpaper.RecentDB.Recent;

@Dao
public interface RecentDAO {

    @Query("SELECT * FROM recent ORDER BY saveTime DESC LIMIT 20")
    Flowable<List<Recent>> getAllRecent();

    @Insert
    void insertRecent(Recent...recent);

    @Update
    void updateRecent(Recent...recent);

    @Delete
    void deleteRecent(Recent...recent);

    @Query("DELETE FROM recent")
    void deleteAllRecent();
}
