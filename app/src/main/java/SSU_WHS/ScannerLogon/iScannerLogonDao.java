package SSU_WHS.ScannerLogon;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iScannerLogonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cScannerLogonEntity scannerLogonEntity);

    @Delete
    void deleteScannerLogon(cScannerLogonEntity scannerLogonEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_SCANNERLOGON)
    void deleteAll();

    @Query("SELECT * FROM "  + cDatabase.TABLENAME_SCANNERLOGON)
    List<cScannerLogonEntity> getAll();
}
