package SSU_WHS.Picken.Pickorders;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import ICS.Complex_types.c_InterfaceShippingPackageIesp;

public class cPickorderViewModel extends AndroidViewModel {
    private cPickorderRepository mRepository;

    public cPickorderViewModel(Application application) {
        super(application);

        mRepository = new cPickorderRepository(application);
    }
    public void insert(cPickorderEntity pickorderEntity) {mRepository.insert(pickorderEntity);}

    public LiveData<List<cPickorderEntity>> getPickorders(Boolean forcerefresh, String user, String branch, Boolean processingorparked, String searchtext, String maintype) {return mRepository.getPickorders(forcerefresh, user,branch,processingorparked,searchtext,maintype);}

    public Boolean getProcessingOrParkedOrdersBln(String user, String branch, String maintype) { return mRepository.getProcessingOrParkedOrdersBln(user, branch, maintype);}

    public List<cPickorderEntity> getLocalPickorders() {return mRepository.getLocalPickorders();}

    public void deleteAll() {mRepository.deleteAll();}

    public LiveData<List<cPickorderEntity>> getFilteredPickorders(String currentUser, Boolean useFilters, Boolean showProcessedWait, Boolean showSingleArticles, Boolean showAssignedToMe, Boolean showAssignedToOthers, Boolean showNotAssigned) {return mRepository.getFilteredPickorders(currentUser, useFilters, showProcessedWait,showSingleArticles,  showAssignedToMe, showAssignedToOthers, showNotAssigned);}

    public Boolean pickorderStepHandled(String user, String language, String branch, String orderNumber, String device, String workplace, String workflowStepcode, Integer workflowStep, String culture) { return mRepository.pickorderStepHandled(user, language,branch, orderNumber,device,workplace,workflowStepcode, workflowStep, culture);}

    public LiveData<List<cPickorderEntity>> getSortOrShiporders(Boolean forcerefresh, String user, String branch, Integer pickstep, String searchtext, String maintype) {return mRepository.getSortOrShiporders(forcerefresh, user,branch,pickstep,searchtext,maintype);}

    public cPickorderEntity getPickorderByOrderNumber(String ordernumber) { return mRepository.getPickorderByOrderNumber(ordernumber);}

    public Boolean pickorderSourceDocumentShipped(String user, String branch, String ordernumber, String sourceno, String culture, String shippingagent, String shippingservice, List<c_InterfaceShippingPackageIesp> packages) { return mRepository.pickorderSourceDocumentShipped(user, branch, ordernumber, sourceno, culture, shippingagent, shippingservice, packages);}
    //public void pickorderSourceDocumentShipped(String user, String branch, String ordernumber, String sourceno, String culture, String shippingagent, String shippingservice, c_InterfaceShippingPackageIesp packages) {mRepository.pickorderSourceDocumentShipped(user, branch, ordernumber, sourceno, culture, shippingagent, shippingservice, packages);}

    public Boolean pickorderUpdateWorkplace(String user, String branch, String ordernumber, String workplace) { return mRepository.pickorderUpdateWorkplace(user, branch, ordernumber, workplace);}

    public int updatePickorderWorkplaceLocal(String ordernumber, String workplace) {return mRepository.updatePickorderWorkplaceLocal(ordernumber, workplace);}
}
