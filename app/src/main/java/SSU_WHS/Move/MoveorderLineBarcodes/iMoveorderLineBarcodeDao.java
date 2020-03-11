package SSU_WHS.Move.MoveorderLineBarcodes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iMoveorderLineBarcodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cMoveorderLineBarcodeEntity moveorderLineBarcodeEntity);

    @Delete
    void delete(cMoveorderLineBarcodeEntity moveorderLineBarcodeEntity);


    @Query("DELETE FROM " + cDatabase.TABLENAME_MOVEORDERLINEBARCODE)
    void deleteAll();

    @Query("DELETE FROM " + cDatabase.TABLENAME_MOVEORDERLINEBARCODE + " WHERE " + cDatabase.LINENO_NAMESTR + " == :pvLineNoInt")
    void deleteLinesForLineNo(Integer pvLineNoInt);

    @Query("UPDATE " + cDatabase.TABLENAME_MOVEORDERLINEBARCODE + " SET " + cDatabase.QUANTITYHANDLED_NAMESTR + " = :pvAmountDbl WHERE " + cDatabase.BARCODE_NAMESTR + " = :pvBarcodeStr")
    void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl);

    @Query ("SELECT * FROM " + cDatabase.TABLENAME_MOVEORDERLINEBARCODE + " WHERE " + cDatabase.LINENO_NAMESTR + " == :pvLineNoInt")
    List<cMoveorderLineBarcodeEntity> getMoveorderLineBarcodesForLineNo(Integer pvLineNoInt);

}



