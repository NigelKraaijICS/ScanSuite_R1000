package SSU_WHS.Picken.FinishSinglePieceLine;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cPickorderLineFinishSinglePiece {

    //Region Public Properties

    private String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    private String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    private String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    private String vendorItemNoStr;
    public String getVendorItemNoStr() {
        return vendorItemNoStr;
    }

    private String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() {
        return vendorItemDescriptionStr;
    }

    private String sourceNoStr;
    public String getSourceNoStr() {
        return sourceNoStr;
    }

    public int statusInt;
    public  int getStatusInt(){
        return  statusInt;
    }

    public int localStatusInt;
    public int getLocalStatusInt() {
        return localStatusInt;
    }

    public double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    public static List<cPickorderLineFinishSinglePiece> allFinishSinglePieceLinesObl;

    public  static  List<cPickorderLineFinishSinglePiece> linesToShipObl () {

        List <cPickorderLineFinishSinglePiece> resultObl = new ArrayList<>();

        if (cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl == null || cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl.size() == 0 ) {
            return  resultObl;
        }

        for (cPickorderLineFinishSinglePiece  pickorderLineFinishSinglePiece : cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl) {
            if (pickorderLineFinishSinglePiece.getLocalStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW) {
                resultObl.add(pickorderLineFinishSinglePiece);
            }
        }

        return  resultObl;

    }

    public  static  List<cPickorderLineFinishSinglePiece> linesShippedObl () {

        List <cPickorderLineFinishSinglePiece> resultObl = new ArrayList<>();

        if (cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl == null || cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl.size() == 0 ) {
            return  resultObl;
        }

        for (cPickorderLineFinishSinglePiece  pickorderLineFinishSinglePiece : cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl) {
            if (pickorderLineFinishSinglePiece.getLocalStatusInt() > cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW) {
                resultObl.add(pickorderLineFinishSinglePiece);
            }
        }

        return  resultObl;

    }

    public static cPickorderLineFinishSinglePiece currentFinishSinglePieceLine;

    public  List<cPickorderBarcode> barcodesObl;

    private cPickorderLineFinishSinglePieceViewModel getPickorderLineFinishSinglePieceViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLineFinishSinglePieceViewModel.class);
    }

    //End Region Public Properties

    //Region Constructor

    public cPickorderLineFinishSinglePiece(JSONObject pvJsonObject){

        cPickorderLineFinishSinglePieceEntity pickorderLineFinishSinglePieceEntity = new cPickorderLineFinishSinglePieceEntity(pvJsonObject);

        this.itemNoStr = pickorderLineFinishSinglePieceEntity.getItemNoStr();
        this.variantCodeStr = pickorderLineFinishSinglePieceEntity.getVariantCodeStr();
        this.descriptionStr = pickorderLineFinishSinglePieceEntity.getDescriptionStr();
        this.description2Str = pickorderLineFinishSinglePieceEntity.getDescription2Str();
        this.vendorItemNoStr =  pickorderLineFinishSinglePieceEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = pickorderLineFinishSinglePieceEntity.getVendorItemDescriptionStr();
        this.sourceNoStr = pickorderLineFinishSinglePieceEntity.getSourceNoStr();

        this.quantityDbl =  pickorderLineFinishSinglePieceEntity.getQuantityDbl();
        this.quantityHandledDbl = pickorderLineFinishSinglePieceEntity.getQuantityHandledDbl();
        this.statusInt = pickorderLineFinishSinglePieceEntity.getStatusInt();
        this.localStatusInt = pickorderLineFinishSinglePieceEntity.getLocalStatusInt();

    }

    public static cPickorderLineFinishSinglePiece pGetPickorderLineFinisgSinglePieceWithScannedArticleBarcode(cBarcodeScan pvScannedBarcodeStr){

        //If scanned value is empty, we are done
        if (pvScannedBarcodeStr.getBarcodeOriginalStr().isEmpty()) {
            return  null;
        }

        //If there are no shipments to handle, we are done
        cPickorderLineFinishSinglePiece.linesToShipObl();
        if (cPickorderLineFinishSinglePiece.linesToShipObl().size() == 0) {
            return  null;
        }

        //Check all barcodes for scanned value
        for (cPickorderBarcode pickorderBarcode : cPickorder.currentPickOrder.barcodesObl() ) {

            //We found a match in barcodes of the pickorder
            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvScannedBarcodeStr.getBarcodeOriginalStr()) ||
                    pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvScannedBarcodeStr.getBarcodeFormattedStr())) {

                //Get not handled line for this ItemNo and VariantCoce
                for  (cPickorderLineFinishSinglePiece  pickorderLineFinishSinglePiece : cPickorderLineFinishSinglePiece.linesToShipObl()) {

                    //We found a match on ItemNo and VariantCode
                    if (pickorderLineFinishSinglePiece.getItemNoStr().equalsIgnoreCase(pickorderBarcode.getItemNoStr()) && pickorderLineFinishSinglePiece.getVariantCodeStr().equalsIgnoreCase(pickorderBarcode.getVariantCodeStr())) {

                        //Get Shipment by SourceNo
                        return  pickorderLineFinishSinglePiece;

                    }
                }
            }
        }
        return  null;
    }

    public cResult pPrintDocumentsViaWebserviceWrs() {

        cResult resultRst = new cResult();
        resultRst.resultBln = true;

        cWebresult WebResult;
        WebResult =  this.getPickorderLineFinishSinglePieceViewModel().pPrintDocumentsViaWebserviceWrs();
        if (!WebResult.getResultBln() || !WebResult.getSuccessBln()) {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            resultRst.resultBln = false;
            resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_print_failed) + " " + WebResult.getResultObl().get(0));
        }
        return resultRst;
    }

    //End Region Constructor

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods

}
