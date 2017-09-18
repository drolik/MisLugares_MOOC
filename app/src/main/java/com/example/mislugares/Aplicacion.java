package com.example.mislugares;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by drolik on 18/12/16.
 */

public class Aplicacion extends Application {
    // Firebase auth
    private FirebaseAuth auth;

    // Firebase storage
    private FirebaseStorage storage;
    private static StorageReference storageRef;

    private String ITEMS_CHILD_NAME = "misLugares";
    private static DatabaseReference misLugaresReference;

    public static RequestQueue getColaPeticiones() {
        return colaPeticiones;
    }

    public static void setColaPeticiones(RequestQueue colaPeticiones) {
        Aplicacion.colaPeticiones = colaPeticiones;
    }

    private static RequestQueue colaPeticiones;

    public static ImageLoader getLectorImagenes() {
        return lectorImagenes;
    }

    public static void setLectorImagenes(ImageLoader lectorImagenes) {
        Aplicacion.lectorImagenes = lectorImagenes;
    }

    private static ImageLoader lectorImagenes;
    @Override
    public void onCreate() {
        super.onCreate();
        // Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Volley
        colaPeticiones = Volley.newRequestQueue(this);
        lectorImagenes = new ImageLoader(colaPeticiones,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache =
                            new LruCache<String, Bitmap>(10);

                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }

                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }
                });

        // Firebase storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://mis-lugares-mooc-d1bc9.appspot.com");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        misLugaresReference = database.getReference(ITEMS_CHILD_NAME);

    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public static StorageReference getStorageReference() {
        return storageRef;
    }

    public static DatabaseReference getItemsReference() {
        return misLugaresReference;
    }
    static void mostrarDialogo(final Context context, final String mensaje) {
        Log.d("MIERROR", mensaje);
        Intent intent = new Intent(context, Dialogo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mensaje" , mensaje);
        context.startActivity(intent);
    }
}