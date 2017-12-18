package com.mateus.tripadvisorapi;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class LocalizacaoAdapter extends ArrayAdapter<Localizacao> {

    private ArrayList<Localizacao> dataSet;
    Context mContext;

    // View lookup cache
    static class ViewHolder {
        public TextView title; // title
        public TextView address; // artist name
        public TextView evaluation; // duration
        public TextView value; // duration
        public TextView phone; // duration
        public ImageView image;
    }

    public LocalizacaoAdapter(Context context, int resource, ArrayList<Localizacao> objects) {
        super(context, resource, objects);
        this.dataSet = objects;
        this.mContext=context;

    }

    /*@Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

        switch (v.getId())
        {
            case R.id.item_info:
                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }*/

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Localizacao dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list, parent, false);
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

        lastPosition = position;

        viewHolder.title.setText(dataModel.getTitle());
        viewHolder.address.setText(dataModel.getAddress());
        viewHolder.evaluation.setText(Float.toString(dataModel.getRating()));
        viewHolder.value.setText(dataModel.getPrice());
        viewHolder.phone.setText(dataModel.getPhone());
        if (viewHolder.image != null) {
            new GetImageTask(viewHolder.image).execute(dataModel.getImg_url());
        }
//        viewHolder.image.setImageBitmap(GetImage.execute());

        /*viewHolder.value.setOnClickListener(this);
        viewHolder.info.setTag(position);
        */// Return the completed view to render on screen
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
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_restaurant));
                }
            }
        }
    }
}