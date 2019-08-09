package SSU_WHS.Pickorders;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import java.util.List;

@Dao
public interface iPickorderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderEntity pickorderEntity);

    @Query("DELETE FROM Pickorders")
    void deleteAll();

    @Query("SELECT * FROM Pickorders")
    List<cPickorderEntity> getAll();

    @Query("SELECT * FROM Pickorders")
    List<cPickorderEntity> getLocalPickorders();

    @Query("SELECT * FROM Pickorders WHERE Ordernumber = :ordernumber LIMIT 1")
    cPickorderEntity getPickorderByOrderNumber(String ordernumber);

//    @Query("SELECT * FROM Pickorders")
//    LiveData<List<cPickorderEntity>> getFilteredPickorders(Boolean pv_useFilters, Boolean pv_showProcessedWait, Boolean pv_showSingleArticles, Boolean pv_showAssignedToMe, Boolean pv_showAssignedToOthers, Boolean pv_showNotAssigned);
    @RawQuery(observedEntities = cPickorderEntity.class)
    LiveData<List<cPickorderEntity>> getFilteredPickorders(SupportSQLiteQuery query);

    @Query("UPDATE Pickorders SET Workplace =:workplace WHERE Ordernumber=:ordernumber")
    int updatePickorderWorkplace(String ordernumber, String workplace);

}



