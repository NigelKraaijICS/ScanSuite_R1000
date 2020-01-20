package SSU_WHS.Return.ReturnOrder;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
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

    @ColumnInfo(name= cDatabase.EXTERNALREFERENCE_NAMESTR)
    public String externalReferenceStr;
    public String getExternalReferenceStr() {return this.externalReferenceStr;}

    @ColumnInfo(name= cDatabase.STOCKOWNER_NAMESTR)
    public String stockOwnerStr;
    public String getStockOwnerStr() {return this.stockOwnerStr;}

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

    @ColumnInfo(name = cDatabase.RECEIVEDDATETIME_NAMESTR)
    public String receivedDateTimeStr;
    public String getReceivedDateTimeDat() {return this.receivedDateTimeStr;}

    @ColumnInfo(name= cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR)
    public String webserviceTimeOuteERPinSStr;
    public String getWebserviceTimeOutERPInsStr() {return this.webserviceTimeOuteERPinSStr;}

    @ColumnInfo(name= cDatabase.INTERFACERESULTMETHOD_NAMESTR)
    public String interfaceResultMethodStr;
    public String getInterfaceResultMethodStr() {return this.interfaceResultMethodStr;}

    @ColumnInfo(name=cDatabase.SORTING_NAMESTR)
    public String sorteringStr;
    public String getSorteringStr() {return this.sorteringStr;}

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
            this.externalReferenceStr = pvJsonObject.getString(cDatabase.EXTERNALREFERENCE_NAMESTR);
            this.stockOwnerStr = pvJsonObject.getString(cDatabase.STOCKOWNER_NAMESTR);
            this.retourAmountManualBln = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.RETOURAMAOUNTMANUAL_NAMESTR),false);
            this.retourBarcodeCheckBln = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.RETOURBARCODECHECK_NAMESTR),false);
            this.retourMultiDocumentBln = cText.pStringToBooleanBln(pvJsonObject.getString(cDatabase.RETOURMULTIDOCUMENT_NAMESTR),false);
            this.sourceDocumentStr = pvJsonObject.getString(cDatabase.SOURCEDOCUMENT_NAMESTR);
            this.documentStr = pvJsonObject.getString(cDatabase.DOCUMENT_NAMESTR);
            this.document2Str = pvJsonObject.getString(cDatabase.DOCUMENT2_NAMESTR);
            this.reasonStr = pvJsonObject.getString(cDatabase.REASON_NAMESTR);
            this.receivedDateTimeStr = pvJsonObject.getString(cDatabase.RECEIVEDDATETIME_NAMESTR);
            this.webserviceTimeOuteERPinSStr = pvJsonObject.getString(cDatabase.WEBSERVICETIMEOUTERPINS_NAMESTR);
            this.interfaceResultMethodStr = pvJsonObject.getString(cDatabase.INTERFACERESULTMETHOD_NAMESTR);
            this.sorteringStr = pvJsonObject.getString(cDatabase.SORTING_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor

}


