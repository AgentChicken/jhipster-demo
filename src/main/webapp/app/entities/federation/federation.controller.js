(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('FederationController', FederationController);

    FederationController.$inject = ['Federation'];

    function FederationController(Federation) {
        var vm = this;

        vm.federations = [];

        loadAll();

        function loadAll() {
            Federation.query(function(result) {
                vm.federations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
