package ex5.it.mcm.fhooe.classroomplusplus.utils;

import android.app.Activity;
import android.nfc.NfcAdapter;

/**
 * Created by Tob0t on 27.04.2016.
 */
public class NFCManager {
    private Activity activity;
    private NfcAdapter nfcAdpt;

    public NFCManager(Activity activity) {
        this.activity = activity;
    }

    public void verifyNFC() throws NFCNotSupported, NFCNotEnabled {
        nfcAdpt = NfcAdapter.getDefaultAdapter(activity);
        if (nfcAdpt == null)
            throw new NFCNotSupported("NFC not supported");
        if (!nfcAdpt.isEnabled())
            throw new NFCNotEnabled("NFC not enabled");
    }

    public class NFCNotSupported extends Exception {
        //Parameterless Constructor
        public NFCNotSupported() {}

        //Constructor that accepts a message
        public NFCNotSupported(String message)
        {
            super(message);

        }
    }

    public class NFCNotEnabled extends Exception {
        //Parameterless Constructor
        public NFCNotEnabled() {}

        //Constructor that accepts a message
        public NFCNotEnabled(String message)
        {
            super(message);
        }
    }
}
