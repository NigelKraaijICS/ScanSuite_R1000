package SSU_WHS.Basics.ArticleProperty;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_ARTICLEPROPERTY)
public class cArticlePropertyEntity {
    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
    public Integer getRecordidInt() {return this.recordid;}

    @ColumnInfo(name = cDatabase.ITEMNO_NAMESTR )
    public String itemnoStr;
    public String getItemNoStr() {return this.itemnoStr;}

    @ColumnInfo(name = cDatabase.VARIANTCODE_NAMESTR )
    public String variantCodeStr;
    public String getVariantCodeStr() {return this.variantCodeStr;}

    @ColumnInfo(name = cDatabase.PROPERTYCODE_NAMESTR)
    public String propertyCodeStr;
    public String getPropertyCodeStr() {return this.propertyCodeStr;}

    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR )
    public Integer sortingSequenceNoInt;
    public Integer getSortingSequenceNoInt() {return this.sortingSequenceNoInt;}

    @ColumnInfo(name = cDatabase.INPUTWORKFLOWS_NAMESTR)
    public String inputWorkflowsStr;
    public String getInputWorkflowsStr() {return this.inputWorkflowsStr;}

    @ColumnInfo(name = cDatabase.REQUIREDWORKFLOWS_NAMESTR)
    public String requiredWorkFlowsStr;
    public String getRequiredWorkFlowsStr() {return this.requiredWorkFlowsStr;}


    //empty constructor
    public cArticlePropertyEntity() {

    }

    public cArticlePropertyEntity(JSONObject pvJsonObject) {
        try {

            this.propertyCodeStr = pvJsonObject.getString(cDatabase.PROPERTYCODE_NAMESTR);
            this.sortingSequenceNoInt = pvJsonObject.getInt(cDatabase.SORTINGSEQUENCENO_NAMESTR);
            this.inputWorkflowsStr = pvJsonObject.getString(cDatabase.INPUTWORKFLOWS_NAMESTR);
            this.requiredWorkFlowsStr = pvJsonObject.getString(cDatabase.REQUIREDWORKFLOWS_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
