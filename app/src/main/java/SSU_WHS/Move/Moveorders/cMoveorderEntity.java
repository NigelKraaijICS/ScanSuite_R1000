package SSU_WHS.Move.MoveOrders;

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

@Entity(tableName= cDatabase.TABLENAME_MOVEORDER)
public class cMoveorderEntity {

    //Region Public Properties
    @PrimaryKey @NonNull
    @ColumnInfo(name= cDatabase.ORDERNUMBER_NAMESTR)
    public String ordernumber = "";
    public String getOrdernumberStr() {return this.ordernumber;}

    @ColumnInfo(name= cDatabase.ORDERTYPE_NAMESTR)
    public String ordertype;
    public String getOrderTypeStr() {return this.ordertype;}

    @ColumnInfo(name= cDatabase.NUMBEROFBINS_NAMESTR)
    public String numberofBins;
    public String getNumberofBinsStr() {return this.numberofBins;}

    @ColumnInfo(name= cDatabase.ASSIGNEDUSERID_NAMESTR)
    public String assignedUserId;
    public String getAssignedUserIdStr() {return this.assignedUserId;}

    @ColumnInfo(name= cDatabase.CURRENTUSERID_NAMESTR)
    public String currentUserId;
    public String getCurrentUserIdStr() {return this.currentUserId;}

    @ColumnInfo(name= cDatabase.STATUS_NAMESTR)
    public String status;
    public String getStatusStr() {return this.status;}

    @ColumnInfo(name= cDatabase.BINCODE_NAMESTR)
    public String bincode;
    public String getBincodeStr() {return this.externalReference;}

    @ColumnInfo(name= cDatabase.EXTERNALREFERENCE_NAMESTR)
    public String externalReference;
    public String getExternalReferenceStr() {return this.bincode;}

    @ColumnInfo(name= cDatabase.SOURCEDOCUMENT_NAMESTR)
    public String sourceDocument;
    public String getSourceDocumentStr() {return this.sourceDocument;}

    @ColumnInfo(name= cDatabase.DOCUMENT_NAMESTR)
    public String document;
    public String getDocumentStr() {return this.document;}

    @ColumnInfo(name= cDatabase.DOCUMENT2_NAMESTR)
    public String document2;
    public String getDocument2Str() {return this.document2;}

    @ColumnInfo(name= cDatabase.MOVEAMOUNTMANUAL_NAMESTR)
    public String moveAmountManual;
    public String getMoveAmountManualStr() {return this.moveAmountManual;}

    @ColumnInfo(name= cDatabase.MOVEBARCODECHECK_NAMESTR)
    public String moveBarcodeCheck;
    public String getMoveBarcodeCheckStr() {return this.moveBarcodeCheck;}

    @ColumnInfo(name= cDatabase.MOVEVALIDATESTOCK_NAMESTR)
    public String moveValidateStock;
    public String getMoveValidateStockStr() {return this.moveValidateStock;}

    @ColumnInfo(name= cDatabase.MOVEVALIDATESTOCKENFORCE_NAMESTR)
    public String moveValidateStockEnforce;
    public String getMoveValidateStockEnforceStr() {return this.moveValidateStockEnforce;}

    @ColumnInfo(name= cDatabase.MOVE_AUTOACCEPTATREQUESTED_NAMESTR)
    public String moveAutoAcceptAtRequested;
    public String getMoveAutoAcceptAtRequestedStr() {return this.moveAutoAcceptAtRequested;}

    @ColumnInfo(name= cDatabase.MOVE_NOEXTRABINS_NAMESTR)
    public String moveNoExtraBins;
    public String getMoveNoExtraBinsStr() {return this.moveNoExtraBins;}

    @ColumnInfo(name= cDatabase.MOVE_NOEXTRAITEMS_NAMESTR)
    public String moveNoExtraItems;
    public String getMoveNoExtraItemsStr() {return this.moveNoExtraItems;}

    @ColumnInfo(name= cDatabase.MOVE_NOEXTRAPIECES_NAMESTR)
    public String moveNoExtraPieces;
    public String getMoveNoExtraPiecesStr() {return this.moveNoExtraPieces;}

    @ColumnInfo(name="IsProcessingOrParked")
    public Boolean isprocessingorparked;
    public Boolean getIsProcessingOrParkedStr() {return this.isprocessingorparked;}

    @ColumnInfo(name="Priority")
    public int priorityInt;
    public int getPriorityInt() {return this.priorityInt;}


    //End Region Public Properties

    //Region Constructor
    public cMoveorderEntity() {

    }

    public cMoveorderEntity(JSONObject pvJsonObject) {
        try {
            this.ordernumber = pvJsonObject.getString(cDatabase.ORDERNUMBER_NAMESTR);
            this.ordertype = pvJsonObject.getString(cDatabase.ORDERTYPE_NAMESTR);
            this.numberofBins = pvJsonObject.getString(cDatabase.NUMBEROFBINS_NAMESTR);
            this.assignedUserId = pvJsonObject.getString(cDatabase.ASSIGNEDUSERID_NAMESTR);
            this.currentUserId = pvJsonObject.getString(cDatabase.CURRENTUSERID_NAMESTR);
            this.status = pvJsonObject.getString(cDatabase.STATUS_NAMESTR);

            this.bincode = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.externalReference = pvJsonObject.getString(cDatabase.EXTERNALREFERENCE_NAMESTR);

            this.moveAmountManual = pvJsonObject.getString(cDatabase.MOVEAMOUNTMANUAL_NAMESTR);
            this.moveBarcodeCheck = pvJsonObject.getString(cDatabase.MOVEBARCODECHECK_NAMESTR);
            this.moveValidateStock = pvJsonObject.getString(cDatabase.MOVEVALIDATESTOCK_NAMESTR);
            this.moveValidateStockEnforce = pvJsonObject.getString(cDatabase.MOVEVALIDATESTOCKENFORCE_NAMESTR);

            this.moveAutoAcceptAtRequested = pvJsonObject.getString(cDatabase.MOVE_AUTOACCEPTATREQUESTED_NAMESTR);
            this.moveNoExtraBins = pvJsonObject.getString(cDatabase.MOVE_NOEXTRABINS_NAMESTR);
            this.moveNoExtraItems = pvJsonObject.getString(cDatabase.MOVE_NOEXTRAITEMS_NAMESTR);
            this.moveNoExtraPieces = pvJsonObject.getString(cDatabase.MOVE_NOEXTRAPIECES_NAMESTR);

            this.sourceDocument = pvJsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR);
            this.document = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            this.document2 = pvJsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);

            this.isprocessingorparked = !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.MoveStatusEnu.Move_Take_Busy)) &&
                    !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.MoveStatusEnu.Move_Take_Wait)) &&
                    !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.MoveStatusEnu.Move_Place_Busy)) &&
                     !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.MoveStatusEnu.Move_Place_Wait));

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


