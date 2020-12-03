package SSU_WHS.Basics.CompositeBarcodeProperty;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_COMPOSITEBARCODEPROPERTY)

public class cCompositeBarcodePropertyEntity {

    //Region Public Properties
    @ColumnInfo(name = cDatabase.COMMENT_NAMESTR)
    public String commentStr = "";
    public String getCommentStr() { return commentStr;}

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = cDatabase.FIELD_NAMESTR)
    public String fieldStr;
    public String getFieldStr() { return fieldStr;}

    @ColumnInfo(name = cDatabase.FIELDIDENTIFIER_NAMESTR)
    public String fieldIdentifierStr = "";
    public String getFieldIdentifierStr() { return fieldIdentifierStr;}

    @ColumnInfo(name = cDatabase.FIELDTYPE_NAMESTR)
    public String fieldTypeStr = "";
    public String getFieldTypeStr() { return fieldTypeStr;}

    @ColumnInfo(name = cDatabase.POSITIONSTART_NAMESTR)
    public int positionStartInt = 0;
    public int getPositionStartInt() { return positionStartInt;}

    @ColumnInfo(name = cDatabase.POSITIONEND_NAMESTR)
    public int positionEndInt = 0;
    public int getPositionEndInt() { return positionEndInt;}

    @ColumnInfo(name = cDatabase.SORTINGSEQUENCENO_NAMESTR)
    public int sortingSequenceNoInt = 0;
    public int getSortingSequenceNoInt() { return sortingSequenceNoInt;}

    @ColumnInfo(name = cDatabase.STRIPTHIS_NAMESTR)
    public String stripThisStr;
    public String getStripThisStr() { return stripThisStr;}
    //End Region Public Properties

    //Region Constructor
    public cCompositeBarcodePropertyEntity() {

    }

    public cCompositeBarcodePropertyEntity(JSONObject jsonObject) {
        try {
            this.commentStr = jsonObject.getString(cDatabase.COMMENT_NAMESTR);

            this.fieldStr = jsonObject.getString(cDatabase.FIELD_NAMESTR);
            this.fieldIdentifierStr = jsonObject.getString(cDatabase.FIELDIDENTIFIER_NAMESTR);
            this.fieldTypeStr = jsonObject.getString(cDatabase.FIELDTYPE_NAMESTR);

            this.positionStartInt = jsonObject.getInt(cDatabase.POSITIONSTART_NAMESTR);
            this.positionEndInt = jsonObject.getInt(cDatabase.POSITIONEND_NAMESTR);
            this.sortingSequenceNoInt = jsonObject.getInt(cDatabase.SORTINGSEQUENCENO_NAMESTR);
            this.stripThisStr = jsonObject.getString(cDatabase.STRIPTHIS_NAMESTR);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor

}
