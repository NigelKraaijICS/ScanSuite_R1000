package ICS.Utils;

import java.util.ArrayList;
import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;

public class cResult {

    public boolean resultBln;
    public List<String> messageObl;
    public cWarehouseorder.ActivityActionEnu activityActionEnu;

    public  void pAddErrorMessage(String pvErrorMessageStr) {

        if (this.messageObl == null) {
            this.messageObl = new ArrayList<>();
        }

        this.messageObl.add(pvErrorMessageStr);
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


