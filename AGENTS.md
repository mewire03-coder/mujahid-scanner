# Mujahid Scanner project

## Architecture

The user supplied a static Urdu barcode-scanning application in a ZIP and requested Android APK conversion. The web application was retained and wrapped in Capacitor. No APK was compiled because the follow-up did not authorize Android builds. Do not describe the source archive as an APK or claim that Android device testing has passed.

## Key directories

- `www/`: Netlify publish directory and Capacitor web assets. `index.html` is the original scanner with narrow Android integration changes; `android.html` explains packaging and links to the source ZIP.
- `www/vendor/`: Pinned, locally bundled ZXing decoder and its license.
- `www/downloads/`: Generated Android source archive, not native binaries.
- `android/`: Capacitor-generated Gradle project. The custom `MasterExportPlugin` uses Android's Storage Access Framework for user-selected JSON exports.
- `scripts/package-android-source.mjs`: Creates a source-only ZIP, excluding downloads, native build outputs, SDK paths, and signing files.
- `.github/workflows/android-apk.yml`: Manually triggered Android debug APK workflow; it does not run on push.

## Conventions

Keep the existing scanner's Urdu text, barcode layout, master data, and checksum rules intact. Use plain JavaScript and CSS for the web app and Java for native integration. Do not introduce a web framework merely to package Android. Keep Capacitor dependencies at matching versions and refresh `package-lock.json` when changing them. Preserve third-party licenses. Do not add inline comments to hand-written code.

## Non-obvious decisions

`webDir` is `www` and the Android app loads bundled assets, not a remote site. This retains the HTTPS local origin provided by Capacitor for camera access and allows bundled decoding. Android camera access uses Capacitor's existing WebChromeClient; camera hardware is optional so manual decoding remains available. Do not enable mixed content or unrestricted external navigation.

The custom native export plugin is registered before `BridgeActivity.onCreate`. Browser blob downloads do not suffice for Android export. No microphone or broad storage permission is needed. The browser service worker is disabled inside Capacitor to avoid serving stale packaged assets. Source downloads bypass the browser service worker.

The original browser-local persistence was retained as part of the uploaded application's behavior, not replaced with a new backend. Any future shared/server-side persistence must use the site's Netlify storage primitives. App and website data are separate, and uninstalling clears Android-local data.

After changing `www/`, synchronize Android assets before an authorized APK build. Refresh the source archive after all edits. Do not include `.netlify/`, credentials, signing keys, SDK locations, dependency trees, or build caches in downloads. Java 21 and Android SDK 36 are required by the generated Capacitor project. Native compilation and physical-device checks remain necessary before distribution.
