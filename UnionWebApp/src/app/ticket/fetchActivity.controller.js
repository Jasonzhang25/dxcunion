(function() {
  'use strict';

  angular
    .module('unionWebApp')
    .controller('FetchActivityController', TheController);

  /** @ngInject */
  /*eslint-disable no-unused-vars*/     
  function TheController($rootScope,$log,employeeService) {
    var vm = this;
    vm.searchEmployee = searchEmployee;
    vm.searchCriteria = "EmployeeId";
    vm.searchValue = "";
    vm.searchSubstitute = searchSubstitute;
    vm.isFetchedStatus = isFetchedStatus;
    vm.updateEmployee = updateEmployee;
    vm.substituteId = "";
    vm.employee={};
    vm.employeeList= [];

    //--Search event------------------------------------------------
    function searchEmployee() {
        vm.updatealert= { show: false };
        var noFound = { type: "danger", msg: "No records were found that match the search criteria.", show: true };
        vm.alerts = [];
        
        if(vm.searchValue!="" &&
         vm.searchCriteria!="" &&
         vm.searchCriteria !=="noValue"){
           buildsearchdata(); 
           employeeService.getEmployeeByCriteria(vm.employee).then(function (data) {
           if(data.length>0){
              vm.employeeList = data;
           }else{
              vm.employeeList=[];
              vm.alerts.push(noFound);
           }
            
         }, function (error) {
            $log.info(error);
            vm.alerts.push(error);
         });
        }
    }

    function searchSubstitute() {
        vm.updatealert= { show: false };
        var noFound = { type: "danger", msg: "No records were found that match the search criteria.", show: true };
        var substitute = {"eid":vm.substituteId};
        vm.subalerts=[];
        if(vm.substituteId!=""){
          employeeService.getEmployeeByCriteria(substitute).then(function (data) {
          if(data.length>0){
            vm.substituteName = data[0].name;
          }else{
            vm.substituteName ="";
            vm.subalerts.push(noFound);
          }
          }, function (error) {
            $log.info(error);
            vm.subalerts.push(error);
          });
        }
    }
    
    function buildsearchdata(){
        if(vm.searchCriteria=="EmployeeId"){

            vm.employee={"eid":vm.searchValue};
        }
        if(vm.searchCriteria=="EmployeeEmail"){

            vm.employee={"email":vm.searchValue};
        }
    }

    function isFetchedStatus(){
      var retflg = true;
      angular.forEach(vm.employeeList[0], function(value, key) {
        if(key=="status" && value=="Fetched"){
           retflg=false;
        }
      });
      return retflg;
    }

     function updateEmployee(){
        buildEmployee();
        employeeService.updateEmployee(vm.employee).then(function (data) {
            vm.updatealert= { type: "success", msg: "Update successfully!", show: true };
        }, function (reason) {
            vm.updatealert= { type: "danger", msg: "The fectch activity could not be updated. Please try again.!", show: true };
        });
    }

    function buildEmployee(){
      vm.employee = vm.employeeList[0];
      vm.employee.operatorid = $rootScope.currentUser.id;
      vm.employee.status='Fetched';
      if(vm.substituteId!="" && vm.substituteName!=""){
        vm.employee.substituteid = vm.substituteId;
        vm.employee.substitutename = vm.substituteName;
      }
    }
  }
})();