(function () {
    'use strict';

    angular.module('unionWebApp')
        .factory('userService', TheFactory);

    TheFactory.$inject = ['$http', '$q', '$log', 'app.config'];

    function TheFactory($http, $q, $log,appConfig) {
        /*eslint-disable no-unused-vars*/
        var aipurl = appConfig.apiUrl;
        var service = {};
        service.getUserByID = getUserByID;
        service.addUser = addUser;
        service.getUserByCriteria = getUserByCriteria;
        service.updateUserByID = updateUserByID;
        service.getUserAll = getUserAll;
        service.uploadFile = uploadFile;
        return service;
        
        function addUser(data) {
            var deferred = $q.defer();
            $http({
                method: 'POST',
                url: aipurl + '/addOperator',
                data: data,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (data) {
                $log.error(data);
                deferred.reject();
            });
            return deferred.promise;
        }

        //update User by ID
        function updateUserByID(mydata) {
            var deferred = $q.defer();
            $http({
                method: 'POST',
                url: aipurl + '/updateOperator',
                data: mydata,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (data) {
                $log.error(data);
                deferred.reject();
            });
            return deferred.promise;
        }
        
        //Get User by Critria
        function getUserByCriteria(data) {
            var deferred = $q.defer();
            $http({
                method: 'POST',
                url: aipurl + '/getOpeByCriteria',
                data: data,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(deferred.reject);
            return deferred.promise;
        }
    
        //Get User by ID
        function getUserByID(id) {
            var deferred = $q.defer();
            var data = {"id":id};
            $http({
                method: 'POST',
                url: aipurl + '/getOpeById/',
                data: data,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(deferred.reject);
            return deferred.promise;
        }

        //Get all users
        function getUserAll(){
            var deferred = $q.defer();
            var url = aipurl + '/getOpeAll/';
            $http.get(url).success(function (data) {
                deferred.resolve(data);
            }).error(deferred.reject);
            return deferred.promise;
        }
        //updateFile
        function uploadFile(file, type){
            var deferred = $q.defer();
            var fd = new FormData();
            fd.append('file', file);
            var uploadUrl = aipurl + '/uploadEmpPkg?type='+type;
            $http({
                method: 'POST',
                url: uploadUrl,
                headers: {'Content-Type': undefined},
                data: fd,
                transformRequest: function(data, headersGetterFunction) {
                                return data;
                 }
            }).success(function (data) {
                
                deferred.resolve(data);
            }).error(deferred.reject);
            return deferred.promise;

        }
    }
})();