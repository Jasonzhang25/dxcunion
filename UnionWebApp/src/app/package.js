(function () { 
 return angular.module('unionWebApp')
.constant('name', "unionWebApp")
.constant('version', "0.1.0")
.constant('dependencies', {"gulp-bump":"^1.0.0"})
.constant('scripts', {"test":"gulp test"})
.constant('devDependencies', {"estraverse":"~4.1.0","gulp":"~3.9.0","gulp-autoprefixer":"~3.0.2","gulp-angular-templatecache":"~1.8.0","del":"~2.0.2","lodash":"~3.10.1","gulp-minify-css":"~1.2.1","gulp-filter":"~3.0.1","gulp-flatten":"~0.2.0","gulp-eslint":"~1.0.0","eslint-plugin-angular":"~0.12.0","gulp-load-plugins":"~0.10.0","gulp-size":"~2.0.0","gulp-uglify":"~1.4.1","gulp-useref":"~1.3.0","gulp-util":"~3.0.6","gulp-ng-annotate":"~1.1.0","gulp-replace":"~0.5.4","gulp-rename":"~1.2.2","gulp-rev":"~6.0.1","gulp-rev-replace":"~0.4.2","gulp-minify-html":"~1.0.4","gulp-inject":"~3.0.0","gulp-protractor":"~1.0.0","gulp-sourcemaps":"~1.6.0","gulp-angular-filesort":"~1.1.1","main-bower-files":"~2.9.0","wiredep":"~2.2.2","karma":"~0.13.10","karma-jasmine":"~0.3.6","karma-phantomjs-launcher":"~0.2.1","phantomjs":"~1.9.18","karma-angular-filesort":"~1.0.0","karma-coverage":"~0.5.2","karma-ng-html2js-preprocessor":"~0.2.0","browser-sync":"~2.9.11","browser-sync-spa":"~1.0.3","http-proxy-middleware":"~0.9.0","chalk":"~1.1.1","uglify-save-license":"~0.4.1","wrench":"~1.5.8"})
.constant('engines', {"node":">=0.10.0"});

})();
