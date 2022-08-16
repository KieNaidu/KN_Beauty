package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Graph extends AppCompatActivity {

    BarChart barChart;

    //Firebase Realtime Database reference
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    User user;

    //declaring arraylist for bargraph entries and data for graph
    ArrayList<Integer> itemNoInCollection = new ArrayList<>();//Num items per category
    ArrayList<String> collectionNameList = new ArrayList<>();//collection name for labels
    ArrayList<Integer> itemPerCat = new ArrayList<>();//counter for items
    ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();//bar entries

    Item itemAll;//hold item from firebase
    Integer sum;
    ImageView img_returnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---------------------------------------Code Attribution------------------------------------------------
        //Author:geeksforgeeks
        //Uses:Hides the action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); //Hide the action bar
        //Link:https://www.geeksforgeeks.org/different-ways-to-hide-action-bar-in-android-with-examples/#:~:text=If%20you%20want%20to%20hide,AppCompat
        //-----------------------------------------------End------------------------------------------------------
        setContentView(R.layout.activity_graph);

        //find bar graph component
        barChart = (BarChart) findViewById(R.id.bargraph);

        //return back to the home screen
        img_returnHome = findViewById(R.id.img_ReturnHomeFromGraph);
        img_returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListUtils.collectionList.clear();
                Intent returnHome = new Intent(Graph.this,Home.class);
                startActivity(returnHome);
            }
        });

        //clear the arraylists
        itemNoInCollection.clear();
        ListUtils.allItems.clear();
        itemPerCat.clear();
        user = ListUtils.usersList.get(0);

        //---------------------------------------Code Attribution------------------------------------------------
        //Author:Sarina Till
        //Uses:Read data from firebase realtime database

        // reference for data in firebase
        myRef = database.getReference().child("Users").child(user.getUserID()).child("Items");

        //get data from firebase whilst using reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                itemAll = new Item();

                //pull data from firebase realtime database
                for (DataSnapshot itemFirebase : snapshot.getChildren()) {
                    //store the snapshot in item instance
                    itemAll = itemFirebase.getValue(Item.class);

                    ListUtils.allItems.add(itemAll);
                }

        //Link:https://www.youtube.com/watch?v=Ydn5cXn1j-0&list=PL480DYS-b_kdor_f0IFgS7iiEsOwxdx6w&index=26
        //-----------------------------------------------End------------------------------------------------------

                //calculate the total items for each category
                for (Collection collectInfo:ListUtils.collectionList) {
                    itemPerCat.clear();
                    for (Item itemCat:ListUtils.allItems) {
                       if (itemCat.getCollectionID().equals(collectInfo.getCollectionId())){
                           itemPerCat.add(1);
                       }
                    }
                    sum =0;
                    //calculate the sum of items in each category
                    for (Integer itemNo:itemPerCat) {
                        sum = sum + itemNo;
                    }
                    //check if the have a goal and stores the sum
                    if (collectInfo.getGoal() == true){
                        itemNoInCollection.add(sum);
                    }else{
                        itemNoInCollection.add(0);
                    }
                }

                //calculate the percentage of items the user has in each category
                float percent = 0;
                collectionNameList.clear();

                    for (int k = 0 ; k < ListUtils.collectionList.size();k++){
                        if (ListUtils.collectionList.get(k).getGoal() == true){
                            float value = (float) itemNoInCollection.get(k)/ListUtils.collectionList.get(k).getCollectionGoalItem();
                            percent = (float) value*100;
                            collectionNameList.add(ListUtils.collectionList.get(k).getCollectionName());

                            //populate the bar entries arraylist with bar entry for each
                            barEntries.add(new BarEntry(k,percent));
                        }
                    }
                //---------------------------------------Code Attribution------------------------------------------------
                //Author:Android Hands,weeklycoding,PhilJay,CodingWithMitch
                //Uses:Display the data in the bar graph

                //create dataset, and the data
                BarDataSet barDataSet = new BarDataSet(barEntries,"Category");
                BarData theData = new BarData(barDataSet);

                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:weeklycoding
                        //Uses: set the colour for bar and text, and set the text size
                        theData.setBarWidth(0.5f);
                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        barDataSet.setValueTextColor(Color.BLACK);
                        barDataSet.setValueTextSize(16f);
                        //Link:https://weeklycoding.com/mpandroidchart-documentation/setting-data/
                        //-----------------------------------------------End------------------------------------------------------


                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author: eprabhakar
                        //Uses: Remove the right axis
                        YAxis leftAxis = barChart.getAxisLeft();
                        leftAxis.setEnabled(true);
                        YAxis rightAxis = barChart.getAxisRight();
                        //rightAxis.setAxisMinimum(0f);
                        rightAxis.setEnabled(false);
                        //Link:https://github.com/PhilJay/MPAndroidChart/issues/2402
                        //-----------------------------------------------End------------------------------------------------------


                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author: tabine
                        //Uses: enable pinch zoom
                        barChart.setPinchZoom(true);
                        //Link:https://www.tabnine.com/code/java/methods/com.github.mikephil.charting.components.YAxis/setValueFormatter
                        //-----------------------------------------------End------------------------------------------------------


                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:Amir Raza
                        //Uses: Display the labels for the bars
                        XAxis xAxis = barChart.getXAxis();

                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:user1626183
                        //Uses:set the spacemax
                        xAxis.setSpaceMax(0.2f);
                        //Link:https://stackoverflow.com/questions/48124505/bar-width-reduce-after-loading-data-in-bar-chart-mpandroidchart
                        //-----------------------------------------------End------------------------------------------------------


                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:Muhammad Saad Rafique
                        //Uses:enable the axis label
                        xAxis.setCenterAxisLabels(true);
                        //Link:https://stackoverflow.com/questions/53130042/labels-and-bars-are-not-aligned-in-mpandroidchart-bar-chart
                        //-----------------------------------------------End------------------------------------------------------


                        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author: Vishal Chhodwani
                        //Uses:count of label for the bar graph
                        barChart.getXAxis().setLabelCount(collectionNameList.size());
                        //Link:https://stackoverflow.com/questions/41299454/mpandroidchart-only-alternate-labels-are-shown-in-x-axis-when-more-entries-com
                        //-----------------------------------------------End------------------------------------------------------


                        //---------------------------------------Code Attribution------------------------------------------------
                        //Author:sachinrana135
                        //Uses:Remove the duplication of the labels
                        xAxis.setGranularity(1f);
                        //Link:https://github.com/PhilJay/MPAndroidChart/issues/2437
                        //-----------------------------------------------End------------------------------------------------------


                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(collectionNameList));
                //Link:https://stackoverflow.com/questions/47637653/how-to-set-x-axis-labels-in-mp-android-chart-bar-graph
                //-----------------------------------------------End------------------------------------------------------

                //---------------------------------------Code Attribution------------------------------------------------
                //Author:Stack Overflow
                //Uses:Manage the grid lines

                xAxis.setDrawAxisLine(true);
                xAxis.setDrawGridLines(false);

                YAxis yAxisLeft = barChart.getAxisLeft();
                yAxisLeft.setDrawAxisLine(true);
                yAxisLeft.setDrawGridLines(true);


                YAxis yAxisRight = barChart.getAxisRight();
                yAxisRight.setDrawAxisLine(false);
                yAxisRight.setDrawGridLines(false);
                //Link:https://stackoverflow.com/questions/39505362/how-to-remove-grid-lines-behind-the-bars-in-horizontalbarchart
                //-----------------------------------------------End------------------------------------------------------

                //---------------------------------------Code Attribution------------------------------------------------
                //Author:Learn to droid
                //Uses:Remove the description
                barChart.getDescription().setEnabled(false);
                //Link:https://learntodroid.com/how-to-display-a-bar-chart-in-your-android-app/
                //-----------------------------------------------End------------------------------------------------------

                //set the data in the barchart
                barChart.setData(theData);
                barChart.setFitBars(true); // make the x-axis fit exactly all bars
                barChart.invalidate(); // refresh

                barChart.setTouchEnabled(true);
                barChart.setDragEnabled(true);
                barChart.setScaleEnabled(true);

                //Links:
                //https://www.youtube.com/watch?v=sXo2SkX7rGk
                //Doc:https://weeklycoding.com/mpandroidchart/
                //Part1:https://www.youtube.com/watch?v=pi1tq-bp7uA
                //Part2:https://www.youtube.com/watch?v=H6QxMBI2QH4
                //https://github.com/PhilJay/MPAndroidChart
                //https://weeklycoding.com/mpandroidchart-documentation/
                //-----------------------------------------------End------------------------------------------------------

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

