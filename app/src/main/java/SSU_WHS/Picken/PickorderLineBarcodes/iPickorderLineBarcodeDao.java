package SSU_WHS.Picken.PickorderLineBarcodes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iPickorderLineBarcodeDao {
    @Delete
    void delete(cPickorderLineBarcodeEntity pickorderLineBarcodeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderLineBarcodeEntity pickorderLineBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERLINEBARCODE)
    void deleteAll();

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERLINEBARCODE + " WHERE " + cDatabase.PICKORDERLINEBARCODE_LINENO + " == :pvLineNoInt")
    void deleteLinesForLineNo(Integer pvLineNoInt);

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERLINEBARCODE)
    List<cPickorderLineBarcodeEntity> getLocalPickorderLineBarcodes();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERLINEBARCODE)
    List<cPickorderLineBarcodeEntity> getAll();

    @Query ("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEBARCODE + " WHERE " + cDatabase.PICKORDERLINEBARCODE_LINENO + " == :pvLineNoInt")
    List<cPickorderLineBarcodeEntity> getPickorderLineBarcodesForLineNo(Integer pvLineNoInt);

    @Query ("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEBARCODE + " WHERE " + cDatabase.PICKORDERLINEBARCODE_BARCODE + " == :barcode LIMIT 1")
    cPickorderLineBarcodeEntity getPickorderLineBarcodeByBarcode(String barcode);

    @Query("UPDATE " + cDatabase.TABLENAME_PICKORDERLINEBARCODE + " SET " + cDatabase.PICKORDERLINEBARCODE_QUANTITY + " = :pvAmountDbl WHERE " + cDatabase.PICKORDERLINEBARCODE_BARCODE + " = :pvBarcodeStr")
    void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl);
}
