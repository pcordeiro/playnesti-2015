package pt.uac.playnesti.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 24-02-2015.
 */
public final class Game implements Parcelable {
    private String title;
    private String thumbnailSrc;
    private String platform;
    private String numberOfPlayers;
    private String price;

    public static Parcelable.Creator<Game> CREATOR = new ClassLoaderCreator<Game>() {
        @Override
        public Game createFromParcel(final Parcel source, final ClassLoader loader) {
            return new Game(source);
        }

        @Override
        public Game createFromParcel(final Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[0];
        }
    };

    private Game(final Parcel in) {
        title = in.readString();
        thumbnailSrc = in.readString();
        platform = in.readString();
        numberOfPlayers = in.readString();
        price = in.readString();
    }

    public Game() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailSrc() {
        return thumbnailSrc;
    }

    public void setThumbnailSrc(String thumbnailSrc) {
        this.thumbnailSrc = thumbnailSrc;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(String numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(title);
        dest.writeString(thumbnailSrc);
        dest.writeString(platform);
        dest.writeString(numberOfPlayers);
        dest.writeString(price);
    }
}
