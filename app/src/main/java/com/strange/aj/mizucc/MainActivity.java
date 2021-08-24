package com.strange.aj.mizucc;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView songsListView;
    String[] items;
    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for(File singlefile: files){
            if(singlefile.isDirectory() && !singlefile.isHidden()){
                arrayList.addAll(findSong(singlefile));
            }
            else{
                if(singlefile.getName().endsWith(".mp3") ||singlefile.getName().endsWith(".m4a")||singlefile.getName().endsWith(".wav")||singlefile.getName().endsWith(".aac")||singlefile.getName().endsWith(".flac")){
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }
    void displaySongs()
    {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for(int i = 0;i<mySongs.size();i++){
            items[i] = mySongs.get(i).getName().replace(".mp3","").replace(".wav","").replace(".aac","").replace(".m4a","").replace(".flac","");
        }
       /* ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        songsListView.setAdapter(myAdapter);*/
        CutomAdapter adapter =  new CutomAdapter();
        songsListView.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songsListView = findViewById(R.id.songs_ListView);
        runtimePermission();
    }
    public void runtimePermission(){
    Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                displaySongs();
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
                }
            }).check();
    }
class CutomAdapter extends BaseAdapter{

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView = getLayoutInflater().inflate(R.layout.list_item,null);
        TextView songName = myView.findViewById(R.id.songName_TextView);
        songName.setSelected(true);
        songName.setText(items[i]);
        return myView;
    }
}

}