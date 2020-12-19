package SSU_WHS.PackAndShip.PackAndShipOrders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_PACKANDSHIPORDER)
public class cPackAndShipOrderEntity {

    //Region Public Properties
    @ColumnInfo(name= cDatabase.NUMBEROFBINS_NAMESTR)
    public String numberofBins;
    public String getNumberofBinsStr() {return this.numberofBins;}

    @ColumnInfo(name= cDatabase.ASSIGNEDUSERID_NAMESTR)
    public String assignedUserId;
    public String getAssignedUserIdStr() {return this.assignedUserId;}

    @ColumnInfo(name= cDatabase.CURRENTLOCATION_NAMESTR)
    public String currentLocation;
    public String getCurrentLocationStr() {return this.currentLocation;}

    @ColumnInfo(name= cDatabase.CURRENTUSERID_NAMESTR)
    public String currentUserId;
    public String getCurrentUserIdStr() {return this.currentUserId;}

    @ColumnInfo(name= cDatabase.DESTINATIONNO_NAMESTR)
    public String destinationNo;
    public String getDestinationStr() {return this.destinationNo;}

    @ColumnInfo(name= cDatabase.DOCUMENT_NAMESTR)
    public String document;
    public String getDocumentStr() {return this.document;}

    @ColumnInfo(name= cDatabase.DOCUMENT2_NAMESTR)
    public String document2;
    public String getDocument2Str() {return this.document2;}

    @ColumnInfo(name= cDatabase.DOCUMENTTYPE_NAMESTR)
    public String documentType;
    public String getDocumentTypeStr() {return this.documentType;}

    @ColumnInfo(name= cDatabase.DOCUMENTTYPE2_NAMESTR)
    public String documentType2;
    public String getDocumentType2Str() {return this.documentType2;}

    @ColumnInfo(name= cDatabase.EXTERNALREFERENCE_NAMESTR)
    public String externalReference;
    public String getExternalReferenceStr() {return this.externalReference;}

    @PrimaryKey @NonNull
    @ColumnInfo(name= cDatabase.ORDERNUMBER_NAMESTR)
    public String ordernumber = "";
    public String getOrdernumberStr() {return this.ordernumber;}

    @ColumnInfo(name= cDatabase.ORDERTYPE_NAMESTR)
    public String ordertype;
    public String getOrderTypeStr() {return this.ordertype;}

    @ColumnInfo(name= cDatabase.SOURCEDOCUMENT_NAMESTR)
    public String sourceDocument;
    public String getSourceDocumentStr() {return this.sourceDocument;}

    @ColumnInfo(name= cDatabase.STATUS_NAMESTR)
    public String status;
    public String getStatusStr() {return this.status;}

    @ColumnInfo(name= cDatabase.WORKPLACE_NAMESTR)
    public String workplace;
    public String getWorkplaceStr() {return this.workplace;}

    @ColumnInfo(name="IsProcessingOrParked")
    public Boolean isprocessingorparked;
    public Boolean getIsProcessingOrParkedStr() {return this.isprocessingorparked;}

    @ColumnInfo(name="Priority")
    public int priorityInt;
    public int getPriorityInt() {return this.priorityInt;}

    //End Region Public Properties

    //Region Constructor
    public cPackAndShipOrderEntity() {

    }

    public cPackAndShipOrderEntity(JSONObject pvJsonObject) {
        try {
            this.numberofBins = pvJsonObject.getString(cDatabase.NUMBEROFBINS_NAMESTR);
            this.assignedUserId = pvJsonObject.getString(cDatabase.ASSIGNEDUSERID_NAMESTR);
            this.currentLocation = pvJsonObject.getString(cDatabase.CURRENTLOCATION_NAMESTR);
            this.currentUserId = pvJsonObject.getString(cDatabase.CURRENTUSERID_NAMESTR);
            this.destinationNo = pvJsonObject.getString(cDatabase.DESTINATIONNO_NAMESTR);
            this.document = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            this.document2 = pvJsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);
            this.documentType = pvJsonObject.getString(cDatabase.DOCUMENTTYPE_NAMESTR);
            this.documentType2 = pvJsonObject.getString(cDatabase.DOCUMENTTYPE2_NAMESTR);
            this.externalReference = pvJsonObject.getString(cDatabase.EXTERNALREFERENCE_NAMESTR);

            this.ordernumber = pvJsonObject.getString(cDatabase.ORDERNUMBER_NAMESTR);
            this.ordertype = pvJsonObject.getString(cDatabase.ORDERTYPE_NAMESTR);
            this.sourceDocument = pvJsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR);

            this.status = pvJsonObject.getString(cDatabase.STATUS_NAMESTR);
            this.workplace = pvJsonObject.getString(cDatabase.WORKPLACE_NAMESTR);


            this.isprocessingorparked = !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship_Busy)) &&
                    !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship_Wait));

            this.priorityInt = 6;

            if (this.currentUserId.equalsIgnoreCase(cUser.currentUser.getUsernameStr()) && (this.isprocessingorparked)) {
                this.priorityInt = 1;
                return;
            }

            if (this.currentUserId.equalsIgnoreCase(cUser.currentUser.getUsernameStr()) && (!this.isprocessingorparked)) {
                this.priorityInt = 2;
                return;
            }

            if (this.assignedUserId.equalsIgnoreCase(cUser.currentUser.getUsernameStr())) {
                this.priorityInt = 3;
                return;
            }

            if (this.assignedUserId.isEmpty()) {
                this.priorityInt = 4;
                return;
            }

            if (!this.assignedUserId.equalsIgnoreCase(cUser.currentUser.getUsernameStr())) {
                this.priorityInt = 5;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor

}


