package SSU_WHS.Intake.Intakeorders;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Dao
public interface iIntakeorderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cIntakeorderEntity intakeorderEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_INTAKEORDER)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_INTAKEORDER)
    List<cIntakeorderEntity> getAll();

    @Query("SELECT * FROM " +  cDatabase.TABLENAME_INTAKEORDER)
    List<cIntakeorderEntity> getIntakeordersFromDatabase();

    @RawQuery(observedEntities = cIntakeorderEntity.class)
    List<cIntakeorderEntity> getFilteredIntakeOrders(SupportSQLiteQuery query);

    //Quantity's
    @Query("SELECT SUM(quantity) FROM IntakeLinesMAT WHERE Localstatus = " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW )
    Double getQuantityNotHandledDbl();

    @Query("SELECT SUM(quantityhandled) FROM IntakeLinesMAT WHERE Localstatus > " + cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW )
    Double getNumberHandledDbl();

    @Query("SELECT SUM(Quantity) FROM IntakeLinesMAT")
    Double getTotalQuantityDbl();


}
