package SSU_WHS.PackAndShip.PackAndShipShipment;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cResult;
import ICS.cAppExtension;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddress;
import SSU_WHS.Webservice.cWebresult;
import nl.icsvertex.scansuite.R;

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

    public cShippingAgent shippingAgent() {
        return   cShippingAgent.pGetShippingAgentByCodeStr(this.getShippingAgentCodeStr());
    }

    public String getShippingAgentCodeDescriptionStr() {



        if (this.shippingAgent() != null) {
            return  shippingAgent().getDescriptionStr();
        }

        return shippingAgentCodeStr;
    }

    public String shippingAgentServiceCodeStr;
    public String getShippingAgentServiceCodeStr() {
        return shippingAgentServiceCodeStr;
    }

    public  cShippingAgentService shippingAgentService() {
        return  cShippingAgentService.pGetShippingAgentServiceByCodeStr(this.getShippingAgentCodeStr(), this.getShippingAgentServiceCodeStr());
    }

    public String getShippingAgentServiceCodeDescriptionStr() {


        if (this.shippingAgentService() != null) {
            return  this.shippingAgentService() .getDescriptionStr();
        }

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

    public boolean isShippingBln() {

        if (this.getShippingLabelsStr().equalsIgnoreCase("NONE")) {
            return  false;
        }

        return  true;

    }



    private cPackAndShipShipmentEntity packAndShipShipmentEntity;

    public cPackAndShipAddress deliveryAddress() {

        if (cPackAndShipAddress.allAddressesObl == null || cPackAndShipAddress.allAddressesObl.size() == 0) {
            return  null;
        }

        if ( this.getDeliveryAddressCodeStr() == null ||  getDeliveryAddressCodeStr().isEmpty()) {
            return cPackAndShipAddress.allAddressesObl.get(0);
        }

        for (cPackAndShipAddress packAndShipAddress : cPackAndShipAddress.allAddressesObl) {
            if (packAndShipAddress.getAddressCodeStr().equalsIgnoreCase(this.getDeliveryAddressCodeStr())) {
                return packAndShipAddress;
            }
        }

        return  null;

    }

    public static List<cPackAndShipShipment> allShipmentsObl;
    public static cPackAndShipShipment currentShipment;

    private cPackAndShipShipmentViewModel getPackAndShipShipmentViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipShipmentViewModel.class);
    }


    //Region Public Properties


    //End Region Public Properties

    //Region Constructor
    public cPackAndShipShipment(JSONObject pvJsonObject, boolean pvViaDocumentBln) {
        this.packAndShipShipmentEntity = new cPackAndShipShipmentEntity(pvJsonObject, pvViaDocumentBln);

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

    public cResult pHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult webresult;

        webresult =  this.getPackAndShipShipmentViewModel().pHandledViaWebserviceWrs();

        //No result, so something really went wrong
        if (webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Everything was fine, so we are done
        if (webresult.getSuccessBln() && webresult.getResultBln()) {
            result.resultBln = true;
            return result;
        }

        //Something really went wrong
        if (!webresult.getSuccessBln()) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        // We got a succesfull response, but we need to do something with this activity
        return  result;

    }

    public cResult pShipViaWebserviceRst( List<cShippingAgentServiceShippingUnit> pvShippingAgentServiceShippingUnitsObl) {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult webresult;

        webresult =  this.getPackAndShipShipmentViewModel().pShipViaWebserviceWrs(pvShippingAgentServiceShippingUnitsObl);

        //No result, so something really went wrong
        if (webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Everything was fine, so we are done
        if (webresult.getSuccessBln() && webresult.getResultBln()) {
            result.resultBln = true;
            return result;
        }

        //Something really went wrong
        if (!webresult.getSuccessBln()) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        // We got a succesfull response, but we need to do something with this activity
        return  result;

    }


}
