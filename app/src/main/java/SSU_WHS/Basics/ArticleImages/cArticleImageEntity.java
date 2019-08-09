package SSU_WHS.Basics.ArticleImages;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_ARTICLEIMAGE, primaryKeys = {"Itemno", "Variantcode"})
public class cArticleImageEntity {
    @NonNull
    @ColumnInfo(name="Itemno")
    public String itemno;
    @NonNull
    @ColumnInfo(name="Variantcode")
    public String variantcode;
    @ColumnInfo(name="Image")
    public String image;
    @ColumnInfo(name="Datatimestamp")
    public String datatimestamp;

    //empty constructor
    public cArticleImageEntity() {

    }
    public cArticleImageEntity(JSONObject jsonObject) {
        try {
            itemno = jsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            variantcode = jsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            image = jsonObject.getString(cDatabase.IMAGE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getItemno() {return this.itemno;}
    public void setItemno(String pv_itemno) {this.itemno = pv_itemno;}
    public String getVariantcode() {return this.variantcode;}
    public void setVariantcode(String pv_variantcode) {this.variantcode = pv_variantcode;}
    public String getImage() {return this.image;}
    public void setImage(String pv_image) {this.image = pv_image;}
}
