package SSU_WHS.Picken.Pickorders;

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

    @ColumnInfo(name="QuantityTotal")
    public String quantitytotal;
    public String getQuantityTotalStr() {return this.quantitytotal;}

    @ColumnInfo(name="SingleArticleOrders")
    public String singleArticleOrders;
    public String getSingleArticleOrdersStr() {return this.singleArticleOrders;}

    @ColumnInfo(name="Pick_Sales_ask_workplace")
    public String pickSalesaskworkplace;
    public String getPickSalesAskWorkplaceStr() {return this.pickSalesaskworkplace;}

    @ColumnInfo(name="Pick_Transfer_ask_workplace")
    public String pickTransferaskworkplace;
    public String getPickTransferAskWorkplaceStr() {return this.pickTransferaskworkplace;}

    @ColumnInfo(name="Pick_Pick_PV_VKO_each_piece")
    public String pickPickPVVKOeachpiece;
    public String getPickPickPVVKOEachPieceStr() {return this.pickPickPVVKOeachpiece;}

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

    @ColumnInfo(name="Pick_auto_next")
    public String pickAutoNext;
    public String getPickAutoNext() {return this.pickAutoNext;}

    @ColumnInfo(name="AssignedUserId")
    public String assignedUserId;
    public String getAssignedUserIdStr() {return this.assignedUserId;}

    @ColumnInfo(name="CurrentUserId")
    public String currentUserId;
    public String getCurrentUserIdStr() {return this.currentUserId;}

    @ColumnInfo(name="Status")
    public String status;
    public String getStatusStr() {return this.status;}

    @ColumnInfo(name="ExternalReference")
    public String externalReference;
    public String getExternalReferenceStr() {return this.externalReference;}

    @ColumnInfo(name="StockOwner")
    public String stockOwner;
    public String getStockOwnerStr() {return this.stockOwner;}

    @ColumnInfo(name="SourceDocument")
    public String sourceDocument;
    public String getSourceDocumentStr() {return this.sourceDocument;}

    @ColumnInfo(name="Document")
    public String document;
    public String getDocumentStr() {return this.document;}

    @ColumnInfo(name="Document2")
    public String document2;
    public String getDocument2Str() {return this.document2;}

    @ColumnInfo(name="Currentlocation")
    public String currentlocation;
    public String getCurrentLocationStr() {return this.currentlocation;}

    @ColumnInfo(name="Interface_result_method")
    public String interfaceresultmethod;
    public String getInterfaceResultMethodStr() {return this.interfaceresultmethod;}

    @ColumnInfo(name="IsProcessingOrParked")
    public Boolean isprocessingorparked;
    public Boolean getIsProcessingOrParkedStr() {return this.isprocessingorparked;}

    @ColumnInfo(name="IsSelected")
    public boolean isSelected;
    public boolean getIsSelected() {return this.isSelected;}

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

            this.quantitytotal = pvJsonObject.getString(cDatabase.QUANTITYTOTAL_NAMESTR);
            this.singleArticleOrders = pvJsonObject.getString(cDatabase.SINGLEARTICLEORDERS_NAMESTR);

            this.assignedUserId = pvJsonObject.getString(cDatabase.ASSIGNEDUSERID_NAMESTR);
            this.currentUserId = pvJsonObject.getString(cDatabase.CURRENTUSERID_NAMESTR);
            this.status = pvJsonObject.getString(cDatabase.STATUS_NAMESTR);

            this.externalReference = pvJsonObject.getString(cDatabase.EXTERNALREFERENCE_NAMESTR);

            this.stockOwner = pvJsonObject.getString(cDatabase.STOCKOWNER_NAMESTR);

            this.sourceDocument = pvJsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR);
            this.document = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);

            this.document2 = pvJsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);

            this.currentlocation = pvJsonObject.getString(cDatabase.CURRENTLOCATION_NAMESTR);

            this.interfaceresultmethod = pvJsonObject.getString(cDatabase.INTERFACERESULTMETHOD_NAMESTR);

            this.isSelected = false;


            //Is processing

            this.isprocessingorparked = !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowPickStepEnu.PickPicking)) &&
                    !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowPickStepEnu.PickSorting)) &&
                    !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowPickStepEnu.PickQualityControl)) &&
                    !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip)) &&
                    !this.status.equalsIgnoreCase(cText.pIntToStringStr(cWarehouseorder.WorkflowPickStepEnu.PickStorage));

            //Settings

            this.pickSalesaskworkplace = pvJsonObject.getString(cDatabase.PICKSALESASKWORKPLACE_NAMESTR);
            this.pickTransferaskworkplace = pvJsonObject.getString(cDatabase.PICKTRANSFERASKWORKPLACE_NAMESTR);

            this.pickPickPVVKOeachpiece = pvJsonObject.getString(cDatabase.PICKPICKPVVKKOEACHPIECE_NAMESTR);

            this.pickWithPicture = pvJsonObject.getString(cDatabase.PICKWITHPICTURE_NAMESTR);
            this.pickWithPictureAutoOpen = pvJsonObject.getString(cDatabase.PICKWITHPICTURE_AUTO_OPEN_NAMESTR);
            this.pickWithPicturePrefetch = pvJsonObject.getString(cDatabase.PICKWITHPICTURE_PREFETCH_NAMESTR);
            this.pickActivityBinRequired = pvJsonObject.getString(cDatabase.PICKACTIVITYBINREQUIRED_NAMESTR);
            this.pickAutoNext = pvJsonObject.getString(cDatabase.PICKAUTONEXT_NAMESTR);

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


