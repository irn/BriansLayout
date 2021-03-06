package com.example.brian;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.brian.adapters.ContactAdapter;

public class StartActivity extends Activity implements AdapterView.OnItemClickListener {
    /**
     * Called when the activity is first created.
     */

    private GridView mGridView;

    private final String[] contactsProjection = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID, ContactsContract.Contacts._ID};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.

        ContentResolver cr = getContentResolver();
        //TODO this comment below gets only contacts which contain photo
//        Cursor mCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, contactsProjection, ContactsContract.Contacts.PHOTO_ID + ">?", new String[]{"0"}, null);
        Cursor mCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, contactsProjection, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        if (mCursor != null && mCursor.getCount() > 0){
            ContactAdapter adapter = new ContactAdapter(this, mCursor);
            mGridView.setAdapter(adapter);
        }

    }

    /**
     * This is first way to catch click on the contact
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long contactId = id;

    }
}
