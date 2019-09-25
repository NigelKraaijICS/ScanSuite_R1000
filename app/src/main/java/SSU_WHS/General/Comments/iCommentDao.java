package SSU_WHS.General.Comments;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iCommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cCommentEntity commentEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_COMMENT)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_COMMENT)
    LiveData<List<cCommentEntity>> getComments();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_COMMENT + " ORDER BY " + cDatabase.COMMENTLINENO_NAMESTR + " ASC")
    List<cCommentEntity> getAll();
}
