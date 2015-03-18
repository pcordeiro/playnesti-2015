package pt.uac.playnesti.client;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.uac.playnesti.model.Event;
import pt.uac.playnesti.model.Game;
import pt.uac.playnesti.model.Image;

public class PlaynestiService extends IntentService {
    private static final String TAG = "PlaynestiService";
    // Broadcast intent filters
    public static final String BROADCAST_ACTION_GET_EVENTS = "pt.uac.playnesti.broadcast.action.GET_EVENTS";
    public static final String BROADCAST_ACTION_GET_LAN_PARTY = "pt.uac.playnesti.broadcast.action.GET_LAN_PARTY";
    public static final String BROADCAST_ACTION_ERROR = "pt.uac.playnesti.broadcast.action.ERROR";
    public static final String BROADCAST_ACTION_GET_GALLERY = "pt.uac.playnesti.broadcast.action.GET_GALLERY";

    // Broadcast extras
    public static final String BROADCAST_EXTRA_EVENTS = "pt.uac.playnesti.broadcast.extra.EVENTS";
    public static final String BROADCAST_EXTRA_LAN_PARTY = "pt.uac.playnesti.broadcast.extra.LAN_PARTY";
    public static final String BROADCAST_EXTRA_ERROR = "pt.uac.playnesti.broadcast.extra.ERROR";
    public static final String BROADCAST_EXTRA_GALLERY = "pt.uac.playnesti.broadcast.extra.GALLERY";

    // Actions
    private static final String ACTION_GET_ALL_EVENTS = "pt.uac.playnesti.client.action.GET_ALL_EVENTS";
    private static final String ACTION_GET_EVENTS_BY_DAY = "pt.uac.playnesti.client.action.GET_EVENTS_BY_DAY";
    private static final String ACTION_GET_ACTIVITIES = "pt.uac.playnesti.client.action.GET_ACTIVITIES";
    private static final String ACTION_GET_LAN_PARTY = "pt.uac.playnesti.client.action.GET_LAN_PARTY";
    private static final String ACTION_GET_GALLERY = "pt.uac.playnesti.client.action.GET_GALLERY";

    // Parameters
    private static final String EXTRA_PARAM_DAY = "pt.uac.playnesti.client.extra.DAY";


    public PlaynestiService() {
        super("PlaynestiService");
    }

    public static void startActionGetAllEvents(final Context context) {
        final Intent intent = new Intent(context, PlaynestiService.class);
        intent.setAction(ACTION_GET_ALL_EVENTS);
        context.startService(intent);
    }

    public static void startActionGetEventsByDay(final Context context, final String day) {
        final Intent intent = new Intent(context, PlaynestiService.class);
        intent.setAction(ACTION_GET_EVENTS_BY_DAY);
        intent.putExtra(EXTRA_PARAM_DAY, day);
        context.startService(intent);
    }

    public static void startActionGetActivities(final Context context) {
        final Intent intent = new Intent(context, PlaynestiService.class);
        intent.setAction(ACTION_GET_ACTIVITIES);
        context.startService(intent);
    }

    public static void startActionGetLanParty(final Context context) {
        final Intent intent = new Intent(context, PlaynestiService.class);
        intent.setAction(ACTION_GET_LAN_PARTY);
        context.startService(intent);
    }

    public static void startActionGetGallery(final Context context) {
        final Intent intent = new Intent(context, PlaynestiService.class);
        intent.setAction(ACTION_GET_GALLERY);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final PlaynestiClient client = new PlaynestiClient();

            try {
                if (ACTION_GET_ALL_EVENTS.equals(action)) {
                    broadcastEvents(client.getAllEvents());
                } else if (ACTION_GET_EVENTS_BY_DAY.equals(action)) {
                    final String day = intent.getStringExtra(EXTRA_PARAM_DAY);
                    broadcastEvents(client.getEvents(day));
                } else if (ACTION_GET_ACTIVITIES.equals(action)) {
                    broadcastEvents(client.getAllActivities());
                } else if (ACTION_GET_LAN_PARTY.equals(action)) {
                    broadcastLanParty(client.getAllGames());
                } else if (ACTION_GET_GALLERY.equals(action)) {
                    broadcastGallery(client.getGallery());
                }
            } catch (Exception ex) {
                Log.e(TAG, null, ex);
                broadcastError(ex);
            }
        }
    }

    private void broadcastEvents(final List<Event> events) {
        final Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_GET_EVENTS);
        intent.putParcelableArrayListExtra(BROADCAST_EXTRA_EVENTS, (ArrayList<Event>) events);
        sendBroadcast(intent);
    }

    private void broadcastLanParty(final List<Game> games) {
        final Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_GET_LAN_PARTY);
        intent.putParcelableArrayListExtra(BROADCAST_EXTRA_LAN_PARTY, (ArrayList<Game>) games);
        sendBroadcast(intent);
    }

    private void broadcastGallery(final List<Image> gallery) {
        final Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_GET_GALLERY);
        intent.putParcelableArrayListExtra(BROADCAST_EXTRA_GALLERY, (ArrayList<Image>) gallery);
        sendBroadcast(intent);
    }

    private void broadcastError(final Exception ex) {
        final Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_ERROR);
        intent.putExtra(BROADCAST_EXTRA_ERROR, ex.getMessage());
        sendBroadcast(intent);
    }
}
