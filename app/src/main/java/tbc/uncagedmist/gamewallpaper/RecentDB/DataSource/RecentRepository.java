package tbc.uncagedmist.gamewallpaper.RecentDB.DataSource;

import java.util.List;

import io.reactivex.Flowable;
import tbc.uncagedmist.gamewallpaper.RecentDB.Recent;

public class RecentRepository implements IRecentDataSource {

    private IRecentDataSource mLocalDataSource;
    private static RecentRepository instance;

    public RecentRepository(IRecentDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static RecentRepository getInstance(IRecentDataSource mLocalDataSource) {
        if (instance == null)   {
            instance = new RecentRepository(mLocalDataSource);
        }
        return instance;
    }

    @Override
    public Flowable<List<Recent>> getAllRecent() {
        return mLocalDataSource.getAllRecent();
    }

    @Override
    public void insertRecent(Recent... recent) {
        mLocalDataSource.insertRecent(recent);
    }

    @Override
    public void updateRecent(Recent... recent) {
        mLocalDataSource.updateRecent(recent);
    }

    @Override
    public void deleteRecent(Recent... recent) {
        mLocalDataSource.deleteRecent(recent);
    }

    @Override
    public void deleteAllRecent() {
        mLocalDataSource.deleteAllRecent();
    }
}