package com.example.brian;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.GridView;
import com.example.brian.adapters.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private GridView mGridView;

    private List<String> mContacts;
    private final String[] contactsProjection = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID, ContactsContract.Contacts._ID};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mGridView = (GridView) findViewById(R.id.gridView);

        mContacts = new ArrayList<String>();
        mContacts.add("Eric Sanders");
        mContacts.add("Amanda Hopenshire");
        mContacts.add("Michael Hopenshire");
        mContacts.add("Debbie Hopenshire");
        mContacts.add("Charlie Sanders");
        mContacts.add("Susan Calwright");
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.

        ContentResolver cr = getContentResolver();
        Cursor mCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, contactsProjection, null, null, null);
        if (mCursor != null && mCursor.getCount() > 0){
            ContactAdapter adapter = new ContactAdapter(this, mCursor);
            mGridView.setAdapter(adapter);
        }

    }
}
