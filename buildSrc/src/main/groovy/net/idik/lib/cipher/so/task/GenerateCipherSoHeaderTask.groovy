package net.idik.lib.cipher.so.task

import net.idik.lib.cipher.so.extension.KeyExt
import net.idik.lib.cipher.so.generater.CipherSoHeaderBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class GenerateCipherSoHeaderTask extends DefaultTask {

    private static final String TARGET_FILE_NAME = "extern-keys.h"
    private static final String GROUP_NAME = 'cipher.so'

    @OutputDirectory
    File outputDir

    @Input
    List<KeyExt> keyExts

    @Input
    String signature

    @Input
    String secretKey

    GenerateCipherSoHeaderTask() {
        group = GROUP_NAME
    }

    @TaskAction
    void generate() {
        def targetFile = new File(outputDir, TARGET_FILE_NAME)
        def writer = new FileWriter(targetFile)
        new CipherSoHeaderBuilder(TARGET_FILE_NAME, keyExts)
                .setSignature(signature)
                .setSecretKey(secretKey)
                .build()
                .each {
            writer.append(it)
        }
        writer.flush()
        writer.close()
    }


}