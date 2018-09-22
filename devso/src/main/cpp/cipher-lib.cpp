//
// Created by 林帅斌 on 2017/12/7.
//

#include "include/cipher-lib.h"
#include "include/extern-keys.h"
#include "include/Environments.h"
#include "include/Encryptor.h"

map<string, string> _map;

Environments *environments;

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    environments = new Environments(env, NULL);
    if (!environments->check()) {
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}

jstring
Java_net_idik_lib_cipher_so_CipherCore_getString(JNIEnv *env, jobject instance, jstring key_) {
    const char *key = env->GetStringUTFChars(key_, 0);
    string keyStr(key);
    string value = _map[keyStr];
    Encryptor *encryptor = new Encryptor(env, environments->getContext());
    const char *result = encryptor->decrypt(SECRET_KEY, value.c_str());
    env->ReleaseStringUTFChars(key_, key);
    return env->NewStringUTF(result);
}

void Java_net_idik_lib_cipher_so_CipherCore_init(JNIEnv *env, jclass type) {
    LOAD_MAP(_map);
}
