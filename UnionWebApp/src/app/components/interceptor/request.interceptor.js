(function () {
    'use strict';

    angular.module('unionWebApp')
        .factory('requestInterceptor', theFactory);

    theFactory.$inject = ['$rootScope', '$cookies', 'app.config'];
    function theFactory($rootScope, $cookies, appConfig) {
        var emailAddressInterceptor = {
            request: function (config) {
                if (appConfig.env && appConfig.env === "local") {
                    config.headers.SM_UNIVERSALID =  $cookies["km_ni"];
                } else {
                    config.headers.SM_UNIVERSALID = $cookies["km_ni"];
                }
                return config;
            }
        };
        return emailAddressInterceptor;
    }
})();