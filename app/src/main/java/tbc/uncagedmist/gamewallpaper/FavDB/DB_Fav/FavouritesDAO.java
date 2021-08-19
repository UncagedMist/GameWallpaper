package tbc.uncagedmist.gamewallpaper.FavDB.DB_Fav;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import tbc.uncagedmist.gamewallpaper.FavDB.Favourites;

@Dao
public interface FavouritesDAO {

    @Query("SELECT * FROM favourites ORDER BY saveTime DESC LIMIT 20")
    Flowable<List<Favourites>> getAllFavourites();

    @Insert
    void insertFavourites(Favourites...favourites);

    @Update
    void updateFavourites(Favourites...favourites);

    @Delete
    void deleteFavourites(Favourites...favourites);

    @Query("DELETE FROM favourites")
    void deleteAllFavourites();
}
