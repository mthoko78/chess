package com.mthoko.mobile.presentation.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.mthoko.mobile.model.RecyclerItem;
import com.mthoko.mobile.presentation.activity.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment<T> extends Fragment {

    RecyclerView recyclerView;

    MyAdapter adapter;

    final List<RecyclerItem> listItems = new ArrayList<>();

    protected OnFragmentInteractionListener mListener;

    public BaseFragment() {
        // Required empty public constructor
    }

    public void addItem(String title, String body, Long id){
        addItem(title, body, id, true);
    }

    public void addItem(String title, String body, Long id, boolean refresh){
        listItems.add(new RecyclerItem(title, body, id));
        if (refresh) {
            refresh();
        }
    }

    public RecyclerItem removeItemById(Long id) {
        RecyclerItem itemToRemove = getItemById(id);
        if (itemToRemove != null) {
            listItems.remove(itemToRemove);
            adapter.notifyDataSetChanged();
        }
        return itemToRemove;
    }

    public RecyclerItem getItemById(Long id) {
        for (RecyclerItem item : listItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public void refresh(){
        recyclerView.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
