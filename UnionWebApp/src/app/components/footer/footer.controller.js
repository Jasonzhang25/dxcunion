(function () {
    'use strict';
    angular.module('unionWebApp')
    .controller('FooterController', FootController);
    
    FootController.$inject = ['$rootScope','$log','version'];

    function FootController($rootScope,$log,version) {
      var vm = this;   
      vm.unionVersion=version;
    }
})();