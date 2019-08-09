package SSU_WHS.Pickorders;

import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;

public class cPickorder {
    public enum eOrderTypes {
        BC,
        BM,
        BP,
        EOM,
        EOOM,
        EOOS,
        EOR,
        EOS,
        ER,
        IVM,
        IVS,
        MAM,
        MAS,
        MAT,
        MI,
        MO,
        MT,
        MV,
        MVI,
        OMM,
        OMOM,
        OMOS,
        OMR,
        OMS,
        PA,
        PF,
        PV,
        RVR,
        RVS,
        SPV,
        UNKNOWN
    }

}
