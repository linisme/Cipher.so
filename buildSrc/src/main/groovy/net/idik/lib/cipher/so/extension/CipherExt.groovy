package net.idik.lib.cipher.so.extension

import org.gradle.api.Project

class CipherExt {

    SoExt soExt

    Project project

    CipherExt(Project project) {
        this.soExt = new SoExt(project)
        this.project = project
    }

    def so(Closure closure) {
//        closure.resolveStrategy = Closure.DELEGATE_ONLY
//        closure.delegate = soExt
        project.configure(soExt, closure)
    }
}