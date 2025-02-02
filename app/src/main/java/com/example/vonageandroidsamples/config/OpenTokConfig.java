package com.example.vonageandroidsamples.config;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public class OpenTokConfig {
    /*
    Fill the following variables using your own Project info from the OpenTok dashboard
    https://dashboard.tokbox.com/projects

    Note that this application will ignore credentials in the `OpenTokConfig` file when `CHAT_SERVER_URL` contains a
    valid URL.
    */

    // Replace with a API key
    public static String API_KEY = "47763931";
    
    // Replace with a generated Session ID
    public static String SESSION_ID = "1_MX40Nzc2MzkzMX5-MTcyMzQ1ODk1MDUwMH5NQ0RITDJ0QkJQSzZRd1BrbHU2T01uUnV-fn4";
    
    // Replace with a generated token (from the dashboard or using an OpenTok server SDK)
    public static String TOKEN = "T1==cGFydG5lcl9pZD00Nzc2MzkzMSZzaWc9MTk3NzliZWU0ZWJjMDZlMzRjNmFiZWU4YWUzMWNmOGFmNWQxY2YxNjpzZXNzaW9uX2lkPTFfTVg0ME56YzJNemt6TVg1LU1UY3lNelExT0RrMU1EVXdNSDVOUTBSSVRESjBRa0pRU3paUmQxQnJiSFUyVDAxdVVuVi1mbjQmY3JlYXRlX3RpbWU9MTcyMzQ1ODk2MSZub25jZT0wLjE4NjI5NDQ3MDM0MzE2NDYmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTcyNjA1MDk2MSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";

    // *** The code below is to validate this configuration file. You do not need to modify it  ***

    public static boolean isValid() {
        if (TextUtils.isEmpty(OpenTokConfig.API_KEY)
                || TextUtils.isEmpty(OpenTokConfig.SESSION_ID)
                || TextUtils.isEmpty(OpenTokConfig.TOKEN)) {
            return false;
        }

        return true;
    }

    @NonNull
    public static String getDescription() {
        return "OpenTokConfig:" + "\n"
                + "API_KEY: " + OpenTokConfig.API_KEY + "\n"
                + "SESSION_ID: " + OpenTokConfig.SESSION_ID + "\n"
                + "TOKEN: " + OpenTokConfig.TOKEN + "\n";
    }
}
