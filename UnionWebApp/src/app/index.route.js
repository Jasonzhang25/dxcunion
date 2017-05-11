(function() {
  'use strict';

  angular
    .module('unionWebApp')
    .config(routerConfig);

  /** @ngInject */
  function routerConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('authorization', {
          url: '/authorization',
          templateUrl: 'app/auth/authorization.html',
          controller: 'AuthorizationController',
          controllerAs: 'vm'
     }).state('home', {
        url: '/',
        templateUrl: 'app/login/login.html',
        controller: 'LoginController',
        controllerAs: 'main'
      }).state('login', {
        url:'/login',
        templateUrl:'app/login/login.html',
        controller:'LoginController',
        controllerAs: 'vm'
      }).state('user', {
            abstract: true,
            url: '/user',
            template: '<ui-view/>'
      }).state('user.list', {
        url:'/list',
        templateUrl:'app/user/userList.html',
        controller:'UserListController',
        controllerAs: 'vm'
      }).state('user.add', {
        url:'/add',
        templateUrl:'app/user/userAdd.html',
        controller:'UserAddController',
        controllerAs: 'vm'
      }).state('user.edit', {
        url:'/edit/{ID}',
        templateUrl:'app/user/userEdit.html',
        controller:'UserEditController',
        controllerAs: 'vm'
      }).state('user.uploademployee', {
        url:'/uploademployee',
        templateUrl:'app/user/employeeUpload.html',
        controller:'EmployeeUploadController',
        controllerAs: 'vm'
      }).state('ticket', {
            abstract: true,
            url: '/ticket',
            template: '<ui-view/>'
      }).state('ticket.fetch', {
        url:'/fetch',
        templateUrl:'app/ticket/fetchActivity.html',
        controller:'FetchActivityController',
        controllerAs: 'vm'
      })
    $urlRouterProvider.otherwise('/');
  }

})();
