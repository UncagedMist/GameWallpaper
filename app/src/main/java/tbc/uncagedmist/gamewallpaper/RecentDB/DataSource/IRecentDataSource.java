package tbc.uncagedmist.gamewallpaper.RecentDB.DataSource;

import java.util.List;

import io.reactivex.Flowable;
import tbc.uncagedmist.gamewallpaper.RecentDB.Recent;

public interface IRecentDataSource {
    Flowable<List<Recent>> getAllRecent();
    void insertRecent(Recent...recent);
    void updateRecent(Recent...recent);
    void deleteRecent(Recent...recent);
    void deleteAllRecent();
}
