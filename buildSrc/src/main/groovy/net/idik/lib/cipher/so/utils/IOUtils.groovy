package net.idik.lib.cipher.so.utils

import org.gradle.api.Project

class IOUtils {

    static File getNativeHeaderFile(Project project, String fileName) {
        return getFile(project, "src/main/cpp/include/$fileName")
    }

    static File getFile(Project project, String path) {
        File file = new File(project.getBuildDir(), "cipher.so/$path")
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        return file
    }
}