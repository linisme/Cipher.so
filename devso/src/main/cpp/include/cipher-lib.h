//
// Created by 林帅斌 on 2017/12/7.
//

#ifndef CIPHERSO_CIPHER_LIB_H
#define CIPHERSO_CIPHER_LIB_H

#include <jni.h>
#include <map>
#include <string>

using namespace std;

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint
JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved);

JNIEXPORT void JNICALL
Java_net_idik_lib_cipher_so_CipherCore_init(JNIEnv *env, jclass type);

JNIEXPORT jstring
JNICALL
Java_net_idik_lib_cipher_so_CipherCore_getString(JNIEnv *env, jobject
instance, jstring key_);

#ifdef __cplusplus
};
#endif



#endif //CIPHERSO_CIPHER_LIB_H
