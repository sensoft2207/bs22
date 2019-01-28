package com.mxi.buildsterapp.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.utils.TouchImageView;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;


/**
 * Created by vishal on 7/3/18.
 */

public class PageFragment extends Fragment {

    private String imageResource,imageId;
    private Bitmap bitmap;

    public static PageFragment getInstance(String resourceID/*, String task_id*/) {
        PageFragment f = new PageFragment();
        Bundle args = new Bundle();
        args.putString("image_source",resourceID);
//        args.putString("image_id",task_id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageResource = getArguments().getString("image_source");
//        imageId = getArguments().getString("image_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageViewTouch imageView = (ImageViewTouch) view.findViewById(R.id.image);
        final ProgressBar pb = (ProgressBar)view.findViewById(R.id.pb);


        imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_WIDTH);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 4;
        o.inDither = false;
       // bitmap = BitmapFactory.decodeResource(getResources(), imageResource, o);

        Log.e("IMAGEPAGER",imageResource);
//        Log.e("IMAGEPAGER",imageId);

       /* Glide.with(this)
                .load(imageResource)
                .into(imageView);
*/
        Glide.with(this).load(imageResource).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        pb.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        pb.setVisibility(View.GONE);

                        return false;
                    }
                }).into(imageView);

    }
}
