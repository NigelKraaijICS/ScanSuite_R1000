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

    //Standard SQL functions
    @Delete
    void deletePickorder(cPickorderLineEntity pickorderLineEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderLineEntity pickorderLineEntity);

    @Query("DELETE FROM Pickorderlines")
    void deleteAll();

    @Query("SELECT * FROM Pickorderlines")
    List<cPickorderLineEntity> getAll();

   //Quantity's
    @Query("SELECT SUM(quantity) FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW )
    Double getQuantityNotHandledDbl();

    @Query("SELECT SUM(quantityhandled) FROM Pickorderlines WHERE Localstatus > " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW )
    Double getNumberHandledDbl();

    @Query("SELECT SUM(Quantity) FROM Pickorderlines")
    Double getTotalQuantityDbl();

    //Get picklines
    @Query("SELECT * FROM Pickorderlines WHERE Localstatus <= " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY)
    List<cPickorderLineEntity> getNotHandledPickorderLineEntitiesLin();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus <= " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY  + " AND DestinationNo = :pvDestinationNoStr ORDER BY recordid")
    List<cPickorderLineEntity> getNotHandledPickorderLineForBranchEntitiesLin(String pvDestinationNoStr);

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY)
    List<cPickorderLineEntity> getBusyPickorderLineEntitiesLin();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus > " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY + " ORDER BY TakenTimestamp DESC ")
    List<cPickorderLineEntity> getHandledPickorderLineEntities();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT + " OR  Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_ERROR_SENDING)
    List<cPickorderLineEntity> getPickorderLineEntitiesToSend();

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW + " AND SourceNo = :pvSourceNoStr ORDER BY recordid")
    List<cPickorderLineEntity>  getPickLinesNotHandledForSourceNo(String pvSourceNoStr);

    @Query("SELECT * FROM Pickorderlines WHERE Localstatus <= " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY + " AND ItemNo = :itemno AND VariantCode = :variant ORDER BY Localstatus DESC")
    List<cPickorderLineEntity> getSortorderLinesNotHandledByItemNoAndVariant(String itemno, String variant);

    //Update line(s)
    @Query("UPDATE Pickorderlines SET QuantityHandled = :pvQuantityHandled WHERE recordid = :pvRecordInt")
    int updateOrderLineQuantity(Integer pvRecordInt, Double pvQuantityHandled);

    @Query("UPDATE Pickorderlines SET Localstatus = :pvNewStatusInt WHERE recordid = :pvRecordIdInt")
    int updateOrderLineLocalStatus(Integer pvRecordIdInt, Integer pvNewStatusInt);

    @Query("UPDATE Pickorderlines SET TakenTimestamp = :pvHandledTimeStampStr WHERE recordid = :pvRecordIdInt")
    int updateOrderLineLocalHandledTimeStamp(Integer pvRecordIdInt, String pvHandledTimeStampStr);

    @Query("UPDATE Pickorderlines SET ProcessingSequence = :pvProcessingSequenceStr WHERE recordid = :pvRecordidInt")
    int updateOrderLineProcessingSequence(Integer pvRecordidInt, String pvProcessingSequenceStr);

    @Query("UPDATE Pickorderlines SET QuantityHandled = :pvQuantityHandledDbl, LocalSortLocation = :pvLocationStr WHERE recordid = :pvRecordIDInt")
    int updateSortOrderLine(Integer pvRecordIDInt, Integer pvQuantityHandledDbl, String pvLocationStr);

    @Query("UPDATE Pickorderlines SET Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT + " WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW + " OR Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY)
    void abortOrder();



}
