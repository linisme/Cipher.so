package net.idik.lib.cipher.so.task

import net.idik.lib.cipher.so.extension.CipherSoExt
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GenerateCipherSoHeaderTask extends DefaultTask {

    public static final String TARGET_FILE_NAME = "extension_keys.h"
    private File targetFile

    GenerateCipherSoHeaderTask() {
        group = 'cipher.so'
        targetFile = findTargetNativeFile(project.buildDir)
    }

    @TaskAction
    void generate() {
        def keyContainer = project.cipher as CipherSoExt
        def writer = new FileWriter(targetFile)
        new SoHeaderBuilder(TARGET_FILE_NAME, keyContainer.keys.asList()).build().each {
            writer.append(it)
        }
        writer.flush()
        writer.close()
    }

    private File findTargetNativeFile(File rootFile) {
        File dir = new File(rootFile, "cipher.so/include/main/cpp")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return new File(dir, TARGET_FILE_NAME)
    }

}