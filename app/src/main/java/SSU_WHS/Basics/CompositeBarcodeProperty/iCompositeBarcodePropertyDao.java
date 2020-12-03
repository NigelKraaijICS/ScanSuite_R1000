package SSU_WHS.Basics.CompositeBarcodeProperty;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.Basics.CompositeBarcode.cCompositeBarcodeEntity;
import SSU_WHS.General.cDatabase;

@Dao
public interface iCompositeBarcodePropertyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cCompositeBarcodePropertyEntity pvCompositeBarcodePropertyEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_COMPOSITEBARCODEPROPERTY)
    void deleteAll();

}

