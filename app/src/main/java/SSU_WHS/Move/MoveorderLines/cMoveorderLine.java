package SSU_WHS.Move.MoveorderLines;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cDateAndTime;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLineBarcodes.cMoveorderLineBarcode;
import SSU_WHS.Move.MoveorderLineBarcodes.cMoveorderLineBarcodeViewModel;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cMoveorderLine {

    private Integer recordIDInt;
    public Integer getRecordIDInt() {
        return recordIDInt;
    }

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

    private cMoveorderLineEntity moveorderLineEntity;

    public static List<cMoveorderLine> allLinesObl;
    public static cMoveorderLine currentMoveOrderLine;
    public List<cMoveorderLineBarcode> lineBarcodesObl;

    private cMoveorderLineViewModel getMoveorderLineViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineViewModel.class);
    }

    private cMoveorderLineBarcodeViewModel getMoveorderLineBarcodeViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineBarcodeViewModel.class);
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

        this.extraField1Str =  this.moveorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.moveorderLineEntity.getExtraField2Str();
        this.extraField3Str =  this.moveorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.moveorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.moveorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.moveorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.moveorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.moveorderLineEntity.getExtraField8Str();
    }

    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        this.getMoveorderLineViewModel().insert(this.moveorderLineEntity);

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

    public boolean pHandledIndatabaseBln(){


        if (!this.mUpdateQuanitityHandled(this.quantityHandledDbl)) {
            return  false;
        }

        if (!this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT)) {
            return  false;
        }

        this.handledTimeStampStr = cDateAndTime.pGetCurrentDateTimeForWebserviceStr();

        return this.mUpdateHandledTimeStampBln(this.handledTimeStampStr);

    }


    public boolean pHandledTakeBln() {

        cWebresult WebResult;
        WebResult =  this.getMoveorderLineViewModel().pMoveLineHandledTakeMTViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            return this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT);
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public boolean pCancelIndatabaseBln(){


        if (!this.mUpdateQuanitityHandled(this.quantityHandledDbl)) {
            return  false;
        }

        if (!this.mUpdateLocalStatusBln(cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_NEW)) {
            return  false;
        }

        this.handledTimeStampStr = "";

        return this.mUpdateHandledTimeStampBln(this.handledTimeStampStr);

    }

    public static boolean pMoveNewItemHandledBln(String pvBincodeStr, List<cMoveorderBarcode> pvScannedBarcodesObl, String pvActionTypeStr) {
       cWebresult WebResult;

        cMoveorderLineViewModel moveorderLineViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineViewModel.class);
        WebResult = moveorderLineViewModel.pMoveNewItemHandledViaWebserviceWrs(pvBincodeStr, pvScannedBarcodesObl, pvActionTypeStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (   JSONObject jsonObject : WebResult.getResultDtt()) {
                cMoveorderLine moveorderLine = new cMoveorderLine(jsonObject);
                moveorderLine.pInsertInDatabaseBln();
                currentMoveOrderLine = moveorderLine;
                return true;
            }
            return  true;
        }
        else {

            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public static boolean pMoveItemHandledBln() {
        cWebresult WebResult;
        cMoveorderLineViewModel moveorderLineViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineViewModel.class);
        WebResult =  moveorderLineViewModel.pMoveItemHandledViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            cUserInterface.pShowToastMessage("Succes!!", null);
//            if(this.mUpdateLocalStatusBln(cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_DONE_SENT) == false) {
//                return  false;
//            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public boolean pMoveItemPlaceHandledBln() {
        cWebresult WebResult;
        WebResult =  this.getMoveorderLineViewModel().pMoveItemPlaceHandledViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            cUserInterface.pShowToastMessage("Succes!!", null);
//            if(this.mUpdateLocalStatusBln(cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_DONE_SENT) == false) {
//                return  false;
//            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public cMoveorderLineBarcode pAddLineBarcode(String pvBarcodeStr, Double pvQuantityDbl) {

        cMoveorderLineBarcode moveorderLineBarcode = new cMoveorderLineBarcode((long) this.getLineNoInt(),pvBarcodeStr,pvQuantityDbl);

        if (cMoveorderLineBarcode.allLineBarcodesObl == null){
            cMoveorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }

        cMoveorderLineBarcode.allLineBarcodesObl.add(moveorderLineBarcode);

        return moveorderLineBarcode;
    }

    public boolean pHandledPlaceBln() {

        cWebresult WebResult;
        WebResult =  this.getMoveorderLineViewModel().pMoveLineHandledPlaceMTViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            return this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT);
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public boolean pRemoveOrUpdateLineBarcodeBln(){

        //If there are no line barcodes, this should not be possible
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            return false;
        }

        for (cMoveorderLineBarcode moveorderLineBarcode : this.handledBarcodesObl()  ) {

            if (moveorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cMoveorderBarcode.currentMoveOrderBarcode.getBarcodeStr())) {

                moveorderLineBarcode.quantityHandledDbl -= cMoveorderBarcode.currentMoveOrderBarcode.quantityPerUnitOfMeasure;

                if (moveorderLineBarcode.getQuantityhandledDbl() > 0) {
                    moveorderLineBarcode.pUpdateAmountInDatabase();
                    return  true;
                }

                moveorderLineBarcode.pDeleteFromDatabaseBln();
                return  true;
            }
        }
        return  false;
    }

    private boolean mUpdateQuanitityHandled(double pvQuantityHandledBln) {

        boolean resultBln;
        resultBln =   this.getMoveorderLineViewModel().pUpdateQuantityHandledBln(pvQuantityHandledBln);

        if (!resultBln) {
            return  false;
        }

        this.quantityHandledDbl = pvQuantityHandledBln;
        return true;

    }
    private boolean mUpdateLocalStatusBln(Integer pvNewStatusInt) {

        boolean resultBln;
        resultBln =   this.getMoveorderLineViewModel().pUpdateLocalStatusBln(pvNewStatusInt);

        if (!resultBln) {
            return  false;
        }

        this.localStatusInt = pvNewStatusInt;
        return true;

    }

    private boolean mUpdateHandledTimeStampBln(String pvHandledTimeStampStr) {

        boolean resultBln;
        resultBln =   this.getMoveorderLineViewModel().pUpdateHandledTimeStampBln(pvHandledTimeStampStr);

        return resultBln;

    }

    public static void getTakeLineForBincodeFromDatabase(String binCodeStr) {
        cMoveorderLine moveorderLine = cMoveorder.currentMoveOrder.pGetLineForBin(binCodeStr);
        if (moveorderLine != null) {
            cMoveorderLine.currentMoveOrderLine = moveorderLine;
            return;
        }
        cMoveorderLine.currentMoveOrderLine = null;
    }

    public static void getTakeLineForArticleFromDatabase() {
        cMoveorderLine moveorderLine = cMoveorder.currentMoveOrder.pGetLineForArticle();
        if (moveorderLine != null) {
            cMoveorderLine.currentMoveOrderLine = moveorderLine;
            return;
        }
        cMoveorderLine.currentMoveOrderLine = null;
    }

    public List<cMoveorderLineBarcode> handledBarcodesObl(){


        List<cMoveorderLineBarcode> resultObl;
        resultObl = new ArrayList<>();

        if (cMoveorderLineBarcode.allLineBarcodesObl == null) {
            return  resultObl;
        }

        for (cMoveorderLineBarcode moveorderLineBarcode :cMoveorderLineBarcode.allLineBarcodesObl ) {

            int result = Long.compare(moveorderLineBarcode.getLineNoLng(), this.getLineNoInt());
            if (result == 0) {
                resultObl.add(moveorderLineBarcode);
            }
        }


        return  resultObl;
    }

      public boolean pAddOrUpdateLineBarcodeBln(Double pvAmountDbl){

        //If there are no line barcodes, then simply add this one
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            cMoveorderLineBarcode moveorderLineBarcode = new cMoveorderLineBarcode((long) cMoveorderLine.currentMoveOrderLine.getLineNoInt(), cMoveorderBarcode.currentMoveOrderBarcode.getBarcodeStr());
            moveorderLineBarcode.quantityHandledDbl = pvAmountDbl;
            moveorderLineBarcode.pInsertInDatabaseBln();
            return true;
        }

        for (cMoveorderLineBarcode moveorderLineBarcode : this.handledBarcodesObl()  ) {

            if (moveorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cMoveorderBarcode.currentMoveOrderBarcode.getBarcodeStr())) {
                moveorderLineBarcode.quantityHandledDbl += pvAmountDbl;
                moveorderLineBarcode.pUpdateAmountInDatabase();
                return  true;
            }
        }
        return  false;
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


    private void mUpdateQuantityInDatabase(){
        this.getMoveorderLineViewModel().pUpdateQuantityBln();
    }

    public cMoveorderLineBarcode pGetLineBarcodeByScannedBarcode(String pvBarcodeStr) {

        //We scanned a barcode that belongs to the current article, so check if we already have a line barcode
        for (cMoveorderLineBarcode moveorderLineBarcode : this.lineBarcodesObl) {

            //We have a match, so set the current line
            if (moveorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeStr)) {
                return moveorderLineBarcode;
            }
        }

        return  null;
    }

    public cResult pResetRst(){

        //nit the result
        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        WebResult =  this.getMoveorderLineViewModel().pResetLineViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            //Remove line barcodes from the database
            this.getMoveorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoInt());

            //Reset this line
            this.quantityHandledDbl = 0.0;
            this.mUpdateQuantityInDatabase();
            this.lineBarcodesObl.clear();

            return  result;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_MOVELINERESET);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed));
            return  result;
        }
    }

    private  List<cMoveorderBarcode> mGetBarcodesObl(){

        //If barcodes already filled, then we are done
        if (this.barcodesObl != null) {
            return this.barcodesObl;
        }

        // Get barcodes via PickorderBarcode class
        barcodesObl = cMoveorderBarcode.pGetMovebarcodeViaVariantAndItemNoObl(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.barcodesObl == null || this.barcodesObl.size() == 0) {
            return  null;
        }

        return  this.barcodesObl;
    }

}
