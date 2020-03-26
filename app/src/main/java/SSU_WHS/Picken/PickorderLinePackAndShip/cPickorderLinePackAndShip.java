package SSU_WHS.Picken.PickorderLinePackAndShip;

import org.json.JSONObject;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;

public class cPickorderLinePackAndShip {

    //Region Public Properties


    private int lineNoInt;
    public int getLineNoInt() {
        return lineNoInt;
    }

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

    public static List<cPickorderLinePackAndShip> allPackAndShipLinesObl;

    //End Region Public Properties

    //Region Constructor

    public cPickorderLinePackAndShip(JSONObject pvJsonObject){

        cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity = new cPickorderLinePackAndShipEntity(pvJsonObject);

        this.lineNoInt =  pickorderLinePackAndShipEntity.getLineNoInt();

        this.itemNoStr = pickorderLinePackAndShipEntity.getItemNoStr();
        this.variantCodeStr = pickorderLinePackAndShipEntity.getVariantCodeStr();
        this.descriptionStr = pickorderLinePackAndShipEntity.getDescriptionStr();
        this.description2Str = pickorderLinePackAndShipEntity.getDescription2Str();
        this.vendorItemNoStr =   this.descriptionStr = pickorderLinePackAndShipEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr =   this.descriptionStr = pickorderLinePackAndShipEntity.getVendorItemDescriptionStr();

        this.quantityDbl =  pickorderLinePackAndShipEntity.getQuantityDbl();
        this.quantityHandledDbl = pickorderLinePackAndShipEntity.getQuantityHandledDbl();
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


    //End Region Constructor

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods

}
