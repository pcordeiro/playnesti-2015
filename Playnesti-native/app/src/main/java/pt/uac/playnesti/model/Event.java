package pt.uac.playnesti.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 24-02-2015.
 */
public final class Event implements Parcelable {
    private String title;
    private String subTitle;
    private String thumbnailSrc;
    private String day;
    private String schedule;
    private String location;

    public static Parcelable.Creator<Event> CREATOR = new ClassLoaderCreator<Event>() {
        @Override
        public Event createFromParcel(final Parcel source, final ClassLoader loader) {
            return new Event(source);
        }

        @Override
        public Event createFromParcel(final Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[0];
        }
    };

    private Event(final Parcel in) {
        title = in.readString();
        subTitle = in.readString();
        thumbnailSrc = in.readString();
        day = in.readString();
        schedule = in.readString();
        location = in.readString();
    }

    public Event() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getThumbnailSrc() {
        return thumbnailSrc;
    }

    public void setThumbnailSrc(String thumbnailSrc) {
        this.thumbnailSrc = thumbnailSrc;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subTitle);
        dest.writeString(thumbnailSrc);
        dest.writeString(day);
        dest.writeString(schedule);
        dest.writeString(location);
    }
}
