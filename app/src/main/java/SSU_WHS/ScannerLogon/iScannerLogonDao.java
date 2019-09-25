package SSU_WHS.ScannerLogon;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iScannerLogonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cScannerLogonEntity scannerLogonEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SCANNERLOGON)
    void deleteAll();

    @Query("SELECT * FROM "  + cDatabase.TABLENAME_SCANNERLOGON)
    List<cScannerLogonEntity> getAll();
}
