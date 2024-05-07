import React, { useEffect, useRef, useState } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  type EmitterSubscription,
  ToastAndroid,
} from 'react-native';
import RustoreUpdateClient, {
  eventEmitter,
  Events,
  type InstallState,
  InstallStatus,
  ResultCode,
  UpdateAvailability,
  AppUpdateType,
} from 'react-native-rustore-update';

export default function App() {
  const listener = useRef<EmitterSubscription>();

  const [error, setError] = useState('');
  const [totalBytesToDownload, setTotalBytesToDownload] = useState(0);
  const [bytesDownloaded, setBytesDownloaded] = useState(0);

  useEffect(() => {
    RustoreUpdateClient.init();
  }, []);

  useEffect(() => {
    return () => {
      listener.current?.remove();
    };
  }, []);

  const registerListener = () => {
    listener.current = eventEmitter.addListener(
      Events.INSTALL_STATE_UPDATE,
      async (installState: InstallState) => {
        switch (installState.installStatus) {
          case InstallStatus.DOWNLOADED: {
            try {
              await RustoreUpdateClient.completeUpdate(AppUpdateType.FLEXIBLE);
            } catch (err) {
              setError(JSON.stringify(err));
            }
            break;
          }
          case InstallStatus.DOWNLOADING: {
            setTotalBytesToDownload(installState?.totalBytesToDownload ?? 0);
            setBytesDownloaded(installState?.bytesDownloaded ?? 0);
            break;
          }
          case InstallStatus.FAILED: {
            setError(`Ошибка при загрузке: ${installState?.installErrorCode}`);
            break;
          }
        }
      }
    );
  };

  const handleUpdate = async () => {
    try {
      const appUpdateInfo = await RustoreUpdateClient.getAppUpdateInfo();
      if (appUpdateInfo.updateAvailability === UpdateAvailability.UPDATE_AVAILABLE) {
        registerListener();

        const resultCode = await RustoreUpdateClient.download();
        
        if (resultCode === ResultCode.RESULT_CANCELED) {
          setError('Пользователь отказался от скачивания');
        }
      } else if ( appUpdateInfo.updateAvailability ===UpdateAvailability.UPDATE_NOT_AVAILABLE) {
        ToastAndroid.show('Обновлений нет', ToastAndroid.LONG);
      }
    } catch (err) {
      setError(JSON.stringify(err));
    }
  };

  if (error) {
    return (
      <View style={styles.container}>
        <Text>{error}</Text>
      </View>
    );
  }

  if (totalBytesToDownload > 0 && bytesDownloaded > 0) {
    return (
      <View style={styles.container}>
        <Text>
          Скачивание: {bytesDownloaded}/{totalBytesToDownload}
        </Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={handleUpdate}>
        <Text>Получить данные об обновлении</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
