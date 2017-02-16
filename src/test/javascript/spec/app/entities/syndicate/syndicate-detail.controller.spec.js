'use strict';

describe('Controller Tests', function() {

    describe('Syndicate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSyndicate, MockSelfManagedWorkplace;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSyndicate = jasmine.createSpy('MockSyndicate');
            MockSelfManagedWorkplace = jasmine.createSpy('MockSelfManagedWorkplace');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Syndicate': MockSyndicate,
                'SelfManagedWorkplace': MockSelfManagedWorkplace
            };
            createController = function() {
                $injector.get('$controller')("SyndicateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterdemoApp:syndicateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
