(function () {
    'use strict';

    angular.module('unionWebApp')
        .factory('roleService', TheFactory);

    TheFactory.$inject = ['$http', '$q', '$log', 'app.config'];

    function TheFactory($http, $q, $log, appConfig) {
        /*eslint-disable no-unused-vars*/
        var url = appConfig.apiUrl + "/getRoleAll";
        var service = {};
        service.getAllRole = getAllRole;
        return service;
        //--getAllRole--------------------------------
        function getAllRole(){
            var deferred = $q.defer();
            $http.get(url).success(function (data) {
                deferred.resolve(data);
            }).error(function (data) {
                deferred.reject(data);
            });
            return deferred.promise;
        }
    }
})();
