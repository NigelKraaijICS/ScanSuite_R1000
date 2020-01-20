package SSU_WHS.Intake.Intakeorders;

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

@Entity(tableName= cDatabase.TABLENAME_INTAKEORDER)
public class cIntakeorderEntity {
    //Region Public Properties

    @PrimaryKey
    @NonNull
    @ColumnInfo(name= cDatabase.ORDERNUMBER_NAMESTR)
    public String ordernumber = "" ;
    public String getOrdernumberStr() {return this.ordernumber;}

    @ColumnInfo(name= cDatabase.ORDERTYPE_NAMESTR)
    public String ordertype;
    public String getOrderTypeStr() {return this.ordertype;}

    @ColumnInfo(name= cDatabase.NUMBEROFBINS_NAMESTR)
    public int numberofBins;
    public int getNumberofBinsInt() {return this.numberofBins;}

    @ColumnInfo(name= cDatabase.ASSIGNEDUSERID_NAMESTR)
    public String assignedUserId;
    public String getAssignedUserIdStr() {return this.assignedUserId;}

    @ColumnInfo(name= cDatabase.CURRENTUSERID_NAMESTR)
    public String currentUserId;
    public String getCurrentUserIdStr() {return this.currentUserId;}

    @ColumnInfo(name= cDatabase.REQUESTEDSTARTDATETIME_NAMESTR)
    public String requestedStartDateTime;
    public String getRequestedStartDateTimeStr() {return this.requestedStartDateTime;}

    @ColumnInfo(name= cDatabase.REQUESTEDENDDATETIME_NAMESTR)
    public String requestedEndDateTime;
    public String getRequestedEndDateTimeStr() {return this.requestedEndDateTime;}

    @ColumnInfo(name= cDatabase.STATUS_NAMESTR)
    public int status;
    public int getStatusInt() {return this.status;}

    @ColumnInfo(name= cDatabase.BINCODE_NAMESTR)
    public String binCode;
    public String getBinCodeStr() {return this.binCode;}

    @ColumnInfo(name= cDatabase.EXTERNALREFERENCE_NAMESTR)
    public String externalReference;
    public String getExternalReferenceStr() {return this.externalReference;}

    @ColumnInfo(name= cDatabase.WORKPLACE_NAMESTR)
    public String workplace;
    public String getWorkplaceStr() {return this.workplace;}

    @ColumnInfo(name= cDatabase.STOCKOWNER_NAMESTR)
    public String stockOwner;
    public String getStockOwnerStr() {return this.stockOwner;}

    @ColumnInfo(name= cDatabase.RECEIVEEXPORTPART_NAMESTR)
    public String receiveExportPart;
    public String getReceiveExportPartStr() {return this.receiveExportPart;}

    @ColumnInfo(name= cDatabase.RECEIVEAMOUNTMANUAL_EO_NAMESTR)
    public String receiveAmountManualEO;
    public String getReceiveAmountManualEOStr() {return this.receiveAmountManualEO;}

    @ColumnInfo(name= cDatabase.RECEIVEAMOUNTMANUAL_OM_NAMESTR)
    public String receiveAmountManualOM;
    public String getReceiveAmountManualOMStr() {return this.receiveAmountManualOM;}

    @ColumnInfo(name= cDatabase.RECEIVEAMOUNTMANUAL_MA_NAMESTR)
    public String receiveAmountManualMA;
    public String getReceiveAmountManualMAStr() {return this.receiveAmountManualMA;}

    @ColumnInfo(name= cDatabase.RECEIVEBARCODECHECK_NAMESTR)
    public String receiveBarcodeCheck;
    public String getReceiveBarcodeCheckStr() {return this.receiveBarcodeCheck;}

    @ColumnInfo(name= cDatabase.RECEIVESTOREAUTOACCEPTATREQUESTED_NAMESTR)
    public String receiveStoreAutoAcceptAtRequested;
    public String getReceiveStoreAutoAcceptAtRequestedStr() {return this.receiveStoreAutoAcceptAtRequested;}

    @ColumnInfo(name= cDatabase.RECEIVESTOREAUTOACCEPTATNEWITEM_NAMESTR)
    public String receiveStoreAutoAcceptAtNewItem;
    public String getreceiveStoreAutoAcceptAtNewItem() {return this.receiveStoreAutoAcceptAtNewItem;}

    @ColumnInfo(name= cDatabase.RECEIVESTOREAUTOACCEPTVALIDATIONMESSAGE_NAMESTR)
    public String receiveStoreAutoAcceptValidationMessage;
    public String getReceiveStoreAutoAcceptValidationMessageStr() {return this.receiveStoreAutoAcceptValidationMessage;}

    @ColumnInfo(name= cDatabase.RECEIVENOEXTRABINS_NAMESTR)
    public String receiveNoExtraBins;
    public String getReceiveNoExtraBinsStr() {return this.receiveNoExtraBins;}

    @ColumnInfo(name= cDatabase.RECEIVENOEXTRAITEMS_NAMESTR)
    public String receiveNoExtraItems;
    public String getReceiveNoExtraItemsStr() {return this.receiveNoExtraItems;}

    @ColumnInfo(name= cDatabase.RECEIVENOEXTRAPIECES_NAMESTR)
    public String receiveNoExtraPieces;
    public String getReceiveNoExtraPiecesStr() {return this.receiveNoExtraPieces;}

    @ColumnInfo(name= cDatabase.RECEIVEMATAUTOSPLITINCOMPLETELINE_NAMESTR)
    public String receiveMatAutoSplitIncompleteLine;
    public String getReceiveMatAutoSplitIncompleteLineStr() {return this.receiveMatAutoSplitIncompleteLine;}

    @ColumnInfo(name= cDatabase.SOURCEDOCUMENT_NAMESTR)
    public int sourceDocument;
    public int getSourceDocumentInt() {return this.sourceDocument;}

    @ColumnInfo(name= cDatabase.DOCUMENT_NAMESTR)
    public String document;
    public String getDocumentStr() {return this.document;}

    @ColumnInfo(name= cDatabase.DOCUMENT2_NAMESTR)
    public String document2;
    public String getDocument2Str() {return this.document2;}

    @ColumnInfo(name= cDatabase.CONTAINER_NAMESTR)
    public String container;
    public String getContainerStr() {return this.container;}

    @ColumnInfo(name= cDatabase.CURRENTLOCATION_NAMESTR)
    public String currentLocation;
    public String getCurrentLocationStr() {return this.currentLocation;}

    @ColumnInfo(name= cDatabase.ORIGINNO_NAMESTR)
    public String originNo;
    public String getOriginNoStr() {return this.originNo;}

    @ColumnInfo(name= cDatabase.RECEIVEDDATETIME_NAMESTR)
    public String receivedDateTime;
    public String getReceivedDateTime() {return this.receivedDateTime;}

    @ColumnInfo(name= cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR)
    public String webservicetimeouterpins;
    public String getWebserviceTimeOutERPInsStr() {return this.webservicetimeouterpins;}

    @ColumnInfo(name= cDatabase.INTERFACERESULTMETHOD_NAMESTR)
    public String interfaceresultmethod;
    public String getInterfaceResultMethodStr() {return this.interfaceresultmethod;}

    @ColumnInfo(name= cDatabase.SORTING_NAMESTR)
    public int sorting;
    public int getSortingInt() {return this.sorting;}

    @ColumnInfo(name="IsProcessingOrParked")
    public Boolean isprocessingorparked;
    public Boolean getIsProcessingOrParkedStr() {return this.isprocessingorparked;}

    @ColumnInfo(name="Receive_with_picture")
    public String receiveWithPicture;
    public String getReceiveWithPictureStr() {return this.receiveWithPicture;}

    @ColumnInfo(name="Receive_with_picture_auto_open")
    public String receiveWithPictureAutoOpen;
    public String getReceiveWithPictureAutoOpenStr() {return this.receiveWithPictureAutoOpen;}

    @ColumnInfo(name="Receive_with_picture_prefetch")
    public String receiveWithPicturePrefetch;
    public String getReceiveWithPicturePrefetchStr() {return this.receiveWithPicturePrefetch;}

    @ColumnInfo(name="Priority")
    public int priorityInt;
    public int getPriorityInt() {return this.priorityInt;}

    //End Region Public Properties

    //Region Constructor
    public cIntakeorderEntity() {

    }

    public cIntakeorderEntity(JSONObject pvJsonObject) {
        try {
            this.ordernumber = pvJsonObject.getString(cDatabase.ORDERNUMBER_NAMESTR);
            this.ordertype = pvJsonObject.getString(cDatabase.ORDERTYPE_NAMESTR);
            this.numberofBins = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.NUMBEROFBINS_NAMESTR));
            this.assignedUserId = pvJsonObject.getString(cDatabase.ASSIGNEDUSERID_NAMESTR);
            this.currentUserId = pvJsonObject.getString(cDatabase.CURRENTUSERID_NAMESTR);
            this.requestedStartDateTime = pvJsonObject.getString(cDatabase.REQUESTEDSTARTDATETIME_NAMESTR);
            this.requestedEndDateTime = pvJsonObject.getString(cDatabase.REQUESTEDENDDATETIME_NAMESTR);
            this.status = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.STATUS_NAMESTR));
            this.binCode = pvJsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.externalReference = pvJsonObject.getString(cDatabase.EXTERNALREFERENCE_NAMESTR);
            this.workplace = pvJsonObject.getString(cDatabase.WORKPLACE_NAMESTR);
            this.stockOwner = pvJsonObject.getString(cDatabase.STOCKOWNER_NAMESTR);

            this.receiveExportPart = pvJsonObject.getString(cDatabase.RECEIVEEXPORTPART_NAMESTR);
            this.receiveAmountManualEO = pvJsonObject.getString(cDatabase.RECEIVEAMOUNTMANUAL_EO_NAMESTR);
            this.receiveAmountManualOM = pvJsonObject.getString(cDatabase.RECEIVEAMOUNTMANUAL_OM_NAMESTR);
            this.receiveAmountManualMA = pvJsonObject.getString(cDatabase.RECEIVEAMOUNTMANUAL_MA_NAMESTR);
            this.receiveBarcodeCheck = pvJsonObject.getString(cDatabase.RECEIVEBARCODECHECK_NAMESTR);
            this.receiveStoreAutoAcceptAtRequested = pvJsonObject.getString(cDatabase.RECEIVESTOREAUTOACCEPTATREQUESTED_NAMESTR);
            this.receiveStoreAutoAcceptValidationMessage = pvJsonObject.getString(cDatabase.RECEIVESTOREAUTOACCEPTVALIDATIONMESSAGE_NAMESTR);
            this.receiveNoExtraBins = pvJsonObject.getString(cDatabase.RECEIVENOEXTRABINS_NAMESTR);
            this.receiveNoExtraItems = pvJsonObject.getString(cDatabase.RECEIVENOEXTRAITEMS_NAMESTR);
            this.receiveNoExtraPieces = pvJsonObject.getString(cDatabase.RECEIVENOEXTRAPIECES_NAMESTR);
            this.receiveMatAutoSplitIncompleteLine = pvJsonObject.getString(cDatabase.RECEIVEMATAUTOSPLITINCOMPLETELINE_NAMESTR);

            this.sourceDocument = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR)) ;
            this.document = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            this.document2 = pvJsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);
            this.container = pvJsonObject.getString(cDatabase.CONTAINER_NAMESTR);
            this.currentLocation = pvJsonObject.getString(cDatabase.CURRENTLOCATION_NAMESTR);
            this.originNo = pvJsonObject.getString(cDatabase.ORIGINNO_NAMESTR);
            this.receivedDateTime = pvJsonObject.getString(cDatabase.RECEIVEDDATETIME_NAMESTR);
            this.webservicetimeouterpins = pvJsonObject.getString(cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR);
            this.interfaceresultmethod = pvJsonObject.getString(cDatabase.INTERFACERESULTMETHOD_NAMESTR);
            this.sorting = cText.pStringToIntegerInt(pvJsonObject.getString(cDatabase.SORTING_NAMESTR));

            //todo: do this the correct way
//            this.receiveWithPicture = pvJsonObject.getString(pvJsonObject.getString(cDatabase.RECEIVEWITHPICTURE_NAMESTR));
//            this.receiveWithPictureAutoOpen = pvJsonObject.getString(pvJsonObject.getString(cDatabase.RECEIVEWITHPICTURE_AUTO_OPEN_NAMESTR));
//            this.receiveWithPicturePrefetch = pvJsonObject.getString(pvJsonObject.getString(cDatabase.RECEIVEWITHPICTURE_PREFETCH_NAMESTR));

            this.receiveWithPicture = "false";
            this.receiveWithPictureAutoOpen ="false";
            this.receiveWithPicturePrefetch = "false";


            //Is processing

            this.isprocessingorparked = !cText.pIntToStringStr(this.status).equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowReceiveStoreStepEnu.Receive_Store));


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

            if (!this.assignedUserId.equalsIgnoreCase(cUser.currentUser.getNameStr())) {
                this.priorityInt = 5;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor

}
