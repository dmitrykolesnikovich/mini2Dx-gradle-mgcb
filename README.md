# gradle-mini2Dx-mgcb
mini2Dx gradle plugin for generating a MonoGame Content Builder project

## Usage

Add the following to your build.gradle

```
buildscript {
    repositories {
	mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath "org.mini2Dx:gradle-mini2Dx-mgcb:1.1.0"
    }
}

...

project(":your-project") {
    apply plugin: "org.mini2Dx.mgcb"
    
    mgcb {
        assetsDirectory = file('../path/to/assets')
        projectDirectory = file('../path/to/monogame/Content')
        dlls = files('../path/to/monogame-pipeline-ext.dll')
		excludes = ['**/.cache/*']
    }
...
```

The full set of options for the extension are as follows:

```
mgcb {
    //Your source assets directory
    assetsDirectory = file('../path/to/assets')
    
    //Your MonoGame Content project directory
    projectDirectory = file('../path/to/monogame/Content')
    
    //Any pipeline extension DLLs required (including mini2Dx)
    dlls = files('../path/to/monogame-pipeline-ext.dll')
    
    //The MonoGame platform
    platform = "DesktopGL"
    
    //True if assets should be compressed
    compress = true
    
    //Override default sounds directory search
    //By default it searches for the following folders with the assets directory:
    //sfx , sounds , sound , soundeffects , sound-effects
    soundsDirectory = file('../path/to/assets/sound')
    
    //Override default music directory search
    //By default it searches for the following folders with the assets directory:
    //music , tracks , soundtrack
    musicDirectory = file('../path/to/assets/music')
}
```

## License

The project is licensed under the Apache License 2.0 and can be found [here](https://github.com/mini2Dx/gradle-mini2Dx-mgcb/blob/master/LICENSE)