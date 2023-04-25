module.exports = function (grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        //General cli commands
        exec: {
            onPremise: [].join('&&'),
            cloud: [].join('&&'),
            //Play commands
            playClean: 'echo "play clean" && play clean',
            playCompile: 'echo "play compile" && play compile',
            playStage: 'echo "play stage" && play stage',
            processFiles: 'bash processFiles.sh',
            playCompress: 'echo "compressing stage" && cd target/universal; zip -r stage.zip stage/; cd ../../'
        },
        watch: {
            translation: {
                files: ['conf/messages.es'
                ],
                tasks: ['compile-translations']
            }
        },
        execute: {
            otherTarget:{
                src: ['translationCompiler.js']
            }
        },
        'string-replace': {
            dist: {
                files: [{
                    expand: true,
                    cwd: 'conf/',
                    dest: 'conf/',
                    src: ['logger.xml']
                }],
                options: {
                    replacements: [{
                        pattern: /level\s*=\s*"\s*(DEBUG|TRACE)\s*"/gi,
                        replacement: 'level="INFO"'
                    },
                        {
                            pattern: /<\s*appender-ref\s*ref\s*=\s*"STDOUT"\s*\/\s*>/gi,
                            replacement: ''
                        }]
                }
            }
        }
    });

    //External tasks
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-exec');
    grunt.loadNpmTasks('grunt-execute');
    grunt.loadNpmTasks('grunt-string-replace');

    //Clean tasks
    grunt.registerTask('clean', ['exec:playClean']);

    //Compilation tasks
    grunt.registerTask('compile-translations', ['execute:otherTarget']);

    //Release tasks
    grunt.registerTask('stage-onpremise', ['clean', 'string-replace', 'exec:playStage', 'exec:processFiles', 'exec:playCompress']);

};