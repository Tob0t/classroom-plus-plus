package ex5.it.mcm.fhooe.classroomplusplus.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import ex5.it.mcm.fhooe.classroomplusplus.R;
import ex5.it.mcm.fhooe.classroomplusplus.ui.availability.AvailabilityFragment;
import ex5.it.mcm.fhooe.classroomplusplus.ui.voting.VoteFragment;
import ex5.it.mcm.fhooe.classroomplusplus.ui.voting.VoteResultsActivity;
import ex5.it.mcm.fhooe.classroomplusplus.utils.Constants;
import ex5.it.mcm.fhooe.classroomplusplus.utils.DataService;
import ex5.it.mcm.fhooe.classroomplusplus.utils.NFCManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private NFCManager nfcMger;
    private View v;
    private Tag mTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v = findViewById(R.id.fragment_container);
        nfcMger = new NFCManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;

        // Handle navigation view item clicks here.
        switch(item.getItemId()) {
            case R.id.nav_vote:
                //startActivity(new Intent(getApplicationContext(), VoteActivity.class));
                fragmentClass = VoteFragment.class;
                break;
            case R.id.nav_availability:
                fragmentClass = AvailabilityFragment.class;
            default:
                fragmentClass = MainFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // Set action bar title
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        readIntent(intent);
    }

    @Override
    protected void onPause() {super.onPause();
        NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        nfcAdpt.disableForegroundDispatch(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            nfcMger.verifyNFC();
            Intent nfcIntent = new Intent(this, getClass());
            nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);

            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try {
                ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
            }
            catch (IntentFilter.MalformedMimeTypeException e) {
                throw new RuntimeException("fail", e);
            }
            IntentFilter[] intentFiltersArray = new IntentFilter[] {ndef, };
            String [][] techListsArray = new String[][] { new String[] {NfcA.class.getName() } };

            NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
            nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
        }

        catch(NFCManager.NFCNotSupported nfcnsup) {
            Snackbar.make(v, "NFC not supported", Snackbar.LENGTH_LONG).show();
        }

        catch(NFCManager.NFCNotEnabled nfcnEn) {
            Snackbar.make(v, "NFC Not enabled", Snackbar.LENGTH_LONG).show();
        }

        // read Intent if NDEF discovered
        if(getIntent().getAction() == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            readIntent(getIntent());
        }

    }

    private void readIntent(Intent intent) {
        Log.d("LOG_TAG", "onNewIntent: new Intent");
        Snackbar.make(v, "Tag detected", Snackbar.LENGTH_LONG).show();
        mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Log.d("LOG_TAG", "ACTION_NDEF_DISCOVERED");
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            new NdefReaderTask().execute(tag);
        }
    }

    // To display the UID
    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            if(ndefMessage != null) {
                NdefRecord[] records = ndefMessage.getRecords();
                for (NdefRecord ndefRecord : records) {
                    //if (ndefRecord.getTnf() == NdefRecord.TNF_MIME_MEDIA) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e("ReaderTask", "Unsupported Encoding", e);
                    }
                    //}
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, 0, payload.length, textEncoding);

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                String answer = "";
                String voteId = "";
                try {
                    JSONObject jsonRootObject = new JSONObject(result);
                    JSONObject survey = jsonRootObject.optJSONObject("survey");
                    String roomId = survey.optString("roomId");
                    voteId = survey.optString("voteId");
                    answer = survey.optString("answer");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //.setText("Read content: " + answer);
                Log.d("MAIN", "Read content: "+answer);
                if(answer.equalsIgnoreCase(Constants.Answer.LEFT.toString())){
                    Log.d("MAIN", "Forward to LEFT");
                    Snackbar.make(v, "Left tag has been selected ..", Snackbar.LENGTH_LONG).show();
                    handleVoting(voteId, Constants.Answer.LEFT);
                } else if(answer.equalsIgnoreCase(Constants.Answer.RIGHT.toString())) {
                    Log.d("MAIN", "Forward to RIGHT");
                    Snackbar.make(v, "Right tag has been selected ..", Snackbar.LENGTH_LONG).show();
                    handleVoting(voteId, Constants.Answer.RIGHT);
                }
            }
        }
    }

    private void handleVoting(final String voteId, final Constants.Answer answer) {
        Query surveyRef = new Firebase(Constants.FIREBASE_URL_SURVEYS).limitToLast(1);

        surveyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String surveyId = dataSnapshot.getKey();
                transmitVoting(voteId, answer, surveyId);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void transmitVoting(String voteId, Constants.Answer answer, String surveyId) {
        Log.d("MAIN", "tagID: "+voteId);
        // send SurveyId to Activity
        Intent intent = new Intent(this.getApplicationContext(), VoteResultsActivity.class);
        intent.putExtra(Constants.KEY_SURVEY_ID, surveyId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        DataService.sendAnswer(answer, voteId, surveyId);
        startActivity(intent);
    }
}
