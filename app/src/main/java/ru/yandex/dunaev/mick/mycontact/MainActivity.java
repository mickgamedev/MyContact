package ru.yandex.dunaev.mick.mycontact;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISION_CODE = 6688;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissionContact();
    }

    private void requestPermissionContact() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            PermisionContactGranted();
        } else {
            //запросить разрешение
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_CONTACTS},REQUEST_PERMISION_CODE);
        }
    }

    private void PermisionContactGranted(){
        //Разрешение на чтение контактов предоставлено
        //можно читать
        Log.v("Read contacts:","Разрешение на чтение контактов предоставлено пользователем");

        final List<Contact> contactList = new ArrayList<>();

        Cursor cursor = getContentResolver().query(Contact.CONTENT_URI,
                new String[] {Contact.DISPLAY_NAME, Contact.PHONE_NUMBER, Contact.PHOTO_FULL_URI},
                null,
                null,
                null);
        if(cursor == null) return;//Нету контактов
        if(cursor.getCount() == 0) return;//Нету контактов

        while(cursor.moveToNext()){
            contactList.add(new Contact(cursor.getString(cursor.getColumnIndex(Contact.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(Contact.PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(Contact.PHOTO_FULL_URI))));
        }

        cursor.close();

        for(Contact contact : contactList) Log.v("Contact: ", contact.toString());

        fillRecycler(contactList);
    }

    private void fillRecycler(final List<Contact> contactList){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler);

        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                CardView v = (CardView)LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_card_view,viewGroup,false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                CardView cardView = (CardView)viewHolder.itemView;
                TextView tv = (TextView)cardView.findViewById(R.id.phone);
                tv.setText(contactList.get(i).getPhone());
                tv = (TextView)cardView.findViewById(R.id.name);
                tv.setText(contactList.get(i).getName());
                ImageView imageView = (ImageView)cardView.findViewById(R.id.photo);
                try {
                    if(contactList.get(i).isNoEmptyPhoto())
                        imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(),
                            Uri.parse(contactList.get(i).getPhoto_thumbanall_uri())));
                    else
                        imageView.setImageBitmap(null);
                } catch (IOException e) {

                }
            }

            @Override
            public int getItemCount() {
                return contactList.size();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Разрешение таки получено
                    PermisionContactGranted();
                }
                break;
        }
    }
}
