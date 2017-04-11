package tech.dylanarndt.howmuchfree;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;
import com.adcolony.sdk.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnFocusChangeListener, MoPubInterstitial.InterstitialAdListener {
    // Remove the below line after defining your own ad unit ID.
    //private static final String TOAST_TEXT = "Test ads are being shown. "
    //  + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";
    private Button Reset, Go; //Declares all the View from the UI to be used
    private EditText Wage,Hours,Tax;
    private TextView Total,PerMin,PerSec;
    private MoPubView moPubView;
    private MoPubInterstitial moPubInterstitial;
    private String AdUnitID1, Error;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Reset = (Button)findViewById(R.id.ResetButton);//Binds all the views from the UI to methods for use in code
        Go = (Button)findViewById(R.id.GoButton);
        Tax = (EditText)findViewById(R.id.TaxWithholding);
        Wage = (EditText)findViewById(R.id.HourlyPayRate);
        Total = (TextView)findViewById(R.id.Total);
        PerMin = (TextView)findViewById(R.id.PerMin);
        PerSec = (TextView)findViewById(R.id.PerSec);
        Hours = (EditText)findViewById(R.id.Hours);
        moPubView = (MoPubView) findViewById(R.id.adview);

        Error = "Ad not ready";
        AdUnitID1 = "0c8ca741ebbe4fb29a2088dc37fd94e1";

        moPubInterstitial = new MoPubInterstitial(this,AdUnitID1);
        moPubInterstitial.setInterstitialAdListener(this);
        moPubInterstitial.load();

        Reset.setOnClickListener(this);
        Go.setOnClickListener(this);
        Wage.setOnFocusChangeListener(this);
        Tax.setOnFocusChangeListener(this);
        Hours.setOnFocusChangeListener(this);
        Tax.setOnClickListener(this);
        Hours.setOnClickListener(this);


        // Load an ad into the AdMob banner view.
        moPubView.setAdUnitId("b8031064115f416fb98970261e510cbd");
        moPubView.loadAd();




    }
    public void ShowInterstitialMethod(){
        if (moPubInterstitial.isReady()){
            moPubInterstitial.show();
        }else {
            // Todo Make code for ad not ready
        }
    }



    public void onClick(View view){

        switch (view.getId()){
            case R.id.ResetButton:
                Wage.setText(R.string.DefaultPayValue);
                Tax.setText(R.string.DefaultTaxPercent);
                Hours.setText(R.string.DefaultHoursValue);
                break;
            case R.id.GoButton:
                //Checks to see if there is an entry in hourly wage editText in UI
                if (Float.valueOf(Wage.getText().toString()) > 0){
                    //If there is an entry in the hourly wage then app checks to see if there's an entry in tax withholding editText. If the result is null app skips to next else.
                    if (Float.valueOf(Tax.getText().toString()) > 0){
                        //If there is a Tax entry then it checks if there is a entry in Hours
                        if (Float.valueOf(Hours.getText().toString()) > 0){
                            //If there is a Wage, Tax, and hours entry do this math
                            //Gets value of Hourly Wage, Hours and Tax Withholding entries and makes them into a float variables
                            float W = Float.valueOf(Wage.getText().toString());
                            float TX = Float.valueOf(Tax.getText().toString());
                            float H = Float.valueOf(Hours.getText().toString());
                            //Take TX and divide by 100. Then Multiply W and TX for the Withheld amount. Then subtract Withheld from W.
                            float Withheld = (TX/100)*W;
                            W = W-Withheld;
                            //Take W and multiply by H to get Total and name it "T"
                            float T = W*H;
                            //Then Divide W by 60 to get the money earned per min. Then make a variable named PM for this number. Repeat to get the value per sec.
                            float PM = W/60;
                            float PS = PM/60;
                            // Make two numbers formatter to make sure the numbers are in the right format to output to the UI
                            NumberFormat formatter2spot = new DecimalFormat("$##0.00");
                            NumberFormat formatter3spot = new DecimalFormat("$##0.000");
                            //Reformat PM, PS, and Total  and change their corresponding editText views to show the output
                            PerMin.setText(formatter3spot.format(PM));
                            PerSec.setText(formatter3spot.format(PS));
                            Total.setText(formatter2spot.format(T));

                            return;

                        }else{
                            //If there is a entry into Wage and Tax, and NO Hours do this math
                            //Take the string from Wage and Tax and turn them into floats named "W" and "TX"
                            float W = Float.valueOf(Wage.getText().toString());
                            float TX = Float.valueOf(Tax.getText().toString());

                            //Take TX and divide by 100. Then Multiply W and TX for the Withheld amount. Then subtract Withheld from W.
                            float Withheld = (TX/100)*W;
                            W = W-Withheld;

                            //Then Divide W by 60 to get the money earned per min. Then make a variable named PM for this number. Repeat to get the value per sec.
                            float PM = W/60;
                            float PS = PM/60;
                            // Make a numbers formatter to make sure the numbers are in the right format to output to the UI
                            NumberFormat formatter3spot = new DecimalFormat("$##0.000");
                            //Reformat PM and PS and change their corresponding editText views to show the output
                            PerMin.setText(formatter3spot.format(PM));
                            PerSec.setText(formatter3spot.format(PS));
                            return;
                        }
                    }else if (Float.valueOf(Hours.getText().toString()) > 0){
                        //If there is and entry into Wage but not tax start here.
                        //If there is and entry into Wage and Hours, but NOT Tax, do this
                        //Take the string from Wage and Hours and turn them into floats named "W" and "H"
                        float W = Float.valueOf(Wage.getText().toString());
                        float H = Float.valueOf(Hours.getText().toString());
                        //Take W and multiply by H to get T
                        float T = W*H;
                        //Then Divide W by 60 to get the money earned per min. Then make a variable named PM for this number. Repeat to get the value per sec.
                        float PM = W/60;
                        float PS = PM/60;
                        // Make two numbers formatter to make sure the numbers are in the right format to output to the UI
                        NumberFormat formatter2spot = new DecimalFormat("$##0.00");
                        NumberFormat formatter3spot = new DecimalFormat("$##0.000");
                        //Reformat PM, PS, and Total  and change their corresponding editText views to show the output
                        PerMin.setText(formatter3spot.format(PM));
                        PerSec.setText(formatter3spot.format(PS));
                        Total.setText(formatter2spot.format(T));
                        return;
                    }else{
                        //If there is an entry into only Wage then
                        float W = Float.valueOf(Wage.getText().toString());
                        //Then Divide W by 60 to get the money earned per min. Then make a variable named PM for this number. Repeat to get the value per sec.
                        float PM = W/60;
                        float PS = PM/60;
                        // Make a numbers formatter to make sure the numbers are in the right format to output to the UI
                        NumberFormat formatter3spot = new DecimalFormat("$##0.000");
                        //Reformat PM and PS and change their corresponding editText views to show the output
                        PerMin.setText(formatter3spot.format(PM));
                        PerSec.setText(formatter3spot.format(PS));
                        return;
                    }

                }else{
                    //Todo: If Wage has no enrty play this toast
                    Toast.makeText(this, "Must input Hourly Pay Rate", Toast.LENGTH_LONG).show();
                    break;
                }

        }

    }



    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            //make a number formatter to restrict the input
            NumberFormat formatter2spot = new DecimalFormat("##0.00");
            NumberFormat formatter1spot = new DecimalFormat("#0.0");

            Float NewTax = Float.valueOf(Tax.getText().toString());
            Float NewWage = Float.valueOf(Wage.getText().toString());
            Float NewHours = Float.valueOf(Hours.getText().toString());

            Wage.setText(formatter2spot.format(NewWage));
            Tax.setText(formatter1spot.format(NewTax));
            Hours.setText(formatter1spot.format(NewHours));
        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //TOdo: Add item in options menu to remove ads.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.RemoveAds:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=tech.nocturnallabs.scruf.howmuchismytimeworth"));
                startActivity(intent);
            /*Todo:Add this help menu
            case R.id.help:
                startActivity(new Intent(this, Help.class));
                return true;
                */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        if (interstitial.isReady()){
            moPubInterstitial.show();
        }else {
            // other code
        }
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {

    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {

    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {

    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {

    }
}
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   /** public Action getIndexApiAction() {
        return Actions.newView("Main", "http://tech.nocturnallabs.scruf.howmuchismytimeworth");
        }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }
 */

