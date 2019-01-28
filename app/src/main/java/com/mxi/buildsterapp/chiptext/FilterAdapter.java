package com.mxi.buildsterapp.chiptext;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.model.MessageData;
import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

public class FilterAdapter extends FilteredArrayAdapter<MessageData> {

    public FilterAdapter(Context context, int resource, List<MessageData> objects) {
        super(context, resource,  objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.invite_autotext_item, parent, false);
        }

        MessageData contact = getItem(position);
        ((TextView) convertView.findViewById(R.id.tv_country_name)).setText(contact.getName());
//        assert contact != null;
//        ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(contact.getDrawableId());

        return convertView;
    }

    @Override
    protected boolean keepObject(MessageData person, String mask) {
        mask = mask.toLowerCase();
        return person.getName().toLowerCase().startsWith(mask) || person.getName().toLowerCase().startsWith(mask);
    }
}