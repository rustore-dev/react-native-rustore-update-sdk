# react-native-rustore-update

React Native RuStoreSDK для отзывов

RuStore In-app updates SDK помогает поддерживать актуальную версию вашего приложения на устройстве пользователя.

Когда пользователи поддерживают приложение в актуальном состоянии, они могут опробовать новые функции, а также воспользоваться улучшениями производительности и исправлениями ошибок.

Вы можете использовать RuStore In-app updates SDK для отображения процесса обновления приложения, который обеспечивает фоновую загрузку и установку обновления с контролем состояния. Пользователь сможет использовать ваше приложение в момент загрузки обновления.

## Общее

### Пример реализации
Для того, чтобы узнать как правильно интегрировать отзывы, рекомендуется ознакомиться с приложением-примером в папке `example`.

### Пример пользовательского сценария

<img src="https://gitflic.ru/project/rustore/flutter-rustore-update/blob/raw?file=flow.png" alt="Update flow" height="400px">

### Условия корректной работы SDK

Для работы RuStore In-app updates SDK необходимо соблюдение следующих условий:

- ОС Android версии 7.0 или выше.
- На устройстве пользователя должен быть установлен RuStore.
- Версия RuStoreApp на устройстве пользователя должна быть актуальной.
- Приложению RuStore должна быть разрешена установка приложений.

## Подключение в проект

Для подключения пакета к проекту нужно выполнить команду

```sh
npm install react-native-rustore-update
```

## Запрос обновления
Для работы механизма обновлений необходимо выполнить инициализацию плагина
```typescript
RustoreUpdateClient.init();
```

## Проверка наличия обновлений

Прежде чем запрашивать обновление, проверьте, доступно ли обновление для вашего приложения. Для проверки наличия обновлений вызовите метод `getAppUpdateInfo()`. При вызове данного метода проверяются следующие условия:

- На устройстве пользователя должен быть установлен RuStore.
- Версия RuStoreApp на устройстве пользователя должна быть актуальной.
- Пользователь и приложение не должны быть заблокированы в RuStore.

В ответ на данный метод вы получите объект `appUpdateInfo`, который будет содержать в себе информацию о необходимости обновления.

```typescript
try {
  const appUpdateInfo = await RustoreUpdateClient.getAppUpdateInfo();
  console.log(appUpdateInfo);
} catch (err) {
  console.log(err);
}
```

Объект `appUpdateInfo` содержит набор параметров, необходимых для определения доступности обновления:

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
- `updatedAt` - дата обновления
- `packageName` - имя пакета
- `updatePriority` - приоритет обновления
- `availableVersionCode` - код обновления
- `updateAvailability` - доступность обновления:
  - `UNKNOWN` - статус по умолчанию.
  - `UPDATE_NOT_AVAILABLE` - обновление не нужно.
  - `UPDATE_AVAILABLE` - обновление требуется загрузить или обновление уже загружено на устройство пользователя.
  - `DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS` - обновление уже скачивается или установка уже запущена.
- `installStatus` - статус установки обновления, если пользователь уже устанавливает обновление в текущий момент времени:
  - `UNKNOWN` - по умолчанию.
  - `DOWNLOADED` - скачано.
  - `DOWNLOADING` - скачивается.
  - `FAILED` - ошибка.
  - `INSTALLING` - устанавливается.
  - `PENDING` - в ожидании.

Запуск скачивания обновления возможен только в том случае, если поле `updateAvailability` содержит значение `UpdateAvailability.UPDATE_AVAILABLE`.

## Скачивание обновления

После подтверждения доступности обновления вы можете запросить у пользователя скачивание обновления, но перед этим необходимо запустить слушатель статуса скачивания обновления, используя метод `eventEmitter.addListener`:

```typescript

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

Объект `installState` описывает текущий статус скачивания обновления:

- `installStatus` - статус установки обновления, если пользователь уже устанавливает обновление в текущий момент времени:
  - `INSTALL_STATUS_DOWNLOADED` - скачано.
  - `INSTALL_STATUS_DOWNLOADING` - скачивается.
  - `INSTALL_STATUS_FAILED` - ошибка.
  - `INSTALL_STATUS_INSTALLING` - устанавливается.
  - `INSTALL_STATUS_PENDING` - в ожидании.
  - `INSTALL_STATUS_UNKNOWN` - по умолчанию.
- `bytesDownloaded` - количество загруженных байт.
- `totalBytesToDownload` - общее количество байт, которое необходимо скачать.
- `installErrorCode` - код ошибки во время скачивания.

### Скачивание с UI от RuStore

Для запуска скачивания обновления приложения вызовите метод download().

```typescript
try {
  const resultCode = await RustoreUpdateClient.startUpdateFlow();
  console.log(resultCode);
} catch (err) {
  console.log(err);
}
```

Если пользователь подтвердил скачивание обновления, то `resultCode` будет равен `ResultCode.RESULT_OK`, если отказался `ResultCode.RESULT_CANCELED`.

После вызова метода вы можете следить за статусом скачивания обновления в слушателе. Если в слушателе вы получили статус `InstallStatus.DOWNLOADED`, то вы можете вызвать метод установки обновления. Рекомендуем уведомить пользователя о готовности установки обновления.

## Установка обновления

После завершения скачивания apk-файла обновления вы можете запустить установку обновления. Для запуска установки обновления вызовите метод `completeUpdate()`.

```typescript
try {
  await RustoreUpdateClient.completeUpdate();
} catch (err) {
  console.log(err);
}
```

Обновление происходит через нативный инструмент Android. В случае успешного обновления приложение закроется.

## Возможные ошибки

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

Список возможных ошибок:
- `UPDATE_ERROR_DOWNLOAD` - Ошибка при скачивании.
- `UPDATE_ERROR_BLOCKED` - Установка заблокированна системой.
- `UPDATE_ERROR_INVALID_APK` - Некорректный APK обновления.
- `UPDATE_ERROR_CONFLICT` - Конфликт с текущей версией приложения.
- `UPDATE_ERROR_STORAGE` - Недостаточно памяти на устройстве.
- `UPDATE_ERROR_INCOMPATIBLE` - Несовместимо с устройством.
- `UPDATE_ERROR_APP_NOT_OWNED` - Приложение не куплено.
- `UPDATE_ERROR_INTERNAL_ERROR` - Внутренняя ошибка.
- `UPDATE_ERROR_ABORTED` - Пользователь отказался от установки обновления.
- `UPDATE_ERROR_APK_NOT_FOUND` - apk для запуска установки не найден.
- `UPDATE_ERROR_EXTERNAL_SOURCE_DENIED` - Запуск обновления запрещён. Например, в первом методе вернулся ответ о том, что обновление недоступно, но пользователь вызывает второй метод.
