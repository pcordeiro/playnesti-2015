package pt.uac.playnesti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.Arrays;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    static final String BROADCAST_ACTION_SPINNER = "pt.playnesti.broadcast.action.SPINNER";
    static final String BROADCAST_EXTRA_INDEX = "pt.playnesti.broadcast.extra.INDEX";

    private NavigationDrawerFragment navigationDrawerFragment;
    private CharSequence title = "Programa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment fragment;
        final String[] items;

        switch (position) {
            case 0:
                fragment = new ScheduleFragment();
                title = getString(R.string.title_section_schedule);
                items = ScheduleFragment.FILTER_LABELS;
                break;
            case 1:
                fragment = new ActivitiesFragment();
                title = getString(R.string.title_section_activities);
                items = null;
                break;
            case 2:
                fragment = new LanPartyFragment();
                title = getString(R.string.title_section_lan_party);
                items = null;
                break;
            case 3:
                fragment = new GalleriesFragment();
                title = getString(R.string.title_section_galleries);
                items = null;
                break;
            case 4:
                fragment = new ContactsFragment();
                title = getString(R.string.title_section_contacts);
                items = null;
                break;
            case 5:
                fragment = new AboutFragment();
                title = getString(R.string.title_section_about);
                items = null;
                break;
            default:
                throw new IllegalArgumentException();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        if (items != null && items.length > 0) {
            setNavigationModeSpinner(items);
        } else {
            resetActionBar();
        }
    }

    @SuppressWarnings("deprecation")
    public void resetActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    @SuppressWarnings("deprecation")
    public void setNavigationModeSpinner(final String[] items) {
        final ActionBar actionBar = getSupportActionBar();
        final SpinnerAdapter adapter = new ArrayAdapter<>(actionBar.getThemedContext(),
                android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1,
                Arrays.asList(items));

        actionBar.setTitle(title);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                final Intent intent = new Intent();

                intent.setAction(BROADCAST_ACTION_SPINNER);
                intent.putExtra(BROADCAST_EXTRA_INDEX, i);
                sendBroadcast(intent);

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
