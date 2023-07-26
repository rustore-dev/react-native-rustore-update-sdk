export enum Events {
  INSTALL_STATE_UPDATE = 'InstallStateUpdate',
}

export enum ResultCode {
  RESULT_OK = -1,
  RESULT_CANCELED = 0,
}

export interface InstallState {
  bytesDownloaded?: number;
  installErrorCode?: number;
  installStatus?: InstallStatus;
  packageName?: string;
  totalBytesToDownload?: number;
}

export enum InstallStatus {
  UNKNOWN = 0,
  DOWNLOADED = 1,
  DOWNLOADING = 2,
  FAILED = 3,
  INSTALLING = 4,
  PENDING = 5,
}

export enum UpdateAvailability {
  UNKNOWN = 0,
  UPDATE_NOT_AVAILABLE = 1,
  UPDATE_AVAILABLE = 2,
  DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS = 3,
}

export interface AppUpdateInfo {
  updatedAt: string;
  packageName: string;
  updatePriority: number;
  updateAvailability: UpdateAvailability;
  availableVersionCode: number;
  installStatus: InstallStatus;
}
