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
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Pickorders.cPickorderViewModel;
import SSU_WHS.Webservice.cWebresult;
import nl.icsvertex.scansuite.R;

public class cShipment {

    //Region Public Properties

    public enum ShipmentModusEnu {
        Unknown,
        Ship,
        QC
    }

    public  ShipmentModusEnu currentShipmentModus = ShipmentModusEnu.Unknown;

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

    private List<cPickorderLinePackAndShip> packAndShipLineObl;

    public List<cPickorderLine> QCLinesObl;

    public  List<cPickorderLine> QCLinesToCheckObl(){

        List<cPickorderLine> resultObl = new ArrayList<>();

        if (this.QCLinesObl == null || this.QCLinesObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLine pickorderLine : this.QCLinesObl) {
            if (pickorderLine.getQuantityCheckedDbl() == 0) {
                resultObl.add(pickorderLine);
            }
        }

        return  resultObl;
    }

    public  List<cPickorderLine> QCLinesCheckedObl(){

        List<cPickorderLine> resultObl = new ArrayList<>();

        if (this.QCLinesObl == null || this.QCLinesObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLine pickorderLine : this.QCLinesObl) {
            if (pickorderLine.getQuantityCheckedDbl() > 0) {
                resultObl.add(pickorderLine);
            }
        }

        return  resultObl;


    }


    public static List<cShipment> allShipmentsObl;

    public static cShipment currentShipment;

    public cPickorderAddress pickorderAddress () {

        cPickorderAddress resultAddress;

        switch (this.currentShipmentModus) {
            case Ship:

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

            case QC:

                if (this.QCLinesObl == null || this.QCLinesObl.size() == 0) {
                    return  null;
                }

                if (cPickorder.currentPickOrder.adressesObl() == null || cPickorder.currentPickOrder.adressesObl().size() == 0) {
                    return  null;
                }

                for (cPickorderLine pickorderLine : this.QCLinesObl) {
                    for (cPickorderAddress pickorderAddress : cPickorder.currentPickOrder.adressesObl()) {
                        if (pickorderAddress.getAddrescodeStr().equalsIgnoreCase(pickorderLine.getDeliveryAdressCodeStr())) {
                            resultAddress = pickorderAddress;
                            return  resultAddress;
                        }
                    }
                }
        }



        return  null;

    }

    public cShippingAgent shippingAgent () {

        cShippingAgent resultShippingAgent;


        switch (this.currentShipmentModus) {
            case Ship:

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

            case QC:

                if (this.QCLinesObl == null || this.QCLinesObl.size() == 0) {
                    return  null;
                }

                if (cShippingAgent.allShippingAgentsObl == null || cShippingAgent.allShippingAgentsObl.size() == 0) {
                    return  null;
                }

                cPickorderLine firstQCLine = this.QCLinesObl.get(0);

                for (cShippingAgent shippingAgent : cShippingAgent.allShippingAgentsObl) {


                    if (shippingAgent.getShippintAgentStr().equalsIgnoreCase(firstQCLine.getShippingAgentCodeStr())) {
                        resultShippingAgent = shippingAgent;
                        return  resultShippingAgent;
                    }

                }

        }




        return  null;

    }

    public cShippingAgentService shippingAgentService () {

        cShippingAgentService resultShippingAgentService;


        switch (this.currentShipmentModus) {
            case Ship:

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

            case QC:

                if (this.QCLinesObl == null || this.QCLinesObl.size() == 0) {
                    return  null;
                }

                if (this.shippingAgent() == null || this.shippingAgent().shippingAgentServicesObl() == null || this.shippingAgent().shippingAgentServicesObl().size() == 0) {
                    return  null;
                }

                cPickorderLine firstQCLine = this.QCLinesObl.get(0);

                for (cShippingAgentService shippingAgentService : this.shippingAgent().shippingAgentServicesObl()) {


                    if (shippingAgentService.getShippingAgentStr().equalsIgnoreCase(firstQCLine.getShippingAgentCodeStr()) && shippingAgentService.getServiceStr().equalsIgnoreCase(firstQCLine.getShippingAgentServiceCode())  ) {
                        resultShippingAgentService = shippingAgentService;
                        return  resultShippingAgentService;
                    }

                }

        }



        return  null;

    }

    private cPickorderViewModel getPickorderViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
    }


    //End Region Public Properties

    //Region Constructor

    public cShipment(String pvSourceNoStr, ShipmentModusEnu pvShipmentModusEnu) {
        this.currentShipmentModus = pvShipmentModusEnu;
        this.sourceNoStr = pvSourceNoStr;
        this.packAndShipLineObl = new ArrayList<>();
        this.QCLinesObl = new ArrayList<>();
        this.handledBln = false;
        this.quantityDbl = (double) 0;
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

    public void pAddPackAndShipLine(cPickorderLinePackAndShip pvPickorderLinePackAndShip){
        this.packAndShipLineObl.add((pvPickorderLinePackAndShip));
        this.quantityDbl += pvPickorderLinePackAndShip.getQuantityDbl();
        this.processingSequenceStr = pvPickorderLinePackAndShip.getProcessingSequenceStr();
    }

    public void pAddQCLine(cPickorderLine pvPickorderLine){
        this.QCLinesObl.add((pvPickorderLine));
        this.quantityDbl += pvPickorderLine.getQuantityDbl();
        this.processingSequenceStr = pvPickorderLine.getProcessingSequenceStr();
    }

    public cResult pCheckShipmentRst(){

        cResult result = new cResult();
        result.resultBln = true;

        if (!cPickorder.currentPickOrder.isPackAndShipNeededBln()) {
            return  result;
        }

        if (this.currentShipment.shippingAgent() == null) {
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

    public cPickorderLine pGetQCLineNotHandledByBarcode(String pvScannedBarcodeStr) {

        if (cPickorder.currentPickOrder.barcodesObl() == null || cPickorder.currentPickOrder.barcodesObl().size() == 0)  {
            return  null;
        }


        if (this.QCLinesToCheckObl() == null || this.QCLinesToCheckObl().size()  == 0) {
            return  null;
        }

        cPickorderBarcode pickorderBarcodeMatched = null;

        for (cPickorderBarcode pickorderBarcode : cPickorder.currentPickOrder.barcodesObl()) {
            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvScannedBarcodeStr) || pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvScannedBarcodeStr)){
                pickorderBarcodeMatched = pickorderBarcode;
                break;
            }
        }

        if (pickorderBarcodeMatched  == null) {
            return  null;
        }

        for (cPickorderLine pickorderLine : this.QCLinesToCheckObl()) {
            if (pickorderLine.getItemNoStr().equalsIgnoreCase(pickorderBarcodeMatched.getItemNoStr()) &&
               pickorderLine.getVariantCodeStr().equalsIgnoreCase((pickorderBarcodeMatched.getVariantcodeStr()))) {
                cPickorder.currentPickOrder.pickorderQCBarcodeScanned = pickorderBarcodeMatched;
               return pickorderLine;
            }
        }

        return null;
    }


    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods


    //End Region Constructor

}
