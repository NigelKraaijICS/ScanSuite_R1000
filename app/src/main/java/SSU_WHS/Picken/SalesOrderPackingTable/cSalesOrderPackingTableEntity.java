package SSU_WHS.Picken.SalesOrderPackingTable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import SSU_WHS.General.cDatabase;

    @Entity(tableName = cDatabase.TABLENAME_SALESORDERPACKINGTABLE, primaryKeys = {"SalesOrder"})
    public class cSalesOrderPackingTableEntity {
        @NonNull
        @ColumnInfo(name="SalesOrder")
        public String salesorder;
        @NonNull
        @ColumnInfo(name="Packingtable")
        public String packingtable;

        //empty constructor
        public cSalesOrderPackingTableEntity() {

        }

        @NonNull
        public String getSalesorder() {
            return salesorder;
        }

        public void setSalesorder(@NonNull String salesorder) {
            this.salesorder = salesorder;
        }

        public String getPackingtable() {
            return packingtable;
        }

        public void setPackingtable(String packingtable) {
            this.packingtable = packingtable;
        }

    }

