//
// Created by me on 12/09/2020.
//
#include <cstring>
#include <jni.h>
#include <android/log.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_miketmg_graphviewer_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    // Just for simplicity, we do this right away; correct way would do it in
    // another thread...

    return env->NewStringUTF("Hello from JNI LIBS!");
}