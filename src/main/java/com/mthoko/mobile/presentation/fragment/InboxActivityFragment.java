package com.mthoko.mobile.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mthoko.mobile.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class InboxActivityFragment extends BaseFragment<InboxActivityFragment> {

    public InboxActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }
}
