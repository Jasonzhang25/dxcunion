(function () {
    'use strict';
    angular.module('unionWebApp').factory('authorizedService', AuthFactory);
    AuthFactory.$inject = ['$log','$rootScope', '$http', '$q', 'app.config', 'auth.config'];

    function AuthFactory($log, $rootScope, $http, $q, appConfig, authConf) {
        /*eslint-disable no-unused-vars*/
        var service = {};
        service.isAuthorizedFunction = isAuthorizedFunction;
        service.isAuthenticated = isAuthenticated;
        return service;

        //authorize user
        function isAuthorizedFunction(state, roles, $log) {
            var isAuthorized = false;
            /*
            if (appConfig.env && appConfig.env === "local") {
                return true;
            }
            */
            angular.forEach(roles, function (role, roleKey) {
                angular.forEach(authConf, function (stateList, confKey) {
                    if (confKey.indexOf(role.name) > -1) {
                        angular.forEach(stateList, function (value, key) {
                            if (value.indexOf(state) > -1) {
                                isAuthorized = true;
                                return isAuthorized;
                            }
                        });
                    }
                });
            });

            return isAuthorized;
        }

        function isAuthenticated() {
            var deferred = $q.defer();

            if ($rootScope.isAuthenticated === true) {
                if ($rootScope.isAuthorized ===true){
                    deferred.resolve($rootScope.currentUser);
                }else{
                    $http({
                    method: 'POST',
                    url: appConfig.apiUrl + '/authorization',
                    data: $rootScope.credentials,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                    }).success(function (data) {
                        deferred.resolve(data);
                    }).error(function (data) {
                        $log.error("login failed"+ data);
                        deferred.reject();
                    });
                }
            } else {
                $http({
                    method: 'POST',
                    url: appConfig.apiUrl + '/authentication',
                    data: $rootScope.credentials,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).success(function (data) {
                    deferred.resolve(data);
                }).error(function (data) {
                    $log.error("login failed"+ data);
                    deferred.reject();
                });
               }
            return deferred.promise;
        }
    }
})();