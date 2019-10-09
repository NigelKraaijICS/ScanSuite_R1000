package SSU_WHS.Picken.PickorderLines;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;

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

    @Query("SELECT SUM(Quantity) FROM Pickorderlines")
    Double getTotalQuantityDbl();

    @Query("SELECT SUM(QuantityHandled) FROM Pickorderlines")
    Double getTotalQuanitityHandledDbl();

    @Query("SELECT SUM(quantity) FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW )
    Double getNumberNotHandledForCounterDbl();

    @Query("SELECT SUM(quantity) FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW )
    Double getNumberTotalNotHandledForCounterDbl();

    @Query("SELECT SUM(quantityhandled) FROM Pickorderlines WHERE Localstatus > " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW )
    Double getNumberHandledForCounterDbl();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus <= " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY)
    List<cPickorderLineEntity> getNotHandledPickorderLineEntitiesLin();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY)
    List<cPickorderLineEntity> getBusyPickorderLineEntitiesLin();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus > " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY + " ORDER BY TakenTimestamp DESC ")
    List<cPickorderLineEntity> getHandledPickorderLineEntities();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT)
    List<cPickorderLineEntity> getPickorderLineEntitiesToSend();

    @Query("SELECT * FROM Pickorderlines WHERE bincode = :pvBinCodeStr AND Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW + " LIMIT 1 ")
    cPickorderLineEntity getPickorderLineNotHandledByBin(String pvBinCodeStr);

    @Query("UPDATE Pickorderlines SET QuantityHandled = :pvQuantityHandled WHERE recordid = :pvRecordInt")
    int updateOrderLineQuantity(Integer pvRecordInt, Double pvQuantityHandled);

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW + " AND SourceNo = :pvSourceNoStr ORDER BY recordid")
    List<cPickorderLineEntity>  getPickLinesNotHandledForSourceNo(String pvSourceNoStr);

    @Query("UPDATE Pickorderlines SET Localstatus = :pvNewStatusInt WHERE recordid = :pvRecordIdInt")
    int updateOrderLineLocalStatus(Integer pvRecordIdInt, Integer pvNewStatusInt);

    @Query("UPDATE Pickorderlines SET TakenTimestamp = :pvHandledTimeStampStr WHERE recordid = :pvRecordIdInt")
    int updateOrderLineLocalHandledTimeStamp(Integer pvRecordIdInt, String pvHandledTimeStampStr);

    @Query("UPDATE Pickorderlines SET ProcessingSequence = :pvProcessingSequenceStr WHERE recordid = :pvRecordidInt")
    int updateOrderLineProcessingSequence(Integer pvRecordidInt, String pvProcessingSequenceStr);

    @Query("UPDATE Pickorderlines SET QuantityHandled = :pvQuantityHandledDbl, LocalSortLocation = :pvLocationStr WHERE recordid = :pvRecordIDInt")
    int updateSortOrderLine(Integer pvRecordIDInt, Integer pvQuantityHandledDbl, String pvLocationStr);

    @Query("SELECT * FROM Pickorderlines WHERE ItemNo = :pvItemNoStr AND VariantCode = :pvVariantCodeStr AND Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW)
    List<cPickorderLineEntity> getNotHandledPickorderLinesByItemNoandVariantCode(String pvItemNoStr, String pvVariantCodeStr);

    @Query("UPDATE Pickorderlines SET Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT + " WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW + " OR Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY)
    void abortOrder();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus <= " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY + " AND ItemNo = :itemno AND VariantCode = :variant ORDER BY Localstatus DESC")
    List<cPickorderLineEntity> getSortorderLinesNotHandledByItemNoAndVariant(String itemno, String variant);

}
