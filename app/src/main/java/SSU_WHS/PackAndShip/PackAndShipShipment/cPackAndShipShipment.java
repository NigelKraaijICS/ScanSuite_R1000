package SSU_WHS.PackAndShip.PackAndShipShipment;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingViewModel;

public class cPackAndShipShipment {

    public String sourceNoStr;
    public String getSourceNoStr() { return sourceNoStr; }

    public String shippingLabelsStr;
    public String getShippingLabelsStr() {
        return shippingLabelsStr;
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
    public String getActualShippingAgentServiceCodeStr() { return actualShippingAgentServiceCodeStr; }

    public String shippingAddressTypeStr;
    public String getShippingAddressTypeStr() {
        return shippingAddressTypeStr;
    }

    public String shippingAddressCodeStr;
    public String getShippingAddressCodeStr() {
        return shippingAddressCodeStr;
    }

    public String deliveryAddressTypeStr;
    public String getDeliveryAddressTypeStr() {
        return deliveryAddressTypeStr;
    }

    public String deliveryAddressCodeStr;
    public String getDeliveryAddressCodeStr() {
        return deliveryAddressCodeStr;
    }

    public String senderAddressCodeStr;
    public String getSenderAddressCodeStr() {
        return senderAddressCodeStr;
    }

    public String returnAddressCodeStr;
    public String getReturnAddressCodeStr() {
        return returnAddressCodeStr;
    }

    public String returnSenderAddressCodeStr;
    public String getReturnSenderAddressCodeStr() {
        return returnSenderAddressCodeStr;
    }

    public String returnShippingAddressCodeStr;
    public String getReturnShippingAddressCodeStr() {
        return returnShippingAddressCodeStr;
    }

    public String billingAddressCodeStr;
    public String getBillingAddressCodeStr() {
        return billingAddressCodeStr;
    }

    public int statusShippingInt;
    public int getStatusShippingInt() {
        return statusShippingInt;
    }

    public int statusPackingInt;
    public int getStatusPackingInt() {
        return statusPackingInt;
    }

    private cPackAndShipShipmentEntity packAndShipShipmentEntity;

    public static List<cPackAndShipShipment> allShipmentsObl;
    public static cPackAndShipShipment currentShipment;

    private cPackAndShipShipmentViewModel getPackAndShipShipmentViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipShipmentViewModel.class);
    }


    //Region Public Properties


    //End Region Public Properties

    //Region Constructor
    public cPackAndShipShipment(JSONObject pvJsonObject) {
        this.packAndShipShipmentEntity = new cPackAndShipShipmentEntity(pvJsonObject);

        this.sourceNoStr = this.packAndShipShipmentEntity.getSourceNoStr();
        this.shippingLabelsStr = this.packAndShipShipmentEntity.getShippingLabelsStr();
        this.shippingAgentCodeStr = this.packAndShipShipmentEntity.getShippingAgentCodeStr();
        this.shippingAgentServiceCodeStr = this.packAndShipShipmentEntity.getShippingAgentServiceCodeStr();
        this.actualShippingAgentCodeStr =  this.packAndShipShipmentEntity.getActualShippingAgentCodeStr();
        this.actualShippingAgentServiceCodeStr =  this.packAndShipShipmentEntity.getActualShippingAgentServiceCodeStr();
        this.shippingAddressTypeStr =  this.packAndShipShipmentEntity.getShippingAddressTypeStr();
        this.shippingAddressCodeStr =  this.packAndShipShipmentEntity.getShippingAddressCodeStr();
        this.deliveryAddressTypeStr =  this.packAndShipShipmentEntity.getDeliveryAddressTypeStr();
        this.deliveryAddressCodeStr =  this.packAndShipShipmentEntity.getDeliveryAddressCodeStr();
        this.senderAddressCodeStr =  this.packAndShipShipmentEntity.getSenderAddressCodeStr();
        this.returnAddressCodeStr =  this.packAndShipShipmentEntity.getReturnAddressCodeStr();
        this.returnSenderAddressCodeStr =  this.packAndShipShipmentEntity.getReturnSenderAddressCodeStr();
        this.returnShippingAddressCodeStr =  this.packAndShipShipmentEntity.getReturnShippingAddressCodeStr();
        this.billingAddressCodeStr =  this.packAndShipShipmentEntity.getBillingAddressCodeStr();
        this.statusShippingInt = this.packAndShipShipmentEntity.getStatusShippingInt();
        this.statusPackingInt = this.packAndShipShipmentEntity.getStatusPackingInt();
    }


    //End Region Constructor


    public boolean pInsertInDatabaseBln() {

        if (cPackAndShipShipment.allShipmentsObl == null){
            cPackAndShipShipment.allShipmentsObl = new ArrayList<>();
        }
        cPackAndShipShipment.allShipmentsObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cPackAndShipShipmentViewModel packAndShipSettingViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipShipmentViewModel.class);
        packAndShipSettingViewModel.deleteAll();
        return true;
    }

}
