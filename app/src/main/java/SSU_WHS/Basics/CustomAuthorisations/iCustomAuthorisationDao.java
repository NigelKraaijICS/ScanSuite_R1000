package SSU_WHS.Basics.CustomAuthorisations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.Basics.Authorisations.cAuthorisationEntity;
import SSU_WHS.General.cDatabase;

@Dao
public interface iCustomAuthorisationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cCustomAuthorisationEntity customAuthorisationEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_CUSTOMAUTHORISATIONS)
    void deleteAll();

    @Query("SELECT * FROM  " + cDatabase.TABLENAME_CUSTOMAUTHORISATIONS)
    List<cCustomAuthorisationEntity> getAll();

    @Query("SELECT * FROM  " + cDatabase.TABLENAME_CUSTOMAUTHORISATIONS + " WHERE " + cDatabase.AUTHORISATION_NAMESTR + " =:autorisation LIMIT 1")
    cCustomAuthorisationEntity getByAutorisation(String autorisation);

}
