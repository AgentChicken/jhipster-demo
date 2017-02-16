(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('SyndicateDetailController', SyndicateDetailController);

    SyndicateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Syndicate', 'SelfManagedWorkplace'];

    function SyndicateDetailController($scope, $rootScope, $stateParams, previousState, entity, Syndicate, SelfManagedWorkplace) {
        var vm = this;

        vm.syndicate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterdemoApp:syndicateUpdate', function(event, result) {
            vm.syndicate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
