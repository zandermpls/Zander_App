package com.figueroa.xzander.zander;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.figueroa.xzander.zander.fragments.BioFragment;
import com.figueroa.xzander.zander.fragments.NewsFragment;
import com.figueroa.xzander.zander.fragments.PortFragment;
import com.figueroa.xzander.zander.fragments.PressFragment;
import com.figueroa.xzander.zander.nav.NavAdapter;
import com.figueroa.xzander.zander.nav.NavItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class MainActivity extends Activity {

    // Nav Drawer Title
    private CharSequence navTitle;
    // App Title
    private CharSequence appTitle;
    // Setup Nav Drawer
    private DrawerLayout myDrawer;
    private ListView myDrawerList;
    private ActionBarDrawerToggle toggleNav;
    // nav items
    private String[] activityViews;
    // nav icons
    private TypedArray navIcons;
    private ArrayList<NavItem> navItems;
    private NavAdapter navAdapter;

    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        // load menu items
        activityViews = getResources().getStringArray(R.array.my_drawer_items);
        // nav drawer icons
        navIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //myDrawer.setDrawerShadow(R.drawable.shadow, GravityCompat.START);
        myDrawerList = (ListView) findViewById(R.id.my_nav_list);
        // initializing my nav drawer options
        navItems = new ArrayList<NavItem>();

        // add items to array
        navItems.add(new NavItem(activityViews[0], navIcons
                .getResourceId(0, -1)));
        navItems.add(new NavItem(activityViews[1], navIcons
                .getResourceId(1, -1)));
        navItems.add(new NavItem(activityViews[2], navIcons
                .getResourceId(2, -1)));
        navItems.add(new NavItem(activityViews[3], navIcons
                .getResourceId(3, -1)));

        // recycle
        navIcons.recycle();
        // on click
        myDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        navAdapter = new NavAdapter(getApplicationContext(),
                navItems);
        myDrawerList.setAdapter(navAdapter);

        // enabling action bar app icon to make it behave it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        toggleNav = new ActionBarDrawerToggle(this, myDrawer,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open
                R.string.app_name // nav drawer close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(appTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawer) {
                getActionBar().setTitle(navTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        myDrawer.setDrawerListener(toggleNav);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

    }
    // set up listener for when navigation drawer item tapped
    // Slide menu item listener
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    // Nav Drawer Toggle & Update functionality
    	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = myDrawer.isDrawerOpen(myDrawerList);
        // preparing for when I set up action bar
        menu.findItem(R.id.action_interact).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    // will work on action bar later just put the icon in and xml for now
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // toggle nav drawer on selecting action bar app icon/title
        if (toggleNav.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_interact) {

            // call chooser dialog fragment to send an email or interact - intents for interacting via sending email and sharing on social media
            chooseInteraction();


            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    // this is for my chooser for email vs. social media interaction
    private void chooseInteraction() {

        final CharSequence[] options = { "Email Zander", "Be Social With Zander", "Nothing Right Now"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("What Would You Like To Do?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (options[i].equals("Email Zander")){

                    // will use my emailOptions public intent to filter apps on device
                    String subject = "Hello Zander!";
                    String body = "For all inquiries, please include your name and contact information. Thank you for your message and/or inquiries!";

                    // intent to send email
                    Intent emailZander = new Intent(Intent.ACTION_SEND);
                    emailZander.putExtra(Intent.EXTRA_EMAIL, new String[]{"theartistzander@gmail.com"});
                    emailZander.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                    emailZander.putExtra(Intent.EXTRA_TEXT, body);

                    // chooser for email only intent
                    emailZander.setType("message/rfc822");

                    startActivity(emailOptions(emailZander, "Email Zander"));

                } else if (options[i].equals("Be Social With Zander")){

                    // display everything else
                    String social = "@XanderFigueroa oh hai";

                    // begin intent for social interaction
                    Intent beSocial = new Intent(Intent.ACTION_SEND);
                    beSocial.putExtra(Intent.EXTRA_TEXT, social);
                    beSocial.setType("text/plain");

                    // chooser for social media
                    startActivity(Intent.createChooser(beSocial, "Choose Your Social Media:"));


                } else if (options[i].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    // public intent to filter email app options on device like Gmail, Email, etc.
    public Intent emailOptions(Intent src, CharSequence chooser){

        Stack<Intent> intentToEmail = new Stack<Intent>();
        // add recipient
        Intent sendTo = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "theartistzander@gmail.com", null));
        List<ResolveInfo> emailApps = getPackageManager().queryIntentActivities(sendTo, 0);

        for(ResolveInfo resolve : emailApps) {
            Intent option = new Intent(src);
            option.setPackage(resolve.activityInfo.packageName);
            intentToEmail.add(option);
        }

        if (!intentToEmail.isEmpty()) {
            // good then apps on device that can send an email =)
            Intent choose = Intent.createChooser(intentToEmail.remove(0), chooser);
            choose.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentToEmail.toArray(new Parcelable[intentToEmail.size()]));
            return choose;

        } else {
            return Intent.createChooser(src, chooser);
        }

    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new BioFragment();
                break;
            case 1:
                fragment = new PortFragment();
                break;
            case 2:
                fragment = new PressFragment();
                break;

            case 3:
                fragment = new NewsFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            myDrawerList.setItemChecked(position, true);
            myDrawerList.setSelection(position);
            setTitle(activityViews[position]);
            myDrawer.closeDrawer(myDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        appTitle = title;
        getActionBar().setTitle(appTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggleNav.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggleNav.onConfigurationChanged(newConfig);
    }


}
