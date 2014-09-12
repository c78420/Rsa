package com.example.rsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
    private Button setByteBtn = null;
    private Button createKeyBtn = null;
    private TextView createKeyTimeTxt = null;
    private Button encryptionBtn = null;
    private TextView encryptionTimeTxt = null;
    private Button doAllBtn = null;
    private TextView doAllTimeTxt = null;
    private Button decryptionBtn = null;
    private TextView decryptionTimeTxt = null;
    private TextView decryptionTxt = null;
    
    private String inputStr = "";
    private byte [] encryptionByt;
    
    private PublicKey pubKey = null;
    private PrivateKey privKey = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        byteCountTxt = (TextView) findViewById(R.id.textView2);
        inputEdt = (EditText) findViewById(R.id.editText1);
        setByteBtn = (Button) findViewById(R.id.button5);
        createKeyBtn = (Button) findViewById(R.id.button3);
        createKeyTimeTxt = (TextView) findViewById(R.id.textView8);
        encryptionBtn = (Button) findViewById(R.id.button1);
        encryptionTimeTxt = (TextView) findViewById(R.id.textView4);
        doAllBtn = (Button) findViewById(R.id.button4);
        doAllTimeTxt = (TextView) findViewById(R.id.textView10);
        decryptionBtn = (Button) findViewById(R.id.button2);
        decryptionTimeTxt = (TextView) findViewById(R.id.textView6);
        decryptionTxt = (TextView) findViewById(R.id.textView12);
        
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
        
        setByteBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                inputEdt.setText("1234567890abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz12");
            }
        });
        
        createKeyBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                caeateKey();
            }
        });
        
        encryptionBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                encryption();
            }
        });
        
        doAllBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                long star = 0;
                long end = 0;
                
                star = SystemClock.elapsedRealtime();
                
                caeateKey();
                encryption();
                
                end = SystemClock.elapsedRealtime();
                doAllTimeTxt.setText(Double.toString((end - star)/1000.0));
            }
        });
        
        decryptionBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                decryption();
            }
        });
    }

    private void caeateKey() {
        try {
            long star = 0;
            long end = 0;
            
            star = SystemClock.elapsedRealtime();
            
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
//            random.setSeed("test".getBytes());
            keygen.initialize(2048, random);           // Generate 2048-bit keys
            KeyPair generatedKeyPair = keygen.generateKeyPair();
            
            pubKey = generatedKeyPair.getPublic();
            Log.i("test", "Public Key: " + getHexString(pubKey.getEncoded()));
            privKey = generatedKeyPair.getPrivate();
            Log.i("test", "Private Key: " + getHexString(privKey.getEncoded()));
            
            end = SystemClock.elapsedRealtime();
            createKeyTimeTxt.setText(Double.toString((end - star)/1000.0));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private String getHexString(byte[] encoded) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < encoded.length; i++)
            result.append(Integer.toString((encoded[i] & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
    
    private void encryption() {
        try {
            long star = 0;
            long end = 0;
            
            star = SystemClock.elapsedRealtime();
            
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            int blockSize = 64; //要加密字串的大小，可以小於等於，如果大於會出錯
            int writtenSize;
            ByteArrayOutputStream out = new ByteArrayOutputStream(blockSize);
     
            for (int readedBytes=0 ; readedBytes<inputStr.getBytes().length ; readedBytes+=blockSize) {
                if(inputStr.getBytes().length > blockSize)
                    writtenSize = blockSize;
                else
                    writtenSize = inputStr.getBytes().length - readedBytes;
                out.write(cipher.doFinal(inputStr.getBytes(), readedBytes, writtenSize));
            }
            
            encryptionByt = out.toByteArray();
            Log.i("test", new String(encryptionByt));
            
            end = SystemClock.elapsedRealtime();
            encryptionTimeTxt.setText(Double.toString((end - star)/1000.0));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void decryption() {
        int blockSize = 256;
        try {
            long star = 0;
            long end = 0;
            
            star = SystemClock.elapsedRealtime();
            
            ByteArrayOutputStream out = new ByteArrayOutputStream(blockSize);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            
            int j = 0;
            int length = encryptionByt.length - j*blockSize;
            while ((length = encryptionByt.length-j*blockSize) > 0) {
                if(length > blockSize)
                    length = blockSize;
                else
                    length = encryptionByt.length;
                out.write(cipher.doFinal(encryptionByt, j*blockSize, length));
                ++j;
            }
            
            decryptionTxt.setText(new String(out.toByteArray()));
            Log.i("test", new String(out.toByteArray()));
            
            end = SystemClock.elapsedRealtime();
            decryptionTimeTxt.setText(Double.toString((end - star)/1000.0));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); 
        } catch (NoSuchPaddingException e) {
            e.printStackTrace(); 
        } catch (InvalidKeyException e) {
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace(); 
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
