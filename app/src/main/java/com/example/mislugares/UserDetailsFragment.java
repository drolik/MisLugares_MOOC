package com.example.mislugares;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserDetailsFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private long id;
    private Lugar lugar;
    //private ImageView imageView;
    final static int RESULTADO_EDITAR = 1;
    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;
    private Uri uriFoto;
    private View v;
    private View vista;
    private String name;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor,
                             Bundle savedInstanceState) {
        vista = inflador.inflate(R.layout.user_detail,contenedor,false);
        setHasOptionsMenu(false);
        Button cerrarSesion =(Button) vista.findViewById(R.id.btnSignin);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
   /*             Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("LOGOUT", true);
                startActivity(intent);

                getActivity().finish();
                */


                AuthUI.getInstance().signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                SharedPreferences pref = getActivity().getSharedPreferences(
                                        "com.example.audiolibros_internal", getActivity().MODE_PRIVATE);
                                pref.edit().remove("provider").commit();
                                pref.edit().remove("email").commit();
                                pref.edit().remove("name").commit();
                                Intent i = new Intent(getActivity(),LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                getActivity().finish();
                            }
                        });

            }
        });

   /*
        LinearLayout pUrl = (LinearLayout) vista.findViewById(R.id.barra_url);
        pUrl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pgWeb(null);
            }
        });
        LinearLayout pTlf = (LinearLayout) vista.findViewById(R.id.barra_telefono);
        pTlf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                llamadaTelefono(null);
            }
        });
        LinearLayout pMapa = (LinearLayout) vista.findViewById(R.id.barra_direccion);
        pMapa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                verMapa(null);
            }
        });
        ImageView iconoFoto = (ImageView) vista.findViewById(R.id.camara);
        iconoFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { tomarFoto(null);
            }
        });
        ImageView iconoGaleria = (ImageView) vista.findViewById(R.id.galeria);
        iconoGaleria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { galeria(null);
            }
        });
        ImageView iconoBorra = (ImageView) vista.findViewById(R.id.eliminarFoto);
        iconoBorra.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { eliminarFoto(null);
            }
        });
        ImageView iconoHora = (ImageView) vista.findViewById(R.id.icono_hora);
        iconoHora.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cambiarHora();
            }
        });
        ImageView iconoFecha = (ImageView) vista.findViewById(R.id.icono_fecha);
        iconoFecha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cambiarFecha();
            }
        });
        */

        // Nombre de usuario
      //  SharedPreferences pref = getSharedPreferences(
        //        "com.example.mislugares_internal", this.getContext().MODE_PRIVATE);
       // String name = pref.getString("name", null);

        TextView nombre = (TextView) vista.findViewById(R.id.user_name);
        nombre.setText(name);
        return vista;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        v = getView();
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("user_name", null);
            if (name != null) {
                actualizarVistas(name);
            }
        }

        // Foto de usuario
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        Uri urlImagen = usuario.getPhotoUrl();
        if (urlImagen != null) {
            NetworkImageView fotoUsuario = (NetworkImageView)
                    vista.findViewById(R.id.imageView);
            Aplicacion aplicacion = (Aplicacion) getActivity().getApplicationContext();
            fotoUsuario.setImageUrl(urlImagen.toString(),
                    aplicacion.getLectorImagenes());
        }

    }

    public void actualizarVistas(final String name) {
        TextView nombre = (TextView) vista.findViewById(R.id.user_name);
        nombre.setText(name);

        //lugar = MainActivity.lugares.elemento((int) id);
        //lugar = SelectorFragment.adaptador.lugarPosicion((int) id);
        this.id = id;
        /*
        lugar = SelectorFragment.adaptador.lugarPosicion((int) id);
        if (lugar != null) {

        TextView nombre = (TextView) v.findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        ImageView logo_tipo = (ImageView) v.findViewById(R.id.logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());
        TextView tipo = (TextView) v.findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());

        if (lugar.getDireccion().isEmpty()) {
            v.findViewById(R.id.barra_direccion).setVisibility(View.GONE);
        } else {
            TextView direccion = (TextView) v.findViewById(R.id.direccion);
            direccion.setText(lugar.getDireccion());
        }
        if (lugar.getTelefono() == 0) {
            v.findViewById(R.id.barra_telefono).setVisibility(View.GONE);
        } else {
            TextView telefono = (TextView) v.findViewById(R.id.telefono);
            telefono.setText(Integer.toString(lugar.getTelefono()));
        }
        if (lugar.getUrl().isEmpty()) {
            v.findViewById(R.id.barra_url).setVisibility(View.GONE);
        } else {
            TextView url = (TextView) v.findViewById(R.id.url);
            url.setText(lugar.getUrl());
        }
        if (lugar.getComentario().isEmpty()) {
            v.findViewById(R.id.barra_comentario).setVisibility(View.GONE);
        } else {
            TextView comentario = (TextView) v.findViewById(R.id.comentario);
            comentario.setText(lugar.getComentario());
        }
        TextView fecha = (TextView) v.findViewById(R.id.fecha);
        fecha.setText(DateFormat.getDateInstance().format(
                new Date(lugar.getFecha())));
        TextView hora = (TextView) v.findViewById(R.id.hora);
        hora.setText(DateFormat.getTimeInstance().format(
                new Date(lugar.getFecha())));
        RatingBar valoracion = (RatingBar) v.findViewById(R.id.valoracion);
        valoracion.setOnRatingBarChangeListener(null);
        valoracion.setRating(lugar.getValoracion());
        valoracion.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float valor, boolean fromUser) {
                        lugar.setValoracion(valor);
                        actualizaLugar();
                    }
                });
        ponerFoto((ImageView)v.findViewById(R.id.foto), lugar.getFoto());
        */
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.vista_lugar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_compartir:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        lugar.getNombre() + " - " + lugar.getUrl());
                startActivity(intent);
                return true;
            case R.id.accion_llegar:
                verMapa(null);
                return true;
            case R.id.accion_editar:
                lanzarEdicionLugar(id);
                return true;
            case R.id.accion_borrar:
                int _id = SelectorFragment.adaptador.idPosicion((int) id);
                borrarLugar((int) _id);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void verMapa(View view) {
        Uri uri;
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        if (lat != 0 || lon != 0) {
            uri = Uri.parse("geo:" + lat + "," + lon);
        } else {
            uri = Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void borrarLugar(final int id) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Borrado de lugar")
                .setMessage("¿Estás seguro que quieres eliminar este lugar?")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.lugares.borrar(id);
                        SelectorFragment.adaptador.setCursor(
                                MainActivity.lugares.extraeCursor());
                        SelectorFragment.adaptador.notifyDataSetChanged();
                        SelectorFragment selectorFragment = (SelectorFragment) getActivity().
                                getSupportFragmentManager().findFragmentById(R.id.selector_fragment);
                        if (selectorFragment == null) {
                            getActivity().finish();
                        } else {
                            ((MainActivity) getActivity()).muestraLugar(0);
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void llamadaTelefono(View view) {
        startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + lugar.getTelefono())));
    }

    public void pgWeb(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(lugar.getUrl())));
    }

    public void lanzarEdicionLugar(final long id) {
        Intent i = new Intent(getActivity(), EdicionLugarActivity.class);
        i.putExtra("id", id);
        startActivityForResult(i, RESULTADO_EDITAR);
    }

    public void galeria(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULTADO_GALERIA);
    }

    private static final int SOLICITUD_PERMISO_LECTURA = 0;

    ImageView lastImageView;
    String lastUri;

    protected void ponerFoto(ImageView imageView, String uri) {

        if (uri != null && !uri.isEmpty() && !uri.equals("null")) {
            if (uri.startsWith("content://com.example.mislugares/") ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.
                        READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
               imageView.setImageBitmap(reduceBitmap(getActivity(), uri, 1024,   1024));
            } else  {
                    lastImageView=imageView; lastUri=uri;
                    PermisosUtilidades.solicitarPermisoFragment(Manifest.permission.
                          READ_EXTERNAL_STORAGE, "Sin permiso de lectura no es posible "+
                          "mostrar fotos de memoria externa", SOLICITUD_PERMISO_LECTURA, this);
            }
        } else {
            imageView.setImageBitmap(null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_LECTURA) {
            if (grantResults.length== 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ponerFoto(lastImageView, lastUri);
            } else {
                ponerFoto(lastImageView, null);
            }
        }
    }

    public static Bitmap reduceBitmap(Context contexto, String uri,
                                      int maxAncho, int maxAlto) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Fichero/recurso no encontrado",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    public void tomarFoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File file = null;
            try {
                file = File.createTempFile(
                    "img_" + (System.currentTimeMillis() / 1000),       // nombre
                    ".jpg",                                             // extensión
                    //Environment.getExternalStoragePublicDirectory("")
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)); // directorio
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (file != null) {
                uriFoto = FileProvider.getUriForFile(getActivity(),
                        "com.example.mislugares",
                        file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
                startActivityForResult(intent, RESULTADO_FOTO);
            }
        }
    }

    public void tomarFoto2(View view) {
        /////////////////////
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ///////////////////////////
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uriFoto = Uri.fromFile(new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + "img_" + (System.currentTimeMillis() / 1000) + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
        startActivityForResult(intent, RESULTADO_FOTO);
    }

    public void eliminarFoto(View view) {
        lugar.setFoto(null);
        ponerFoto((ImageView)v.findViewById(R.id.foto), ""); //null);
        actualizaLugar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        if (requestCode == RESULTADO_EDITAR) {
            actualizarVistas(name);
            /*View s = v.findViewById(R.id.scrollView1);
            if (s!=null) {
                s.invalidate();
            }*/
            //v.findViewById(R.id.scrollView1).invalidate();
        } else if (requestCode == RESULTADO_GALERIA) {
            if (resultCode == Activity.RESULT_OK) {
                lugar.setFoto(data.getDataString());
                ponerFoto((ImageView)v.findViewById(R.id.foto), lugar.getFoto());
                actualizaLugar();
            } else {
                Toast.makeText(getActivity(), "Error carfando foto", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULTADO_FOTO) {
            if (resultCode == Activity.RESULT_OK
                    && lugar != null && uriFoto != null) {
                lugar.setFoto(uriFoto.toString());
                ponerFoto((ImageView)v.findViewById(R.id.foto), lugar.getFoto());
                actualizaLugar();
            } else {
                Toast.makeText(getActivity(), "Error capturando foto", Toast.LENGTH_LONG).show();
            }
        }
    }

    void actualizaLugar(){
        int _id = SelectorFragment.adaptador.idPosicion((int) id);
        MainActivity.lugares.actualiza(_id, lugar);
        SelectorFragment.adaptador.setCursor(MainActivity.lugares.extraeCursor());
//        SelectorFragment.adaptador.notifyItemChanged((int) id);
        SelectorFragment.adaptador.notifyDataSetChanged();
    }

    public void cambiarHora() {
        DialogoSelectorHora dialogo = new DialogoSelectorHora();
        dialogo.setOnTimeSetListener(this);
        Bundle args = new Bundle();
        args.putLong("fecha", lugar.getFecha());
        dialogo.setArguments(args);
        dialogo.show(getActivity().getSupportFragmentManager(), "selectorHora");
    }

    @Override
    public void onTimeSet(TimePicker vista, int hora, int minuto) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(lugar.getFecha());
        calendario.set(Calendar.HOUR_OF_DAY, hora);
        calendario.set(Calendar.MINUTE, minuto);
        lugar.setFecha(calendario.getTimeInMillis());
        actualizaLugar();
        TextView tHora = (TextView) getView().findViewById(R.id.hora);
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm",
                java.util.Locale.getDefault());
        tHora.setText(formato.format(new Date(lugar.getFecha())));
    }

    public void cambiarFecha() {
        DialogoSelectorFecha dialogo = new DialogoSelectorFecha();
        dialogo.setOnDateSetListener(this);
        Bundle args = new Bundle();
        args.putLong("fecha", lugar.getFecha());
        dialogo.setArguments(args);
        dialogo.show(getActivity().getSupportFragmentManager(), "selectorFecha");
    }

    @Override
    public void onDateSet(DatePicker view, int anyo, int mes, int dia) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(lugar.getFecha());
        calendario.set(Calendar.YEAR, anyo);
        calendario.set(Calendar.MONTH, mes);
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        lugar.setFecha(calendario.getTimeInMillis());
        actualizaLugar();
        TextView tFecha = (TextView) getView().findViewById(R.id.fecha);
        DateFormat formato =  DateFormat.getDateInstance();
        tFecha.setText(formato.format(new Date(lugar.getFecha())));
    }
}