package SSU_WHS.Receive.ReceiveLines;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import SSU_WHS.General.cDatabase;

@Dao
public interface iReceiveorderLineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SSU_WHS.Receive.ReceiveLines.cReceiveorderLineEntity receiveorderLineEntity);

    @Query("DELETE FROM " + cDatabase.TABLENAME_RECEIVELINES)
    void deleteAll();

    @Query("SELECT * FROM " +  cDatabase.TABLENAME_RECEIVELINES)
    List<SSU_WHS.Receive.ReceiveLines.cReceiveorderLineEntity> getReceiveorderLinesFromDatabase();

    @Query("UPDATE ReceiveLines SET Localstatus = :pvNewStatusInt WHERE LineNo = :pvLineNoInt")
    int updateOrderLineLocalStatus(Integer pvLineNoInt, Integer pvNewStatusInt);

    @Query("UPDATE ReceiveLines SET QuantityHandled = :pvQuantityHandledDbl WHERE LineNo = :pvLineNoInt")
    int updateOrderLineQuantityHandled(Integer pvLineNoInt, Double pvQuantityHandledDbl);

    @Query("UPDATE ReceiveLines SET Quantity = :pvQuantityDbl WHERE LineNo = :pvLineNoInt")
    int updateOrderLineQuantity(Integer pvLineNoInt, Double pvQuantityDbl);

}
