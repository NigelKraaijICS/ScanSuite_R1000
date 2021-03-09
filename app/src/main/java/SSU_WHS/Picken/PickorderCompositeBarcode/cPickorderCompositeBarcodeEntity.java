package SSU_WHS.Picken.PickorderCompositeBarcode;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PICKORDERCOMPOSITEBARCODE, primaryKeys = {cDatabase.COMPOSITEBARCODESPLITTED_NAMESTR, cDatabase.ITEMNO_NAMESTR, cDatabase.VARIANTCODE_NAMESTR})
public class cPickorderCompositeBarcodeEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.COMPOSITEBARCODESPLITTED_NAMESTR)
    public String compositeBarcodeStr = "";
    public String getCompositeBarcodeStr() {return compositeBarcodeStr;}

    @NonNull
    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR)
    public String itemNoStr = "";
    public String getItemNoStr() {return itemNoStr;}

    @NonNull
    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR)
    public String variantCodeStr = "";
    public String getVariantCodeStr() {return variantCodeStr;}

    @NonNull
    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public int sortingSequenceNoInt = 0;
    public int getSortingSequenceNoInt() {return sortingSequenceNoInt;}

    //empty constructor
    public cPickorderCompositeBarcodeEntity() {
    }

    public cPickorderCompositeBarcodeEntity(JSONObject jsonObject) {
        try {
            this.compositeBarcodeStr = jsonObject.getString(cDatabase.COMPOSITEBARCODESPLITTED_NAMESTR);
            this.itemNoStr = jsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantCodeStr = jsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.sortingSequenceNoInt= jsonObject.getInt(cDatabase.SORTINGSEQUENCENO_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
