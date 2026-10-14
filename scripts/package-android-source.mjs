import { mkdirSync, rmSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { spawnSync } from 'node:child_process';

const projectRoot = fileURLToPath(new URL('../', import.meta.url));
const downloadDirectory = `${projectRoot}www/downloads`;
const archivePath = `${downloadDirectory}/mujahid-scanner-android-source.zip`;

mkdirSync(downloadDirectory, { recursive: true });
rmSync(archivePath, { force: true });

const result = spawnSync('zip', [
  '-q', '-r', archivePath,
  'android', 'www', 'scripts', '.github', '.gitignore',
  'package.json', 'package-lock.json', 'capacitor.config.json',
  'netlify.toml', 'README.md', 'AGENTS.md',
  '-x', 'www/downloads/*', 'android/.gradle/*', 'android/local.properties',
  'android/*/build/*', 'android/build/*', 'android/app/src/main/assets/public/*',
  'android/.idea/*', '*.jks', '*.keystore', '*.apk',
], { cwd: projectRoot, stdio: 'inherit' });

if (result.error) throw result.error;
if (result.status !== 0) throw new Error('Android source archive could not be created.');
console.log('Created www/downloads/mujahid-scanner-android-source.zip (source code, not an APK).');
