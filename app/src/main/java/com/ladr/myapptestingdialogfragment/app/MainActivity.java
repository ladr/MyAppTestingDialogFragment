package com.ladr.myapptestingdialogfragment.app;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        //Tracks current contextual action mode
        protected ActionMode currentActionMode;
        //Define the callback when ActionMode is activated
        private static ActionMode.Callback modeCallBack;

        public PlaceholderFragment() {
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            //Contextual Action Mode in Fragments by LongClick on Button
            modeCallBack = new ActionMode.Callback(){
                //Called when the action mode is created; startActionMode() was called
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.setTitle("Actions");
                    mode.getMenuInflater().inflate(R.menu.main, menu);
                    return true;
                }

                //Called each time the action mode is shown
                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false; //Return false if nothing is done
                }

                //Called when the user selects a contextual menu item
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_edit:
                            Toast.makeText(getActivity(), "Editing!", Toast.LENGTH_SHORT).show();
                            mode.finish(); //Action picked, so close the contextual menu
                            return true;
                        case R.id.menu_delete:
                            //Trigger deletion here
                            mode.finish(); //Action picked, so close the contextual menu
                            return true;
                        default:
                            return false;
                    }
                }

                //Called when the user exits the action mode
                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    currentActionMode = null; //Clear current action mode
                }
            };
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //Button with context menu (long click)
            Button btnChangeYear = (Button) rootView.findViewById(R.id.btnChangeYear);
            btnChangeYear.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(currentActionMode != null) {return false; }
                    // Start the CAB using the ActionMode.Callback defined above
                    currentActionMode = getActivity().startActionMode(modeCallBack);
                    v.setSelected(true);
                    return true;
                }
            });

            return rootView;
        }
    }
}
