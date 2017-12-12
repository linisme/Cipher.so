package net.idik.lib.cipher.so.utils

import org.gradle.api.Project

class IOUtils {

    static File getNativeHeaderDir(Project project) {
        File file = getFile(project, "src/main/cpp/include")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    static File getFile(Project project, String path) {
        File file = new File(project.getBuildDir(), "cipher.so/$path")
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        return file
    }
}