package SSU_WHS.Picken.Pickorders;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cDatabase;

@Entity(tableName="Pickorders")
public class cPickorderEntity {

    //Region Public Properties
    @PrimaryKey @NonNull
    @ColumnInfo(name="Ordernumber")
    public String ordernumber = "";
    public String getOrdernumberStr() {return this.ordernumber;}

    @ColumnInfo(name="Ordertype")
    public String ordertype;
    public String getOrderTypeStr() {return this.ordertype;}

    @ColumnInfo(name="Number_of_Bins")
    public String numberofBins;
    public String getNumberofBinsStr() {return this.numberofBins;}

    @ColumnInfo(name="QuantityTotal")
    public String quantitytotal;
    public String getQuantityTotalStr() {return this.quantitytotal;}

    @ColumnInfo(name="SingleArticleOrders")
    public String singleArticleOrders;
    public String getSingleArticleOrdersStr() {return this.singleArticleOrders;}

    @ColumnInfo(name="Pick_Reject_during_pick")
    public String pickRejectduringpick;
    public String getPickRejectDuringpickStr() {return this.pickRejectduringpick;}

    @ColumnInfo(name="Pick_Reject_during_sort")
    public String pickRejectduringsort;
    public String getPickRejectDuringSortStr() {return this.pickRejectduringsort;}

    @ColumnInfo(name="Pick_Sales_ask_workplace")
    public String pickSalesaskworkplace;
    public String getPickSalesAskWorkplaceStr() {return this.pickSalesaskworkplace;}

    @ColumnInfo(name="Pick_Transfer_ask_workplace")
    public String pickTransferaskworkplace;
    public String getPickTransferAskWorkplaceStr() {return this.pickTransferaskworkplace;}

    @ColumnInfo(name="Pick_Barcode_check")
    public String pickBarcodecheck;
    public String getPickBarcodeCheckStr() {return this.pickBarcodecheck;}

    @ColumnInfo(name="Pick_Pick_PV_VKO_each_piece")
    public String pickPickPVVKOeachpiece;
    public String getPickPickPVVKOEachPieceStr() {return this.pickPickPVVKOeachpiece;}

    @ColumnInfo(name="Pick_Pick_to_container")
    public String pickPicktocontainer;
    public String getPickPickToContainerStr() {return this.pickPicktocontainer;}

    @ColumnInfo(name="Pick_Pick_to_container_type")
    public String pickPicktocontainertype;
    public String getPickPickToContainerTypeStr() {return this.pickPicktocontainertype;}

    @ColumnInfo(name="Pick_Print_addresslabel")
    public String pickPrintaddresslabel;
    public String getPickPrintAddresslabelStr() {return this.pickPrintaddresslabel;}

    @ColumnInfo(name="Pick_Print_contentlabel")
    public String pickPrintcontentlabel;
    public String getPickPrintContentLabelStr() {return this.pickPrintcontentlabel;}

    @ColumnInfo(name="Pick_with_picture")
    public String pickWithPicture;
    public String getpickWithPictureStr() {return this.pickWithPicture;}

    @ColumnInfo(name="Pick_with_picture_auto_open")
    public String pickWithPictureAutoOpen;
    public String getPickWithPictureAutoOpenStr() {return this.pickWithPictureAutoOpen;}

    @ColumnInfo(name="Pick_with_picture_prefetch")
    public String pickWithPicturePrefetch;
    public String getPickWithPicturePrefetchStr() {return this.pickWithPicturePrefetch;}

    @ColumnInfo(name="Pick_activity_bin_required")
    public String pickActivityBinRequired;
    public String getPickActivityBinRequired() {return this.pickActivityBinRequired;}

    @ColumnInfo(name="AssignedUserId")
    public String assignedUserId;
    public String getAssignedUserIdStr() {return this.assignedUserId;}

    @ColumnInfo(name="CurrentUserId")
    public String currentUserId;
    public String getCurrentUserIdStr() {return this.currentUserId;}

    @ColumnInfo(name="Status")
    public String status;
    public String getStatusStr() {return this.status;}

    @ColumnInfo(name="StatusPrintAtStart")
    public String statusPrintAtStart;
    public String getStatusPrintAtStartStr() {return this.statusPrintAtStart;}

    @ColumnInfo(name="ExternalReference")
    public String externalReference;
    public String getExternalReferenceStr() {return this.externalReference;}

    @ColumnInfo(name="Workplace")
    public String workplace;
    public String getWorkplaceStr() {return this.workplace;}

    @ColumnInfo(name="StockOwner")
    public String stockOwner;
    public String getStockOwnerStr() {return this.stockOwner;}

    @ColumnInfo(name="RequestedEndDateTime")
    public String requestedEndDateTime;
    public String getRequestedEndDateTimeStr() {return this.requestedEndDateTime;}

    @ColumnInfo(name="SourceDocument")
    public String sourceDocument;
    public String getSourceDocumentStr() {return this.sourceDocument;}

    @ColumnInfo(name="Document")
    public String document;
    public String getDocumentStr() {return this.document;}

    @ColumnInfo(name="DocumentType")
    public String documentType;
    public String getDocumentTypeStr() {return this.documentType;}

    @ColumnInfo(name="Document2")
    public String document2;
    public String getDocument2Str() {return this.document2;}

    @ColumnInfo(name="DocumentType2")
    public String documentType2;
    public String getDocumentType2Str() {return this.documentType2;}

    @ColumnInfo(name="Webservice_timeout_erp_in_s")
    public String webservicetimeouterpins;
    public String getWebserviceTimeOutERPInsStr() {return this.webservicetimeouterpins;}

    @ColumnInfo(name="Currentlocation")
    public String currentlocation;
    public String getCurrentLocationStr() {return this.currentlocation;}

    @ColumnInfo(name="Interface_result_method")
    public String interfaceresultmethod;
    public String getInterfaceResultMethodStr() {return this.interfaceresultmethod;}

    @ColumnInfo(name="Sorting")
    public String sorting;
    public String getSortingStr() {return this.sorting;}

    @ColumnInfo(name="IsProcessingOrParked")
    public Boolean isprocessingorparked;
    public Boolean getIsProcessingOrParkedStr() {return this.isprocessingorparked;}

    @ColumnInfo(name="Priority")
    public int priorityInt;
    public int getPriorityInt() {return this.priorityInt;}

    //End Region Public Properties

    //Region Constructor
    public cPickorderEntity() {

    }

    public cPickorderEntity(JSONObject pvJsonObject) {
        try {
            this.ordernumber = pvJsonObject.getString(cDatabase.ORDERNUMBER_NAMESTR);
            this.ordertype = pvJsonObject.getString(cDatabase.ORDERTYPE_NAMESTR);
            this.numberofBins = pvJsonObject.getString(cDatabase.NUMBEROFBINS_NAMESTR);
            this.quantitytotal = pvJsonObject.getString(cDatabase.QUANTITYTOTAL_NAMESTR);
            this.singleArticleOrders = pvJsonObject.getString(cDatabase.SINGLEARTICLEORDERS_NAMESTR);

            this.assignedUserId = pvJsonObject.getString(cDatabase.ASSIGNEDUSERID_NAMESTR);
            this.currentUserId = pvJsonObject.getString(cDatabase.CURRENTUSERID_NAMESTR);
            this.status = pvJsonObject.getString(cDatabase.STATUS_NAMESTR);
            this.statusPrintAtStart = pvJsonObject.getString(cDatabase.STATUSPRINTATSTART_NAMESTR);
            this.externalReference = pvJsonObject.getString(cDatabase.EXTERNALREFERENCE_NAMESTR);
            this.workplace = pvJsonObject.getString(cDatabase.WORKPLACE_NAMESTR);
            this.stockOwner = pvJsonObject.getString(cDatabase.STOCKOWNER_NAMESTR);
            this.requestedEndDateTime = pvJsonObject.getString(cDatabase.REQUESTENDDATETIME_NAMESTR);
            this.sourceDocument = pvJsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR);
            this.document = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            this.documentType = pvJsonObject.getString(cDatabase.DOCUMENTTYPE_NAMESTR);
            this.document2 = pvJsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);
            this.documentType2 = pvJsonObject.getString(cDatabase.DOCUMENTTYPE2_NAMESTR);
            this.currentlocation = pvJsonObject.getString(cDatabase.CURRENTLOCATION_NAMESTR);
            this.webservicetimeouterpins = pvJsonObject.getString(cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR);
            this.interfaceresultmethod = pvJsonObject.getString(cDatabase.INTERFACERESULTMETHOD_NAMESTR);
            this.sorting = pvJsonObject.getString(cDatabase.SORTING_NAMESTR);

            //Is processing

            this.isprocessingorparked = !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowPickStepEnu.PickPicking)) &&
                    !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowPickStepEnu.PickSorting)) &&
                    !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip));

            //Settings
            this.pickRejectduringpick = pvJsonObject.getString(cDatabase.PICKREJECTDURINGPICK_NAMESTR);
            this.pickRejectduringsort = pvJsonObject.getString(cDatabase.PICKREJECTDURINGSORT_NAMESTR);
            this.pickSalesaskworkplace = pvJsonObject.getString(cDatabase.PICKSALESASKWORKPLACE_NAMESTR);
            this.pickTransferaskworkplace = pvJsonObject.getString(cDatabase.PICKTRANSFERASKWORKPLACE_NAMESTR);
            this.pickBarcodecheck = pvJsonObject.getString(cDatabase.PICKBARCODECHECK_NAMESTR);
            this.pickPickPVVKOeachpiece = pvJsonObject.getString(cDatabase.PICKPICKPVVKKOEACHPIECE_NAMESTR);
            this.pickPicktocontainer = pvJsonObject.getString(cDatabase.PICKPICKTOCONTAINER_NAMESTR);
            this.pickPicktocontainertype = pvJsonObject.getString(cDatabase.PICKPICKTOCONTAINERTYPE_NAMESTR);
            this.pickPrintaddresslabel = pvJsonObject.getString(cDatabase.PICKPRINTADDRESSLABEL_NAMESTR);
            this.pickPrintcontentlabel = pvJsonObject.getString(cDatabase.PICKPRINTCONTENTLABEL_NAMESTR);
            this.pickWithPicture = pvJsonObject.getString(cDatabase.PICKWITHPICTURE_NAMESTR);
            this.pickWithPictureAutoOpen = pvJsonObject.getString(cDatabase.PICKWITHPICTURE_AUTO_OPEN_NAMESTR);
            this.pickWithPicturePrefetch = pvJsonObject.getString(cDatabase.PICKWITHPICTURE_PREFETCH_NAMESTR);
            this.pickActivityBinRequired = pvJsonObject.getString(cDatabase.PICKACTIVITYBINREQUIRED_NAMESTR);

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


