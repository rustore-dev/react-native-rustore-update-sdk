export enum Events {
  INSTALL_STATE_UPDATE = 'InstallStateUpdate',
}

export enum ResultCode {
  RESULT_OK = -1,
  RESULT_CANCELED = 0,
}

export enum InstallStatus {
  UNKNOWN = 0,
  DOWNLOADED = 1,
  DOWNLOADING = 2,
  FAILED = 3,
  INSTALLING = 4,
  PENDING = 5,
}

export interface InstallState {
  bytesDownloaded?: number;
  installErrorCode?: InstallErrorCode;
  installStatus?: InstallStatus;
  packageName?: string;
  totalBytesToDownload?: number;
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

export enum AppUpdateType {
  FLEXIBLE = 0,
  IMMEDIATE = 1,
  SILENT = 2,
}

export enum InstallErrorCode {
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
