package SSU_WHS.Intake.IntakeorderMATLines;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Dao
public interface iIntakeorderMATLineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cIntakeorderMATLineEntity intakeorderMATLineEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_INTAKEORDERMATLINES)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INTAKEORDERMATLINES)
    List<cIntakeorderMATLineEntity> getAll();

    @Query("SELECT * FROM " +  cDatabase.TABLENAME_INTAKEORDERMATLINES)
    List<cIntakeorderMATLineEntity> getIntakeorderMATLinesFromDatabase();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INTAKEORDERMATLINES + " WHERE " + cDatabase.LOCALSTATUS_NAMESTR + " = " + cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT)
    List<cIntakeorderMATLineEntity> getIntakeorderMATLineEntitiesToSend();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INTAKEORDERMATLINES + " WHERE " + cDatabase.LOCALSTATUS_NAMESTR + " <= " + cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_BUSY)
    List<cIntakeorderMATLineEntity> getNotHandledIntakeorderMATLineEntities();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INTAKEORDERMATLINES + " WHERE " + cDatabase.LOCALSTATUS_NAMESTR + " = " + cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_BUSY)
    List<cIntakeorderMATLineEntity> getBusyIntakeorderMATLineEntities();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INTAKEORDERMATLINES + " WHERE " + cDatabase.LOCALSTATUS_NAMESTR + " > " + cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_BUSY)
    List<cIntakeorderMATLineEntity> getHandledIntakeorderMATLineEntities();

    @Query("UPDATE IntakeLinesMAT SET Localstatus = :pvNewStatusInt WHERE LineNo = :pvLineNoInt")
    int updateOrderLineLocalStatus(Integer pvLineNoInt, Integer pvNewStatusInt);

    @Query("UPDATE IntakeLinesMAT SET QuantityHandled = :pvQuantityHandledDbl WHERE LineNo = :pvLineNoInt")
    int updateOrderLineQuantityHandled(Integer pvLineNoInt, Double pvQuantityHandledDbl);


    @Query("UPDATE IntakeLinesMAT SET Quantity = :pvQuantityDbl WHERE LineNo = :pvLineNoInt")
    int updateOrderLineQuantity(Integer pvLineNoInt, Double pvQuantityDbl);


    @Query("UPDATE IntakeLinesMAT SET BinCodeHandled = :pvBinCodeHandledStr WHERE LineNo = :pvLineNoInt")
    int updateOrderLineBinCodeHandled(Integer pvLineNoInt, String pvBinCodeHandledStr);



}
