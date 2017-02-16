(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('FederationDialogController', FederationDialogController);

    FederationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Federation', 'Syndicate'];

    function FederationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Federation, Syndicate) {
        var vm = this;

        vm.federation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.syndicates = Syndicate.query({filter: 'federation-is-null'});
        $q.all([vm.federation.$promise, vm.syndicates.$promise]).then(function() {
            if (!vm.federation.syndicate || !vm.federation.syndicate.id) {
                return $q.reject();
            }
            return Syndicate.get({id : vm.federation.syndicate.id}).$promise;
        }).then(function(syndicate) {
            vm.syndicates.push(syndicate);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.federation.id !== null) {
                Federation.update(vm.federation, onSaveSuccess, onSaveError);
            } else {
                Federation.save(vm.federation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterdemoApp:federationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
