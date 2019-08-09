package SSU_WHS.Basics.Branches;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_BRANCH)
public class cBranchEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="Branch")
    public String branch;
    @ColumnInfo(name="Branchtype")
    public String branchtype;
    @ColumnInfo(name="Branchname")
    public String branchname;
    @ColumnInfo(name="BinMandatory")
    public String binmandatory;
    @ColumnInfo(name="PickDefaultRejectReason")
    public String pickdefaultrejectreason;
    @ColumnInfo(name="PickDefaultStorageBin")
    public String pickdefaultstoragebin;
    @ColumnInfo(name="ReceiveDefaultBin")
    public String receivedefaultbin;
    @ColumnInfo(name="ReturnDefaultBin")
    public String returndefaultbin;
    @ColumnInfo(name="MoveDefaultBin")
    public String movedefaultbin;
    @ColumnInfo(name="ShipDefaultBin")
    public String shipdefaultbin;

    //empty constructor
    public cBranchEntity() {

    }
    public cBranchEntity(JSONObject jsonObject) {
        try {
            branch = jsonObject.getString(cDatabase.BRANCH_NAMESTR);
            branchtype = jsonObject.getString(cDatabase.BRANCHTYPE_NAMESTR);
            branchname = jsonObject.getString(cDatabase.BRANCHNAME_NAMESTR);
            binmandatory = jsonObject.getString(cDatabase.BINMANDATORY_NAMESTR);
            pickdefaultrejectreason = jsonObject.getString(cDatabase.PICKDEFAULTREJECTREASON);
            pickdefaultstoragebin = jsonObject.getString(cDatabase.PICKDEFAULTSTORAGEBIN);
            receivedefaultbin = jsonObject.getString(cDatabase.RECEIVEDEFAULTBIN_NAMESTR);
            returndefaultbin = jsonObject.getString(cDatabase.RETURNDEFAULTBIN_NAMESTR);
            movedefaultbin = jsonObject.getString(cDatabase.MOVEDEFAULTBIN_NAMESTR);
            shipdefaultbin = jsonObject.getString(cDatabase.SHIPDEFAULTBIN_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getBranch() {return this.branch;}
    public void setBranch(String pv_branch) {this.branch = pv_branch;}
    public String getBranchtype() {return this.branchtype;}
    public void setBranchtype(String pv_branchtype) {this.branch = pv_branchtype;}
    public String getBranchname() {return this.branchname;}
    public void setBranchname(String pv_branchname) {this.branchname = pv_branchname;}
    public String getBinmandatory() {return this.binmandatory;}
    public void setBinmandatory(String pv_binmandatory) {this.binmandatory = pv_binmandatory;}
    public String getPickdefaultrejectreason() {return this.pickdefaultrejectreason;}
    public void setPickdefaultrejectreason(String pv_pickdefaultrejectreason) {this.pickdefaultrejectreason = pv_pickdefaultrejectreason;}
    public String getPickdefaultstoragebin() {return this.pickdefaultstoragebin;}
    public void setPickdefaultstoragebin(String pv_pickdefaultstoragebin) {this.pickdefaultstoragebin = pv_pickdefaultstoragebin;}
    public String getReceivedefaultbin() {return this.receivedefaultbin;}
    public void setReceivedefaultbin(String pv_receivedefaultbin) {this.receivedefaultbin = pv_receivedefaultbin;}
    public String getReturndefaultbin() {return this.returndefaultbin;}
    public void setReturndefaultbin(String pv_returndefaultbin) {this.returndefaultbin = pv_returndefaultbin;}
    public String getMovedefaultbin() {return this.movedefaultbin;}
    public void setMovedefaultbin(String pv_movedefaultbin) {this.movedefaultbin = pv_movedefaultbin;}
    public String getShipdefaultbin() {return this.shipdefaultbin;}
    public void setShipdefaultbin(String pv_shipdefaultbin) {this.shipdefaultbin = pv_shipdefaultbin;}
}
