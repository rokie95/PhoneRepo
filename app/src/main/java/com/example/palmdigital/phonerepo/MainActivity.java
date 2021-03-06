package com.example.palmdigital.phonerepo;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //////////// DECLARE VARIABLES /////////////////////
    String phoneFilename = "phonerepo";
    String representsNothing = ".";
    String representEndInData = "&#&";
    String representsStartInData = "%#%";
    String[] data;
    String[][] profileData = new String[1000][100];

    File dataPath = new File(Environment.getExternalStorageDirectory() + "/phonerepo/");
    File dataFile = new File(dataPath, "data.txt");

    EditText[] inputFields = new EditText[5];
    EditText addAttInputButton;
    EditText editAttInput;

    Button saveProfile;
    Button[] editButtons = new Button[3];
    Button createProfile;
    Button deleteProfile;
    Button settingsButton;
    Button editBackButton;
    Button editNextButton;
    Button editPrevButton;
    Button newAttribute;
    Button editAttribute;
    Button delAttribute;

    TextView[] editButton1tvs = new TextView[4];
    TextView[] editButton2tvs = new TextView[4];
    TextView[] editButton3tvs = new TextView[4];
    TextView listtv;
    TextView border1;
    TextView border2;
    TextView editInfo;

    boolean settingsOpen = false;
    boolean deleteConformation = false;
    boolean bowTied = false;
    boolean addConformation = false;
    boolean editConformation = false;
    boolean fixDataOnMenu = true;

    int curEditPage = 0;
    int editingProfile = 0;
    int editPage = 0;
    int fontSize = 18;

    //////////////// SETS UP SOME FILE STUFF ////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting stuff ready

        setup();
        String[] testArray = new String[2];
        testArray[0] = "hello";
        testArray[1] = "world";
        setupComplete();

        border2.setVisibility(View.VISIBLE);

        displayList(loadTextFile(dataFile));


    }

    public void setup() {

        String folderName = "phonerepo";
        String dataFileName = "data.txt";

        File file = new File(Environment.getExternalStorageDirectory(),
                folderName);
        if (!file.exists()) {
            file.mkdirs();
        }

        File dataFile = new File(Environment.getExternalStorageDirectory() + "/phonerepo/",
                dataFileName);
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        initializeVariables();

    }

    public void setupComplete() {
        hideEverything();
        createProfile.setVisibility(View.VISIBLE);
        deleteProfile.setVisibility(View.VISIBLE);
        TextView tv = findViewById(R.id.listTV);
        tv.setVisibility(View.VISIBLE);
        changeEditVisibility(false);
        hideViewsInLayout(R.id.attribute_editor);
        //Log.d("asd", profileData.length + "");
        updateProfileListWData();
        displayList(loadTextFile(dataFile));
        updateFont();
    }

    public void editProfile(View v) {
        hideEverything();
        settingsButton.setVisibility(View.GONE);
        listtv.setVisibility(View.GONE);
        changeEditVisibility(true);
        border2.setVisibility(View.VISIBLE);
        editPrevButton.setVisibility(View.VISIBLE);
        editNextButton.setVisibility(View.VISIBLE);

        updateProfileListWData();

        updateEditButtons();
    }

    public void updateEditButtons() {

        //This sets the 3 buttons and 15 TextViews to profiles in profileData[][]

        listtv.setVisibility(View.VISIBLE);
        int viewingPage = curEditPage+3;
        viewingPage = viewingPage/3;
        int spaces = 3;
        String insSpace = "";

        for(int i = 0; i < spaces; i++) {
            insSpace=insSpace+" ";
        }

        profileData[0+curEditPage] = fixListNulls(profileData[0+curEditPage]);
        profileData[1+curEditPage] = fixListNulls(profileData[1+curEditPage]);
        profileData[2+curEditPage] = fixListNulls(profileData[2+curEditPage]);

        listtv.setText(" Viewing Page: " + viewingPage);
        for(int i = 0; i < editButtons.length; i++) {
            profileData[i+curEditPage] = fixListNulls(profileData[i+curEditPage]);
            editButtons[i].setText(profileData[i+curEditPage][0]);
        }
        for(int i = 0; i < editButton1tvs.length; i++) {
            editButton1tvs[i].setText(insSpace + profileData[0+curEditPage][i+1]);
            editButton2tvs[i].setText(insSpace + profileData[1+curEditPage][i+1]);
            editButton3tvs[i].setText(insSpace + profileData[2+curEditPage][i+1]);
        }
    }

    public void editButtonPressed() {
        showViewsInLayout(R.id.attribute_editor);
    }

    public void editButton1Pressed(View v) {
        if(!profileData[curEditPage*3][0].equals(".")) {
            editButtonPressed();
            attributeEditor(1);
        }
    }

    public void editButton2Pressed(View v) {
        if(!profileData[curEditPage*3+1][0].equals(".")) {
            editButtonPressed();
            attributeEditor(2);
        }
    }

    public void editButton3Pressed(View v) {
        if(!profileData[curEditPage*3+2][0].equals(".")) {
            editButtonPressed();
            attributeEditor(3);
        }
    }
    //////////////////////////// ATTRIBUTE EDITOR /////////////////////////
    public void attributeEditor(int button) {
        int buttonDataNum = button - 1;
        int pageToEdit = curEditPage*3;

        editInfo.setVisibility(View.GONE);
        deleteConformation=false;
        editingProfile = pageToEdit+buttonDataNum;

        addAttInputButton.setVisibility(View.GONE);
        editAttInput.setVisibility(View.GONE);

        Log.d("asd", profileData[pageToEdit+buttonDataNum][0]);
        hideViewsInLayout(R.id.edit_menu);
        hideKeyboard();
        String[] list = profileData[pageToEdit+buttonDataNum];
        String toTV = "";

        for(int i = 0; i < list.length; i++) {

            if(list[i] == null) {list[i] = representsNothing;}

            if(list[i] == representsNothing) {} else {

                toTV = toTV +" [" + i + "] " +list[i] + "\n";

            }

        }
        listtv.setText(toTV);

    }

    public void newAttribute(View view) {
        editAttribute.setVisibility(View.GONE);
        delAttribute.setVisibility(View.GONE);
        addAttInputButton.setVisibility(View.VISIBLE);
        addAttInputButton.setHint("name of new attribute");

        if(!addConformation) {
            newAttribute.setText("Add Attribute");
            addConformation = true;
            addAttInputButton.setText("");
            border2.setVisibility(View.INVISIBLE);
        } else {
            Log.d("asd", editingProfile +"");
            addAttribute(editingProfile, addAttInputButton.getText().toString());
            addConformation = false;
            editBack();
            border2.setVisibility(View.VISIBLE);
        }
    }

    public void addAttribute(int profileNum, String toAdd) {
        int count = 0;
        for(int i = 0; i < profileData[profileNum].length; i++) {
            if(!profileData[profileNum][i].equals(representsNothing)) {
                count++;
            }
        }

        profileData[profileNum][count+1] = toAdd;
        updateDataListWProfile();
    }

    public void editAttribute(View view) {
        delAttribute.setVisibility(View.GONE);
        newAttribute.setVisibility(View.GONE);
        int selectedValue = 0;

        editAttInput.setVisibility(View.VISIBLE);
        addAttInputButton.setHint("replace value to");
        addAttInputButton.setVisibility(View.VISIBLE);

        if(editConformation) {

           String userInput = addAttInputButton.getText().toString();
           int userProfileIn = -1;

           if(!editAttInput.getText().toString().equals("")) {
               userProfileIn = Integer.parseInt(editAttInput.getText().toString());
           }

           if(userProfileIn == -1) {
               editBack();
               editConformation = false;
           }

           if(userInput.equals(representsNothing)) {
               delSingleAtt(editingProfile, userProfileIn);
                updateDataListWProfile();
           } else {
               profileData[editingProfile][userProfileIn] = userInput;
               updateDataListWProfile();
           }

           editInfo.setVisibility(View.GONE);

           editConformation = false;

           Log.d("asd", "userInput: " + userInput + "  userProfileInput: " + userProfileIn);
           editInfo.setVisibility(View.VISIBLE);
           editBack();

        } else {

            if (!editConformation) {
                editConformation=true;
                addAttInputButton.setText("");
                editAttInput.setText("");
            }
        }
    }

    public void deleteAttribute(View view) {
        if(deleteConformation == false) {
            editAttribute.setVisibility(View.GONE);
            newAttribute.setVisibility(View.GONE);
            listtv.setText("Are you sure?");
            deleteConformation = true;
        } else {
            Log.d("asd", "deleted");
            editBack();
            removeAttribute(editingProfile);
        }
    }

    public void removeAttribute(int profileNum) {
        profileData[profileNum][0]=representsNothing;
        //Log.d("asd", profileNum+"");
        updateDataListWProfile();
        displayList(loadTextFile(dataFile));
    }

    public void prevEdit(View view) {
        if(curEditPage == 0) {} else {
            curEditPage -= 3;
        }
        updateEditButtons();
    }

    public void nextEdit(View view) {
        curEditPage+=3;
        updateEditButtons();

    }

    /////////////// MAIN MENU BUTTON METHODS ///////////////////////

    public void editBack() {
        hideEverything();
        findViewById(R.id.scroll).setScrollY(0);
        hideViewsInLayout(R.id.attribute_editor);
        createProfile.setVisibility(View.VISIBLE);
        deleteProfile.setVisibility(View.VISIBLE);
        listtv.setVisibility(View.GONE);
        settingsButton.setVisibility(View.VISIBLE);

        changeEditVisibility(false);

        editPrevButton.setVisibility(View.GONE);
        editNextButton.setVisibility(View.GONE);

        displayList(loadTextFile(dataFile));

        editPage=0;
        if(!editAttribute.getText().toString().equals("edit attribute")) {
            editAttribute.setText("edit attribute");
        }
    }

    public void creProfile(View v) {
        hideEverything();

        listtv.setVisibility(View.GONE);
        Button settings = findViewById(R.id.settingsButton);
        settings.setVisibility(View.GONE);

        for(int i = 0; i < inputFields.length; i++) {
            inputFields[i].setVisibility(View.VISIBLE);
        }
        saveProfile.setVisibility(View.VISIBLE);

    }

    public void saveProfile(View v) {
        hideKeyboard();
        String[] oldData = data;
        String[] newData = new String[inputFields.length];

        Button settings = findViewById(R.id.settingsButton);
        settings.setVisibility(View.VISIBLE);

        for(int i = 0; i < inputFields.length; i++) {
            newData[i] = inputFields[i].getText().toString();
            if (newData[i].matches("")) {
                newData[i] = null;
            }
        }

        hideEverything();
        createProfile.setVisibility(View.VISIBLE);
        deleteProfile.setVisibility(View.VISIBLE);

        inputFields[0].setText("");
        inputFields[1].setText("");
        inputFields[2].setText("");
        inputFields[3].setText("");
        inputFields[4].setText("");

        String[] newDataWSpc = new String[newData.length+2];
        for(int i = 0; i < newData.length; i++) {
            newDataWSpc[i+1] = newData[i];
        }
        newDataWSpc[0] = representsStartInData;
        newDataWSpc[newData.length+1] = representEndInData;

        String[] combine = new String[loadTextFile(dataFile).length+newDataWSpc.length];
        String[] dataLoad = loadTextFile(dataFile);

        for(int i = 0; i < dataLoad.length; i++) {
            combine[i] = dataLoad[i];
        }
        for(int i = 0; i < newDataWSpc.length; i++) {
            combine[dataLoad.length+i] = newDataWSpc[i];
        }

        writeToData(combine);

        dataLoad = loadTextFile(dataFile);
        displayList(dataLoad);
    }

    public void settings(View v) {
        settingsOpen = settingsOpen == false;

        if(settingsOpen == true) {
            hideEverything();
            Button clearSettings = findViewById(R.id.clearDataButton);
            clearSettings.setVisibility(View.VISIBLE);
            showViewsInLayout(R.id.fontChanger);
            updateFont();
            findViewById(R.id.fontTV).setVisibility(View.VISIBLE);
        } else {
            Button clearSettings = findViewById(R.id.clearDataButton);
            clearSettings.setVisibility(View.GONE);
            editBack();
            hideViewsInLayout(R.id.fontChanger);
            findViewById(R.id.fontTV).setVisibility(View.GONE);
        }

    }

    public void fontSizeMinus(View v) {
        fontSize--;
        updateFont();
    }

    public void fontSizePlus(View v) {
        fontSize++;
        updateFont();
    }

    public void updateFont() {
        listtv.setTextSize(fontSize);
        TextView etv = (TextView) findViewById(R.id.textFont);
        etv.setText(fontSize+"");
    }

    public void clearData(View v) {
        String[] restore = new String[2];
        restore[0] = "hello";
        restore[1] = "world";
        writeToData(restore);
        hideEverything();
        Button clearSettings = findViewById(R.id.clearDataButton);
        clearSettings.setVisibility(View.GONE);
        createProfile.setVisibility(View.VISIBLE);
        deleteProfile.setVisibility(View.VISIBLE);
        displayList(loadTextFile(dataFile));
    }

    //////////// GENERAL USE METHODS ////////////////

    public void updateProfileListWData() {
        String[] dataLoad = loadTextFile(dataFile);
        String[] blankList = new String[100];
        blankList = fixListNulls(blankList);

        for(int i = 0; i < dataLoad.length-2; i++) {
            dataLoad[i+1] = dataLoad[i+2];
            if(dataLoad[i] == null || dataLoad[i] == "" || dataLoad[i] == " ") {
                dataLoad[i] = representsNothing;
            }
        }
        dataLoad[0] = representEndInData;
        dataLoad[dataLoad.length-1] = representsNothing;
        dataLoad[dataLoad.length-2] = representsNothing;

        int count = 0;
        int saves = -1;
        String[] tempDataLoad = new String[dataLoad.length];
        boolean reading = false;

        dataLoad = moveDownBy(dataLoad, 1);

        //print2DList(profileDataFiles);

        for(int i = 0; i < dataLoad.length; i++) {
            if(dataLoad[i].equals(representEndInData)) {
                reading = false;
                saves++;
                //Log.d("asd", "next save at " + saves);
                //printList(tempDataLoad);
                tempDataLoad = moveDownBy(tempDataLoad, 1);
                saveListTo(tempDataLoad, profileData[saves]);
                count = 0;
            }

            if(dataLoad[i].equals(representsStartInData)) {
                reading = true;
                //Log.d("asd", "new start");
                for(int c = 0; c < tempDataLoad.length; c++) {
                    tempDataLoad[c] = representsNothing;
                }
            }

            if(reading == true) {
                tempDataLoad[count] = dataLoad[i];
                count++;
            }
        }
    }

    public void updateDataListWProfile() {

        String output = "hello\nworld\n";

        for(int i = 0; i < profileData.length; i++) {
            String[] blankList = new String[profileData[i].length];
            blankList = fixListNulls(blankList);

            profileData[i] = fixListNulls(profileData[i]);

            if(!profileData[i][0].equals(representsNothing)) {
                String[] savePart = new String[profileData[i].length];
                String saveString = "";

                for(int j = 0; j < savePart.length; j++) {
                    if(!profileData[i][j].equals(representsNothing)) {
                        saveString+=profileData[i][j] + '\n';

                    }
                }

                saveString = representsStartInData + '\n' + saveString+ representEndInData;

                output += saveString;
            }
        }

        writeToDataSingleString(output);
        displayList(loadTextFile(dataFile));
    }

    public String[] loadTextFile(File inFile) {
        FileInputStream fis;
        String[] output = new String[1000000];

        FileInputStream is;
        BufferedReader reader;
        final File textFile = new File(String.valueOf(inFile));
        String[] textFileStr = new String[1000];


        if (textFile.exists()) {
            try {
                is = new FileInputStream(textFile);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                int count = 0;

                while ((line = reader.readLine()) != null) {
                    textFileStr[count] = line;
                    count++;
                }

                return textFileStr;


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void displayList(String[] list) {
        hideKeyboard();
        String toTV = "";

        if(fixDataOnMenu) {
            String[] fixedData = loadTextFile(dataFile);
            fixedData = fixListNulls(fixedData);
            int profileCounter = 0;

            for(int i = 0; i < fixedData.length; i++) {
                if(fixedData[i].equals(representsStartInData)) {
                    profileCounter++;
                    fixedData[i] = profileCounter +" : " +fixedData[i+1] + " {";
                } else if(fixedData[i].equals(representEndInData)) {
                    fixedData[i] = "}\n";
                } else {
                    fixedData[i] = "   " + fixedData[i];
                }
            }

            list = fixedData;
        } else {
            list = loadTextFile(dataFile);
        }

        for(int i = 2; i < list.length-2; i++) {

            if(list[i] == null) {list[i] = representsNothing;}

            if(list[i] == representsNothing) {} else {

                toTV = toTV + list[i] + "\n";

            }

        }

        listtv.setVisibility(View.VISIBLE);
        listtv.setText(toTV);

    }

    public void delSingleAtt(int profileTo, int selected) {

        printList(profileData[profileTo]);

        for(int i = selected; i < profileData[0].length - selected; i++) {
            profileData[profileTo][i] = profileData[profileTo][i+1];
        }

    }

    public void onBackPressed() {
        editBack();
    }

    public void hideViewsInLayout(int intid) {
        LinearLayout view = findViewById(intid);
        for (int i = 0; i < view.getChildCount(); i++) {
            View child = view.getChildAt(i);
            child.setVisibility(View.GONE);
        }
    }

    public void showViewsInLayout(int intid) {
        LinearLayout view = findViewById(intid);
        for (int i = 0; i < view.getChildCount(); i++) {
            View child = view.getChildAt(i);
            child.setVisibility(View.VISIBLE);
        }
    }

    public void saveListTo(String[] from, String[] to) {
        for(int i = 0; i < to.length; i++) {
            to[i] = from[i];
        }
    }

    public String[] moveDownBy(String[] list, int downBy) {
        for(int i = 0; i < list.length - downBy; i++) {
            list[i] = list[i+1];
        }
        list = fixListNulls(list);
        return list;
    }

    public String[] moveUpBy(String[] list, int upBy) {
        String[] newList = new String[list.length+upBy];
        for(int i = 0; i < list.length; i++) {
            newList[i] = list[i];
        }
        for(int i = 1; i < newList.length; i++) {
            newList[i] = newList[i-1];
        }
        for(int i = 0; i < upBy; i++) {
            newList[i] = representsNothing;
        }
        newList = fixListNulls(newList);
        return newList;
    }

    public String[] fixListNulls(String[] list) {
        for(int i = 0; i < list.length; i++) {
            if(list[i] == null) {
                list[i] = representsNothing;
            }
        }
        return list;
    }

    public void print2DList(String[][] list2d) {
        int count=0;
        int countMini = 0;
        for(int i = 0; i < list2d.length; i++) {

            if(list2d[count][countMini] == representEndInData) {
                list2d[count][countMini] = representsNothing;
            }

            Log.d("asd", "[" + count + "]" + "[" + countMini + "] = "  + list2d[count][countMini]);

            if(countMini > 10) {
                count++;
                countMini=-1;
            }

            countMini++;
        }
    }

    public void printList(String[] list) {
        for(int i = 0; i < list.length; i++) {
            if(list[i] == null) {
                list[i] = representsNothing;
            }
            Log.d("asd", list[i]);
        }
    }

    public void changeEditVisibility(boolean showorhide) {
        if(showorhide == true) {
            for (int i = 0; i < 4; i++) {
                editButton3tvs[i].setVisibility(View.VISIBLE);
                editButton2tvs[i].setVisibility(View.VISIBLE);
                editButton1tvs[i].setVisibility(View.VISIBLE);
            }
            for(int i = 0; i < editButtons.length; i++) {
                editButtons[i].setVisibility(View.VISIBLE);
            }
        } else {

            editPrevButton.setVisibility(View.GONE);
            editNextButton.setVisibility(View.GONE);
            listtv.setVisibility(View.GONE);

            for (int i = 0; i < 4; i++) {
                editButton3tvs[i].setVisibility(View.GONE);
                editButton2tvs[i].setVisibility(View.GONE);
                editButton1tvs[i].setVisibility(View.GONE);
            }
            for(int i = 0; i < editButtons.length; i++) {
                editButtons[i].setVisibility(View.GONE);
            }
        }
    }

    public void hideEverything() {
        createProfile.setVisibility(View.GONE);
        deleteProfile.setVisibility(View.GONE);
        for(int i = 0 ; i < inputFields.length; i++) {
            inputFields[i].setVisibility(View.GONE);
        }
        saveProfile.setVisibility(View.GONE);
    }

    public void writeToData(String[] toWriteData) {
        try {
            FileWriter writer = new FileWriter(dataFile);

            for (int i = 0; i < toWriteData.length; i++) {
                if (toWriteData[i] == null) {
                } else {
                    writer.append(toWriteData[i] + "\n");
                }
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToDataSingleString(String toWriteData) {
        try {
            FileWriter writer = new FileWriter(dataFile);
            writer.append(toWriteData);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /////////////// INITIALIZE VARIABLES ///////////////////

    public void initializeVariables() {
        //Buttons, Textviews, and Editviews
        createProfile = findViewById(R.id.createProfile);
        deleteProfile = findViewById(R.id.deleteProfile);
        saveProfile = findViewById(R.id.saveProfile);
        border2 = findViewById(R.id.border2);
        border1 = findViewById(R.id.border1);
        editPrevButton = findViewById(R.id.prevEditPage);
        editNextButton = findViewById(R.id.nextEditPage);

        editNextButton.setVisibility(View.GONE);
        editPrevButton.setVisibility(View.GONE);

        newAttribute = findViewById(R.id.newAtt);
        editAttribute = findViewById(R.id.editAtt);
        delAttribute = findViewById(R.id.delAtt);
        addAttInputButton = findViewById(R.id.addAttInput);
        addAttInputButton.setVisibility(View.GONE);
        editAttInput = findViewById(R.id.editAttNum);
        editAttInput.setVisibility(View.GONE);
        editInfo = findViewById(R.id.editInfoViewer);
        editInfo.setText("Set the replace value to \".\" to delete the single line");
        editInfo.setVisibility(View.GONE);

        hideViewsInLayout(R.id.fontChanger);
        findViewById(R.id.fontTV).setVisibility(View.GONE);

        settingsButton =findViewById(R.id.settingsButton);
        editButtons[0] = findViewById(R.id.editButton1);
        editButtons[1] = findViewById(R.id.editButton2);
        editButtons[2] = findViewById(R.id.editButton3);

        editButton1tvs[0] = findViewById(R.id.button1tv1);
        editButton1tvs[1] = findViewById(R.id.button1tv2);
        editButton1tvs[2] = findViewById(R.id.button1tv3);
        editButton1tvs[3] = findViewById(R.id.button1tv4);

        editButton2tvs[0] = findViewById(R.id.button2tv1);
        editButton2tvs[1] = findViewById(R.id.button2tv2);
        editButton2tvs[2] = findViewById(R.id.button2tv3);
        editButton2tvs[3] = findViewById(R.id.button2tv4);

        editButton3tvs[0] = findViewById(R.id.button3tv1);
        editButton3tvs[1] = findViewById(R.id.button3tv2);
        editButton3tvs[2] = findViewById(R.id.button3tv3);
        editButton3tvs[3] = findViewById(R.id.button3tv4);

        listtv = findViewById(R.id.listTV);
        listtv.setVisibility(View.VISIBLE);

        inputFields[0] = findViewById(R.id.input1);
        inputFields[1] = findViewById(R.id.input2);
        inputFields[2] = findViewById(R.id.input3);
        inputFields[3] = findViewById(R.id.input4);
        inputFields[4] = findViewById(R.id.input5);

        inputFields[0].setVisibility(View.GONE);
        inputFields[1].setVisibility(View.GONE);
        inputFields[2].setVisibility(View.GONE);
        inputFields[3].setVisibility(View.GONE);
        inputFields[4].setVisibility(View.GONE);

        findViewById(R.id.addAttInput).setVisibility(View.GONE);
    }
}