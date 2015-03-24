package com.figueroa.xzander.zander.nav;

import java.util.ArrayList;

import com.figueroa.xzander.zander.*;
import com.figueroa.xzander.zander.nav.NavItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xzander on 10/9/14.
 */
public class NavAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavItem> navItems;

    public NavAdapter(Context context, ArrayList<NavItem> navItems){

        this.context = context;
        this.navItems = navItems;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return navItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View myView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // checking if myView exists of navigation drawer, will instantiate in MainActivity.java
        if (myView == null){
            // inflate the view calling in my nav_drawer_item xml
            LayoutInflater inflateView = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            myView = inflateView.inflate(R.layout.nav_drawer_item, null);

        }

        ImageView iconImg = (ImageView) myView.findViewById(R.id.icon);
        TextView titleText = (TextView) myView.findViewById(R.id.title);

        iconImg.setImageResource(navItems.get(position).getIcon());
        titleText.setText(navItems.get(position).getTitle());


        return myView;
    }



}
