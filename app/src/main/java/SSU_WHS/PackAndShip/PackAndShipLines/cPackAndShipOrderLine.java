package SSU_WHS.PackAndShip.PackAndShipLines;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cResult;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveItemVariant.cMoveItemVariant;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLineBarcode.cMoveorderLineBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineViewModel;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cPackAndShipOrderLine {

    public String actionTypeCodeStr;
    public String getActionTypeCodeStr() { return actionTypeCodeStr; }

    public String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
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

    public String handledTimeStampStr;
    public String getHandledTimeStampStr() { return handledTimeStampStr; }

    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public int lineNoInt;
    public int getLineNoInt() {
        return lineNoInt;
    }

    public  String getItemNoAndVariantCodeStr(){

        String resultStr = this.getItemNoStr();

        if (!this.getVariantCodeStr().isEmpty()) {
            resultStr += " " + this.getVariantCodeStr();
        }

        return  resultStr;
    }

    public double quantityDbl;
    public double getQuantityDbl() {
        return quantityDbl;
    }

    public double quantityHandledDbl;
    public double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    public  int sortingSequenceInt;
    public  int getSortingSequenceInt(){
        return  this.sortingSequenceInt;
    }

    public int statusInt;
    public int getStatusInt() { return statusInt; }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public int localStatusInt;
    public int getLocalStatusInt() { return localStatusInt; }

    public  String getKeyStr() {
        return  this.getItemNoStr() + "þ" + this.getVariantCodeStr();
    }

    private cPackAndShipOrderLineEntity packAndShipOrderLineEntity;

    public static List<cPackAndShipOrderLine> allLinesObl;
    public static cPackAndShipOrderLine currentMoveOrderLine;

    private cPackAndShipOrderLineViewModel getPackAndShipOrderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipOrderLineViewModel.class);
    }


    //Region Public Properties


    //End Region Public Properties

    //Region Constructor
    public cPackAndShipOrderLine(JSONObject pvJsonObject) {
        this.packAndShipOrderLineEntity = new cPackAndShipOrderLineEntity(pvJsonObject);

        this.actionTypeCodeStr = this.packAndShipOrderLineEntity.getActiontypecodeStr();
        this.binCodeStr = this.packAndShipOrderLineEntity.getBincodeStr();
        this.descriptionStr = this.packAndShipOrderLineEntity.getDescriptionStr();
        this.description2Str = this.packAndShipOrderLineEntity.getDescription2Str();
        this.handledTimeStampStr = this.packAndShipOrderLineEntity.getHandledtimestampStr();
        this.itemNoStr = this.packAndShipOrderLineEntity.getItemNoStr();
        this.lineNoInt = this.packAndShipOrderLineEntity.getLineNoInt();
        this.quantityDbl = this.packAndShipOrderLineEntity.getQuantityDbl();
        this.quantityHandledDbl = this.packAndShipOrderLineEntity.getQuantityhandledDbl();
        this.sortingSequenceInt = this.packAndShipOrderLineEntity.getSortingSequenceNoInt();
        this.statusInt = this.packAndShipOrderLineEntity.getStatusInt();
        this.variantCodeStr = this.packAndShipOrderLineEntity.getVariantCodeStr();
    }


    //End Region Constructor

    public static cPackAndShipOrderLine pGetLineByLineNo(int pvLineNoLInt){

        if (cPackAndShipOrderLine.allLinesObl == null) {
            return  null;
        }

        for (cPackAndShipOrderLine packAndShipOrderLine : cPackAndShipOrderLine.allLinesObl ) {

            if (packAndShipOrderLine.getLineNoInt() == pvLineNoLInt) {
                return  packAndShipOrderLine;
            }
        }

        return  null;

    }

    public boolean pInsertInDatabaseBln() {

        if (cPackAndShipOrderLine.allLinesObl == null){
            cPackAndShipOrderLine.allLinesObl = new ArrayList<>();
        }
        cPackAndShipOrderLine.allLinesObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cPackAndShipOrderLineViewModel packAndShipOrderLineViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipOrderLineViewModel.class);
        packAndShipOrderLineViewModel.deleteAll();
        return true;
    }

}
