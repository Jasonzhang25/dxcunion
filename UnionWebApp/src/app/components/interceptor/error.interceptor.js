(function () {
    'use strict';

    angular.module('unionWebApp')
		.factory('errorInterceptor', theFactory);

    theFactory.$inject = ['$rootScope', '$q', '$log', 'app.config'];
    function theFactory($rootScope, $q, $log, appConfig) {
        var errorInterceptor = {
            responseError: function (response) {
                if (response.status === 402) {

                    $rootScope.$broadcast(appConfig.usernotExistEvent);
                }
                if (response.status === 401) {

                    $rootScope.$broadcast(appConfig.notAuthenticatedEvent);
                }
                return $q.reject(response.data);
            }
        };

        return errorInterceptor;
    }

})();