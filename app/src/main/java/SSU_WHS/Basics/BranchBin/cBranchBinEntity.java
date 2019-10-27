package SSU_WHS.Basics.BranchBin;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import ICS.Utils.cText;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_BRANCHBIN)
public class cBranchBinEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.BRANCH_NAMESTR)
    public String branch;
    public String getBranchStr() {return this.branch;}

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.BINCODE_NAMESTR)
    public String binCode;
    public String getbinCodeStr() {return this.binCode;}

    @ColumnInfo(name=cDatabase.DESCRIPTION_NAMESTR)
    public String description;
    public String getDescriptionStr() {return this.description;}

    @ColumnInfo(name=cDatabase.ZONE_NAMESTR)
    public String zone;
    public String getZoneStr() {return this.zone;}

    @ColumnInfo(name=cDatabase.BINTYPE_NAMESTR)
    public String binType;
    public String getBinTypeStr() {return this.zone;}

    @ColumnInfo(name=cDatabase.USEFORSTORAGE_NAMESTR)
    public Boolean useForStorageBln;
    public Boolean getUseForStorageBln() {return this.useForStorageBln;}

    @ColumnInfo(name=cDatabase.USEFORRETURNSALES_NAMESTR)
    public Boolean useForReturnSalesBln;
    public Boolean getUseForReturnSalesBln() {return this.useForReturnSalesBln;}

    //End Region Public Properies

    //Region Constructor
    public cBranchBinEntity() {

    }

    public cBranchBinEntity(JSONObject jsonObject) {
        try {
            this.branch = cUser.currentUser.currentBranch.getBranchStr();
            this.binCode = jsonObject.getString(cDatabase.BINCODENL_NAMESTR);
            this.description = jsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.zone = jsonObject.getString(cDatabase.ZONE_NAMESTR);
            this.binType = jsonObject.getString(cDatabase.BINTYPE_NAMESTR);
            this.useForStorageBln = cText.pStringToBooleanBln(jsonObject.getString(cDatabase.USEFORSTORAGE_NAMESTR),false) ;
            this.useForReturnSalesBln = cText.pStringToBooleanBln(jsonObject.getString(cDatabase.USEFORRETURNSALES_NAMESTR),false) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor










}
