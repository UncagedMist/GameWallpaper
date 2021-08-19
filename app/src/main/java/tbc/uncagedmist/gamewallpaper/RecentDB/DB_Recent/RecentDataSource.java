package tbc.uncagedmist.gamewallpaper.RecentDB.DB_Recent;

import java.util.List;

import io.reactivex.Flowable;
import tbc.uncagedmist.gamewallpaper.RecentDB.DataSource.IRecentDataSource;
import tbc.uncagedmist.gamewallpaper.RecentDB.Recent;

public class RecentDataSource implements IRecentDataSource {

    private RecentDAO recentDAO;
    private static RecentDataSource instance;

    public RecentDataSource(RecentDAO recentDAO) {
        this.recentDAO = recentDAO;
    }

    public static RecentDataSource getInstance(RecentDAO recentDAO)   {
        if (instance == null)   {
            instance = new RecentDataSource(recentDAO);
        }
        return instance;
    }

    @Override
    public Flowable<List<Recent>> getAllRecent() {
        return recentDAO.getAllRecent();
    }

    @Override
    public void insertRecent(Recent... recent) {
        recentDAO.insertRecent(recent);
    }

    @Override
    public void updateRecent(Recent... recent) {
        recentDAO.updateRecent(recent);
    }

    @Override
    public void deleteRecent(Recent... recent) {
        recentDAO.deleteRecent(recent);
    }

    @Override
    public void deleteAllRecent() {
        recentDAO.deleteAllRecent();
    }
}