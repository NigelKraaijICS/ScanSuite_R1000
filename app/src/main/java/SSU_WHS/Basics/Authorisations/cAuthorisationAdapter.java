package SSU_WHS.Basics.Authorisations;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Utils.cImages;
import ICS.cAppExtension;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.R;

public class cAuthorisationAdapter extends RecyclerView.Adapter<cAuthorisationAdapter.AuthorisationViewHolder> {

    //Region Public Properties
    public class AuthorisationViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewAuthorisation;
        private ImageView imageViewAuthorisation;
        public LinearLayout authorisationItemLinearLayout;

        public AuthorisationViewHolder(View itemView) {
            super(itemView);
            this.textViewAuthorisation = itemView.findViewById(R.id.textViewAuthorisationItem);
            this.textViewAuthorisation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewAuthorisation.setSingleLine(true);
            this.textViewAuthorisation.setMarqueeRepeatLimit(5);
            this.textViewAuthorisation.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewAuthorisation.setSelected(true);
                }
            },1500);
            this.imageViewAuthorisation = itemView.findViewById(R.id.imageViewBinItem);
            this.authorisationItemLinearLayout = itemView.findViewById(R.id.authorisationItemLinearLayout);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cAuthorisationAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods
    @NonNull
    @Override
    public cAuthorisationAdapter.AuthorisationViewHolder onCreateViewHolder(@NonNull ViewGroup pvViewGroup, int pvViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_authorisation, pvViewGroup, false);
        return new cAuthorisationAdapter.AuthorisationViewHolder(itemView);
    }

    public void setAuthorisations() {
       notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull cAuthorisationAdapter.AuthorisationViewHolder pvHolder, int pvPositionInt) {
        if (cUser.currentUser.autorisationWithoutMergeObl() != null) {
            final cAuthorisation authorisation = cUser.currentUser.autorisationWithoutMergeObl().get(pvPositionInt);

            if (authorisation.getCustomAuthorisation() != null) {
                pvHolder.textViewAuthorisation.setText(authorisation.getCustomAuthorisation().getDescriptionStr());
                pvHolder.imageViewAuthorisation.setImageBitmap(authorisation.customImageBmp());
            }
            else
                {
                if (authorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.EXTERNAL_RETURN) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_external_return);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_external_return);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INTAKE);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INTAKE);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_EO) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_eo);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_eo);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INTAKE);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INTAKE);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_MA) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_ma);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_ma);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INTAKE);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INTAKE);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKE_OM) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_om);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intake_om);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INTAKECLOSE) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_intake_close);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_intakeclose);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INVENTORY) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_inventory);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_inventory);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_INVENTORY);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_INVENTORY);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.INVENTORYCLOSE) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_inventoryclose);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_inventoryclose);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_move);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_MOVE);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_MOVE);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MI) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mi);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mi);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MI_SINGLEPIECE) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mi_singlepiece);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mi);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MO) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mo);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mo);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVE_MV) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_move_mv);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_move_mv);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_MOVE);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_MOVE);
                }
                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.MOVEITEM) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_moveitem);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_moveitem);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_pick);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PICK);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PICK);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SORTING) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_sort);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_sort);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_SORT);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_SORT);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SHIPPING) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_ship);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_ship);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_SHIP);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_SHIP);
                }

                    if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.FINISH_SHIPPING) {
                        pvHolder.textViewAuthorisation.setText(R.string.menuitem_finish_ship);
                        pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_ship);
                        pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_FINISH_SHIP);
                        pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_FINSIH_SHIP);
                    }

                    if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.QC) {
                        pvHolder.textViewAuthorisation.setText(R.string.menuitem_qc);
                        pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_qualitycontrol);
                        pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_QC);
                        pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_QC);
                    }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PF) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_pick_pf);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick_pf);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PICK);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PICK);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.PICK_PV) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_pick_pv);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_pick_pv);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_PICK);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_PICK);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.RETURN) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_return);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_return);
                    pvHolder.imageViewAuthorisation.setTag(cAuthorisation.TAG_IMAGE_RETURN);
                    pvHolder.textViewAuthorisation.setTag(cAuthorisation.TAG_TEXT_RETURN);
                }

                if (authorisation.getAutorisationEnu() ==  cAuthorisation.AutorisationEnu.SELFPICK) {
                    pvHolder.textViewAuthorisation.setText(R.string.menuitem_selfpick);
                    pvHolder.imageViewAuthorisation.setImageResource(R.drawable.ic_menu_selfpick);
                }
            }

            pvHolder.authorisationItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cAppExtension.context instanceof MenuActivity) {
                        cUser.currentUser.currentAuthorisation = authorisation;
                        MenuActivity menuActivity = new MenuActivity();
                        menuActivity.pAuthorisationSelected();
                     }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cUser.currentUser.autorisationWithoutMergeObl() != null)
            return cUser.currentUser.autorisationWithoutMergeObl().size();
        else return 0;
    }
    //End Region Public Methods
}
