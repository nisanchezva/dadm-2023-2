package com.example.unal.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import com.example.unal.myapplication.DatabaseHelper;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import java.util.ArrayList;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public EditText  eTId;
    public EditText  eTNombreEmpresa;
    public EditText  eTUrlE;
    public EditText  eTTelefono;
    public EditText  eTEmail;
    public EditText  eTProductos;
    public EditText  eTClasificacion;

    public RecyclerView recyclerView;
    public DatabaseManager dbManager;
    public EmpresaAdapter empresaAdapter;

    public EditText  eTFiltroNombreEmpresa;
    public EditText  eTFiltroClasificacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eTId = (EditText) findViewById(R.id.textoIdEmpresa);
        eTNombreEmpresa = (EditText) findViewById(R.id.textoNombreEmpresa);
        eTUrlE = (EditText) findViewById(R.id.textoUrlEmpresa);
        eTTelefono = (EditText) findViewById(R.id.textoTelefonoEmpresa);
        eTEmail = (EditText) findViewById(R.id.textoEmailEmpresa);
        eTProductos = (EditText) findViewById(R.id.textoProducosEmpresa);
        eTClasificacion = (EditText) findViewById(R.id.textoClasificacionEmpresa);
        eTFiltroNombreEmpresa = (EditText) findViewById(R.id.textoFiltroNombreEmpresa);
        eTFiltroClasificacion = (EditText) findViewById(R.id.textoFiltroClasificacionEmpresa);

        dbManager = new DatabaseManager(this);

        try{
            dbManager.open();
        }catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView = findViewById(R.id.recyclerView); // Asegúrate de que este ID coincida con el RecyclerView en tu diseño XML
        // Configura el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Crea una lista de empresas (puedes obtenerla de tu base de datos o de cualquier fuente de datos)
        List<Empresa> empresas = fetching("",""); // Debes implementar este método
        // Crea y configura el adaptador
        empresaAdapter = new EmpresaAdapter(empresas);
        recyclerView.setAdapter(empresaAdapter);
    }
    public void btnInsert(View v){
        String nombreEmpresa = eTNombreEmpresa.getText().toString();
        String clasificacion = eTClasificacion.getText().toString();

        if (TextUtils.isEmpty(nombreEmpresa) || TextUtils.isEmpty(clasificacion)) {
            Toast.makeText(this, "Nombre y clasificación no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        DataEmpresa dataEmpresa = new DataEmpresa();
        dataEmpresa.nombreEmpresa		= eTNombreEmpresa.getText().toString();
        dataEmpresa.urlE				= eTUrlE.getText().toString();
        dataEmpresa.telefono			= eTTelefono.getText().toString();
        dataEmpresa.email				= eTEmail.getText().toString();
        dataEmpresa.productos			= eTProductos.getText().toString();
        dataEmpresa.clasificacion		= eTClasificacion.getText().toString();
        try {
            dbManager.insert(dataEmpresa);
            Toast.makeText(this, "Empresa Agregada", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Log.i("mesa","ERRROR");
            e.printStackTrace();
        }
    }
    public void btnUpdate(View v){

        String idString = eTId.getText().toString().trim();
        String nombreEmpresa = eTNombreEmpresa.getText().toString();
        String clasificacion = eTClasificacion.getText().toString();
        if(!idString.isEmpty() && !TextUtils.isEmpty(nombreEmpresa) && !TextUtils.isEmpty(clasificacion)){

            if (IdExist(Long.parseLong(idString))) {
                DataEmpresa dataEmpresa = new DataEmpresa();
                dataEmpresa.nombreEmpresa = eTNombreEmpresa.getText().toString();
                dataEmpresa.urlE = eTUrlE.getText().toString();
                dataEmpresa.telefono = eTTelefono.getText().toString();
                dataEmpresa.email = eTEmail.getText().toString();
                dataEmpresa.productos = eTProductos.getText().toString();
                dataEmpresa.clasificacion = eTClasificacion.getText().toString();
                dbManager.update(Long.parseLong(eTId.getText().toString()), dataEmpresa);
                Toast.makeText(this, "Empresa Actualizada", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "La ID no existe", Toast.LENGTH_SHORT).show();
            }
        }else{

            Toast.makeText(this, "Hay campos vacíos", Toast.LENGTH_SHORT).show();
        }
    }
    public void btnDelete(View v) {
        String idString = eTId.getText().toString().trim();
        if(!idString.isEmpty()){
            if (IdExist(Long.parseLong(idString))) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Estás seguro de que quieres eliminar esta empresa?")
                        .setTitle("Confirmar eliminación");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        dbManager.delete(Long.parseLong(eTId.getText().toString()));
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                Toast.makeText(this, "La ID no existe", Toast.LENGTH_SHORT).show();
            }
    }else{
            Toast.makeText(this, "El campo de ID está vacío", Toast.LENGTH_SHORT).show();
        }
        }

    public void btnFetch(View v){
        String filtroNombre = eTFiltroNombreEmpresa.getText().toString();
        String filtroClasificacion = eTFiltroClasificacion.getText().toString();
        recyclerView = findViewById(R.id.recyclerView); // Asegúrate de que este ID coincida con el RecyclerView en tu diseño XML
        // Configura el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Crea una lista de empresas (puedes obtenerla de tu base de datos o de cualquier fuente de datos)
        List<Empresa> empresas =  fetching(filtroNombre,filtroClasificacion); // Debes implementar este método
        // Crea y configura el adaptador
        empresaAdapter = new EmpresaAdapter(empresas);
        recyclerView.setAdapter(empresaAdapter);
    }

    public List<Empresa> fetching(String filtroNombre,String filtroClasificacion){
        Cursor cursor = dbManager.fetch(filtroNombre,filtroClasificacion);

        List<Empresa> empresas = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Empresa empresa = new Empresa();
                String ID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_ID));
                String nombreEmpresa = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOMBRE_EMPRESA));
                String clasificacion = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CLASIFICACION));
                empresa.id = ID;
                empresa.nombreEmpresa = nombreEmpresa;
                empresa.clasificacion = clasificacion;
                empresas.add(empresa);
            }while(cursor.moveToNext());
        }
        return empresas;
    }

    private boolean IdExist(long id) {
        Cursor cursor = dbManager.fetchById(id);
        boolean idExist = cursor.moveToFirst();
        cursor.close();
        return idExist;
    }

}