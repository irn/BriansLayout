package com.example.brian.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
public class ContactAdapter extends CursorAdapter {

    private Context mContext;

    private int mResourcesId;

    private int itemWidth;

    private Drawable defaultItemDrawable;

    public ContactAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        defaultItemDrawable = mContext.getResources().getDrawable(R.drawable.contact_drawable);
        itemWidth = (int) (defaultItemDrawable.getMinimumWidth()* 1.3);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        //To change body of implemented methods use File | Settings | File Templates.
        TextView contactView = new TextView(context);

        Drawable mDrawable = mContext.getResources().getDrawable(R.drawable.contact_drawable);
        contactView.setLayoutParams(new GridView.LayoutParams(itemWidth, GridView.LayoutParams.WRAP_CONTENT));
        contactView.setText(cursor.getString(0));
        contactView.setGravity(Gravity.CENTER_HORIZONTAL);
        contactView.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);

        return contactView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //To change body of implemented methods use File | Settings | File Templates.
        TextView contactView;
//        LayerDrawable mDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.contact_drawable);
        if (view == null){
            contactView = new TextView(context);

            contactView.setLayoutParams(new GridView.LayoutParams(itemWidth, GridView.LayoutParams.WRAP_CONTENT));
            contactView.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            contactView = (TextView) view;
        }
        long photoId = cursor.getLong(1);
        long contactId = cursor.getLong(2);
        if (photoId != 0){
            Drawable drawable = openPhoto(context, photoId, contactId);
            if (drawable != null){
                LayerDrawable mDrawable = new LayerDrawable(new Drawable[]{drawable, context.getResources().getDrawable(R.drawable.rings)});
//                mDrawable.setId(0, 777);
//                mDrawable.setDrawableByLayerId(777, drawable);
                contactView.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);
            }

//            contactView.setLayoutParams(new GridView.LayoutParams((int) (mDrawable.getMinimumWidth()* 1.3), GridView.LayoutParams.WRAP_CONTENT));
//            contactView.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);
        } else {
            contactView.setCompoundDrawablesWithIntrinsicBounds(null, (LayerDrawable) mContext.getResources().getDrawable(R.drawable.contact_drawable), null, null);
        }

        contactView.setText(cursor.getString(0));

    }

    private Drawable openPhoto(Context context, long photoId, long contactId){

        Drawable drawable = null;
        if (photoId != 0){
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

            try {

//                InputStream is = context.getContentResolver().openInputStream(uri);
                BitmapFactory.Options options= new BitmapFactory.Options();
                InputStream is = null;
                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                    is = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri, true);
                else
                    is = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);
//                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
//                if (bitmap.getWidth() > 0 || bitmap.getHeight() > 100){
//                    bitmap = Bitmap.createScaledBitmap(bitmap,100, 100, true);
//                    drawable = new BitmapDrawable(bitmap);
//                }
                drawable = Drawable.createFromStream(is, "photo");
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

//        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), photoId, MediaStore.Images.Thumbnails.MICRO_KIND, null);
//        if (bitmap != null)
//            drawable = new BitmapDrawable(bitmap);

        return  drawable;
    }

}
