(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('FederationDetailController', FederationDetailController);

    FederationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Federation', 'Syndicate'];

    function FederationDetailController($scope, $rootScope, $stateParams, previousState, entity, Federation, Syndicate) {
        var vm = this;

        vm.federation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterdemoApp:federationUpdate', function(event, result) {
            vm.federation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
