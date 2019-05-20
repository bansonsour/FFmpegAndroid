#include <jni.h>
#include <malloc.h>
#include <android/log.h>
#include "../ffmpeg/ffmpeg.h"
#include "../include/libavutil/log.h"

#define ATAG "GourdCmd"
#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO, ATAG, FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR, ATAG, FORMAT,##__VA_ARGS__);

static void log_callback_print(void *ptr, int level, const char *fmt, va_list vl) {
    LOGI("log_callback_print msg--->%s", fmt, vl);
}

#define ANDROID_LOG(level, TAG, ...)    ((void)__android_log_vprint(level, TAG, __VA_ARGS__))

#define SYS_LOG_TAG "GourdCmd"

static void av_log_callback_print(void *ptr, int level, const char *fmt, va_list vl) {
    switch (level) {
        case AV_LOG_DEBUG:
            ANDROID_LOG(ANDROID_LOG_VERBOSE, SYS_LOG_TAG, fmt, vl);
            break;
        case AV_LOG_VERBOSE:
            ANDROID_LOG(ANDROID_LOG_DEBUG, SYS_LOG_TAG, fmt, vl);
            break;
        case AV_LOG_INFO:
            ANDROID_LOG(ANDROID_LOG_INFO, SYS_LOG_TAG, fmt, vl);
            break;
        case AV_LOG_WARNING:
            ANDROID_LOG(ANDROID_LOG_WARN, SYS_LOG_TAG, fmt, vl);
            break;
        case AV_LOG_ERROR:
            ANDROID_LOG(ANDROID_LOG_ERROR, SYS_LOG_TAG, fmt, vl);
            break;
    }
}

JNIEXPORT jint JNICALL Java_com_frank_ffmpeg_FFmpegCmd_processCmd(JNIEnv *env, jclass obj, jobjectArray commands) {
    int argc = (*env)->GetArrayLength(env, commands);
    char **argv = (char **) malloc(argc * sizeof(char *));
    int i;
    int result;
    for (i = 0; i < argc; i++) {
        jstring jstr = (jstring) (*env)->GetObjectArrayElement(env, commands, i);
        char *temp = (char *) (*env)->GetStringUTFChars(env, jstr, 0);
        argv[i] = malloc(1024);
        strcpy(argv[i], temp);
        (*env)->ReleaseStringUTFChars(env, jstr, temp);
    }
    av_log_set_callback(av_log_callback_print);
    //执行ffmpeg命令
    result = run(argc, argv);
    //释放内存
    for (i = 0; i < argc; i++) {
        free(argv[i]);
    }
    free(argv);
    return result;
}

JNIEXPORT jint JNICALL Java_com_frank_ffmpeg_FFmpegCmd_processConfig(JNIEnv *env, jclass obj) {
    char *info = (char *) malloc(40000);
    memset(info, 0, 40000);
    av_register_all();

    AVCodec *c_temp = av_codec_next(NULL);

    while (c_temp != NULL) {
        if (c_temp->decode != NULL) {
            strcat(info, "[Decode]");
        } else {
            strcat(info, "[Encode]");
        }
        switch (c_temp->type) {
            case AVMEDIA_TYPE_VIDEO:
                strcat(info, "[Video]");
                break;
            case AVMEDIA_TYPE_AUDIO:
                strcat(info, "[Audeo]");
                break;
            default:
                strcat(info, "[Other]");
                break;
        }
        sprintf(info, "%s %10s\n", info, c_temp->name);
        strcat(info, c_temp->name);
        c_temp = c_temp->next;
    }
    LOGE("Codecs info=: %s", info);
    puts(info);
    free(info);
    return 0;
}