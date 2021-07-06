package SSU_WHS.Basics.ContentlabelContainer;


import org.json.JSONObject;

import SSU_WHS.Basics.BranchReason.cBranchReasonEntity;

public class cContentlabelContainer {

    public Long containerSequencoNoLng = 0L;
    public Long getContainerSequencoNoLng() {return this.containerSequencoNoLng;}

    public double quantityHandledDbl;
    public double getQuantityHandledDbl() {return this.quantityHandledDbl;}

    public static cContentlabelContainer currentContentlabelContainer;

    //Region Constructor

    public cContentlabelContainer(Long pvSequenceNoLng, Double pvQuantityDbl){
        this.containerSequencoNoLng = pvSequenceNoLng;
        this.quantityHandledDbl = pvQuantityDbl;
        currentContentlabelContainer = this;
    }


    public cContentlabelContainer(JSONObject pvJsonObject) {
        cContentlabelContainerEntity contentlabelContainerEntity = new cContentlabelContainerEntity(pvJsonObject);
        this.containerSequencoNoLng = contentlabelContainerEntity.getContainerSequenceNoLng();
        this.quantityHandledDbl =  contentlabelContainerEntity.getQuantityDbl();
        if (this.containerSequencoNoLng > 0){ currentContentlabelContainer = this;}
    }
}
