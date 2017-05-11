(function() {
  'use strict';

  angular
    .module('unionWebApp')
    .run(runBlock);

  
  runBlock.$inject = ['$rootScope', '$state','$log',
        'app.config', 'authorizedService'];
        
  function runBlock($rootScope, $state, $log, appConfig,authorizedService) {
  /*eslint-disable no-unused-vars*/
    $log.debug('runBlock end');
    $rootScope.currentUser = {};
    $rootScope.isAuthenticated = false;
    $rootScope.isAuthorized = false;

    var register = $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {
        $log.info("toState.name = " + toState.name);
        if (toState.name === 'authentication' || toState.name === 'authorization'  || toState==='home') {
            return;
        }
        if(toState.name === 'login'){
          $rootScope.currentUser = {};
          $rootScope.credentials = {};
          $rootScope.$broadcast(appConfig.clearMessageEvent);
          $rootScope.$broadcast(appConfig.currentUserChangedEvent);
          return;
        }
        if ($rootScope.isAuthorized) {
            $log.info("User was authorized");
            $rootScope.isAuthorized = false;
            return;
        }
        event.preventDefault();
        authorizedService.isAuthenticated().then(function (data) {
            $log.info("User was not authorized. Authorizing: " + data.email);
              if (data.roles!=undefined && authorizedService.isAuthorizedFunction(toState.name, data.roles)) {
                  $rootScope.currentUser = data;
                  $rootScope.isAuthenticated = true;
                  $rootScope.$broadcast(appConfig.currentUserChangedEvent);
                  $rootScope.isAuthorized = true;
                  $log.info("Authorization was successful. Logging into the application now." + toState.name);
                  $state.go(toState.name, toParams);
              } else {
                  $state.go('authorization');
              }
          }, function (reason) {
                  $state.go('login');
              });
      });

  }
})();
