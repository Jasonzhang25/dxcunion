(function () {
    'use strict';

    describe('NavbarController test', function () {
        var scope;

        beforeEach(module('unionWebApp'));
        beforeEach(inject(function ($rootScope) {
            scope = $rootScope.$new();
        }));
        it('should pass', inject(function ($controller) {
            expect(scope.date).toBeUndefined();
            $controller('NavbarController', {
                $scope: scope
            });
            expect(scope.date).toBeDefined();
        }));
    });
})();