(function () {
    'use strict';

    angular.module('unionWebApp')
        .factory('employeeService', TheFactory);

    TheFactory.$inject = ['$http', '$q', '$log', 'app.config'];

    function TheFactory($http, $q, $log, appConfig) {
        /*eslint-disable no-unused-vars*/
        var aipurl = appConfig.apiUrl;
        var service = {};
        service.getEmployeeByCriteria = getEmployeeByCriteria;
        service.updateEmployee = updateEmployee;
        return service;  
        
        //Get User by Critria
        function getEmployeeByCriteria(data) {
            var deferred = $q.defer();
            $http({
                method: 'POST',
                url: aipurl + '/getEmpByCriteria',
                data: data,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(deferred.reject);
            return deferred.promise;
        }
    
        //Update Employee 
        function updateEmployee(data) {
            var deferred = $q.defer();
            $http({
                method: 'POST',
                url: aipurl + '/updateEmp',
                data: data,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (data) {
                if (!data.success) {
                    deferred.resolve(data);
                    $log.info("update success!");
                } else {
                    $log.info("update failed!");
                }
            });
            return deferred.promise;
        }
    }

})();