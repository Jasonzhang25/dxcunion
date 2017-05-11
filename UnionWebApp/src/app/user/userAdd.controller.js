(function() {
  'use strict';

  angular
    .module('unionWebApp')
    .controller('UserAddController', TheController);

  /** @ngInject */
  /*eslint-disable no-unused-vars*/     
  function TheController($q,$rootScope, userService,roleService) {
     var vm = this;
     vm.user = {};
     vm.addUser = addUser;
     vm.checkIfSelectedAdmin = checkIfSelectedAdmin;
     initialize();

     //---function---
     function getAllRolePromise() {
        return roleService.getAllRole();
     }

     function initialize() {
         vm.isAdmin = true;
         vm.roleConfigs = [];
         var promises = [getAllRolePromise()];
         $q.all(promises).then(function (datas) {
              var roleConfigs = datas[0];
              vm.roleConfigs = roleConfigs;
         });
     }

    //--click Add------------------------------------------------
    function addUser() {
        var roles = [];
        angular.forEach(vm.roleConfigs, function (roleConfig, key) {
            if (roleConfig.isChecked) {
                roles.push(roleConfig);
            }
        });
        if(roles.length>0){
            vm.user.roles = roles;
        }
        vm.user.addedby =  $rootScope.currentUser.email;
        var successAlert = { type: "success", msg: "User has been added succesfully.", show: true };
        var failAlert = { type: "danger", msg: "An error ocurred, user could not be added. Please try again.", show: true };      
        userService.addUser(vm.user).then(function (data) {
            vm.alerts = [];
            vm.alerts.push(successAlert);
            //clear
            vm.user = {};
            vm.user.roles = {};
            vm.isAdmin = true;
        }, function (reason) {
            vm.alerts = [];
            vm.alerts.push(failAlert);
        });
    }
    //-----------------checkIfSelectedAdmin------------
    function checkIfSelectedAdmin(index) {
            var role = vm.roleConfigs[index];
            if (role.isChecked && role.name === 'Admin') {
                vm.isAdmin = true;
            } else {
                vm.isAdmin = false;
            }

            if (vm.isAdmin) {
                angular.forEach(vm.roleConfigs, function (roleConfig, key) {
                    if (key !== index) {
                        roleConfig.isChecked = false;
                        roleConfig.isDisabled = true;
                    } else {
                        roleConfig.isDisabled = false;
                    }
                });
            } else {
                angular.forEach(vm.roleConfigs, function (roleConfig, key) {
                    roleConfig.isDisabled = false;
                });
            }
        }
  }
})();