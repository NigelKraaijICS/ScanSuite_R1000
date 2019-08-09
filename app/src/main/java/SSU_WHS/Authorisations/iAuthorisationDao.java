package SSU_WHS.Authorisations;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface iAuthorisationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cAuthorisationEntity authorisationEntity);

    @Query("DELETE FROM Authorisations")
    void deleteAll();

    @Query("SELECT * FROM Authorisations")
    List<cAuthorisationEntity> getAll();

}
