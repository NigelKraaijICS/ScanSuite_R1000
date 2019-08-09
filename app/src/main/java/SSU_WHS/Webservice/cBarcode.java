package SSU_WHS.Webservice;


public class cBarcode {

    String barcode;
    Double quantityHandled;

    public cBarcode(String pv_barcode, Double pv_quantityHandled) {
        barcode = pv_barcode;
        quantityHandled = pv_quantityHandled;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getQuantityHandled() {
        return quantityHandled;
    }

    public void setQuantityHandled(Double quantityHandled) {
        this.quantityHandled = quantityHandled;
    }
}
