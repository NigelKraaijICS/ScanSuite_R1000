package SSU_WHS.Webservice;

public class cContainer {
    Long containerSequenceNo;
    Double quantityHandled;

    public cContainer(Long pv_containerSequenceNo, Double pv_quantityHandled) {
        containerSequenceNo = pv_containerSequenceNo;
        quantityHandled = pv_quantityHandled;
    }

    public Long getContainerSequenceNo() {
        return containerSequenceNo;
    }

    public void setContainerSequenceNo(Long containerSequenceNo) {
        this.containerSequenceNo = containerSequenceNo;
    }

    public Double getQuantityHandled() {
        return quantityHandled;
    }

    public void setQuantityHandled(Double quantityHandled) {
        this.quantityHandled = quantityHandled;
    }
}
