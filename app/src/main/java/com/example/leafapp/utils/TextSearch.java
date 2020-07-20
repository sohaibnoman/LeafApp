package com.example.leafapp.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextSearch {
    private String TAG = "TextSearch";

    public HashMap<String, ArrayList<String>> getResults(String input_text, InputStream file_stream) {

        if (TextUtils.isEmpty(input_text)){
            return null;
        }
        String search_string = input_text;
        HashMap<String, ArrayList<String>> search_results = new HashMap<>();

        // Create regex with up to 3 words forgotten
        String reg = "";
        for (int i=0; i<search_string.length(); i++){
            reg = reg + search_string.charAt(i)+".{0,2}";
        }
        Pattern p = Pattern.compile(".*"+reg+".*");

        // Find matches to the regex
        Scanner sc = new Scanner(file_stream);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line.toLowerCase());
            boolean b = m.matches();
            if (b) {
                //Log.d(TAG, "getResults: " + p.toString() + " and " + line + "=" + b);;

                String[] line_split = line.split("@LEAFSPLIT@");
                String bok_name = line_split[0].trim();
                String bok_isbn = line_split[1].trim();
            }
        }
        return search_results;
    }

    // TODO NLP N-gram noisy channel

    // TODO Edit distance to catch misspellings
    
}
