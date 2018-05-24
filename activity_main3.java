<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.example.acer.myapplication.Main3Activity"
    android:background="#000000">


    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <ProgressBar
            android:layout_width="match_parent"
            android:layout_height="5dp"
            android:layout_alignBottom="@+id/siriButton"
            android:id="@+id/progressBar1"/>
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerHorizontal="true"
            android:layout_marginTop="20dp"
            android:text="Ask any FAQ about GRE.."
            android:textSize="22dp"
            android:id="@+id/textView1"
            android:textColor="#fff"/>
        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_below="@+id/textView1"
            android:layout_above="@id/siriButton"
            android:layout_marginTop="10dp"
            >
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_below="@+id/textView1"
            android:layout_above="@id/siriButton"
            android:layout_marginTop="10dp"
            >

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerHorizontal="true"
                android:layout_marginTop="40dp"
                android:textColor="#ffffff"
                android:text="hey whst"
                android:layout_marginLeft="10dp"
                android:layout_marginRight="10dp"
                android:textSize="23dp"
                android:id="@+id/heading"
                />
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerHorizontal="true"
                android:layout_marginTop="77dp"
                android:textColor="#ffffff"
                android:text="hey whst"
                android:layout_marginLeft="10dp"
                android:layout_marginRight="10dp"
                android:textSize="23dp"
                android:id="@+id/meaning"
                />
        </RelativeLayout>
        </ScrollView>
        <!--    <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerVertical="true"
                android:layout_centerHorizontal="true"
                android:textSize="22dp"
                android:id="@+id/textView1"
                android:textColor="#fff"
                android:text="tap the button and ask"/>-->
        <ImageView
            android:layout_width="80dp"
            android:layout_height="80dp"
            android:src="@drawable/siri"
            android:id="@+id/siriButton"

            android:layout_alignParentBottom="true"
            android:layout_centerHorizontal="true"
            android:layout_marginBottom="48dp"
            />
    </RelativeLayout>
</LinearLayout>
