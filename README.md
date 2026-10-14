# Mujahid Scanner

Mujahid Scanner decodes the uploaded application's 30-digit production barcodes for Mujahid Enamel Wire. The original Urdu interface, camera scanning, manual decoding, master data imports, and scan history were retained. A Capacitor Android project wraps the scanner as a native application, with bundled ZXing decoding and Android file-picker support for master data exports.

## Packaging status

**No compiled APK was produced in this session.** Android compilation was not authorized in the follow-up answer. `www/downloads/mujahid-scanner-android-source.zip` contains the Android project, not an installable Android package. Native compilation and physical-device testing remain outstanding. Renaming the ZIP to `.apk` will not make it installable.

## Technologies and requirements

- Static HTML, CSS, and JavaScript, hosted by Netlify without a web build.
- Capacitor 8.5.3 and ZXing 0.21.3.
- Node.js 22 or later, Java 21, Android SDK platform 36, and Android SDK build tools 36.0.0 for Android builds.
- Android 7.0 (API 24) or later with an up-to-date Android System WebView.

## Run locally

Run `npm ci` to install pinned dependencies. To serve the browser application locally, run `npx netlify dev --dir www --port 8889`. Camera capture requires localhost or HTTPS; phones opening a computer's plain HTTP network address cannot use the browser camera.

## Create an APK using Android Studio

1. Extract the Android source archive, or use this repository directly.
2. Install Android Studio, Java 21, Android SDK platform 36, and build tools 36.0.0.
3. Run `npm ci` and then `npm run android:sync` from the project root.
4. Run `npm run android:open` to open the native project in Android Studio. Allow its Gradle sync to finish.
5. Choose **Build → Generate App Bundles or APKs → Generate APKs**. Depending on Android Studio's version, this may be labelled **Build APK(s)**.
6. Find the installable debug APK at `android/app/build/outputs/apk/debug/app-debug.apk`.
7. Transfer it to an Android phone, allow installation from that trusted source, install it, and grant camera access when prompted.

Alternatively, run `cd android && ./gradlew assembleDebug` after installing the prerequisites and syncing. On Windows use `gradlew.bat assembleDebug`.

## Create an APK using GitHub Actions

Upload the project, including `.github/workflows/android-apk.yml`, to a GitHub repository. Under **Actions**, choose **Package Android APK → Run workflow**. Once the job succeeds, download the `mujahid-scanner-apk` artifact and extract `app-debug.apk`. The workflow runs only when manually requested; it does not run on push.

The generated debug APK is for sideloading and testing, not Play Store distribution. Release distribution requires a separately managed signing key and release configuration. Retain the same signing key for future updates; new debug signing keys can require uninstalling an earlier debug application. Uninstallation removes its local data.

## Device checks before distribution

Confirm camera permission approval and denial, Data Matrix and QR scanning, 30-digit manual decoding, CSV/JSON imports, JSON exports, application backgrounding, offline startup, and persistence after closing and reopening on an actual phone. No Android build or runtime validation was performed here.

## Data and offline behavior

The uploaded scanner's existing browser-local master data and history behavior was preserved; no server-side storage, account system, or shared database was added. Android and web installations have separate local data. Export master data before clearing app data or uninstalling. The application does not sync between devices. This conversion does not change the original barcode checksum rules or data layout.

The Android application bundles the scanner, icons, and decoding library, so scanning does not depend on downloading ZXing at runtime. The web application caches its scanner assets after the first successful visit. Native offline behavior still needs device verification.

## Updating the package

Edit `www/` for the scanner, then run `npm run android:sync` before packaging. Update Android native integration in `android/app/src/main/`. Run `npm run package:source` to refresh the downloadable source ZIP; this only archives files and does not compile an APK. Netlify publishes `www/` with no build command.

The original application assets were provided by the user. The bundled ZXing license is included at `www/vendor/ZXING-LICENSE`.
