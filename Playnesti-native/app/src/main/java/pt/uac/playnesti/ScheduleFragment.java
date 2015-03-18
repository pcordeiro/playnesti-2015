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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pt.uac.playnesti.client.PlaynestiService;
import pt.uac.playnesti.model.Event;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ScheduleFragment extends Fragment {
    static final String[] FILTER_LABELS =  { "Programa", "Dia 18", "Dia 19", "Dia 20", "Dia 21", "Dia 22" };
    static final String[] FILTER_VALUES = { null, "18", "19", "20", "21", "22" };

    private ScheduleListAdapter adapter;
    private ProgressBar progressBar;
    private StickyListHeadersListView listView;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();

            if (MainActivity.BROADCAST_ACTION_SPINNER.equals(action)) {
                final int index = intent.getIntExtra(MainActivity.BROADCAST_EXTRA_INDEX, 0);
                fetchEvents(FILTER_VALUES[index]);
            } else if (PlaynestiService.BROADCAST_ACTION_GET_EVENTS.equals(action)) {
                final List<Event> events = intent.getParcelableArrayListExtra(PlaynestiService.BROADCAST_EXTRA_EVENTS);
                displayEvents(events);
            } else if (PlaynestiService.BROADCAST_ACTION_ERROR.equals(action)) {
                final String errorMessage = intent.getStringExtra(PlaynestiService.BROADCAST_EXTRA_ERROR);
                displayError(errorMessage);
            }
        }
    };

    public ScheduleFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        // Setup intent filter
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlaynestiService.BROADCAST_ACTION_GET_EVENTS);
        intentFilter.addAction(PlaynestiService.BROADCAST_ACTION_ERROR);
        intentFilter.addAction(MainActivity.BROADCAST_ACTION_SPINNER);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        adapter = new ScheduleListAdapter(getActivity());
        progressBar = (ProgressBar) rootView.findViewById(R.id.schedule_progress);
        listView = (StickyListHeadersListView) rootView.findViewById(R.id.schedule_listview);
        listView.setAdapter(adapter);

        fetchEvents(FILTER_VALUES[0]);
        return rootView;
    }

    private void fetchEvents(final String day) {
        final Context context = getActivity();

        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

        if (day == null) {
            PlaynestiService.startActionGetAllEvents(context);
        } else {
            PlaynestiService.startActionGetEventsByDay(context, day);
        }
    }

    private void displayEvents(final List<Event> events) {
        adapter.loadItems(events);
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    private void displayError(final String errorMessage) {
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    private static final class ScheduleListAdapter extends AbstractListViewAdapter<Event> implements StickyListHeadersAdapter {
        protected ScheduleListAdapter(final Context context) {
            super(context);
        }

        @Override
        public long getItemId(final int position) {
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

        @Override
        public View getHeaderView(int i, View view, ViewGroup viewGroup) {
            final Event event = getItem(i);
            final HeaderViewHolder headerHolder;

            if (view == null) {
                view = getInflater().inflate(R.layout.event_list_header_item, null);
                headerHolder = new HeaderViewHolder(view);
            } else {
                headerHolder = (HeaderViewHolder) view.getTag();
            }

            headerHolder.bind(event);
            return view;
        }

        @Override
        public long getHeaderId(int i) {
            return Integer.parseInt(getItem(i).getDay());
        }
    }

    private static final class HeaderViewHolder {
        private final TextView title;

        HeaderViewHolder(final View view) {
            this.title = (TextView) view.findViewById(R.id.event_day_header);
            view.setTag(this);
        }

        void bind(final Event event) {
            title.setText("Dia " + event.getDay());
        }
    }
}
