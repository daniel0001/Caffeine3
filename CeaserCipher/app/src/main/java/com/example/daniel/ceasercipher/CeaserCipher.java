
/**
    Caesar Cipher Cryptography. Enter some text at the input text field,
and click the encrypt or decrypt buttons to perform a caesar cipher
shift. For example, encrypting 'marty' gives 'pduwb', and decrypting
'dqgurlg' gives 'android'.
 */

package com.example.daniel.ceasercipher;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class CeaserCipher extends AppCompatActivity {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceaser_cipher);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void Encrypt(View view) {
        EditText input = (EditText)findViewById(R.id.input);
        TextView encrypted = (TextView)findViewById(R.id.Output);
        String output = input.getText().toString();
        encrypted.setText(performEncryption(output));
    }

    private String performEncryption(String output) {
        String encryptedWord = "";
        char newChar = 'x';
        for(int i = 0; i < output.length(); i++) {
            newChar = output.charAt(i);
            newChar++;
            encryptedWord += newChar;
        }
        return encryptedWord;
    }



    public void Decrypt(View view) {
        EditText input = (EditText)findViewById(R.id.input);
        TextView encrypted = (TextView)findViewById(R.id.Output);
        String output = input.getText().toString();
        encrypted.setText(performDecryption(output));
    }

    private String performDecryption(String output) {
        String encryptedWord = "";
        char newChar = 'x';
        for(int i = 0; i < output.length(); i++) {
            newChar = output.charAt(i);
            newChar--;
            encryptedWord += newChar;
        }
        return encryptedWord;
    }

}
