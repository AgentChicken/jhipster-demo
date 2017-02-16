(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('SyndicateDeleteController',SyndicateDeleteController);

    SyndicateDeleteController.$inject = ['$uibModalInstance', 'entity', 'Syndicate'];

    function SyndicateDeleteController($uibModalInstance, entity, Syndicate) {
        var vm = this;

        vm.syndicate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Syndicate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
