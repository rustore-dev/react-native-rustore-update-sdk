package com.rustoreupdate

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateInfo
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus

class RustoreUpdateModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  private var isInitCalled: Boolean = false

  private var appUpdateInfo: AppUpdateInfo? = null

  companion object {
    lateinit var appUpdateManager: RuStoreAppUpdateManager
  }

  override fun getName(): String {
    return "RustoreUpdate"
  }

  @ReactMethod
  fun init() {
    if (isInitCalled) {
      return
    }
    appUpdateManager = RuStoreAppUpdateManagerFactory.create(reactApplicationContext)
    isInitCalled = true
  }

  @ReactMethod
  fun getAppUpdateInfo(promise: Promise) {
    appUpdateManager.getAppUpdateInfo().addOnSuccessListener { appUpdateInfo ->
      this.appUpdateInfo = appUpdateInfo

      val response = WritableNativeMap().apply {
        putString("updatedAt", appUpdateInfo.updatedAt)
        putString("packageName", appUpdateInfo.packageName)
        putInt("updatePriority", appUpdateInfo.updatePriority)
        putInt("updateAvailability", appUpdateInfo.updateAvailability)
        putInt("availableVersionCode", appUpdateInfo.availableVersionCode.toInt())
        putInt("installStatus", appUpdateInfo.installStatus)
      }

      promise.resolve(response)
    }.addOnFailureListener { throwable ->
      promise.reject(throwable)
    }
  }

  @ReactMethod
  fun download(promise: Promise) {
    startUpdateFlow(AppUpdateType.FLEXIBLE, promise)
  }

  @ReactMethod
  fun immediate(promise: Promise) {
    startUpdateFlow(AppUpdateType.IMMEDIATE, promise)
  }

  @ReactMethod
  fun silent(promise: Promise) {
    startUpdateFlow(AppUpdateType.SILENT, promise)
  }

  @ReactMethod
  fun completeUpdate(promise: Promise) {
    appUpdateManager.completeUpdate().addOnSuccessListener {
      promise.resolve(true)
    }.addOnFailureListener { throwable ->
      promise.reject(throwable)
    }
  }

  @ReactMethod
  fun addListener(eventName: String) {
    appUpdateManager.registerListener(installStateUpdateListener)
  }

  @ReactMethod
  fun removeListeners(count: Int) {
    appUpdateManager.unregisterListener(installStateUpdateListener)
  }

  private fun startUpdateFlow(appUpdateType: Int, promise: Promise) {
    val appUpdateInfo = this.appUpdateInfo
    if (appUpdateInfo == null) {
      val throwable = Throwable(message = "false")
      promise.reject(throwable)
      return
    }

    val appUpdateOptions =  AppUpdateOptions.Builder().appUpdateType(appUpdateType)
    appUpdateManager.startUpdateFlow(appUpdateInfo, appUpdateOptions.build())
      .addOnSuccessListener { resultCode ->
        promise.resolve(resultCode)
      }.addOnFailureListener { throwable ->
        promise.reject(throwable)
      }
  }

  private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }

  private val installStateUpdateListener = InstallStateUpdateListener { installState ->
    val params = WritableNativeMap().apply {
      putInt("installStatus", installState.installStatus)
    }

    when (installState.installStatus) {
      InstallStatus.DOWNLOADING -> {
        params.apply {
          putInt("totalBytesToDownload", installState.totalBytesToDownload.toInt())
          putInt("bytesDownloaded", installState.bytesDownloaded.toInt())
        }
        sendEvent(reactContext, "InstallStateUpdate", params)
      }

      InstallStatus.FAILED -> {
        params.apply {
          putInt("installErrorCode", installState.installErrorCode)
        }
        sendEvent(reactContext, "InstallStateUpdate", params)
      }

      InstallStatus.PENDING,
      InstallStatus.INSTALLING,
      InstallStatus.DOWNLOADED,
      InstallStatus.UNKNOWN -> {
        sendEvent(reactContext, "InstallStateUpdate", params)
      }
    }
  }
}
