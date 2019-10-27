package SSU_WHS.Basics.BranchBin;

import org.json.JSONObject;


public class cBranchBin {

    //Region Public Properties
    public String branchStr;
    public String getBranchStr() {
        return branchStr;
    }

    public String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public String zoneStr;
    public String getZoneStr() {
        return zoneStr;
    }

    public String binTypeStr;
    public String getBinTypeStr() { return binTypeStr; }

    public Boolean useForStorageBln;
    public Boolean isUseForStorageBln() {
        return useForStorageBln;
    }

    public Boolean useForReturnSalesBln;
    public Boolean isUseForReturnSalesBln() {
        return useForReturnSalesBln;
    }

    public cBranchBinEntity branchBinEntity;
    public boolean inDatabaseBln;

    //end region Public Propties

    //Region Constructor
    public cBranchBin(JSONObject pvJsonObject) {
        this.branchBinEntity = new cBranchBinEntity(pvJsonObject);
        this.branchStr = this.branchBinEntity.getBranchStr();
        this.binCodeStr =  this.branchBinEntity.getbinCodeStr();
        this.descriptionStr = this.branchBinEntity.getDescriptionStr();
        this.zoneStr = this.branchBinEntity.getZoneStr();
        this.binTypeStr = this.branchBinEntity.getBinTypeStr();
        this.useForStorageBln = this.branchBinEntity.getUseForStorageBln();
        this.useForReturnSalesBln = this.branchBinEntity.getUseForReturnSalesBln();
    }
    //End Region Constructor

    //Region Public Methods

    //End Region Public Methods
}
