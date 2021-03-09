package SSU_WHS.Picken.PickorderCompositeBarcode;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iPickorderCompositeBarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderCompositeBarcodeEntity pvPickorderCompositeBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERCOMPOSITEBARCODE)
    void deleteAll();

    @Query("SELECT * FROM "+ cDatabase.TABLENAME_PICKORDERCOMPOSITEBARCODE)
    List<cPickorderCompositeBarcodeEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERCOMPOSITEBARCODE + " WHERE " + cDatabase.ITEMNO_NAMESTR + " = :itemno AND " + cDatabase.VARIANTCODE_NAMESTR + " = :variantcode")
    List<cPickorderCompositeBarcodeEntity> getPickorderBarcodesForItemnoVariant(String itemno, String variantcode);
}
