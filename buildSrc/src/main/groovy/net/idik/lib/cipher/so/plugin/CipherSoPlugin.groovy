package net.idik.lib.cipher.so.plugin

import com.android.build.gradle.AppExtension
import net.idik.lib.cipher.so.extension.CipherSoExt
import net.idik.lib.cipher.so.task.GenerateCipherSoHeaderTask
import net.idik.lib.cipher.so.task.GenerateJavaClientFileTask

import org.gradle.api.Plugin
import org.gradle.api.Project

class CipherSoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.add("cipher", new CipherSoExt(project))

        prepareCipherEnvironment(project)

        setupProjectNativeSupport(project)

    }

    static def prepareCipherEnvironment(Project project) {
//        project.afterEvaluate {

        def android = project.extensions.findByType(AppExtension)

//        def copyCppTask = project.tasks.create("copyCpp", Copy) {
//            group "cipher.so"
//            from project.rootProject.subprojects.find {
//                it.name == "devso"
//            }.file("src/main/cpp")
//            exclude "include/extern-keys.h"
//            into new File(project.buildDir, "cipher.so/src/main/cpp")
//        }
//        def copyCMakeListsTask = project.tasks.create("copyCMakeList", Copy) {
//            group "cipher.so"
//            from project.rootProject.subprojects.find {
//                it.name == "devso"
//            }.file("CMakeLists.txt").toPath()
//            into new File(project.buildDir, "cipher.so")
//        }


        android.applicationVariants.all { variant ->
            def generateCipherSoExternTask = project.tasks.create("generate${capitalize(variant.name)}CipherSoHeader", GenerateCipherSoHeaderTask)
            project.getTasksByName("pre${capitalize(variant.name)}Build", false).each {
//                it.dependsOn copyCppTask
//                it.dependsOn copyCMakeListsTask
                it.dependsOn generateCipherSoExternTask
            }
            def generateJavaClientTask = project.tasks.create("generate${capitalize(variant.name)}JavaClient", GenerateJavaClientFileTask)
            def outputDir = new File("${project.buildDir}/generated/source/cipher.so/${variant.name}")
            generateJavaClientTask.configure {
                it.keyExts = project.cipher.keys.asList()
                it.outputDir = outputDir
            }
            variant.registerJavaGeneratingTask(generateJavaClientTask, outputDir)

        }

//        }
    }

    static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase(Locale.US) + str.substring(1)
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
            }.file("src/main/cpp")
            exclude "include/extern-keys.h"
            into new File(project.buildDir, "cipher.so/src/main/cpp")
        }

        project.copy {
            from project.rootProject.subprojects.find {
                it.name == "devso"
            }.file("CMakeLists.txt").toPath()
            into new File(project.buildDir, "cipher.so")
        }

        project.android.externalNativeBuild {
            cmake {
                path "${project.buildDir.path}/cipher.so/CMakeLists.txt"
            }
        }

    }
}