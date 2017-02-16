(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('SelfManagedWorkplaceController', SelfManagedWorkplaceController);

    SelfManagedWorkplaceController.$inject = ['SelfManagedWorkplace'];

    function SelfManagedWorkplaceController(SelfManagedWorkplace) {
        var vm = this;

        vm.selfManagedWorkplaces = [];

        loadAll();

        function loadAll() {
            SelfManagedWorkplace.query(function(result) {
                vm.selfManagedWorkplaces = result;
                vm.searchQuery = null;
            });
        }
    }
})();
