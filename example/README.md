<!-- ── Language switch (RU active) ──────────────────────────────────── -->
<div align="left" style="margin:0 0 14px 0;">

  <span style="display:inline-block;
               padding:.28rem .6rem;
               border:1px solid rgba(0,0,0,.18);
               border-radius:10px 0 0 10px;
               font-weight:400;
               font-size:12px;
               letter-spacing:.06em;
               color:#111827;
               background:linear-gradient(180deg,#e9edf2,#ffffff);
               box-shadow:inset 0 2px 6px rgba(0,0,0,.10);">
    RU
  </span><span style="display:inline-block;
               margin-left:-1px;
               padding:.28rem .6rem;
               border:1px solid rgba(0,0,0,.14);
               border-radius:0 10px 10px 0;
               font-weight:400;
               font-size:12px;
               letter-spacing:.06em;
               background:linear-gradient(180deg,#ffffff,#f3f4f6);
               box-shadow:0 1px 0 rgba(0,0,0,.06);">
    [EN][en]
  </span>

</div>
<!-- ────────────────────────────────────────────────────────────────── -->

Это новый [**React Native**](https://reactnative.dev) проект, созданный с помощью [`@react-native-community/cli`](https://github.com/react-native-community/cli).

# Начало работы

>**Примечание**: Перед началом убедитесь, что вы выполнили инструкции из раздела [Настройка окружения React Native](https://reactnative.dev/docs/environment-setup) до шага **"Создание нового приложения"**.

При сборке Android-примера нативный SDK загружается из [Maven-репозитория RuStore](https://nexus-external.rustore.ru/repository/maven-rustore-exposed). Репозиторий подключается автоматически в [`android/build.gradle` библиотеки](../android/build.gradle).

## Шаг 1: Запуск Metro Server

Сначала необходимо запустить **Metro** — сборщик JavaScript, который поставляется с React Native.

Чтобы запустить Metro, выполните следующую команду из корневой папки вашего React Native проекта:

```bash
# используя npm
npm start

# ИЛИ используя Yarn
yarn start
```

## Шаг 2: Запуск приложения

Оставьте Metro Bundler работать в своём окне терминала. Откройте новое окно терминала из корневой папки вашего React Native проекта. Выполните следующую команду для запуска приложения на Android или iOS:

### Для Android

```bash
# используя npm
npm run android

# ИЛИ используя Yarn
yarn android
```

### Для iOS

```bash
# используя npm
npm run ios

# ИЛИ используя Yarn
yarn ios
```

Если всё настроено правильно, ваше новое приложение вскоре появится в Android Emulator или iOS Simulator (при условии, что эмулятор/симулятор настроен корректно).

Это один из способов запуска приложения — вы также можете запустить его непосредственно из Android Studio или Xcode соответственно.

## Шаг 3: Внесение изменений в приложение

Теперь, когда вы успешно запустили приложение, давайте внесём в него изменения.

1. Откройте `App.tsx` в любом текстовом редакторе и измените несколько строк.
2. Для **Android**: Дважды нажмите клавишу <kbd>R</kbd> или выберите **"Reload"** (Перезагрузить) в **меню разработчика** (<kbd>Ctrl</kbd> + <kbd>M</kbd> (на Windows и Linux) или <kbd>Cmd ⌘</kbd> + <kbd>M</kbd> (на macOS)), чтобы увидеть изменения!

   Для **iOS**: Нажмите <kbd>Cmd ⌘</kbd> + <kbd>R</kbd> в симуляторе iOS, чтобы перезагрузить приложение и увидеть изменения!

## Поздравляем! :tada:

Вы успешно запустили и изменили ваше React Native приложение. :partying_face:

### Что дальше?

- Если вы хотите добавить этот новый React Native код в существующее приложение, ознакомьтесь с [руководством по интеграции](https://reactnative.dev/docs/integration-with-existing-apps).
- Если вам интересно узнать больше о React Native, загляните в [Введение в React Native](https://reactnative.dev/docs/getting-started).

# Устранение неполадок

Если у вас возникли проблемы с запуском, обратитесь к странице [Устранение неполадок](https://reactnative.dev/docs/troubleshooting).

# Дополнительная информация

Чтобы узнать больше о React Native, ознакомьтесь со следующими ресурсами:

- [Веб-сайт React Native](https://reactnative.dev) — узнайте больше о React Native.
- [Начало работы](https://reactnative.dev/docs/environment-setup) — обзор React Native и настройки окружения.
- [Изучение основ](https://reactnative.dev/docs/getting-started) — экскурсия по основам React Native.
- [Блог](https://reactnative.dev/blog) — читайте последние официальные посты в блоге React Native.
- [`@facebook/react-native`](https://github.com/facebook/react-native) — открытый исходный код; GitHub репозиторий React Native.

[en]: README.en.md
