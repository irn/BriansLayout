package com.example.brian.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.example.brian.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: android
 * Date: 19.02.13
 * Time: 9:13
 * To change this template use File | Settings | File Templates.
 */
public class ContactAdapter extends CursorAdapter implements View.OnClickListener {

    private Context mContext;

    private int itemWidth;

    private Drawable defaultItemDrawable;

    public ContactAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        defaultItemDrawable = mContext.getResources().getDrawable(R.drawable.contact_drawable);
        itemWidth = (int) (defaultItemDrawable.getMinimumWidth()* 1.5);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        //To change body of implemented methods use File | Settings | File Templates.
        TextView contactView = new TextView(context);

        Drawable mDrawable = mContext.getResources().getDrawable(R.drawable.contact_drawable);
        contactView.setLayoutParams(new GridView.LayoutParams(itemWidth, itemWidth));
        contactView.setText(cursor.getString(0));
        contactView.setGravity(Gravity.CENTER | Gravity.TOP);
        contactView.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);

        return contactView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //To change body of implemented methods use File | Settings | File Templates.
        TextView contactView;
        if (view == null){
            contactView = new TextView(context);

            contactView.setLayoutParams(new GridView.LayoutParams(itemWidth, itemWidth));
            contactView.setGravity(Gravity.CENTER | Gravity.TOP);
        } else {
            contactView = (TextView) view;
        }
        long photoId = cursor.getLong(1);
        long contactId = cursor.getLong(2);
        if (photoId != 0){
            Drawable drawable = openPhoto(context, photoId, contactId);
            if (drawable != null){
                LayerDrawable mDrawable = new LayerDrawable(new Drawable[]{drawable, context.getResources().getDrawable(R.drawable.rings)});
                contactView.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);
            } else {
                contactView.setCompoundDrawablesWithIntrinsicBounds(null, (LayerDrawable) mContext.getResources().getDrawable(R.drawable.contact_drawable), null, null);
            }

        } else {
            contactView.setCompoundDrawablesWithIntrinsicBounds(null, (LayerDrawable) mContext.getResources().getDrawable(R.drawable.contact_drawable), null, null);
        }

        contactView.setTag(contactId);
        contactView.setText(cursor.getString(0));
        //TODO This listener is second way to catch click on some contact
//        contactView.setOnClickListener(this);

    }

    private Drawable openPhoto(Context context, long photoId, long contactId){

        Drawable drawable = null;
        if (photoId != 0){
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

            try {

                InputStream is = null;
                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                    is = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri, true);
                else
                    is = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);

                if (is != null){

                    BitmapFactory.Options options= new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(is, null, options);
                    if (options.outWidth > itemWidth || options.outHeight > itemWidth){
                        float minDivide = Math.min((float)itemWidth/ 1.5f / options.outWidth, (float)itemWidth / 1.5f/ options.outHeight);
                        int dstWidth = (int) (options.outWidth * minDivide);
                        int dstHeight = (int) (options.outHeight * minDivide);
                        is.reset();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        if (bitmap != null){
                            drawable = new BitmapDrawable(Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true));
                            bitmap.recycle();
                        }
                    } else {
                        is.reset();
                        drawable = Drawable.createFromStream(is, "photo");
                    }

                    is.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return  drawable;
    }

    /**
     * This is listener for perform something when user clicked on some Contact
     * @param v View which was clicked. It has Tag with contact ID
     */
    @Override
    public void onClick(View v) {
        //To change body of implemented methods use File | Settings | File Templates.
        if (v.getTag() != null && v.getTag() instanceof Long){
            Long contactId = (Long) v.getTag();
        }
    }
}
