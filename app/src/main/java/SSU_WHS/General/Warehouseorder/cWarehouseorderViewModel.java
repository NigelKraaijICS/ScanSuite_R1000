package SSU_WHS.General.Warehouseorder;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cWarehouseorderViewModel extends AndroidViewModel {

    //Region Public Properties

    //End Region Public Properties

    //Region Constructor
    public cWarehouseorderViewModel(Application pvApplication) {
        super(pvApplication);
    }
    //End Region Constructor



    public cWebresult pLockWarehouseopdrachtViaWebserviceWrs(String pvOrderTypeStr,
                                                             String pvOrderNumberStr,
                                                             String pvDeviceStr,
                                                             String pvWorkFlowStepStr,
                                                             Integer pvWorkflowStepInt,
                                                             boolean pvIgnoreBusyBln)
    {
        return cWarehouseorderRepository.pWarehouseopdrachtLockViaWebserviceWrs(pvOrderTypeStr,pvOrderNumberStr, pvDeviceStr, pvWorkFlowStepStr, pvWorkflowStepInt,pvIgnoreBusyBln);
    }

    public cWebresult pLockReleaseWarehouseorderViaWebserviceWrs(String pvOrderTypeStr,
                                                                 String pvOrderNumberStr,
                                                                 String pvDeviceStr,
                                                                 String pvWorkFlowStepStr,
                                                                 Integer pvWorkFlowStepInt)
    {
        return cWarehouseorderRepository.pWarehouseorderLockReleaseViaWebserviceWrs(pvOrderTypeStr,pvOrderNumberStr, pvDeviceStr, pvWorkFlowStepStr, pvWorkFlowStepInt);
    }


}
