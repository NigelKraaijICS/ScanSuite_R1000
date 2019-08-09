package SSU_WHS.Pickorders;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.cDatabase;

@Entity(tableName="Pickorders")
public class cPickorderEntity {
    @PrimaryKey @NonNull
    @ColumnInfo(name="Ordernumber")
    public String ordernumber;
    @ColumnInfo(name="Ordertype")
    public String ordertype;
    @ColumnInfo(name="Number_of_Bins")
    public String numberofBins;
    @ColumnInfo(name="QuantityTotal")
    public String quantitytotal;
    @ColumnInfo(name="SingleArticleOrders")
    public String singleArticleOrders;
    @ColumnInfo(name="Pick_Reject_during_pick")
    public String pickRejectduringpick;
    @ColumnInfo(name="Pick_Reject_during_sort")
    public String pickRejectduringsort;
    @ColumnInfo(name="Pick_Sales_ask_workplace")
    public String pickSalesaskworkplace;
    @ColumnInfo(name="Pick_Transfer_ask_workplace")
    public String pickTransferaskworkplace;
    @ColumnInfo(name="Pick_Barcode_check")
    public String pickBarcodecheck;
    @ColumnInfo(name="Pick_Pick_PV_VKO_each_piece")
    public String pickPickPVVKOeachpiece;
    @ColumnInfo(name="Pick_Pick_to_container")
    public String pickPicktocontainer;
    @ColumnInfo(name="Pick_Pick_to_container_type")
    public String pickPicktocontainertype;
    @ColumnInfo(name="Pick_Print_addresslabel")
    public String pickPrintaddresslabel;
    @ColumnInfo(name="Pick_Print_contentlabel")
    public String pickPrintcontentlabel;
    @ColumnInfo(name="AssignedUserId")
    public String assignedUserId;
    @ColumnInfo(name="CurrentUserId")
    public String currentUserId;
    @ColumnInfo(name="Status")
    public String status;
    @ColumnInfo(name="StatusPrintAtStart")
    public String statusPrintAtStart;
    @ColumnInfo(name="ExternalReference")
    public String externalReference;
    @ColumnInfo(name="Workplace")
    public String workplace;
    @ColumnInfo(name="StockOwner")
    public String stockOwner;
    @ColumnInfo(name="RequestedEndDateTime")
    public String requestedEndDateTime;
    @ColumnInfo(name="SourceDocument")
    public String sourceDocument;
    @ColumnInfo(name="Document")
    public String document;
    @ColumnInfo(name="DocumentType")
    public String documentType;
    @ColumnInfo(name="Document2")
    public String document2;
    @ColumnInfo(name="DocumentType2")
    public String documentType2;
    @ColumnInfo(name="Webservice_timeout_erp_in_s")
    public String webservicetimeouterpins;
    @ColumnInfo(name="Currentlocation")
    public String currentlocation;
    @ColumnInfo(name="Interface_result_method")
    public String interfaceresultmethod;
    @ColumnInfo(name="Sorting")
    public String sorting;
    @ColumnInfo(name="IsProcessingOrParked")
    public Boolean isprocessingorparked;


    //empty constructor
    public cPickorderEntity() {

    }

    public cPickorderEntity(JSONObject jsonObject, Boolean inProgress) {
        try {
            ordernumber = jsonObject.getString(cDatabase.ORDERNUMBER_NAMESTR);
            ordertype = jsonObject.getString(cDatabase.ORDERTYPE_NAMESTR);
            numberofBins = jsonObject.getString(cDatabase.NUMBEROFBINS_NAMESTR);
            quantitytotal = jsonObject.getString(cDatabase.QUANTITYTOTAL_NAMESTR);
            singleArticleOrders = jsonObject.getString(cDatabase.SINGLEARTICLEORDERS_NAMESTR);
            pickRejectduringpick = jsonObject.getString(cDatabase.PICKREJECTDURINGPICK_NAMESTR);
            pickRejectduringsort = jsonObject.getString(cDatabase.PICKREJECTDURINGSORT_NAMESTR);
            pickSalesaskworkplace = jsonObject.getString(cDatabase.PICKSALESASKWORKPLACE_NAMESTR);
            pickTransferaskworkplace = jsonObject.getString(cDatabase.PICKTRANSFERASKWORKPLACE_NAMESTR);
            pickBarcodecheck = jsonObject.getString(cDatabase.PICKBARCODECHECK_NAMESTR);
            pickPickPVVKOeachpiece = jsonObject.getString(cDatabase.PICKPICKPVVKKOEACHPIECE_NAMESTR);
            pickPicktocontainer = jsonObject.getString(cDatabase.PICKPICKTOCONTAINER_NAMESTR);
            pickPicktocontainertype = jsonObject.getString(cDatabase.PICKPICKTOCONTAINERTYPE_NAMESTR);
            pickPrintaddresslabel = jsonObject.getString(cDatabase.PICKPRINTADDRESSLABEL_NAMESTR);
            pickPrintcontentlabel = jsonObject.getString(cDatabase.PICKPRINTCONTENTLABEL_NAMESTR);
            assignedUserId = jsonObject.getString(cDatabase.ASSIGNEDUSERID_NAMESTR);
            currentUserId = jsonObject.getString(cDatabase.CURRENTUSERID_NAMESTR);
            status = jsonObject.getString(cDatabase.STATUS_NAMESTR);
            statusPrintAtStart = jsonObject.getString(cDatabase.STATUSPRINTATSTART_NAMESTR);
            externalReference = jsonObject.getString(cDatabase.EXTERNALREFERENCE_NAMESTR);
            workplace = jsonObject.getString(cDatabase.WORKPLACE_NAMESTR);
            stockOwner = jsonObject.getString(cDatabase.STOCKOWNER_NAMESTR);
            requestedEndDateTime = jsonObject.getString(cDatabase.REQUESTENDDATETIME_NAMESTR);
            sourceDocument = jsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR);
            document = jsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            documentType = jsonObject.getString(cDatabase.DOCUMENTTYPE_NAMESTR);
            document2 = jsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);
            documentType2 = jsonObject.getString(cDatabase.DOCUMENTTYPE2_NAMESTR);
            currentlocation = jsonObject.getString(cDatabase.CURRENTLOCATION_NAMESTR);
            webservicetimeouterpins = jsonObject.getString(cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR);
            interfaceresultmethod = jsonObject.getString(cDatabase.INTERFACERESULTMETHOD_NAMESTR);
            sorting = jsonObject.getString(cDatabase.SORTING_NAMESTR);
            isprocessingorparked = inProgress;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getOrdernumber() {return this.ordernumber;}
    public String getOrdertype() {return this.ordertype;}
    public String getNumberofBins() {return this.numberofBins;}
    public String getQuantitytotal() {return this.quantitytotal;}
    public String getSingleArticleOrders() {return this.singleArticleOrders;}
    public String getPickRejectduringpick() {return this.pickRejectduringpick;}
    public String getPickRejectduringsort() {return this.pickRejectduringsort;}
    public String getPickSalesaskworkplace() {return this.pickSalesaskworkplace;}
    public String getPickTransferaskworkplace() {return this.pickTransferaskworkplace;}
    public String getPickBarcodecheck() {return this.pickBarcodecheck;}
    public String getPickPickPVVKOeachpiece() {return this.pickPickPVVKOeachpiece;}
    public String getPickPicktocontainer() {return this.pickPicktocontainer;}
    public String getPickPicktocontainertype() {return this.pickPicktocontainertype;}
    public String getPickPrintaddresslabel() {return this.pickPrintaddresslabel;}
    public String getPickPrintcontentlabel() {return this.pickPrintcontentlabel;}
    public String getAssignedUserId() {return this.assignedUserId;}
    public String getCurrentUserId() {return this.currentUserId;}
    public String getStatus() {return this.status;}
    public String getStatusPrintAtStart() {return this.statusPrintAtStart;}
    public String getExternalReference() {return this.externalReference;}
    public String getWorkplace() {return this.workplace;}
    public String getStockOwner() {return this.stockOwner;}
    public String getRequestedEndDateTime() {return this.requestedEndDateTime;}
    public String getSourceDocument() {return this.sourceDocument;}
    public String getDocument() {return this.document;}
    public String getDocumentType() {return this.documentType;}
    public String getDocument2() {return this.document2;}
    public String getDocumentType2() {return this.documentType2;}
    public String getCurrentlocation() {return this.currentlocation;}
    public String getWebservicetimeouterpins() {return this.webservicetimeouterpins;}
    public String getInterfaceresultmethod() {return this.interfaceresultmethod;}
    public String getSorting() {return this.sorting;}
    public Boolean getIsprocessingorparked() {return this.isprocessingorparked;}
}


