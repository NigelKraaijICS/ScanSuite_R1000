package SSU_WHS.Basics.LabelTemplate;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;
@Dao
public interface iLabelTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cLabelTemplateEntity labelTemplateEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_LABELTEMPLATE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_LABELTEMPLATE)
    List<cLabelTemplateEntity> getAll();
}
