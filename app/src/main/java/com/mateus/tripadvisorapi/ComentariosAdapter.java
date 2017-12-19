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
 * Created by Lucka on 19/12/2017.
 */

public class ComentariosAdapter extends ArrayAdapter<Comentarios> {

    private ArrayList<Comentarios> dataSet;
    Context mContext;

    static class ViewHolder {
        public TextView name;
        public TextView text;
        public TextView evaluation;
        public ImageView image;
    }

    public ComentariosAdapter(Context context, int resource, ArrayList<Comentarios> objects) {
        super(context, resource, objects);
        this.dataSet = objects;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comentarios dataModel = getItem(position);
        ComentariosAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ComentariosAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            /*convertView = inflater.inflate(R.layout.item_list, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.itemName);
            viewHolder.text = (TextView) convertView.findViewById(R.id.itemText);
            viewHolder.evaluation = (TextView) convertView.findViewById(R.id.itemAvaliacao);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            */
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ComentariosAdapter.ViewHolder) convertView.getTag();
        }

        lastPosition = position;

        viewHolder.name.setText(dataModel.getUser().getName());
        viewHolder.text.setText(dataModel.getText());
        viewHolder.evaluation.setText(String.format("%.1f", dataModel.getRating()));
        if (viewHolder.image != null) {
            new ComentariosAdapter.GetImageTask(viewHolder.image).execute(dataModel.getUser().getImg_url());
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
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_restaurant));
                }
            }
        }
    }
}
