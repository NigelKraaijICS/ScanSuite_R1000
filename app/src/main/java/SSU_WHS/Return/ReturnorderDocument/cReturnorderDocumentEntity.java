package SSU_WHS.Return.ReturnorderDocument;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_RETURNORDERDOCUMENT)
public class cReturnorderDocumentEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name= cDatabase.SOURCEDOCUMENT_NAMESTR)
    public String sourceDocument;
    public String getSourceDocumentStr() {return this.sourceDocument;}

    @ColumnInfo(name= cDatabase.STATUS_NAMESTR)
    public int status;
    public int getStatusInt() {return this.status;}

    //End Region Public Properties

    public cReturnorderDocumentEntity(){

    }

    public cReturnorderDocumentEntity(@NonNull String pvSourceNoStr) {
        this.sourceDocument = pvSourceNoStr;
        this.status = cWarehouseorder.ReturnDocumentStatusEnu.New;
    }



    //End Region Constructor

}


