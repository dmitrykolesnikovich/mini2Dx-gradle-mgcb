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

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.ListProperty

class MgcbExtension {
    final DirectoryProperty assetsDirectory;
    final DirectoryProperty projectDirectory;
    final DirectoryProperty soundsDirectory, musicDirectory;
    final ListProperty<String> excludes;

    FileCollection dlls;
    String platform;
    boolean compress;

    String sfxQuality;
    String musicQuality;

    MgcbExtension(Project project) {
        assetsDirectory = project.objects.directoryProperty();
        projectDirectory = project.objects.directoryProperty();

        soundsDirectory = project.objects.directoryProperty();
        musicDirectory = project.objects.directoryProperty();

        excludes = project.objects.listProperty(String.class);
    }

    DirectoryProperty getAssetsDirectory() {
        return assetsDirectory
    }

    DirectoryProperty getSoundsDirectory() {
        return soundsDirectory
    }

    DirectoryProperty getMusicDirectory() {
        return musicDirectory
    }

    DirectoryProperty getProjectDirectory() {
        return projectDirectory
    }

    ListProperty<String> getExcludes() {
        return excludes
    }

    FileCollection getDlls() {
        return dlls
    }

    void setDlls(FileCollection dlls) {
        this.dlls = dlls
    }

    String getPlatform() {
        return platform
    }

    void setPlatform(String platform) {
        this.platform = platform
    }

    boolean getCompress() {
        return compress
    }

    void setCompress(boolean compress) {
        this.compress = compress
    }

    String getSfxQuality() {
        return sfxQuality
    }

    void setSfxQuality(String sfxQuality) {
        this.sfxQuality = sfxQuality
    }

    String getMusicQuality() {
        return musicQuality
    }

    void setMusicQuality(String musicQuality) {
        this.musicQuality = musicQuality
    }
}
