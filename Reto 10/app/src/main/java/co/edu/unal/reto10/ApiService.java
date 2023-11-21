package co.edu.unal.reto10;

import com.google.gson.JsonArray;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("resource/q455-q5b5.json?$limit=57400")
    Call<JsonArray> getData();
}
