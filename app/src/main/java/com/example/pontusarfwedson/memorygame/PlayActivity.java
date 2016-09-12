package com.example.pontusarfwedson.memorygame;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    GridView cardsLayout;
    ImageAdapter imgAd;
    TextView pointsTxt;
    boolean clickedOne = false;
    int lastPicId;
    int lastPicPos;
    int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        cardsLayout = (GridView) findViewById(R.id.gridCardsLayout);
        pointsTxt = (TextView) findViewById(R.id.txtPoints);

        imgAd = new ImageAdapter(this);
        cardsLayout.setAdapter(imgAd);
        cardsLayout.setClickable(true);

        cardsLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.d("PlayActivity: ", "clicked pos " + position + " with id " + id);
                ImageView imageView = (ImageView) v;
                imageView.setImageResource(imgAd.randPicIds.get(position));




                if(!clickedOne)
                {
                    lastPicId = imgAd.randPicIds.get(position);
                    lastPicPos = position;
                    clickedOne = true;
                }
                else
                {
                    clickedOne = false;
                    if(lastPicId == imgAd.randPicIds.get(position) && position != lastPicPos)
                    {
                        clickedOne = false;
                        points++;
                        pointsTxt.setText("Points: " + points);
                        imgAd.getImageView(lastPicPos).setVisibility(View.INVISIBLE);
                        imgAd.getImageView(position).setVisibility(View.INVISIBLE);

                    }
                    else if (lastPicId != imgAd.randPicIds.get(position))
                    {
                        Log.d("PlayActivity: ", "pos " + lastPicPos +" and pos " + position + " to be ?");

                       imgAd.getImageView(lastPicPos).setImageResource(R.drawable.qmark);
                       imgAd.getImageView(position).setImageResource(R.drawable.qmark);
                    }
                    else if(position  == lastPicPos)
                    {
                        clickedOne = true; //clicked same picture twice, should not count
                    }
                    else
                    {
                        Log.e("PlayActivity: ", "SHOULD NOT GET HERE, SOMETHING WRONG!");
                    }

                }

            }
        });

    }

    private int getImageSizes()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width/5;

    }


    /**
     * Outline of class ImageAdapter gotten from android developers page about GridView (https://developer.android.com/guide/topics/ui/layout/gridview.html)
     * Small changes to it been made to fit this project.
     */
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        public ArrayList<Integer> randPicIds;
        int qmarkpicId = R.drawable.qmark;
        public ArrayList<ImageView> imageViews = new ArrayList<ImageView>();

        public ImageAdapter(Context c) {
            mContext = c;
            randPicIds = randomizePictures();
        }

        public int getCount() {
            return mThumbIds.length*2;
        }



        private ArrayList<Integer> randomizePictures()
        {
            ArrayList<Integer> allImagesTwice = new ArrayList<Integer>();
            for(int i = 0; i < 2; i++)
            {

                for(int j = 0; j < mThumbIds.length; j++)
                {
                    allImagesTwice.add(mThumbIds[j]);
                }

            }

            ArrayList<Integer> allImagesRandom = new ArrayList<Integer>();
            Log.d("PlayActivity.java", "allImagesTwice.size(): "+allImagesTwice.size());

            for(int i = 0; i < allImagesTwice.size();)
            {
                int randInd = (int) (Math.random()*(allImagesTwice.size()-i));
                Log.d("TEST", ""+allImagesTwice.size());
                allImagesRandom.add(allImagesTwice.remove(randInd));
            }

            Log.d("PlayActivity.java", "allImagesRandom.size(): "+allImagesRandom.size());
            return allImagesRandom;
        }

        public Object getItem(int position) {
            return null;
        }

        public ImageView getImageView(int position)
        {
            return imageViews.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(getImageSizes(), getImageSizes()));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            }
            else
            {
                imageView = (ImageView) convertView;
            }

            //imageView.setImageResource(randPicIds.get(position));
            imageView.setImageResource(qmarkpicId);
            if(position == imageViews.size()) {
                Log.d("PlayActivity:", "generate imViews: " + position);
                imageViews.add(imageView);
            }
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.aladdin,
                R.drawable.goofey,
                R.drawable.kalleanka,
                R.drawable.lady,
                R.drawable.mickey,
                R.drawable.nallepuh,
                R.drawable.pluto,
                R.drawable.simba,
                R.drawable.snowwhite,
                R.drawable.tiger
        };




    }
}

