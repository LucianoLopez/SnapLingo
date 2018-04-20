package teamc.finalproject;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WordListGenerator {

    /**
     * Currently, only creates word pairs of [English object, Spanish translation].
     * TODO Test out the Spanish translation API and make an array of word pairs
     */
    String[] words = new String[2];
    private final String URI = "http://roger.redevised.com/api/v1";

    public String[] getWordList() {
        ObjectTask cbt = new ObjectTask();
        cbt.execute(URI);
        String word = cbt.result;
        words[0] = word;
        SpanishTranslationTask stt = new SpanishTranslationTask();
        String params = translations(word);
        stt.execute(params);
        return words;
    }
    /**
     Used to set parameters for the Oxford language Translation API
     */
    private String translations(String word) {
        final String language = "en";
        final String target_lang = "es";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id +
                "/translations=" + target_lang;
    }


    /**
     * Returns a common everyday object from the Roger Random Object Generator
     * http://roger.redevised.com/roger.redevised.com/api/v1#
     */
    private class ObjectTask extends AsyncTask<String, Integer, String> {

        private final String URI = "http://roger.redevised.com/api/v1";
        private String result = null;


        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(URI);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "You fuuuucked up";
            }

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            this.result = result;
            System.out.println(result);
        }

    }

    /**
     * FIXME Most likely doesn't work (Have yet to debug)
     * Returns the Spanish equivalent of an English word.
     *
     * Connects to the Oxford Dictionary language translator.
     * https://developer.oxforddictionaries.com/documentation
     *
     */
    private class SpanishTranslationTask extends AsyncTask<String, Integer, String> {

        String result = null;
        final String APP_ID = "8c959af2";
        final String APP_KEY = "2eb19e337f6cf66ad223c6c0f77f5d9b";
        @Override
        protected String doInBackground(String... params) {
            ByteArrayOutputStream result = new ByteArrayOutputStream(50);
            String word = null;
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", APP_ID);
                urlConnection.setRequestProperty("app_key", APP_KEY);
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                int current = 0;
                while ((current = rd.read()) != -1) {
                    result.write((byte) current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject jOBject = new JSONObject(result.toString());
                JSONArray results = jOBject.getJSONArray("results");
                JSONObject lexicalEntries = results.getJSONObject(0);
                JSONArray entries = lexicalEntries.getJSONArray("entries");
                JSONArray senses = entries.getJSONObject(0).getJSONArray("senses");
                JSONArray examples = senses.getJSONObject(0).getJSONArray("examples");
                JSONArray translations = examples.getJSONObject(0).getJSONArray("translations");
                word = translations.getJSONObject(0).getString("text");

//                JSONArray jArray = new JSONArray(result.toString());

//                JSONObject jObject =
            } catch (Exception e) {
                e.printStackTrace();
            }
            return word;
        }
        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            this.result = results;
        }
    }
}

