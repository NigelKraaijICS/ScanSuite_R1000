package SSU_WHS.Return.ReturnorderLineBarcode;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;

@Dao
public interface iReturnorderLineBarcodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cReturnorderLineBarcodeEntity returnorderLineBarcodeEntity);

    @Delete
    void delete(cReturnorderLineBarcodeEntity returnorderLineBarcodeEntity);


    @Query("DELETE FROM " + cDatabase.TABLENAME_RETURNORDERLINEBARCODE)
    void deleteAll();

    @Query("DELETE FROM " + cDatabase.TABLENAME_RETURNORDERLINEBARCODE + " WHERE " + cDatabase.LINENO_NAMESTR + " == :pvLineNoInt")
    void deleteLinesForLineNo(Integer pvLineNoInt);

    @Query("UPDATE " + cDatabase.TABLENAME_RETURNORDERLINEBARCODE + " SET " + cDatabase.QUANTITYHANDLED_NAMESTR + " = :pvAmountDbl WHERE " + cDatabase.BARCODE_NAMESTR + " = :pvBarcodeStr")
    void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl);
}



