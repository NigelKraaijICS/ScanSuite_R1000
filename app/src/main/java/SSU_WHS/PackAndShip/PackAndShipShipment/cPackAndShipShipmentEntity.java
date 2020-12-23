package SSU_WHS.PackAndShip.PackAndShipShipment;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_PACKANDSHIPSHIPMENT)
public class cPackAndShipShipmentEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceNoStr;
    public String getSourceNoStr() {return this.sourceNoStr;}

    @ColumnInfo(name = cDatabase.SHIPPINGLABELS_NAMESTR)
    public String shippingLabelsStr;
    public String getShippingLabelsStr() {return this.shippingLabelsStr;}

    @ColumnInfo(name = cDatabase.SHIPPINGAGENTCODE_NAMESTR)
    public String shippingAgentCodeStr;
    public String getShippingAgentCodeStr() {return this.shippingAgentCodeStr;}

    @ColumnInfo(name = cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR)
    public String shippingAgentServiceCodeStr;
    public String getShippingAgentServiceCodeStr() {return this.shippingAgentServiceCodeStr;}

    @ColumnInfo(name = cDatabase.ACTUALSHIPPINGAGENTCODE_NAMESTR)
    public String actualShippingAgentCodeStr;
    public String getActualShippingAgentCodeStr() {return this.actualShippingAgentCodeStr;}

    @ColumnInfo(name = cDatabase.ACTUEALSHIPPINGAGENTSERVICECODE_NAMESTR)
    public String actualShippingAgentServiceCodeStr;
    public String getActualShippingAgentServiceCodeStr() {return this.actualShippingAgentServiceCodeStr;}

    @ColumnInfo(name = cDatabase.SHIPPINGADDRESSTYPE_NAMESTR)
    public String shippingAddressTypeStr;
    public String getShippingAddressTypeStr() {return this.shippingAddressTypeStr;}

    @ColumnInfo(name = cDatabase.SHIPPINGADDRESSCODE_NAMESTR)
    public String shippingAddressCodeStr;
    public String getShippingAddressCodeStr() {return this.shippingAddressCodeStr;}

    @ColumnInfo(name = cDatabase.DELIVERYADDRESSTYPE_NAMESTR)
    public String deliveryAddressTypeStr;
    public String getDeliveryAddressTypeStr() {return this.deliveryAddressTypeStr;}

    @ColumnInfo(name = cDatabase.DELIVERYADDRESSCODE_NAMESTR)
    public String deliveryAddressCodeStr;
    public String getDeliveryAddressCodeStr() {return this.deliveryAddressCodeStr;}

    @ColumnInfo(name = cDatabase.SENDERADDRESSCODE_NAMESTR)
    public String senderAddressCodeStr;
    public String getSenderAddressCodeStr() {return this.senderAddressCodeStr;}

    @ColumnInfo(name = cDatabase.RETURNADDRESSCODE_NAMESTR)
    public String returnAddressCodeStr;
    public String getReturnAddressCodeStr() {return this.returnAddressCodeStr;}

    @ColumnInfo(name = cDatabase.RETURNSENDERADDRESSCODE_NAMESTR)
    public String returnSenderAddressCodeStr;
    public String getReturnSenderAddressCodeStr() {return this.returnSenderAddressCodeStr;}

    @ColumnInfo(name = cDatabase.RETURNSHIPPINGADDRESSCODE_NAMESTR)
    public String returnShippingAddressCodeStr;
    public String getReturnShippingAddressCodeStr() {return this.returnShippingAddressCodeStr;}

    @ColumnInfo(name = cDatabase.BILLINGADDRESSCODE_NAMESTR)
    public String billingAddressCodeStr;
    public String getBillingAddressCodeStr() {return this.billingAddressCodeStr;}

    @ColumnInfo(name = cDatabase.STATUSSHIPPING_NAMESTR)
    public int statusShippingInt;
    public int getStatusShippingInt() {return this.statusShippingInt;}

    @ColumnInfo(name = cDatabase.STATUSPACKING_NAMESTR)
    public int statusPackingInt;
    public int getStatusPackingInt() {return this.statusPackingInt;}

    //empty constructor
    public cPackAndShipShipmentEntity() {

    }

    public cPackAndShipShipmentEntity(JSONObject pvJsonObject, boolean pvViaDocumentBln) {
        try {

            if (!pvViaDocumentBln) {
                this.sourceNoStr = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
                this.shippingLabelsStr = pvJsonObject.getString(cDatabase.SHIPPINGLABELS_NAMESTR);
                this.shippingAgentCodeStr = pvJsonObject.getString(cDatabase.SHIPPINGAGENTCODE_NAMESTR);
                this.shippingAgentServiceCodeStr = pvJsonObject.getString(cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR);
                this.actualShippingAgentCodeStr =  pvJsonObject.getString(cDatabase.ACTUALSHIPPINGAGENTCODE_NAMESTR);
                this.actualShippingAgentServiceCodeStr = pvJsonObject.getString(cDatabase.ACTUEALSHIPPINGAGENTSERVICECODE_NAMESTR);
                this.shippingAddressTypeStr = pvJsonObject.getString(cDatabase.SHIPPINGADDRESSTYPE_NAMESTR);
                this.shippingAddressCodeStr = pvJsonObject.getString(cDatabase.SHIPPINGADDRESSCODE_NAMESTR);
                this.deliveryAddressTypeStr = pvJsonObject.getString(cDatabase.DELIVERYADDRESSTYPE_NAMESTR);
                this.deliveryAddressCodeStr = pvJsonObject.getString(cDatabase.DELIVERYADDRESSCODE_NAMESTR);
                this.senderAddressCodeStr = pvJsonObject.getString(cDatabase.SENDERADDRESSCODE_NAMESTR);
                this.returnAddressCodeStr = pvJsonObject.getString(cDatabase.RETURNADDRESSCODE_NAMESTR);
                this.returnSenderAddressCodeStr = pvJsonObject.getString(cDatabase.RETURNSENDERADDRESSCODE_NAMESTR);
                this.returnShippingAddressCodeStr = pvJsonObject.getString(cDatabase.RETURNSHIPPINGADDRESSCODE_NAMESTR);
                this.billingAddressCodeStr =  pvJsonObject.getString(cDatabase.BILLINGADDRESSCODE_NAMESTR);
                this.statusShippingInt = pvJsonObject.getInt(cDatabase.STATUSSHIPPING_NAMESTR);
                this.statusPackingInt = pvJsonObject.getInt(cDatabase.STATUSPACKING_NAMESTR);
            }
            else
            {
                this.sourceNoStr = pvJsonObject.getString(cDatabase.SOURCENO_NAMESTR);
                this.shippingLabelsStr = pvJsonObject.getString(cDatabase.SHIPPINGLABELS_NAMESTR);
                this.shippingAgentCodeStr = pvJsonObject.getString(cDatabase.SHIPPINGAGENTCODE_NAMESTR);
                this.shippingAgentServiceCodeStr = pvJsonObject.getString(cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR);
                this.actualShippingAgentCodeStr ="";
                this.actualShippingAgentServiceCodeStr = "";
                this.shippingAddressTypeStr = pvJsonObject.getString(cDatabase.SHIPPINGADDRESSTYPE_NAMESTR);
                this.shippingAddressCodeStr = pvJsonObject.getString(cDatabase.SHIPPINGADDRESSCODE_NAMESTR);
                this.deliveryAddressTypeStr = pvJsonObject.getString(cDatabase.DELIVERYADDRESSTYPE_NAMESTR);
                this.deliveryAddressCodeStr = pvJsonObject.getString(cDatabase.DELIVERYADDRESSCODE_NAMESTR);
                this.senderAddressCodeStr = pvJsonObject.getString(cDatabase.SENDERADDRESSCODE_NAMESTR);
                this.returnAddressCodeStr = pvJsonObject.getString(cDatabase.RETURNADDRESSCODE_NAMESTR);
                this.returnSenderAddressCodeStr = pvJsonObject.getString(cDatabase.RETURNSENDERADDRESSCODE_NAMESTR);
                this.returnShippingAddressCodeStr = pvJsonObject.getString(cDatabase.RETURNSHIPPINGADDRESSCODE_NAMESTR);
                this.billingAddressCodeStr =  pvJsonObject.getString(cDatabase.BILLINGADDRESSCODE_NAMESTR);
                this.statusShippingInt =0;
                this.statusPackingInt = 0;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
