//
// Created by 林帅斌 on 2018/1/17.
//

#include "include/Encryptor.h"
#include "include/utils.h"

Encryptor::Encryptor(JNIEnv *jniEnv, jobject context) {
    this->jniEnv = jniEnv;
    this->context = getApplicationContext(jniEnv, context);
}

std::string Encryptor::decrypt(char *raw) {





    return std::string();
}
