package SSU_WHS.General.Warehouseorder;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cWarehouseorderViewModel extends AndroidViewModel {

    //Region Public Properties
    public cWarehouseorderRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cWarehouseorderViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cWarehouseorderRepository(pvApplication);
    }
    //End Region Constructor



    public cWebresult pLockWarehouseopdrachtViaWebserviceWrs(String pvOrderTypeStr,
                                                             String pvOrderNumberStr,
                                                             String pvDeviceStr,
                                                             String pvWorkFlowStepStr,
                                                             Integer pvWorkflowStepInt,
                                                             Boolean pvIgnoreBusyBln)
    {
        return Repository.pWarehouseopdrachtLockViaWebserviceWrs(pvOrderTypeStr,pvOrderNumberStr, pvDeviceStr, pvWorkFlowStepStr, pvWorkflowStepInt,pvIgnoreBusyBln);
    }

    public cWebresult pLockReleaseWarehouseorderViaWebserviceWrs(String pvOrderTypeStr,
                                                                 String pvOrderNumberStr,
                                                                 String pvDeviceStr,
                                                                 String pvWorkFlowStepStr,
                                                                 Integer pvWorkFlowStepInt)
    {
        return Repository.pWarehouseorderLockReleaseViaWebserviceWrs(pvOrderTypeStr,pvOrderNumberStr, pvDeviceStr, pvWorkFlowStepStr, pvWorkFlowStepInt);
    }


}
