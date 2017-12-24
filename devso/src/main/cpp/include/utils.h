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

#endif //CIPHERSO_LOGGER_H
