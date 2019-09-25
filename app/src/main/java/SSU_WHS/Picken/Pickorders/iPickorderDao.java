package SSU_WHS.Picken.Pickorders;

import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;

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
    List<cPickorderEntity> getPickordersFromDatabase();

    @Query("SELECT * FROM Pickorders WHERE Ordernumber = :ordernumber LIMIT 1")
    cPickorderEntity getPickorderByOrderNumber(String ordernumber);

    @RawQuery(observedEntities = cPickorderEntity.class)
    List<cPickorderEntity> getFilteredPickorders(SupportSQLiteQuery query);

    @Query("UPDATE Pickorders SET Workplace =:workplace WHERE Ordernumber=:ordernumber")
    int updatePickorderWorkplace(String ordernumber, String workplace);

    @Query("UPDATE Pickorders SET Currentlocation =:CurrentLocationStr WHERE Ordernumber=:pvOrderNumberStr")
    int updatePickorderCurrentLocation(String pvOrderNumberStr, String CurrentLocationStr);

}



