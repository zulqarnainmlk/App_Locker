package helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class DataManager {

    public final static boolean isDeveloperMode = false;
    public static String childRegistrationStatus = "is_child_registered";

    private final String SAVE_USER_LOGIN_STATE = "save_user_login_state";
    private final String SAVE_USER_ID = "save_user_id";
    private final String SAVE_PARENT_ID = "save_parent_id";
    private final String SAVE_CHILD_NAME = "save_child_name";
    private final String SAVE_CHILD_DOB = "save_child_dob";
    private final String SAVE_CHILD_AGE = "save_child_age";
    private final String SAVE_CHILD_IMAGE = "save_child_image";
    private final String SAVE_DEVICE_TOKEN = "save_device_token";
    private final String SAVE_DEVICE_IMEI = "save_device_imei";
    private final String SAVE_USER_TYPE = "save_user_type";
    private final String SAVE_DEVICE_NAME = "save_device_name";
    private final String SAVE_DEVICE_OS = "save_device_os";
    private final String SAVE_DEVICE_BATTERY = "save_device_battery";
    private final String SAVE_IMG_STRING = "save_img_string";
    private final String SAVE_SPEED_LIMIT = "save_speed_limit";
    private final String SAVE_TIMEZONE = "save_timezone";
    private final String SAVE_DEVICE_INFO_DATA_POSTED_STATUS = "save_device_contacts_data_posted_status";
    private final String SAVE_DEVICE_USAGE_DATA_POSTED_STATUS = "save_device_usage_data_posted_status";
    private final String SAVE_DEVICE_ACTIVITY_DATA_POSTED_STATUS = "save_device_activity_data_posted_status";
    private final String SAVE_APPS_DATA_POSTED_STATUS = "save_apps_data_posted_status";
    private final String SAVE_CONTACTS_DATA_POSTED_STATUS = "save_contacts_data_posted_status";
    private final String SAVE_PERMISSION_STATUS = "save_permission_status";
    private final String SAVE_DEVICE_LOCATION_POSTED_STATUS = "save_device_location_posted_status";
    private final String SAVE_REMINDER_REQUEST_CODE = "save_reminder_request_code";
    private final String SAVE_CURRENT_PACKAGE_NAME = "save_currrent_package_name";
    private final String SAVE_CURRENT_USAGE_TIME = "save_currrent_usage_time";

    private final String SAVE_SCREEN_TIME_MONDAY = "save_screen_time_mon";
    private final String SAVE_SCREEN_TIME_TUESDAY = "save_screen_time_tue";
    private final String SAVE_SCREEN_TIME_WEDNESDAY = "save_screen_time_wed";
    private final String SAVE_SCREEN_TIME_THURSDAY = "save_screen_time_thu";
    private final String SAVE_SCREEN_TIME_FRIDAY = "save_screen_time_fri";
    private final String SAVE_SCREEN_TIME_SATURDAY = "save_screen_time_sat";
    private final String SAVE_SCREEN_TIME_SUNDAY = "save_screen_time_sun";

    private final String SAVE_BONUS_TIME = "save_bonus_time";
    private final String SAVE_BONUS_TIME_START_TIME = "save_bonus_start_time";
    private final String SAVE_BONUS_TIME_END_TIME = "save_bonus_end_time";
    private final String SAVE_BONUS_TIME_STATUS = "save_bonus_time_status";

    private final String SCREEN_LOCK_START_TIME = "screen_lock_start_time";
    private final String SCREEN_LOCK_END_TIME = "screen_lock_end_time";


    private final String SAVE_B_START_TIME_MONDAY = "save_b_start_time_mon";
    private final String SAVE_B_END_TIME_MONDAY = "save_b_end_time_mon";
    private final String SAVE_B_START_TIME_TUESDAY = "save_b_start_time_tue";
    private final String SAVE_B_END_TIME_TUESDAY = "save_b_end_time_tue";
    private final String SAVE_B_START_TIME_WEDNESDAY = "save_b_start_time_wed";
    private final String SAVE_B_END_TIME_WEDNESDAY = "save_b_end_time_wed";
    private final String SAVE_B_START_TIME_THURSDAY = "save_b_start_time_thu";
    private final String SAVE_B_END_TIME_THURSDAY = "save_b_end_time_thu";
    private final String SAVE_B_START_TIME_FRIDAY = "save_b_start_time_fri";
    private final String SAVE_B_END_TIME_FRIDAY = "save_b_end_time_fri";
    private final String SAVE_B_START_TIME_SATURDAY = "save_b_start_time_sat";
    private final String SAVE_B_END_TIME_SATURDAY = "save_screen_time_sat";
    private final String SAVE_B_START_TIME_SUNDAY = "save_b_start_time_sun";
    private final String SAVE_B_END_TIME_SUNDAY = "save_screen_time_sun";
    private final String SAVE_BED_TIME_MONDAY_STATUS = "save_bed_time_monday_status";
    private final String SAVE_BED_TIME_TUESDAY_STATUS = "save_bed_time_tuesday_status";
    private final String SAVE_BED_TIME_WEDNESDAY_STATUS = "save_bed_time_wednesday_status";
    private final String SAVE_BED_TIME_THURSDAY_STATUS = "save_bed_time_thursday_status";
    private final String SAVE_BED_TIME_FRIDAY_STATUS = "save_bed_time_friday_status";
    private final String SAVE_BED_TIME_SATURDAY_STATUS = "save_bed_time_saturday_status";
    private final String SAVE_BED_TIME_SUNDAY_STATUS = "save_bed_time_sunday_status";


    private final String SAVE_SCREEN_LOCK_DIALOG_STATUS = "screen_lock_dialog_status";
    private final String SAVE_AUTO_LOCK_DIALOG_STATUS = "auto_lock_dialog_status";
    private final String SAVE_BONUS_TIME_LOCK_DIALOG_STATUS = "bonus_time_lock_dialog_status";
    private final String SAVE_BED_TIME_LOCK_DIALOG_STATUS = "bed_time_lock_dialog_status";
    private final String SAVE_SCREEN_TIME_LOCK_DIALOG_STATUS = "screen_time_lock_dialog_status";

    private final String LET_ME_USE_ALLOWED_APPS_STATUS = "let_me_use_allowed_apps_status";
    private final String SAVE_DATE_TIME = "save_date_time";
    private final String DISABLE_ADMIN = "disable_admin";
    private final String SAVE_PERMANENT_APPS_STATUS ="save_permanent_apps_status";
    private final String SAVE_CUSTOM_BLOCK_APPS_STATUS ="save_custom_apps_status";
    private final String SAVE_CUSTOM_TIME_SCHEDULE_STATUS ="save_custom_time_schedule";
    private final String ALL_SERVICES_STOPPED ="all_services_stopped";
    private final String SAVE_SCREEN_LOCK_STATUS_MONDAY = "save_screen_lock_status_monday";
    private final String SAVE_SCREEN_LOCK_STATUS_TUESDAY = "save_screen_lock_status_tuesday";
    private final String SAVE_SCREEN_LOCK_STATUS_WEDNESDAY = "save_screen_lock_status_wednesday";
    private final String SAVE_SCREEN_LOCK_STATUS_THURSDAY = "save_screen_lock_status_thursday";
    private final String SAVE_SCREEN_LOCK_STATUS_FRIDAY = "save_screen_lock_status_friday";
    private final String SAVE_SCREEN_LOCK_STATUS_SATURDAY = "save_screen_lock_status_saturday";
    private final String SAVE_SCREEN_LOCK_STATUS_SUNDAY = "save_screen_lock_status_sunday";
    private final String SAVE_DEVICE_USAGE_ACTIVE_STATUS = "save_device_usage_active_status";
    private final String SAVE_SCREEN_TIME_SUNDAY_STATUS = "save_screen_time_sunday_status";
    private final String SAVE_SCREEN_TIME_MONDAY_STATUS = "save_screen_time_monday_status";
    private final String SAVE_SCREEN_TIME_TUESDAY_STATUS = "save_screen_time_tuesday_status";
    private final String SAVE_SCREEN_TIME_WEDNESDAY_STATUS = "save_screen_time_wednesday_status";
    private final String SAVE_SCREEN_TIME_THURSDAY_STATUS = "save_screen_time_thursday_status";
    private final String SAVE_SCREEN_TIME_FRIDAY_STATUS = "save_screen_time_friday_status";
    private final String SAVE_SCREEN_TIME_SATURDAY_STATUS = "save_screen_time_saturday_status";
    private final String SAVE_PERMANENT_NOTIFICATION_STATUS = "save_permanent_notification_status";
    private final String SAVE_LET_ME_USE_ALLOWED_APPS = "save_let_me_use_allowed_apps";
    private final String IS_DEVICE_ALLOWED = "IS_DEVICE_ALLOWED";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    public DataManager(Context context) {
        sharedPreferences = context.getSharedPreferences("BriskCargo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void saveAppIcon(String packageName, String appIcon){
        editor.putString(packageName, appIcon);
        editor.commit();
    }

    public String getAppIcon(String packageName) {
        return sharedPreferences.getString(packageName, "");
    }

    public void saveUserSignedIn (boolean user_signed_in){
        editor.putBoolean(SAVE_USER_LOGIN_STATE, user_signed_in);
        editor.commit();
    }
    public void setRunOnce (boolean runOnce){
        editor.putBoolean("RUN_ONCE", runOnce);
        editor.commit();
    }

    public boolean getRunOnce() {
        return sharedPreferences.getBoolean("RUN_ONCE", false);
    }



    public boolean isUnblockAllowed() {
        return sharedPreferences.getBoolean("isUnblockAllowed", false);
    }

    public void setUnblockAllowed(boolean isUnblockAllowed) {
        editor.putBoolean("isUnblockAllowed", isUnblockAllowed);
        editor.commit();
    }

    public void setLastScreenTimeCheckTime(long interval) {
        editor.putLong("lastScreenTimeCheckTime", interval);
        editor.commit();
    }

    public long getLastScreenTimeCheckTime() {
        return sharedPreferences.getLong("lastScreenTimeCheckTime", 0);
    }


    public long getUnblockAllowedEndTime() {
        return sharedPreferences.getLong("unblockAllowedEndTime", 0);
    }

    public void setUnblockAllowedEndTime(long unblockAllowedEndTime) {
        editor.putLong("unblockAllowedEndTime", unblockAllowedEndTime);
        editor.commit();
    }

    public boolean isUserSignedIN() {
        return sharedPreferences.getBoolean(SAVE_USER_LOGIN_STATE, false);
    }


    public void saveDeviceAdminStatus (boolean device_admin_status){
        editor.putBoolean(DISABLE_ADMIN, device_admin_status);
        editor.commit();
    }

    public boolean isDeviceAdmin() {
        return sharedPreferences.getBoolean(DISABLE_ADMIN, false);
    }

    public void saveBlockAppsStatus(boolean permanent_app_status){
        editor.putBoolean(SAVE_PERMANENT_APPS_STATUS, permanent_app_status);
        editor.commit();
    }
    public void saveCustomBlockAppsStatus(boolean custom_app_status){
        editor.putBoolean(SAVE_CUSTOM_BLOCK_APPS_STATUS, custom_app_status);
        editor.commit();
    }
    public boolean isAppCustomBlocked() {
        return sharedPreferences.getBoolean(SAVE_CUSTOM_BLOCK_APPS_STATUS, false);
    }

    public void saveChildRegisteredStatus (boolean status){
        editor.putBoolean(childRegistrationStatus, status);
        editor.commit();
    }

    public boolean isChildRegistered() {
        return sharedPreferences.getBoolean(childRegistrationStatus, false);
    }
    public void saveLetMeUseAllowedAppsStatus (boolean status){
        editor.putBoolean(LET_ME_USE_ALLOWED_APPS_STATUS, status);
        editor.commit();
    }

    public boolean getLetMeUseAllowedAppsStatus() {
        return sharedPreferences.getBoolean(LET_ME_USE_ALLOWED_APPS_STATUS, false);
    }
    public void saveCustomTimeSchedule (boolean timely_app_status){
        editor.putBoolean(SAVE_CUSTOM_TIME_SCHEDULE_STATUS, timely_app_status);
        editor.commit();
    }

    public boolean isCustomTimeSchedule() {
        return sharedPreferences.getBoolean(SAVE_CUSTOM_TIME_SCHEDULE_STATUS, false);
    }

    public void saveAllServicesStopped(boolean status) {
        editor.putBoolean(ALL_SERVICES_STOPPED, status);
        editor.commit();
    }

    public boolean isAllServicesStopped() {
        return sharedPreferences.getBoolean(ALL_SERVICES_STOPPED, false);
    }

    public void saveScreenLockDialogStatus(boolean status) {
        editor.putBoolean(SAVE_SCREEN_LOCK_DIALOG_STATUS, status);
        editor.commit();
    }

    public boolean getBonusTimeLockDialogStatus() {
        return sharedPreferences.getBoolean(SAVE_BONUS_TIME_LOCK_DIALOG_STATUS, false);
    }
    public void saveBonusTimeLockDialogStatus(boolean status) {
        editor.putBoolean(SAVE_BONUS_TIME_LOCK_DIALOG_STATUS, status);
        editor.commit();
    }
    public boolean getBedTimeLockDialogStatus() {
        return sharedPreferences.getBoolean(SAVE_BED_TIME_LOCK_DIALOG_STATUS, false);
    }
    public void saveBedTimeLockDialogStatus(boolean status) {
        editor.putBoolean(SAVE_BED_TIME_LOCK_DIALOG_STATUS, status);
        editor.commit();
    }
    public boolean getScreenTimeLockDialogStatus() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_TIME_LOCK_DIALOG_STATUS, false);
    }
    public void saveScreenTimeLockDialogStatus(boolean status) {
        editor.putBoolean(SAVE_SCREEN_TIME_LOCK_DIALOG_STATUS, status);
        editor.commit();
    }

    public boolean getScreenLockDialogStatus() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_LOCK_DIALOG_STATUS, false);
    }
    public void saveAutoLockDialogStatus(boolean status) {
        editor.putBoolean(SAVE_SCREEN_TIME_LOCK_DIALOG_STATUS, status);
        editor.commit();
    }

    public boolean getAutoLockDialogStatus() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_LOCK_DIALOG_STATUS, false);
    }
    public void setAppBecameZombie(boolean status) {
        editor.putBoolean("ZInput", status);
        editor.commit();
    }

    public boolean isAppBecameZombie() {
        return sharedPreferences.getBoolean("ZInput", false);
    }

    public void setNewUpdateAvailable(boolean status) {
        editor.putBoolean("APP_UPDATE", status);
        editor.commit();
    }

    public boolean getNewUpdateAvailable() {
        return sharedPreferences.getBoolean("APP_UPDATE", false);
    }

    public void setDownloadedUpdateVersion(String version) {
        editor.putString("DOWNLOADED_UPDATE_V", version);
        editor.commit();
    }

    public String getDownloadedUpdateVersion() {
        return sharedPreferences.getString("DOWNLOADED_UPDATE_V", "");
    }


    public void savePermanentNotificationStatus (boolean notificationStatus){
        editor.putBoolean(SAVE_PERMANENT_NOTIFICATION_STATUS, notificationStatus);
        editor.commit();
    }


    public boolean isPermanentNotificationCreated() {
        return sharedPreferences.getBoolean(SAVE_PERMANENT_NOTIFICATION_STATUS, false);
    }


    public void saveCurrentPackageName (String packageName){
        editor.putString(SAVE_CURRENT_PACKAGE_NAME, packageName);
        editor.commit();
    }


    public String getCurrentPackageName() {
        return sharedPreferences.getString(SAVE_CURRENT_PACKAGE_NAME, "1");
    }
    public void saveCurrentUsageTimeInMinutes (String time){
        editor.putString(SAVE_CURRENT_USAGE_TIME, time);
        editor.commit();
    }


    public String getCurrentUsageTimeInMinutes() {
        return sharedPreferences.getString(SAVE_CURRENT_USAGE_TIME, "1");
    }

    public void saveRequestCode (String requestCode){
        editor.putString(SAVE_REMINDER_REQUEST_CODE, requestCode);
        editor.commit();
    }


    public String getRequestCode() {
        return sharedPreferences.getString(SAVE_REMINDER_REQUEST_CODE, "1");
    }


    public void saveUserId (String user_id){
        editor.putString(SAVE_USER_ID, user_id);
        editor.commit();
    }


    public String getUserID() {
        return sharedPreferences.getString(SAVE_USER_ID, "");
    }

    public void saveParentID (String user_id){
        editor.putString(SAVE_PARENT_ID, user_id);
        editor.commit();
    }


    public String getParentID() {
        return sharedPreferences.getString(SAVE_PARENT_ID, "");
    }

    public void isDeviceAllowed (boolean isAllowed){
        editor.putBoolean(IS_DEVICE_ALLOWED, isAllowed);
        editor.commit();
    }


    public boolean isDeviceAllowed() {
        return sharedPreferences.getBoolean(IS_DEVICE_ALLOWED, false);
    }
    public void savePermissionStatus (boolean user_signed_in){
        editor.putBoolean(SAVE_PERMISSION_STATUS, user_signed_in);
        editor.apply();
        editor.commit();
    }


    public boolean isPermissionDOne() {
        return sharedPreferences.getBoolean(SAVE_PERMISSION_STATUS, false);
    }

    public void saveChildName (String childName){
        editor.putString(SAVE_CHILD_NAME, childName);

        editor.commit();
    }


    public String getDate_of_birth() {
        return sharedPreferences.getString(SAVE_CHILD_DOB, "");
    }

    public void saveDate_of_birth (String childDob){
        editor.putString(SAVE_CHILD_DOB, childDob);
        editor.commit();
    }

    public String getAge() {
        return sharedPreferences.getString(SAVE_CHILD_AGE, "");
    }

    public void saveAge (String childAge){
        editor.putString(SAVE_CHILD_AGE, childAge);
        editor.commit();
    }

    public String getChildName() {
        return sharedPreferences.getString(SAVE_CHILD_NAME, "");
    }

    public void saveChildImage (String childImage){
        editor.putString(SAVE_CHILD_IMAGE, childImage);

        editor.commit();
    }


    public String getChildImage() {
        String a = sharedPreferences.getString(SAVE_CHILD_IMAGE, "");
        return a;
    }

    public void saveUserType (String userType){
        editor.putString(SAVE_USER_TYPE, userType);
        editor.commit();
    }


    public String getUserType() {
        String a = sharedPreferences.getString(SAVE_USER_TYPE, "");
        return a;
    }

    public void saveDeviceDataPosted (boolean isdataPosted){
        editor.putBoolean(SAVE_DEVICE_INFO_DATA_POSTED_STATUS, isdataPosted);
        editor.commit();
    }

    public boolean isDeviceDataPosted() {
        return sharedPreferences.getBoolean(SAVE_DEVICE_INFO_DATA_POSTED_STATUS, false);
    }

    public void saveDeviceUsageDataPosted (boolean isdataPosted){
        editor.putBoolean(SAVE_DEVICE_USAGE_DATA_POSTED_STATUS, isdataPosted);
        editor.commit();
    }


    public boolean isDeviceUsageDataPosted() {
        return sharedPreferences.getBoolean(SAVE_DEVICE_USAGE_DATA_POSTED_STATUS, false);
    }

    public void saveDeviceUsageActiveStatus (boolean isServiceActive){
        editor.putBoolean(SAVE_DEVICE_USAGE_ACTIVE_STATUS, isServiceActive);
        editor.commit();
    }


    public boolean isDeviceUsageActive() {
        return sharedPreferences.getBoolean(SAVE_DEVICE_USAGE_ACTIVE_STATUS, false);
    }

    public void saveDeviceActivityDataPosted (boolean isdataPosted){
        editor.putBoolean(SAVE_DEVICE_ACTIVITY_DATA_POSTED_STATUS, isdataPosted);
        editor.commit();
    }


    public boolean isDeviceActivityDataPosted() {
        boolean a = sharedPreferences.getBoolean(SAVE_DEVICE_ACTIVITY_DATA_POSTED_STATUS, false);
        return a;
    }
    public void saveAppsDataPosted (boolean isdataPosted){
        editor.putBoolean(SAVE_APPS_DATA_POSTED_STATUS, isdataPosted);
        editor.commit();
    }


    public boolean isAppsDataPosted() {
        return sharedPreferences.getBoolean(SAVE_APPS_DATA_POSTED_STATUS, false);
    }


    public void saveContactsDataPosted (boolean isdataPosted){
        editor.putBoolean(SAVE_CONTACTS_DATA_POSTED_STATUS, isdataPosted);
        editor.commit();
    }


    public boolean isContactsDataPosted() {
        return sharedPreferences.getBoolean(SAVE_CONTACTS_DATA_POSTED_STATUS, false);
    }


    public void saveLocationDataPosted (boolean isdataPosted){
        editor.putBoolean(SAVE_DEVICE_LOCATION_POSTED_STATUS, isdataPosted);
        editor.commit();
    }


    public boolean isLocationDataPosted() {
        boolean a = sharedPreferences.getBoolean(SAVE_DEVICE_LOCATION_POSTED_STATUS, false);
        return a;
    }
    public void saveDeviceName (String name){
        editor.putString(SAVE_DEVICE_NAME, name);
        editor.commit();
    }


    public String getDeviceName() {
        String a = sharedPreferences.getString(SAVE_DEVICE_NAME, "");
        return a;
   }

    public void saveDeviceOS (String name){
        editor.putString(SAVE_DEVICE_OS, name);
        editor.commit();
    }


    public String getDeviceOS() {
        String a = sharedPreferences.getString(SAVE_DEVICE_OS, "");
        return a;
    }

    public void saveDeviceBatteryStatus (String name){
        editor.putString(SAVE_DEVICE_BATTERY, name);
        editor.commit();
    }


    public String getDeviceBatteryStatus() {
        String a = sharedPreferences.getString(SAVE_DEVICE_BATTERY, "");
        return a;
    }

    public void saveDeviceToken (String deviceToken){
        editor.putString(SAVE_DEVICE_TOKEN, deviceToken);
        editor.commit();
    }


    public String getDeviceToken() {
        String a = sharedPreferences.getString(SAVE_DEVICE_TOKEN, "");
        return a;
    }

    public void saveDeviceIMEI (String deviceToken){
        editor.putString(SAVE_DEVICE_IMEI, deviceToken);
        editor.commit();
    }


    public String getDeviceIMEI() {
        String a = sharedPreferences.getString(SAVE_DEVICE_IMEI, "");
        return a;
    }

    public void saveImageString (String imgString){
        editor.putString(SAVE_IMG_STRING, imgString);
        editor.commit();
    }


    public String getImageString() {
        return sharedPreferences.getString(SAVE_IMG_STRING, "");
    }

    public void SaveSpeedLimit (String speedLimit){
        editor.putString(SAVE_SPEED_LIMIT, speedLimit);
        editor.commit();
    }

    public void saveTimeZone(String timeZone){
        editor.putString(SAVE_TIMEZONE, timeZone);
        editor.commit();
    }


    public String getTimeZone() {
        String a = sharedPreferences.getString(SAVE_TIMEZONE, "");
        return a;
    }

    //////////////BED TIME///////////////////////////

    public void saveBedTimeMondayStartTime(String time){
        editor.putString(SAVE_B_START_TIME_MONDAY, time);
        editor.commit();
    }
    public String getBedTimeMondayStartTime() {
        String a = sharedPreferences.getString(SAVE_B_START_TIME_MONDAY, "");
        return a;
    }
    public void saveBedTimeMondayEndTime(String time){
        editor.putString(SAVE_B_END_TIME_MONDAY, time);
        editor.commit();
    }
    public String getBedTimeMondayEndTime() {
        String a = sharedPreferences.getString(SAVE_B_END_TIME_MONDAY, "");
        return a;
    }
    public void saveBedTimeTuesdayStartTime(String time){
        editor.putString(SAVE_B_START_TIME_TUESDAY, time);
        editor.commit();
    }
    public String getBedTimeTuesdayStartTime() {
        String a = sharedPreferences.getString(SAVE_B_START_TIME_TUESDAY, "");
        return a;
    }
    public void saveBedTimeTuesdayEndTime(String time){
        editor.putString(SAVE_B_END_TIME_TUESDAY, time);
        editor.commit();
    }
    public String getBedTimeTuesdayEndTime() {
        String a = sharedPreferences.getString(SAVE_B_END_TIME_TUESDAY, "");
        return a;
    }

    public void saveBedTimeWednesdayStartTime(String time){
        editor.putString(SAVE_B_START_TIME_WEDNESDAY, time);
        editor.commit();
    }
    public String getBedTimeWednesdayStartTime() {
        String a = sharedPreferences.getString(SAVE_B_START_TIME_WEDNESDAY, "");
        return a;
    }

    public void saveBedTimeWednesdayEndTime(String time){
        editor.putString(SAVE_B_END_TIME_WEDNESDAY, time);
        editor.commit();
    }
    public String getBedTimeWednesdayEndTime() {
        String a = sharedPreferences.getString(SAVE_B_END_TIME_WEDNESDAY, "");
        return a;
    }
    public void saveBedTimeThursdayStartTime(String time){
        editor.putString(SAVE_B_START_TIME_THURSDAY, time);
        editor.commit();
    }
    public String getBedTimeThursdayStartTime() {
        String a = sharedPreferences.getString(SAVE_B_START_TIME_THURSDAY, "");
        return a;
    }
    public void saveBedTimeThursdayEndTime(String time){
        editor.putString(SAVE_B_END_TIME_THURSDAY, time);
        editor.commit();
    }
    public String getBedTimeThursdayEndTime() {
        String a = sharedPreferences.getString(SAVE_B_END_TIME_THURSDAY, "");
        return a;
    }

    public void saveBedTimeFridayStartTime(String time){
        editor.putString(SAVE_B_START_TIME_FRIDAY, time);
        editor.commit();
    }
    public String getBedTimeFridayStartTime() {
        String a = sharedPreferences.getString(SAVE_B_START_TIME_FRIDAY, "");
        return a;
    }

    public void saveBedTimeFridayEndTime(String time){
        editor.putString(SAVE_B_END_TIME_FRIDAY, time);
        editor.commit();
    }
    public String getBedTimeFridayEndTime() {
        String a = sharedPreferences.getString(SAVE_B_END_TIME_FRIDAY, "");
        return a;
    }
    public void saveBedTimeSaturdayStartTime(String time){
        editor.putString(SAVE_B_START_TIME_SATURDAY, time);
        editor.commit();
    }
    public String getBedTimeSaturdayStartTime() {
        String a = sharedPreferences.getString(SAVE_B_START_TIME_SATURDAY, "");
        return a;
    }
    public void saveBedTimeSaturdayEndTime(String time){
        editor.putString(SAVE_B_END_TIME_SATURDAY, time);
        editor.commit();
    }
    public String getBedTimeSaturdayEndTime() {
        String a = sharedPreferences.getString(SAVE_B_END_TIME_SATURDAY, "");
        return a;
    }
    public void saveBedTimeSundayStartTime(String time){
        editor.putString(SAVE_B_START_TIME_SUNDAY, time);
        editor.commit();
    }
    public String getBedTimeSundayStartTime() {
        String a = sharedPreferences.getString(SAVE_B_START_TIME_SUNDAY, "");
        return a;
    }

    public void saveBedTimeSundayEndTime(String time){
        editor.putString(SAVE_B_END_TIME_SUNDAY, time);
        editor.commit();
    }
    public String getBedTimeSundayEndTime() {
        String a = sharedPreferences.getString(SAVE_B_END_TIME_SUNDAY, "");
        return a;
    }

    public void saveBedTimeMondayStatus (boolean s_status){
        editor.putBoolean(SAVE_BED_TIME_MONDAY_STATUS, s_status);
        editor.commit();
    }

    public boolean getBedTimeMondayStatus() {
        return sharedPreferences.getBoolean(SAVE_BED_TIME_MONDAY_STATUS, false);
    }

    public void saveBedTimeTuesdayStatus (boolean s_status){
        editor.putBoolean(SAVE_BED_TIME_TUESDAY_STATUS, s_status);
        editor.commit();
    }

    public boolean getBedTimeTuesdayStatus() {
        return sharedPreferences.getBoolean(SAVE_BED_TIME_TUESDAY_STATUS, false);
    }

    public void saveBedTimeWednesdayStatus (boolean s_status){
        editor.putBoolean(SAVE_BED_TIME_WEDNESDAY_STATUS, s_status);
        editor.commit();
    }

    public boolean getBedTimeWednesdayStatus() {
        return sharedPreferences.getBoolean(SAVE_BED_TIME_WEDNESDAY_STATUS, false);
    }

    public void saveBedTimeThursdayStatus (boolean s_status){
        editor.putBoolean(SAVE_BED_TIME_THURSDAY_STATUS, s_status);
        editor.commit();
    }

    public boolean getBedTimeThursdayStatus() {
        return sharedPreferences.getBoolean(SAVE_BED_TIME_THURSDAY_STATUS, false);
    }

    public void saveBedTimeFridayStatus (boolean s_status){
        editor.putBoolean(SAVE_BED_TIME_FRIDAY_STATUS, s_status);
        editor.commit();
    }

    public boolean getBedTimeFridayStatus() {
        return sharedPreferences.getBoolean(SAVE_BED_TIME_FRIDAY_STATUS, false);
    }

    public void saveBedTimeSaturdayStatus (boolean s_status){
        editor.putBoolean(SAVE_BED_TIME_SATURDAY_STATUS, s_status);
        editor.commit();
    }

    public boolean getBedTimeSaturdayStatus() {
        return sharedPreferences.getBoolean(SAVE_BED_TIME_SATURDAY_STATUS, false);
    }

    public void saveBedTimeSundayStatus (boolean s_status){
        editor.putBoolean(SAVE_BED_TIME_SUNDAY_STATUS, s_status);
        editor.commit();
    }

    public boolean getBedTimeSundayStatus() {
        return sharedPreferences.getBoolean(SAVE_BED_TIME_SUNDAY_STATUS, false);
    }

    public void saveLockTimeStartTime(String time){
        editor.putString(SCREEN_LOCK_START_TIME, time);
        editor.commit();
    }


    public String getLockTimeStartTime() {
        String a = sharedPreferences.getString(SCREEN_LOCK_START_TIME, "");
        return a;
    }
    public void saveLockTimeEndTime(String time){
        editor.putString(SCREEN_LOCK_END_TIME, time);
        editor.commit();
    }


    public String getLockTimeEndTime() {
        String a = sharedPreferences.getString(SCREEN_LOCK_END_TIME, "");
        return a;
    }

    public void saveScreenTimeMonday(String time){
        editor.putString(SAVE_SCREEN_TIME_MONDAY, time);
        editor.commit();
    }


    public String getScreenTimeMonday() {
        String a = sharedPreferences.getString(SAVE_SCREEN_TIME_MONDAY, "");
        return a;
    }


    public void saveScreenTimeTuesday(String time){
        editor.putString(SAVE_SCREEN_TIME_TUESDAY, time);
        editor.commit();
    }


    public String getScreenTimeTuesday() {
        String a = sharedPreferences.getString(SAVE_SCREEN_TIME_TUESDAY, "");
        return a;
    }

    public void saveScreenTimeWednesday(String time){
        editor.putString(SAVE_SCREEN_TIME_WEDNESDAY, time);
        editor.commit();
    }


    public String getScreenTimeWednesday() {
        String a = sharedPreferences.getString(SAVE_SCREEN_TIME_WEDNESDAY, "");
        return a;
    }

    public void saveScreenTimeThursday(String time){
        editor.putString(SAVE_SCREEN_TIME_THURSDAY, time);
        editor.commit();
    }


    public String getScreenTimeThursday() {
        String a = sharedPreferences.getString(SAVE_SCREEN_TIME_THURSDAY, "");
        return a;
    }

    public void saveScreenTimeFriday(String time){
        editor.putString(SAVE_SCREEN_TIME_FRIDAY, time);
        editor.commit();
    }


    public String getScreenTimeFriday() {
        String a = sharedPreferences.getString(SAVE_SCREEN_TIME_FRIDAY, "");
        return a;
    }

    public void saveScreenTimeSaturday(String time){
        editor.putString(SAVE_SCREEN_TIME_SATURDAY, time);
        editor.commit();
    }


    public String getScreenTimeSaturday() {
        String a = sharedPreferences.getString(SAVE_SCREEN_TIME_SATURDAY, "");
        return a;
    }

    public void saveScreenTimeSunday(String time){
        editor.putString(SAVE_SCREEN_TIME_SUNDAY, time);
        editor.commit();
    }


    public String getScreenTimeSunday() {
        String a = sharedPreferences.getString(SAVE_SCREEN_TIME_SUNDAY, "");
        return a;
    }
    public void saveBonusTime(String time){
        editor.putString(SAVE_BONUS_TIME, time);
        editor.commit();
    }


    public String getBonusTime() {
        String a = sharedPreferences.getString(SAVE_BONUS_TIME, "");
        return a;
    }
    public void saveBonusTimeStartTime(String time){
        editor.putString(SAVE_BONUS_TIME_START_TIME, time);
        editor.commit();
    }


    public String getBonusTimeStartTime() {
        String a = sharedPreferences.getString(SAVE_BONUS_TIME_START_TIME, "");
        return a;
    }
    public void saveBonusTimeEndTime(String time){
        editor.putString(SAVE_BONUS_TIME_END_TIME, time);
        editor.commit();
    }


    public String getBonusTimeEndTime() {
        String a = sharedPreferences.getString(SAVE_BONUS_TIME_END_TIME, "");
        return a;
    }
    public void saveBonusTimeStatus(boolean m_status){
        editor.putBoolean(SAVE_BONUS_TIME_STATUS, m_status);
        editor.commit();
    }
    public boolean getBonusTimeStatus() {
        return sharedPreferences.getBoolean(SAVE_BONUS_TIME_STATUS, false);
    }

    public void saveLastDateTime(Date lastDateTime){
        editor.putLong(SAVE_DATE_TIME, lastDateTime.getTime());
        editor.commit();
    }

    public Date getLastDateTime() {
        long a = sharedPreferences.getLong(SAVE_DATE_TIME, 0L);
        Date theDate = new Date(a);
        return theDate;
    }

    public void saveScreenLockStatusMonday(boolean m_status){
        editor.putBoolean(SAVE_SCREEN_LOCK_STATUS_MONDAY, m_status);
        editor.commit();
    }
    public boolean isScreenLockedMonday() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_LOCK_STATUS_MONDAY, false);
    }
    public void saveScreenLockStatusWednesday(boolean m_status){
        editor.putBoolean(SAVE_SCREEN_LOCK_STATUS_WEDNESDAY, m_status);
        editor.commit();
    }
    public boolean isScreenLockedWednesday() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_LOCK_STATUS_WEDNESDAY, false);
    }

    public void saveScreenLockStatusThursday(boolean m_status){
        editor.putBoolean(SAVE_SCREEN_LOCK_STATUS_THURSDAY, m_status);
        editor.commit();
    }
    public boolean isScreenLockedThursday() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_LOCK_STATUS_THURSDAY, false);
    }

    public void saveScreenLockStatusFriday(boolean m_status){
        editor.putBoolean(SAVE_SCREEN_LOCK_STATUS_FRIDAY, m_status);
        editor.commit();
    }
    public boolean isScreenLockedFriday() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_LOCK_STATUS_FRIDAY, false);
    }

    public void saveScreenLockStatusSaturday(boolean m_status){
        editor.putBoolean(SAVE_SCREEN_LOCK_STATUS_SATURDAY, m_status);
        editor.commit();
    }
    public boolean isScreenLockedSaturday() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_LOCK_STATUS_SATURDAY, false);
    }

    public void saveScreenLockStatusSunday(boolean m_status){
        editor.putBoolean(SAVE_SCREEN_LOCK_STATUS_SUNDAY, m_status);
        editor.commit();
    }
    public boolean isScreenLockedSunday() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_LOCK_STATUS_SUNDAY, false);
    }

    public void saveScreenLockStatusTuesday(boolean m_status){
        editor.putBoolean(SAVE_SCREEN_LOCK_STATUS_TUESDAY, m_status);
        editor.commit();
    }
    public boolean isScreenLockedTuesday() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_LOCK_STATUS_TUESDAY, false);
    }

    public void saveSundayScreenTimeStatus (boolean s_status){
        editor.putBoolean(SAVE_SCREEN_TIME_SUNDAY_STATUS, s_status);
        editor.commit();
    }

    public boolean isSundayScreenTimeOver() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_TIME_SUNDAY_STATUS, false);
    }

    public void saveMondayScreenTimeStatus (boolean m_status){
        editor.putBoolean(SAVE_SCREEN_TIME_MONDAY_STATUS, m_status);
        editor.commit();
    }

    public boolean isMondayScreenTimeOver() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_TIME_MONDAY_STATUS, false);
    }

    public void saveTuesdayScreenTimeStatus (boolean t_status){
        editor.putBoolean(SAVE_SCREEN_TIME_TUESDAY_STATUS, t_status);
        editor.commit();
    }


    public boolean isTuesdayScreenTimeOver() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_TIME_TUESDAY_STATUS, false);
    }
    public void saveWednesdayScreenTimeStatus (boolean w_status){
        editor.putBoolean(SAVE_SCREEN_TIME_WEDNESDAY_STATUS, w_status);
        editor.commit();
    }


    public boolean isWednesdayScreenTimeOver() {
        return sharedPreferences.getBoolean(SAVE_SCREEN_TIME_WEDNESDAY_STATUS, false);
    }
    public void saveThursdayScreenTimeStatus (boolean t_status){
        editor.putBoolean(SAVE_SCREEN_TIME_THURSDAY_STATUS, t_status);
        editor.commit();
    }


    public boolean isThursdayScreenTimeOver() {
        boolean a = sharedPreferences.getBoolean(SAVE_SCREEN_TIME_THURSDAY_STATUS, false);
        return a;
    }

    public void saveFridayScreenTimeStatus (boolean f_status){
        editor.putBoolean(SAVE_SCREEN_TIME_FRIDAY_STATUS, f_status);
        editor.commit();
    }


    public boolean isFridayScreenTimeOver() {
        boolean a = sharedPreferences.getBoolean(SAVE_SCREEN_TIME_FRIDAY_STATUS, false);
        return a;
    }
    public void saveSaturdayScreenTimeStatus (boolean s_status){
        editor.putBoolean(SAVE_SCREEN_TIME_SATURDAY_STATUS, s_status);
        editor.commit();
    }


    public boolean isSaturdayScreenTimeOver() {
        boolean a = sharedPreferences.getBoolean(SAVE_SCREEN_TIME_SATURDAY_STATUS, false);
        return a;
    }

    public void saveLetMeUseAllowedApplicationStatus(boolean m_status){
        editor.putBoolean(SAVE_LET_ME_USE_ALLOWED_APPS, m_status);
        editor.commit();
    }
    public boolean getLetMeUseAllowedApplicationStatus() {
        return sharedPreferences.getBoolean(SAVE_LET_ME_USE_ALLOWED_APPS, false);
    }
}
