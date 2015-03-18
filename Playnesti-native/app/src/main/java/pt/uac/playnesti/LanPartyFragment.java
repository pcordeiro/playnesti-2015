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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import pt.uac.playnesti.client.PlaynestiService;
import pt.uac.playnesti.model.Game;

public class LanPartyFragment extends Fragment {
    private GamesAdapter adapter;
    private ProgressBar progressBar;
    private ListView listView;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();

            if (PlaynestiService.BROADCAST_ACTION_GET_LAN_PARTY.equals(action)) {
                final List<Game> games = intent.getParcelableArrayListExtra(PlaynestiService.BROADCAST_EXTRA_LAN_PARTY);
                displayGames(games);
            } else if (PlaynestiService.BROADCAST_ACTION_ERROR.equals(action)) {
                final String errorMessage = intent.getStringExtra(PlaynestiService.BROADCAST_EXTRA_ERROR);
                displayError(errorMessage);
            }
        }
    };

    public LanPartyFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        // Setup intent filter
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlaynestiService.BROADCAST_ACTION_GET_LAN_PARTY);
        intentFilter.addAction(PlaynestiService.BROADCAST_ACTION_ERROR);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_activities, container, false);

        adapter = new GamesAdapter(getActivity());
        progressBar = (ProgressBar) rootView.findViewById(R.id.activities_progress);
        listView = (ListView) rootView.findViewById(R.id.activities_listview);
        listView.setAdapter(adapter);

        fetchGames();
        return rootView;
    }

    private void fetchGames() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        PlaynestiService.startActionGetLanParty(getActivity());
    }

    private void displayGames(final List<Game> games) {
        adapter.loadItems(games);
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    private void displayError(final String errorMessage) {
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    private static final class GamesAdapter extends AbstractListViewAdapter<Game> {
        protected GamesAdapter(final Context context) {
            super(context);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        protected View inflateView(final LayoutInflater inflater) {
            return inflater.inflate(R.layout.game_list_item, null);
        }

        @Override
        protected ViewHolder<Game> createViewHolder(final View view) {
            return new GameViewHolder(view);
        }
    }

    private static final class GameViewHolder extends AbstractListViewAdapter.ViewHolder<Game> {
        private final ImageView imageView;
        private final TextView titleView;
        private final TextView platformView;
        private final TextView playersView;
        private final TextView priceView;
        private final int imageWidth;
        private final int imageHeight;

        protected GameViewHolder(final View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.game_image);
            titleView = (TextView) view.findViewById(R.id.game_title);
            platformView = (TextView) view.findViewById(R.id.game_platform);
            playersView = (TextView) view.findViewById(R.id.game_players);
            priceView = (TextView) view.findViewById(R.id.game_price);
            imageWidth = context.getResources().getDimensionPixelSize(R.dimen.game_image_width);
            imageHeight = context.getResources().getDimensionPixelSize(R.dimen.game_image_height);
        }

        @Override
        protected void bind(final Game item) {
            Picasso.with(context)
                    .load(item.getThumbnailSrc())
                    .resize(imageWidth, imageHeight)
                    .centerInside()
                    .into(imageView);

            titleView.setText(item.getTitle());

            platformView.setText(item.getPlatform());

            if (item.getNumberOfPlayers().equals("1")) {
                playersView.setText(item.getNumberOfPlayers() + " jogador");
            } else {
                playersView.setText(item.getNumberOfPlayers() + " jogadores");
            }

            priceView.setText(item.getPrice());
        }
    }
}
