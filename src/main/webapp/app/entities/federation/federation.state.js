(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('federation', {
            parent: 'entity',
            url: '/federation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterdemoApp.federation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/federation/federations.html',
                    controller: 'FederationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('federation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('federation-detail', {
            parent: 'federation',
            url: '/federation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterdemoApp.federation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/federation/federation-detail.html',
                    controller: 'FederationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('federation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Federation', function($stateParams, Federation) {
                    return Federation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'federation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('federation-detail.edit', {
            parent: 'federation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/federation/federation-dialog.html',
                    controller: 'FederationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Federation', function(Federation) {
                            return Federation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('federation.new', {
            parent: 'federation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/federation/federation-dialog.html',
                    controller: 'FederationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                syndicates: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('federation', null, { reload: 'federation' });
                }, function() {
                    $state.go('federation');
                });
            }]
        })
        .state('federation.edit', {
            parent: 'federation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/federation/federation-dialog.html',
                    controller: 'FederationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Federation', function(Federation) {
                            return Federation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('federation', null, { reload: 'federation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('federation.delete', {
            parent: 'federation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/federation/federation-delete-dialog.html',
                    controller: 'FederationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Federation', function(Federation) {
                            return Federation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('federation', null, { reload: 'federation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
