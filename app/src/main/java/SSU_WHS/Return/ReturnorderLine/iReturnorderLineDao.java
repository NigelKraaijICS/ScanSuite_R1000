package SSU_WHS.Return.ReturnorderLine;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iReturnorderLineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cReturnorderLineEntity returnorderLineEntity);

    @Delete
    void delete(cReturnorderLineEntity returnorderLineEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_RETURNORDERLINE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_RETURNORDERLINE)
    List<cReturnorderLineEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_RETURNORDERLINE + " WHERE " + cDatabase.DOCUMENT_NAMESTR + " =:pvSourceDocumentStr " )
    List<cReturnorderLineEntity> getReturnorderLinesForSourceDocument(String pvSourceDocumentStr);

      @Query("SELECT SUM (Quantity_Take) FROM " + cDatabase.TABLENAME_RETURNORDERLINE + " WHERE " + cDatabase.DOCUMENT_NAMESTR + " =:pvSourceDocumentStr " )
    Double getCountForSourceDocumentDbl(String pvSourceDocumentStr);

    @Query("SELECT SUM (Quantity_Take) FROM " + cDatabase.TABLENAME_RETURNORDERLINE )
    Double getTotalCountDbl();


    @Query("UPDATE ReturnorderLine SET QuantityHandled_take = :pvQuantityHandledDbl WHERE SortingSequenceNo_take = :pvLineNoLng")
    int updateOrderLineQuantity(Long pvLineNoLng, Double pvQuantityHandledDbl);

}
