(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('SyndicateDialogController', SyndicateDialogController);

    SyndicateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Syndicate', 'SelfManagedWorkplace'];

    function SyndicateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Syndicate, SelfManagedWorkplace) {
        var vm = this;

        vm.syndicate = entity;
        vm.clear = clear;
        vm.save = save;
        vm.workplaces = SelfManagedWorkplace.query({filter: 'syndicate-is-null'});
        $q.all([vm.syndicate.$promise, vm.workplaces.$promise]).then(function() {
            if (!vm.syndicate.workplace || !vm.syndicate.workplace.id) {
                return $q.reject();
            }
            return SelfManagedWorkplace.get({id : vm.syndicate.workplace.id}).$promise;
        }).then(function(workplace) {
            vm.workplaces.push(workplace);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.syndicate.id !== null) {
                Syndicate.update(vm.syndicate, onSaveSuccess, onSaveError);
            } else {
                Syndicate.save(vm.syndicate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterdemoApp:syndicateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
