package com.mateus.tripadvisorapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Lucas on 19/11/2017.
 */

public class LocalizacaoAdapter extends BaseAdapter implements ListAdapter{

    private ArrayList<Localizacao> dataSet;
    private LayoutInflater layoutInflater;
    private Bitmap[] imagesList;

    private boolean SCROLL_STOP = true;

    public LocalizacaoAdapter(Context context , ArrayList objects) {
        dataSet = objects;
        layoutInflater = LayoutInflater.from(context);
        imagesList = new Bitmap[dataSet.size()];

        for (int i = 0; i < dataSet.size(); i++) {
            imagesList[i] = null;
        }
    }

    public boolean isSCROLL_STOP() {
        return SCROLL_STOP;
    }

    public void setSCROLL_STOP(boolean SCROLL_STOP) {
        this.SCROLL_STOP = SCROLL_STOP;
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

        if (imagesList[position] != null) {
            viewHolder.image.setImageBitmap(imagesList[position]);
        } else {
            Drawable defaultDrawable = viewHolder.image.getContext().getResources().getDrawable(R.drawable.ic_restaurant);
            viewHolder.image.setImageDrawable(defaultDrawable);
        }

        if (imagesList[position] == null && SCROLL_STOP) {
            new GetImageTask(viewHolder.image, imagesList, position).execute(localizacao.getImg_url());
        }


        return convertView;
    }

    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        private final WeakReference<Bitmap[]> imageListReference;
        private int position;

        public GetImageTask(ImageView imageView, Bitmap[] imageList, int pos) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            imageListReference = new WeakReference<Bitmap[]>(imageList);
            position = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("ASYNC", "CREATES ME");
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
            ImageView imageView = imageViewWeakReference.get();
            Bitmap[] imageList = imageListReference.get();

            if (imageView != null && imageList != null && result != null) {
                imageView.setImageBitmap(result);
                imageList[position] = result;
            }
        }
    }
}