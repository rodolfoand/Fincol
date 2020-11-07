package com.fatec.fincol.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fatec.fincol.R;
import com.fatec.fincol.model.AccountVersion2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeFragViewModel mHomeViewModel;
    private TextView symbolCurrencyTextView;
    private TextView balanceValueTextView;
    private FloatingActionButton transactFloatingActionButton;
    private WebView homeChartWebView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel =
                ViewModelProviders.of(this).get(HomeFragViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        balanceValueTextView = root.findViewById(R.id.balanceValueTextView);
        symbolCurrencyTextView = root.findViewById(R.id.symbolCurrencyTextView);
        transactFloatingActionButton = root.findViewById(R.id.transactFloatingActionButton);
        homeChartWebView = root.findViewById(R.id.homeChartWebView);

        //homeChartWebView.getSettings().setJavaScriptEnabled(true);
        //homeChartWebView.setWebViewClient(new WebViewClient());

        //String url =  "http://chart.apis.google.com/chart?cht=p3&chs=500x200&chd=e:TNTNTNGa&chts=000000,16&chtt=A+Better+Web&chl=Hello|Hi|anas|Explorer&chco=FF5533,237745,9011D3,335423&chdl=Apple|Mozilla|Google|Microsoft";

//        String url = "https://chart.googleapis.com/chart?" +
//                "cht=lc&" + //define o tipo do gráfico "linha"
//                "chxt=x,y&" + //imprime os valores dos eixos X, Y
//                "chs=300x300&" + //define o tamanho da imagem
//                "chd=t:10,45,5,10,13,26&" + //valor de cada coluna do gráfico
//                "chl=Jan|Fev|Mar|Abr|Mai|Jun&" + //rótulo para cada coluna
//                "chdl=Vendas&" + //legenda do gráfico
//                "chxr=1,0,50&" + //define o valor de início e fim do eixo
//                "chds=0,50&" + //define o valor de escala dos dados
//                "chg=0,5,0,0&" + //desenha linha horizontal na grade
//                "chco=3D7930&" + //cor da linha do gráfico
//                "chtt=Vendas+x+1000&" + //cabeçalho do gráfico
//                "chm=B,C5D4B5BB,0,0,0"; //fundo verde

        String url = "<html>"+
                "<head>"+
    "<!--Load the AJAX API-->"+
    "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"+
    "<script type=\"text/javascript\">"+

                // Load the Visualization API and the corechart package.
                "google.charts.load('current', {'packages':['corechart']});"+

        // Set a callback to run when the Google Visualization API is loaded.
        "google.charts.setOnLoadCallback(drawChart);"+

        // Callback that creates and populates a data table,
        // instantiates the pie chart, passes in the data and
        // draws it.
        "function drawChart() {"+

            // Create the data table.
            "var data = new google.visualization.DataTable();"+
            "data.addColumn('string', 'Topping');"+
            "data.addColumn('number', 'Slices');"+
            "data.addRows(["+
          "['Mushrooms', 3],"+
          "['Onions', 1],"+
          "['Olives', 1],"+
          "['Zucchini', 1],"+
          "['Pepperoni', 2]"+
        "]);"+

            // Set chart options
            "var options = {'title':'How Much Pizza I Ate Last Night',"+
                    "'width':400,"+
                    "'height':300};"+

            // Instantiate and draw our chart, passing in some options.
            "var chart = new google.visualization.PieChart(document.getElementById('chart_div'));"+
            "chart.draw(data, options);"+
        "}"+
    "</script>"+
  "</head>"+

  "<body>"+
    "<!--Div that will hold the pie chart-->"+
    "<div id=\"chart_div\"></div>"+
  "</body>"+
"</html>";

        //homeChartWebView.loadUrl(url);
        //url = "<html><body>You scored <b>192</b> points.</body></html>";
        //homeChartWebView.loadUrl("file:///android_res/layout/homechart.html");

        homeChartWebView.setVisibility(View.INVISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String user = preferences.getString(getString(R.string.saved_uid_user_key), "");

        mHomeViewModel.initialize(user);


        transactFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_home_to_transactionFragment);
            }
        });

        mHomeViewModel.mActiveAccount.observe(getActivity(), new Observer<AccountVersion2>() {
            @Override
            public void onChanged(AccountVersion2 accountVersion2) {


                String formatted = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(accountVersion2.getBalance());

                String replaceable = String.format("[%s\\s]", NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol());
                String cleanString = formatted.replaceAll(replaceable, "");
                symbolCurrencyTextView.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol() + " ");
                balanceValueTextView.setText(cleanString);
            }
        });


        return root;
    }
}