package SSU_WHS.Return.ReturnorderDocument;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Dao
public interface iReturnorderDocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cReturnorderDocumentEntity returnorderBinEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_RETURNORDERDOCUMENT)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_RETURNORDERDOCUMENT)
    List<cReturnorderDocumentEntity> getAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_RETURNORDERDOCUMENT +
           " WHERE " + cDatabase.STATUS_NAMESTR + " = " + cWarehouseorder.WorkflowReturnStepEnu.Return)
    List<cReturnorderDocumentEntity> getReturnorderDocumentNotDone();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_RETURNORDERDOCUMENT +
            " WHERE " + cDatabase.STATUS_NAMESTR + " = " + cWarehouseorder.WorkflowReturnStepEnu.ReturnPause )
    List<cReturnorderDocumentEntity> getReturnorderDocumentDone();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_RETURNORDERDOCUMENT)
    List<cReturnorderDocumentEntity> getReturnorderDocumentTotal();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_RETURNORDERDOCUMENT +
            " WHERE " + cDatabase.SOURCEDOCUMENT_NAMESTR + " =:pvSourceDocumentStr" )
    cReturnorderDocumentEntity checkBin(String pvSourceDocumentStr);


    @Query("UPDATE ReturnorderDocument SET Status = :pvStatusInt   WHERE  Sourcedocument = :pvSourcedocumentStr")
    int updateBinStatus(String pvSourcedocumentStr, int pvStatusInt);


}
