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
import com.anupom.covidinfo.adapter.BreakingNewsAdapter;

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
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
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

    //-----------------------------------------------------
    // arraylist to hold the news data
    ArrayList<String> newsTitle = new ArrayList<>();
    ArrayList<String> newsDesc = new ArrayList<>();
    ArrayList<String> newsDate = new ArrayList<>();
    //-----------------------------------------------------

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(Context context, String param1, String param2) {
        mContext = context;

        NewsFragment fragment = new NewsFragment();
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

        // get the news data
        getNewsByApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = (RecyclerView) inflater.inflate(R.layout.fragment_news, container, false);
        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //view.setAdapter(new BreakingNewsAdapter(mContext, newsTitle, newsDesc, newsImageLink));

        return view;
    }

    //---------------------------------------
    // method to get the breaking news data
    private void getNewsData(String title, String description, String date) {

        Log.e("TAG", "getNewsData: " + title + " " + description);
        newsTitle.add(title);
        newsDesc.add(description);
        newsDate.add(date);

       /* newsTitle.add("News2");
        newsDesc.add("Desc2");
        newsImageLink.add("link2");

        newsTitle.add("News3");
        newsDesc.add("Desc3");
        newsImageLink.add("link3");*/
    }
    //---------------------------------------

    private void getNewsByApi() {
        Call<ResponseBody> getNewsData = RetrofitClient
                .getInstance()
                .getApi()
                .getNewsList("WxKoI4fsqouZAwJhuhpKoo6ooKx33kkeFH47KZln2BZSIm5dgW6b8be9zvOu");

        getNewsData.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseBody);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String title = jsonArray.getJSONObject(i).get("title").toString();
                        String description = jsonArray.getJSONObject(i).get("description").toString();
                        String source = jsonArray.getJSONObject(i).get("source").toString();
                        String created_at = jsonArray.getJSONObject(i).get("created_at").toString();

                        Log.e("see", "onResponse: " + title + " " + description + " " + created_at);

                        getNewsData(title, description, created_at);
                        view.setAdapter(new BreakingNewsAdapter(mContext, newsTitle, newsDesc, newsDate));
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
