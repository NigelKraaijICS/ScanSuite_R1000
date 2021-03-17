package SSU_WHS.Basics.PrintItemLabel;

import androidx.lifecycle.ViewModelProvider;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPrintItemLabel {

    private String orderTypeStr;
    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    private String orderNumberStr;
    public String getOrderNumberStr() {
        return orderNumberStr;
    }

    private Long lineNoLng;
    public Long getLineNoLng() { return lineNoLng; }

    private String barcodeStr;
    public String getBarcodeStr() { return barcodeStr; }

    private Long quantityLng;
    public Long getQuantityLng() { return quantityLng; }

    private String stockOwnerStr;
    public String getStockOwnerStr() { return stockOwnerStr; }


    public static cPrintItemLabel currentPrintItemLabel;

    public cPrintItemLabel(String pvOrderTypeStr, String pvOrderNumberStr, Long pvLineNoLng, String pvBarcodeStr, Long pvQuantityLng){
        this.orderTypeStr = pvOrderTypeStr;
        this.orderNumberStr = pvOrderNumberStr;
        this.lineNoLng = pvLineNoLng;
        this.barcodeStr = pvBarcodeStr;
        this.quantityLng = pvQuantityLng;
        this.stockOwnerStr = "";
        currentPrintItemLabel = this;
    }

    public cPrintItemLabel (String pvStockOwnerStr, String pvBarcodeStr, Long pvQuantityLng){
        this.orderTypeStr = "";
        this.orderNumberStr = "";
        this.lineNoLng = 0L;
        this.barcodeStr = pvBarcodeStr;
        this.quantityLng = pvQuantityLng;
        this.stockOwnerStr = pvStockOwnerStr;
        currentPrintItemLabel = this;
    }

    public boolean pPrintLineItemLabelViaWebserviceBln() {
        cWebresult WebResult;
        cPrintItemLabelViewModel printItemLabelViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cPrintItemLabelViewModel.class);

        WebResult =  printItemLabelViewModel.pPrintLineItemLabelViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PRINTLINEITEMLABEL);
            return  false;
        }
    }
    public boolean pPrintItemLabelViaWebserviceBln() {
        cWebresult WebResult;
        cPrintItemLabelViewModel printItemLabelViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cPrintItemLabelViewModel.class);

        WebResult =  printItemLabelViewModel.pPrintItemLabelViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PRINTITEMLABEL);
            return  false;
        }
    }
}
