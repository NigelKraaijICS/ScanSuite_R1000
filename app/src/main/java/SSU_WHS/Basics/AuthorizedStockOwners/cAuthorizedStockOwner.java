package SSU_WHS.Basics.AuthorizedStockOwners;

import org.json.JSONObject;

import SSU_WHS.Basics.BranchReason.cBranchReasonEntity;

public class cAuthorizedStockOwner {

    public String stockownerStr = "";
    public String getStockownerStr() {return this.stockownerStr;}

    public String vestigingStr;
    public String getVestigingStr() {return this.vestigingStr;}

    //Region Constructor
    public cAuthorizedStockOwner(JSONObject pvJsonObject) {
        cAuthorizedStockOwnerEntity authorizedStockOwnerEntity = new cAuthorizedStockOwnerEntity(pvJsonObject);
        this.stockownerStr = authorizedStockOwnerEntity.getStockownerStr();
        this.vestigingStr =  authorizedStockOwnerEntity.getVestigingStr();
    }

}
