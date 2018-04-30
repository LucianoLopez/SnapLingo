package teamc.finalproject;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class WordListGenerator {

    /**
     * Use this to get an ArrayList of Word Objects
     */
    String[] words = new String[2];
    public int size;
    public HashMap<String, String> lwords;
    public ArrayList<Word> wordList;
    private final String URI = "http://roger.redevised.com/api/v1";

    public ArrayList<Word> getWordList(int size) {
        HashMap<String, String> lwords = new HashMap<>();
        wordList = new ArrayList<>();
        GenerateList gl = new GenerateList();
        return wordList;
    }
    private void executeWordPair() {
        ObjectTask ot = new ObjectTask();
        SpanishTranslationTask stt = new SpanishTranslationTask();
        try {
            getObject(ot);
            getTranslation(stt);
        } catch (Exception e) {
            return;
        }
    }

    private void getObject(ObjectTask ot) throws Exception {
        ot.execute(URI);
    }
    private void getTranslation(SpanishTranslationTask stt) throws Exception {
        stt.execute(translations(words[0]));
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

    private class GenerateList extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String...params) {
            try {
                for (int i = 0; i < size; i++) {
                    executeWordPair();
                    if (words[1] == null || lwords.containsKey(words[0])) {
                        i -= 1;
                        continue;
                    }
                    Word input = new Word(words[0], words[1]);
                    wordList.add(input);
                    lwords.put(words[0], words[1]);
                    words = new String[2];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }
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
        }

    }

    /**
     * Returns the Spanish equivalent of an English word from the Oxford Dictionary language
     * translator.
     * https://developer.oxforddictionaries.com/documentation
     */
    private class SpanishTranslationTask extends AsyncTask<String, Integer, String> {

        String result = null;
        final String APP_ID = "8c959af2";
        final String APP_KEY = "2eb19e337f6cf66ad223c6c0f77f5d9b";
        @Override
        protected String doInBackground(String... params) {
            ByteArrayOutputStream result = new ByteArrayOutputStream(50);
            StringBuilder results1 = new StringBuilder();
            //used to be outputByteArray
            String word = null;
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", APP_ID);
                urlConnection.setRequestProperty("app_key", APP_KEY);

                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String current = null;
                while ((current = rd.readLine()) != null) {
//                    result.write((byte) current);
                    results1.append(current);
                }
                urlConnection.disconnect();
            } catch (Exception e) {
//                e.printStackTrace();
            }
            try {
                JSONObject jOBject = new JSONObject(results1.toString());
                JSONArray results = jOBject.getJSONArray("results");
                JSONArray lexicalEntries = results.getJSONObject(0).getJSONArray("lexicalEntries");
                JSONArray entries = lexicalEntries.getJSONObject(0).getJSONArray("entries");
                JSONArray senses = entries.getJSONObject(0).getJSONArray("senses");
                JSONArray translations = senses.getJSONObject(0).getJSONArray("translations");
                word = translations.getJSONObject(0).getString("text");

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

