package com.mxi.buildsterapp.chiptext;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.model.MessageData;
import com.tokenautocomplete.TokenCompleteTextView;

public class ContactsCompletionView extends TokenCompleteTextView<MessageData> {

    public ContactsCompletionView(Context context) {
        super(context);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(MessageData contact) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View tokenView = l.inflate(R.layout.item_autocomplete_contact, (ViewGroup) getParent(), false);
        TokenTextView textView = (TokenTextView) tokenView.findViewById(R.id.token_text);

        textView.setText(contact.getName());

        return tokenView;
    }

    @Override
    protected MessageData defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');
        if (index == -1) {
            return new MessageData("email");
        } else {
            return new MessageData(completionText);
        }
    }
}
