package SSU_WHS.Intake.IntakeorderMATLineBarcodes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;

@Dao
public interface iIntakeorderMATLineBarcodeDao {
    @Delete
    void delete(cIntakeorderMATLineBarcodeEntity intakeorderMATLineBarcodeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cIntakeorderMATLineBarcodeEntity intakeorderMATLineBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_INTAKEORDERMATLINEBARCODE)
    void deleteAll();

    @Query("DELETE FROM " + cDatabase.TABLENAME_INTAKEORDERMATLINEBARCODE + " WHERE " + cDatabase.LINENO_NAMESTR + " == :pvLineNoInt")
    void deleteLinesForLineNo(Integer pvLineNoInt);

    @Query("UPDATE " + cDatabase.TABLENAME_INTAKEORDERMATLINEBARCODE + " SET " + cDatabase.QUANTITYHANDLED_NAMESTR + " = :pvAmountDbl WHERE " + cDatabase.BARCODE_NAMESTR + " = :pvBarcodeStr")
    void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl);
}



