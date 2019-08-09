package SSU_WHS.PickorderLines;

public class cPickorderLine {
    public enum pickorderLineLocalStatusEnum {
        UNKNOWN,
        NEW,
        BUSY,
        DONE_NOTSENT,
        DONE_SENDING,
        DONE_SENT,
        DELETED
    }

    public final static int LOCALSTATUS_UNKNOWN = 0;
    public final static int LOCALSTATUS_DELETED = 1;
    public final static int LOCALSTATUS_NEW = 10;
    public final static int LOCALSTATUS_BUSY = 20;
    public final static int LOCALSTATUS_DONE_NOTSENT = 30;
    public final static int LOCALSTATUS_DONE_SENDING = 31;
    public final static int LOCALSTATUS_DONE_SENT = 32;

    public final static int STATUS_CREATING = 5;
    public final static int STATUS_STEP1= 10;
    public final static int STATUS_STEP1_TOREPORT = 11;
    public final static int STATUS_STEP2 = 20;
    public final static int STATUS_STEP2_TOREPORT = 21;
    public final static int STATUS_COMPLETED = 90;
}
