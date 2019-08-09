package SSU_WHS.Complex_types;


import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class c_BarcodeHandledUwbh implements KvmSerializable {

    public String g_BarcodeStr;
    public Double g_QuantityHandledDbl;

    public String getG_BarcodeStr() {
        return g_BarcodeStr;
    }

    public void setG_BarcodeStr(String g_BarcodeStr) {
        this.g_BarcodeStr = g_BarcodeStr;
    }

    public Double getG_QuantityHandledDbl() {
        return g_QuantityHandledDbl;
    }

    public void setG_QuantityHandledDbl(Double g_QuantityHandledDbl) {
        this.g_QuantityHandledDbl = g_QuantityHandledDbl;
    }

    //empty constructor
    public c_BarcodeHandledUwbh() {

    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return g_BarcodeStr;
            case 1:
                return g_QuantityHandledDbl;
        }
        return null;
    }
    @Override
    public int getPropertyCount() {
        return 2;
    }
    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
        switch(arg0){
            case 0:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "g_BarcodeStr";
                break;
            case 1:
                arg2.type = Double.class;
                arg2.name = "g_QuantityHandledDbl";
                break;
            default:
                break;
        }
    }
    @Override
    public void setProperty(int arg0, Object arg1) {
        switch(arg0){
            case 0:
                g_BarcodeStr = arg1.toString();
                break;
            case 1:
                g_QuantityHandledDbl = Double.parseDouble(arg1.toString());
                break;
            default:
                break;
        }
    }
}
