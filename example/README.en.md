<!-- ── Language switch (EN active) ──────────────────────────────────── -->
<div align="left" style="margin:0 0 14px 0;">

  <span style="display:inline-block;
               padding:.28rem .6rem;
               border:1px solid rgba(0,0,0,.18);
               border-radius:10px 0 0 10px;
               font-weight:400;
               font-size:12px;
               letter-spacing:.06em;
               color:#111827;
               background:linear-gradient(180deg,#ffffff,#f3f4f6);
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
               color:#111827;
               background:linear-gradient(180deg,#e9edf2,#ffffff);
               box-shadow:inset 0 2px 6px rgba(0,0,0,.10);">
    EN
  </span>

</div>
<!-- ────────────────────────────────────────────────────────────────── -->

This is a new [**React Native**](https://reactnative.dev) project created using [`@react-native-community/cli`](https://github.com/react-native-community/cli).

# Getting Started

>**Note**: Before starting, make sure you have completed the instructions in the [React Native Environment Setup](https://reactnative.dev/docs/environment-setup) section up to the **"Creating a New Application"** step.

When building the Android example, the native SDK is downloaded from the [RuStore Maven repository](https://nexus-external.rustore.ru/repository/maven-rustore-exposed). The repository is configured automatically in [the library's `android/build.gradle`](../android/build.gradle).

## Step 1: Start the Metro Server

First, you need to start **Metro**, the JavaScript bundler that comes with React Native.

To start Metro, run the following command from the root folder of your React Native project:

```bash
# using npm
npm start

# OR using Yarn
yarn start
```

## Step 2: Run the App

Leave the Metro Bundler running in its terminal window. Open a new terminal window from the root folder of your React Native project. Run the following command to launch the app on Android or iOS:

### For Android

```bash
# using npm
npm run android

# OR using Yarn
yarn android
```

### For iOS

```bash
# using npm
npm run ios

# OR using Yarn
yarn ios
```

If everything is set up correctly, your new app will soon appear in the Android Emulator or iOS Simulator (assuming the emulator/simulator is properly configured).

This is one way to run the app — you can also run it directly from Android Studio or Xcode respectively.

## Step 3: Making Changes to the App

Now that you've successfully launched the app, let's make some changes.

1. Open `App.tsx` in any text editor and change a few lines.
2. For **Android**: Double press the <kbd>R</kbd> key or select **"Reload"** (Reload) in the **Developer Menu** (<kbd>Ctrl</kbd> + <kbd>M</kbd> on Windows and Linux or <kbd>Cmd ⌘</kbd> + <kbd>M</kbd> on macOS) to see the changes!

   For **iOS**: Press <kbd>Cmd ⌘</kbd> + <kbd>R</kbd> in the iOS simulator to reload the app and see the changes!

## Congratulations! :tada:

You've successfully run and modified your React Native app. :partying_face:

### What's Next?

- If you want to add this new React Native code to an existing app, check out the [Integration Guide](https://reactnative.dev/docs/integration-with-existing-apps).
- If you're curious to learn more about React Native, take a look at the [React Native Introduction](https://reactnative.dev/docs/getting-started).

# Troubleshooting

If you encounter issues during startup, refer to the [Troubleshooting page](https://reactnative.dev/docs/troubleshooting).

# Additional Information

To learn more about React Native, check out the following resources:

- [React Native Website](https://reactnative.dev) — learn more about React Native.
- [Getting Started](https://reactnative.dev/docs/environment-setup) — overview of React Native and environment setup.
- [Learn the Basics](https://reactnative.dev/docs/getting-started) — a tour of the basics of React Native.
- [Blog](https://reactnative.dev/blog) — read the latest official posts from the React Native blog.
- [`@facebook/react-native`](https://github.com/facebook/react-native) — open source; React Native GitHub repository.

[ru]: README.md
[en]: README.en.md
