package com.rustoreupdate

import android.app.Activity
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
import ru.rustore.sdk.appupdate.model.InstallState
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability

class RustoreUpdateModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  private var isInitCalled: Boolean = false

  private var appUpdateInfo: AppUpdateInfo? = null

  companion object{
    lateinit var appUpdateManager: RuStoreAppUpdateManager
  }

  override fun getName(): String {
    return "RustoreUpdate"
  }

  @ReactMethod
  fun init(){
    if (isInitCalled) {
      return
    }
    appUpdateManager = RuStoreAppUpdateManagerFactory.create(reactApplicationContext)
    isInitCalled = true
  }

  @ReactMethod
  fun getAppUpdateInfo(promise: Promise) {
    try {
      val appUpdateInfo = appUpdateManager.getAppUpdateInfo().await()

      this.appUpdateInfo = appUpdateInfo

      val response = WritableNativeMap().apply {
        putString("updatedAt", appUpdateInfo.updatedAt)
        putString("packageName", appUpdateInfo.packageName)
        putInt("updatePriority", appUpdateInfo.updatePriority)
        putInt("updateAvailability", appUpdateInfo.updateAvailability)
        putInt("availableVersionCode", appUpdateInfo.availableVersionCode)
        putInt("installStatus", appUpdateInfo.installStatus)
      }

      promise.resolve(response)
    } catch (throwable: Throwable) {
      promise.reject(throwable)
    }
  }


  @ReactMethod
  fun startUpdateFlow(promise: Promise) {
    val appUpdateInfo = this.appUpdateInfo

    if (appUpdateInfo != null) {
      try {
        val appUpdateOptions = AppUpdateOptions.Builder().build()
        val resultCode = appUpdateManager.startUpdateFlow(appUpdateInfo, appUpdateOptions).await()
        promise.resolve(resultCode)
      } catch (throwable: Throwable) {
        promise.reject(throwable)
      }
    } else {
      val throwable = Throwable(message = "false")
      promise.reject(throwable)
    }
  }

  @ReactMethod
  fun completeUpdate(promise: Promise){
    try {
      appUpdateManager.completeUpdate().await()
      promise.resolve(true)
    } catch (throwable: Throwable) {
      promise.reject(throwable)
    }
  }

  @ReactMethod
  fun addListener() {
    appUpdateManager.registerListener(installStateUpdateListener)
  }

  @ReactMethod
  fun removeListeners() {
    appUpdateManager.unregisterListener(installStateUpdateListener)
  }

  private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }

  private val installStateUpdateListener = InstallStateUpdateListener { installState ->
    when (installState.installStatus) {
      InstallStatus.DOWNLOADED -> {
        val params = WritableNativeMap().apply {
          putInt("installStatus", installState.installStatus)
        }
        sendEvent(reactContext, "InstallStateUpdate", params)
      }
      InstallStatus.DOWNLOADING -> {
        val params = WritableNativeMap().apply {
          putInt("installStatus", installState.installStatus)
          putInt("totalBytesToDownload", installState.totalBytesToDownload.toInt())
          putInt("bytesDownloaded", installState.bytesDownloaded.toInt())
        }
        sendEvent(reactContext, "InstallStateUpdate", params)
      }
      InstallStatus.FAILED -> {
        val params = WritableNativeMap().apply {
          putInt("installStatus", installState.installStatus)
          putInt("installErrorCode", installState.installErrorCode)
        }
        sendEvent(reactContext, "InstallStateUpdate", params)
      }
    }
  }
}
