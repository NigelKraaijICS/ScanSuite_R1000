package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.arch.persistence.room.ColumnInfo;

import SSU_WHS.General.cDatabase;

public class cPickorderLinePackAndShipGroupBySourceNo {
    @ColumnInfo(name = cDatabase.SOURCENO_NAMESTR)
    public String sourceno;
    @ColumnInfo(name = cDatabase.QUANTITYHANDLEDSUM_NAMESTR)
    public Double quantityhandled;
    @ColumnInfo(name = cDatabase.PROCESSINGSEQUENCE_NAMESTR)
    public String processingsequence;



    public cPickorderLinePackAndShipGroupBySourceNo(String pv_sourceno, Double pv_quantityhandled) {
        this.sourceno = pv_sourceno;
        this.quantityhandled = pv_quantityhandled;
    }
    public cPickorderLinePackAndShipGroupBySourceNo() {

    }

    public String getSourceno() {
        return sourceno;
    }

    public void setSourceno(String sourceno) {
        this.sourceno = sourceno;
    }

    public Double getQuantityhandled() {
        return quantityhandled;
    }

    public void setQuantityhandled(Double quantityhandled) {
        this.quantityhandled = quantityhandled;
    }
    public String getProcessingsequence() {
        return processingsequence;
    }

    public void setProcessingsequence(String processingsequence) {
        this.processingsequence = processingsequence;
    }
}
