package SSU_WHS.Move.MoveorderLines;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ICS.Utils.cResult;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Basics.ArticlePropertyValue.cArticlePropertyValue;
import SSU_WHS.Basics.ArticleStock.cArticleStock;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Move.MoveItemVariant.cMoveItemVariant;
import SSU_WHS.Move.Moveorders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLineBarcode.cMoveorderLineBarcode;
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

        String resultStr = this.getItemNoStr();

        if (!this.getVariantCodeStr().isEmpty()) {
            resultStr += " " + this.getVariantCodeStr();
        }

        return  resultStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    public  String getDescriptionExtendedStr(){

        String resultStr = this.getDescriptionStr();

        if (!this.getDescription2Str().isEmpty()) {
            resultStr += " " + this.getDescription2Str();
        }

        return  resultStr;
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

    public  Double getQuantityPlaceable() {

        if(this.moveItemVariant() == null) {
            return Double.valueOf(0);
        }


        if (this.moveItemVariant().getQuantityTodoPlaceDbl() >  this.getQuantityDbl()) {
           return  this.getQuantityDbl();
        }
        else
        {
            return  this.moveItemVariant().getQuantityTodoPlaceDbl();
        }

    }

    public  boolean handledBln = false;
    public boolean isHandledBln() {
        return handledBln;
    }

    public  String getKeyStr() {
        return  this.getItemNoStr() + "þ" + this.getVariantCodeStr();
    }

    public  cMoveItemVariant moveItemVariant() {
      return   cMoveItemVariant.allMoveItemVariantObl.get(this.getKeyStr());
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

    public boolean isUniqueBln() {

        if (this.barcodesObl == null || this.barcodesObl.size() == 0) {
            return  false;
        }

        for (cMoveorderBarcode moveorderBarcode : this.barcodesObl) {
            if (moveorderBarcode.getIsUniqueBarcodeBln()) {
                return  true;
            }
        }

        return  false;

    }


    //Region Public Properties

    public cArticleImage articleImage;

    public  List<cMoveorderBarcode> barcodesObl;
    public List<cMoveorderBarcode> orderBarcodesObl(){

        List<cMoveorderBarcode> resultObl = new ArrayList<>();

        //We have a different barcode, so check if this barcode belong to the current article
        for (cMoveorderBarcode moveorderBarcode : cMoveorderBarcode.allMoveorderBarcodesObl) {

            if (moveorderBarcode.getItemNoStr().equalsIgnoreCase(this.getItemNoStr()) && moveorderBarcode.getVariantCodeStr().equalsIgnoreCase(this.getVariantCodeStr())) {
                resultObl.add((moveorderBarcode));
            }
        }

        return  resultObl;

    }

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

        if (cMoveorder.currentMoveOrder.getOrderTypeStr().equalsIgnoreCase("MT")) {
            if (this.getStatusInt() > cWarehouseorder.MoveStatusEnu.Move_Take ) {
                this.handledBln = true;
            }
        }

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

    public cMoveorderLine(cArticleStock pvArticleStock) {
        this.moveorderLineEntity = null;

        this.lineNoInt = 0;
        this.actionTypeCodeStr = cWarehouseorder.ActionTypeEnu.TAKE.toString();

        this.itemNoStr = pvArticleStock.getItemNoStr();
        this.variantCodeStr = pvArticleStock.getVariantCodeStr();
        this.descriptionStr = "";
        this.description2Str = "";
        this.binCodeStr = pvArticleStock.getBincodeStr();

        this.quantityDbl = pvArticleStock.getQuantityDbl();
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

    public static cMoveorderLine pGetLineByLineNo(int pvLineNoLInt){

        if (cMoveorderLine.allLinesObl == null) {
            return  null;
        }

        for (cMoveorderLine moveorderLine : cMoveorderLine.allLinesObl ) {

            if (moveorderLine.getLineNoInt() == pvLineNoLInt) {
                return  moveorderLine;
            }
        }

        return  null;

    }

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

            if (!cMoveorder.currentMoveOrder.pGetBarcodesViaWebserviceBln(true)) {
                resultRst.resultBln = false;
                resultRst.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
                return resultRst;
            }

            if (!cMoveorder.currentMoveOrder.pGetLineBarcodesViaWebserviceBln(true)) {
                resultRst.resultBln = false;
                resultRst.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
                return resultRst;
            }

            if (cMoveorder.currentMoveOrder.getOrderTypeStr().equalsIgnoreCase("MI")) {
                if (!cMoveorder.currentMoveOrder.pMatchBarcodesAndLinesBln()) {
                    resultRst.resultBln = false;
                    resultRst.pAddErrorMessage(cAppExtension.context.getString(R.string.error_matching_lines_and_barcodes_failed));
                    return resultRst;
                }
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

    public ArrayList<cLinePropertyValue> presetValueObl;
    public ArrayList<cLinePropertyValue> generatedValueObl;

    public LinkedHashMap<String, ArrayList<cLinePropertyValue>> generatedTakeItemPropertySortObl(){
        if (this.generatedValueObl == null){
            return null;
        }

        LinkedHashMap<String, ArrayList<cLinePropertyValue>> linkedHashMap = new LinkedHashMap<>();

        for (cLinePropertyValue linePropertyValue : generatedValueObl) {
            //Create the hasmap dynammically and fill it
            ArrayList<cLinePropertyValue> loopList = linkedHashMap.get(linePropertyValue.getPropertyCodeStr());
            if (loopList == null) {
                ArrayList<cLinePropertyValue> propertyValues = new ArrayList<>();
                propertyValues.add(linePropertyValue);
                linkedHashMap.put(linePropertyValue.getPropertyCodeStr(), propertyValues);
            }
            else{
                loopList.add(linePropertyValue);
            }

        }
        return linkedHashMap;
    }

    public ArrayList<cLinePropertyValue> generatedPlaceLineValueObl;

    public LinkedHashMap<String, ArrayList<cLinePropertyValue>> generatedPlaceItemPropertySortObl(){
        if (this.generatedPlaceLineValueObl == null){
            return null;
        }

        LinkedHashMap<String, ArrayList<cLinePropertyValue>> linkedHashMap = new LinkedHashMap<>();

        for (cLinePropertyValue linePropertyValue : generatedPlaceLineValueObl) {
            //Create the hasmap dynammically and fill it
            ArrayList<cLinePropertyValue> loopList = linkedHashMap.get(linePropertyValue.getPropertyCodeStr());
            if (loopList == null) {
                ArrayList<cLinePropertyValue> propertyValues = new ArrayList<>();
                propertyValues.add(linePropertyValue);
                linkedHashMap.put(linePropertyValue.getPropertyCodeStr(), propertyValues);
            }
            else{
                loopList.add(linePropertyValue);
            }

        }
        return linkedHashMap;
    }

    private  List<cLineProperty> linePropertyCachedObl;
    private List<cLineProperty> linePropertyObl(boolean pvRefreshBln) {

        if (pvRefreshBln){this.linePropertyCachedObl = null;}

        if (this.linePropertyCachedObl != null) {
            return  this.linePropertyCachedObl;
        }

        this.linePropertyCachedObl = new ArrayList<>();

        if (cLineProperty.allLinePropertysObl == null || cLineProperty.allLinePropertysObl.size() == 0) {
            return  this.linePropertyCachedObl;
        }

        for (cLineProperty lineProperty :cLineProperty.allLinePropertysObl ) {
            if (lineProperty.getLineNoInt().equals(this.getLineNoInt())) {
                this.linePropertyCachedObl.add(lineProperty);
            }
        }
        return  this.linePropertyCachedObl;
    }

    private  List<cLineProperty> linePropertyNoInputCachedObl;
    public List<cLineProperty> linePropertyNoInputObl() {

        if (this.linePropertyNoInputCachedObl != null) {
            return  this.linePropertyNoInputCachedObl;
        }

        this.linePropertyNoInputCachedObl = new ArrayList<>();

        if (this.linePropertyObl(true) == null || this.linePropertyObl(false).size() == 0) {
            return  this.linePropertyNoInputCachedObl;
        }

        for (cLineProperty lineProperty :this.linePropertyObl(false)) {
            if (!lineProperty.getIsInputBln() &&  !lineProperty.getIsRequiredBln()) {
                this.linePropertyNoInputCachedObl.add(lineProperty);
            }
        }

        return  this.linePropertyNoInputCachedObl;
    }

    public List<cLineProperty> linePropertyInputObl() {

        List<cLineProperty> resultObl = new ArrayList<>();

        if (this.linePropertyObl(true) == null || this.linePropertyObl(false).size() == 0) {
            return  resultObl;
        }

        for (cLineProperty lineProperty :this.linePropertyObl(false)) {
            if (lineProperty.getIsInputBln() ) {
                resultObl.add(lineProperty);
            }
        }

        return  resultObl;
    }

    public  cLineProperty getLineProperty(String pvPropertyCodeStr){

        if (this.linePropertyInputObl().size() == 0) {
            return  null;
        }

        for (cLineProperty lineProperty : this.linePropertyObl(true) ) {
            if (lineProperty.getLineNoInt().equals(this.getLineNoInt()) && lineProperty.getPropertyCodeStr().equalsIgnoreCase(pvPropertyCodeStr)) {
                return lineProperty;
            }
        }

        return  null;

    }

    public void pAddPropertyValue(cArticlePropertyValue pvPropertyValue, double pvAvailableDbl){
        if (this.generatedValueObl == null){
            this.generatedValueObl = new ArrayList<>();
        }
        cLinePropertyValue linePropertyValue = new cLinePropertyValue(this.lineNoInt, pvPropertyValue.getPropertyCodeStr(), pvPropertyValue.getValueStr());
        linePropertyValue.quantityAvailableDbl = pvAvailableDbl;

        this.generatedValueObl.add(linePropertyValue);
        if (cLinePropertyValue.allLinePropertysValuesObl == null) {
            cLinePropertyValue.allLinePropertysValuesObl = new ArrayList<>();
        }
        cLinePropertyValue.allLinePropertysValuesObl.add(linePropertyValue);
    }

    public  List<cLinePropertyValue> linePropertyValuesObl() {

        List<cLinePropertyValue> resultObl = new ArrayList<>();

        for (cLineProperty inputLineProperty : this.linePropertyInputObl()) {
            resultObl.addAll(inputLineProperty.propertyValueObl());
        }

        return  resultObl;

    }
    public  boolean hasPropertysBln() {
        return this.linePropertyObl(true).size() != 0;
    }
}
