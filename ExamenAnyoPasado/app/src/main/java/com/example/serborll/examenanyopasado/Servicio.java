package com.example.serborll.examenanyopasado;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class Servicio extends Service implements LocationListener {

    private static final long TIEMPO_MIN = 10 * 1000 ; // 10 segundos
    private static final long DISTANCIA_MIN = 5; // 5 metros
    private LocationManager manejador;
    private String proveedor;
    private TextView salida;
    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;


    @Override public void onCreate() {
        Toast.makeText(this,"Servicio creado",
                Toast.LENGTH_SHORT).show();
        Log.d("Prueba","Entra en onCreate");

    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        Log.d("Prueba","Entra en onStartCommand");
        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        Toast.makeText(this,"Servicio arrancado "+ idArranque,
                Toast.LENGTH_SHORT).show();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "Mis Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notificacion")
                        .setContentText("Notificacion al arrancar el servicio.");
        notificationManager.notify(NOTIFICACION_ID, notificacion.build());

        PendingIntent intencionPendiente = PendingIntent.getActivity(
                this, 0, new Intent(this, MainActivity.class), 0);
        notificacion.setContentIntent(intencionPendiente);

        startForeground(NOTIFICACION_ID, notificacion.build());
        Toast.makeText(this, "Servicio arrancado " + idArranque,
                Toast.LENGTH_SHORT).show();

        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejador.getBestProvider(criterio, true);
        Location localizacion = manejador.getLastKnownLocation(proveedor);
        muestraLocaliz(localizacion);



        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("Prueba","Nueva localización: ");
        muestraLocaliz(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private void muestraLocaliz(Location localizacion) {

        if (localizacion == null)
            Log.d("Prueba","Localización desconocida\n");
        else
            Log.d("Prueba",localizacion.toString() + "\n");
    }

}
