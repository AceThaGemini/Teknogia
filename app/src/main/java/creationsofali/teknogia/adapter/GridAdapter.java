package creationsofali.teknogia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import creationsofali.teknogia.R;
import creationsofali.teknogia.activities.ContactUsActivity;
import creationsofali.teknogia.activities.ProfileActivity;
import creationsofali.teknogia.datamodels.Muhuni;
import creationsofali.teknogia.helpers.EmailHelper;
import creationsofali.teknogia.helpers.PermissionsHelper;
import creationsofali.teknogia.helpers.PhoneCallHelper;
import creationsofali.teknogia.helpers.TwitterHelper;

/**
 * Created by ali on 6/16/17.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private ArrayList<Muhuni> crewList;
    private Context context;
    private ContactUsActivity activity;
    private Gson gson;
    private Animation animationScaleIn;

    public GridAdapter(Context context, ArrayList<Muhuni> crewList) {
        this.crewList = crewList;
        this.context = context;
        this.activity = (ContactUsActivity) context;
        this.gson = new Gson();
        this.animationScaleIn = AnimationUtils.loadAnimation(context, R.anim.anim_scale_in);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageDp, iconEmail, iconPhone, iconTwitter, iconInstagram;
        TextView textName, textWhoIsHe;

        ViewHolder(View itemView) {
            super(itemView);

            imageDp = itemView.findViewById(R.id.imageDp);
            iconEmail = itemView.findViewById(R.id.iconEmail);
            iconPhone = itemView.findViewById(R.id.iconPhone);
            iconTwitter = itemView.findViewById(R.id.iconTwitter);
            iconInstagram = itemView.findViewById(R.id.iconInstagram);
            textName = itemView.findViewById(R.id.textName);
            textWhoIsHe = itemView.findViewById(R.id.textWhoIsHe);

            itemView.findViewById(R.id.layoutDp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // do magic
                    //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    //     imageDp.setTransitionName(context.getString(R.string.share_image_dp));

                    String gsonProfile = gson.toJson(crewList.get(getAdapterPosition()));

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(
                                    activity, imageDp, context.getString(R.string.shared_image_dp));

                    Intent profileIntent = new Intent(context, ProfileActivity.class);
                    profileIntent.putExtra("profile", gsonProfile)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(profileIntent, optionsCompat.toBundle());
                }
            });

            iconEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = crewList.get(getAdapterPosition()).getEmail();
                    if (email != null)
                        EmailHelper.launchComposeEmail(email, context);
                    else
                        Toast.makeText(context, "No email defined yet.", Toast.LENGTH_SHORT).show();
                }
            });

            iconTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String screenName = crewList.get(getAdapterPosition()).getTwitterName();
                    if (screenName != null)
                        TwitterHelper.launchTwitter(screenName, context);
                    else
                        Toast.makeText(context, "No account defined yet.", Toast.LENGTH_SHORT).show();

                }
            });

            iconPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = crewList.get(getAdapterPosition()).getPhone();
                    if (phone != null) {
                        // check permission
                        if (PermissionsHelper.hasCallPermission(context))
                            PhoneCallHelper.callPhone(context, phone);
                        else
                            // request permission
                            PermissionsHelper.requestCallPermission(activity);

                    } else
                        // no phone
                        Toast.makeText(context, "New phone who dis?", Toast.LENGTH_SHORT).show();
                }
            });

            iconInstagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "No account defined yet.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_team, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // from Surname, FirstName -> FirstName
        holder.itemView.startAnimation(animationScaleIn);
        String[] names = crewList.get(position).getName().split(", ");
        // FirstName (Nickname)
        holder.textName.setText(names[1]);
        holder.textName.append(" (" + crewList.get(position).getNickName() + ")");
        holder.textWhoIsHe.setText(crewList.get(position).getShortDesc());
        Drawable image = ContextCompat.getDrawable(context, crewList.get(position).getImageDp());
        holder.imageDp.setImageDrawable(image);
    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

}
