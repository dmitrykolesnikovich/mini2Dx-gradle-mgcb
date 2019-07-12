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

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class MgcbTask extends DefaultTask {

    @InputDirectory
    private DirectoryProperty assetsDirectoryProperty;
    @InputDirectory
    private DirectoryProperty soundsDirectoryProperty;
    @InputDirectory
    private DirectoryProperty musicDirectoryProperty;
    @InputFiles
    private FileCollection dllsProperty;
    @Input
    private String platform;
    @Input
    private boolean compress;
    @Input
    private boolean copyAssets;

    @OutputDirectory
    private DirectoryProperty projectDirectoryProperty;

    private File soundsDir, musicDir;

    @TaskAction
    public void run() {
        final File assetsDir = assetsDirectoryProperty.getAsFile();
        final File outputDir = projectDirectoryProperty.getAsFile();

        final File [] fallbackSoundsDir = [
                new File(assetsDir, "sfx"),
                new File(assetsDir, "sounds"),
                new File(assetsDir, "sound"),
                new File(assetsDir, "soundeffects"),
                new File(assetsDir, "sound-effects")
        ];
        final File [] fallbackMusicDir = [
                new File(assetsDir, "tracks"),
                new File(assetsDir, "music"),
                new File(assetsDir, "soundtrack")
        ];

        if(soundsDirectoryProperty == null || !soundsDirectoryProperty.isPresent()) {
            soundsDir = soundsDirectoryProperty.getAsFile();
        } else {
            for(int i = 0; i < fallbackSoundsDir.length; i++) {
                if(fallbackSoundsDir[i].exists()) {
                    soundsDir = fallbackSoundsDir[i];
                    break;
                }
            }
        }
        if(musicDirectoryProperty == null || !musicDirectoryProperty.isPresent()) {
            musicDir = musicDirectoryProperty.getAsFile();
        } else {
            for(int i = 0; i < fallbackMusicDir.length; i++) {
                if(fallbackMusicDir[i].exists()) {
                    musicDir = fallbackMusicDir[i];
                    break;
                }
            }
        }

        final PrintWriter printWriter = new PrintWriter(new File(outputDir, "Content.mgcb"), "UTF-8");
        printWriter.println();
        printWriter.println("#----------------------------- Global Properties ----------------------------#");
        printWriter.println();
        printWriter.println("/outputDir:bin/\$(Platform)")
        printWriter.println("/intermediateDir:obj/\$(Platform)")
        printWriter.println("/platform:" + platform)
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

        appendAssets(assetsDir, printWriter);
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
        return file.getAbsolutePath().startsWith(soundsDir.getAbsolutePath());
    }

    private boolean isMusic(File file) {
        if(musicDir == null) {
            return false;
        }
        return file.getAbsolutePath().startsWith(musicDir.getAbsolutePath());
    }

    private String getRelativePath(File outputDir, File file) {
        return new File(outputDir).toURI().relativize(new File(file).toURI()).getPath();
    }

    private String getSuffix(File file) {
        return file.getName().substring(file.getName().lastIndexOf('.'));
    }
}
