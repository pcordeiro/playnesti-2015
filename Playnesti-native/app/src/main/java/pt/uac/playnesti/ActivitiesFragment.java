package pt.uac.playnesti;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import pt.uac.playnesti.client.PlaynestiService;
import pt.uac.playnesti.model.Event;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesFragment extends Fragment {
    private ActivitiesAdapter adapter;
    private ProgressBar progressBar;
    private ListView listView;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();

            if (PlaynestiService.BROADCAST_ACTION_GET_EVENTS.equals(action)) {
                final List<Event> events = intent.getParcelableArrayListExtra(PlaynestiService.BROADCAST_EXTRA_EVENTS);
                displayActivities(events);
            } else if (PlaynestiService.BROADCAST_ACTION_ERROR.equals(action)) {
                final String errorMessage = intent.getStringExtra(PlaynestiService.BROADCAST_EXTRA_ERROR);
                displayError(errorMessage);
            }
        }
    };

    public ActivitiesFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        // Setup intent filter
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlaynestiService.BROADCAST_ACTION_GET_EVENTS);
        intentFilter.addAction(PlaynestiService.BROADCAST_ACTION_ERROR);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_activities, container, false);

        adapter = new ActivitiesAdapter(getActivity());
        progressBar = (ProgressBar) rootView.findViewById(R.id.activities_progress);
        listView = (ListView) rootView.findViewById(R.id.activities_listview);
        listView.setAdapter(adapter);

        fetchActivities();
        return rootView;
    }

    private void fetchActivities() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        PlaynestiService.startActionGetActivities(getActivity());
    }

    private void displayActivities(final List<Event> activities) {
        adapter.loadItems(activities);
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    private void displayError(final String errorMessage) {
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    private static final class ActivitiesAdapter extends AbstractListViewAdapter<Event>  {
        protected ActivitiesAdapter(final Context context) {
            super(context);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        protected View inflateView(final LayoutInflater inflater) {
            return inflater.inflate(R.layout.event_list_item, null);
        }

        @Override
        protected ViewHolder<Event> createViewHolder(final View view) {
            return new EventViewHolder(view);
        }
    }
}
