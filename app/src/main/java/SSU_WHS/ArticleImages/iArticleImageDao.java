package SSU_WHS.ArticleImages;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.cDatabase;

@Dao
public interface iArticleImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cArticleImageEntity articleImageEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_ARTICLEIMAGE)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_ARTICLEIMAGE)
    List<cArticleImageEntity> getLocalArticleImages();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_ARTICLEIMAGE)
    List<cArticleImageEntity> getAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_ARTICLEIMAGE + " WHERE Itemno = :pv_itemno AND Variantcode = :pv_variantcode")
    cArticleImageEntity getArticleImageByItemnoAndVariantCode(String pv_itemno, String pv_variantcode);

}
