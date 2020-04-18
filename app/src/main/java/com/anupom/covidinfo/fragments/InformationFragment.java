package com.anupom.covidinfo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anupom.covidinfo.R;
import com.anupom.covidinfo.adapter.InformationAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit_clients.RetrofitClient;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView view;

    private static Context mContext;

    private OnFragmentInteractionListener mListener;

    //----------------------------------------------------------------------
    // variable to store the information
    private ArrayList<Integer> contentTypeList = new ArrayList<Integer>();
    private ArrayList<String> contentList = new ArrayList<String>();
    //----------------------------------------------------------------------

    public InformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationFragment newInstance(Context context, String param1, String param2) {
        mContext = context;
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getInformationApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = (RecyclerView) inflater.inflate(R.layout.fragment_information, container, false);
        view.setLayoutManager(new LinearLayoutManager(view.getContext()));


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //----------------------------------------------------
    // method to get the information data
    private void getInformationData(String flag, String data, String helpline_file) {
        Log.e("TAG", "getInformationData: "+helpline_file);
       /* contentTypeList.add(0);
        contentTypeList.add(0);
        contentTypeList.add(1);
        contentTypeList.add(0);
        contentTypeList.add(1);

        contentList.add("Hello world");
        contentList.add("Hi there");
        contentList.add("aaaa");
        contentList.add("This is testing information content");
        contentList.add("aaa");*/
        if (flag.equals("Text")) {
            contentTypeList.add(0);
            if (data != null) {
                contentList.add(data);
            }

        } else if (flag.equals("Image")) {
            contentTypeList.add(1);
            if (helpline_file != null) {
                contentList.add(helpline_file);
            }
        }
    }
    //----------------------------------------------------

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


    /*............................................................................................................*/
    public void getInformationApi() {
        Call<ResponseBody> getInformation = RetrofitClient
                .getInstance()
                .getApi()
                .getInformationData("WxKoI4fsqouZAwJhuhpKoo6ooKx33kkeFH47KZln2BZSIm5dgW6b8be9zvOu");

        getInformation.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String type = jsonArray.getJSONObject(i).get("type").toString();
                        String title = jsonArray.getJSONObject(i).get("title").toString();
                        String help_line_file = jsonArray.getJSONObject(i).get("helpline_file").toString();
                        Log.e("TAG", "onResponse: "+help_line_file);

                        getInformationData(type, title, help_line_file);
                        view.setAdapter(new InformationAdapter(mContext, contentTypeList, contentList));
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    /*.......................................................................................................................*/
}
