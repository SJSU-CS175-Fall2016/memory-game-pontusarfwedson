package com.example.pontusarfwedson.memorygame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity {

    private GridView cardsLayout;
    private ImageAdapter imgAd;
    private TextView pointsTxt, tryTxt;
    private Resources res;
    private boolean clickedOne = false;
    private boolean allowedToClick = true;
    public ArrayList<Integer> pictureIds;
    public int pictureVisibility[];
    private int lastPicId;
    private int lastPicPos;
    private int points = 0, tries = 0;
    private int currPos;
    Context mContext;

    //TAGS AND IDENTIFIERS
    private final String SHARED_PREF_ID_TAG = "id";
    private final String SHARED_PREF_VIS_TAG = "vis";
    private final String SHARED_PREF_POINTS_TAG = "pts";
    private final String SHARED_PREF_TRIES_TAG = "tries";

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initializeRes();
        Log.d("PlayActivity:", "onCreate!");



            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            if (sharedPref.getInt(SHARED_PREF_VIS_TAG + 0, -1) == -1) {
                Log.d("PlayActivity: ", "NO shared preferences");
                newGameInit();

            } else {
                Log.d("PlayActivity: ", "FOUND shared preferences");
                if(savedInstanceState == null) {
                    Log.d("PlayActivity: ", "No savedInstanceState: SHOW DIALOG");
                    handleSavedGame();
                }

                for (int i = 0; i < pictureVisibility.length; i++) {
                    pictureIds.add(i, sharedPref.getInt(SHARED_PREF_ID_TAG + i, R.drawable.aladdin));
                    pictureVisibility[i] = sharedPref.getInt(SHARED_PREF_VIS_TAG + i, 1);
                }
                points = sharedPref.getInt(SHARED_PREF_POINTS_TAG, 0);
                tries = sharedPref.getInt(SHARED_PREF_TRIES_TAG, 0);
            }



        initializeAll();


    }



    private int getImageSizes() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width / 5;

    }

    /**************************************************************************
     ********************* Overridden android methods
     **************************************************************************/

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("PlayActivity:", "onPause!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("PlayActivity:", "onResume!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("PlayActivity:", "onStart!");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("PlayActivity:", "onStop!");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveCurrentData();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("PlayActivity: ", "protected onSaveInstanceState");
        outState.putIntegerArrayList(SHARED_PREF_ID_TAG, pictureIds);
        outState.putIntArray(SHARED_PREF_VIS_TAG, pictureVisibility);
        outState.putInt(SHARED_PREF_POINTS_TAG, points);
        outState.putInt(SHARED_PREF_TRIES_TAG, tries);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("PlayActivity: ", "onRestoreInstanceState");
        pictureIds = savedInstanceState.getIntegerArrayList(SHARED_PREF_ID_TAG);
        pictureVisibility = savedInstanceState.getIntArray(SHARED_PREF_VIS_TAG);
        points = savedInstanceState.getInt(SHARED_PREF_POINTS_TAG);
        tries = savedInstanceState.getInt(SHARED_PREF_TRIES_TAG);
        initializeAll();
    }


    /****************************************************************************
     ****************** METHODS FOR STORING DATA AND INITIALIZING
     ***************************************************************************/


    /**
     * Method to save the current data into shared preferences.
     */
    private void saveCurrentData()
    {
        Log.d("PlayActivity", "SAVING STATE to shared pref");
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for(int i = 0; i < pictureVisibility.length; i++)
        {
            editor.putInt(SHARED_PREF_VIS_TAG+i, pictureVisibility[i]);
            editor.putInt(SHARED_PREF_ID_TAG +i, pictureIds.get(i));

        }
        editor.putInt(SHARED_PREF_POINTS_TAG, points);
        editor.putInt(SHARED_PREF_TRIES_TAG, tries);
        editor.commit();
    }

    /**
     * Method to clear the shared preferences of all its data.
     */
    private void clearSharedPref()
    {
        Log.d("PlayActivity: ", "CLEARING shared pref!");
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }


    /**
     * Method invoked when initializing new game. Will reset and re-randomize all images.
     */
    private void newGameInit()
    {
        clearSharedPref();
        pictureIds = randomizePictures();
        Arrays.fill(pictureVisibility, 1);
        points = 0;
        tries = 0;
    }



    /**
     * Initializing the used resources.
     */
    private void initializeRes()
    {
        cardsLayout = (GridView) findViewById(R.id.gridCardsLayout);
        pointsTxt = (TextView) findViewById(R.id.txtPoints);
        tryTxt = (TextView) findViewById(R.id.txtTry);
        res = getResources();
        pictureVisibility = new int[20];
        pictureIds = new ArrayList<Integer>();
    }

    /**
     * Initializing everything but the resources. Currently uses handler for a delayed post, not desirable.
     */
    private void initializeAll()
    {
        pointsTxt.setText(res.getString(R.string.points_str, points));
        tryTxt.setText(res.getString(R.string.try_str, tries));
        mContext = this;

        imgAd = new ImageAdapter(this, pictureIds);
        cardsLayout.setAdapter(imgAd);
        cardsLayout.setClickable(true);
        setCardClickListener();

        //Using handler to invoke small delay before setting images to invisible. Otherwise imageViews have not enough
        //time to initialize. Bad implementation but not sure how to fix this without too much of a workaround.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setDoneToInvisible();
            }
        }, 10);
    }


    /**
     * Setting the tiles to invisible if already chosen. Controlled by the instance variable pictureInvisibility.
     */
    private void setDoneToInvisible()
    {
        Log.d("PlayActivity:", "setDoneToInvisible");
        for(int i = 0; i < pictureVisibility.length; i++){
            if(pictureVisibility[i] == 0) {
                if(i < imgAd.imageViewLen()) {
                     Log.d("PlayActivity: " , "setting " + i + " to invisible");
                    imgAd.getImageView(i).setVisibility(View.INVISIBLE);
                }else{
                    Log.d("PlayActivity: ", "not setting to invisible, not in view: " +i);
                }
            }
        }
    }


    /**
     * Method that randomizes the pictures with two of every one defined in mThumdIds.
     * @return ArrayList containing all the new randomized pictures to be used when setting up views.
     */
    private ArrayList<Integer> randomizePictures() {
        ArrayList<Integer> allImagesTwice = new ArrayList<Integer>();
        for (int i = 0; i < 2; i++) {

            for (int j = 0; j < mThumbIds.length; j++) {
                allImagesTwice.add(mThumbIds[j]);
            }

        }

        ArrayList<Integer> allImagesRandom = new ArrayList<Integer>();

        for (int i = 0; i < allImagesTwice.size(); ) {
            int randInd = (int) (Math.random() * (allImagesTwice.size() - i));
            allImagesRandom.add(allImagesTwice.remove(randInd));
        }

        return allImagesRandom;
    }




    /***********************************************************************************
    ********************* Method handling clicking on tiles
     ***********************************************************************************/

    /**
     * Setting the onItemClickListener for the tiles and defining the onClick implementation.
     */
    private void setCardClickListener()
    {

        cardsLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (allowedToClick) {
                    currPos = position;
                    Log.d("PlayActivity: ", "clicked pos " + position + " with id " + id);
                    ImageView imageView = (ImageView) v;
                    imageView.setImageResource(imgAd.randPicIds.get(position));
                    imgAd.getImageView(position).setImageResource(imgAd.randPicIds.get(position));


                    if (!clickedOne) {
                        lastPicId = imgAd.randPicIds.get(position);
                        lastPicPos = position;
                        clickedOne = true;
                    } else {
                        tryTxt.setText(res.getString(R.string.try_str, ++tries));
                        clickedOne = false;
                        if (lastPicId == imgAd.randPicIds.get(position) && position != lastPicPos) {
                            clickedOne = false;
                            pointsTxt.setText(res.getString(R.string.points_str, ++points));
                           setToInvisibleDelayed();
                            if(points == imgAd.getMaxPoints())
                                handleWin();

                        } else if (lastPicId != imgAd.randPicIds.get(position)) {
                            Log.d("PlayActivity: ", "pos " + lastPicPos + " and pos " + position + " to be ?");
                            setToQmarkDelayed();



                        } else if (position == lastPicPos) {
                            clickedOne = true; //clicked same picture twice, should not count. Keep letting user click!
                        } else {
                            Log.e("PlayActivity: ", "SHOULD NOT GET HERE, SOMETHING WRONG!");
                        }

                    }

                }
            }
        });
    }


    /**
     * Method to set two clicked tiles back to questionmark.
     */

    private void setToQmarkDelayed()
    {
        imgAd.getImageView(lastPicPos).postDelayed(setQmarkLast, 1000);
        imgAd.getImageView(currPos).postDelayed(setQmarkCurr, 1000);
        allowedToClick = false;
        imgAd.getImageView(currPos).postDelayed(setClickable, 1000);
    }

    /**
     * Method to set two clicked tiles to invisible.
     */
    private void setToInvisibleDelayed()
    {
        imgAd.getImageView(lastPicPos).postDelayed(setInviLast, 1000);
        imgAd.getImageView(currPos).postDelayed(setInviCurr, 1000);
        pictureVisibility[currPos] = 0;
        pictureVisibility[lastPicPos] = 0;
        allowedToClick = false;
        imgAd.getImageView(currPos).postDelayed(setClickable, 1000);
    }


    /***********************************************************************************
    ***************************** ALERT DIALOGS
     ***********************************************************************************/

    /**
     * Method to create, define and show the alert dialog used when finishing the game.
     */
    private void handleWin()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       // builder.setMessage(R.string.dialog_congrats_str);
        builder.setMessage(res.getString(R.string.dialog_congrats_str, (int)(1000./tries))); //If makes it in 10 tries, score is 100. The more tries, the lower score.
        builder.setPositiveButton(R.string.dialog_ok_str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                newGameInit();
                initializeAll();
            }
        });
        builder.setNegativeButton(R.string.dialog_no_str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                newGameInit();
                finish();
            }
        });

        AlertDialog dialog  = builder.create();
        dialog.show();

    }

    /**
     * Method used to create, define and show the alert dialog used when starting new game with saved data.
     */
    private void handleSavedGame()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_saved_game_str);
        builder.setPositiveButton(R.string.dialog_ok_str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //On same game, initialize (may already have been done through onCreate)
                initializeAll();
            }
        });
        builder.setNegativeButton(R.string.dialog_new_game_str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //On new game: clear ref, set values and initialize everything
                newGameInit();
                initializeAll();
            }
        });
        builder.setNeutralButton(R.string.dialog_go_back_str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //On go back: onBackPressed
                onBackPressed();
            }
        });

        AlertDialog dialog  = builder.create();
        dialog.show();

    }




    /**********************************************************************
     *******************RUNNABLES USED WITH POSTDELAYED
     **********************************************************************/


    /**
     * Runnable to set current imageview to questionmark
     */
    Runnable setQmarkCurr = new Runnable() {
        @Override
        public void run() {
            imgAd.getImageView(currPos).setImageResource(R.drawable.qmark);
        }
    };

    /**
     * Runnable to set last imageview to questionmark
     */
    Runnable setQmarkLast = new Runnable() {
        @Override
        public void run() {
            imgAd.getImageView(lastPicPos).setImageResource(R.drawable.qmark);
        }
    };

    /**
     * Runnable to set current clicked tile to invisible
     */
    Runnable setInviCurr = new Runnable() {
        @Override
        public void run() {
            imgAd.getImageView(currPos).setVisibility(View.INVISIBLE);
        }
    };

    /**
     * Runnable to set last clicked tile to invisible.
     */
    Runnable setInviLast = new Runnable() {
        @Override
        public void run() {
            imgAd.getImageView(lastPicPos).setVisibility(View.INVISIBLE);
        }
    };

    Runnable setInviSharedPref = new Runnable() {
        @Override
        public void run() {
            setDoneToInvisible();
        }
    };



    /**
     * Runnable to set gridview to clickable
     */
    Runnable setClickable = new Runnable() {
        @Override
        public void run() {
            allowedToClick = true;
        }
    };





    /***********************************************************************
     *######################################################################
     *                    IMPLEMENTED IMAGE ADAPTER CLASS
     *######################################################################
     **********************************************************************/


    /**
     * Outline of class ImageAdapter gotten from android developers page about GridView (https://developer.android.com/guide/topics/ui/layout/gridview.html)
     * Changes has been made to it to make it fit this project.
     */
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        public ArrayList<Integer> randPicIds;
        int qmarkpicId = R.drawable.qmark;
        public ArrayList<ImageView> imageViews = new ArrayList<ImageView>();

        public ImageAdapter(Context c, ArrayList<Integer> picIds) {
            mContext = c;
            randPicIds = picIds;
        }
        public int getCount() {
            return mThumbIds.length * 2;
        }

        public int getMaxPoints(){
            return mThumbIds.length;
        }

        public ArrayList<Integer> getPictureIds()
        {
            return randPicIds;
        }


        public Object getItem(int position) {
            return null;
        }

        public ImageView getImageView(int position) {
            return imageViews.get(position);
        }

        public int imageViewLen()
        {
            return imageViews.size();
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
            } else {
                imageView = (ImageView) convertView;
            }


            imageView.setImageResource(qmarkpicId);
            if (position == imageViews.size()) {
                Log.d("PlayActivity: ", "adding view: " + position);
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

