package co.edu.unal.reto10;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import co.edu.unal.reto10.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button mConsultar;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ArrayList<MyDataModel> dataModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConsultar = findViewById(R.id.do_query_button);
        mRecyclerView = findViewById(R.id.recycler_view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(dataModels);
        mRecyclerView.setAdapter(mAdapter);

        mConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataModels.clear();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://www.datos.gov.co/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                ApiService apiService = retrofit.create(ApiService.class);


                Call<JsonArray> call = apiService.getData();

                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            JsonArray dataArray = response.body();

                            for (int i = 0; i < dataArray.size(); i++) {
                                try {

                                    MyDataModel dataModel = new MyDataModel();
                                    dataModel.setPeriodo(getAsString(dataArray.get(i).getAsJsonObject(), "periodo"));
                                    dataModel.setGenero(getAsString(dataArray.get(i).getAsJsonObject(), "genero"));
                                    dataModel.setFacultad(getAsString(dataArray.get(i).getAsJsonObject(), "facultad"));
                                    dataModel.setProgramaAcademico(getAsString(dataArray.get(i).getAsJsonObject(), "programa_academico"));
                                    dataModel.setNombreDelApoyo(getAsString(dataArray.get(i).getAsJsonObject(), "nombre_del_apoyo"));



                                    dataModels.add(dataModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("Error", "Error en la respuesta de la API");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.e("Error", "Error de conexión: " + t.getMessage());
                    }
                });
            }
        });
    }


    private String getAsString(JsonObject jsonObject, String fieldName) {
        return jsonObject != null && jsonObject.has(fieldName) && !jsonObject.get(fieldName).isJsonNull()
                ? jsonObject.get(fieldName).getAsString()
                : "";
    }
}
