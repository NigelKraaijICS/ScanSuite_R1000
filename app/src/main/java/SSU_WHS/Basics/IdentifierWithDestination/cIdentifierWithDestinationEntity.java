package SSU_WHS.Basics.IdentifierWithDestination;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_IDENTIFIERWITHDESTINATION)
public class cIdentifierWithDestinationEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.IDENTIFIER_NAMESTR)
    public String identifier = "";
    public String getIdentifierStr() {return this.identifier;}

    @ColumnInfo(name=cDatabase.DESTINATION_NAMESTR)
    public String destination;
    public String getDestinationStr() {return this.destination;}

    @ColumnInfo(name=cDatabase.INFOKEY_NAMESTR)
    public String infoKey;
    public String getInfoKeyStr() {return this.infoKey;}

    @ColumnInfo(name=cDatabase.INFOVALUE_NAMESTR)
    public String infoValue;
    public String getInfoValueStr() {return this.infoValue;}

    //empty constructor
    public cIdentifierWithDestinationEntity() {

    }
    public cIdentifierWithDestinationEntity(JSONObject jsonObject) {
        try {
            this.identifier = jsonObject.getString(cDatabase.IDENTIFIER_NAMESTR);
            this.destination = jsonObject.getString(cDatabase.DESTINATION_NAMESTR);
            this.infoKey = jsonObject.getString(cDatabase.INFOKEY_NAMESTR);
            this.infoValue = jsonObject.getString(cDatabase.INFOVALUE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
