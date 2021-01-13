package SSU_WHS.Picken.Storement;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.cAppExtension;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddress;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShip;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Pickorders.cPickorderViewModel;
import SSU_WHS.Webservice.cWebresult;
import nl.icsvertex.scansuite.R;

public class cStorement {

    //Region Public Properties

    private String sourceNoStr;
    public String getSourceNoStr() {
        return this.sourceNoStr;
    }

    public Double quantityDbl;
    public Double getQuantityDbl() {
        return this.quantityDbl;
    }

    private String processingSequenceStr;
    public String getProcessingSequenceStr() {
        return this.processingSequenceStr;
    }

    public Boolean handledBln;
    public Boolean isHandledBln(){return  this.handledBln;}

    public String binCodeStr;
    public String getBinCodeStr() {
        return this.binCodeStr;
    }


    public List<cPickorderLine> pickorderLinesObl;

    public static List<cStorement> allStorementsObl;

    public static cStorement currentStorement;


    private cPickorderViewModel getPickorderViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
    }

    //End Region Public Properties

    //Region Constructor

    public cStorement(String pvSourceNoStr) {
        this.sourceNoStr = pvSourceNoStr;
        this.pickorderLinesObl = new ArrayList<>();
        this.handledBln = false;
        this.quantityDbl = (double) 0;
        this.processingSequenceStr = "";
    }

    //End Region Constructor

    //Region Public Methods

    public static void pAddStorement(cStorement pvShipment) {

        if (cStorement.allStorementsObl == null) {
            cStorement.allStorementsObl = new ArrayList<>();
        }

        cStorement.allStorementsObl.add(pvShipment);

    }

    public static cStorement pGetStorement(String pvSourceNoStr){

        if (cStorement.allStorementsObl == null || cStorement.allStorementsObl.size() == 0) {
            return  null;
        }

        for (cStorement storement : cStorement.allStorementsObl) {
            if (storement.getSourceNoStr().equalsIgnoreCase(pvSourceNoStr)) {
                return  storement;
            }
        }

        return  null;

    }

    public static cStorement pGetStorementWithScannedBarcode(cBarcodeScan pvBarcodeScan){

        if (pvBarcodeScan.getBarcodeOriginalStr().isEmpty()) {
            return  null;
        }

        String strippedBarcodeStr = pvBarcodeScan.getBarcodeOriginalStr();


        if (cRegex.pHasPrefix(strippedBarcodeStr)) {
            strippedBarcodeStr = cRegex.pStripRegexPrefixStr(strippedBarcodeStr);
        }

        List<cStorement> hulpObl = cPickorder.currentPickOrder.pGetNotHandledStorementsObl();

        if (hulpObl== null || hulpObl.size() == 0) {
            return  null;
        }

        for (cStorement storement : hulpObl) {
            if (storement.getSourceNoStr().equalsIgnoreCase(strippedBarcodeStr) ||
                    storement.getProcessingSequenceStr().equalsIgnoreCase(strippedBarcodeStr)) {
                return  storement;
            }
        }

        return  null;

    }

    public cResult pStorementDoneRst() {

        cResult result = new cResult();

        //Call webservice
        cWebresult webresult =  this.getPickorderViewModel().pPickorderSourceDocumentStoredViaWebserviceBln();

        //If something went wrong, show this and rest statusses
        if (! webresult.getResultBln()) {

            result.resultBln = false;

            //Add web errors to result
            for (String resulStr : webresult.getResultObl()) {
                result.pAddErrorMessage(resulStr);
            }

            //Leave, we are done
            return result;
        }

        //We are done, so this shipmemt is handled and result is true
        result.resultBln = true;
        cStorement.currentStorement.handledBln = true;
        return  result;
    }

    public void pAddPickorderLine(cPickorderLine pvPickorderLine){
        this.pickorderLinesObl.add((pvPickorderLine));
        this.quantityDbl += pvPickorderLine.getQuantityDbl();
        this.processingSequenceStr = pvPickorderLine.getProcessingSequenceStr();
    }


    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods


    //End Region Constructor

}
