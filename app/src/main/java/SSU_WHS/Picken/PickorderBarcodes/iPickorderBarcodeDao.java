package SSU_WHS.Picken.PickorderBarcodes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iPickorderBarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderBarcodeEntity pickorderBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERBARCODE)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERBARCODE)
    List<cPickorderBarcodeEntity> getLocalPickorderBarcodes();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERBARCODE)
    List<cPickorderBarcodeEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERBARCODE + " WHERE " + cDatabase.ITEMNO_NAMESTR + " = :itemno AND " + cDatabase.VARIANTCODE_NAMESTR + " = :variantcode")
    List<cPickorderBarcodeEntity> getPickorderBarcodesForItemnoVariant(String itemno, String variantcode);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERBARCODE + " WHERE " + cDatabase.BARCODE_NAMESTR + " = :barcode LIMIT 1")
    cPickorderBarcodeEntity getPickorderBarcodesByBarcode(String barcode);
}
