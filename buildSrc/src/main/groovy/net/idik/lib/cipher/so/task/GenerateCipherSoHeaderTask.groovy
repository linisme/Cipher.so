package net.idik.lib.cipher.so.task

import net.idik.lib.cipher.so.extension.CipherSoExt
import net.idik.lib.cipher.so.utils.IOUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GenerateCipherSoHeaderTask extends DefaultTask {

    private static final String TARGET_FILE_NAME = "extern-keys.h"
    private static final String GROUP_NAME = 'cipher.so'

    private File targetFile

    GenerateCipherSoHeaderTask() {
        group = GROUP_NAME
        targetFile = IOUtils.getNativeHeaderFile(project, TARGET_FILE_NAME)
    }

    @TaskAction
    void generate() {
        def keyContainer = project.cipher as CipherSoExt
        def writer = new FileWriter(targetFile)
        new CipherSoHeaderBuilder(TARGET_FILE_NAME, keyContainer.keys.asList()).build().each {
            writer.append(it)
        }
        writer.flush()
        writer.close()
    }


}