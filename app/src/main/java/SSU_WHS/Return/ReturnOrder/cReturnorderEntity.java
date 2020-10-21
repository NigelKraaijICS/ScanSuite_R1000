package SSU_WHS.Return.ReturnOrder;

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

@Entity(tableName= cDatabase.TABLENAME_RETURNORDER)
public class cReturnorderEntity {

    //Region Public Properties
    @PrimaryKey @NonNull
    @ColumnInfo(name= cDatabase.ORDERNUMBER_NAMESTR)
    public String ordernumberStr = "";
    @NonNull
    public String getOrdernumberStr() {return this.ordernumberStr;}

    @ColumnInfo(name= cDatabase.ORDERTYPE_NAMESTR)
    public String ordertypeStr;
    public String getOrderTypeStr() {return this.ordertypeStr;}

    @ColumnInfo(name= cDatabase.ASSIGNEDUSERID_NAMESTR)
    public String assignedUserIdStr;
    public String getAssignedUserIdStr() {return this.assignedUserIdStr;}

    @ColumnInfo(name= cDatabase.CURRENTUSERID_NAMESTR)
    public String currentUserIdStr;
    public String getCurrentUserIdStr() {return this.currentUserIdStr;}

    @ColumnInfo(name= cDatabase.STATUS_NAMESTR)
    public String statusStr;
    public String getStatusStr() {return this.statusStr;}

    @ColumnInfo(name = cDatabase.BINCODE_NAMESTR)
    public String bincodeStr;
    public String getBincodeStr() {return  this.bincodeStr;}

    @ColumnInfo(name = cDatabase.CURRENTLOCATION_NAMESTR)
    public String currentLocationStr;
    public String getCurrentLocationStr() {return  this.currentLocationStr;}

    @ColumnInfo(name= cDatabase.EXTERNALREFERENCE_NAMESTR)
    public String externalReferenceStr;
    public String getExternalReferenceStr() {return this.externalReferenceStr;}

    @ColumnInfo(name = cDatabase.RETOURAMAOUNTMANUAL_NAMESTR)
    public Boolean retourAmountManualBln;
    public Boolean getRetourAmountManualBln(){return this.retourAmountManualBln;}

    @ColumnInfo(name = cDatabase.RETOURBARCODECHECK_NAMESTR)
    public Boolean retourBarcodeCheckBln;
    public Boolean getRetourBarcodeCheckBln(){return this.retourBarcodeCheckBln;}

    @ColumnInfo(name = cDatabase.RETOURMULTIDOCUMENT_NAMESTR)
    public Boolean retourMultiDocumentBln;
    public Boolean getRetourMultiDocumentBln(){return this.retourMultiDocumentBln;}

    @ColumnInfo(name= cDatabase.SOURCEDOCUMENT_NAMESTR)
    public String sourceDocumentStr;
    public String getSourceDocumentStr() {return this.sourceDocumentStr;}

    @ColumnInfo(name= cDatabase.DOCUMENT_NAMESTR)
    public String documentStr;
    public String getDocumentStr() {return this.documentStr;}

    @ColumnInfo(name= cDatabase.DOCUMENT2_NAMESTR)
    public String document2Str;
    public String getDocument2Str() {return this.document2Str;}

    @ColumnInfo(name = cDatabase.REASON_NAMESTR)
    public String reasonStr;
    public String getReasonStr() {return this.reasonStr;}

    @ColumnInfo(name = cDatabase.RETOURORDERBINNOCHECK_NAMESTR)
    public Boolean retourOrderBINNoCheckBln;
    public Boolean getRetourOrderBINNoCheckBln() {return this.retourOrderBINNoCheckBln;}

    @ColumnInfo(name=cDatabase.ISPROCESSINGORPARKED_NAMESTR)
    public Boolean isprocessingorparked;
    public Boolean getIsProcessingOrParkedStr() {return this.isprocessingorparked;}

    @ColumnInfo(name=cDatabase.PRIORITY_NAMESTR)
    public int priorityInt;
    public int getPriorityInt() {return this.priorityInt;}

    //End Region Public Properties

    //Region Constructor
    public cReturnorderEntity() {

    }

    public cReturnorderEntity(JSONObject pvJsonObject) {
        try {
            this.ordernumberStr = pvJsonObject.getString(cDatabase.ORDERNUMBER_NAMESTR);
            this.ordertypeStr = pvJsonObject.getString(cDatabase.ORDERTYPE_NAMESTR);
            this.assignedUserIdStr = pvJsonObject.getString(cDatabase.ASSIGNEDUSERID_NAMESTR);
            this.currentUserIdStr = pvJsonObject.getString(cDatabase.CURRENTUSERID_NAMESTR);
            this.statusStr = pvJsonObject.getString(cDatabase.STATUS_NAMESTR);
            this.bincodeStr = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.currentLocationStr = pvJsonObject.getString(cDatabase.CURRENTLOCATION_NAMESTR);
            this.externalReferenceStr = pvJsonObject.getString(cDatabase.EXTERNALREFERENCE_NAMESTR);
            this.retourAmountManualBln = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.RETOURAMAOUNTMANUAL_NAMESTR),false);
            this.retourBarcodeCheckBln = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.RETOURBARCODECHECK_NAMESTR),false);
            this.retourMultiDocumentBln = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.RETOURMULTIDOCUMENT_NAMESTR),false);
            this.sourceDocumentStr = pvJsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR);
            this.documentStr = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            this.document2Str = pvJsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);
            this.reasonStr = pvJsonObject.getString(cDatabase.REASON_NAMESTR);
            this.retourOrderBINNoCheckBln =  cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.RETOURORDERBINNOCHECK_NAMESTR), false);

            //Is processing

            this.isprocessingorparked = !this.getStatusStr().equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowReturnStepEnu.Return)) &&
                    !this.getStatusStr().equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowReturnStepEnu.ReturnBusy));

            this.priorityInt = 6;

            if (this.getCurrentUserIdStr().equalsIgnoreCase(cUser.currentUser.getUsernameStr()) && (this.getIsProcessingOrParkedStr())) {
                this.priorityInt = 1;
                return;
            }

            if (this.getCurrentUserIdStr().equalsIgnoreCase(cUser.currentUser.getUsernameStr()) && (!this.getIsProcessingOrParkedStr())) {
                this.priorityInt = 2;
                return;
            }

            if (this.getCurrentUserIdStr().equalsIgnoreCase(cUser.currentUser.getUsernameStr())) {
                this.priorityInt = 3;
                return;
            }

            if (this.getCurrentUserIdStr().isEmpty()) {
                this.priorityInt = 4;
                return;
            }

            if (!this.getCurrentUserIdStr().equalsIgnoreCase(cUser.currentUser.getUsernameStr())) {
                this.priorityInt = 5;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor

}


