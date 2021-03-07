Pod::Spec.new do |spec|
    spec.name                     = 'imagecore'
    spec.version                  = '0.0.3'
    spec.homepage                 = 'https://www.github.com/CoronelGlober/ImageCore'
    spec.source                   = { :git => "https://www.github.com/CoronelGlober/ImageCore.git", :tag => "Cocoapods/#{spec.name}/#{spec.version}" }
    spec.authors                  = ''
    spec.license                  = { :type => "MIT", :file => "LICENSE" }
    spec.summary                  = 'Image core frame for iOS'

    spec.static_framework         = true
    spec.vendored_frameworks      = "build/cocoapods/framework/imagecore.framework"
    spec.libraries                = "c++"
    spec.module_name              = "#{spec.name}_umbrella"

                

                

    spec.pod_target_xcconfig = {
        'KOTLIN_TARGET[sdk=iphonesimulator*]' => 'ios_x64',
        'KOTLIN_TARGET[sdk=iphoneos*]' => 'ios_arm',
        'KOTLIN_TARGET[sdk=watchsimulator*]' => 'watchos_x86',
        'KOTLIN_TARGET[sdk=watchos*]' => 'watchos_arm',
        'KOTLIN_TARGET[sdk=appletvsimulator*]' => 'tvos_x64',
        'KOTLIN_TARGET[sdk=appletvos*]' => 'tvos_arm64',
        'KOTLIN_TARGET[sdk=macosx*]' => 'macos_x64'
    }

    spec.script_phases = [
        {
            :name => 'Build imagecore',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" :imagecore:syncFramewoÅrk \
                    -Pkotlin.native.cocoapods.target=$KOTLIN_TARGET \
                    -Pkotlin.native.cocoapods.configuration=$CONFIGURATION \
                    -Pkotlin.native.cocoapods.cflags="$OTHER_CFLAGS" \Å
                    -Pkotlin.native.cocoapods.paths.headers="$HEADER_SEARCH_PATHS" \
                    -Pkotlin.native.cocoapods.paths.frameworks="$FRAMEWORK_SEARCH_PATHS"
            SCRIPT
        }
    ]
end