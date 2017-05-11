/**
 *  This file contains the variables used in other gulp files
 *  which defines tasks
 *  By design, we only put there very generic config values
 *  which are used in several places to keep good readability
 *  of the tasks
 */
 'use strict';

var gulp = require('gulp');
var ngConfig = require('gulp-ng-config');
var path = require('path');
var gutil = require('gulp-util');

/**
 *  The main paths of your project handle these with care
 */
exports.paths = {
  src: 'src',
  dist: 'dist',
  tmp: '.tmp',
  e2e: 'e2e'
};

/**
 *  Wiredep is the lib which inject bower dependencies in your project
 *  Mainly used to inject script tags in the index.html but also used
 *  to inject css preprocessor deps and js files in karma
 */
exports.wiredep = {
  exclude: [/\/bootstrap\.js$/],
  directory: 'bower_components'
};

/**
 *  Common implementation for an error handler of a Gulp plugin
 */
exports.errorHandler = function(title) {
  'use strict';

  return function(err) {
    gutil.log(gutil.colors.red('[' + title + ']'), err.toString());
    this.emit('end');
  };
};
gulp.task('config', function () {
    gulp.src(path.join(exports.paths.src,'/app/components/config/config.json'))
             .pipe(ngConfig('unionWebApp', {
                 environment: 'local',
                 createModule: false,
                 wrap: true
             }))
            .pipe(gulp.dest(path.join(exports.paths.src, '/app')));
});

gulp.task('tagversion', function () {
    gulp.src(path.join(exports.paths.src,  '/package.json'))
         .pipe(ngConfig('unionWebApp', {
             environment: 'local',
             createModule: false,
             wrap: true
         }))
        .pipe(gulp.dest(path.join(exports.paths.src, '/app')));
});

gulp.task('authconfig', function () {
    gulp.src(path.join(exports.paths.src,'/app/components/config/authorizeConf.json'))
           .pipe(ngConfig('unionWebApp', {
               environment: 'local',
               createModule: false,
               wrap: true
           }))
           .pipe(gulp.dest(path.join(exports.paths.src, '/app')));
});
