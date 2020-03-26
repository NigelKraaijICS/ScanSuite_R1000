package SSU_WHS.Basics.ArticleImages;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_ARTICLEIMAGE, primaryKeys = {"Itemno", "Variantcode"})
public class cArticleImageEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name="Itemno")
    public String itemno = "";
    public String getItemnoStr() {return this.itemno;}

    @NonNull
    @ColumnInfo(name="Variantcode")
    public String variantcode = "";
    public String getVariantCodeStr() {return this.variantcode;}

    @ColumnInfo(name="Image")
    public String image;
    public String getImageStr() {return this.image;}

    //End Region Public Properies

    //Region Constructor
    public cArticleImageEntity() {

    }
    public cArticleImageEntity(JSONObject pvJsonObject) {
        try {
            this.itemno = pvJsonObject.getString(cDatabase.ITEMNO_NAMESTR);
            this.variantcode = pvJsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.image = pvJsonObject.getString(cDatabase.IMAGE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor


}
