package SSU_WHS.Basics.Branches;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_BRANCH)
public class cBranchEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name=cDatabase.BRANCH_NAMESTR)
    public String branch = "";
    public String getBranchStr() {return this.branch;}

    @ColumnInfo(name=cDatabase.BRANCHTYPE_NAMESTR)
    public String branchtype;
    public String getBranchtypeStr() {return this.branchtype;}

    @ColumnInfo(name=cDatabase.BRANCHNAME_NAMESTR)
    public String branchname;
    public String getBranchnameStr() {return this.branchname;}

    @ColumnInfo(name=cDatabase.BINMANDATORY_NAMESTR)
    public String binmandatory;
    public String getBinmandatoryStr() {return this.binmandatory;}

    @ColumnInfo(name=cDatabase.RECEIVEDEFAULTBIN_NAMESTR)
    public String receivedefaultbin;
    public String getReceivedefaultbinStr() {return this.receivedefaultbin;}

    @ColumnInfo(name=cDatabase.RETURNDEFAULTBIN_NAMESTR)
    public String returndefaultbin;
    public String getReturndefaultbinStr() {return this.returndefaultbin;}

    @ColumnInfo(name=cDatabase.MOVEDEFAULTBIN_NAMESTR)
    public String movedefaultbin;
    public String getMovedefaultbinStr() {return this.movedefaultbin;}

    @ColumnInfo(name=cDatabase.PICKDEFAULTSTORAGEBIN)
    public String pickDefaultStorageBinStr;
    public String getPickDefaultStorageBinStr() {return this.pickDefaultStorageBinStr;}

    //End Region Public Properies

    //Region Constructor
    public cBranchEntity() {

    }

    public cBranchEntity(JSONObject jsonObject) {
        try {
            this.branch = jsonObject.getString(cDatabase.BRANCH_NAMESTR);
            this.branchtype = jsonObject.getString(cDatabase.BRANCHTYPE_NAMESTR);
            this.branchname = jsonObject.getString(cDatabase.BRANCHNAME_NAMESTR);
            this.binmandatory = jsonObject.getString(cDatabase.BINMANDATORY_NAMESTR);
            this.receivedefaultbin = jsonObject.getString(cDatabase.RECEIVEDEFAULTBIN_NAMESTR);
            this.returndefaultbin = jsonObject.getString(cDatabase.RETURNDEFAULTBIN_NAMESTR);
            this.movedefaultbin = jsonObject.getString(cDatabase.MOVEDEFAULTBIN_NAMESTR);
            this.pickDefaultStorageBinStr = jsonObject.getString(cDatabase.PICKDEFAULTSTORAGEBIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor










}
