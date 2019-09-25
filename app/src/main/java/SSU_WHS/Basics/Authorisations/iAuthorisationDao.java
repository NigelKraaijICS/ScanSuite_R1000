package SSU_WHS.Basics.Authorisations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
