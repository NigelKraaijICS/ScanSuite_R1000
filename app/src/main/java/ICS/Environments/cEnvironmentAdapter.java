package ICS.Environments;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import ICS.Utils.Scanning.cBarcodeGenerator;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;
import nl.icsvertex.scansuite.R;

public class cEnvironmentAdapter extends RecyclerView.Adapter<cEnvironmentAdapter.EnvironmentViewHolder>{


    //Region Public Properties
    public static class EnvironmentViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewDescription;
        private final TextView textViewName;
        private final TextView textViewURL;
        private final ImageView imageQRCode;
        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public EnvironmentViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewDescription = pvItemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);
            this.textViewName = pvItemView.findViewById(R.id.textViewName);
            this.textViewName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewName.setSingleLine(true);
            this.textViewName.setMarqueeRepeatLimit(5);
            this.textViewName.setSelected(true);
            this.textViewURL = pvItemView.findViewById(R.id.textViewURL);
            this.textViewURL.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewURL.setSingleLine(true);
            this.textViewURL.setMarqueeRepeatLimit(5);
            this.textViewURL.setSelected(true);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
            this.imageQRCode = pvItemView.findViewById(R.id.imageQRCode);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cEnvironmentAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cEnvironmentAdapter.EnvironmentViewHolder onCreateViewHolder(@NonNull ViewGroup pvViewGroup, int pbViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_environment, pvViewGroup, false);
        return new EnvironmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final cEnvironmentAdapter.EnvironmentViewHolder pvHolder, int pvPositionInt) {

        if (cEnvironment.allEnviroments != null) {

            final cEnvironment environment = cEnvironment.allEnviroments.get(pvPositionInt);

            pvHolder.textViewDescription.setText(environment.getDescriptionStr());
            pvHolder.textViewName.setText(environment.getNameStr());
            pvHolder.textViewURL.setText(environment.getWebserviceURLStr());

            pvHolder.imageQRCode.setOnClickListener(view -> pvHolder.imageQRCode.setVisibility(View.GONE));

            pvHolder.viewForeground.setOnClickListener(v -> {
                cEnvironment.pSetCurrentEnviroment(environment);

                if (cAppExtension.context instanceof MainDefaultActivity) {
                    MainDefaultActivity mainDefaultActivity = (MainDefaultActivity)cAppExtension.activity;
                    mainDefaultActivity.pSetChosenEnvironment();
                }

            });
            pvHolder.viewForeground.setOnLongClickListener(view -> {
                String barcodeText = "name=" + environment.getNameStr() + "|";
                barcodeText += "description=" + environment.getDescriptionStr() +"|";
                barcodeText += "url=" + environment.getWebserviceURLStr() + "|";
                barcodeText += "default=f";

                try {
                    Bitmap qrCodeImage = cBarcodeGenerator.encodeAsBitmap(barcodeText, BarcodeFormat.QR_CODE, 400, 400);
                    pvHolder.imageQRCode.setImageBitmap(qrCodeImage);
                    pvHolder.imageQRCode.setVisibility(View.VISIBLE);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                return true;
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cEnvironment.allEnviroments != null)
            return cEnvironment.allEnviroments.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Private Methods


    //End Region Private Methods


}
