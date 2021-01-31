package com.rhdigital.rhclient.activities.rhapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rhdigital.rhclient.R;
import com.rhdigital.rhclient.common.dto.UserFieldDto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class EditProfileArrayAdapter extends ArrayAdapter {

    private LinkedHashMap<String, View> viewList = new LinkedHashMap<>();
    private List<String> items;
    private int currentIndex;
    private UserFieldDto userField;

    public EditProfileArrayAdapter(Context context, ArrayList<String> items, int currentIndex, UserFieldDto userField) {
        super(context, 0, items);
        this.items = items;
        this.currentIndex = currentIndex;
        this.userField = userField;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_edit_list_item, parent, false);

        LinearLayout linearLayout = listItem.findViewById(R.id.container);
        RadioButton radioButton = listItem.findViewById(R.id.radioButton);
        TextView textView = listItem.findViewById(R.id.tvTitle);
        textView.setText(items.get(position));

        if (position == currentIndex){
            radioButton.toggle();
        }

        linearLayout.setOnClickListener(view -> {
            Boolean selected = !radioButton.isSelected();
            radioButton.setChecked(selected);
            if (selected) {
                if (currentIndex > -1) {
                    View viewItem = viewList.get(items.get(currentIndex));
                    if (viewItem != null) {
                        RadioButton radio = viewItem.findViewById(R.id.radioButton);
                        radio.setChecked(false);
                    }
                }
                userField.setValue(items.get(position));
                currentIndex = position;
            } else {
                userField.setValue("");
            }
        });

        viewList.put(items.get(position), listItem);

        return listItem;
    }
}
