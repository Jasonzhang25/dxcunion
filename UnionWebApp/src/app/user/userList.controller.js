(function() {
  'use strict';

  angular
    .module('unionWebApp')
    .controller('UserListController', TheController);

  /** @ngInject */
  function TheController($rootScope,userService, $log) {
    /*eslint-disable no-unused-vars*/
    var vm = this;
    vm.searchUser = searchUser;
    vm.clearSearch = clearSearch;
    vm.searchCriteria = "noValue";
    vm.searchValue = "";
    vm.searchUserCriteria = {};
    initialize();

    //--initialize-------------------------------------------
    function initialize() {
        getUserList();
        return false;
    }
    //--Search event------------------------------------------------
    function searchUser() {
      var noFound = { type: "danger", msg: "No user records were found that match the search criteria.", show: true };
      if(vm.searchValue!="" &&
         vm.searchCriteria!="" &&
         vm.searchCriteria !=="noValue"){
          vm.userList = [];
          buildsearchdata();
          userService.getUserByCriteria(vm.searchUserCriteria).then(function (data) {
                vm.userList = data;
            }, function (error) {
                $log.info(error);
                vm.alerts.push(noFound);
            });
      }else{
            getUserList();
      }
    }
    //--Clear event------------------------------------------------
    function clearSearch() {
      vm.searchCriteria ="noValue";
      vm.searchValue = "";
      initialize();
    }
    //--Get User List View------------------------------------------
    function getUserList() {
        var noFound = { type: "danger", msg: "No user records were found that match the search criteria.", show: true };
        vm.userList=[];
        vm.alerts = [];
        userService.getUserAll().then(function (data) {
        if(data.length>0){
          vm.userList = data;
        }else{
          vm.alerts.push(noFound);
        }
          
        }, function (error) {
          $log.info(error);
          vm.alerts.push(error);
        });
    }
    //-------------build user object for search--------------------------------------
    function buildsearchdata(){
        if(vm.searchCriteria=="Email"){

            vm.searchUserCriteria={"email":vm.searchValue};
        }
        if(vm.searchCriteria=="UserId"){

            vm.searchUserCriteria={"eid":vm.searchValue};
        }
        if(vm.searchCriteria=="Addedby"){

            vm.searchUserCriteria={"addedby":vm.searchValue};
        }

    }
  }

})();
