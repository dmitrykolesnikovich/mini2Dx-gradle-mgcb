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

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection

class MgcbExtension {
    final DirectoryProperty assetsDirectory;
    final DirectoryProperty projectDirectory;
    FileCollection dlls;
    DirectoryProperty soundsDirectory, musicDirectory;
    String platform;
    boolean compress;
    boolean copyAssets;

    MgcbExtension(Project project) {
        assetsDirectory = project.objects.directoryProperty();
        projectDirectory = project.objects.directoryProperty();
    }

    DirectoryProperty getAssetsDirectory() {
        return assetsDirectory
    }

    DirectoryProperty getSoundsDirectory() {
        return soundsDirectory
    }

    void setSoundsDirectory(DirectoryProperty soundsDirectory) {
        this.soundsDirectory = soundsDirectory
    }

    DirectoryProperty getMusicDirectory() {
        return musicDirectory
    }

    void setMusicDirectory(DirectoryProperty musicDirectory) {
        this.musicDirectory = musicDirectory
    }

    DirectoryProperty getProjectDirectory() {
        return projectDirectory
    }

    boolean getCopyAssets() {
        return copyAssets
    }

    void setCopyAssets(boolean copyAssets) {
        this.copyAssets = copyAssets
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
}
