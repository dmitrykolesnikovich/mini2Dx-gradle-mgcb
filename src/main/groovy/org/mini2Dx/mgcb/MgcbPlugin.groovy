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

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

class MgcbPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extension = project.extensions.create("mgcb", MgcbExtension, project);

        project.getTasks().register("generateMonoGameContent", MgcbTask.class, new Action<MgcbTask>() {
            public void execute(MgcbTask task) {
                task.assetsDirectoryProperty = extension.assetsDirectory;
                task.projectDirectoryProperty = extension.projectDirectory;

                task.soundsDirectoryProperty = extension.soundsDirectory;
                task.musicDirectoryProperty = extension.musicDirectory;

                task.excludes = extension.excludes;

                task.dllsProperty = extension.dlls;
                task.platform = extension.platform;
                task.compress = extension.compress;

                task.sfxQuality = extension.sfxQuality == null ? "Medium" : extension.sfxQuality;
                task.musicQuality = extension.musicQuality == null ? "Medium" : extension.musicQuality;
            }
        });
    }
}
