package SSU_WHS.Picken.Shipment;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddress;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShip;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;

public class cShipment {

    //Region Public Properties

    public String sourceNoStr;
    public String getSourceNoStr() {
        return this.sourceNoStr;
    }

    public Double quantityDbl;
    public Double getQuantityDbl() {
        return this.quantityDbl;
    }

    public String processingSequenceStr;
    public String getProcessingSequenceStr() {
        return this.processingSequenceStr;
    }

    public Boolean handledBln;
    public Boolean getHandledBln(){return  this.handledBln;}

    public List<cPickorderLinePackAndShip> packAndShipLineObl;

    public static List<cShipment> allShipmentsObl;

    public static cShipment currentShipment;

    public static cShipmentAdapter gShipmentsToshipAdapter;
    public static cShipmentAdapter getShipmentsToshipAdapter() {
        if (gShipmentsToshipAdapter == null) {
            gShipmentsToshipAdapter = new cShipmentAdapter();
        }
        return gShipmentsToshipAdapter;
    }

    public static cShipmentAdapter gShipmentsShippedAdapter;
    public static cShipmentAdapter gethipmentsShippedAdapter() {
        if (gShipmentsShippedAdapter == null) {
            gShipmentsShippedAdapter = new cShipmentAdapter();
        }
        return gShipmentsShippedAdapter;
    }

    public static cShipmentAdapter gShipmentsTotalAdapter;
    public static cShipmentAdapter getShipmentsTotalAdapter() {
        if (gShipmentsTotalAdapter == null) {
            gShipmentsTotalAdapter = new cShipmentAdapter();
        }
        return gShipmentsTotalAdapter;
    }

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


            if (shippingAgent.getShippintAgentStr().equalsIgnoreCase(firstPickLine.getShippingAgentCodeStr())) {
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



    //End Region Public Properties

    //Region Constructor

    public cShipment(String pvSourceNoStr) {
        this.sourceNoStr = pvSourceNoStr;
        this.packAndShipLineObl = new ArrayList<>();
        this.handledBln = false;
        this.quantityDbl = Double.valueOf(0);
        this.processingSequenceStr = "";

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

    public static cShipment pGetShipmentWithScannedBarcode(String pvScannedBarcodeStr){

        if (pvScannedBarcodeStr.isEmpty()) {
            return  null;
        }


        if (cRegex.hasPrefix(pvScannedBarcodeStr)) {
            pvScannedBarcodeStr = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);
        }

        List<cShipment> hulpObl = cPickorder.currentPickOrder.pGetNotHandledShipmentsObl();

        if (hulpObl== null || hulpObl.size() == 0) {
            return  null;
        }

        for (cShipment shipment : hulpObl) {
            if (shipment.getSourceNoStr().equalsIgnoreCase(pvScannedBarcodeStr) || shipment.getProcessingSequenceStr().equalsIgnoreCase(pvScannedBarcodeStr)) {
                               return  shipment;
            }
        }

        return  null;

    }

    public static cShipment pGetShipmentWithScannedArticleBarcode(String pvScannedBarcodeStr){

        //If scanned value is empty, we are done
        if (pvScannedBarcodeStr.isEmpty()) {
            return  null;
        }

        //If there are no shipments to handle, we are done
        List<cPickorderLinePackAndShip> hulpObl = cPickorder.currentPickOrder.pGetPackAndShipLinesNotHandledFromDatabasObl();
        if (hulpObl== null || hulpObl.size() == 0) {
            return  null;
        }

        //Check all barcodes for scanned value
        for (cPickorderBarcode pickorderBarcode : cPickorder.currentPickOrder.barcodesObl() ) {

            //We found a match in barcodes of the pickorder
            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvScannedBarcodeStr) || pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvScannedBarcodeStr)) {

                //Get not handled line for this ItemNo and VariantCoce
              for  (cPickorderLinePackAndShip  pickorderLinePackAndShip : hulpObl) {

                  //We found a match on ItemNo and VariantCode
                  if (pickorderLinePackAndShip.getItemNoStr().equalsIgnoreCase(pickorderBarcode.getItemNoStr()) && pickorderLinePackAndShip.getVariantCodeStr().equalsIgnoreCase(pickorderBarcode.getVariantcodeStr())) {

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
        cWebresult webresult =  cPickorder.getPickorderViewModel().pPickorderSourceDocumentShippedViaWebserviceBln();

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
        cShipment.currentShipment.handledBln = true;
        return  result;
    }

    public void pAddPackAndShipLine(cPickorderLinePackAndShip pvPickorderLinePackAndShip){
        this.packAndShipLineObl.add((pvPickorderLinePackAndShip));
        this.quantityDbl += pvPickorderLinePackAndShip.getQuantityDbl();
        this.processingSequenceStr = pvPickorderLinePackAndShip.getProcessingSequenceStr();
    }

    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods


    //End Region Constructor

}
