package SSU_WHS.Picken.SalesOrderPackingTable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import SSU_WHS.General.cDatabase;

    @Entity(tableName = cDatabase.TABLENAME_SALESORDERPACKINGTABLE, primaryKeys = {cDatabase.SALESORDER_NAMESTR})

    public class cSalesOrderPackingTableEntity {

        //Region Public Properties

        @NonNull
        @ColumnInfo(name=cDatabase.SALESORDER_NAMESTR)
        public String salesorder;
        @NonNull
        public String getSalesOrderStr() {
            return salesorder;
        }

        @NonNull
        @ColumnInfo(name=cDatabase.PACKINGTABLE_NAMESTR)
        public String packingtable;
        public String getPackingTableStr() {
            return packingtable;
        }

        // End Region Public Properties

        // Region Constructor

        cSalesOrderPackingTableEntity(){
            // empty constructor
        }

        public cSalesOrderPackingTableEntity(String pvSalesOrderStr, String pvPackingTableStr) {
            this.salesorder = pvSalesOrderStr;
            this.packingtable = pvPackingTableStr;
        }
                //End Region Constructor

    }

