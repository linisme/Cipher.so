package net.idik.lib.cipher.so.plugin

import net.idik.lib.cipher.so.extension.CipherSoExt
import net.idik.lib.cipher.so.task.GenerateCipherSoHeaderTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.AbstractCopyTask
import org.gradle.api.tasks.Copy

class CipherSoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.add("cipher", new CipherSoExt(project))
        project.tasks.create("printKey", GenerateCipherSoHeaderTask)

        prepareCipherEnvironment(project)

        setupProjectNativeSupport(project)

    }

    static def prepareCipherEnvironment(Project project) {
        project.afterEvaluate {
            def android = project.extensions.android
//            android.applicationVariants.all { variant ->

            def copyCppTask = project.tasks.create("copyCpp", Copy) {
                group "cipher.so"
                from project.rootProject.subprojects.find {
                    it.name == "devso"
                }.file("src/main/cpp")
                into new File(project.buildDir, "cipher.so/src/main/cpp")
            }
            def copyCMakeListsTask = project.tasks.create("copyCMakeList", Copy) {
                group "cipher.so"
                from project.rootProject.subprojects.find {
                    it.name == "devso"
                }.file("CMakeLists.txt")
                into new File(project.buildDir, "cipher.so")

            }

            project.getTasksByName("printKey", false).each {
                it.dependsOn copyCppTask
//                it.dependsOn copyCMakeListsTask
            }

//            }
        }
    }

    static def setupProjectNativeSupport(Project project) {
        project.android
                .defaultConfig
                .externalNativeBuild {

            cmake {
                String currentFlags = cppFlags ?: ""
                cppFlags currentFlags
            }

        }

        project.copy {
            from project.rootProject.subprojects.find {
                it.name == "devso"
            }.file("CMakeLists.txt")
            into new File(project.buildDir, "cipher.so")
        }

        project.android.externalNativeBuild {
            cmake {
                path "${project.buildDir.path}/cipher.so/CMakeLists.txt"
            }
        }

    }
}