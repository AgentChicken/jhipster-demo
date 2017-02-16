(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('FederationDeleteController',FederationDeleteController);

    FederationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Federation'];

    function FederationDeleteController($uibModalInstance, entity, Federation) {
        var vm = this;

        vm.federation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Federation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
