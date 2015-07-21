package net.whend.soodal.whend.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
    
	@Override
    public IBinder onBind(Intent intent) {
        CustomAuthenticator authenticator = new CustomAuthenticator(this.getApplicationContext());
        return authenticator.getIBinder();
        
    }
}