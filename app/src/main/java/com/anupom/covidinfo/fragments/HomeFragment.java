package com.anupom.covidinfo.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anupom.covidinfo.InfoWindowData;
import com.anupom.covidinfo.R;
import com.anupom.covidinfo.adapter.NewsHeadlineAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit_clients.RetrofitClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int total_case_count = 0;
    private int total_death_count = 0;
    private int total_recover_count = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<Float> graphX = new ArrayList<>();
    ArrayList<Float> graphY = new ArrayList<>();

    private RecyclerView headlineRecyclerView;

    private static Context mContext;

    private OnFragmentInteractionListener mListener;

    private GoogleMap gmp;

    //----------------------------------------------------------------------------------
    // Textview for the corona statistics section
    TextView newAddedCase, totalCases, newDeathCase, totalDeathCase, newRecoveredCase,
            totalRecoveredCase;
    //----------------------------------------------------------------------------------

    // creating object for line chart
    LineChart lineChart;

    //---------------------------------------------------------------
    // array list to store the headline news
    private ArrayList<String> newsHeadlineArray = new ArrayList<>();
    //---------------------------------------------------------------

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(Context context, String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        mContext = context;

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

        //----------------------------------------
        // get the data for the news headline
        getLatestNewsDataApiCall();
        //----------------------------------------

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //-------------------------------------------------------------------------
        // get the corona statistics textview
        newAddedCase = view.findViewById(R.id.new_added_case);
        totalCases = view.findViewById(R.id.total_confirmed_cases);
        newDeathCase = view.findViewById(R.id.new_death_case);
        totalDeathCase = view.findViewById(R.id.total_death_cases);
        newRecoveredCase = view.findViewById(R.id.new_recovered_case);
        totalRecoveredCase = view.findViewById(R.id.total_recovered_cases);
        //-------------------------------------------------------------------------


        // now implement the google map structure
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // initialize the headline recyclerview
        headlineRecyclerView = view.findViewById(R.id.headline_recyclerView);
        headlineRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));


        // initialize the line chart
        lineChart = view.findViewById(R.id.lineChart);
        getGraphApiCall();


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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gmp = googleMap;
        makeApiCall();
    }

    //----------------------------------------------------
    // method to setup the line chart
    private void setUpChart() {
        ArrayList<Entry> lineEntries = getLineGraphData();

        LineDataSet set1;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(lineEntries);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(lineEntries, "Total cases");
            set1.setDrawIcons(true);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.DKGRAY);
            set1.setCircleColor(Color.DKGRAY);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            if (Utils.getSDKInt() >= 18) {
                //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                //set1.setFillDrawable(drawable);
                set1.setFillColor(Color.BLUE);
            } else {
                set1.setFillColor(Color.DKGRAY);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            lineChart.setData(data);
        }
    }

    // get the graph data

    private ArrayList<Entry> getLineGraphData() {

        ArrayList<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < graphY.size(); i++) {
            lineEntries.add(new Entry(graphX.get(i), graphY.get(i)));
        }

        /*lineEntries.add(new Entry(2, 30));
        lineEntries.add(new Entry(3, 60));
        lineEntries.add(new Entry(4, 50));
        lineEntries.add(new Entry(5, 70));
        lineEntries.add(new Entry(6, 100));
        lineEntries.add(new Entry(7, 174));
        lineEntries.add(new Entry(8, 543));
        lineEntries.add(new Entry(9, 289));
        lineEntries.add(new Entry(10, 774));
        lineEntries.add(new Entry(11, 916));
        lineEntries.add(new Entry(12, 725));*/

        return lineEntries;
    }

    //----------------------------------------------------

    //-----------------------------------------------------
    // method to set the data for the news headlines
    private void getHeadlineNews(String newsData) {
        newsHeadlineArray.add(newsData);
    }
    //-----------------------------------------------------

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

    private void makeApiCall() {
        Call<ResponseBody> getMapList = RetrofitClient
                .getInstance()
                .getApi()
                .getMapList("WxKoI4fsqouZAwJhuhpKoo6ooKx33kkeFH47KZln2BZSIm5dgW6b8be9zvOu");

        getMapList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String getResponseData = response.body().string();

                    JSONArray jsonArray = new JSONArray(getResponseData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String id = jsonArray.getJSONObject(i).get("id").toString();
                        String state = jsonArray.getJSONObject(i).get("state").toString();
                        String district = jsonArray.getJSONObject(i).get("district").toString();
                        String latitude = jsonArray.getJSONObject(i).get("latitude").toString();
                        String longitude = jsonArray.getJSONObject(i).get("longitude").toString();
                        String total_affected = jsonArray.getJSONObject(i).get("total_affected").toString();
                        String total_death = jsonArray.getJSONObject(i).get("total_death").toString();
                        String total_recovered = jsonArray.getJSONObject(i).get("total_recovered").toString();
                        String user_id = jsonArray.getJSONObject(i).get("user_id").toString();
                        String created_at = jsonArray.getJSONObject(i).get("created_at").toString();
                        String updated_at = jsonArray.getJSONObject(i).get("updated_at").toString();

                        /*Log.e("tags", "onResponse: " + id + " " + state + " " + total_death + " " + updated_at);*/
                        total_case_count = total_case_count + Integer.parseInt(total_affected);
                        total_death_count = total_death_count + Integer.parseInt(total_death);
                        total_recover_count = total_recover_count + Integer.parseInt(total_recovered);

                        addTheMarker(latitude, longitude, district, total_affected, total_death, total_recovered);
                    }

                    totalCases.setText(String.valueOf(total_case_count));
                    totalDeathCase.setText(String.valueOf(total_death_count));
                    totalRecoveredCase.setText(String.valueOf(total_recover_count));


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getLatestNewsDataApiCall() {
        Call<ResponseBody> getLatestNews = RetrofitClient
                .getInstance()
                .getApi()
                .getLatestNews("WxKoI4fsqouZAwJhuhpKoo6ooKx33kkeFH47KZln2BZSIm5dgW6b8be9zvOu");

        getLatestNews.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseBody);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String title = jsonArray.getJSONObject(i).get("title").toString();

                        getHeadlineNews(title);
                        headlineRecyclerView.setAdapter(new NewsHeadlineAdapter(mContext, newsHeadlineArray));
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

    private void addTheMarker(String Lat, String lon, String district, String total_affected, String total_death, String total_recovered) {
        // String[] latlong = "26.1805978,91.753943".split(",");
        //String latlong[] = passLocation;
        //  Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());


        double latitude = Double.parseDouble(Lat);
        double longitude = Double.parseDouble(lon);
        LatLng location = new LatLng(latitude, longitude);
        /*List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

        String address = addresses.get(0).getLocality();*/
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.title("Details");

        InfoWindowData infoWindowData = new InfoWindowData();
        infoWindowData.setDistrict(district);
        infoWindowData.setTotal_affected("affected : " + total_affected);
        infoWindowData.setTotal_death("death : " + total_death);
        infoWindowData.setTotal_recovered("recovered : " + total_recovered);

        CustomInfoWindowGoogleMap customInfoWindowGoogleMap = new CustomInfoWindowGoogleMap(getContext());
        gmp.setInfoWindowAdapter(customInfoWindowGoogleMap);
        Marker marker = gmp.addMarker(markerOptions);
        marker.setTag(infoWindowData);
        gmp.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });


        /*
        InfoWindowData info = new InfoWindowData();
        info.setImage("snowqualmie");
        info.setHotel("Hotel : excellent hotels available");
        info.setFood("Food : all types of restaurants available");
        info.setTransport("Reach the site by bus, car and train.");

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        Marker m = mMap.addMarker(markerOptions);
        m.setTag(info);
        m.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(snowqualmie));
        */
        gmp.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 7));
        gmp.addMarker(markerOptions);

    }

    private void getGraphApiCall() {
        Call<ResponseBody> makeGraphCall = RetrofitClient
                .getInstance()
                .getApi()
                .getGraphCall("WxKoI4fsqouZAwJhuhpKoo6ooKx33kkeFH47KZln2BZSIm5dgW6b8be9zvOu");

        makeGraphCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseBody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String id = jsonArray.getJSONObject(i).get("id").toString();
                        String total_affected = jsonArray.getJSONObject(i).get("total_affected").toString();
                        String total_death = jsonArray.getJSONObject(i).get("total_death").toString();
                        String total_recovered = jsonArray.getJSONObject(i).get("total_recovered").toString();
                        String created_at = jsonArray.getJSONObject(i).get("created_at").toString();
                        String updated_at = jsonArray.getJSONObject(i).get("updated_at").toString();
                        Log.e("graphData", "onResponse: " + total_affected);

                        String[] arrOfStr = created_at.split(" ", 2);
                        Log.e("date", "onResponse: " + arrOfStr[0]);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                        Date date = formatter.parse(arrOfStr[0]);
                        assert date != null;
                        String month = String.valueOf(date.getMonth());
                        String dateOfMonth = String.valueOf(date.getDay());
                        Log.e("month", "onResponse: " + dateOfMonth);

                        /*getLineGraphData(Integer.parseInt(created_at), Integer.parseInt(total_affected));*/
                        //graphX.add(Float.parseFloat(dateOfMonth));
                        graphX.add(Float.parseFloat(String.valueOf(22.0)));
                        graphX.add(Float.parseFloat(String.valueOf(23.0)));
                        graphX.add(Float.parseFloat(String.valueOf(24.0)));
                        graphY.add(Float.parseFloat(total_affected));
                        Log.e("testGraph", "onResponse: " + graphY.get(i) + " " + graphX);
                    }
                    setUpChart();

                } catch (IOException | JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

        private Context context;

        public CustomInfoWindowGoogleMap(Context ctx) {
            context = ctx;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = ((Activity) context).getLayoutInflater()
                    .inflate(R.layout.window_map, null);
            TextView district = view.findViewById(R.id.district);
            TextView total_affected = view.findViewById(R.id.total_affected);
            TextView total_death = view.findViewById(R.id.total_death);
            TextView total_recovered = view.findViewById(R.id.total_recovered);
            InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
            assert infoWindowData != null;

            district.setText(infoWindowData.getDistrict());
            total_affected.setText(infoWindowData.getTotal_affected());
            total_death.setText(infoWindowData.getTotal_death());
            total_recovered.setText(infoWindowData.getTotal_recovered());


/*
        infoWindowData.setDistrict();
        infoWindowData.setTotal_affected();
        infoWindowData.setTotal_death();
        infoWindowData.setTotal_recovered();
*/

            return view;
        }
    }
}
