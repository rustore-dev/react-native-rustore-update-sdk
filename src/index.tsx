import { NativeModules, NativeEventEmitter } from 'react-native';
import { ResultCode, type AppUpdateInfo } from './types';

interface RustoreUpdateModule {
  init: () => void;
  getAppUpdateInfo: () => Promise<AppUpdateInfo>;
  download: () => Promise<ResultCode>;
  immediate: () => Promise<ResultCode>;
  silent: () => Promise<ResultCode>;
  completeUpdate: () => Promise<boolean>;
}

export default NativeModules.RustoreUpdate as RustoreUpdateModule;

const eventEmitter = new NativeEventEmitter(NativeModules.RustoreUpdate);

export { eventEmitter };
export * from './types';
