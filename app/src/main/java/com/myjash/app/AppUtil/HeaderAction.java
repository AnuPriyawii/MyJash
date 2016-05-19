package com.myjash.app.AppUtil;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myjash.app.R;

/**
 * Created by ubundu on 26/4/16.
 */
public class HeaderAction {
    RelativeLayout lytMenu;
    DrawerLayout drawerLayout;
    TextView txtTitle;

    public HeaderAction(View view, Activity activity) {
        lytMenu = (RelativeLayout) view.findViewById(R.id.lytMenu);
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawerLayout);
//        txtTitle = (TextView) activity.findViewById(R.id.txtHeaderTitle);
//        txtTitle.setText(msg.toUpperCase());
        lytMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout != null)
                    drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }
}
