package com.devivek.price_tracker_amazon;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.devivek.price_tracker_amazon.utilities.OnlineWork;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebViewFragment extends Fragment {

    private static final String TAG = "WebViewFragment";

    private static WebView webView;
    private OnlineWork onlineWork;
    private SharedPreferences sharedPreferences;

    String countryName;

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview_fragment, container, false);
        Log.e(TAG, "onCreateView: Started");

        onlineWork = new OnlineWork(getActivity());

        //SharedPref
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        countryName =sharedPreferences.getString(getString(R.string.DefaultCountry),"US");

        //Widgets declaration
        webView = view.findViewById(R.id.WebView);
        ImageView add = view.findViewById(R.id.add);
        ImageView back = view.findViewById(R.id.backarrow);
        ImageView refresh = view.findViewById(R.id.refresh);
        Spinner country = view.findViewById(R.id.country);

        //Country list
        HashMap<String, String> countries = new HashMap<>();
        countries.put("BR","https://www.amazon.com.br");
        countries.put("TR","https://www.amazon.com.tr");
        countries.put("SP","https://www.amazon.es");
        countries.put("GE", "https://www.amazon.de");
        countries.put("IN","https://www.amazon.in");
        countries.put("AU","https://www.amazon.com.au");
        countries.put("UK", "https://www.amazon.co.uk");
        countries.put("CA","https://www.amazon.ca");
        countries.put("US", "https://www.amazon.com");

        List<String> indexes = new ArrayList<String>(countries.keySet());


        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, countries.keySet().toArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        country.setAdapter(adapter);
        country.setSelection(indexes.indexOf(countryName));


        //WebView settings
        webView.getSettings().setUserAgentString("Android");
        webView.setWebViewClient(new WebViewClient());

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                webView.loadUrl(countries.get(country.getSelectedItem().toString()));
                countryName = country.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //Constants
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        refresh.setOnClickListener(view13 -> webView.loadUrl(webView.getUrl()));

        //Back button listener
        back.setOnClickListener(view12 -> {
            if(webView.canGoBack()){
                webView.goBack();
            }
        });

        //Add Button listener

        add.setOnClickListener(view1 -> {
            String currentUrl = webView.getUrl();

            String url = "https://amazonscraper.herokuapp.com/"+currentUrl;

            onlineWork.getPrice(currentUrl);
        });

        return view;
    }

    public static void SetWebView(String url){
        webView.loadUrl(url);
    }
}
