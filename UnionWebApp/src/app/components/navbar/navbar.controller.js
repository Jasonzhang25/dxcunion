(function () {
    'use strict';

    angular.module('unionWebApp').controller('NavbarController', NavController);

    NavController.$inject = ['$log', '$rootScope', '$cookies', '$state', '$window', 'authorizedService', 'app.config'];
    function NavController($log, $rootScope, $cookies,$state, $window, authorizedService, appConfig) {
    /*eslint-disable no-unused-vars*/
    var vm = this;
    vm.$state = $state;
    vm.signOut = signOut;

    var reject = $rootScope.$on(appConfig.currentUserChangedEvent, function () {
        initNavBarItems();
    });
    //-----------------function signOut-----------------------
    function signOut() {
        $rootScope.currentUser = {};
        $rootScope.isAuthenticated = false;

       // authorizedService.signOut().then(function (data) {
            $cookies["km_ni"] = "";
            delete $cookies["km_ni"];
            $state.go('login');
            $log.info('logout.');
       // }, function () {
       //     $log.info('Unable to logout, please try closing your browser.');
       // });
    }

    //-----------------function initNavBarItems-----------------------
    function initNavBarItems() { 
        vm.navbarItems = [];
        var TicketManagement = {};
        var parentTicketStateName = 'ticket';
        var ticketStateName = 'ticket.fetch';
        TicketManagement.show = isAuthorized(ticketStateName);
        TicketManagement.parentState = parentTicketStateName;
        TicketManagement.sref = ticketStateName;
        TicketManagement.text = "Ticket Management";
        vm.navbarItems.push(TicketManagement);

        var UserManagementItem = {};
        var parentUserStateName = 'user';
        var userStateName = 'user.list';
        UserManagementItem.show = isAuthorized(userStateName);
        UserManagementItem.parentState = parentUserStateName;
        UserManagementItem.sref = userStateName;
        UserManagementItem.text = "User Management";
        vm.navbarItems.push(UserManagementItem);
    }

    //-----------------function isAuthorized-----------------------
    function isAuthorized(stateName) {
        return authorizedService.isAuthorizedFunction(stateName, $rootScope.currentUser.roles);
    }
    }    
})();