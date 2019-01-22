package alv.app.utpl.edu.ec.appidtracker01;

import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

class GpsUbicacion implements LocationListener {
    MainActivity mainActivity;
    public MainActivity getMainActivity(){
        return mainActivity;
    }
    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(Location location) {
        String ub = location.getLatitude()+"--"+location.getLongitude()+"\n--"+location.getSpeed()+"---"+location.getSpeedAccuracyMetersPerSecond();
        mainActivity.txtGps.setText(ub);
        this.mainActivity.gpsLoc(location);
        speed=((location.getSpeed()*3600)/1000);
        String sp = String.format("%.2f",speed);


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
}
