# IMPORTANT

[ODataJClient](https://github.com/MSOpenTech/ODataJClient) source code was donated to [Apache Olingo](http://olingo.apache.org): an updated version of this sample Android application, that relies upon Apache Olingo, is [available](https://github.com/Tirasa/olingoClientOnAndroidSample).

### How to test

1. download [Android SDK](http://developer.android.com/sdk/index.html) and unpack somewhere
2. `git clone https://github.com/Tirasa/ODataJClientOnAndroidSample.git`
3. `cd ODataJClientOnAndroidSample`
4. change the value of `android.sdk.path` property in `pom.xml`
5. build and deploy
 1. `mvn clean package` if you want to manually deploy `target/odatajclient-android.apk` to any device
 2. `mvn clean package android:deploy android:run` to automatically deploy and launch on all attached devices
