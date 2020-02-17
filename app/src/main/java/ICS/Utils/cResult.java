package ICS.Utils;

import java.util.ArrayList;
import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;

public class cResult {

    public boolean resultBln;
    public List<String> messageObl;
    public cWarehouseorder.ActivityActionEnu activityActionEnu;
    public cResultAction resultAction;

    public void pAddErrorMessage(String pvErrorMessageStr) {

        if (this.messageObl == null) {
            this.messageObl = new ArrayList<>();
        }

        this.messageObl.add(pvErrorMessageStr);
    }

    public void pSetResultAction(List<String>  pvNextAcitivityObl) {

        if (pvNextAcitivityObl == null) {
            return;
        }

        if (pvNextAcitivityObl.size() < 5) {
            return;
        }


        if (pvNextAcitivityObl.size() >= 5) {

            this.resultAction = new cResultAction();


            this.resultAction.nextAutorisationCodeStr  = pvNextAcitivityObl.get(0);
            this.resultAction.nextAssignmentStr = pvNextAcitivityObl.get(3);
            this.resultAction.nextStatusInt = cText.pStringToIntegerInt(pvNextAcitivityObl.get(4));


        }
    }


    public String messagesStr(){

        String resultStr = "";

        if (this.messageObl == null || this.messageObl.size() == 0 ) {
            return resultStr;
        }

        for (String messageStr : this.messageObl) {
            resultStr = resultStr +  messageStr;
        }

        return  resultStr;

    }



}


