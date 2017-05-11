(function() {
  'use strict';

  angular
    .module('unionWebApp')
    .controller('EmployeeUploadController', TheController);

    /** @ngInject */
    /*eslint-disable no-unused-vars*/     
    function TheController($scope,$q,userService,$log) {
      var vm = this;
      vm.uploadEmployee = uploadEmployeeFile;
      vm.uploadtype = "incremental";
      vm.errorMsgs = {};
      vm.errorMsgsShow = false;
      //----function---
      function uploadEmployeeFile() {
        var successAlert = { type: "success", msg: "Upload Employee File Successfully!", show: true };
        var failAlert = { type: "danger", msg: "An error ocurred to upload file. Please try again.", show: true };      
        var file = $scope.uploadFile;
        userService.uploadFile(file,vm.uploadtype).then(function (data) {
          if(data.status=="failed"){
            vm.errorMsgs= data.messages;
            vm.errorMsgsShow = true;
          }else{
            vm.errorMsgsShow = false;
            vm.alerts = [];
            vm.alerts.push(successAlert);
          }
        }, function (reason) {
            vm.alerts = [];
            vm.alerts.push(failAlert);
        });
        //return userService.uploadFile(file);
      }
    }
 })();
