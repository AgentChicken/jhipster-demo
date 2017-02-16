(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .controller('SyndicateController', SyndicateController);

    SyndicateController.$inject = ['Syndicate'];

    function SyndicateController(Syndicate) {
        var vm = this;

        vm.syndicates = [];

        loadAll();

        function loadAll() {
            Syndicate.query(function(result) {
                vm.syndicates = result;
                vm.searchQuery = null;
            });
        }
    }
})();
