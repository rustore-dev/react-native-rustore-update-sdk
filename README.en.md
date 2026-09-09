<div align="left" style="margin:0 0 14px 0;">

  <span style="display:inline-block;
               padding:.28rem .6rem;
               border:1px solid rgba(0,0,0,.18);
               border-radius:10px 0 0 10px;
               font-weight:400;
               font-size:12px;
               letter-spacing:.06em;
               color:#111827;
               background:linear-gradient(180deg,#ffffff,#e9edf2);
               box-shadow:0 1px 0 rgba(0,0,0,.06);">
    [RU][ru]
  </span><span style="display:inline-block;
               margin-left:-1px;
               padding:.28rem .6rem;
               border:1px solid rgba(0,0,0,.14);
               border-radius:0 10px 10px 0;
               font-weight:400;
               font-size:12px;
               letter-spacing:.06em;
               background:linear-gradient(180deg,#f3f4f6,#ffffff);
               box-shadow:inset 0 2px 6px rgba(0,0,0,.10);">
    EN
  </span>

</div>

# react-native-rustore-update

React Native RuStore SDK for application updates

## General

RuStore In-app updates SDK helps keep your application up to date on the user's device.

When users maintain their applications in an updated state, they can try out new features and take advantage of performance improvements and bug fixes.

You can use the RuStore In-app updates SDK to display the application update process, which provides background download and installation with status control. The user will be able to use your application during the update download.

### Implementation Example
To learn how to correctly integrate RuStore In-app updates, it is recommended to review the example application in the `example` folder.

### Conditions for Correct SDK Operation

For the RuStore In-app updates SDK to function properly, the following conditions must be met:

- Android OS version 7.0 or higher.
- RuStore must be installed on the user's device.
- The RuStoreApp version on the user's device must be current.
- The RuStore application must be allowed to install apps.

## Integration into Project

To add the package to your project, run the command

```sh
// HTTPS
npm install git+https://git@gitflic.ru/project/rustore/react-native-rustore-update-sdk.git

// SSH
npm install git+ssh://git@gitflic.ru/project/rustore/react-native-rustore-update-sdk.git
```

The native Android SDK is downloaded from the [RuStore Maven repository](https://nexus-external.rustore.ru/repository/maven-rustore-exposed). The repository is configured automatically in [`android/build.gradle`](android/build.gradle).

## Update Request
To use the update mechanism, initialize the SDK first:
```typescript
RustoreUpdateClient.init();
```

## Checking for Updates

Before requesting an update, check if an update is available for your application. To check for updates, call the `getAppUpdateInfo()` method. When calling this method, the following conditions are verified:

- The user's device has the latest version of RuStore installed.
- The user and the application should not be blocked in RuStore.
- RuStore is allowed to install applications.
- The user is authenticated in RuStore.

In response to this method, you will receive an `appUpdateInfo` object that will contain information about whether an update is needed.

```typescript
try {
  const appUpdateInfo = await RustoreUpdateClient.getAppUpdateInfo();
  console.log(appUpdateInfo);
} catch (err) {
  console.log(err);
}
```

The `appUpdateInfo` object contains a set of parameters necessary to determine the availability of an update:

```typescript
enum InstallStatus {
  UNKNOWN = 0,
  DOWNLOADED = 1,
  DOWNLOADING = 2,
  FAILED = 3,
  INSTALLING = 4,
  PENDING = 5,
}

enum UpdateAvailability {
  UNKNOWN = 0,
  UPDATE_NOT_AVAILABLE = 1,
  UPDATE_AVAILABLE = 2,
  DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS = 3,
}

interface AppUpdateInfo {
  updatedAt: string;
  packageName: string;
  updatePriority: number;
  updateAvailability: UpdateAvailability;
  availableVersionCode: number;
  installStatus: InstallStatus;
}
```

- `updatedAt` - update date
- `packageName` - package name
- `updatePriority` - update priority
- `availableVersionCode` - update code
- `updateAvailability` - update availability:
  - `UNKNOWN` - default status.
  - `UPDATE_NOT_AVAILABLE` - no update needed.
  - `UPDATE_AVAILABLE` - update needs to be downloaded or already downloaded to the user's device.
  - `DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS` - update is currently downloading or installation has started.
- `installStatus` - update installation status, if the user is installing the update at the moment:
  - `UNKNOWN` - default.
  - `DOWNLOADED` - downloaded.
  - `DOWNLOADING` - downloading.
  - `FAILED` - error.
  - `INSTALLING` - installing.
  - `PENDING` - pending.

Downloading an update is only possible if the `updateAvailability` field contains the value `UpdateAvailability.UPDATE_AVAILABLE`.

## Downloading the Update

After confirming update availability, you can request the user to download the update, but first you need to start listening to the update download status using the `eventEmitter.addListener` method:

```typescript
enum InstallStatus {
  UNKNOWN = 0,
  DOWNLOADED = 1,
  DOWNLOADING = 2,
  FAILED = 3,
  INSTALLING = 4,
  PENDING = 5,
}

interface InstallState {
  bytesDownloaded?: number;
  installErrorCode?: InstallErrorCode;
  installStatus?: InstallStatus;
  packageName?: string;
  totalBytesToDownload?: number;
}

const listener = useRef<EmitterSubscription>();

listener.current = eventEmitter.addListener(Events.INSTALL_STATE_UPDATE, (installState: InstallState) => {
    console.log(installState);
});

// ...

useEffect(() => {
  return () => {
    listener.current?.remove();
  };
}, []);
```

The `installState` object describes the current status of the update download:

- `installStatus` - update installation status, if the user is installing the update at the moment:
  - `UNKNOWN` - default.
  - `DOWNLOADED` - downloaded.
  - `DOWNLOADING` - downloading.
  - `FAILED` - error.
  - `INSTALLING` - installing.
  - `PENDING` - pending.
- `packageName` - package name.
- `bytesDownloaded` - number of downloaded bytes.
- `totalBytesToDownload` - total number of bytes to download.
- `installErrorCode` - error code during download.

### Deferred Update

Download from RuStore UI

To initiate the application update download, call the `download()` method.

```typescript
try {
  const resultCode = await RustoreUpdateClient.download();
  console.log(resultCode);
} catch (err) {
  console.log(err);
}
```

If the user confirms the update download, `resultCode` will be `ResultCode.RESULT_OK`; if they decline, `ResultCode.RESULT_CANCELED`.

After calling the method, you can monitor the update download status in the listener. If you receive `InstallStatus.DOWNLOADED` in the listener, you can then call the update installation method `completeUpdate`. It is recommended to notify the user when the update is ready for installation.

### Forced Update

To initiate the forced update download, call the `immediate()` method.

```typescript
try {
  const resultCode = await RustoreUpdateClient.immediate();
  console.log(resultCode);
} catch (err) {
  console.log(err);
}
```

`resultCode (Int)`:

* `ResultCode.RESULT_OK (-1)` — update completed, the code may not be received because the app terminates during the update.
* `ResultCode.RESULT_CANCELED (0)` — flow interrupted by the user or an error occurred. It is assumed that upon receiving this code, the app should terminate.
* `ResultCode.ACTIVITY_NOT_FOUND (2)` — RuStore is not installed, or the installed version does not support forced updates (`RuStore versionCode` < `191`).

`throwable` — error starting the update flow.

No further actions are required after a successful update.

### Silent Update

For silent updates, it is recommended to implement your own interface.

To initiate the silent update download, call the `silent()` method.

```typescript
try {
  const resultCode = await RustoreUpdateClient.silent();
  console.log(resultCode);
} catch (err) {
  console.log(err);
}
```

If the user confirms the update download, `resultCode` will be `ResultCode.RESULT_OK`; if they decline, `ResultCode.RESULT_CANCELED`.

After calling the method, you can monitor the update download status in the listener. If you receive `InstallStatus.DOWNLOADED` in the listener, you can then call the update installation method `completeUpdate`. It is recommended to notify the user when the update is ready for installation.

More details about scenarios can be found at https://www.rustore.ru/help/en/.

## Installing the Update

After the APK file of the update is downloaded, you can start the update installation. To initiate the update installation, call the `completeUpdate()` method.

```typescript
try {
  await RustoreUpdateClient.completeUpdate();
} catch (err) {
  console.log(err);
}
```

The update is performed via the native Android tool. Upon successful update, the application will close.

## Possible Errors

List of possible errors:

```typescript
enum InstallErrorCode {
  UPDATE_ERROR_DOWNLOAD = 4002,
  UPDATE_ERROR_BLOCKED = 4003,
  UPDATE_ERROR_INVALID_APK = 4004,
  UPDATE_ERROR_CONFLICT = 4005,
  UPDATE_ERROR_STORAGE = 4006,
  UPDATE_ERROR_INCOMPATIBLE = 4007,
  UPDATE_ERROR_APP_NOT_OWNED = 4008,
  UPDATE_ERROR_INTERNAL_ERROR = 4009,
  UPDATE_ERROR_ABORTED = 4010,
  UPDATE_ERROR_APK_NOT_FOUND = 4011,
  UPDATE_ERROR_EXTERNAL_SOURCE_DENIED = 4012,
}
```
- `UPDATE_ERROR_DOWNLOAD` - Error during download.
- `UPDATE_ERROR_BLOCKED` - Installation blocked by the system.
- `UPDATE_ERROR_INVALID_APK` - Invalid APK update.
- `UPDATE_ERROR_CONFLICT` - Conflict with the current application version.
- `UPDATE_ERROR_STORAGE` - Insufficient storage space on the device.
- `UPDATE_ERROR_INCOMPATIBLE` - Incompatible with the device.
- `UPDATE_ERROR_APP_NOT_OWNED` - Application not purchased.
- `UPDATE_ERROR_INTERNAL_ERROR` - Internal error.
- `UPDATE_ERROR_ABORTED` - User canceled the update installation.
- `UPDATE_ERROR_APK_NOT_FOUND` - APK for installation not found.
- `UPDATE_ERROR_EXTERNAL_SOURCE_DENIED` - Update execution is denied. For example, the first method returned a response indicating that the update is unavailable, but the user calls the second method.

[ru]: README.md
[en]: README.en.md
