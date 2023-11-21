package co.edu.unal.reto10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<MyDataModel> dataSet;

    public MyAdapter(ArrayList<MyDataModel> data) {
        this.dataSet = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtPeriodo;
        TextView txtGenero;
        TextView txtFacultad;
        TextView txtProgramaAcademico;
        TextView txtMetodologia;
        TextView txtNombreDelApoyo;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtPeriodo = itemView.findViewById(R.id.txt_periodo);
            this.txtGenero = itemView.findViewById(R.id.txt_genero);
            this.txtFacultad = itemView.findViewById(R.id.txt_facultad);
            this.txtProgramaAcademico = itemView.findViewById(R.id.txt_programa_academico);
            this.txtNombreDelApoyo = itemView.findViewById(R.id.txt_nombre_del_apoyo);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView txtPeriodo = holder.txtPeriodo;
        TextView txtGenero = holder.txtGenero;
        TextView txtFacultad = holder.txtFacultad;
        TextView txtProgramaAcademico = holder.txtProgramaAcademico;
        TextView txtNombreDelApoyo = holder.txtNombreDelApoyo;

        txtPeriodo.setText(dataSet.get(listPosition).getPeriodo());
        txtGenero.setText(dataSet.get(listPosition).getGenero());
        txtFacultad.setText(dataSet.get(listPosition).getFacultad());
        txtProgramaAcademico.setText(dataSet.get(listPosition).getProgramaAcademico());
        txtNombreDelApoyo.setText(dataSet.get(listPosition).getNombreDelApoyo());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
