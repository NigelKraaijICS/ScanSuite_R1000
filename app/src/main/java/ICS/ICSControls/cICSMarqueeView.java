package ICS.ICSControls;

import android.text.TextUtils;
import android.widget.TextView;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cICSMarqueeView {
    public static void pSetMarqueeview(TextView textView) {
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(cAppExtension.activity.getResources().getInteger(R.integer.marqueeTextRepeatCount) );
        textView.setSingleLine(true);
        textView.setSelected(true);
    }
}
