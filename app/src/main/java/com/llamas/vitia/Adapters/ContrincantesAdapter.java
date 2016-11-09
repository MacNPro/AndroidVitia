package com.llamas.vitia.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.llamas.vitia.Model.Contrincante;
import com.llamas.vitia.R;

import java.util.ArrayList;
import java.util.List;

public class ContrincantesAdapter extends RecyclerView.Adapter<ContrincantesAdapter.ViewHolder> {

    private List<Contrincante> contrincantes;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View v) {
            super(v);
        }

    }

    public ContrincantesAdapter(Context context, List<Contrincante> contrincantes) {
        this.contrincantes = this.contrincantes;
        this.context = context;
    }

    @Override
    public ContrincantesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_contrincante, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return contrincantes.size();
    }
}


