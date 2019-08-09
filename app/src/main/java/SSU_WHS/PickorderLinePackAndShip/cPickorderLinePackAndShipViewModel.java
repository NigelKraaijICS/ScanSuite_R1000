package SSU_WHS.PickorderLinePackAndShip;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import SSU_WHS.PickorderLines.cPickorderLineEntity;
import SSU_WHS.PickorderLines.cPickorderLineRepository;

public class cPickorderLinePackAndShipViewModel extends AndroidViewModel {
    private cPickorderLinePackAndShipRepository mRepository;

    public cPickorderLinePackAndShipViewModel(Application application) {
        super(application);

        mRepository = new cPickorderLinePackAndShipRepository(application);
    }

    public void insert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity) {
        mRepository.insert(pickorderLinePackAndShipEntity);
    }

    public LiveData<List<cPickorderLinePackAndShipEntity>> getPickorderLinePackAndShips(Boolean forcerefresh, String branchStr, String ordernumberStr) {
        return mRepository.getPickorderLinePackAndShips(forcerefresh, branchStr, ordernumberStr);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void delete(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity) {
        mRepository.delete(pickorderLinePackAndShipEntity);
    }

    public List<cPickorderLinePackAndShipEntity> getAll() {
        return mRepository.getAll();
    }

    public Double getTotalArticles() {return mRepository.getTotalArticles();}

    public Double getHandledArticles() {return mRepository.getHandledArticles();}

    public LiveData<List<cPickorderLinePackAndShipEntity>> getTotalPickorderLinePackAndShipEntities() { return mRepository.getTotalPickorderLinePackAndShipEntities(); }

    public LiveData<List<cPickorderLinePackAndShipEntity>> getHandledPickorderLinePackAndShipEntities() {return mRepository.getHandledPickorderLinePackAndShipEntities();}

    public LiveData<List<cPickorderLinePackAndShipEntity>> getNotHandledPickorderLinePackAndShipEntities() {return mRepository.getNotHandledPickorderLinePackAndShipEntities();}

    public Integer updateOrderLinePackAndShipQuantity(Integer pv_recordid, Double pv_quantity) {return mRepository.updateOrderLinePackAndShipQuantity(pv_recordid, pv_quantity);}

    public Integer updateOrderLinePackAndShipLocalStatus(Integer pv_recordid, Integer pv_newstatus) {return mRepository.updateOrderLinePackAndShipLocalStatus(pv_recordid, pv_newstatus);}

    public List<cPickorderLinePackAndShipEntity> getNotHandledPickorderLinePackAndShipEntitiesBySourceNo(String sourceno) { return mRepository.getNotHandledPickorderLinePackAndShipEntitiesBySourceNo(sourceno);}

    public List<cPickorderLinePackAndShipEntity> getNotHandledPickorderLinePackAndShipEntitiesByProcessingSequence(String processingsequence) { return mRepository.getNotHandledPickorderLinePackAndShipEntitiesByProcessingSequence(processingsequence);}

    public LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> getNotHandledPickorderinePackAndShipEntitiesDistinctSourceno() { return mRepository.getNotHandledPickorderinePackAndShipEntitiesDistinctSourceno();}

    public LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> getHandledPickorderinePackAndShipEntitiesDistinctSourceno() { return mRepository.getHandledPickorderinePackAndShipEntitiesDistinctSourceno();}

    public LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> getAllPickorderinePackAndShipEntitiesDistinctSourceno() { return mRepository.getAllPickorderinePackAndShipEntitiesDistinctSourceno();}

    public int updateOrderLinePackAndShipLocalStatusBySourceno(String sourceno, Integer newstatus) { return mRepository.updateOrderLinePackAndShipLocalStatusBySourceno(sourceno, newstatus);}

    public int getNumberToShipForCounter() {return mRepository.getNumberToShipForCounter();}

    public int getNumberShippedForCounter() {return mRepository.getNumberShippedForCounter();}

    public int getNumberTotalForCounter() {return mRepository.getNumberTotalForCounter();}

}
