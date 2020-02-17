package SSU_WHS.Picken.PickorderBarcodes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PICKORDERBARCODE, primaryKeys = {cDatabase.BARCODE_NAMESTR, cDatabase.BARCODETYPE_NAMESTR, cDatabase.ITEMNO_NAMESTR, cDatabase.VARIANTCODE_NAMESTR})
public class cPickorderBarcodeEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.BARCODE_NAMESTR)
    public String barcode;
    public String getBarcodeStr() {return barcode;}

    @NonNull
    @ColumnInfo(name = cDatabase.BARCODETYPE_NAMESTR)
    public String barcodetype;
    public String getBarcodeTypeStr() {return barcodetype;}

    @ColumnInfo(name = cDatabase.ISUNIQUEBARCODE_NAMESTR)
    public String isuniquebarcode;
    public String getIsuniquebarcodeStr() {return isuniquebarcode;}

    @NonNull
    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemno;
    public String getItemnoStr() {return itemno;}

    @NonNull
    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantcode;
    public String getVariantcodeStr() {return variantcode;}

    @ColumnInfo(name = cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR)
    public String quantityperunitofmeasure;
    public String getQuantityperunitofmeasureStr() {return quantityperunitofmeasure;}

    @ColumnInfo(name = cDatabase.QUANTITYHANDLED_NAMESTR)
    public String quantityhandled;
    public String getQuantityhandledStr() {return quantityhandled;}

    //empty constructor
    public cPickorderBarcodeEntity() {
    }

    public cPickorderBarcodeEntity(JSONObject jsonObject) {
        try {
            this.barcode = jsonObject.getString(cDatabase.BARCODE_NAMESTR);
            this.barcodetype = jsonObject.getString(cDatabase.BARCODETYPE_NAMESTR);
            this.isuniquebarcode = jsonObject.getString(cDatabase.ISUNIQUEBARCODE_NAMESTR);
            this.itemno= jsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode= jsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.quantityperunitofmeasure= jsonObject.getString(cDatabase.QUANTITYPERUNITOFMEASURE_NAMESTR);
            this.quantityhandled = jsonObject.getString(cDatabase.QUANTITYHANDLED_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
