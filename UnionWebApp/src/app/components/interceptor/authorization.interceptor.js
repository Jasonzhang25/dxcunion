(function () {
	'use strict';

	angular.module('unionWebApp')
		.factory('authorizationInterceptor', TheFactory);

	TheFactory.$inject = ['$q', '$injector'];
	function TheFactory($q, $injector) {
		var authorizationInterceptor = {
			responseError: function (response) {
				if (response.status === 402) {
					$injector.get('$state').go('authorization');
				}
				return $q.reject(response);
			}
		};

		return authorizationInterceptor;
	}

})();