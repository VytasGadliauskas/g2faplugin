# 
2023-06    
OpenVPN plugin :

Simple plugin to have single user Google 2FA autentification to OpenVpn

APPLICATION folder have wraped jar versions of apliaction
     |--> g2faplugin.sh     -- linux version (first line java path, after download set chmod +x g2faplugin.sh)
     |--> g2faplugin.exe    -- windows (nead set JAVA_HOME environment variable picture2.png) 


copy application to OpenVpn  directory.
in openvpn config set:
auth-user-pass-verify g2faplugin.[exe|sh] via-env

run g2faplugin.[exe|sh] --key email name 
picture2.png

use QR code or Key in Google Autentificator
https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=en_US



Use OpenVpn client:
username can be any password Google 2FA Code

report bugs suggestions :)
mail.: vytasgadliauskas@gmail.com



