package pt.uac.playnesti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pt.uac.playnesti.client.PlaynestiService;
import pt.uac.playnesti.model.Image;
import pt.uac.playnesti.slideshow.SlideShowPageIndicator;
import pt.uac.playnesti.slideshow.SlideShowPager;

public class GalleriesFragment extends Fragment {
    private SlideshowAdapter adapter;
    private ProgressBar progressBar;
    private View galleryLayout;
    private SlideShowPageIndicator indicator;
    private SlideShowPager pager;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();

            if (PlaynestiService.BROADCAST_ACTION_GET_GALLERY.equals(action)) {
                final List<Image> gallery = intent.getParcelableArrayListExtra(PlaynestiService.BROADCAST_EXTRA_GALLERY);
                displayGallery(gallery);
            } else if (PlaynestiService.BROADCAST_ACTION_ERROR.equals(action)) {
                final String errorMessage = intent.getStringExtra(PlaynestiService.BROADCAST_EXTRA_ERROR);
                displayError(errorMessage);
            }
        }
    };

    public GalleriesFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        // Setup intent filter
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PlaynestiService.BROADCAST_ACTION_GET_GALLERY);
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
        final View rootView = inflater.inflate(R.layout.fragment_galleries, container, false);

        adapter = new SlideshowAdapter(getActivity());
        galleryLayout = rootView.findViewById(R.id.gallery_layout);
        progressBar = (ProgressBar) rootView.findViewById(R.id.gallery_progress);
        pager = (SlideShowPager) rootView.findViewById(R.id.view_pager);
        indicator = (SlideShowPageIndicator) rootView.findViewById(R.id.indicator);

        pager.setAdapter(adapter);

        fetchGallery();
        return rootView;
    }

    private void fetchGallery() {
        progressBar.setVisibility(View.VISIBLE);
        galleryLayout.setVisibility(View.GONE);
        PlaynestiService.startActionGetGallery(getActivity());
    }

    private void displayGallery(final List<Image> gallery) {
        progressBar.setVisibility(View.GONE);
        galleryLayout.setVisibility(View.VISIBLE);
        adapter.loadGallery(gallery);
        indicator.setPager(pager, 0);
        indicator.startTimer();
    }

    private void displayError(final String errorMessage) {
        progressBar.setVisibility(View.GONE);
        galleryLayout.setVisibility(View.VISIBLE);
    }

    private static final class SlideshowAdapter extends PagerAdapter {
        private final List<Image> items;
        private final Context context;
        private final LayoutInflater inflater;

        public SlideshowAdapter(final Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.items = new ArrayList<>();
        }

        public void loadGallery(final List<Image> gallery) {
            items.addAll(gallery);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Image item = items.get(position);
            final View slide = inflater.inflate(R.layout.slide, null);
            final ImageView image = (ImageView) slide.findViewById(R.id.slide_show_image);

            Picasso.with(context)
                    .load(item.getImageSrc())
                    .fit()
                    .centerInside()
                    .into(image);

            container.addView(slide);
            return slide;
        }
    }
}
