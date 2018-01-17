//
// Created by 林帅斌 on 2017/12/24.
//

#ifndef CIPHERSO_ENVIRONMENTCHECKER_H
#define CIPHERSO_ENVIRONMENTCHECKER_H

#include <jni.h>

class Environments {

private:

    JNIEnv *jniEnv;
    jobject context;

    jobject getPackageInfo();

    jstring getPackageName();

    bool checkSignature();

public:
    Environments(JNIEnv *env, jobject context);

    bool check();

};


#endif //CIPHERSO_ENVIRONMENTCHECKER_H
