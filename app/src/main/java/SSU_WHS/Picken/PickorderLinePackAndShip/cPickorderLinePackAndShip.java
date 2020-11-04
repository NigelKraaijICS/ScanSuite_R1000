package SSU_WHS.Picken.PickorderLinePackAndShip;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ICS.Utils.cResult;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cPickorderLinePackAndShip {

    //Region Public Properties


    private int lineNoInt;
    public int getLineNoInt() {
        return lineNoInt;
    }

    private int lineNoTakeInt;
    public int getLineNoTakeInt() {
        return lineNoTakeInt;
    }

    private String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    private String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public  String getItemNoAndVariantStr(){
        return  this.getItemNoStr() + " " + this.getVariantCodeStr();
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

    private String destinationNoStr;
    public String getDestinationNoStr() {
        return destinationNoStr;
    }

    private String deliveryAdressCodeStr;
    public String getDeliveryAdressCodeStr() {
        return deliveryAdressCodeStr;
    }

    private String processingSequenceStr;
    public String getProcessingSequenceStr() {
        return processingSequenceStr;
    }

    public int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    private int statusShippingInt;
    private int getStatusShippingInt() {
        return statusShippingInt;
    }

    public int localStatusInt;
    public int getLocalStatusInt() {
        return localStatusInt;
    }

    private String shippingAgentCodeStr;
    public String getShippingAgentCodeStr() {
        return shippingAgentCodeStr;
    }

    private String shippingAgentServiceCodeStr;
    public String getShippingAgentServiceCodeStr() {
        return shippingAgentServiceCodeStr;
    }

    public double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    public double quantityCheckedDbl;
    public Double getQuantityCheckedDbl() {
        return quantityCheckedDbl;
    }

    public static List<cPickorderLinePackAndShip> allPackAndShipLinesObl;
    public static cPickorderLinePackAndShip currentPickorderLinePackAndShip;

    public  List<cPickorderBarcode> barcodesObl;
    public List<cPickorderLineBarcode> handledBarcodesObl(){


        List<cPickorderLineBarcode> resultObl;
        resultObl = new ArrayList<>();

        if (cPickorderLineBarcode.allLineBarcodesObl == null) {
            return  resultObl;
        }

        for (cPickorderLineBarcode pickorderLineBarcode :cPickorderLineBarcode.allLineBarcodesObl ) {

            int result = Long.compare(pickorderLineBarcode.getLineNoLng(), this.getLineNoInt());
            if (result == 0) {
                resultObl.add(pickorderLineBarcode);
            }
        }


        return  resultObl;
    }

    private cPickorderLinePackAndShipViewModel getPickorderLinePackAndShipViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLinePackAndShipViewModel.class);
    }

    //End Region Public Properties

    //Region Constructor

    public cPickorderLinePackAndShip(JSONObject pvJsonObject){

        cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity = new cPickorderLinePackAndShipEntity(pvJsonObject);

        this.lineNoInt =  pickorderLinePackAndShipEntity.getLineNoInt();
        this.lineNoTakeInt = pickorderLinePackAndShipEntity.getLineNoTakeInt();

        this.itemNoStr = pickorderLinePackAndShipEntity.getItemNoStr();
        this.variantCodeStr = pickorderLinePackAndShipEntity.getVariantCodeStr();
        this.descriptionStr = pickorderLinePackAndShipEntity.getDescriptionStr();
        this.description2Str = pickorderLinePackAndShipEntity.getDescription2Str();
        this.vendorItemNoStr =   this.descriptionStr = pickorderLinePackAndShipEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr =   this.descriptionStr = pickorderLinePackAndShipEntity.getVendorItemDescriptionStr();

        this.quantityDbl =  pickorderLinePackAndShipEntity.getQuantityDbl();
        this.quantityHandledDbl = pickorderLinePackAndShipEntity.getQuantityHandledDbl();
        this.quantityCheckedDbl = pickorderLinePackAndShipEntity.getQuantityChecked();

        this.statusInt = pickorderLinePackAndShipEntity.getStatusInt();
        this.statusShippingInt =  pickorderLinePackAndShipEntity.getStatusShippingInt();

        if (this.getStatusShippingInt()== 90) {
            this.localStatusInt = cWarehouseorder.PackingAndShippingStatusEnu.NotNeeded;
        }

        this.localStatusInt = pickorderLinePackAndShipEntity.getLocalStatusInt();

        this.sourceNoStr = pickorderLinePackAndShipEntity.getSourceNoStr();
        this.destinationNoStr = pickorderLinePackAndShipEntity.getDestinationNoStr();

        this.deliveryAdressCodeStr = pickorderLinePackAndShipEntity.getDeliveryAdressCodeStr();
        this.processingSequenceStr = pickorderLinePackAndShipEntity.getProcessingSequenceStr();

        this.shippingAgentCodeStr = pickorderLinePackAndShipEntity.getShippinAgentCodeStr();
        this.shippingAgentServiceCodeStr = pickorderLinePackAndShipEntity.getShippinAgentServiceCodeStr();

    }

    public boolean pCheckedBln() {

        cWebresult WebResult;
        WebResult =  this.getPickorderLinePackAndShipViewModel().pLineCheckedViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public void pAddOrUpdateLineBarcode(Double pvAmountDbl){

        boolean addBarcodeBln = false;

        //If there are no line barcodes, then simply add this one
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            addBarcodeBln = true;
        }

        if (!addBarcodeBln) {
            for (cPickorderLineBarcode pickorderLineBarcode : this.handledBarcodesObl()  ) {

                if (pickorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr())) {
                    pickorderLineBarcode.quantityHandledDbl += pvAmountDbl;
                    pickorderLineBarcode.pUpdateAmountInDatabase();
                    return;
                }
            }
        }

        cPickorderLineBarcode pickorderLineBarcode = new cPickorderLineBarcode((long) cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getLineNoInt(), cPickorderBarcode.currentPickorderBarcode.getBarcodeStr());
        pickorderLineBarcode.quantityHandledDbl = pvAmountDbl;
        pickorderLineBarcode.pInsertInDatabaseBln();

    }

    public void pRemoveOrUpdateLineBarcode(){

        //If there are no line barcodes, this should not be possible
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            return;
        }

        for (cPickorderLineBarcode pickorderLineBarcode : this.handledBarcodesObl()  ) {

            if (pickorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr())) {

                pickorderLineBarcode.quantityHandledDbl -= cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl();

                if (pickorderLineBarcode.getQuantityhandledDbl() > 0) {
                    pickorderLineBarcode.pUpdateAmountInDatabase();
                    return;
                }

                pickorderLineBarcode.pDeleteFromDatabaseBln();
                return;
            }
        }
    }


    public cResult pLineBusyRst(){

        cResult result = new cResult();
        result.resultBln = false;

        if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip.mGetBarcodesObl() == null || Objects.requireNonNull(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.mGetBarcodesObl()).size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.no_barcodes_availabe_for_this_line));
            return result;
        }


        result.resultBln = true;

        return  result;

    }

    private  List<cPickorderBarcode> mGetBarcodesObl(){

        //If barcodes already filled, then we are done
        if (this.barcodesObl != null) {
            return this.barcodesObl;
        }

        // Get barcodes via PickorderBarcode class
        this.barcodesObl = cPickorderBarcode.pGetPickbarcodesViaVariantAndItemNoObl(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.barcodesObl == null || this.barcodesObl.size() == 0) {
            return  null;
        }

        return  this.barcodesObl;
    }


    //End Region Constructor

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods

}
