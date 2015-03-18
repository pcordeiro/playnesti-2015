package pt.uac.playnesti.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pt.uac.playnesti.model.Event;
import pt.uac.playnesti.model.Game;
import pt.uac.playnesti.model.Image;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 16-03-2015.
 */
final class PlaynestiClient {
    private static final String BASE_URL = "http://private-f2acc-playnesti.apiary-mock.com";
    private static final String SCHEDULE_URL = BASE_URL + "/schedule";
    private static final String ACTIVITIES_URL = BASE_URL + "/activities";
    private static final String LAN_PARTY_URL = BASE_URL + "/lanparty";
    private static final String GALLERY_URL = BASE_URL + "/gallery";

    public List<Event> getAllEvents() throws JSONException, IOException {
        final String result = get(SCHEDULE_URL);
        return readEvents(result);
    }

    public List<Event> getEvents(final String day) throws JSONException, IOException {
        final String url = String.format("%s/%s", SCHEDULE_URL, day);
        final String result = get(url);
        return readEvents(result);
    }

    public List<Event> getAllActivities() throws JSONException, IOException {
        final String result = get(ACTIVITIES_URL);
        return readEvents(result);
    }

    public List<Game> getAllGames() throws JSONException, IOException {
        final String result = get(LAN_PARTY_URL);
        final JSONArray array = new JSONArray(result);
        final List<Game> games = new ArrayList<>();

        for (int i = 0; i < array.length(); ++i) {
            final JSONObject object = array.getJSONObject(i);
            final Game game = new Game();

            game.setTitle(object.getString("title"));
            game.setThumbnailSrc(object.getString("thumbnailSrc"));
            game.setPlatform(object.getString("platform"));
            game.setNumberOfPlayers(object.getString("players"));
            game.setPrice(object.getString("price"));

            games.add(game);
        }

        return games;
    }

    public List<Image> getGallery() throws JSONException, IOException {
        final String result = get(GALLERY_URL);
        final JSONArray array = new JSONArray(result);
        final List<Image> gallery = new ArrayList<>();

        for (int i = 0; i < array.length(); ++i) {
            final JSONObject object = array.getJSONObject(i);
            final Image image = new Image();

            image.setImageSrc(object.getString("imageSrc"));

            gallery.add(image);
        }

        return gallery;
    }

    private List<Event> readEvents(final String rawJson) throws JSONException {
        final JSONArray array = new JSONArray(rawJson);
        final List<Event> events = new ArrayList<>();

        for (int i = 0; i < array.length(); ++i) {
            final JSONObject object = array.getJSONObject(i);
            final Event event = new Event();

            event.setTitle(object.getString("title"));

            if (!object.isNull("subtitle"))
                event.setSubTitle(object.getString("subtitle"));

            if (!object.isNull("thumbnailSrc"))
                event.setThumbnailSrc(object.getString("thumbnailSrc"));

            if (!object.isNull("day"))
                event.setDay(object.getString("day"));

            event.setSchedule(object.getString("schedule"));

            if (!object.isNull("location"))
                event.setLocation(object.getString("location"));

            events.add(event);
        }

        return events;
    }

    private String get(final String uri) throws IOException {
        final URL url = new URL(uri);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        final StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            String line;

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } finally {
            close(reader);
        }

        return sb.toString();
    }

    private void close(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ex) {
            // Ignore the error
        }
    }
}
