package SSU_WHS.PickorderLines;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Weberror.cWeberrorEntity;
import SSU_WHS.Complex_types.c_BarcodeHandledUwbh;
import SSU_WHS.Webservice.cBarcode;
import SSU_WHS.Webservice.cContainer;
import SSU_WHS.Webservice.cWebresult;

public class cPickorderLineViewModel extends AndroidViewModel {
    private cPickorderLineRepository mRepository;

    public cPickorderLineViewModel(Application application) {
        super(application);

        mRepository = new cPickorderLineRepository(application);
    }

    public void insert(cPickorderLineEntity pickorderLineEntity) {mRepository.insert(pickorderLineEntity);}

    public LiveData<List<cPickorderLineEntity>> getPickorderLines(Boolean forcerefresh,
                                                                  String pickorderType,
                                                                  String branchStr,
                                                                  String ordernumberStr,
                                                                  String actiontypeStr,
                                                                  String extraField1,
                                                                  String extraField2,
                                                                  String extraField3,
                                                                  String extraField4,
                                                                  String extraField5,
                                                                  String extraField6,
                                                                  String extraField7,
                                                                  String extraField8)
    {return mRepository.getPickorderLines(forcerefresh, pickorderType, branchStr, ordernumberStr, actiontypeStr, extraField1, extraField2,extraField3,extraField4,extraField5,extraField6,extraField7,extraField8);}

    public List<cPickorderLineEntity> getLocalPickorderLines() {return mRepository.getLocalPickorderLines();}

    public void deleteAll() {mRepository.deleteAll();}

    public void delete(cPickorderLineEntity pickorderLineEntity) {mRepository.delete(pickorderLineEntity);}

    public cPickorderLineEntity getPickorderLineByItemNo(String pv_itemNo) { return mRepository.getPickorderLineByItemNo(pv_itemNo); }

    public Double getTotalArticles() {return mRepository.getTotalArticles();}

    public Double getHandledArticles() {return mRepository.getHandledArticles();}

    public LiveData<List<cPickorderLineEntity>> getTotalPickorderLineEntities() { return mRepository.getTotalPickorderLineEntities(); }

    public LiveData<List<cPickorderLineEntity>> getHandledPickorderLineEntities() {return mRepository.getHandledPickorderLineEntities();}

    public LiveData<List<cPickorderLineEntity>> getNotHandledPickorderLineEntities() {return mRepository.getNotHandledPickorderLineEntities();}

    public List<cPickorderLineEntity> getNotHandledPickorderLineEntitiesLin() {return mRepository.getNotHandledPickorderLineEntitiesLin();}

    public List<cPickorderLineEntity> getAll() { return mRepository.getAll(); }

    public cPickorderLineEntity getPickorderLineNotHandledByBin (String pv_bin) {return mRepository.getPickorderLineNotHandledByBin(pv_bin);}

    public Integer updateOrderLineQuantity(Integer pv_recordid, Double pv_quantity) {return mRepository.updateOrderLineQuantity(pv_recordid, pv_quantity);}

    public cPickorderLineEntity getNextPickLineFromLocation (String pv_bin) {return mRepository.getNextPickLineFromLocation(pv_bin);}

    public cPickorderLineEntity getNextPickLineFromSourceNo (String pv_sourceno) {return mRepository.getNextPickLineFromSourceNo(pv_sourceno);}

    public cPickorderLineEntity getPickLineByRecordid(Integer pv_recordid) {return mRepository.getPickLineByRecordid(pv_recordid);}

    public Integer updateOrderLineLocalStatus(Integer pv_recordid, Integer pv_newstatus) {return mRepository.updateOrderLineLocalStatus(pv_recordid, pv_newstatus);}

    public Integer updateOrderLineProcessingSequence(Integer pv_recordid, String pv_processingsequence) {return mRepository.updateOrderLineProcessingSequence(pv_recordid, pv_processingsequence);}

    public List<cPickorderLineEntity> getNotHandledPickorderLinesByItemNoandVariantCode(String pv_itemNo, String pv_variantCode) {return mRepository.getNotHandledPickorderLinesByItemNoandVariantCode(pv_itemNo, pv_variantCode);}

    public void abortOrder() {mRepository.abortOrder(); }

    //public cWebresult pickOrderLineHandled(final String userName, final String branch, final String orderNumber, final Long lineNumber, final String handledTimeStamp, final String containerHandled, final List<c_BarcodeHandledUwbh> barcodes, final String containers, final String processingSequence ) throws ExecutionException, InterruptedException {return mRepository.pickOrderLineHandled(userName, branch, orderNumber, lineNumber, handledTimeStamp, containerHandled, barcodes, containers, processingSequence);}

    public void pickOrderLineHandled(final String userName, final String branch, final String orderNumber, final Long lineNumber, final String handledTimeStamp, final String containerHandled, final List<c_BarcodeHandledUwbh> barcodes, final String containers, final String processingSequence ) {mRepository.pickOrderLineHandled(userName, branch, orderNumber, lineNumber, handledTimeStamp, containerHandled, barcodes, containers, processingSequence);}

    public void sortOrderLineHandled(final String userName, final String branch, final String orderNumber, final Long lineNumber, final String handledTimeStamp, final Double number, final String barcode, final String propertiesHandled, final String containers, final String  location) {mRepository.sortOrderLineHandled(userName, branch, orderNumber, lineNumber, handledTimeStamp, number, barcode, propertiesHandled, containers, location);}

    public void updateSortOrder(Integer recordId, Integer number, String location) {mRepository.updateSortOrderLine(recordId, number, location);}

    public LiveData<List<cPickorderLineEntity>> getPickorderLineEntitiesToSend() { return mRepository.getPickorderLineEntitiesToSend();}

    public Boolean pickorderlineReset(String user, String branch, String orderNumber, Long lineno) { return mRepository.pickorderlineReset(user, branch,orderNumber,lineno);}

    public cPickorderLineEntity getSortorderLineNotHandledByItemNoAndVariant (String itemno, String variant) {return mRepository.getSortorderLineNotHandledByItemNoAndVariant(itemno,variant);}

    public LiveData<List<String>> getSortLocationAdvice(String ordertype, String branch, String ordernumber, String sourceno) {return mRepository.getSortLocationAdvice(ordertype, branch, ordernumber, sourceno);}

    public int getNumberNotHandledForCounter() {return mRepository.getNumberNotHandledForCounter();}

    public int getNumberTotalNotHandledForCounter() {return mRepository.getNumberTotalNotHandledForCounter();}

    public int getNumberHandledForCounter() {return mRepository.getNumberHandledForCounter();}

    public int getNumberTotalHandledForCounter() {return mRepository.getNumberTotalHandledForCounter();}

    public int getNumberTotalForCounter() {return mRepository.getNumberTotalForCounter();}

    public int getNumberTotalTotalForCounter() {return mRepository.getNumberTotalTotalForCounter();}


}
