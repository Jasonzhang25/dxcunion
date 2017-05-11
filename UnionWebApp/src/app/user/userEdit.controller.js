(function() {
  'use strict';

  angular
    .module('unionWebApp')
    .controller('UserEditController', TheController);

  /** @ngInject */
  function TheController($rootScope, $q, $log, $stateParams,userService, roleService) {
    /*eslint-disable no-unused-vars*/
    var vm = this;
    var userId = $stateParams.ID;
    vm.user = {};
    vm.editUser = editUser;
    vm.checkIfSelectedAdmin = checkIfSelectedAdmin;

    initialize();

      //----------------getAllRolePromise function----------------
      function getAllRolePromise() {
            return roleService.getAllRole();
      }

      //----------------getUserByIdPromise function----------------
      function getUserByIdPromise() {
            return userService.getUserByID(userId);
      }
      //----------------initialize function----------------
      function initialize(){
          vm.roleConfigs = [];
          var promises = [getUserByIdPromise(), getAllRolePromise()];
          $q.all(promises).then(function (datas) {
             var user = datas[0];
             var roleConfigs = datas[1];
             vm.user = user;
             vm.roleConfigs = roleConfigs;
             determinTheSelectedRoles(user, roleConfigs);
          });
      }
      //----------------determinTheSelectedRoles function----------------
      function determinTheSelectedRoles(user, roleConfigs){
          angular.forEach(roleConfigs, function (roleConfig, key) {
            angular.forEach(user.roles, function (role, key) {
               if (role.name === roleConfig.name) {
                  roleConfig.isChecked = true;
                  roleConfig.isDisabled = false;
               }
            });
          });
          /*
           angular.forEach(user.roles, function (role, key) {
                angular.forEach(roleConfigs, function (roleConfig, key) {
                if (role.name === roleConfig.name) {
                    roleConfig.isChecked = true;
                    roleConfig.isDisabled = false;
                } else {
                    roleConfig.isChecked = false;
                    roleConfig.isDisabled = false;
                }
             });
           });
*/
      }
      //----------------editUser function----------------
      function editUser(){
        //rebuild the user role
        vm.user.roles = [];
        angular.forEach(vm.roleConfigs, function (roleConfig, key) {
            if (roleConfig.isChecked) {
                vm.user.roles.push(roleConfig);
            }
        });
        vm.user.addedby =  $rootScope.currentUser.email;
        var successAlert = { type: "success", msg: "The user info has been updated successfully.", show: true };
        var failAlert = { type: "danger", msg: "An error ocurred, user profile update could not be completed. Please try again.", show: true };
        userService.updateUserByID(vm.user).then(function (data) {
            vm.alerts = [];
            vm.alerts.push(successAlert);
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
                if(roleConfig.name==='Admin'){
                   roleConfig.isChecked = false;
                }
                roleConfig.isDisabled = false;
            });
        }
      }
    }
})();