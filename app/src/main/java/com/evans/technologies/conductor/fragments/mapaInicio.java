package com.evans.technologies.conductor.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evans.technologies.conductor.Activities.MainActivity;
import com.evans.technologies.conductor.R;
import com.evans.technologies.conductor.Retrofit.RetrofitClient;
import com.evans.technologies.conductor.Utils.ComunicacionesRealTime.ComunicateFrag;
import com.evans.technologies.conductor.Utils.ComunicacionesRealTime.updateListenerNotifications;
import com.evans.technologies.conductor.model.Driver;
import com.evans.technologies.conductor.model.LoginResponse;
import com.evans.technologies.conductor.model.infoDriver;
import com.evans.technologies.conductor.model.notification.data;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.evans.technologies.conductor.Utils.UtilsKt.GetLastPosition;
import static com.evans.technologies.conductor.Utils.UtilsKt.getApiWebVersion;
import static com.evans.technologies.conductor.Utils.UtilsKt.getCronometroStop;
import static com.evans.technologies.conductor.Utils.UtilsKt.getCurrentRoute;
import static com.evans.technologies.conductor.Utils.UtilsKt.getDestinoLat;
import static com.evans.technologies.conductor.Utils.UtilsKt.getDestinoLong;
import static com.evans.technologies.conductor.Utils.UtilsKt.getDriverId_Prefs;
import static com.evans.technologies.conductor.Utils.UtilsKt.getEndAddress;
import static com.evans.technologies.conductor.Utils.UtilsKt.getEstadoView;
import static com.evans.technologies.conductor.Utils.UtilsKt.getLlaveChat;
import static com.evans.technologies.conductor.Utils.UtilsKt.getOrigenLat;
import static com.evans.technologies.conductor.Utils.UtilsKt.getOrigenLong;
import static com.evans.technologies.conductor.Utils.UtilsKt.getPrice;
import static com.evans.technologies.conductor.Utils.UtilsKt.getPriceDiscount;
import static com.evans.technologies.conductor.Utils.UtilsKt.getStartAddress;
import static com.evans.technologies.conductor.Utils.UtilsKt.getStatusSwitch;
import static com.evans.technologies.conductor.Utils.UtilsKt.getTieneInfo;
import static com.evans.technologies.conductor.Utils.UtilsKt.getTienePasajero;
import static com.evans.technologies.conductor.Utils.UtilsKt.getUserId;
import static com.evans.technologies.conductor.Utils.UtilsKt.getVersionApp;
import static com.evans.technologies.conductor.Utils.UtilsKt.getViajeId;
import static com.evans.technologies.conductor.Utils.UtilsKt.getViewUpdateVersion;
import static com.evans.technologies.conductor.Utils.UtilsKt.getaccountActivate;
import static com.evans.technologies.conductor.Utils.UtilsKt.getdataNotification_noti;
import static com.evans.technologies.conductor.Utils.UtilsKt.getgetTieneNotificacionViaje;
import static com.evans.technologies.conductor.Utils.UtilsKt.llaveChat;
import static com.evans.technologies.conductor.Utils.UtilsKt.setChatJson;
import static com.evans.technologies.conductor.Utils.UtilsKt.setClaseActual;
import static com.evans.technologies.conductor.Utils.UtilsKt.setCronometroStop;
import static com.evans.technologies.conductor.Utils.UtilsKt.setCurrentRoute;
import static com.evans.technologies.conductor.Utils.UtilsKt.setDataNotification;
import static com.evans.technologies.conductor.Utils.UtilsKt.setEstadoViews;
import static com.evans.technologies.conductor.Utils.UtilsKt.setLastDirection;
import static com.evans.technologies.conductor.Utils.UtilsKt.setLastPostition;
import static com.evans.technologies.conductor.Utils.UtilsKt.setPrice;
import static com.evans.technologies.conductor.Utils.UtilsKt.setPriceDiscount;
import static com.evans.technologies.conductor.Utils.UtilsKt.setStatusSwitch;
import static com.evans.technologies.conductor.Utils.UtilsKt.setTieneNotificacionViaje;
import static com.evans.technologies.conductor.Utils.UtilsKt.setTienePasajero;
import static com.evans.technologies.conductor.Utils.UtilsKt.toastLong;
import static com.evans.technologies.conductor.Utils.constantes.AppConstants.SEND_NOTIFICATION_REQUEST_DRIVER;
import static com.evans.technologies.conductor.Utils.constantes.AppConstants.SEND_NOTIFICATION_SUBIO_ABORDO;
import static com.evans.technologies.conductor.Utils.constantes.AppConstants.SEND_NOTIFICATION_VIAJE_CANCELADO;
import static com.evans.technologies.conductor.Utils.constantes.AppConstants.SEND_NOTIFICATION_VIAJE_TERMINADO;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


/**
 * A simple {@link Fragment} subclass.
 */
public class mapaInicio extends Fragment implements OnMapReadyCallback, View.OnClickListener,PermissionsListener {

    private static final String TAG = "Mapa Inicio";

    private final int GPS = 51;
    //Bindeos Mapa principal//////////////////////////////////////////////////////////////////
    View view;

    //Bindeos dialog destinos//////////////////////////////////////////////////////////////////

    //Bindeos Dialog Precio///////////////////////////////////////////////////////////////////////


    //markers
    private Geocoder geo;
    private LocationManager locationManager;

    //Line
    private String segundos;
    private MapboxMap mapboxMap;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;

    private Geocoder geocoder;
    private List<Address> address;
    //Frament inicial
    @BindView(R.id.startButton)
    ImageView button;
    @BindView(R.id.mapa_icon_gps)
    ImageView gps;
    @BindView(R.id.mapView)
    MapView mapView;
    //dialog_estado_txt_estado
    @BindView(R.id.fmi_dialog_estado)
    View dialog_estado;
    @BindView(R.id.dialog_estado_txt_estado)
    TextView dialog_txt_estado;
    @BindView(R.id.dialog_estado_txt_estado_true)
    TextView dialog_txt_true;
    ////Time
    private Handler handler = new Handler();

    private final int  TIEMPO=4000;
    //Bindeos Dialog Viajes
    @BindView(R.id.dvav_vista_accept)
    View dialog_viaje;
    //@BindView(R.id.dvav)
    // TextView dialog_viaje_tiempo_espera;
    @BindView(R.id.dialog_fin_viaje_imgbtn_message_notify)
    ImageView dialog_fin_viaje_imgbtn_message_notify;
    @BindView(R.id.dvav_imgbtn_accept)
    ImageButton dialog_viaje_btn_aceptar;
    @BindView(R.id.dvav_imgbtn_close)
    ImageButton dialog_viaje_btn_rechazar;
    @BindView(R.id.dvav_img_add)
    ImageView dvav_img_add;
    @BindView(R.id.dvav_img_money)
    ImageView dvav_img_money;
    @BindView(R.id.dvav_img_cupon)
    ImageView dvav_img_cupon;
    @BindView(R.id.dvav_txt_name)
    TextView dialog_viaje_txt_nombre;
    @BindView(R.id.dvav_txt_time)
    TextView dialog_viaje_txt_tiempo;
    @BindView(R.id.dvav_txt_distance)
    TextView dialog_viaje_txt_distance;
    //Dialogs acepted
    @BindView(R.id.dialog_fin_viaje_imgbtn_delete)
    ImageButton dialog_fin_viaje_imgbtn_delete;

    @BindView(R.id.fmi_dialog_aceppted)
    View dialog_fin_viaje;
    @BindView(R.id.dialog_fin_viaje_imgbtn_settings)
    ImageButton dialog_fin_viaje_imgbtn_settings;
    @BindView(R.id.dialog_fin_viaje_imgbtn_message)
    ImageButton dialog_fin_viaje_imgbtn_message;
    @BindView(R.id.dialog_fin_viaje_btn_aceptar)
    Button dialog_fin_viaje_btn_aceptar;
    @BindView(R.id.dialog_fin_viaje_txt_destino)
    TextView dialog_fin_viaje_txt_destino;
    @BindView(R.id.dialog_fin_viaje_txt_origen)
    TextView dialog_fin_viaje_txt_origen;
    @BindView(R.id.dialog_fin_viaje_txt_titulo)
    TextView dialog_fin_viaje_txt_titulo;
    @BindView(R.id.dialog_fin_viaje_txt_precio)
    TextView dialog_fin_viaje_txt_precio;
    @BindView(R.id.dialog_fin_viaje_txt_precio_coupon)
    TextView dialog_fin_viaje_txt_precio_coupon;



    @BindView(R.id.dialog_no_connect_internet)
    View dialog_no_connect_internet;
    private PermissionsManager permissionsManager;
    CountDownTimer countDownTimer;
    int estado_fin_viaje;
    MediaPlayer mp;
    //SharedPreferences prefs;
    private SharedPreferences dataDriver;
    private SharedPreferences prefs;
    private BroadcastReceiver receiver;
    //mabox Route
    int valor=1;
    private Marker origen;
    private Marker destino;
    private IconFactory iconFactory;
    private updateListenerNotifications comunicateFrag;

    Runnable runnable_uc;
    private Activity activiy;
    DataSnapshot chatSnapshot;
    /*router ide layer*/
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";

    //
//    private static final String ICON_LAYER_ID = "icon-layer-id";
//    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    public mapaInicio(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Mapbox.getInstance(Objects.requireNonNull(getContext()), getString(R.string.access_token));
        dataDriver = getContext().getSharedPreferences("datadriver", Context.MODE_PRIVATE);
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setClaseActual(prefs,new mapaInicio().toString());
        view = inflater.inflate(R.layout.fragment_mapa_inicio, container, false);
        ButterKnife.bind(this,view);

        iconFactory = IconFactory.getInstance(getContext());

        dialog_viaje_btn_aceptar.setOnClickListener(this);
        dialog_viaje_btn_rechazar.setOnClickListener(this);
        dialog_fin_viaje_imgbtn_settings.setOnClickListener(this);
        dialog_fin_viaje_imgbtn_message.setOnClickListener(this);
        dialog_fin_viaje_btn_aceptar.setOnClickListener(this);

        gps.setOnClickListener(this);
        dialog_fin_viaje_imgbtn_delete.setOnClickListener(this);

        if (mapView != null) {
            mapView.onCreate( savedInstanceState );
            mapView.onResume();
            mapView.getMapAsync( this );
        }
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);


        gpsEnable();

        ((MainActivity) Objects.requireNonNull(getActivity())).updateApi(new ComunicateFrag.mapa_inicio() {
            @Override
            public void updatefrag(Boolean checked) {
                Log.e("borrardo"," updatefrag valor"+checked);
                setStatusSwitch(dataDriver,checked);
                if (checked){
                    if (handler != null && runnable_uc != null) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    ejecutarTarea();

                    try {
                        Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLng(
                                new com.mapbox.mapboxsdk.geometry.LatLng(Objects.requireNonNull(lastKnownLocation).getLatitude(), lastKnownLocation.getLongitude())));
                    }catch (Exception e){
                        Log.e("deviceLocation", e.getMessage());
                    }
                    setEstadoViews(dataDriver,2);
                    Log.e("estado2","entroenupdatefrag");
                    segundo_estado();
                }else {
                    if (runnable_uc!=null&&handler!=null){
                        handler.removeCallbacks(runnable_uc);
                        handler.removeCallbacksAndMessages(null);
                    }
                    Log.e("borrardo"," updatefrag");
                    deleteFunFirebaseUc();
                    borrarSharedPreferencesDataDriver(false,1);
                    primer_estado();

                }
            }

            @Override
            public void mensajeGet(@NotNull DataSnapshot p0) {
                chatSnapshot=p0;
                dialog_fin_viaje_imgbtn_message_notify.setVisibility(View.VISIBLE);
               // toastLong(getActivity(),"entro");
            }
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String datassss=intent.getExtras().getString("data");
                if (getEstadoView(dataDriver)<3){
                    setTieneNotificacionViaje(dataDriver,false);
                }
                Log.e("getData","entro on recive");
                ejecutar_acciones_data_notificacion(datassss);
            }
        };

        comprobarStatusTrip();

       /* Intent intent = new Intent("subsUnsubs");
        intent.putExtra("subs", "notify");
        intent.putExtra("status","register");
        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
        broadcaster.sendBroadcast(intent);
        Intent intent2 = new Intent("subsUnsubs");
        intent2.putExtra("subs", "notify"+getDriverId_Prefs(prefs));
        intent2.putExtra("status","register");
        LocalBroadcastManager broadcaster2 = LocalBroadcastManager.getInstance(getContext());
        broadcaster2.sendBroadcast(intent2);*/




        // setRetainInstance(false);
        return view;

    }

    private void comprobarStatusTrip() {
        if (!getViajeId(dataDriver).equals("nulo")&&getTienePasajero(dataDriver)){
            Log.e("CST","entro aca");
            Call<infoDriver> statusTrip= RetrofitClient.getInstance().getApi().getStatusTrip(getViajeId(dataDriver));
            statusTrip.enqueue(new Callback<infoDriver>() {
                @Override
                public void onResponse(Call<infoDriver> call, Response<infoDriver> response) {
                    try{
                        if ( !(response.body().isOk())){
                            setChatJson(dataDriver,"nulo");
                           /* Intent intent = new Intent("subsUnsubs");
                            intent.putExtra("subs", "chat");
                            intent.putExtra("status","false");
                            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
                            broadcaster.sendBroadcast(intent);*/
                            if (getStatusSwitch(dataDriver)){
                               // borrarSharedPreferencesDataDriver(true,2);
                                segundo_estado();
                            }
                            else{
                               // borrarSharedPreferencesDataDriver(false,1);
                                primer_estado();
                            }

                        }
                    }catch(Exception e){

                    }
                }

                @Override
                public void onFailure(Call<infoDriver> call, Throwable t) {

                }
            });

        }

    }
    private void ejecutar_acciones_data_notificacion(String datassss){


        Log.e("getData","entro ejecutarnotifyu  "+datassss);
        Gson gson = new Gson();
        data topic = gson.fromJson(datassss, data.class);

        if (getTienePasajero(dataDriver)){
            if (topic.getResponse().contains(SEND_NOTIFICATION_SUBIO_ABORDO)){
                forzarpaso6();



            }else if(topic.getResponse().equals(SEND_NOTIFICATION_VIAJE_TERMINADO)){
                try{
                    setPrice(dataDriver,topic.getPrice());
                    setPriceDiscount(dataDriver,topic.getPricediscount());
                }catch (Exception e){

                }
                setEstadoViews(dataDriver,8);
                octavo_estado();
            }else if(topic.getResponse().equals(SEND_NOTIFICATION_VIAJE_CANCELADO)){
                clearMessages();
                if (!(getApiWebVersion(prefs).equals(getVersionApp(getContext())))){
                    getViewUpdateVersion(getActivity(),getContext());
                }
                if (mp!=null){
                    mp.stop();
                }
                borrarSharedPreferencesDataDriver(true,2);
                Log.e("estado2","entroen viaje cancelado");
                segundo_estado();
            }else {

            }
        }else {
            if (getgetTieneNotificacionViaje(dataDriver)){

            }else{
                if (topic.getResponse().equals(SEND_NOTIFICATION_REQUEST_DRIVER)){


                    Log.e("getData","entro requesDriver  "+SEND_NOTIFICATION_REQUEST_DRIVER);

                    setDataNotification(dataDriver,topic.getLatitudeOrigen()+"",topic.getLongitudeOrigen()+"",topic.getLatitudeDestino()+"",topic.getLongitudeDestino()+"",
                            topic.getStartAddress().replace("-"," "),topic.getDestinationAddress().replace("-"," "),topic.getTravelRate(),topic.getTravelRateDiscount(),
                            topic.getUserId(),getDriverId_Prefs(prefs),topic.getViajeId());



                    Point destination = Point.fromLngLat( Double.parseDouble(getOrigenLong(dataDriver)),Double.parseDouble(getOrigenLat(dataDriver)));
                    setEstadoViews(dataDriver,3);
                    setTieneNotificacionViaje(dataDriver,true);
                    DibujarLineas( GetLastPosition(prefs),destination);


                }
            }

        }
    }

    private void forzarpaso6() {

        setEstadoViews(dataDriver,6);
        RemoveLayersMapbox();

        Point origin = Point.fromLngLat(  Double.parseDouble(getOrigenLong(dataDriver)),Double.parseDouble(getOrigenLat(dataDriver)));
        Point destination = Point.fromLngLat( Double.parseDouble(getDestinoLong(dataDriver)),Double.parseDouble(getDestinoLat(dataDriver)));
        setCurrentRoute(dataDriver,"nulo");
        DibujarLineas(origin,destination);
        sexto_estado();
    }

    public  void ejecutarTarea(){

        runnable_uc= new Runnable() {
            @Override
            public void run() {

                if (isNetDisponible()||isOnlineNet()){
                    dialog_no_connect_internet.setVisibility(View.GONE);

                    try {
                        if (getStatusSwitch(dataDriver)){

                            Double lat=mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude();
                            Double log= mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude();
                            setLastPostition(prefs,lat,log);
                            Map<String, Object> map = new HashMap<String, Object>();
                            address = geo.getFromLocation(lat,log,1);
                            Log.e("ejecutarTarea" ,lat+"  "+log);
                            String[] direccion2;
                            direccion2 = (address.get(0).getAddressLine(0)).split(",");
                            setLastDirection(dataDriver,direccion2[0]);
                            map.put("lat",lat);
                            map.put("log",log);
                            map.put("statusSwitch",getStatusSwitch(dataDriver));
                            if (getEstadoView(dataDriver)>3){
                                map.put("statusTrip",true);
                            }else{
                                map.put("statusTrip",false);
                            }

                            FirebaseDatabase.getInstance().getReference().child("coordenadaUpdate").child(getDriverId_Prefs(prefs)).setValue(map);

                            if (!getStatusSwitch(dataDriver)) {
                                deleteFunFirebaseUc();

                            }
                        }


                               /* Call<Driver> call = RetrofitClient.getInstance()
                                        .getApi().updateCoordenadas(getToken(prefs),getDriverId_Prefs(prefs),lat,log);

                                call.enqueue(new Callback<Driver>() {
                                    @Override
                                    public void onResponse(Call<Driver> call, Response<Driver> response) {
                                        if (response.isSuccessful()){




                                            try {

                                                String pais;
                                                String departamento;
                                                if(!getLastDirection(dataDriver).equals(direccion2[0])){
                                                    pais=address.get(0).getCountryName();
                                                    departamento=address.get(0).getLocality();

                                                    map.put("ciudadpais",departamento+", "+pais);
                                                    map.put("name",direccion2[0]);
                                                    map.put("ciudad",departamento);
                                                    map.put("pais",pais);
                                                    String datos=(lat+""+log);
                                                    datos= datos.replace(".","a");
                                                    datos= datos.replace("-","b");
                                                    Log.e("LocationUpdate",  datos);
                                                    DatabaseReference base= FirebaseDatabase.getInstance().getReference();
                                                    base.child(pais).child(departamento).child(datos).updateChildren(map);

                                                }

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Driver> call, Throwable t) {

                                    }
                                });*/
                    }catch (Exception e){
                        Log.e("Update","Cordenadas "+e.getMessage());
                    }
                }else{
                    dialog_no_connect_internet.setVisibility(View.VISIBLE);
                }




                handler.postDelayed(this,TIEMPO);

            }
        };

        handler.postDelayed(runnable_uc,TIEMPO);
    }
    private void deleteFunFirebaseUc(){
        handler.removeCallbacks(runnable_uc);
        FirebaseDatabase.getInstance().getReference().child("coordenadaUpdate").child(getDriverId_Prefs(prefs)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    dataSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS:
                if (resultCode == RESULT_OK) {

                    getDeviceLocation();

                } else {
                    Toast.makeText(getContext(), "Error de gps", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        activiy=getActivity();
        try {
            comunicateFrag = (updateListenerNotifications) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
        super.onAttach(context);
    }
    private void getRoute(Point origin, Point destination) {
        Log.e("errordl11",getCurrentRoute(dataDriver));

        if (destino!=null){
            destino.remove();
            destino=null;
        }
        if (origen!=null){
            origen.remove();
            origen=null;
        }
        if (getCurrentRoute(dataDriver).equals("nulo")){
            NavigationRoute.builder(getContext())
                    .accessToken(getString(R.string.access_token))
                    .origin(origin)
                    .destination(destination)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                            try {
                                Log.d(TAG, "Response code: " + response.body());
                                if (response.body() == null) {
                                    Log.e(TAG, "el cuerpo es nulo o no se encontro una ruta");
                                    return;
                                } else if (response.body().routes().size() < 1) {
                                    Log.e(TAG, "no se encontro rutas");
                                    return;
                                }else{

                                }
                            }catch (Exception e){
                                Log.e(TAG, e.getMessage());
                            }


                           // toastLong(getActivity(),getEstadoView(dataDriver)+"");
                            if (getEstadoView(dataDriver)==3){
                                setCurrentRoute(dataDriver,response.body().routes().get(0).toJson());
                                tercer_estado(response.body().routes().get(0).distance().toString(),
                                        response.body().routes().get(0).duration().toString());
                                llenarRuta();
                            }

                            if (getEstadoView(dataDriver)==6){
                                Log.e("rutas    ", response.body().routes().get(0).toJson());
                                setCurrentRoute(dataDriver,response.body().routes().get(0).toJson());
                                llenarRuta();
                            }
                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(
                                    new LatLngBounds.Builder()
                                            .include(new LatLng(origin.latitude(), origin.longitude()))
                                            .include(new LatLng(destination.latitude(), destination.longitude()))
                                            .build(), 150), 1000);



                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                            Log.e(TAG, "Error: " + throwable.getMessage());
                        }
                    });
        }else{
            //  setCurrentRoute(dataDriver,"nulo");
            //  getRoute(origin,destination);
            llenarRuta();
        }

        //button.setVisibility(View.VISIBLE);
    }
    private void llenarRuta(){
/*try {
            initSource( mapboxMap.getStyle(),origin,destination);

            initLayers(mapboxMap.getStyle());
        }catch (Exception e){
            Log.e("errordl1",e.getMessage());
        }*/
        Log.e("llenarRuta",getCurrentRoute(dataDriver));
        if (getCurrentRoute(dataDriver).equals("nulo")){
            toastLong(getActivity(),"No se encontro la ruta.");
        }else{
            if (currentRoute==null){
                Log.e("rutas ","entro");
                currentRoute=DirectionsRoute.fromJson(getCurrentRoute(dataDriver));

            }
            if (mapboxMap!=null) {
                button.setVisibility(View.VISIBLE);
                if (getEstadoView(dataDriver)>=6){
                    if (destino==null){
                        destino=mapboxMap.addMarker(new MarkerOptions()
                                .setIcon(iconFactory.fromResource(R.drawable.logo22))
                                .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(Double.parseDouble(getDestinoLat(dataDriver)),Double.parseDouble(getDestinoLong(dataDriver))))
                                .title("Destino"));
                    }
                    if (origen==null){
                        origen=mapboxMap.addMarker(new MarkerOptions()
                                .setIcon(iconFactory.fromResource(R.drawable.logo33))
                                .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(Double.parseDouble(getOrigenLat(dataDriver)),Double.parseDouble(getOrigenLong(dataDriver))))
                                .title("Origen"));
                    }
                }else{
                    if (destino==null){
                        destino=mapboxMap.addMarker(new MarkerOptions()
                                .setIcon(iconFactory.fromResource(R.drawable.logo22))
                                .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(Double.parseDouble(getOrigenLat(dataDriver)),Double.parseDouble(getOrigenLong(dataDriver))))
                                .title("Destino"));
                    }
                    if (origen==null){
                        origen=mapboxMap.addMarker(new MarkerOptions()
                                .setIcon(iconFactory.fromResource(R.drawable.logo33))
                                .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(GetLastPosition(prefs).latitude(),GetLastPosition(prefs).longitude()))
                                .title("Origen"));
                    }
                }

                try {

                    //navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.navegation);
                    // navigationMapRoute.addRoute(currentRoute);
                    //button.setVisibility(View.VISIBLE);

                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                            Log.e("rutas ", "entrox2");


                            GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);
                            if (source == null) {
                                if (getEstadoView(dataDriver) <= 6) {
                                    try {
                                        Point origin = Point.fromLngLat(GetLastPosition(prefs).longitude(),GetLastPosition(prefs).latitude());
                                        Point destination = Point.fromLngLat(Double.parseDouble(getOrigenLong(dataDriver)), Double.parseDouble(getOrigenLat(dataDriver)));
                                        initSource(mapboxMap.getStyle(), origin, destination);

                                        initLayers(mapboxMap.getStyle());
                                    } catch (Exception e) {
                                        Log.e("errordl1", e.getMessage());
                                    }
                                } else {
                                    Point origin = Point.fromLngLat(Double.parseDouble(getOrigenLong(dataDriver)), Double.parseDouble(getOrigenLat(dataDriver)));
                                    Point destination = Point.fromLngLat(Double.parseDouble(getDestinoLong(dataDriver)), Double.parseDouble(getDestinoLat(dataDriver)));

                                    initSource(mapboxMap.getStyle(), origin, destination);

                                    initLayers(mapboxMap.getStyle());
                                }

                                source = style.getSourceAs(ROUTE_SOURCE_ID);
                            }

                            if (source != null) {
                                source.setGeoJson(FeatureCollection.fromFeature(
                                        Feature.fromGeometry(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6))));
                                Log.e("rutas ", "dibujo");
                            }

                        }
                    });
                } catch (Exception e) {
                    toastLong(getActivity(), "Error al dibujar en el mapa");
                }
            }

        }

        //Log.e("dibujarmapa","get Route "+currentRoute.toString());
       /* if (navigationMapRoute != null) {
            navigationMapRoute.removeRoute();
            navigationMapRoute.onStop();
            navigationMapRoute=null;
        }*/

    }
    private void getDeviceLocation() {
        try {
            Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

            Double log=lastKnownLocation.getLongitude();
            Double lat= lastKnownLocation.getLatitude();
            CameraPosition position = new CameraPosition.Builder()
                    .target(new com.mapbox.mapboxsdk.geometry.LatLng(lat,log )) // Sets the new camera position
                    .zoom(15)
                    .build(); // Creates a CameraPosition from the builder

            mapboxMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), 1000);

        }catch (Exception e){

            Log.e("deviceLocation", e.getMessage());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver),
                new IntentFilter("clase")
        );
        Log.e("estado  ","onStart");
        if (getStatusSwitch(dataDriver)){
            if (handler != null && runnable_uc != null) {
                handler.removeCallbacksAndMessages(null);
            }
            ejecutarTarea();
        }

        /*if (runnable_uc==null&&handler==null){
            if (getStatusSwitch(dataDriver)){

            }
        }else{
            if (!getStatusSwitch(dataDriver)){
                handler.removeCallbacks(runnable_uc);
                handler.removeCallbacksAndMessages(null);
            }else{
                Log.e("ejecutarTarea" ,"  runable con datos");
            }
        }*/
        mapView.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Log.e("estado  ","onResume");
//        if (handler != null && runnable_uc != null) {
//             handler.post(runnable_uc);
//         }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        Log.e("estado  ","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        Log.e("estado  ","onStop");
        if (handler != null && runnable_uc != null) {
            handler.removeCallbacksAndMessages(null);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Log.e("estado  ","onDestroy");
         if (handler != null && runnable_uc != null) {
            handler.removeCallbacksAndMessages(null);
         }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), "R.string.user_location_permission_explanation", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getActivity()," R.string.user_location_permission_not_granted", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    private void gpsEnable() {
        //Ultima Ubicacion
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {

                        resolvableApiException.startResolutionForResult(getActivity(), GPS);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
    public void clearMessages(){
        FirebaseDatabase.getInstance().getReference().child("chatsFirebase").child(getDriverId_Prefs(prefs)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    dataSnapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mapa_icon_gps:
               /* Intent aaaa = new Intent("subsUnsubs");
                aaaa.putExtra("subs", "notify");
                aaaa.putExtra("subsUnsubs",true);
                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
                broadcaster.sendBroadcast(aaaa);*/
                try {
                    Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

                    Double log=lastKnownLocation.getLongitude();
                    Double lat= lastKnownLocation.getLatitude();
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new com.mapbox.mapboxsdk.geometry.LatLng(lat,log )) // Sets the new camera position
                            .zoom(15)
                            .build(); // Creates a CameraPosition from the builder

                    mapboxMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(position), 1000);

                }catch (Exception e){

                    Log.e("deviceLocation", e.getMessage());
                    Intent intent= new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.dvav_imgbtn_accept:
               /* Intent intent = new Intent("subsUnsubs");
                intent.putExtra("subs", "chat");
                intent.putExtra("status","register");
                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
                broadcaster.sendBroadcast(intent);*/
                clearMessages();
                setTieneNotificacionViaje(dataDriver,false);
                if (getTieneInfo(prefs)){

                     if (mp!=null){
                         mp.stop();
                     }


                    if (countDownTimer!=null){
                        countDownTimer.onFinish();
                    }
                    DatabaseReference mDatabase5 = FirebaseDatabase.getInstance().getReference();
                    final Map<String,Object> mensajeAdmin=new HashMap<>(  );
                    String key;
                    Date date = new Date();
                    DateFormat hourFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    DatabaseReference coment=  FirebaseDatabase.getInstance().getReference().child("chatsFirebase").push();
                    key =coment.getKey();
                    mensajeAdmin.put("mensajeChat","Bienvenidos al Chat");
                    mensajeAdmin.put("nombreChat","Administrador");
                    mensajeAdmin.put("idUserChat","Administrador");
                    mensajeAdmin.put("fechaChat",hourFormat.format(date));
                    dialog_viaje_btn_aceptar.setEnabled(false);
                    Call call=RetrofitClient.getInstance().getApi()
                            .acceptNotification(getDriverId_Prefs(prefs),getViajeId(dataDriver));
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.e("errorFirebase","acceptNotifiacion "+response.code()+""+getDriverId_Prefs(prefs)+"    "+getViajeId(dataDriver)+"  "+response.message()+"  "+response.code());
                            if (response.code()==200 ){
                                Log.e("errorFirebase","enviar datos "+getDriverId_Prefs(prefs)+"   "+getUserId(dataDriver)+"   "+"Viaje-Aceptado"+"   "+"Llegando-a-por-usted"+"   "+"viajeaceptado"+"   "+"");
                                Call comando=RetrofitClient.getInstance().getApi().puStatusTrip(getViajeId(dataDriver),false,true,false,false);
                                comando.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        if (response.isSuccessful()){
                                            Log.e("errorFirebase",response.message());
                                            Call llamada=RetrofitClient.getInstance().getApi()
                                                    .driverTOuser(getDriverId_Prefs(prefs),getUserId(dataDriver),"Viaje-Aceptado","Llegando-a-por-usted","viajeaceptado",key,getViajeId(dataDriver));
                                            llamada.enqueue(new Callback<LoginResponse>() {
                                                @Override
                                                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                                    if (response.isSuccessful()){

                                                                FirebaseDatabase.getInstance().getReference().child("chatsFirebase").child(getDriverId_Prefs(prefs)).push().updateChildren(mensajeAdmin);
                                                                llaveChat(dataDriver,key);
                                                                setTienePasajero(dataDriver,true);
                                                                setEstadoViews(dataDriver,4);
                                                                cuarto_estado();

                                                    }else{

                                                        toastLong(getActivity(),"Error al aceptar en la base de datos");
                                                        Log.e("errorFirebase","driverToUser "+response.code()+" ");
                                                        borrarSharedPreferencesDataDriver(true,2);
                                                        segundo_estado();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call call, Throwable t) {
                                                    Log.e("errorFirebase","driverToUsererror "+t.getMessage()+" ");
                                                    dialog_viaje_btn_aceptar.setEnabled(true);
                                                    borrarSharedPreferencesDataDriver(true,2);
                                                    toastLong(getActivity(),"Error al aceptar");
                                                    segundo_estado();
                                                }
                                            });
                                        }
                                        Log.e("errorFirebase",response.code()+"");
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {

                                    }
                                });

                            }else if(response.code()==404){
                                Log.e("errorFirebase","acceptNotifiacionerror "+response.code()+"");
                                borrarSharedPreferencesDataDriver(true,2);
                                Log.e("estado2","entroenaceppt notificacion false");
                                segundo_estado();
                                toastLong(getActivity(),"El viaje ya fue aceptado");

                            }else if(response.code()==500){
                                borrarSharedPreferencesDataDriver(true,2);
                                Log.e("estado2","entroenaccept notification 500");
                                segundo_estado();
                                toastLong(getActivity(),"El viaje ya fue aceptado");
                                Log.e("errorFirebase","acceptNotifiacionerror "+response.code()+" "+response.message());
                                dialog_viaje_btn_aceptar.setEnabled(true);
                            }else{
                                borrarSharedPreferencesDataDriver(true,2);
                                Log.e("estado2","entroenaccept notification 500");
                                segundo_estado();
                                toastLong(getActivity(),"El viaje ya fue aceptado");
                                Log.e("errorFirebase","acceptNotifiacionerror "+response.code()+" "+response.message());
                                dialog_viaje_btn_aceptar.setEnabled(true);
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.e("errorFirebase", "error acceptNotificationFailre"+t.getMessage());
                            dialog_viaje_btn_aceptar.setEnabled(true);
                            borrarSharedPreferencesDataDriver(true,2);
                            segundo_estado();
                        }
                    });
                }else {
                    toastLong(getActivity(),"Necesita Registrar sus datos, Habra Opciones");
                    Log.e("estado2","entroenbton aceptar else");
                    segundo_estado();
                    borrarSharedPreferencesDataDriver(true,2);
                    //cronometro.removeCallbacks(inicializarCronometro);
                    // tarea_cronometro=null;
                    if (countDownTimer!=null){
                        countDownTimer.onFinish();
                    }


                }



                break;
            case  R.id.dvav_imgbtn_close:
                if (mp!=null){
                    mp.stop();
                }
                if (countDownTimer!=null){
                    countDownTimer.onFinish();
                }
                borrarSharedPreferencesDataDriver(true,2);
                segundo_estado();
                countDownTimer.cancel();
                break;
            case R.id.dialog_fin_viaje_imgbtn_delete:

                crear_dialog_cancelado(2,true);

                break;
            case R.id.dialog_fin_viaje_btn_aceptar:
                switch (getEstadoView(dataDriver)){
                    case 4:
                        //getUserId(prefs)
                        Call llamadaaaa=RetrofitClient.getInstance().getApi()
                                .driverTOuser(getDriverId_Prefs(prefs),getUserId(dataDriver),"Conductor-llego-al-destino","Conductor-esperandolo","notificarllegada",getLlaveChat(dataDriver),getViajeId(dataDriver));
                        llamadaaaa.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                Log.e("evans","conductor llego " +response.code());
                                if (response.isSuccessful()){
                                    setEstadoViews(dataDriver,5);
                                    quinto_estado();

                                }else{
                                    Log.e("error Retrofit22",response.code()+"");
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.e("evans","conductor llego error" + t.getMessage());
                                Log.e("error Retrofit2", t.getMessage());
                                dialog_viaje_btn_aceptar.setEnabled(true);
                            }
                        });
                        break;
                    case 6:
                        Call<Driver> llamadaaaaa= RetrofitClient.getInstance().getApi()
                                .driverTOuser(getDriverId_Prefs(prefs),getUserId(dataDriver),"Iniciando-Viaje","Somos-Evans","viajeiniciado",getLlaveChat(dataDriver),getViajeId(dataDriver));
                        llamadaaaaa.enqueue(new Callback<Driver>() {
                            @Override
                            public void onResponse(Call<Driver> call, Response<Driver> response) {
                                Log.e("evans","iniciando viaje " +response.code());
                                if (response.isSuccessful()){
                                    setEstadoViews(dataDriver,7);
                                    septimo_estado();
                                    Call comando=RetrofitClient.getInstance().getApi().puStatusTrip(getViajeId(dataDriver),false,true,true,false);
                                    comando.enqueue(new Callback() {
                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            if (response.isSuccessful()){
                                                Log.e("Viaje aceptado",response.message());
                                            }
                                            Log.e("Viaje aceptado",response.code()+"");
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<Driver> call, Throwable t) {
                                Log.e("evans","iniciando viaje error" +t.getMessage());
                            }
                        });

                        break;
                    case 8:
                        clearMessages();
                        Call<Driver> pagoExitoso= RetrofitClient.getInstance().getApi()
                                .driverTOuser(getDriverId_Prefs(prefs),getUserId(dataDriver),"Evans-le-desea ","Un-buen-dia.-Gracias","pagoexitoso",getLlaveChat(dataDriver),getViajeId(dataDriver));
                        pagoExitoso.enqueue(new Callback<Driver>() {
                            @Override
                            public void onResponse(Call<Driver> call, Response<Driver> response) {
                                Log.e("evans","pago exitoso" +response.code());
                                if (response.isSuccessful()){
                                    if (!(getApiWebVersion(prefs).equals(getVersionApp(getContext())))){
                                        getViewUpdateVersion(getActivity(),getContext());
                                    }
                                    if(getStatusSwitch(dataDriver)){
                                        Log.e("estado2","entroenpago exitoso");

                                       changeEstatusTrip();
                                        segundo_estado();

                                    }else {
                                        changeEstatusTrip();
                                        primer_estado();

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Driver> call, Throwable t) {
                                Log.e("evans","pago exitoso error " +t.getMessage());
                            }
                        });

                        break;
                    default:
                        if (getEstadoView(dataDriver)==5){
                            forzarpaso6();
                        }else if(getEstadoView(dataDriver)<8){
                            setEstadoViews(dataDriver,getEstadoView(dataDriver)+1);
                            comprobarestadoViews();
                        }
                        break;
                }
                break;
            case R.id.dialog_fin_viaje_imgbtn_message:
                dialog_fin_viaje_imgbtn_message_notify.setVisibility(View.GONE);
                comunicateFrag.updateNotificatones(true,"mapaInicio",new Fragment_chat());
                //comunicateFrag.updateNotificatones(true,"mapaInicio",new Fragment_chat());
                break;
        }
    }

    private void changeEstatusTrip() {
        Call comando=RetrofitClient.getInstance().getApi().puStatusTrip(getViajeId(dataDriver),false,true,true,true);
        comando.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    Log.e("Viaje aceptado",response.message());
                    //viajeterminado();
                }
                Log.e("Viaje aceptado",response.code()+"");
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void viajeterminado() {
        Call<Driver> llamada=RetrofitClient.getInstance().getApi()
                .puStatusTrip(getViajeId(dataDriver),false,true,true,true);
        llamada.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()){
                    Log.e("viaje","germinado");
                }
            }
            /*
             * sadasdsaasdasdsadasdsad    sadsadasdasd
             *  sadsadsad
             * sadsadsad
             * sadsad
             * hhhsadas
             * sadsadsa
             * sadsadsa
             * asasdsad
             * sadsadsa
             *
             * */
            @Override
            public void onFailure(Call<Driver> call, Throwable t) {

            }
        });
    }

    private void crear_dialog_cancelado(int estado,boolean chaecked) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Call comando=RetrofitClient.getInstance().getApi().puStatusTrip(getViajeId(dataDriver),true,false,false,false);
                        comando.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()){
                                    clearMessages();
                                    Log.e("chat ",getApiWebVersion(prefs)+"  "+getVersionApp(getContext()));
                                    if (!(getApiWebVersion(prefs).equals(getVersionApp(getContext())))){
                                        getViewUpdateVersion(getActivity(),getContext());
                                    }
                                    Log.e("Viaje aceptado",response.message());
                                    cancel_viaje_noti(estado,chaecked);
                                    setChatJson(dataDriver,"nulo");
                                   /* Intent intent = new Intent("subsUnsubs");
                                    intent.putExtra("subs", "chat");
                                    intent.putExtra("status","false");
                                    LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
                                    broadcaster.sendBroadcast(intent);*/
                                }
                                Log.e("Viaje aceptado",response.code()+"");
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Log.e("Viaje aceptado",t.getMessage());
                            }
                        });

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Esta seguro de querer cancelar el viaje?").setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void cancel_viaje_noti(int estado,boolean chaecked) {
        Call llamada= RetrofitClient.getInstance().getApi().driverTOuser(getDriverId_Prefs(prefs),
                getUserId(dataDriver),"Viaje-Cancelado","Taxy-Cancelo-Viaje","viajecancelado",getLlaveChat(dataDriver),getViajeId(dataDriver));
        llamada.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    Log.e("Mensaje Cancelad","");
                    setChatJson(dataDriver,"nulo");
                    /*Intent intent = new Intent("subsUnsubs");
                    intent.putExtra("subs", "chat");
                    intent.putExtra("status","false");
                    LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
                    broadcaster.sendBroadcast(intent);*/
                    borrarSharedPreferencesDataDriver(chaecked,estado);

                    if (estado==1){

                        primer_estado();
                    }else {
                        Log.e("estado2","cancel notificacion");
                        segundo_estado();
                    }

                }
                Log.e("Mensaje Cancelad",response.code()+"");

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Mensaje Cancelad",t.getMessage()+"");
            }
        });
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;


        geo = new Geocoder(getContext(), Locale.getDefault());
        mapboxMap.getUiSettings().setCompassEnabled(true);
        //this.mapboxMap.getUiSettings().setCompassMargins(0,0,0,120);
        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                // addDestinationIconSymbolLayer(style);



                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (currentRoute==null){
                            currentRoute=DirectionsRoute.fromJson(getCurrentRoute(dataDriver));
                        }
                        // Log.e("dibujarmapa","on click "+currentRoute.toString());
                        if(activiy==null){
                            activiy=getActivity();
                        }
                        if (activiy!=null){
                            try{
                                mapView.onPause();
                                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                        .directionsRoute(currentRoute)
                                        .build();
                                NavigationLauncher.startNavigation(activiy, options);
                                // mapView.onDestroy();
                            }catch (Exception e){

                            }
                        }



                    }
                });

            }

        });
        comprobarestadoViews();








    }

    private void initSource(@NonNull Style loadedMapStyle, Point origen, Point destination) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID,
                FeatureCollection.fromFeatures(new Feature[] {})));

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {
                Feature.fromGeometry(Point.fromLngLat(origen.longitude(), origen.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))}));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    /**
     * Add the route and marker icon layers to the map
     */
    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);
//
////// Add the red marker icon image to the map
//        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
//                getResources().getDrawable(R.drawable.marcador50x20)));
//
//// Add the red marker icon SymbolLayer to the map
//        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
//                iconImage(RED_PIN_ICON_ID),
//                iconIgnorePlacement(true),
//                iconAllowOverlap(true),
//                iconOffset(new Float[] {0f, -9f})));
    }







    /* private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {

         }
     };*/
    private void borrarSharedPreferencesDataDriver(Boolean checkedSwitch,int estado){
        //limpiarNotify(getActivity());
        Log.e("borrardo","entro en borrar");
        SharedPreferences.Editor editor = dataDriver.edit();
        editor.clear();
        editor.apply();
        setStatusSwitch(dataDriver,checkedSwitch);
        setEstadoViews(dataDriver,estado);
    }
    private void RemoveLayersMapbox(){
        if (mapboxMap!=null){
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {


                    Layer layer = style.getLayer(ROUTE_LAYER_ID );
                    //  Layer layer3 = style.getLayer(ICON_LAYER_ID );
                    Source layer1 = style.getSource(ROUTE_SOURCE_ID );
                    Source layer2 = style.getSource(ICON_SOURCE_ID );
                    //  style.removeImage(RED_PIN_ICON_ID);

                    if (layer != null) {
                        style.removeLayer(layer);
                    }
                    if (layer1 != null) {
                        style.removeSource(layer1);
                    }
                    if (layer2 != null) {
                        style.removeSource(layer2);
                    }
                   /* if (layer3!=null){
                        style.removeLayer(layer3);
                    }*/
                    currentRoute=null;
                }
            });
        }
    }
    private void primer_estado(){

        removeDataMaps();
        borrarSharedPreferencesDataDriver(false,1);
        Log.e("borrardo","primerStadp");
        dialog_viaje_btn_aceptar.setEnabled(true);

        dialog_txt_true.setVisibility(View.GONE);
        if(!getaccountActivate(prefs)){
            dialog_txt_estado.setText("Registra tus datos y ");
            dialog_txt_true.setVisibility(View.VISIBLE);
            dialog_txt_true.setText("Espera activacion de la cuenta");
        }else{
            dialog_txt_true.setVisibility(View.GONE);
            dialog_txt_estado.setText("Estás Desconectado");
        }
        button.setVisibility(View.GONE);
        Log.e("Estado","Apagado");
        dialog_estado.setVisibility(View.VISIBLE);
        dialog_viaje.setVisibility(View.GONE);
        dialog_fin_viaje.setVisibility(View.GONE);
        Log.e("vistas","entra la primera vista");
    }
    private void segundo_estado(){

        Log.e("getData","entro ona map Ready \n"+getdataNotification_noti(dataDriver));
        if (!getdataNotification_noti(dataDriver).equals("nulo")){
            Log.e("getData","entro get Data notifty");
            ejecutar_acciones_data_notificacion(getdataNotification_noti(dataDriver));
        }else{
            Log.e("borrardo","segundo estadp");
            removeDataMaps();
            borrarSharedPreferencesDataDriver(true,2);
        }

        dialog_viaje_btn_aceptar.setEnabled(true);
        dialog_estado.setVisibility(View.VISIBLE);
        dialog_viaje.setVisibility(View.GONE);
        dialog_fin_viaje.setVisibility(View.GONE);
        dialog_txt_true.setVisibility(View.VISIBLE);
        dialog_txt_estado.setText("Estás Conectado");
        dialog_txt_true.setText("Buscando viajes...");
        button.setVisibility(View.GONE);
        Log.e("Estado","Encendido"+getEstadoView(dataDriver));
        Log.e("vistas","entra la segunda vista");
    }
    private void removeDataMaps(){


        currentRoute=null;
        if(origen!=null){
            mapboxMap.removeMarker(origen);
        }
        if(destino!=null){
            mapboxMap.removeMarker(destino);
        }

        /*try{

            //mapboxMap.clear();
           //mapboxMap.removeAnnotations();
            navigationMapRoute.removeRoute();
        }catch (Exception e){

        }*/
        RemoveLayersMapbox();
        if (navigationMapRoute!=null){
            navigationMapRoute.removeRoute();
        }

    }
    private void DibujarLineas(Point origin, Point destination){
        try {
            if (getOrigenLat(dataDriver).equals("nulo")||getOrigenLong(dataDriver).equals("nulo")||
                    getDestinoLat(dataDriver).equals("nulo")||getDestinoLong(dataDriver).equals("nulo") ){
                toastLong(getActivity(),"Uno de los datos no fue guardado correctamente del viaje.");
            }else{
                getRoute(origin, destination);
            }
        }catch (Exception e){
            Log.e("errordl",e.getMessage());
        }

    }
    private void tercer_estado(String distancia, String duracion ) {
        try {
            mp = MediaPlayer.create(getContext(), R.raw.sound);
            mp.setLooping(true);
            mp.start();
        }catch (Exception e){

        }
        ejecutarCronometro();
        if (countDownTimer != null) {
            countDownTimer.start();
        }
        dialog_viaje_txt_tiempo.setText(duracion);
        dialog_viaje_txt_distance.setText(distancia);
        dialog_viaje_btn_aceptar.setEnabled(true);
        dialog_estado.setVisibility(View.GONE);
        dialog_viaje.setVisibility(View.VISIBLE);
        dialog_fin_viaje.setVisibility(View.GONE);
        if (!getPriceDiscount(dataDriver).equals("0.0")){
            dvav_img_cupon.setVisibility(View.VISIBLE);
            dvav_img_money.setVisibility(View.VISIBLE);
            dvav_img_add.setVisibility(View.VISIBLE);
        }else{
            dvav_img_cupon.setVisibility(View.GONE);
            dvav_img_money.setVisibility(View.VISIBLE);
            dvav_img_add.setVisibility(View.GONE);
        }
        dialog_viaje_txt_nombre.setText(getStartAddress(dataDriver));
        dialog_viaje_txt_tiempo.setText(getEndAddress(dataDriver));
    }
    private void ejecutarCronometro() {

        countDownTimer = new CountDownTimer(24000, 1000) {
            public void onTick(long millisUntilFinished) {

                segundos=String.format(Locale.getDefault(), "%d", millisUntilFinished / 1000L);
                // dialog_viaje_tiempo_espera.setText(segundos);

            }
            public void onFinish() {
                if (Integer.parseInt(segundos)>0){
                    if (mp!=null){
                        mp.stop();
                    }
                    setCronometroStop(dataDriver,true);
                }else {
                    if (mp!=null){
                        mp.stop();
                    }
                    Log.d("MainActivity22","finnn"+segundos+getCronometroStop(dataDriver));
                    if (getCronometroStop(dataDriver)){
                        countDownTimer.cancel();
                    }else {
                        Log.e("estado2","entroencronometro false");
                        segundo_estado();
                        countDownTimer.cancel();
                    }

                }


            }

        };
    }

    private void cuarto_estado(){

        dialog_estado.setVisibility(View.GONE);
        dialog_viaje.setVisibility(View.GONE);
        dialog_fin_viaje.setVisibility(View.VISIBLE);
        dialog_fin_viaje_txt_destino.setText("Destino: "+getEndAddress(dataDriver));
        dialog_fin_viaje_txt_origen.setText("Origen: "+getStartAddress(dataDriver));
        dialog_fin_viaje_txt_titulo.setText("Llegando al Destino");
        dialog_fin_viaje_btn_aceptar.setText("Llegue al Destino");
        dialog_fin_viaje_btn_aceptar.setEnabled(true);
        dialog_fin_viaje_btn_aceptar.setText("NOTIFICAR LLEGADA");
        dialog_fin_viaje_btn_aceptar.setEnabled(true);
        dialog_fin_viaje_txt_precio_coupon.setText(getPriceDiscount(dataDriver)+"PEN");
        dialog_fin_viaje_txt_precio.setText(getPrice(dataDriver)+"PEN");
        // dialog_fin_viaje_imgbtn_delete.setVisibility(View.GONE);
        // dialog_fin_viaje_imgbtn_message.setVisibility(View.VISIBLE);
        llenarRuta();
    }
    private void quinto_estado(){


        dialog_estado.setVisibility(View.GONE);
        dialog_viaje.setVisibility(View.GONE);
        dialog_fin_viaje.setVisibility(View.VISIBLE);
        dialog_fin_viaje_txt_destino.setText("Destino: "+getEndAddress(dataDriver));
        dialog_fin_viaje_txt_origen.setText("Origen: "+getStartAddress(dataDriver));
        dialog_fin_viaje_btn_aceptar.setText("CONTINUAR");
        dialog_fin_viaje_btn_aceptar.setEnabled(true);
        dialog_fin_viaje_txt_precio_coupon.setText(getPriceDiscount(dataDriver)+"PEN");
        dialog_fin_viaje_txt_precio.setText(getPrice(dataDriver)+"PEN");
        llenarRuta();
        //  dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        //  dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
    }
    private void sexto_estado(){
        dialog_estado.setVisibility(View.GONE);
        dialog_viaje.setVisibility(View.GONE);
        dialog_fin_viaje.setVisibility(View.VISIBLE);
        dialog_fin_viaje_txt_destino.setText("Destino: "+getEndAddress(dataDriver));
        dialog_fin_viaje_txt_origen.setText("Origen: "+getStartAddress(dataDriver));
        dialog_fin_viaje_txt_titulo.setText("ESTA A BORDO");
        dialog_fin_viaje_btn_aceptar.setText("Iniciar Viaje");
        dialog_fin_viaje_txt_precio_coupon.setText(getPriceDiscount(dataDriver)+"PEN");
        dialog_fin_viaje_txt_precio.setText(getPrice(dataDriver)+"PEN");
        dialog_fin_viaje_btn_aceptar.setEnabled(true);
        // dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        //dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
        llenarRuta();
        Log.e("borrardo","forzar paso 6" +getEstadoView(dataDriver));
    }
    private  void septimo_estado(){


        dialog_estado.setVisibility(View.GONE);
        dialog_viaje.setVisibility(View.GONE);
        dialog_fin_viaje.setVisibility(View.VISIBLE);
        dialog_fin_viaje_txt_destino.setText("Destino: "+getEndAddress(dataDriver));
        dialog_fin_viaje_txt_origen.setText("Origen: "+getStartAddress(dataDriver));
        dialog_fin_viaje_txt_titulo.setText("Esperando que usuario Finalice Viaje");
        dialog_fin_viaje_btn_aceptar.setText("CONTINUAR");
        dialog_fin_viaje_btn_aceptar.setEnabled(true);
        dialog_fin_viaje_txt_precio_coupon.setText(getPriceDiscount(dataDriver)+"PEN");
        dialog_fin_viaje_txt_precio.setText(getPrice(dataDriver)+"PEN");
        llenarRuta();
        // dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        //dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
    }
    private void octavo_estado(){

        dialog_estado.setVisibility(View.GONE);
        dialog_viaje.setVisibility(View.GONE);
        dialog_fin_viaje.setVisibility(View.VISIBLE);
        dialog_fin_viaje_txt_destino.setText("Destino: "+getEndAddress(dataDriver));
        dialog_fin_viaje_txt_origen.setText("Origen: "+getStartAddress(dataDriver));
        dialog_fin_viaje_txt_titulo.setText("Llegó al Destino");
        dialog_fin_viaje_btn_aceptar.setText("Pago Existoso");
        dialog_fin_viaje_btn_aceptar.setEnabled(true);
        dialog_fin_viaje_txt_precio_coupon.setText(getPriceDiscount(dataDriver)+"PEN");
        dialog_fin_viaje_txt_precio.setText(getPrice(dataDriver)+"PEN");
        llenarRuta();
        // dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        //dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
    }
    private void comprobarestadoViews() {
        Log.e("borrardo","entro comprobar Views" +getEstadoView(dataDriver)+ "   "+getStatusSwitch(dataDriver));

        switch (getEstadoView(dataDriver)){
            case 1:
                primer_estado();
                break;
            case 2:
                Log.e("estado2","entroencromprobar views 2"+getEstadoView(dataDriver));
                segundo_estado();
                break;
            case 3:

                borrarSharedPreferencesDataDriver(true,2);
                Log.e("estado2","entroencromprobar views");
                segundo_estado();
                break;
            case 4:
                cuarto_estado();
                break;
            case 5:
                quinto_estado();
                break;
            case 6:
                sexto_estado();
                break;
            case 7:
                septimo_estado();
                break;
            case 8:
                octavo_estado();
                break;
        }
    }
    //pRUEBAS DE INTERNET
    private boolean isNetDisponible() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

            return (actNetInfo != null && actNetInfo.isConnected());
        }catch (Exception e){
            return true;
        }

    }
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return true;
        }

    }

}
