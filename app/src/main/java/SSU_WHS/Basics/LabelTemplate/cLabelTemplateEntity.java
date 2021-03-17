package SSU_WHS.Basics.LabelTemplate;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_LABELTEMPLATE)
public class cLabelTemplateEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.LABELCODE_NAMESTR)
    public String labelcodeStr = "";
    public String getLabelcodeStr() {return this.labelcodeStr;}

    @ColumnInfo(name=cDatabase.TEMPLATE_NAMESTR)
    public String templateStr;
    public String getTemplateStr() {return this.templateStr;}


    public  cLabelTemplateEntity(){

    }

    public  cLabelTemplateEntity(JSONObject jsonObject){
        try {
            this.labelcodeStr = jsonObject.getString(cDatabase.LABELCODE_NAMESTR);
            this.templateStr = jsonObject.getString(cDatabase.TEMPLATE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
