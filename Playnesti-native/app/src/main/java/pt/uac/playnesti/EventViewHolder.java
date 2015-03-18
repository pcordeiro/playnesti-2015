package pt.uac.playnesti;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pt.uac.playnesti.model.Event;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 17-03-2015.
 */
final class EventViewHolder extends AbstractListViewAdapter.ViewHolder<Event> {
    private final ImageView imageView;
    private final TextView titleView;
    private final TextView subTitleView;
    private final TextView scheduleView;
    private final TextView locationView;
    private final int imageWidth;
    private final int imageHeight;

    protected EventViewHolder(final View view) {
        super(view);

        imageView = (ImageView) view.findViewById(R.id.event_image);
        titleView = (TextView) view.findViewById(R.id.event_title);
        subTitleView = (TextView) view.findViewById(R.id.event_subtitle);
        scheduleView = (TextView) view.findViewById(R.id.event_schedule);
        locationView = (TextView) view.findViewById(R.id.event_location);
        imageWidth = context.getResources().getDimensionPixelSize(R.dimen.event_image_width);
        imageHeight = context.getResources().getDimensionPixelSize(R.dimen.event_image_height);
    }

    @Override
    protected void bind(final Event item) {
        if (item.getThumbnailSrc() != null) {
            Picasso.with(context)
                    .load(item.getThumbnailSrc())
                    .resize(imageWidth, imageHeight)
                    .centerCrop()
                    .into(imageView);
        } else {
            imageView.setImageDrawable(null);
        }

        titleView.setText(item.getTitle());

        if (item.getSubTitle() != null) {
            subTitleView.setText(item.getSubTitle());
            subTitleView.setVisibility(View.VISIBLE);
        } else {
            subTitleView.setVisibility(View.GONE);
        }

        scheduleView.setText(item.getSchedule());

        if (item.getLocation() != null) {
            locationView.setText(item.getLocation());
            locationView.setVisibility(View.VISIBLE);
        } else {
            locationView.setVisibility(View.GONE);
        }
    }
}
