//
// Created by 林帅斌 on 2017/12/24.
//

#ifndef CIPHERSO_LOGGER_H
#define CIPHERSO_LOGGER_H

#include <android/log.h>
#include <string>
#include <sstream>

#define INFO(...)__android_log_print(ANDROID_LOG_INFO, "Cipher.so", __VA_ARGS__)
#define ERROR(...)__android_log_print(ANDROID_LOG_ERROR, "Cipher.so", __VA_ARGS__)

template<typename T>
std::string to_string(const T &n) {
    std::ostringstream stream;
    stream << n;
    return stream.str();
}

jobject getApplicationContext(JNIEnv *jniEnv, jobject context) {
    jobject application = NULL;
    jclass application_clz = jniEnv->FindClass("android/app/ActivityThread");
    if (application_clz != NULL) {
        jmethodID current_application_method_id = jniEnv->GetStaticMethodID(application_clz,
                                                                            "currentApplication",
                                                                            "()Landroid/app/Application;");
        if (current_application_method_id != NULL) {
            application = jniEnv->CallStaticObjectMethod(application_clz,
                                                         current_application_method_id);
        }
        jniEnv->DeleteLocalRef(application_clz);
    }
    if (application == NULL) {
        ERROR("ClassNotFoundException: android.app.ActivityThread.class");
        application = context;
    }
    return application;
}

#endif //CIPHERSO_LOGGER_H
