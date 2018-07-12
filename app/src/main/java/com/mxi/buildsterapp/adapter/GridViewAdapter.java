package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;


import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.model.ImagePdf;
import com.mxi.buildsterapp.utils.SquareImageView;

import java.util.ArrayList;


public class GridViewAdapter extends BaseAdapter {
    private Context mContext;

    private final ArrayList<ImagePdf> Imageid;

    //private static LayoutInflater inflater=null;

    private SparseBooleanArray mSparseBooleanArray;

    public GridViewAdapter(Context c, ArrayList<ImagePdf> Imageid ) {
        mContext = c;
        this.Imageid = Imageid;

//        inflater = LayoutInflater.from(mContext);
        mSparseBooleanArray = new SparseBooleanArray();
    }

    public ArrayList<ImagePdf> getCheckedItems() {
        ArrayList<ImagePdf> mTempArry = new ArrayList<>();

        for (int i = 0; i < Imageid.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                mTempArry.add(Imageid.get(i));
            }
        }

        return mTempArry;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Imageid.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class ViewHolder
    {

        SquareImageView imageView;
        private CheckBox checkBox;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.grid_pdf_item, parent, false);//Inflate layout

        CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_image);

        final ImagePdf im = Imageid.get(position);

        imageView.setImageBitmap(im.getBim());

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

       /*        //If Context is MainActivity then hide checkbox
        if (!isCustomGalleryActivity)
            mCheckBox.setVisibility(View.GONE);

        ImageLoader.getInstance().displayImage("file://" + imageUrls.get(position), imageView, options);//Load Images over ImageView
*/
        mCheckBox.setTag(position);//Set Tag for CheckBox
        mCheckBox.setChecked(mSparseBooleanArray.get(position));
        mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);

       /* ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            //inflate the layout on basis of boolean

            convertView = inflater.inflate(R.layout.grid_item, parent, false);

            final ImagePdf im = Imageid.get(position);

            viewHolder.imageView = (SquareImageView) convertView.findViewById(R.id.grid_image);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            viewHolder.imageView.setImageBitmap(im.getBim());



            viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
*/



        return convertView;
    }

    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);//Insert selected checkbox value inside boolean array
//            ((SelectPdfActivity)mContext).showSelectButton();//call custom gallery activity method
        }
    };

}
