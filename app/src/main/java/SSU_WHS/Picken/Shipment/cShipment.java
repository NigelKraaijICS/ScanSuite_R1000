package SSU_WHS.Picken.Shipment;

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
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Pickorders.cPickorderViewModel;
import SSU_WHS.Webservice.cWebresult;
import nl.icsvertex.scansuite.R;

public class cShipment {

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

    public Boolean checkedBln;
    public Boolean isCheckedBln(){return  this.checkedBln;}

    public List<cPickorderLinePackAndShip> packAndShipLineObl;

    public List<cPickorderLinePackAndShip> linesToCheckObl(){

        List<cPickorderLinePackAndShip> resultObl = new ArrayList<>();

        if (this.packAndShipLineObl == null || this.packAndShipLineObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLinePackAndShip pickorderLinePackAndShip :this.packAndShipLineObl ) {
            if (pickorderLinePackAndShip.getQuantityCheckedDbl() == 0) {
                resultObl.add((pickorderLinePackAndShip));
            }
        }

        return  resultObl;

    }

    public List<cPickorderLinePackAndShip> linesCheckedObl(){

        List<cPickorderLinePackAndShip> resultObl = new ArrayList<>();

        if (this.packAndShipLineObl == null || this.packAndShipLineObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLinePackAndShip pickorderLinePackAndShip :this.packAndShipLineObl ) {
            if (pickorderLinePackAndShip.getQuantityCheckedDbl() > 0) {
                resultObl.add((pickorderLinePackAndShip));
            }
        }

        return  resultObl;

    }

    public static List<cShipment> allShipmentsObl;

    public static cShipment currentShipment;

    public cPickorderAddress pickorderAddress () {

        cPickorderAddress resultAddress;



                if (this.packAndShipLineObl == null || this.packAndShipLineObl.size() == 0) {
                    return  null;
                }

                if (cPickorder.currentPickOrder.adressesObl() == null || cPickorder.currentPickOrder.adressesObl().size() == 0) {
                    return  null;
                }

                for (cPickorderLinePackAndShip pickorderLinePackAndShip : this.packAndShipLineObl) {
                    for (cPickorderAddress pickorderAddress : cPickorder.currentPickOrder.adressesObl()) {
                        if (pickorderAddress.getAddrescodeStr().equalsIgnoreCase(pickorderLinePackAndShip.getDeliveryAdressCodeStr())) {
                            resultAddress = pickorderAddress;
                            return  resultAddress;
                        }
                    }

        }


        return  null;

    }

    public cShippingAgent shippingAgent () {

        cShippingAgent resultShippingAgent;




                if (this.packAndShipLineObl == null || this.packAndShipLineObl.size() == 0) {
                    return  null;
                }

                if (cShippingAgent.allShippingAgentsObl == null || cShippingAgent.allShippingAgentsObl.size() == 0) {
                    return  null;
                }

                cPickorderLinePackAndShip firstPickLine = this.packAndShipLineObl.get(0);

                for (cShippingAgent shippingAgent : cShippingAgent.allShippingAgentsObl) {


                    if (shippingAgent.getShippingAgentStr().equalsIgnoreCase(firstPickLine.getShippingAgentCodeStr())) {
                        resultShippingAgent = shippingAgent;
                        return  resultShippingAgent;
                    }

                }

        return  null;

    }

    public cShippingAgentService shippingAgentService () {

        cShippingAgentService resultShippingAgentService;

                if (this.packAndShipLineObl == null || this.packAndShipLineObl.size() == 0) {
                    return  null;
                }

                if (this.shippingAgent() == null || this.shippingAgent().shippingAgentServicesObl() == null || this.shippingAgent().shippingAgentServicesObl().size() == 0) {
                    return  null;
                }

                cPickorderLinePackAndShip firstPickLine = this.packAndShipLineObl.get(0);

                for (cShippingAgentService shippingAgentService : this.shippingAgent().shippingAgentServicesObl()) {


                    if (shippingAgentService.getShippingAgentStr().equalsIgnoreCase(firstPickLine.getShippingAgentCodeStr()) && shippingAgentService.getServiceStr().equalsIgnoreCase(firstPickLine.getShippingAgentServiceCodeStr())  ) {
                        resultShippingAgentService = shippingAgentService;
                        return  resultShippingAgentService;
                    }

                }

        return  null;

    }

    private cPickorderViewModel getPickorderViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
    }

    //End Region Public Properties

    //Region Constructor

    public cShipment(String pvSourceNoStr) {
        this.sourceNoStr = pvSourceNoStr;
        this.packAndShipLineObl = new ArrayList<>();
        this.handledBln = false;
        this.quantityDbl = (double) 0;
        this.processingSequenceStr = "";

        if (!cPickorder.currentPickOrder.PICK_SHIPPING_QC_CHECK_COUNT()) {
            this.checkedBln = true;
        }
        else {
            this.checkedBln = false;
        }

    }

    //End Region Constructor

    //Region Public Methods

    public static void pAddShipment(cShipment pvShipment) {

        if (cShipment.allShipmentsObl == null) {
            cShipment.allShipmentsObl = new ArrayList<>();
        }

        cShipment.allShipmentsObl.add(pvShipment);

    }

    public static cShipment pGetShipment (String pvSourceNoStr){

        if (cShipment.allShipmentsObl == null || cShipment.allShipmentsObl.size() == 0) {
            return  null;
        }

        for (cShipment shipment : cShipment.allShipmentsObl) {
            if (shipment.getSourceNoStr().equalsIgnoreCase(pvSourceNoStr)) {
                return  shipment;
            }
        }

        return  null;

    }

    public static cShipment pGetShipmentWithScannedBarcode(cBarcodeScan pvBarcodeScan){

        if (pvBarcodeScan.getBarcodeOriginalStr().isEmpty()) {
            return  null;
        }

        String strippedBarcodeStr = pvBarcodeScan.getBarcodeOriginalStr();


        if (cRegex.pHasPrefix(strippedBarcodeStr)) {
            strippedBarcodeStr = cRegex.pStripRegexPrefixStr(strippedBarcodeStr);
        }

        List<cShipment> hulpObl = cPickorder.currentPickOrder.pGetNotHandledShipmentsObl();

        if (hulpObl== null || hulpObl.size() == 0) {
            return  null;
        }

        for (cShipment shipment : hulpObl) {
            if (shipment.getSourceNoStr().equalsIgnoreCase(strippedBarcodeStr) ||
                shipment.getProcessingSequenceStr().equalsIgnoreCase(strippedBarcodeStr)) {
                return  shipment;
            }
        }

        return  null;

    }

    public static cShipment pGetShipmentWithScannedArticleBarcode(cBarcodeScan pvScannedBarcodeStr){

        //If scanned value is empty, we are done
        if (pvScannedBarcodeStr.getBarcodeOriginalStr().isEmpty()) {
            return  null;
        }

        //If there are no shipments to handle, we are done
        if (cPickorderLinePackAndShip.allPackAndShipLinesObl == null || cPickorderLinePackAndShip.allPackAndShipLinesObl.size() == 0) {
            return  null;
        }

        //Check all barcodes for scanned value
        for (cPickorderBarcode pickorderBarcode : cPickorder.currentPickOrder.barcodesObl() ) {

            //We found a match in barcodes of the pickorder
            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvScannedBarcodeStr.getBarcodeOriginalStr()) ||
                pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvScannedBarcodeStr.getBarcodeFormattedStr())) {

                //Get not handled line for this ItemNo and VariantCoce
              for  (cPickorderLinePackAndShip  pickorderLinePackAndShip : cPickorderLinePackAndShip.allPackAndShipLinesObl) {


                  if (pickorderLinePackAndShip.getStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT) {
                      continue;
                  }

                  //We found a match on ItemNo and VariantCode
                  if (pickorderLinePackAndShip.getItemNoStr().equalsIgnoreCase(pickorderBarcode.getItemNoStr()) && pickorderLinePackAndShip.getVariantCodeStr().equalsIgnoreCase(pickorderBarcode.getVariantCodeStr())) {

                      //Get Shipment by SourceNo
                      return  cShipment.pGetShipment(pickorderLinePackAndShip.getSourceNoStr());

                  }
              }
            }
        }
        return  null;
    }

    public cResult pShipmentDoneRst() {

        cResult result = new cResult();

        //Call webservice
        cWebresult webresult =  this.getPickorderViewModel().pPickorderSourceDocumentShippedViaWebserviceBln();

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

        for (cPickorderLinePackAndShip pickorderLinePackAndShip : this.packAndShipLineObl) {
            pickorderLinePackAndShip.statusInt = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
        }

        //We are done, so this shipmemt is handled and result is true
        result.resultBln = true;
        cShipment.currentShipment.handledBln = true;
        return  result;
    }

    public cResult pCheckingDoneRst() {

        cResult result = new cResult();
        result.resultBln = true;
        //We are done, so this shipmemt is checked and result is true

        this.checkedBln = true;
        return  result;
    }

    public void pAddPackAndShipLine(cPickorderLinePackAndShip pvPickorderLinePackAndShip){
        this.packAndShipLineObl.add((pvPickorderLinePackAndShip));
        this.quantityDbl += pvPickorderLinePackAndShip.getQuantityDbl();
        this.processingSequenceStr = pvPickorderLinePackAndShip.getProcessingSequenceStr();
    }

    public cResult pCheckShipmentRst(){

        cResult result = new cResult();
        result.resultBln = true;

        if (!cPickorder.currentPickOrder.isPackAndShipNeededBln()) {
            return  result;
        }

        if (cShipment.currentShipment.shippingAgent() == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_shipping_agent_unkown_or_empty));
            return  result;
        }

        if (this.shippingAgentService() == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_shipping_agentservice_unkown_or_empty));
            return  result;
        }

        if (this.shippingAgentService().shippingUnitsObl() == null || this.shippingAgentService().shippingUnitsObl().size() == 0 ) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_shipping_agentservice_shippingingunits_unkown_or_empty));
            return  result;
        }

        return  result;

    }
    
    public void pCheckIfShipmentIsChecked(){
        
     if (this.packAndShipLineObl == null || this.packAndShipLineObl.size() == 0) {
         return;
         
     }

     //If we habe a line that still needs to be checken, then we are done
     for (cPickorderLinePackAndShip pickorderLinePackAndShip : this.packAndShipLineObl) {
         if (pickorderLinePackAndShip.getQuantityCheckedDbl() < pickorderLinePackAndShip.getQuantityDbl()) {
             return;
         }
     }

     //All lines are checken, so this shipment is checked
     this.pCheckingDoneRst();
     
        
    }

    public cPickorderLinePackAndShip pGetLineNotHandledByBarcode(String pvScannedBarcodeStr) {

        if (cPickorder.currentPickOrder.barcodesObl() == null || cPickorder.currentPickOrder.barcodesObl().size() == 0)  {
            return  null;
        }

        if (this.linesToCheckObl() == null || this.linesToCheckObl().size() == 0) {
            return  null;
        }



        for (cPickorderBarcode pickorderBarcode : cPickorder.currentPickOrder.barcodesObl() ) {
            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvScannedBarcodeStr) || pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvScannedBarcodeStr)){
                cPickorder.currentPickOrder.pickorderQCBarcodeScanned = pickorderBarcode;
                break;
            }
        }

        if (cPickorder.currentPickOrder.pickorderQCBarcodeScanned   == null) {
            return  null;
        }

        for (cPickorderLinePackAndShip pickorderLinePackAndShip : this.linesToCheckObl()) {
            if (pickorderLinePackAndShip.getItemNoStr().equalsIgnoreCase(cPickorder.currentPickOrder.pickorderQCBarcodeScanned .getItemNoStr()) &&
                    pickorderLinePackAndShip.getVariantCodeStr().equalsIgnoreCase((cPickorder.currentPickOrder.pickorderQCBarcodeScanned .getVariantCodeStr()))) {
                    return  pickorderLinePackAndShip;
            }
        }

        return null;
    }



    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods


    //End Region Constructor

}
