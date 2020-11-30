package SSU_WHS.Basics.Translations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.General.cDatabase;

@Dao
public interface iTranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cTranslationEntity pvTranslationEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_TRANSLATION)
    void deleteAll();

    @Query("SELECT * FROM "  + cDatabase.TABLENAME_TRANSLATION)
    List<cTranslationEntity> getAll();

}
