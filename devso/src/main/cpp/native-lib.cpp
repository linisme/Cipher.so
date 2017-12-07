#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_net_idik_lib_cipher_so_devso_Devso_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
