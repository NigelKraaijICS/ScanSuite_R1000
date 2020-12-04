package SSU_WHS.Basics.CompositeBarcode;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.Basics.PropertyGroup.cPropertyGroupEntity;
import SSU_WHS.General.cDatabase;

@Dao
public interface iCompositeBarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cCompositeBarcodeEntity pvCompositeBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_COMPOSITEBARCODE)
    void deleteAll();

}

