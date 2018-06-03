package com.example.texteditor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText mTextEditor = null;
    //private static final String FILENAME = "sample.txt";
    public static ArrayList<String> mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //RelativeLayout layout = findViewById(R.id.LayoutID);
        mTextEditor = findViewById(R.id.MainTextEditor);
        mFileName = new ArrayList<>();
        mFileName.add(0, "sample.txt");
        //File file = new File(Environment.DIRECTORY_DOWNLOADS.toString() + "/" +"example.txt");

        getWindow().setBackgroundDrawableResource(R.drawable.dark_background);
        mTextEditor.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:
                openFile(mFileName.get(0));                                         //!!!!!!!!!!!!!!!!
                return true;
            case R.id.action_save:
                saveFile(mFileName.get(0));                                         //!!!!!!!!!!!!!!!!
                return true;
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_new:
                mTextEditor.selectAll();
                mTextEditor.setText("");
            default:
                return true;
        }
    }

    private void openFile(String fileName) {    //PLUS ITEM
        /*File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/" + fileName);
        try {
            FileInputStream inputStream = new FileInputStream(myFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                mTextEditor.setText(stringBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        /*------------------------------------------------------------------------*/
        try {
            InputStream inputStream = openFileInput(mFileName.get(0));

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                inputStream.close();
                mTextEditor.setText(builder.toString());
            }
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void saveFile(String fileName) {
        /*try {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Log.d("log", "SD-карта не доступна: " + Environment.getExternalStorageState());
                return;
            }
            File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/" + fileName);
            myFile.createNewFile();                                         // Создается файл, если он не был создан
            FileOutputStream outputStream = new FileOutputStream(myFile);   // После чего создаем поток для записи
            outputStream.write(mTextEditor.getText().toString().getBytes());                            // и производим непосредственно запись
            outputStream.close();
            //Toast.makeText(this, R.string.write_done, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /******************************************************************************/
        try {
            OutputStream outputStream = openFileOutput(mFileName.get(0), 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(mTextEditor.getText().toString());
            osw.close();
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(getString(R.string.pref_openmode), false)) {
            openFile(mFileName.get(0));
        }

        float fSize = Float.parseFloat(prefs.getString(
                getString(R.string.pref_size), "14"));
        mTextEditor.setTextSize(fSize);

        String regular = prefs.getString(getString(R.string.pref_style), "");
        int typeface = Typeface.NORMAL;
        if (regular.contains("Полужирный")) {
            typeface += Typeface.BOLD;
        }
        if (regular.contains("Курсив")) {
            typeface += Typeface.ITALIC;
        }
        mTextEditor.setTypeface(null, typeface);

        String theme = prefs.getString(getString(R.string.pref_background), "");
        if(theme.contains("Темная")){
            getWindow().setBackgroundDrawableResource(R.drawable.dark_background);
            mTextEditor.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        if(theme.contains("Серая")){
            getWindow().setBackgroundDrawableResource(R.drawable.paper_background);
            mTextEditor.setTextColor(getResources().getColor(R.color.colorDark));
        }
        if(theme.contains("Светлая")){
            getWindow().setBackgroundDrawableResource(R.drawable.light_background);
            mTextEditor.setTextColor(getResources().getColor(R.color.colorBlack));
        }

        String file = prefs.getString(getString(R.string.choose_file), "sample");
          mFileName.add(0, file+".txt");
    }
}
