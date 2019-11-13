package com.abood.crowdfunding;


import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
public class PhotoFragment extends Fragment
{

    private static  final String ARG_PHOTO_URL="com.abood.crowdfunding.photoUrl";
    String photoUrl;
    ImageView imageView;


    public static Fragment newInstance(String photoUrl)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_URL, photoUrl);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        photoUrl =  getArguments().getString(ARG_PHOTO_URL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_photo, container, false);
        imageView=v.findViewById(R.id.campaign_photo);

        Glide.with(getActivity())
                .load(photoUrl)
                .asBitmap()
                .centerCrop()
                .error(R.color.grey_20)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        circularBitmapDrawable.setCornerRadius((float) 10); // radius for corners
                        view.setImageDrawable(circularBitmapDrawable);
                    }
                });


        return v;
    }

}
