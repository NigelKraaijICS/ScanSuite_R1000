package SSU_WHS.Comments;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.cDatabase;

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
