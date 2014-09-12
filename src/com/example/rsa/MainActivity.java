package com.example.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView byteCountTxt = null;
    private EditText inputEdt = null;
    private Button createKeyBtn = null;
    private TextView createKeyTimeTxt = null;
    private Button encryptionBtn = null;
    private TextView encryptionTimeTxt = null;
    private Button decryptionBtn = null;
    private TextView decryptionTxt = null;
    
    private String inputStr = "";
    
    private PublicKey pubKey = null;
    private PrivateKey privKey = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        byteCountTxt = (TextView) findViewById(R.id.textView2);
        inputEdt = (EditText) findViewById(R.id.editText1);
        createKeyBtn = (Button) findViewById(R.id.button3);
        createKeyTimeTxt = (TextView) findViewById(R.id.textView8);
        encryptionBtn = (Button) findViewById(R.id.button1);
        encryptionTimeTxt = (TextView) findViewById(R.id.textView4);
        decryptionBtn = (Button) findViewById(R.id.button2);
        decryptionTxt = (TextView) findViewById(R.id.textView6);
        
        inputEdt.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                inputStr = s.toString();
                byte[] b = inputStr.getBytes();
                byteCountTxt.setText(Integer.toString(b.length));
            }
        });
        
        createKeyBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    long star = 0;
                    long end = 0;
                    
                    star = SystemClock.elapsedRealtime();
                    
                    KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
                    SecureRandom random = new SecureRandom();
                    keygen.initialize(2048, random);           // Generate 2048-bit keys
                    KeyPair generatedKeyPair = keygen.generateKeyPair();
                    
                    pubKey = generatedKeyPair.getPublic();
                    Log.i("test", "Public Key: " + getHexString(pubKey.getEncoded()));
                    privKey = generatedKeyPair.getPrivate();
                    Log.i("test", "Private Key: " + getHexString(privKey.getEncoded()));
                    
                    end = SystemClock.elapsedRealtime();
                    
                    createKeyTimeTxt.setText(Double.toString((end - star)/1000.0));
                    Log.i("test", "" + (end - star)/1000.0);
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            private String getHexString(byte[] encoded) {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < encoded.length; i++)
                    result.append(Integer.toString((encoded[i] & 0xff) + 0x100, 16).substring(1));
                return result.toString();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
