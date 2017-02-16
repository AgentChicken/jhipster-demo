(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('SelfManagedWorkplaceDialogController', SelfManagedWorkplaceDialogController);

    SelfManagedWorkplaceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SelfManagedWorkplace'];

    function SelfManagedWorkplaceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SelfManagedWorkplace) {
        var vm = this;

        vm.selfManagedWorkplace = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.selfManagedWorkplace.id !== null) {
                SelfManagedWorkplace.update(vm.selfManagedWorkplace, onSaveSuccess, onSaveError);
            } else {
                SelfManagedWorkplace.save(vm.selfManagedWorkplace, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterdemoApp:selfManagedWorkplaceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
