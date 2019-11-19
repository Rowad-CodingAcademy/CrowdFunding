package com.abood.crowdfunding;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstCampaignInfoFragment extends Fragment {

    private Button startDate, endDate;
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "DialogDate";
    private View v;
    private Date currentTime = Calendar.getInstance().getTime();



    public FirstCampaignInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_firstcampaigninfo, container, false);
        initComponent();

        return v;
    }


    private void initComponent() {
        startDate=v.findViewById(R.id.start_date);

       startDate. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(currentTime);
                dialog.setTargetFragment(FirstCampaignInfoFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
                updateStartDate();
            }
        });


        endDate=v.findViewById(R.id.end_date);

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(currentTime);
                dialog.setTargetFragment(FirstCampaignInfoFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
                updateEndDate();
            }
        });


    }


    private void updateStartDate() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String stringDate = simpleDateFormat.format(currentTime);
        startDate.setText(stringDate);
    }


    private void updateEndDate() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String stringDate = simpleDateFormat.format(currentTime);
        endDate.setText(stringDate);
    }


}
