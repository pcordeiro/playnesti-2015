package pt.uac.playnesti.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 17-03-2015.
 */
public final class Image implements Parcelable {
    private String imageSrc;

    public static Parcelable.Creator<Image> CREATOR = new ClassLoaderCreator<Image>() {
        @Override
        public Image createFromParcel(final Parcel source, final ClassLoader loader) {
            return new Image(source);
        }

        @Override
        public Image createFromParcel(final Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[0];
        }
    };

    private Image(final Parcel in) {
        imageSrc = in.readString();
    }

    public Image() {
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(imageSrc);
    }
}
