package tbc.uncagedmist.gamewallpaper.FavDB.DataSource;


import java.util.List;

import io.reactivex.Flowable;
import tbc.uncagedmist.gamewallpaper.FavDB.Favourites;

public class FavouriteRepository implements IFavDataSource {

    private IFavDataSource mLocalDataSource;
    private static FavouriteRepository instance;

    public FavouriteRepository(IFavDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static FavouriteRepository getInstance(IFavDataSource mLocalDataSource) {
        if (instance == null)   {
            instance = new FavouriteRepository(mLocalDataSource);
        }
        return instance;
    }

    @Override
    public Flowable<List<Favourites>> getAllFavourites() {
        return mLocalDataSource.getAllFavourites();
    }

    @Override
    public void insertFav(Favourites... favourites) {
        mLocalDataSource.insertFav(favourites);
    }

    @Override
    public void updateFav(Favourites... favourites) {
        mLocalDataSource.updateFav(favourites);
    }

    @Override
    public void deleteFav(Favourites... favourites) {
        mLocalDataSource.deleteFav(favourites);
    }

    @Override
    public void deleteAllFav() {
        mLocalDataSource.deleteAllFav();
    }
}