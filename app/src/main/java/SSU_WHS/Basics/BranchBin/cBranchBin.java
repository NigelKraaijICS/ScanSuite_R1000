package SSU_WHS.Basics.BranchBin;

import org.json.JSONObject;

import SSU_WHS.Basics.Users.cUser;

public class cBranchBin {

    //Region Public Properties
    private String branchStr;
    public String getBranchStr() {
        return branchStr;
    }

    private String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    private String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private String zoneStr;
    public String getZoneStr() {
        return zoneStr;
    }

    private String binTypeStr;
    public String getBinTypeStr() { return binTypeStr; }

    private Boolean useForStorageBln;
    public Boolean isUseForStorageBln() {
        return useForStorageBln;
    }

    private Boolean useForReturnSalesBln;
    public Boolean isUseForReturnSalesBln() {
        return useForReturnSalesBln;
    }

    private cBranchBinEntity branchBinEntity;

    public static cBranchBin currentBranchBin;
    //end region Public Propties

    //Region Constructor
    public cBranchBin(JSONObject pvJsonObject) {
        this.branchBinEntity = new cBranchBinEntity(pvJsonObject);
        this.branchStr = this.branchBinEntity.getBranchStr();
        this.binCodeStr =  this.branchBinEntity.getbinCodeStr();
        this.zoneStr = this.branchBinEntity.getZoneStr();
        this.binTypeStr = this.branchBinEntity.getBinTypeStr();
        this.useForStorageBln = this.branchBinEntity.getUseForStorageBln();
        this.useForReturnSalesBln = this.branchBinEntity.getUseForReturnSalesBln();
    }

    public cBranchBin(String pvBinCodeStr) {
        this.branchBinEntity =null;
        this.branchStr = cUser.currentUser.currentBranch.getBranchStr();
        this.binCodeStr =  pvBinCodeStr;
        this.zoneStr = "";
        this.binTypeStr = "";
        this.useForStorageBln = false;
        this.useForReturnSalesBln =false;
    }

    //End Region Constructor

    //Region Public Methods

    //End Region Public Methods
}
