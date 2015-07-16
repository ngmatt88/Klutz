package com.duckwarlocks.klutz.fragments;

/**
 * Created by raf0c on 07/07/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.duckwarlocks.klutz.MainActivity;
import com.duckwarlocks.klutz.adapters.OptionsAdapter;
import com.duckwarlocks.klutz.views.TypefaceSpan;
import com.duckwarlocks.klutz.vo.RowItem;
import com.duckwarlocks.klutz.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class NavigationDrawerFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    public ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerListView;
    private LinearLayout myLayout;
    public View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    String[] menutitles;
    TypedArray menuIcons;
    private List<RowItem> rowItems;
    private OptionsAdapter adapter;
    private Toolbar toolbar;
    public Fragment fragment;
    private ImageView imgProfilePic;
    //private Button btnRevokeAccess;
    public static final int RC_SIGN_IN = 0;
    private static final String TAG = "MainActivity";
    private static final int PROFILE_PIC_SIZE = 800;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton btnSignIn;
    private Button btnSignOut;
    private Context mContext;
    private Activity mActivity;
    private TextView txtName, txtEmail;
    private Context mLittleContext;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menutitles      = getResources().getStringArray(R.array.titles);
        menuIcons       = getResources().obtainTypedArray(R.array.icons);
        toolbar         = (Toolbar) super.getActivity().findViewById(R.id.tool_bar);
        rowItems        = new ArrayList<>();
        mContext        = getActivity().getApplicationContext();
        mLittleContext  = getActivity();
        mActivity       = getActivity();

        for (int i = 0; i < menutitles.length; i++) {
            RowItem items = new RowItem(menutitles[i], menuIcons.getResourceId(i, -1));
            rowItems.add(items);
        }
        menuIcons.recycle();
        adapter = new OptionsAdapter(mContext, rowItems);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
            System.out.println("soy onCreate navigation framget");
        }
        // Select either the default item (0) or the last selected item.
       // selectItem(mCurrentSelectedPosition);

    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout) {

        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        SpannableString s = new SpannableString("KLUTZ");
        s.setSpan(new TypefaceSpan(getActivity().getApplicationContext(), "Roboto-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(s);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                super.getActivity(),
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

        }; // Drawer Toggle Object Made
        mDrawerLayout.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
/*        if (!mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }*/
        mDrawerLayout.closeDrawers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayout = (LinearLayout) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        mDrawerListView = (ListView)      myLayout.findViewById(R.id.categories);
        btnSignIn       = (SignInButton) myLayout.findViewById(R.id.btn_sign_in);
        btnSignOut      = (Button)        myLayout.findViewById(R.id.btn_sign_out);
        txtName         = (TextView)      myLayout.findViewById(R.id.txtName);
        txtEmail        = (TextView)      myLayout.findViewById(R.id.txtEmail);
        imgProfilePic   = (ImageView)     myLayout.findViewById(R.id.imgProfilePic);
        //btnRevokeAccess = (Button)        myLayout.findViewById(R.id.btn_revoke_access);
        mDrawerListView.setAdapter(adapter);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        //btnRevokeAccess.setOnClickListener(this);
        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                fragment = null;

                switch (position) {
                    case 0:
                        fragment = new MainFragment();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        fragment = new SavedLocationsFragment();
                        mDrawerLayout.closeDrawers();
                        break;
                }

                if (fragment != null) {
                    MainActivity.setmCurrentFragment(fragment);

                    FragmentManager fragmentManager = ((FragmentActivity) mLittleContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.emptyFrameForFragment, fragment );
                    fragmentTransaction.commit();
                }
                mDrawerLayout.closeDrawers();
            }
        });

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return myLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                signInWithGplus();
                break;
            case R.id.btn_sign_out:
                signOutFromGplus();
                break;
            case R.id.btn_revoke_access:
                revokeGplusAccess();
                break;
        }
    }

    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {

        if (requestCode == RC_SIGN_IN) {
            if (responseCode != Activity.RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        if(mConnectionResult!=null){
            if (mConnectionResult.hasResolution()) {
                try {
                    mIntentInProgress = true;
                    mConnectionResult.startResolutionForResult(mActivity, RC_SIGN_IN);
                    System.out.println("Dentro del try");
                } catch (IntentSender.SendIntentException e) {
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                    System.out.println("Dentro del catch");
                }
            }
        }
    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            updateUI(false);
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status arg0) {
                    Log.e(TAG, "User access revoked!");
                    mGoogleApiClient.connect();
                    updateUI(false);
                }

            });
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            imgProfilePic.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }


    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */

    private void showGlobalContextActionBar() {

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }
    private void  setSupportToolbar(Toolbar tb){
         ((AppCompatActivity) getActivity()).setSupportActionBar(tb);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Log.e("INFO : ","User is connected!");
        // Get user's information
        getProfileInformation();

        // Update the UI after signin
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), mActivity, 0).show();//requires activity
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    private void getProfileInformation() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                System.out.println("Name: " + personName + ", plusProfile: " + personGooglePlusProfile + ", email: " + email );

                txtName.setText(personName);
                txtEmail.setText(email);
                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Log.e("ERROR","Person information is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
