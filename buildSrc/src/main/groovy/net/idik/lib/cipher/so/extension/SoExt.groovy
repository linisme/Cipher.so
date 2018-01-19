package net.idik.lib.cipher.so.extension

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class SoExt {

    NamedDomainObjectContainer<KeyExt> keys

    String signature = ""
    String encryptSeed = "Cipher.so@DEFAULT"

    SoExt(Project project) {
        keys = project.container(KeyExt)
    }

    def keys(Closure closure) {
        keys.configure closure
    }

    @Override
    public String toString() {
        return "SoExt{" +
                "keys=" + keys +
                ", signature='" + signature + '\'' +
                ", secretKey='" + encryptSeed + '\'' +
                '}';
    }
}