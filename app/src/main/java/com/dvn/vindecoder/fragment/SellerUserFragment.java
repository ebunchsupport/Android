package com.dvn.vindecoder.fragment;

/**
 * Created by palash on 11-01-2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvn.vindecoder.R;


public class SellerUserFragment extends Fragment{
    private View mainView;
    public SellerUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = (View) inflater.inflate(R.layout.fragment_seller_user, container, false);

        return mainView;
    }

}