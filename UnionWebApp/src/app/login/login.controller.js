(function () {
    'use strict';

  angular
     .module('unionWebApp')
     .controller('LoginController', TheController)
     .directive('ngEnter', function () {
      return function (scope, element, attrs) {
          element.bind("keydown keypress", function (event) {
              if(event.which === 13) {
                  scope.$apply(function (){
                      scope.$eval(attrs.ngEnter);
                  });
                  event.preventDefault();
              }
          });
      };
    });


    TheController.$inject = ['$rootScope', '$cookies', '$state', 'authorizedService', 'app.config'];
/*
    function TheKeyEvent =function(keyEvent){
      if(keyEvent.which ===13){
        alert('Test');
      }
    }
    */
    function TheController($rootScope, $cookies, $state,authorizedService,appConfig) {
        /*eslint-disable no-unused-vars*/
        var vm = this;
        vm.loginUserEmail = "";
        vm.setCookie = setCookie;
        vm.credentials = {
          "useremail":"", 
          "password":""
        };

       var errorregister = $rootScope.$on(appConfig.usernotExistEvent, function () {
            vm.credentials = {
              "useremail":"", 
              "password":""
            };
            vm.authenticatealert= { type: "danger", msg: "You are not authorized!Please contact administrator to give you the specific role.", show: true };
        });

       var register = $rootScope.$on(appConfig.notAuthenticatedEvent, function () {
            vm.credentials = {
              "useremail":"", 
              "password":""
            };
            vm.authenticatealert= { type: "danger", msg: "You are not authenticated! Email or password is incorrect!", show: true };
        });
       var clearerrorregister = $rootScope.$on(appConfig.clearMessageEvent, function () {
            vm.credentials = {
              "useremail":"", 
              "password":""
            };
            $rootScope.credentials = vm.credentials;
            vm.authenticatealert= { show: false };
        });

        function setCookie() {
         $cookies["km_ni"] = vm.credentials.useremail;
         $rootScope.credentials = vm.credentials;
         $state.go('ticket.fetch');
        }

    }
})();