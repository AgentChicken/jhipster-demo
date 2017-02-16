(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('SelfManagedWorkplaceDetailController', SelfManagedWorkplaceDetailController);

    SelfManagedWorkplaceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SelfManagedWorkplace'];

    function SelfManagedWorkplaceDetailController($scope, $rootScope, $stateParams, previousState, entity, SelfManagedWorkplace) {
        var vm = this;

        vm.selfManagedWorkplace = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterdemoApp:selfManagedWorkplaceUpdate', function(event, result) {
            vm.selfManagedWorkplace = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
