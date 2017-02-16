(function() {
    'use strict';
    angular
        .module('jhipsterdemoApp')
        .factory('Federation', Federation);

    Federation.$inject = ['$resource'];

    function Federation ($resource) {
        var resourceUrl =  'api/federations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
