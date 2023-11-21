package com.example.unal.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.EmpresaViewHolder> {

    private List<Empresa> empresas;

    public EmpresaAdapter(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    @NonNull
    @Override
    public EmpresaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empresa, parent, false);
        return new EmpresaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpresaViewHolder holder, int position) {
        Empresa empresa = empresas.get(position);
        holder.bind(empresa);
    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    public static class EmpresaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewId;
        private TextView textViewNombreEmpresa;
        private TextView textViewClasificacion;

        public EmpresaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewNombreEmpresa = itemView.findViewById(R.id.textViewNombreEmpresa);
            textViewClasificacion = itemView.findViewById(R.id.textViewClasificacion);
        }

        public void bind(Empresa empresa) {
            textViewId.setText(empresa.id);
            textViewNombreEmpresa.setText(empresa.nombreEmpresa);
            textViewClasificacion.setText(empresa.clasificacion);
        }
    }
}
