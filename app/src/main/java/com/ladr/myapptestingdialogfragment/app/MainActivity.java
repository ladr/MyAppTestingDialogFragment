package com.ladr.myapptestingdialogfragment.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


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
            Toast.makeText(MainActivity.this, "Settings!", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ChangeYearDialogFragment extends DialogFragment {
        /*TimePickerDialog tpDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int hour, int minute ){

            }
        });*/

        /*private DateSlider.OnDateSetListener mDateSetListener = new DateSlider.OnDateSetListener() {
            public void onDateSet(DateSlider view, Calendar selectedDate) {
                MemberAddFragment.startTxt.setText(String.format("%tB %te, %tY", selectedDate, selectedDate, selectedDate));
            }
        };*/

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker dialog, int y, int m, int d) {

                }
            }, int  y, int m, int d);

            return dpDialog;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.choose_year_message)
                    .setView(dpDialog)
                    /*.setItems(list, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })*/
                    .setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Send the positive button event back
                            dialog.dismiss();
                            //switch ()
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Send the negative button event back
                            dialog.dismiss();
                        }
                    });

            return builder.create();
        }
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
                    mode.getMenuInflater().inflate(R.menu.context_menu, menu);
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
                    if (currentActionMode != null) {
                        return false;
                    }
                    // Start the CAB using the ActionMode.Callback defined above
                    currentActionMode = getActivity().startActionMode(modeCallBack);
                    v.setSelected(true);
                    return true;
                }
            });

            btnChangeYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFilterPopup(v);
                    ChangeYearDialogFragment cyd = new ChangeYearDialogFragment();
                    cyd.show(getActivity().getFragmentManager(), "ChangeYearDialogFragment");
                }
            });

            registerForContextMenu(btnChangeYear);  //The context menu should be associated to

            return rootView;
        }

        private void showFilterPopup(View v) {
            PopupMenu popup = new PopupMenu(getActivity(), v);
            //Inflate the menu from xml
            popup.getMenuInflater().inflate(R.menu.context_menu, popup.getMenu());
            //Setup menu item selection
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_edit:
                            Toast.makeText(getActivity(), "Edit!", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.menu_delete:
                            Toast.makeText(getActivity(), "Delete!", Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
        }

    }
}
