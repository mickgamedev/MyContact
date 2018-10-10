package ru.yandex.dunaev.mick.mycontact;

import android.net.Uri;
import android.provider.ContactsContract;

public class Contact {
    public static final String CONTACT_ID = ContactsContract.Contacts._ID;
    public static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    public static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    public static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    public static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    public static final Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    public static final String PHOTO_URI = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI;
    public static final String PHOTO_FULL_URI = ContactsContract.Contacts.PHOTO_URI;

    private String name;
    private String phone;
    private String photo_thumbanall_uri="";

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = convertPhoneNumber(phone);
    }

    public Contact(String name, String phone, String photo_thumbanall_uri) {
        this.name = name;
        this.phone = convertPhoneNumber(phone);
        this.photo_thumbanall_uri = photo_thumbanall_uri;
    }

    @Override
    public String toString() {
        return "Name: " + name + " Phone: " + phone;
    }

    private String convertPhoneNumber(String number){
        //удалить все не цифровые символы
        number = number.replaceAll("[^0-9+]+", "");
        number = number.replaceAll("^8","+7");


        return number;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoto_thumbanall_uri() {
        return photo_thumbanall_uri;
    }

    public boolean isNoEmptyPhoto(){
        return photo_thumbanall_uri != null;
    }
}
