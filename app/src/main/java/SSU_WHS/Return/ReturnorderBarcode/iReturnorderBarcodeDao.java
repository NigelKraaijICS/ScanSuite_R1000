package SSU_WHS.Return.ReturnorderBarcode;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import SSU_WHS.General.cDatabase;

@Dao
public interface iReturnorderBarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cReturnorderBarcodeEntity returnorderBarcodeEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_RETURNORDERBARCODE)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_RETURNORDERBARCODE + " WHERE " + cDatabase.ITEMNO_NAMESTR + " =:pvItemNoStr "
            + " AND " + cDatabase.VARIANTCODE_NAMESTR + " =:pvVariantcodeStr LIMIT 1")
    cReturnorderBarcodeEntity getBarcodeForLine(String pvItemNoStr, String pvVariantcodeStr);
}
