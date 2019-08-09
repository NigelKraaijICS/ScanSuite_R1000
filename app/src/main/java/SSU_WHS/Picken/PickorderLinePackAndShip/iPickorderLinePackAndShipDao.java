package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iPickorderLinePackAndShipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity);

    @Delete
    void deletePickorder(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
    void deleteAll();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
    List<cPickorderLinePackAndShipEntity> getAll();

    @Query("SELECT SUM(Quantity) FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
    Double getTotalArticles();

    //todo: change this to salesorders to send
    @Query("SELECT SUM(QuantityHandled) FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " WHERE " + cDatabase.STATUSSHIPPING_NAMESTR + " = " + cPickorderLinePackAndShip.STATUSSHIPPING_STEP1_TOREPORT )
    Double getHandledArticles();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
    LiveData<List<cPickorderLinePackAndShipEntity>> getTotalPickorderLinePackAndShipEntities();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " WHERE " + cDatabase.LOCALSTATUS_NAMESTR + " > " + cPickorderLinePackAndShip.LOCALSTATUS_NEW)
    LiveData<List<cPickorderLinePackAndShipEntity>> getHandledPickorderLinePackAndShipEntities();

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " WHERE Localstatus = " + cPickorderLinePackAndShip.LOCALSTATUS_NEW)
    LiveData<List<cPickorderLinePackAndShipEntity>> getNotHandledPickorderLinePackAndShipEntities();

    @Query("UPDATE " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " SET QuantityHandled = :pv_quantityHandled WHERE recordid = :pv_recordid")
    int updateOrderLinePackAndShipQuantity(Integer pv_recordid, Double pv_quantityHandled);

    @Query("UPDATE " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " SET Localstatus = :pv_newStatus WHERE recordid = :pv_recordid")
    int updateOrderLinePackAndShipLocalStatus(Integer pv_recordid, Integer pv_newStatus);

    @Query("UPDATE " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP + " SET Localstatus = :pv_newstatus WHERE  " + cDatabase.SOURCENO_NAMESTR + "= :pv_sourceno")
    int updateOrderLinePackAndShipLocalStatusBySourceNo(String pv_sourceno, Integer pv_newstatus);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP +
            " WHERE Localstatus = " + cPickorderLinePackAndShip.LOCALSTATUS_NEW +
            " AND " + cDatabase.SOURCENO_NAMESTR + " =:sourceno")
    List<cPickorderLinePackAndShipEntity> getNotHandledPickorderLinePackAndShipEntitiesBySourceNo(String sourceno);

    @Query("SELECT * FROM " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP +
            " WHERE Localstatus = " + cPickorderLinePackAndShip.LOCALSTATUS_NEW +
            " AND " + cDatabase.PROCESSINGSEQUENCE_NAMESTR + " =:processingsequence")
    List<cPickorderLinePackAndShipEntity> getNotHandledPickorderLinePackAndShipEntitiesByProcessingSequence(String processingsequence);

    @Query("SELECT " + cDatabase.SOURCENO_NAMESTR + ", SUM(" + cDatabase.QUANTITYHANDLED_NAMESTR + ") AS " + cDatabase.QUANTITYHANDLEDSUM_NAMESTR  + ", " + cDatabase.PROCESSINGSEQUENCE_NAMESTR + " FROM  " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP +
            " WHERE Localstatus = " + cPickorderLinePackAndShip.LOCALSTATUS_NEW +
            " GROUP BY " + cDatabase.SOURCENO_NAMESTR + ", " + cDatabase.PROCESSINGSEQUENCE_NAMESTR
            )
    LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> getNotHandledPickorderinePackAndShipEntitiesDistinctSourceno();

    @Query("SELECT " + cDatabase.SOURCENO_NAMESTR + ", SUM(" + cDatabase.QUANTITYHANDLED_NAMESTR + ") AS " + cDatabase.QUANTITYHANDLEDSUM_NAMESTR + ", " + cDatabase.PROCESSINGSEQUENCE_NAMESTR + " FROM  " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP +
            " WHERE Localstatus > " + cPickorderLinePackAndShip.LOCALSTATUS_NEW +
            " GROUP BY " + cDatabase.SOURCENO_NAMESTR + ", " + cDatabase.PROCESSINGSEQUENCE_NAMESTR
    )
    LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> getHandledPickorderinePackAndShipEntitiesDistinctSourceno();

    @Query("SELECT " + cDatabase.SOURCENO_NAMESTR + ", SUM(" + cDatabase.QUANTITYHANDLED_NAMESTR + ") AS " + cDatabase.QUANTITYHANDLEDSUM_NAMESTR + ", " + cDatabase.PROCESSINGSEQUENCE_NAMESTR + " FROM  " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP +
            " GROUP BY " + cDatabase.SOURCENO_NAMESTR + ", " + cDatabase.PROCESSINGSEQUENCE_NAMESTR
    )
    LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> getAllPickorderinePackAndShipEntitiesDistinctSourceno();

    @Query("SELECT COUNT(DISTINCT " + cDatabase.SOURCENO_NAMESTR + ") FROM  " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP +
            " WHERE Localstatus = " + cPickorderLinePackAndShip.LOCALSTATUS_NEW
    )
    int getNumberToShipForCounter();

    @Query("SELECT COUNT(DISTINCT " + cDatabase.SOURCENO_NAMESTR + ") FROM  " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP +
            " WHERE Localstatus > " + cPickorderLinePackAndShip.LOCALSTATUS_NEW
    )
    int getNumberShippedForCounter();

    @Query("SELECT COUNT(DISTINCT " + cDatabase.SOURCENO_NAMESTR + ") FROM  " + cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
    int getNumberTotalForCounter();
}
