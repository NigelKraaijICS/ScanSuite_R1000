package SSU_WHS.Move.MoveorderLines;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Dao
public interface iMoveorderLineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cMoveorderLineEntity moveorderLineEntity);

    @Delete
    void delete(cMoveorderLineEntity moveorderLineEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_MOVEORDERLINE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_MOVEORDERLINE)
    List<cMoveorderLineEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_MOVEORDERLINE + " WHERE " + cDatabase.BINCODE_NAMESTR + " =:pvBincode " )
    List<cMoveorderLineEntity> getMoveorderLineForBincode(String pvBincode);

      @Query("SELECT SUM (QuantityHandled) FROM " + cDatabase.TABLENAME_MOVEORDERLINE + " WHERE " + cDatabase.BINCODE_NAMESTR + " =:pvBincode " )
    Double getCountForBincodeDbl(String pvBincode);

    @Query("SELECT SUM(quantity) FROM Pickorderlines WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW )
    Double getQuantityNotHandledDbl();

    @Query("SELECT SUM(quantityhandled) FROM Pickorderlines WHERE Localstatus > " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW )
    Double getNumberHandledDbl();

    @Query("SELECT SUM (" + cDatabase.QUANTITYHANDLED_NAMESTR + ") FROM " + cDatabase.TABLENAME_MOVEORDERLINE )
    Double getTotalCountDbl();

    @Query("UPDATE " + cDatabase.TABLENAME_MOVEORDERLINE + " SET QuantityHandled = :pvQuantityHandledDbl , Quantity = :pvQuantityHandledDbl   WHERE LineNo = :pvLineNoLng")
    int updateOrderLineQuantity(Long pvLineNoLng, Double pvQuantityHandledDbl);

    @Query("UPDATE " + cDatabase.TABLENAME_MOVEORDERLINE + " SET Localstatus = :pvNewStatusInt WHERE recordid = :pvRecordIdInt")
    int updateOrderLineLocalStatus(Integer pvRecordIdInt, Integer pvNewStatusInt);

    @Query("UPDATE " + cDatabase.TABLENAME_MOVEORDERLINE + " SET HandledTimestamp = :pvHandledTimeStampStr WHERE recordid = :pvRecordIdInt")
    int updateOrderLineLocalHandledTimeStamp(Integer pvRecordIdInt, String pvHandledTimeStampStr);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_MOVEORDERLINE + " WHERE Localstatus > " + cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_BUSY + " ORDER BY HandledTimestamp DESC ")
    List<cMoveorderLineEntity> getHandledMoveorderLineEntities();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_MOVEORDERLINE + " WHERE Localstatus <= " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY)
    List<cMoveorderLineEntity> getNotHandledOrderLineEntities();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_MOVEORDERLINE + " WHERE " + cDatabase.BINCODE_NAMESTR + " = :pvBinStr")
    List<cMoveorderLineEntity> getLinesForBinFromDatabase(String pvBinStr);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_MOVEORDERLINE + " WHERE " + cDatabase.BINCODE_NAMESTR + " = :pvBinStr" + " AND " + cDatabase.ITEMNO_NAMESTR + " = :pvItemNoStr" + " AND " + cDatabase.VARIANTCODE_NAMESTR + " = :pvVariantcodeStr")
    List<cMoveorderLineEntity> getLinesForBinItemNoVariantCodeFromDatabase(String pvBinStr, String pvItemNoStr, String pvVariantcodeStr);


}
