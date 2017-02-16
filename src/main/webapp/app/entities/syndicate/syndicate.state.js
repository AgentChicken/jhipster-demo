(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('syndicate', {
            parent: 'entity',
            url: '/syndicate',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterdemoApp.syndicate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/syndicate/syndicates.html',
                    controller: 'SyndicateController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('syndicate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('syndicate-detail', {
            parent: 'syndicate',
            url: '/syndicate/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterdemoApp.syndicate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/syndicate/syndicate-detail.html',
                    controller: 'SyndicateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('syndicate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Syndicate', function($stateParams, Syndicate) {
                    return Syndicate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'syndicate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('syndicate-detail.edit', {
            parent: 'syndicate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/syndicate/syndicate-dialog.html',
                    controller: 'SyndicateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Syndicate', function(Syndicate) {
                            return Syndicate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('syndicate.new', {
            parent: 'syndicate',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/syndicate/syndicate-dialog.html',
                    controller: 'SyndicateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                workplaces: null,
                                delegates: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('syndicate', null, { reload: 'syndicate' });
                }, function() {
                    $state.go('syndicate');
                });
            }]
        })
        .state('syndicate.edit', {
            parent: 'syndicate',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/syndicate/syndicate-dialog.html',
                    controller: 'SyndicateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Syndicate', function(Syndicate) {
                            return Syndicate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('syndicate', null, { reload: 'syndicate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('syndicate.delete', {
            parent: 'syndicate',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/syndicate/syndicate-delete-dialog.html',
                    controller: 'SyndicateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Syndicate', function(Syndicate) {
                            return Syndicate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('syndicate', null, { reload: 'syndicate' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
