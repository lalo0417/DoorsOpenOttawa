package com.algonquincollege.lalo0417.dooropenottawa;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.algonquincollege.lalo0417.dooropenottawa.model.Building;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by CalebLalonde on 2016-11-08.
 */

public class BuildingAdapter extends ArrayAdapter<Building> {
    private Context context;
    private List<Building> buildingList;

    private LruCache<Integer, Bitmap> imageCache;

    public BuildingAdapter(Context context, int resource, List<Building> objects) {
        super(context, resource, objects);
        this.context = context;
        this.buildingList = objects;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_building, parent, false);

        Building building = buildingList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        TextView tvAddress = (TextView) view.findViewById(R.id.textView2);
        tv.setText(building.getName());
        tvAddress.setText(building.getAddress());

        Bitmap bitmap = imageCache.get(building.getBuildingID());
        if (bitmap != null) {
            Log.i("BUILDINGS", building.getName() + "\tbitmap in cache");
            ImageView image = (ImageView) view.findViewById(R.id.imageView1);
            image.setImageBitmap(building.getBitmap());
        } else {
            Log.i("BUILDINGS", building.getName() + "\tfetching bitmap using AsyncTask");
            BuildingAndView container = new BuildingAndView();
            container.building = building;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }
        if (position % 2 == 1) {
            view.setBackgroundColor(context.getResources().getColor(R.color.listBackground1));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.listBackground2));
        }
        return view;
    }

    private class BuildingAndView {
        public Building building;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<BuildingAndView, Void, BuildingAndView> {

        @Override
        protected BuildingAndView doInBackground(BuildingAndView... params) {

            BuildingAndView container = params[0];
            Building building = container.building;

            try {
                String imageUrl = MainActivity.IMAGES_BASE_URL + building.getImage();
                InputStream in = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                building.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            } catch (Exception e) {
                System.err.println("IMAGE: " + building.getName());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(BuildingAndView result) {
            ImageView image = (ImageView) result.view.findViewById(R.id.imageView1);
            image.setImageBitmap(result.bitmap);
            imageCache.put(result.building.getBuildingID(), result.bitmap);
        }
    }
}

