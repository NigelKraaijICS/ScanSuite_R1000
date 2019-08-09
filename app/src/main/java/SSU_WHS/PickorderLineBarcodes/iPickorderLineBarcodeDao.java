package SSU_WHS.PickorderLineBarcodes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.cDatabase;

@Dao
public interface iPickorderLineBarcodeDao {
    @Delete
    void delete(cPickorderLineBarcodeEntity pickorderLineBarcodeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderLineBarcodeEntity pickorderLineBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERLINEBARCODE)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERLINEBARCODE)
    List<cPickorderLineBarcodeEntity> getLocalPickorderLineBarcodes();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERLINEBARCODE)
    List<cPickorderLineBarcodeEntity> getAll();

    @Query ("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEBARCODE + " WHERE LineNo == :itemno")
    List<cPickorderLineBarcodeEntity> getPickorderLineBarcodesForLineNo(Integer itemno);

    @Query ("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEBARCODE + " WHERE Barcode == :barcode LIMIT 1")
    cPickorderLineBarcodeEntity getPickorderLineBarcodeByBarcode(String barcode);

    @Query("UPDATE " + cDatabase.TABLENAME_PICKORDERLINEBARCODE + " SET " + cDatabase.PICKORDERLINEBARCODE_QUANTITY + " = :amount WHERE Barcode= :barcode")
    void updateBarcodeAmount(String barcode, int amount);
}
