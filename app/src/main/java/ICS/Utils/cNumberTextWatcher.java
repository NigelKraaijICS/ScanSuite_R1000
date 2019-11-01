package ICS.Utils;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.widget.EditText;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import ICS.cAppExtension;

public class cNumberTextWatcher implements TextWatcher {

    private static final String TAG = "NumberTextWatcher";
    private final int numDecimals;

    private String groupingSep;
    private String decimalSep;
    private boolean nonUsFormat;
    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private EditText editText;
    private String value;

    private Double max;

    private String replicate(char ch, int n) {
        return new String(new char[n]).replace("\0", "" + ch);
    }

    public cNumberTextWatcher(EditText editText, int numDecimals, Double max) {

        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        this.numDecimals = numDecimals;
        this.max = max;


        DecimalFormatSymbols symbols = new DecimalFormatSymbols(mGetLocale());

        char gs = symbols.getGroupingSeparator();
        char ds = symbols.getDecimalSeparator();
        groupingSep = String.valueOf(gs);
        decimalSep = String.valueOf(ds);

        String patternInt = "####";
        dfnd = new DecimalFormat(patternInt, symbols);

        String patternDec = patternInt + "." + replicate('#', numDecimals);

        df = new DecimalFormat(patternDec, symbols);
        df.setDecimalSeparatorAlwaysShown(true);
        df.setRoundingMode(RoundingMode.DOWN);

        this.editText = editText;
        hasFractionalPart = false;

        nonUsFormat = !decimalSep.equals(".");
        value = null;

    }


    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged");
        editText.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = editText.getText().length();

            String v = value.replace(groupingSep, "");

            Number n = df.parse(v);
            Double nogeen = n.doubleValue();

            int cp = editText.getSelectionStart();
            if (hasFractionalPart) {
                int decPos = v.indexOf(decimalSep) + 1;
                int decLen = v.length() - decPos;
                if (decLen > numDecimals) {
                    v = v.substring(0, decPos + numDecimals);
                }
                int trz = countTrailingZeros(v);

                StringBuilder fmt = new StringBuilder(df.format(n));
                while (trz-- > 0) {
                    fmt.append("0");
                }
                editText.setText(fmt.toString());
            } else {

                if (nogeen > max) {
                    n=max;
                }
                editText.setText(dfnd.format(n));

            }

            endlen = editText.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= editText.getText().length()) {
                editText.setSelection(sel);
            } else {
                // place cursor at the end?
                editText.setSelection(editText.getText().length() - 1);
            }

        } catch (NumberFormatException | ParseException nfe) {
            // do nothing?
        }

        editText.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged");
        value = editText.getText().toString();
    }

    private int countTrailingZeros(String str) {
        int count = 0;

        for (int i = str.length() - 1; i >= 0; i--) {
            char ch = str.charAt(i);
            if ('0' == ch) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged");



        String newValue = s.toString();
        String change = newValue.substring(start, start + count);
        String prefix = value.substring(0, start);
        String suffix = value.substring(start + before);

        if (".".equals(change) && nonUsFormat) {
            change = decimalSep;
        }

        value = prefix + change + suffix;
        hasFractionalPart = value.contains(decimalSep);

        Log.d(TAG, "VALUE: " + value);

    }

    private Locale mGetLocale(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return cAppExtension.context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return cAppExtension.context.getResources().getConfiguration().locale;
        }
    }


}

