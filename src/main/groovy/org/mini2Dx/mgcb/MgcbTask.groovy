/*******************************************************************************
 * Copyright 2019 Thomas Cashman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.mgcb

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class MgcbTask extends DefaultTask {

    @InputDirectory
    DirectoryProperty assetsDirectoryProperty;
    @Optional
    @InputDirectory
    DirectoryProperty soundsDirectoryProperty;
    @Optional
    @InputDirectory
    DirectoryProperty musicDirectoryProperty;

    @Optional
    @InputDirectory
    FileCollection dllsProperty;
    @Optional
    @Input
    String platform;
    @Input
    boolean compress;

    @OutputDirectory
    DirectoryProperty projectDirectoryProperty;

    private File soundsDir, musicDir;

    @TaskAction
    public void run() {
        final File assetsDir = assetsDirectoryProperty.get().asFile;
        final File outputDir = projectDirectoryProperty.get().asFile;

        if(soundsDirectoryProperty == null || !soundsDirectoryProperty.isPresent()) {
            final File [] fallbackSoundsDir = [
                    new File(assetsDir, "sfx"),
                    new File(assetsDir, "sounds"),
                    new File(assetsDir, "sound"),
                    new File(assetsDir, "soundeffects"),
                    new File(assetsDir, "sound-effects"),
                    assetsDir
            ];

            for(int i = 0; i < fallbackSoundsDir.length; i++) {
                if(fallbackSoundsDir[i].exists()) {
                    soundsDir = fallbackSoundsDir[i];
                    break;
                }
            }
        } else {
            soundsDir = soundsDirectoryProperty.get().getAsFile();
        }

        if(musicDirectoryProperty == null || !musicDirectoryProperty.isPresent()) {
            final File [] fallbackMusicDir = [
                    new File(assetsDir, "tracks"),
                    new File(assetsDir, "music"),
                    new File(assetsDir, "soundtrack"),
                    assetsDir
            ];

            for(int i = 0; i < fallbackMusicDir.length; i++) {
                if(fallbackMusicDir[i].exists()) {
                    musicDir = fallbackMusicDir[i];
                    break;
                }
            }
        } else {
            musicDir = musicDirectoryProperty.get().getAsFile();
        }

        final PrintWriter printWriter = new PrintWriter(new File(outputDir, "Content.mgcb"), "UTF-8");
        printWriter.println();
        printWriter.println("#----------------------------- Global Properties ----------------------------#");
        printWriter.println();
        printWriter.println("/outputDir:bin/\$(Platform)")
        printWriter.println("/intermediateDir:obj/\$(Platform)")
        printWriter.println("/platform:" + (platform == null ? "DesktopGL" : platform))
        printWriter.println("/config:")
        printWriter.println("/profile:Reach")
        printWriter.println("/compress:" + (compress ? "True" : "False"))

        printWriter.println();
        printWriter.println("#-------------------------------- References --------------------------------#");
        printWriter.println();

        if(dllsProperty != null && !dllsProperty.isEmpty()) {
            for(File dllFile : dllsProperty.files) {
                printWriter.println("/reference:" + getRelativePath(outputDir, dllFile));
            }
        }

        printWriter.println();
        printWriter.println("#---------------------------------- Content ---------------------------------#");
        printWriter.println();

        appendAssets(outputDir, assetsDir, printWriter);

        printWriter.flush();
        printWriter.close();
    }

    private void appendAssets(File outputDir, File directory, PrintWriter printWriter) {
        if(!directory.isDirectory()) {
            return;
        }

        for(File file : directory.listFiles()) {
            if(file.isDirectory()) {
                appendAssets(outputDir, file, printWriter);
                continue;
            }

            if(isGlslShader(file)) {
                continue;
            }

            if(isSound(file)) {
                appendSound(outputDir, file, printWriter);
            } else if(isMusic(file)) {
                appendMusic(outputDir, file, printWriter);
            } else {
                appendRaw(outputDir, file, printWriter);
            }
            printWriter.println();
        }
    }

    private void appendRaw(File outputDir, File file, PrintWriter printWriter) {
        printWriter.println("#begin " + getRelativePath(outputDir, file));
        printWriter.println("/importer:mini2DxContentImporter");
        printWriter.println("/processor:mini2DxContentProcessor");
        printWriter.println("/build:" + getRelativePath(outputDir, file) + ";" + file.getName() + getSuffix(file));
    }

    private void appendSound(File outputDir, File file, PrintWriter printWriter) {
        printWriter.println("#begin " + getRelativePath(outputDir, file));
        if(file.getPath().endsWith(".wav")) {
            printWriter.println("/importer:WavImporter");
        } else if(file.getPath().endsWith(".mp3")) {
            printWriter.println("/importer:Mp3Importer");
        } else {
            printWriter.println("/importer:OggImporter");
        }
        printWriter.println("/processor:SoundEffectProcessor");
        printWriter.println("/processorParam:Quality=Best");
        printWriter.println("/build:" + getRelativePath(outputDir, file) + ";" + file.getName() + getSuffix(file));
    }

    private void appendMusic(File outputDir, File file, PrintWriter printWriter) {
        printWriter.println("#begin " + getRelativePath(outputDir, file));
        if(file.getPath().endsWith(".wav")) {
            printWriter.println("/importer:WavImporter");
        } else if(file.getPath().endsWith(".mp3")) {
            printWriter.println("/importer:Mp3Importer");
        } else {
            printWriter.println("/importer:OggImporter");
        }
        printWriter.println("/processor:SongProcessor");
        printWriter.println("/processorParam:Quality=Best");
        printWriter.println("/build:" + getRelativePath(outputDir, file) + ";" + file.getName() + getSuffix(file));
    }

    private boolean isGlslShader(File file) {
        return file.getAbsolutePath().endsWith(".glsl");
    }

    private boolean isSound(File file) {
        if(soundsDir == null) {
            return false;
        }
        if(file.getAbsolutePath().startsWith(soundsDir.getAbsolutePath())) {
            if(file.getName().endsWith(".ogg")) {
                return true;
            }
            if(file.getName().endsWith(".wav")) {
                return true;
            }
            if(file.getName().endsWith(".mp3")) {
                return true;
            }
        }
        return false;
    }

    private boolean isMusic(File file) {
        if(musicDir == null) {
            return false;
        }
        if(file.getAbsolutePath().startsWith(musicDir.getAbsolutePath())) {
            if(file.getName().endsWith(".ogg")) {
                return true;
            }
            if(file.getName().endsWith(".wav")) {
                return true;
            }
            if(file.getName().endsWith(".mp3")) {
                return true;
            }
        }
        return false;
    }

    private String getRelativePath(File outputDir, File file) {
        return new File(outputDir.getAbsolutePath()).toURI().relativize(new File(file.getAbsolutePath()).toURI()).getPath();
    }

    private String getSuffix(File file) {
        return file.getName().substring(file.getName().lastIndexOf('.'));
    }
}
