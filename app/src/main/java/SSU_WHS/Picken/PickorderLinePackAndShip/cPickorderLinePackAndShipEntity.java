package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_PICKORDERLINEPACKANDSHIP)
public class cPickorderLinePackAndShipEntity {
    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
//    @PrimaryKey
//    @NonNull
    @ColumnInfo(name = cDatabase.LINENO_NAMESTR)
    public Integer lineno;
    @ColumnInfo(name = cDatabase.SHOWONTERMINAL_NAMESTR)
    public String showonterminal;
    @ColumnInfo(name = cDatabase.STATUS_NAMESTR)
    public int status;
    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceno;
    @ColumnInfo(name = cDatabase.DESTINATIONNO_NAMESTR)
    public String destinationno;
    @ColumnInfo(name = cDatabase.STORESOURCEORDER_NAMESTR)
    public String storesourceorder;
    @ColumnInfo(name = cDatabase.DELIVERYADDRESSTYPE_NAMESTR)
    public String deliveryaddresstype;
    @ColumnInfo(name = cDatabase.DELIVERYADDRESSCODE_NAMESTR)
    public String deliveryaddresscode;
    @ColumnInfo(name = cDatabase.PROCESSINGSEQUENCE_NAMESTR)
    public String processingsequence;
    @ColumnInfo(name = cDatabase.STATUSSHIPPING_NAMESTR)
    public int statusshipping;
    @ColumnInfo(name = cDatabase.STATUSPACKING_NAMESTR)
    public int statuspacking;
    @ColumnInfo(name = cDatabase.SHIPPINGAGENTCODE_NAMESTR)
    public String shippingagentcode;
    @ColumnInfo(name = cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR)
    public String shippingagentservicecode;
    @ColumnInfo(name = cDatabase.ACTUALSHIPPINGAGENTCODE_NAMESTR)
    public String actualshippingagentcode;
    @ColumnInfo(name = cDatabase.ACTUALSHIPPINGAGENTSERVICECODE_NAMESTR)
    public String actualshippingagentservicecode;
    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;
    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode;
    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String description;
    @ColumnInfo(name = cDatabase.DESCRIPTION2_NAMESTR)
    public String description2;
    @ColumnInfo(name = cDatabase.VENDORITEMNO_NAMESTR)
    public String vendoritemno;
    @ColumnInfo(name = cDatabase.VENDORITEMDESCRIPTION_NAMESTR)
    public String vendoritemdescription;
    @ColumnInfo(name = cDatabase.COMPONENT10_NAMESTR)
    public String component10;
    @ColumnInfo(name = cDatabase.QUANTITY_NAMESTR)
    public Double quantity;
    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public Double quantityhandled;
    @ColumnInfo(name = cDatabase.LOCALSTATUS_NAMESTR)
    public Integer localstatus;

    public Integer getRecordid() {
        return recordid;
    }

    public void setRecordid(Integer recordid) {
        this.recordid = recordid;
    }

    public Integer getLineno() {
        return lineno;
    }

    public void setLineno(Integer lineno) {
        this.lineno = lineno;
    }

    public String getShowonterminal() {
        return showonterminal;
    }

    public void setShowonterminal(String showonterminal) {
        this.showonterminal = showonterminal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSourceno() {
        return sourceno;
    }

    public void setSourceno(String sourceno) {
        this.sourceno = sourceno;
    }

    public String getDestinationno() {
        return destinationno;
    }

    public void setDestinationno(String destinationno) {
        this.destinationno = destinationno;
    }

    public String getStoresourceorder() {
        return storesourceorder;
    }

    public void setStoresourceorder(String storesourceorder) {
        this.storesourceorder = storesourceorder;
    }

    public String getDeliveryaddresstype() {
        return deliveryaddresstype;
    }

    public void setDeliveryaddresstype(String deliveryaddresstype) {
        this.deliveryaddresstype = deliveryaddresstype;
    }

    public String getDeliveryaddresscode() {
        return deliveryaddresscode;
    }

    public void setDeliveryaddresscode(String deliveryaddresscode) {
        this.deliveryaddresscode = deliveryaddresscode;
    }

    public String getProcessingsequence() {
        return processingsequence;
    }

    public void setProcessingsequence(String processingsequence) {
        this.processingsequence = processingsequence;
    }

    public int getStatusshipping() {
        return statusshipping;
    }

    public void setStatusshipping(int statusshipping) {
        this.statusshipping = statusshipping;
    }

    public int getStatuspacking() {
        return statuspacking;
    }

    public void setStatuspacking(int statuspacking) {
        this.statuspacking = statuspacking;
    }

    public String getShippingagentcode() {
        return shippingagentcode;
    }

    public void setShippingagentcode(String shippingagentcode) {
        this.shippingagentcode = shippingagentcode;
    }

    public String getShippingagentservicecode() {
        return shippingagentservicecode;
    }

    public void setShippingagentservicecode(String shippingagentservicecode) {
        this.shippingagentservicecode = shippingagentservicecode;
    }

    public String getActualshippingagentcode() {
        return actualshippingagentcode;
    }

    public void setActualshippingagentcode(String actualshippingagentcode) {
        this.actualshippingagentcode = actualshippingagentcode;
    }

    public String getActualshippingagentservicecode() {
        return actualshippingagentservicecode;
    }

    public void setActualshippingagentservicecode(String actualshippingagentservicecode) {
        this.actualshippingagentservicecode = actualshippingagentservicecode;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getVariantcode() {
        return variantcode;
    }

    public void setVariantcode(String variantcode) {
        this.variantcode = variantcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getVendoritemno() {
        return vendoritemno;
    }

    public void setVendoritemno(String vendoritemno) {
        this.vendoritemno = vendoritemno;
    }

    public String getVendoritemdescription() {
        return vendoritemdescription;
    }

    public void setVendoritemdescription(String vendoritemdescription) {
        this.vendoritemdescription = vendoritemdescription;
    }

    public String getComponent10() {
        return component10;
    }

    public void setComponent10(String component10) {
        this.component10 = component10;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getQuantityhandled() {
        return quantityhandled;
    }

    public void setQuantityhandled(Double quantityhandled) {
        this.quantityhandled = quantityhandled;
    }

    //empty constructor
    public cPickorderLinePackAndShipEntity() {

    }

    public cPickorderLinePackAndShipEntity(JSONObject jsonObject) {
        try {
            lineno = jsonObject.getInt(cDatabase.LINENO_NAMESTR);
            showonterminal = jsonObject.getString(cDatabase.SHOWONTERMINAL_NAMESTR);
            status = jsonObject.getInt(cDatabase.STATUS_NAMESTR);
            sourceno = jsonObject.getString(cDatabase.SOURCENO_NAMESTR);
            destinationno = jsonObject.getString(cDatabase.DESTINATIONNO_NAMESTR);
            storesourceorder = jsonObject.getString(cDatabase.STORESOURCEORDER_NAMESTR);
            deliveryaddresstype = jsonObject.getString(cDatabase.DELIVERYADDRESSTYPE_NAMESTR);
            deliveryaddresscode = jsonObject.getString(cDatabase.DELIVERYADDRESSCODE_NAMESTR);
            processingsequence = jsonObject.getString(cDatabase.PROCESSINGSEQUENCE_NAMESTR);
            statusshipping = jsonObject.getInt(cDatabase.STATUSSHIPPING_NAMESTR);
            statuspacking= jsonObject.getInt(cDatabase.STATUSPACKING_NAMESTR);
            shippingagentcode = jsonObject.getString(cDatabase.SHIPPINGAGENTCODE_NAMESTR);
            shippingagentservicecode = jsonObject.getString(cDatabase.SHIPPINGAGENTSERVICECODE_NAMESTR);
            actualshippingagentcode = jsonObject.getString(cDatabase.ACTUALSHIPPINGAGENTCODE_NAMESTR);
            actualshippingagentservicecode = jsonObject.getString(cDatabase.ACTUALSHIPPINGAGENTSERVICECODE_NAMESTR);
            itemno = jsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            variantcode = jsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            description = jsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            description2 = jsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            vendoritemno = jsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            vendoritemdescription = jsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);
            component10 = jsonObject.getString(cDatabase.COMPONENT10_NAMESTR);
            quantity = jsonObject.getDouble(cDatabase.QUANTITY_NAMESTR);
            quantityhandled = jsonObject.getDouble(cDatabase.QUANTITYHANDLED_NAMESTR);

                switch (statusshipping) {
                    case cPickorderLinePackAndShip.STATUSSHIPPING_STEP1:
                        localstatus = cPickorderLine.LOCALSTATUS_NEW;
                        break;
                    case cPickorderLinePackAndShip.STATUSSHIPPING_STEP1_TOREPORT:
                        localstatus = cPickorderLine.LOCALSTATUS_DONE_SENT;
                        break;
                    default:
                        localstatus = cPickorderLine.LOCALSTATUS_NEW;
                        break;
                }
        }
         catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
