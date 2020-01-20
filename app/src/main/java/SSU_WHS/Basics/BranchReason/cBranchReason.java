package SSU_WHS.Basics.BranchReason;

import org.json.JSONObject;

public class cBranchReason {

    //Region Public Properties

    private String reasonStr;
    public String getReasonStr() { return this.reasonStr; }

    private String descriptionStr;
    public String getDescriptionStr() { return this.descriptionStr; }


    private Boolean returnBln;
    public Boolean isReturn() { return this.returnBln; }

    public static cBranchReason currentBranchReason;


    private static cBranchReasonAdapter gBranchReasonAdapter;
    public static cBranchReasonAdapter getBranchReasonAdapter() {
        if (gBranchReasonAdapter == null) {
            gBranchReasonAdapter = new cBranchReasonAdapter();
        }
        return gBranchReasonAdapter;
    }
    //end region Public Propties

    //Region Constructor
    public cBranchReason(JSONObject pvJsonObject) {
        cBranchReasonEntity branchReasonEntity = new cBranchReasonEntity(pvJsonObject);
        this.reasonStr = branchReasonEntity.getReasonStr();
        this.descriptionStr =  branchReasonEntity.getDescriptionStr();
        this.returnBln = branchReasonEntity.getReturnBln();
    }
}
