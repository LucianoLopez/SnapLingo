package teamc.finalproject;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class WordListGenerator {

    /**
     * Creates a Hashmap of N wordpairs {English -> Spanish}
     */
    String[] words = new String[2];
    ObjectTask ot = null;
    SpanishTranslationTask stt = null;
    private final String URI = "http://roger.redevised.com/api/v1";

    public HashMap<String, String> getWordList(int size) {
        ot = new ObjectTask();
        stt = new SpanishTranslationTask();
        HashMap<String, String> wordList = new HashMap<>();
        try {
            for (int i = 0; i < size; i++) {
                executeWordPair(ot, stt);
                if (words[1] == null || wordList.containsKey(words[0])) {
                    i -= 1;
                    continue;
                }
                wordList.put(words[0], words[1]);
                words = new String[2];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordList;
    }
    void executeWordPair(ObjectTask ot, SpanishTranslationTask stt) {
        ot = new ObjectTask();
        stt = new SpanishTranslationTask();
        try {
            getObject(ot);
            getTranslation(stt);
        } catch (Exception e) {
            return;
        }
    }

    void getObject(ObjectTask ot) throws Exception {
        ot.execute(URI).get();
    }
    void getTranslation(SpanishTranslationTask stt) throws Exception {
        stt.execute(translations(words[0])).get();
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
                String word = result.toString();
                words[0] = word;
                conn.disconnect();
                return word;
            } catch (Exception e) {
//                e.printStackTrace();
                return "You fuuuucked up";
            }

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            this.result = result;
            words[0] = result;
//            SpanishTranslationTask stt = new SpanishTranslationTask();
//            String params = translations(result);
//            stt.execute(params);
//            System.out.println(result);
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
                urlConnection.disconnect();
            } catch (Exception e) {
//                e.printStackTrace();
            }
            try {
                JSONObject jOBject = new JSONObject(result.toString());
                JSONArray results = jOBject.getJSONArray("results");
                JSONArray lexicalEntries = results.getJSONObject(0).getJSONArray("lexicalEntries");
                JSONArray entries = lexicalEntries.getJSONObject(0).getJSONArray("entries");
                JSONObject test = entries.getJSONObject(0);
                JSONArray senses = test.getJSONArray("senses");
//                JSONArray examples = senses.getJSONObject(0).getJSONArray("examples");
                JSONArray translations = senses.getJSONObject(0).getJSONArray("translations");
                word = translations.getJSONObject(0).getString("text");

//                JSONArray jArray = new JSONArray(result.toString());

//                JSONObject jObject =
            } catch (Exception e) {
//                e.printStackTrace();
            }
            words[1] = word;
            return word;
        }
        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            this.result = results;
            words[1] = results;
        }
    }
}

