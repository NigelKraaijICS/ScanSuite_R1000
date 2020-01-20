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

    @Query("SELECT * FROM Pickorders ORDER BY Priority, Ordernumber ASC")
    List<cPickorderEntity> getPickordersFromDatabase();

    @RawQuery(observedEntities = cPickorderEntity.class)
    List<cPickorderEntity> getFilteredPickorders(SupportSQLiteQuery query);

    @Query("UPDATE Pickorders SET Currentlocation =:CurrentLocationStr WHERE Ordernumber=:pvOrderNumberStr")
    int updatePickorderCurrentLocation(String pvOrderNumberStr, String CurrentLocationStr);

}



