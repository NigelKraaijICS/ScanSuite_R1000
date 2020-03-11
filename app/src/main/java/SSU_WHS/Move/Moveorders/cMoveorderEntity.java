package SSU_WHS.Move.MoveOrders;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_MOVEORDER)
public class cMoveorderEntity {

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

    @ColumnInfo(name= cDatabase.BINCODE_NAMESTR)
    public String bincode;
    public String getBincodeStr() {return this.externalReference;}

    @ColumnInfo(name= cDatabase.EXTERNALREFERENCE_NAMESTR)
    public String externalReference;
    public String getExternalReferenceStr() {return this.bincode;}

    @ColumnInfo(name= cDatabase.WORKPLACE_NAMESTR)
    public String workplace;
    public String getWorkplaceStr() {return this.workplace;}

    @ColumnInfo(name= cDatabase.STOCKOWNER_NAMESTR)
    public String stockOwner;
    public String getStockOwnerStr() {return this.stockOwner;}

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

    @ColumnInfo(name= cDatabase.MOVE_MI_BATCHTAKEREQUIRED_NAMESTR)
    public String moveMiBatchTakeRequired;
    public String getMoveMiBatchTakeRequiredStr() {return this.moveMiBatchTakeRequired;}

    @ColumnInfo(name= cDatabase.MOVE_MO_BATCHPLACEREQUIRED_NAMESTR)
    public String moveMoBatchPlaceRequired;
    public String getMoveMoBatchPlaceRequiredStr() {return this.moveMoBatchPlaceRequired;}

    @ColumnInfo(name= cDatabase.MOVE_MT_TAKEAMOUNTMANUAL_NAMESTR)
    public String moveMtTakeAmountManual;
    public String getMoveMtTakeAmountManualStr() {return this.moveMtTakeAmountManual;}

    @ColumnInfo(name= cDatabase.MOVE_MT_TAKEAUTOITEM_NAMESTR)
    public String moveMtTakeAutoItem;
    public String getMoveMtTakeAutoItemStr() {return this.moveMtTakeAutoItem;}

    @ColumnInfo(name= cDatabase.MOVE_MT_TAKEAUTOITEMCONTAINERONCE_NAMESTR)
    public String moveMtTakeAutoItemContainerOnce;
    public String getMoveMtTakeAutoItemContainerOnceStr() {return this.moveMtTakeAutoItemContainerOnce;}

    @ColumnInfo(name= cDatabase.MOVE_MT_TAKEALLOWEND_NAMESTR)
    public String moveMtTakeAllowEnd;
    public String getMoveMtTakeAllowEndStr() {return this.moveMtTakeAllowEnd;}

    @ColumnInfo(name= cDatabase.MOVE_MT_TAKEIGNOREUNDERTAKE_NAMESTR)
    public String moveMtTakeIgnoreUndertake;
    public String getMoveMtTakeIgnoreUndertakeStr() {return this.moveMtTakeIgnoreUndertake;}

    @ColumnInfo(name= cDatabase.MOVE_MT_TAKEAUTOACCEPTSINGLEPIECE_NAMESTR)
    public String moveMtTakeAutoAcceptSinglePiece;
    public String getMoveMtTakeAutoAcceptSinglePieceStr() {return this.moveMtTakeAutoAcceptSinglePiece;}

    @ColumnInfo(name= cDatabase.MOVE_MT_PLACEAMOUNTMANUAL_NAMESTR)
    public String moveMtPlaceAmountManual;
    public String getMoveMtPlaceAmountManualStr() {return this.moveMtPlaceAmountManual;}

    @ColumnInfo(name= cDatabase.MOVE_MT_PLACEAUTOITEM_NAMESTR)
    public String moveMtPlaceAutoItem;
    public String getMoveMtPlaceAutoItemStr() {return this.moveMtPlaceAutoItem;}

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

    @ColumnInfo(name= cDatabase.SOURCEDOCUMENT_NAMESTR)
    public String sourceDocument;
    public String getSourceDocumentStr() {return this.sourceDocument;}

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

    @ColumnInfo(name= cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR)
    public String webservicetimeouterpins;
    public String getWebserviceTimeOutERPInsStr() {return this.webservicetimeouterpins;}

    @ColumnInfo(name= cDatabase.INTERFACERESULTMETHOD_NAMESTR)
    public String interfaceresultmethod;
    public String getInterfaceResultMethodStr() {return this.interfaceresultmethod;}

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
            this.workplace = pvJsonObject.getString(cDatabase.WORKPLACE_NAMESTR);
            this.stockOwner = pvJsonObject.getString(cDatabase.STOCKOWNER_NAMESTR);

            this.moveAmountManual = pvJsonObject.getString(cDatabase.MOVEAMOUNTMANUAL_NAMESTR);
            this.moveBarcodeCheck = pvJsonObject.getString(cDatabase.MOVEBARCODECHECK_NAMESTR);
            this.moveValidateStock = pvJsonObject.getString(cDatabase.MOVEVALIDATESTOCK_NAMESTR);
            this.moveValidateStockEnforce = pvJsonObject.getString(cDatabase.MOVEVALIDATESTOCKENFORCE_NAMESTR);
            this.moveMiBatchTakeRequired = pvJsonObject.getString(cDatabase.MOVE_MI_BATCHTAKEREQUIRED_NAMESTR);
            this.moveMoBatchPlaceRequired = pvJsonObject.getString(cDatabase.MOVE_MO_BATCHPLACEREQUIRED_NAMESTR);
            this.moveMtTakeAmountManual = pvJsonObject.getString(cDatabase.MOVE_MT_TAKEAMOUNTMANUAL_NAMESTR);
            this.moveMtTakeAutoItem = pvJsonObject.getString(cDatabase.MOVE_MT_TAKEAUTOITEM_NAMESTR);
            this.moveMtTakeAutoItemContainerOnce = pvJsonObject.getString(cDatabase.MOVE_MT_TAKEAUTOITEMCONTAINERONCE_NAMESTR);
            this.moveMtTakeAllowEnd = pvJsonObject.getString(cDatabase.MOVE_MT_TAKEALLOWEND_NAMESTR);
            this.moveMtTakeIgnoreUndertake = pvJsonObject.getString(cDatabase.MOVE_MT_TAKEIGNOREUNDERTAKE_NAMESTR);
            this.moveMtTakeAutoAcceptSinglePiece = pvJsonObject.getString(cDatabase.MOVE_MT_TAKEAUTOACCEPTSINGLEPIECE_NAMESTR);
            this.moveMtPlaceAmountManual = pvJsonObject.getString(cDatabase.MOVE_MT_PLACEAMOUNTMANUAL_NAMESTR);
            this.moveMtPlaceAutoItem = pvJsonObject.getString(cDatabase.MOVE_MT_PLACEAUTOITEM_NAMESTR);
            this.moveAutoAcceptAtRequested = pvJsonObject.getString(cDatabase.MOVE_AUTOACCEPTATREQUESTED_NAMESTR);
            this.moveNoExtraBins = pvJsonObject.getString(cDatabase.MOVE_NOEXTRABINS_NAMESTR);
            this.moveNoExtraItems = pvJsonObject.getString(cDatabase.MOVE_NOEXTRAITEMS_NAMESTR);
            this.moveNoExtraPieces = pvJsonObject.getString(cDatabase.MOVE_NOEXTRAPIECES_NAMESTR);

            this.sourceDocument = pvJsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR);
            this.document = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            this.document2 = pvJsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);
            this.container = pvJsonObject.getString(cDatabase.CONTAINER_NAMESTR);
            this.currentLocation = pvJsonObject.getString(cDatabase.CURRENTLOCATION_NAMESTR);

            this.webservicetimeouterpins = pvJsonObject.getString(cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR);
            this.interfaceresultmethod = pvJsonObject.getString(cDatabase.INTERFACERESULTMETHOD_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor

}


