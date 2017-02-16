(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('SelfManagedWorkplaceDeleteController',SelfManagedWorkplaceDeleteController);

    SelfManagedWorkplaceDeleteController.$inject = ['$uibModalInstance', 'entity', 'SelfManagedWorkplace'];

    function SelfManagedWorkplaceDeleteController($uibModalInstance, entity, SelfManagedWorkplace) {
        var vm = this;

        vm.selfManagedWorkplace = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SelfManagedWorkplace.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
