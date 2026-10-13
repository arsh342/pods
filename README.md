# Pods for Android

Pods is an Android companion app for managing supported AirPods features, with an interface tailored for Samsung One UI-style devices.

Pods is an independent community project. It is not affiliated with or endorsed by Apple or Samsung. AirPods is a trademark of Apple Inc.

## Features

Available controls depend on the AirPods model, firmware, Android version, and Bluetooth capabilities reported by the device. Unsupported controls are hidden.

- Connect to AirPods and reconnect to the last saved device.
- View earbud and case battery levels in the app, with a home-screen battery widget and Quick Settings noise-control tile.
- Receive low-battery alerts when an earbud or case reaches 10% or 5%.
- Change supported listening modes, including noise cancellation, transparency, adaptive audio, and off mode where available.
- Configure press-and-hold actions, call controls, microphone selection, automatic ear detection, and automatic connection.
- Adjust supported audio and accessibility features, including conversational awareness, adaptive volume, loud sound reduction, hearing protection, hearing aid, and head gestures.
- Configure optimized charging and supported sleep detection settings.
- Open Android or Samsung Sound quality and effects settings for the system equalizer. Pods does not include a custom equalizer or process audio.
- Configure supported connection takeover behavior and advanced options in App Settings.
- View app and open-source license information.

Some hearing and accessibility features require compatible AirPods and firmware. The optional “Act as an Apple device” setting appears only when a compatible Xposed environment is detected. Its availability and behavior depend on the device and setup.

## Requirements and permissions

- Android 13 (API 33) or newer for the FOSS build.
- The Play build currently requires Android 16 (API 36) or newer.
- Bluetooth-capable Android device and compatible AirPods. Feature support varies by model and firmware.

On first launch, the app opens directly to the main screen and requests the Android permissions it needs. These can include nearby-device Bluetooth access, notifications, and phone-state/call permissions for related controls. Grant only the permissions you want the corresponding features to use.

## Build

Open the `android` directory in Android Studio, or build from a terminal with JDK 21 and the Android SDK installed. The project uses Android SDK 37 and NDK 30.0.14904198.

From the `android` directory:

```bash
# FOSS debug APK (Android 13+)
./gradlew :app:assembleFossDebug

# FOSS release APK (Android 13+)
./gradlew :app:assembleFossRelease

# Play release bundle (Android 16+)
./gradlew :app:bundlePlayRelease

# Collect the FOSS APKs and Play bundle in ../release/
./gradlew :app:packageReleaseArtifacts
```

Build outputs are written under `app/build/outputs/`. The collected artifacts are written to the repository's `release/` directory.

### Release signing

Release builds use the signing values in `android/local.properties`. This file and `.jks` keystores are ignored by Git. Keep the keystore and its passwords backed up securely; never commit them.

The required properties are:

```properties
RELEASE_STORE_FILE=/absolute/path/to/pods-upload.jks
RELEASE_STORE_PASSWORD=your-store-password
RELEASE_KEY_ALIAS=pods-upload
RELEASE_KEY_PASSWORD=your-key-password
```

For a new Play Store listing, upload the signed `.aab` and enroll in Play App Signing. Play Console's app-signing certificate is the fingerprint used by services that verify the app installed from Google Play; the local keystore certificate is the upload key. For an existing listing, preserve its package name and follow its current Play signing setup.

## Project layout

- `android/app/src/main/java/` — Android app and Bluetooth implementation.
- `android/app/src/main/res/` — app resources and launcher icons.
- `android/app/build.gradle.kts` — FOSS and Play variants, signing configuration, and release artifact tasks.
- `docs/` — AirPods protocol notes and reverse-engineering references.

## License

The app is licensed under the [GNU General Public License v3 or later](https://www.gnu.org/licenses/gpl-3.0.html). Source files retain their copyright notices; third-party notices are available in the in-app Open Source Licenses screen.

## License

Pods is distributed under the [GNU General Public License v3 or later](LICENSE). Source copyright notices and the in-app Open Source Licenses screen provide copyright and third-party license details.
# pods
