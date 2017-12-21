package com.mateus.tripadvisorapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Lucas on 19/11/2017.
 */

public class LocalizacaoAdapter extends BaseAdapter{

    private ArrayList<Localizacao> dataSet;
    private LayoutInflater layoutInflater;

    public LocalizacaoAdapter(Context context , ArrayList objects) {
        dataSet = objects;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder {
        public TextView title;
        public TextView address;
        public TextView evaluation;
        public TextView value;
        public TextView phone;
        public ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_list, null);

            viewHolder.title = (TextView) convertView.findViewById(R.id.itemTitle);
            viewHolder.address = (TextView) convertView.findViewById(R.id.itemAddress);
            viewHolder.evaluation = (TextView) convertView.findViewById(R.id.itemAvaliacao);
            viewHolder.value = (TextView) convertView.findViewById(R.id.itemValor);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.itemTelefone);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Localizacao localizacao = (Localizacao) dataSet.get(position);

        viewHolder.title.setText(localizacao.getTitle());
        viewHolder.address.setText(localizacao.getAddress());
        viewHolder.evaluation.setText(String.format("%.1f", localizacao.getRating()));
        viewHolder.value.setText(localizacao.getPrice());
        viewHolder.phone.setText(localizacao.getPhone());

        if (viewHolder.image != null) {
            new GetImageTask(viewHolder.image).execute(localizacao.getImg_url(), String.valueOf(position));
        }

        return convertView;
    }

    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public GetImageTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                return BitmapFactory.decodeStream(conn.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                cancel(true);
            }

            return null;
        }

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            ImageView imageView = imageViewReference.get();

            if (imageView != null) {
                if (result != null) {
                    imageView.setImageBitmap(result);
                } else {
                    Drawable defaultDrawable = imageView.getContext().getResources().getDrawable(R.drawable.ic_restaurant);
                    imageView.setImageDrawable(defaultDrawable);
                }
            }
        }
    }
}