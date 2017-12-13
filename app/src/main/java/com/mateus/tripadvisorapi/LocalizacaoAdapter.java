package com.mateus.tripadvisorapi;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Lucas on 19/11/2017.
 */

public class LocalizacaoAdapter extends ArrayAdapter<Localizacao> {

    public LocalizacaoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Localizacao[] objects) {
        super(context, resource, objects);
    }

    static class ViewHolder {
        public TextView textEstabelecimento;
        public TextView textAvaliacao;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Localizacao localizacao = getItem(position);
        if (localizacao != null) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(android.R.layout.simple_expandable_list_item_2, null);
                LocalizacaoAdapter.ViewHolder holder = new LocalizacaoAdapter.ViewHolder();
                holder.textEstabelecimento = (TextView) view.findViewById(android.R.id.text1);
                holder.textAvaliacao = (TextView) view.findViewById(android.R.id.text2);
                view.setTag(holder);
            }
            LocalizacaoAdapter.ViewHolder holder = (LocalizacaoAdapter.ViewHolder)view.getTag();
            holder.textEstabelecimento.setText(localizacao.getEstabelecimento());
            holder.textAvaliacao.setText(localizacao.getAvaliacao());
        }

        return view;
    }
}