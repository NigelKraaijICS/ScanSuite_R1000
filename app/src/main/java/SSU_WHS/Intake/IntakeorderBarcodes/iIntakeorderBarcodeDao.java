package SSU_WHS.Intake.IntakeorderBarcodes;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;

@Dao
public interface iIntakeorderBarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cIntakeorderBarcodeEntity intakeorderBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_INTAKEORDERBARCODE)
    void deleteAll();
}
