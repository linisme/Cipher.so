package net.idik.lib.cipher.so.plugin

import net.idik.lib.cipher.so.extension.CipherSoExt
import net.idik.lib.cipher.so.task.GenerateCipherSoHeaderTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class CipherSoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.add("cipher", new CipherSoExt(project))
        project.tasks.create("printKey", GenerateCipherSoHeaderTask)

    }
}