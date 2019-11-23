package com.abood.crowdfunding;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondCampaignInfoFragment extends Fragment {


    private NestedScrollView nested_scroll_view;
    private ImageButton typeToggle
            ,targetCostToggle
            ,locationToggle
            ,countryToggle;

    private Button typeSaveBTN, typeHideBTN,
            targetCostSaveBTN, targetCostHideBTN,
            locationSaveBTN, locationHideBTN,
            countrySaveBTN, countryHideBTN;

    private View typeLyEexpand,
            targetCostLyEexpand,
            locationLyEexpand,
            countryLyEexpand,
            rootView;


    public SecondCampaignInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_secondcampaigninfo, container, false);
//        parent_view = rootView.findViewById(android.R.id.content);


        initComponent();

        return rootView;

    }

    private void initComponent() {

        // target cost section
        targetCostToggle = rootView.findViewById(R.id.target_cost_bt_toggle);
        targetCostHideBTN = rootView.findViewById(R.id.target_cost_bt_hide);
        targetCostSaveBTN = rootView.findViewById(R.id.target_cost_bt_save);
        targetCostLyEexpand = rootView.findViewById(R.id.target_cost_lyt_expand);
        targetCostLyEexpand.setVisibility(View.GONE);

        targetCostToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionTarget(targetCostToggle);
            }
        });

        targetCostHideBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionTarget(targetCostToggle);
            }
        });

        targetCostSaveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootView, "Data saved", Snackbar.LENGTH_SHORT).show();
                toggleSectionTarget(targetCostToggle);
            }
        });


        // location cost section
        locationToggle = rootView.findViewById(R.id. location_bt_toggle);
        locationHideBTN = rootView.findViewById(R.id. location_bt_hide);
        locationSaveBTN = rootView.findViewById(R.id. location_bt_save);
        locationLyEexpand = rootView.findViewById(R.id. location_lyt_expand);
        locationLyEexpand.setVisibility(View.GONE);

        locationToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionLocation( locationToggle);
            }
        });

        locationHideBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionLocation( locationToggle);
            }
        });

        locationSaveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootView, "Data saved", Snackbar.LENGTH_SHORT).show();
                toggleSectionLocation( locationToggle);
            }
        });


        // country cost section
        countryToggle = rootView.findViewById(R.id. country_bt_toggle);
        countryHideBTN = rootView.findViewById(R.id. country_bt_hide);
        countrySaveBTN = rootView.findViewById(R.id. country_bt_save);
        countryLyEexpand = rootView.findViewById(R.id.country_lyt_expand);
        countryLyEexpand.setVisibility(View.GONE);

        countryToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionCountry( countryToggle);
            }
        });

        countryHideBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionCountry(countryToggle);
            }
        });

        countrySaveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootView, "Data saved", Snackbar.LENGTH_SHORT).show();
                toggleSectionCountry(countryToggle);
            }
        });


        // type section
        typeToggle =  rootView.findViewById(R.id.type_bt_toggle);
        typeHideBTN = rootView.findViewById(R.id.type_bt_hide);
        typeSaveBTN = rootView.findViewById(R.id.type_bt_save);
        typeLyEexpand = rootView.findViewById(R.id.lyt_expand_input);
        typeLyEexpand.setVisibility(View.GONE);

        typeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionType(typeToggle);
            }
        });

        typeHideBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionType(typeToggle);
            }
        });

        typeSaveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootView, "Data saved", Snackbar.LENGTH_SHORT).show();
                toggleSectionType(typeToggle);
            }
        });

        // nested scrollview
        nested_scroll_view =  rootView.findViewById(R.id.nested_scroll_view);
    }



    private void toggleSectionTarget(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(targetCostLyEexpand, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    nestedScrollTo(nested_scroll_view, targetCostLyEexpand);
                }
            });
        } else {
            ViewAnimation.collapse(targetCostLyEexpand);
        }
    }

    private void toggleSectionLocation(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(locationLyEexpand, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    nestedScrollTo(nested_scroll_view, locationLyEexpand);
                }
            });
        } else {
            ViewAnimation.collapse(locationLyEexpand);
        }
    }


    private void toggleSectionCountry(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(countryLyEexpand, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    nestedScrollTo(nested_scroll_view, countryLyEexpand);
                }
            });
        } else {
            ViewAnimation.collapse(countryLyEexpand);
        }
    }

    private void toggleSectionType(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(typeLyEexpand, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    nestedScrollTo(nested_scroll_view, typeLyEexpand);
                }
            });
        } else {
            ViewAnimation.collapse(typeLyEexpand);
        }
    }


    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }


}
