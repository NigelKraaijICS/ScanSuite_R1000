package SSU_WHS.Move.MoveorderLines;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveOrders.cMoveorderViewModel;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLineBarcode.cMoveorderLineBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cMoveorderLine implements Comparable {

    public String actionTypeCodeStr;
    public String getActionTypeCodeStr() { return actionTypeCodeStr; }

    public int lineNoInt;
    public int getLineNoInt() {
        return lineNoInt;
    }

    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public  String getItemNoAndVariantCodeStr(){
        return   this.getItemNoStr() + " " + this.getVariantCodeStr();
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    public String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    public double quantityDbl;
    public double getQuantityDbl() {
        return quantityDbl;
    }

    public double quantityHandledDbl;
    public double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    public double quantityTakenDbl;
    public double getQuantityTakenDbl() {
        return quantityTakenDbl;
    }

    public double quantityPlacedDbl;
    public double getQuantityPlacedDbl() {
        return quantityPlacedDbl;
    }

    public String handledTimeStampStr;
    public String getHandledTimeStampStr() { return handledTimeStampStr; }

    public String sourceNoStr;
    public String getSourceNoStr() { return sourceNoStr; }

    public int statusInt;
    public int getStatusInt() { return statusInt; }

    public int localStatusInt;
    public int getLocalStatusInt() { return localStatusInt; }

    public  int sortingSequenceInt;
    public  int getSortingSequenceInt(){
        return  this.sortingSequenceInt;
    }

    public String extraField1Str;
    public String getExtraField1Str() {
        return extraField1Str;
    }

    public String extraField2Str;
    public String getExtraField2Str() {
        return extraField2Str;
    }

    public String extraField3Str;
    public String getExtraField3Str() {
        return extraField3Str;
    }

    public String extraField4Str;
    public String getExtraField4Str() {
        return extraField4Str;
    }

    public String extraField5Str;
    public String getExtraField5Str() {
        return extraField5Str;
    }

    public String extraField6Str;
    public String getExtraField6Str() {
        return extraField6Str;
    }

    public String extraField7Str;
    public String getExtraField7Str() {
        return extraField7Str;
    }

    public String extraField8Str;
    public String getExtraField8Str() {
        return extraField8Str;
    }

    public  String getKeyStr() {
        return  this.getItemNoStr() + "þ" + this.getVariantCodeStr();
    }

    private cMoveorderLineEntity moveorderLineEntity;

    public static List<cMoveorderLine> allLinesObl;
    public static cMoveorderLine currentMoveOrderLine;

    private cMoveorderLineViewModel getMoveorderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineViewModel.class);
    }

    public List<cMoveorderLineBarcode> lineBarcodeObl(){

        List<cMoveorderLineBarcode> resultObl = new ArrayList<>();

        if (cMoveorderLineBarcode.allMoveorderLineBarcodesObl == null || cMoveorderLineBarcode.allMoveorderLineBarcodesObl.size() == 0) {
            return  resultObl;
        }

        for (cMoveorderLineBarcode moveorderLineBarcode : cMoveorderLineBarcode.allMoveorderLineBarcodesObl ) {

            if (moveorderLineBarcode.getLineNoInt() == this.getLineNoInt()) {
                resultObl.add(moveorderLineBarcode);
            }
        }

        return  resultObl;

    }

    //Region Public Properties

    public cArticleImage articleImage;

    public  List<cMoveorderBarcode> barcodesObl;
    //End Region Public Properties

    //Region Constructor
    public cMoveorderLine(JSONObject pvJsonObject) {
        this.moveorderLineEntity = new cMoveorderLineEntity(pvJsonObject);

        this.lineNoInt = this.moveorderLineEntity.getLineNoInt();
        this.actionTypeCodeStr = this.moveorderLineEntity.getActiontypecodeStr();

        this.itemNoStr = this.moveorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.moveorderLineEntity.getVariantCodeStr();
        this.descriptionStr = this.moveorderLineEntity.getDescriptionStr();
        this.description2Str = this.moveorderLineEntity.getDescription2Str();
        this.binCodeStr = this.moveorderLineEntity.getBincodeStr();

        this.quantityDbl = this.moveorderLineEntity.getQuantityDbl();
        this.quantityHandledDbl = this.moveorderLineEntity.getQuantityhandledDbl();
        this.quantityTakenDbl = this.moveorderLineEntity.getQuantityTakenDbl();
        this.quantityPlacedDbl = this.moveorderLineEntity.getQuantityPlacedDbl();

        this.handledTimeStampStr = this.moveorderLineEntity.getHandledtimestampStr();
        this.sourceNoStr = this.moveorderLineEntity.getSourcenoStr();
        this.statusInt = this.moveorderLineEntity.getStatusInt();
        this.sortingSequenceInt = this.moveorderLineEntity.getSortingSequenceNoInt();

        this.extraField1Str =  this.moveorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.moveorderLineEntity.getExtraField2Str();
        this.extraField3Str =  this.moveorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.moveorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.moveorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.moveorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.moveorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.moveorderLineEntity.getExtraField8Str();
    }

    public cMoveorderLine(String pvItemNoStr, String pvVariantCodeStr) {
        this.moveorderLineEntity = null;

        this.lineNoInt = 0;
        this.actionTypeCodeStr = cWarehouseorder.ActionTypeEnu.PLACE.toString();

        this.itemNoStr = pvItemNoStr;
        this.variantCodeStr = pvVariantCodeStr;
        this.descriptionStr = "";
        this.description2Str = "";
        this.binCodeStr = "";

        this.quantityDbl = 0;
        this.quantityHandledDbl = 0;
        this.quantityTakenDbl = 0;
        this.quantityPlacedDbl = 0;

        this.handledTimeStampStr = "";
        this.sourceNoStr = "";
        this.statusInt = cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_NEW;
        this.sortingSequenceInt = 0;

        this.extraField1Str =  "";
        this.extraField2Str = "";
        this.extraField3Str =  "";
        this.extraField4Str =  "";
        this.extraField5Str =  "";
        this.extraField6Str =  "";
        this.extraField7Str =  "";
        this.extraField8Str =  "";
    }


    //End Region Constructor

    public boolean pInsertInDatabaseBln() {

        if (cMoveorderLine.allLinesObl == null){
            cMoveorderLine.allLinesObl = new ArrayList<>();
        }
        cMoveorderLine.allLinesObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cMoveorderLineViewModel moveorderLineViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineViewModel.class);
        moveorderLineViewModel.deleteAll();
        return true;
    }

    public boolean pGetArticleImageBln(){

        if (this.articleImage != null) {
            return  true;
        }

        this.articleImage = cArticleImage.pGetArticleImageByItemNoAndVariantCode(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.articleImage != null){
            return  true;
        }

        cWebresult Webresult;

        cArticleImageViewModel articleImageViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleImageViewModel.class);
        Webresult = articleImageViewModel.pGetArticleImageFromWebserviceWrs(this.getItemNoStr(),this.getVariantCodeStr());
        if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {
            return  false;
        }

        for (JSONObject jsonObject : Webresult.getResultDtt()) {
            cArticleImage articleImage = new cArticleImage(jsonObject);
            articleImage.pInsertInDatabaseBln();
            this.articleImage = articleImage;
            return true;

        }
        return  false;

    }

    public cResult pResetRst() {

        cResult resultRst = new cResult();
        resultRst.resultBln = true;

        cWebresult WebResult;
        WebResult =  this.getMoveorderLineViewModel().pResetLineViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            if (!cMoveorder.currentMoveOrder.pGetLinesViaWebserviceBln(true)) {
                resultRst.resultBln = false;
                resultRst.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_movelines_failed));
                return resultRst;
            }

        }

        else {
            resultRst.resultBln = false;
            resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed));
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_MOVELINERESET);
            return resultRst;
        }

        return resultRst;

    }

    @Override
    public int compareTo(Object o) {

        int compareint =((cMoveorderLine)o).getSortingSequenceInt();
        return compareint-this.getSortingSequenceInt();

    }
}
