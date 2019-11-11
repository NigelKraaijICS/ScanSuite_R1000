package SSU_WHS.Inventory.InventoryOrders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_INVENTORYORDER)
public class cInventoryorderEntity {

    //Region Public Properties
    @PrimaryKey @NonNull
    @ColumnInfo(name= cDatabase.ORDERNUMBER_NAMESTR)
    public String ordernumber;
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

    @ColumnInfo(name= cDatabase.INV_AUTOCLOSE_BIN_NAMESTR)
    public String invAutoCloseBin;
    public String getInvAutoCloseBinStr() {return this.invAutoCloseBin;}

    @ColumnInfo(name= cDatabase.INV_PRECOUNT_NAMESTR)
    public String invPrecount;
    public String getInvPrecountStr() {return this.invPrecount;}

    @ColumnInfo(name= cDatabase.INV_AMOUNT_MANUAL_NAMESTR)
    public String invAmountManual;
    public String getInvAmountManualStr() {return this.invAmountManual;}

    @ColumnInfo(name= cDatabase.INV_BARCODECHECK_NAMESTR)
    public String invBarcodeCheck;
    public String getInvBarcodeCheckStr() {return this.invBarcodeCheck;}

    @ColumnInfo(name= cDatabase.INV_ADD_EXTRA_BIN_NAMESTR)
    public String invAddExtraBin;
    public String getInvAddExtraBinStr() {return this.invAddExtraBin;}

    @ColumnInfo(name= cDatabase.EXTERNALREFERENCE_NAMESTR)
    public String externalReference;
    public String getExternalReferenceStr() {return this.externalReference;}

    @ColumnInfo(name= cDatabase.WORKPLACE_NAMESTR)
    public String workplace;
    public String getWorkplaceStr() {return this.workplace;}

    @ColumnInfo(name= cDatabase.STOCKOWNER_NAMESTR)
    public String stockOwner;
    public String getStockOwnerStr() {return this.stockOwner;}

    @ColumnInfo(name= cDatabase.SOURCEDOCUMENT_NAMESTR)
    public String sourceDocument;
    public String getSourceDocumentStr() {return this.sourceDocument;}

    @ColumnInfo(name= cDatabase.DOCUMENT_NAMESTR)
    public String document;
    public String getDocumentStr() {return this.document;}

    @ColumnInfo(name= cDatabase.DOCUMENT2_NAMESTR)
    public String document2;
    public String getDocument2Str() {return this.document2;}

    @ColumnInfo(name= cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR)
    public String webservicetimeouterpins;
    public String getWebserviceTimeOutERPInsStr() {return this.webservicetimeouterpins;}

    @ColumnInfo(name= cDatabase.INTERFACERESULTMETHOD_NAMESTR)
    public String interfaceresultmethod;
    public String getInterfaceResultMethodStr() {return this.interfaceresultmethod;}

    @ColumnInfo(name="IsProcessingOrParked")
    public Boolean isprocessingorparked;
    public Boolean getIsProcessingOrParkedStr() {return this.isprocessingorparked;}

    //End Region Public Properties

    //Region Constructor
    public cInventoryorderEntity() {

    }

    public cInventoryorderEntity(JSONObject pvJsonObject) {
        try {
            this.ordernumber = pvJsonObject.getString(cDatabase.ORDERNUMBER_NAMESTR);
            this.ordertype = pvJsonObject.getString(cDatabase.ORDERTYPE_NAMESTR);
            this.numberofBins = pvJsonObject.getString(cDatabase.NUMBEROFBINS_NAMESTR);
            this.assignedUserId = pvJsonObject.getString(cDatabase.ASSIGNEDUSERID_NAMESTR);
            this.currentUserId = pvJsonObject.getString(cDatabase.CURRENTUSERID_NAMESTR);
            this.status = pvJsonObject.getString(cDatabase.STATUS_NAMESTR);

            this.invAutoCloseBin = pvJsonObject.getString(cDatabase.INV_AUTOCLOSE_BIN_NAMESTR);
            this.invPrecount = pvJsonObject.getString(cDatabase.INV_PRECOUNT_NAMESTR);
            this.invAmountManual = pvJsonObject.getString(cDatabase.INV_AMOUNT_MANUAL_NAMESTR);
            this.invBarcodeCheck = pvJsonObject.getString(cDatabase.INV_BARCODECHECK_NAMESTR);
            this.invAddExtraBin = pvJsonObject.getString(cDatabase.INV_ADD_EXTRA_BIN_NAMESTR);

            this.externalReference = pvJsonObject.getString(cDatabase.EXTERNALREFERENCE_NAMESTR);
            this.workplace = pvJsonObject.getString(cDatabase.WORKPLACE_NAMESTR);
            this.stockOwner = pvJsonObject.getString(cDatabase.STOCKOWNER_NAMESTR);
            this.sourceDocument = pvJsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR);
            this.document = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            this.document2 = pvJsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);
            this.webservicetimeouterpins = pvJsonObject.getString(cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR);
            this.interfaceresultmethod = pvJsonObject.getString(cDatabase.INTERFACERESULTMETHOD_NAMESTR);

            //Is processing
            this.isprocessingorparked = true;

            if (this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowInventoryStepEnu.Inventory))) {
                this.isprocessingorparked = false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor

}

