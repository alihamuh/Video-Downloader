package pheonix.KingKongVid.downloaaderVideoPackage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;

import pheonix.KingKongVid.downloaaderVideoPackage.bookmarks.Bookmarks;
import pheonix.KingKongVid.downloaaderVideoPackage.browsing.BrowserManager;
import pheonix.KingKongVid.downloaaderVideoPackage.download.fragments.Downloads;
import pheonix.KingKongVid.downloaaderVideoPackage.history.History;
import pheonix.KingKongVid.downloaaderVideoPackage.utils.Utils;

public class PheonixActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TextView.OnEditorActionListener, View.OnClickListener, AdapterView.OnItemClickListener {


    private EditText webBox;
    private BrowserManager browserManager;
    private Uri appLinkData;
    private DrawerLayout layout;
    Context context;
    ImageButton searchImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NavigationAppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ////////////////////////////////////////////////////////////////
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        initializingCurrentTab();

        //TextView about = findViewById(R.id.aboutDesc);
        //about.setMovementMethod(new ScrollingMovementMethod());
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);





        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_tab_black_24dp, this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        webBox = findViewById(R.id.searchEditText);



        webBox.setOnEditorActionListener(this);

        context =this;

        searchImage =(ImageButton)findViewById(R.id.search_image);



        webBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            String searchString="";
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    searchImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear_black_24dp));
                    Log.d("TEST","onfocusChanged");

                    //webBox.selectAll();

                    searchImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            searchString =webBox.getText().toString();
                            webBox.setText("");
                        }
                    });

                }else{

                    searchImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh_black_24dp));
                    Log.d("TEST","onfocusChangedfalse");
                    if(webBox.getText().toString().equals("")) {
                        webBox.setText(searchString);
                    }


                }
            }
        });

/*
        ConstraintLayout main =(ConstraintLayout) findViewById(R.id.main);

        main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchImage.setImageDrawable(getDrawable(R.drawable.ic_refresh_black_24dp));
                webBox.setText("");

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                webBox.clearFocus();
                return false;
            }
        });
*/




/*
        webBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //searchImage.setImageDrawable(getDrawable(R.drawable.ic_clear_black_24dp));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //searchImage.setImageDrawable(getDrawable(R.drawable.ic_clear_black_24dp));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/


/*
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webBox.setText("");
            }
        });
        /*
/*

*/
        /*ConstraintLayout main =(ConstraintLayout)findViewById(R.id.main);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLI","IT IS CLICKED, WOW");
            }
        });
*/

/*
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               searchbar.setFocusable(true);
               searchbar.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.showSoftInput( searchbar, InputMethodManager.SHOW_IMPLICIT);
                searchImage.setImageDrawable(getDrawable(R.drawable.ic_clear_black_24dp));
            }
        });
*/

        ///////////////////////////////////////////////////////////////////////////




        if ((browserManager = (BrowserManager) getFragmentManager().findFragmentByTag("BM")) == null) {
            getFragmentManager().beginTransaction().add(browserManager = new BrowserManager(),
                    "BM").commit();
        }
        Intent appLinkIntent = getIntent();
        //String appLinkAction = appLinkIntent.getAction();
        appLinkData = appLinkIntent.getData();

        /*
        ImageButton go = findViewById(R.id.go);
        go.setOnClickListener(this);

        if ((browserManager = (BrowserManager) getFragmentManager().findFragmentByTag("BM")) == null) {
            getFragmentManager().beginTransaction().add(browserManager = new BrowserManager(),
                    "BM").commit();
        }

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        //String appLinkAction = appLinkIntent.getAction();
        appLinkData = appLinkIntent.getData();
/*
        layout = findViewById(R.id.drawer);
        ImageView menu = findViewById(R.id.menuButton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.openDrawer(Gravity.START);
            }
        });
*/

/*
        ListView listView = findViewById(R.id.menu);
        String[] menuItems = new String[]{"Home", "Browser", "Downloads", "Bookmarks",
                "History", "About"};
        ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout
                .simple_list_item_1, menuItems) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        */
        /*
        Switch adBlockerSwitch = findViewById(R.id.adBlockerSwitch);
        final SharedPreferences prefs = getSharedPreferences("settings", 0);
        boolean adBlockOn = prefs.getBoolean(getString(R.string.adBlockON), true);
        adBlockerSwitch.setChecked(adBlockOn);
        adBlockerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean(getString(R.string.adBlockON), isChecked).apply();
            }
        });
        */
        GridView videoSites = findViewById(R.id.mygrid);
        videoSites.setAdapter(new VideoStreamingSitesList(this));
        //videoSites.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        new WebConnect(webBox, this).connect();
        unfoucusSearchBar(v);
        Log.d("TEST","onEDITORACTION");
        return false;
    }

    @Override
    public void onClick(View v) {
        //Log.d("TEST","ONCLICK");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        Log.d("TEST","ONCLICK");
        searchImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh_black_24dp));
        if (getCurrentFocus() != null) {
            Utils.hideSoftKeyboard(this, getCurrentFocus().getWindowToken());
            new WebConnect(webBox, this).connect();

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        layout.closeDrawers();
        switch (position) {
            case 0:
                homeClicked();
                break;
            case 1:
                browserClicked();
                break;
            case 2:
                downloadsClicked();
                break;
            case 3:
                bookmarksClicked();
                break;
            case 4:
                historyClicked();
                break;
            case 5:
                aboutClicked();
                break;
        }
    }



    public void homeClicked() {
        browserManager.hideCurrentWindow();
        closeDownloads();
        closeBookmarks();
        closeHistory();
        setOnBackPressedListener(null);
    }

    public void browserClicked() {
        browserManager.unhideCurrentWindow();
        closeDownloads();
        closeBookmarks();
        closeHistory();
    }

    private void downloadsClicked() {
        closeBookmarks();
        closeHistory();
        if (getFragmentManager().findFragmentByTag("Downloads") == null) {
            browserManager.hideCurrentWindow();
            getFragmentManager().beginTransaction().add(R.id.main, new Downloads(),
                    "Downloads").commit();
        }
    }

    private void bookmarksClicked() {
        closeDownloads();
        closeHistory();
        if (getFragmentManager().findFragmentByTag("Bookmarks") == null) {
            browserManager.hideCurrentWindow();
            getFragmentManager().beginTransaction().add(R.id.main, new Bookmarks(), "Bookmarks")
                    .commit();
        }
    }

    private void historyClicked() {
        closeDownloads();
        closeBookmarks();
        if (getFragmentManager().findFragmentByTag("History") == null) {
            browserManager.hideCurrentWindow();
            getFragmentManager().beginTransaction().add(R.id.main, new History(), "History")
                    .commit();
        }
    }

    private void aboutClicked() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.about, ((ViewGroup) this.getWindow().getDecorView()), false);
        new AlertDialog.Builder(this)
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void privacyClicked(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.privacy_policy, ((ViewGroup) this.getWindow().getDecorView()), false);
        new AlertDialog.Builder(this)
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }


    private void closeDownloads() {
        Fragment fragment = getFragmentManager().findFragmentByTag("Downloads");
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void closeBookmarks() {
        Fragment fragment = getFragmentManager().findFragmentByTag("Bookmarks");
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void closeHistory() {
        Fragment fragment = getFragmentManager().findFragmentByTag("History");
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    public interface OnBackPressedListener {
        void onBackpressed();
    }

    public void setOnBackPressedListener(PheonixActivity.OnBackPressedListener onBackPressedListener) {
        PheonixApp.getInstance().setOnBackPressedListener(onBackPressedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (appLinkData != null) {
            browserManager.newWindow(appLinkData.toString());
        }
       // browserManager.updateAdFilters();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionsResultCallback.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    private ActivityCompat.OnRequestPermissionsResultCallback onRequestPermissionsResultCallback;

    public void setOnRequestPermissionsResultListener(ActivityCompat
                                                              .OnRequestPermissionsResultCallback
                                                              onRequestPermissionsResultCallback) {
        this.onRequestPermissionsResultCallback = onRequestPermissionsResultCallback;
    }

    public BrowserManager getBrowserManager() {
        return browserManager;
    }
/////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        Fragment sourcePage = getFragmentManager().findFragmentByTag("updateSourcePage");
        if (sourcePage != null) {
            getFragmentManager().beginTransaction().remove(sourcePage).commit();
        } else if (PheonixApp.getInstance().getOnBackPressedListener() != null) {
            PheonixApp.getInstance().getOnBackPressedListener().onBackpressed();
        } else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {

            homeClicked();

        }else if(id ==R.id.history){

            historyClicked();

        }else if(id ==R.id.about){

            aboutClicked();

        }else if(id ==R.id.downloads){

            downloadsClicked();

        }else if(id ==R.id.browser){

            browserClicked();

        }else if(id==R.id.bookmarks){

            bookmarksClicked();

        }else if(id==R.id.privacy){

            privacyClicked();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e("TAG", "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        return super.onPrepareOptionsMenu(menu);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void unfoucusSearchBar(View view){
        searchImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh_black_24dp));
        webBox.clearFocus();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        hideKeyboard(PheonixActivity.this);
        webBox.clearFocus();
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCurrentFocus() != null) {
                    Utils.hideSoftKeyboard(PheonixActivity.this, getCurrentFocus().getWindowToken());
                    new WebConnect(webBox, PheonixActivity.this).connect();
                }
            }
        });
        //webBox.setFocusable(false);
    }

    public void initializingCurrentTab(){

        TextView webTitle =findViewById(R.id.current_windowTitle);
        webTitle.setText("Home");

        webTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //page.reload();
            }
        });

        ImageView favIcon =findViewById(R.id.current_fav_icon);
        favIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_black_24dp));






    }



}
