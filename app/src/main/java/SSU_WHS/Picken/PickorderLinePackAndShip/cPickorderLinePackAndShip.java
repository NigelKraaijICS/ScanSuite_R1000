package SSU_WHS.Picken.PickorderLinePackAndShip;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cDateAndTime;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.Shipment.cShipment;
import ICS.cAppExtension;

public class cPickorderLinePackAndShip {

    //Region Public Properties


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

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    public String vendorItemNoStr;
    public String getVendorItemNoStr() {
        return vendorItemNoStr;
    }

    public String vendorDescriptionNoStr;
    public String getVendorDescriptionNoStr() {
        return vendorDescriptionNoStr;
    }

    public String component10NoStr;
    public String getComponent10NoStr() {
        return component10NoStr;
    }

    public String sourceNoStr;
    public String getSourceNoStr() {
        return sourceNoStr;
    }

    public String destinationNoStr;
    public String getDestinationNoStr() {
        return destinationNoStr;
    }

    public boolean storeSourceNoBln;
    public boolean isStoreSourceNoBln() {
        return storeSourceNoBln;
    }

    public String deliveryAdressTypeStr;
    public String getDeliveryAdressTypeStr() {
        return deliveryAdressTypeStr;
    }

    public String deliveryAdressCodeStr;
    public String getDeliveryAdressCodeStr() {
        return deliveryAdressCodeStr;
    }


    public String processingSequenceStr;
    public String getProcessingSequenceStr() {
        return processingSequenceStr;
    }

    public boolean showOnTerminalBln;
    public boolean isShowOnTerminalBln() {
        return showOnTerminalBln;
    }

    public int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    public int statusShippingInt;
    public int getStatusShippingInt() {
        return statusShippingInt;
    }

    public int statusPackingInt;
    public int getStatusPackingInt() {
        return statusPackingInt;
    }

    public int localStatusInt;
    public int getLocalStatusInt() {
        return localStatusInt;
    }

    public String shippingAgentCodeStr;
    public String getShippingAgentCodeStr() {
        return shippingAgentCodeStr;
    }

    public String shippingAgentServiceCodeStr;
    public String getShippingAgentServiceCodeStr() {
        return shippingAgentServiceCodeStr;
    }

    public String actualShippingAgentCodeStr;
    public String getActualShippingAgentCodeStr() {
        return actualShippingAgentCodeStr;
    }

    public String actualShippingAgentServiceCodeStr;
    public String getActualShippingAgentServiceCodeStr() {
        return actualShippingAgentServiceCodeStr;
    }

    public double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    public cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity;
    public boolean inDatabaseBln;

    public static cPickorderLinePackAndShipViewModel gPickorderLinePackAndShipViewModel;
    public static cPickorderLinePackAndShipViewModel getPickorderLinePackAndShipViewModel() {
        if (gPickorderLinePackAndShipViewModel == null) {
            gPickorderLinePackAndShipViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cPickorderLinePackAndShipViewModel.class);
        }
        return gPickorderLinePackAndShipViewModel;
    }

    public static List<cPickorderLinePackAndShip> allPackAndShipLinesObl;
    public static cPickorderLinePackAndShip currentPackAndShipLine;

    //End Region Public Properties

    //Region Constructor

    public cPickorderLinePackAndShip(JSONObject pvJsonObject){

        this.pickorderLinePackAndShipEntity = new cPickorderLinePackAndShipEntity(pvJsonObject);

        this.lineNoInt =  this.pickorderLinePackAndShipEntity.getLineNoInt();
        this.showOnTerminalBln = cText.stringToBoolean(this.pickorderLinePackAndShipEntity.getShowOnTerminalStr(), false);
        this.itemNoStr = this.pickorderLinePackAndShipEntity.getItemNoStr();
        this.variantCodeStr = this.pickorderLinePackAndShipEntity.getVariantCodeStr();
        this.descriptionStr = this.pickorderLinePackAndShipEntity.getDescriptionStr();
        this.description2Str = this.pickorderLinePackAndShipEntity.getDescription2Str();
        this.vendorItemNoStr =   this.descriptionStr = this.pickorderLinePackAndShipEntity.getVendorItemNoStr();
        this.vendorDescriptionNoStr =   this.descriptionStr = this.pickorderLinePackAndShipEntity.getVendorItemDescriptionStr();
        this.component10NoStr =   this.descriptionStr = this.pickorderLinePackAndShipEntity.getComponent10Str();

        this.quantityDbl =  this.pickorderLinePackAndShipEntity.getQuantityDbl();
        this.quantityHandledDbl = this.pickorderLinePackAndShipEntity.getQuantityHandledDbl();
        this.statusInt = this.pickorderLinePackAndShipEntity.getStatusInt();
        this.statusShippingInt =  this.pickorderLinePackAndShipEntity.getStatusShippingInt();
        this.statusPackingInt= this.pickorderLinePackAndShipEntity.getStatusPackingInt();
        this.localStatusInt = pickorderLinePackAndShipEntity.getLocalStatusInt();

        this.sourceNoStr = this.pickorderLinePackAndShipEntity.getSourceNoStr();
        this.destinationNoStr = this.pickorderLinePackAndShipEntity.getDestinationNoStr();
        this.storeSourceNoBln = cText.stringToBoolean(this.pickorderLinePackAndShipEntity.getStoreSourceNoStr(), false);
        this.deliveryAdressTypeStr = this.pickorderLinePackAndShipEntity.getDeliveryAdressTypeStr();
        this.deliveryAdressCodeStr = this.pickorderLinePackAndShipEntity.getDeliveryAdressCodeStr();
        this.processingSequenceStr = this.pickorderLinePackAndShipEntity.getProcessingSequenceStr();

        this.shippingAgentCodeStr = this.pickorderLinePackAndShipEntity.getShippinAgentCodeStr();
        this.shippingAgentServiceCodeStr = this.pickorderLinePackAndShipEntity.getShippinAgentServiceCodeStr();
        this.actualShippingAgentCodeStr =this.pickorderLinePackAndShipEntity.getActualShippinAgentCodeStr();
        this.actualShippingAgentServiceCodeStr = this.pickorderLinePackAndShipEntity.getActualShippinAgentServiceCodeStr();


    }

    public cPickorderLinePackAndShip(cPickorderLinePackAndShipEntity pvPickorderLinePackAndShipEntity){

        this.pickorderLinePackAndShipEntity =pvPickorderLinePackAndShipEntity;

        this.lineNoInt =  this.pickorderLinePackAndShipEntity.getLineNoInt();
        this.showOnTerminalBln = cText.stringToBoolean(this.pickorderLinePackAndShipEntity.getShowOnTerminalStr(), false);
        this.itemNoStr = this.pickorderLinePackAndShipEntity.getItemNoStr();
        this.variantCodeStr = this.pickorderLinePackAndShipEntity.getVariantCodeStr();
        this.descriptionStr = this.pickorderLinePackAndShipEntity.getDescriptionStr();
        this.description2Str = this.pickorderLinePackAndShipEntity.getDescription2Str();
        this.vendorItemNoStr =   this.descriptionStr = this.pickorderLinePackAndShipEntity.getVendorItemNoStr();
        this.vendorDescriptionNoStr =   this.descriptionStr = this.pickorderLinePackAndShipEntity.getVendorItemDescriptionStr();
        this.component10NoStr =   this.descriptionStr = this.pickorderLinePackAndShipEntity.getComponent10Str();

        this.quantityDbl =  this.pickorderLinePackAndShipEntity.getQuantityDbl();
        this.quantityHandledDbl = this.pickorderLinePackAndShipEntity.getQuantityHandledDbl();
        this.statusInt = this.pickorderLinePackAndShipEntity.getStatusInt();
        this.statusShippingInt =  this.pickorderLinePackAndShipEntity.getStatusShippingInt();
        this.statusPackingInt= this.pickorderLinePackAndShipEntity.getStatusPackingInt();
        this.localStatusInt = pickorderLinePackAndShipEntity.getLocalStatusInt();

        this.sourceNoStr = this.pickorderLinePackAndShipEntity.getSourceNoStr();
        this.destinationNoStr = this.pickorderLinePackAndShipEntity.getDestinationNoStr();
        this.storeSourceNoBln = cText.stringToBoolean(this.pickorderLinePackAndShipEntity.getStoreSourceNoStr(), false);
        this.deliveryAdressTypeStr = this.pickorderLinePackAndShipEntity.getDeliveryAdressTypeStr();
        this.deliveryAdressCodeStr = this.pickorderLinePackAndShipEntity.getDeliveryAdressCodeStr();
        this.processingSequenceStr = this.pickorderLinePackAndShipEntity.getProcessingSequenceStr();

        this.shippingAgentCodeStr = this.pickorderLinePackAndShipEntity.getShippinAgentCodeStr();
        this.shippingAgentServiceCodeStr = this.pickorderLinePackAndShipEntity.getShippinAgentServiceCodeStr();
        this.actualShippingAgentCodeStr =this.pickorderLinePackAndShipEntity.getActualShippinAgentCodeStr();
        this.actualShippingAgentServiceCodeStr = this.pickorderLinePackAndShipEntity.getActualShippinAgentServiceCodeStr();
    }


    //End Region Constructor

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods

}
