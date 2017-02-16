(function() {
    'use strict';
    angular
        .module('jhipsterdemoApp')
        .factory('SelfManagedWorkplace', SelfManagedWorkplace);

    SelfManagedWorkplace.$inject = ['$resource'];

    function SelfManagedWorkplace ($resource) {
        var resourceUrl =  'api/self-managed-workplaces/:id';

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
