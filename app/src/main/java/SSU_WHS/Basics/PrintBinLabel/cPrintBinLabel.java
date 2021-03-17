package SSU_WHS.Basics.PrintBinLabel;

import androidx.lifecycle.ViewModelProvider;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPrintBinLabel {

    private final String bincodeStr;

    public String getBincodeStr() { return bincodeStr; }

    private final Long quantityLng;
    public Long getQuantityLng(){ return quantityLng; }

    public  static cPrintBinLabel currentPrintBinLabel;

    public cPrintBinLabel(String bincodeStr, Long quantityLng) {
        this.bincodeStr = bincodeStr;
        this.quantityLng = quantityLng;
        currentPrintBinLabel = this;
    }

    public boolean pPrintBinLabelViaWebserviceBln() {
        cWebresult WebResult;
        cPrintBinLabelViewModel printBinLabelViewModel =     new ViewModelProvider(cAppExtension.fragmentActivity).get(cPrintBinLabelViewModel.class);

        WebResult =  printBinLabelViewModel.pPrintBinLabelViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PRINTBINLABEL);
            return  false;
        }
    }

}
