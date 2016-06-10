package erik.caa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewScoreActivity extends AppCompatActivity {

    private String selectUrl = "https://oege.ie.hva.nl/~leuvert001/php/mobdev/hiscoreSelect.php";
    private String scoreListResponse;
    private RequestQueue queue;
    private ArrayAdapter<String> scoreListAdapter ;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_score);
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        listView = (ListView)findViewById(R.id.scoreListView);


        getHiScores();
    }

    @Override
    public void onResume( ){

        super.onResume();
    }

    public void getHiScores() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, selectUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        scoreListResponse = response;
                        fillHiScores(response);

                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

        };
        queue.add(postRequest);
    }

    private void fillHiScores(String jsonString) {

        JSONObject object = null;
        JSONArray array = null;
        ArrayList<String> scoreList = new ArrayList<String>();
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("hiscore");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        scoreListAdapter =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, scoreList);

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject temp = array.getJSONObject(i);
                String name =temp.getString("name");
                String score = String.valueOf(temp.getInt("score"));
                String scorestring = String.format("NAME: %s, SCORE:  %s", name, score);
                scoreListAdapter.add(scorestring);
                Log.d(this.getLocalClassName(),name + score);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        listView.setAdapter(scoreListAdapter);
    }

}
