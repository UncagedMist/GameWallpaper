package tbc.uncagedmist.gamewallpaper.FavDB.LocalDB;


import java.util.List;

import io.reactivex.Flowable;
import tbc.uncagedmist.gamewallpaper.FavDB.DataSource.IFavDataSource;
import tbc.uncagedmist.gamewallpaper.FavDB.Favourites;

public class FavouritesDataSource implements IFavDataSource {

    private FavouritesDAO favouritesDAO;
    private static FavouritesDataSource instance;

    public FavouritesDataSource(FavouritesDAO favouritesDAO) {
        this.favouritesDAO = favouritesDAO;
    }

    public static FavouritesDataSource getInstance(FavouritesDAO favouritesDAO)   {
        if (instance == null)   {
            instance = new FavouritesDataSource(favouritesDAO);
        }
        return instance;
    }

    @Override
    public Flowable<List<Favourites>> getAllFavourites() {
        return favouritesDAO.getAllFavourites();
    }

    @Override
    public void insertFav(Favourites... favourites) {
        favouritesDAO.insertFavourites(favourites);
    }

    @Override
    public void updateFav(Favourites... favourites) {
        favouritesDAO.updateFavourites(favourites);
    }

    @Override
    public void deleteFav(Favourites... favourites) {
        favouritesDAO.deleteFavourites(favourites);
    }

    @Override
    public void deleteAllFav() {
        favouritesDAO.deleteAllFavourites();
    }
}