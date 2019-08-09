package SSU_WHS.Picken.PickorderLines;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface iPickorderLineDao {
    @Delete
    void deletePickorder(cPickorderLineEntity pickorderLineEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderLineEntity pickorderLineEntity);

    @Query("DELETE FROM Pickorderlines")
    void deleteAll();

    @Query("SELECT * FROM Pickorderlines")
    List<cPickorderLineEntity> getAll();

    @Query("SELECT * FROM Pickorderlines WHERE ItemNo = :itemno")
    cPickorderLineEntity getPickorderLineByItemno(String itemno);

    @Query("SELECT SUM(Quantity) FROM Pickorderlines")
    Double getTotalArticles();

    @Query("SELECT SUM(QuantityHandled) FROM Pickorderlines")
    Double getHandledArticles();

    @Query("SELECT * FROM Pickorderlines")
    List<cPickorderLineEntity> getLocalPickorderLines();

    @Query("SELECT * FROM Pickorderlines")
    LiveData<List<cPickorderLineEntity>> getTotalPickorderLineEntities();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus > " + cPickorderLine.LOCALSTATUS_NEW)
    LiveData<List<cPickorderLineEntity>> getHandledPickorderLineEntities();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cPickorderLine.LOCALSTATUS_NEW)
    LiveData<List<cPickorderLineEntity>> getNotHandledPickorderLineEntities();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cPickorderLine.LOCALSTATUS_DONE_NOTSENT)
    LiveData<List<cPickorderLineEntity>> getPickorderLineEntitiesToSend();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cPickorderLine.LOCALSTATUS_NEW)
    List<cPickorderLineEntity> getNotHandledPickorderLineEntitiesLin();

    @Query("SELECT * FROM Pickorderlines WHERE bincode = :pv_bin AND Localstatus = " + cPickorderLine.LOCALSTATUS_NEW + " LIMIT 1 ")
    cPickorderLineEntity getPickorderLineNotHandledByBin(String pv_bin);

    @Query("UPDATE Pickorderlines SET QuantityHandled = :pv_quantityHandled WHERE recordid = :pv_recordid")
    int updateOrderLineQuantity(Integer pv_recordid, Double pv_quantityHandled);

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cPickorderLine.LOCALSTATUS_NEW + " AND BinCode = :pv_bin ORDER BY recordid ASC LIMIT 1 ")
    cPickorderLineEntity getNextPickLineFromLocation(String pv_bin);

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cPickorderLine.LOCALSTATUS_NEW + " AND SourceNo = :pv_sourceno ORDER BY recordid ASC LIMIT 1 ")
    cPickorderLineEntity getNextPickLineFromSourceNo(String pv_sourceno);

    @Query("SELECT * FROM Pickorderlines WHERE RecordId = :pv_recordid ")
    cPickorderLineEntity getPickLineByRecordid(Integer pv_recordid);

    @Query("UPDATE Pickorderlines SET Localstatus = :pv_newStatus WHERE recordid = :pv_recordid")
    int updateOrderLineLocalStatus(Integer pv_recordid, Integer pv_newStatus);

    @Query("UPDATE Pickorderlines SET ProcessingSequence = :pv_processingSequence WHERE recordid = :pv_recordid")
    int updateOrderLineProcessingSequence(Integer pv_recordid, String pv_processingSequence);

//    @Query("UPDATE Pickorderlines SET Localstatus = " + cPickorderLine.LOCALSTATUS_DONE_NOTSENT + ", Quantity = :pv_number, LocalSortLocation = :pv_location WHERE recordid = :pv_recordid")
//    void updateSortOrderLine(Integer pv_recordid, Integer pv_number, String pv_location);

    @Query("UPDATE Pickorderlines SET QuantityHandled = :pv_number, LocalSortLocation = :pv_location WHERE recordid = :pv_recordid")
    void updateSortOrderLine(Integer pv_recordid, Integer pv_number, String pv_location);

    @Query("SELECT * FROM Pickorderlines WHERE ItemNo = :pv_itemNo AND VariantCode = :pv_variantCode AND Localstatus = " + cPickorderLine.LOCALSTATUS_NEW)
    List<cPickorderLineEntity> getNotHandledPickorderLinesByItemNoandVariantCode(String pv_itemNo, String pv_variantCode);

    @Query("UPDATE Pickorderlines SET Localstatus = " + cPickorderLine.LOCALSTATUS_DONE_NOTSENT + " WHERE Localstatus = " + cPickorderLine.LOCALSTATUS_NEW + " OR Localstatus = " + cPickorderLine.LOCALSTATUS_BUSY)
    void abortOrder();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus <= " + cPickorderLine.LOCALSTATUS_BUSY + " AND ItemNo = :itemno AND VariantCode = :variant ORDER BY Localstatus DESC LIMIT 1 ")
    cPickorderLineEntity getSortorderLineNotHandledByItemNoAndVariant(String itemno, String variant);

    @Query("SELECT SUM(quantityhandled) FROM Pickorderlines WHERE Localstatus = " + cPickorderLine.LOCALSTATUS_NEW )
    int getNumberNotHandledForCounter();

    @Query("SELECT SUM(quantity) FROM Pickorderlines WHERE Localstatus = " + cPickorderLine.LOCALSTATUS_NEW )
    int getNumberTotalNotHandledForCounter();

    @Query("SELECT SUM(quantityhandled) FROM Pickorderlines WHERE Localstatus > " + cPickorderLine.LOCALSTATUS_NEW )
    int getNumberHandledForCounter();

    @Query("SELECT SUM(quantity) FROM Pickorderlines WHERE Localstatus > " + cPickorderLine.LOCALSTATUS_NEW )
    int getNumberTotalHandledForCounter();

    @Query("SELECT SUM(quantityhandled) FROM Pickorderlines")
    int getNumberTotalForCounter();

    @Query("SELECT SUM(quantity) FROM Pickorderlines")
    int getNumberTotalTotalForCounter();


}
