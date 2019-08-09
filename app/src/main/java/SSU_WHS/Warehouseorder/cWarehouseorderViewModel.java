package SSU_WHS.Warehouseorder;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

public class cWarehouseorderViewModel extends AndroidViewModel {
    private cWarehouseorderRepository mRepository;

    public cWarehouseorderViewModel(Application application) {
        super(application);
        mRepository = new cWarehouseorderRepository(application);
    }
    public Boolean updateCurrentOrderLocation(String user,
                                              String branch,
                                              String ordernumber,
                                              String currentLocation)
        {
            return mRepository.updateCurrentOrderLocation(user, branch,ordernumber,currentLocation);
        }

    public Boolean getLockWarehouseorderBln(String user,
                                            String language,
                                            String ordertype,
                                            String branch,
                                            String ordernumber,
                                            String device,
                                            String workflowstepstr,
                                            Integer workflowstepint,
                                            Boolean ignorebusy)
        {
            return mRepository.getWarehouseorderLockBln(user, language, ordertype, branch, ordernumber, device, workflowstepstr, workflowstepint,ignorebusy);
        }

    public Boolean getLockReleaseWarehouseorderBln(String user,
                                            String language,
                                            String ordertype,
                                            String branch,
                                            String ordernumber,
                                            String device,
                                            String workflowstepstr,
                                            Integer workflowstepint)
    {
        return mRepository.getWarehouseorderLockReleaseBln(user, language, ordertype, branch, ordernumber, device, workflowstepstr, workflowstepint);
    }
    public Boolean getUnLockWarehouseorderBln(String user,
                                            String ordertype,
                                            String branch,
                                            String ordernumber)
    {
        return mRepository.getWarehouseUnlockBln(user, ordertype, branch, ordernumber);
    }


}
