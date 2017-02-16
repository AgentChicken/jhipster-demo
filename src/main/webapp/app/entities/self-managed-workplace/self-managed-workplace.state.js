(function() {
    'use strict';

    angular
        .module('jhipsterdemoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('self-managed-workplace', {
            parent: 'entity',
            url: '/self-managed-workplace',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterdemoApp.selfManagedWorkplace.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/self-managed-workplace/self-managed-workplaces.html',
                    controller: 'SelfManagedWorkplaceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('selfManagedWorkplace');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('self-managed-workplace-detail', {
            parent: 'self-managed-workplace',
            url: '/self-managed-workplace/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterdemoApp.selfManagedWorkplace.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/self-managed-workplace/self-managed-workplace-detail.html',
                    controller: 'SelfManagedWorkplaceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('selfManagedWorkplace');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SelfManagedWorkplace', function($stateParams, SelfManagedWorkplace) {
                    return SelfManagedWorkplace.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'self-managed-workplace',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('self-managed-workplace-detail.edit', {
            parent: 'self-managed-workplace-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/self-managed-workplace/self-managed-workplace-dialog.html',
                    controller: 'SelfManagedWorkplaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SelfManagedWorkplace', function(SelfManagedWorkplace) {
                            return SelfManagedWorkplace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('self-managed-workplace.new', {
            parent: 'self-managed-workplace',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/self-managed-workplace/self-managed-workplace-dialog.html',
                    controller: 'SelfManagedWorkplaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                employees: null,
                                delegates: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('self-managed-workplace', null, { reload: 'self-managed-workplace' });
                }, function() {
                    $state.go('self-managed-workplace');
                });
            }]
        })
        .state('self-managed-workplace.edit', {
            parent: 'self-managed-workplace',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/self-managed-workplace/self-managed-workplace-dialog.html',
                    controller: 'SelfManagedWorkplaceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SelfManagedWorkplace', function(SelfManagedWorkplace) {
                            return SelfManagedWorkplace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('self-managed-workplace', null, { reload: 'self-managed-workplace' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('self-managed-workplace.delete', {
            parent: 'self-managed-workplace',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/self-managed-workplace/self-managed-workplace-delete-dialog.html',
                    controller: 'SelfManagedWorkplaceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SelfManagedWorkplace', function(SelfManagedWorkplace) {
                            return SelfManagedWorkplace.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('self-managed-workplace', null, { reload: 'self-managed-workplace' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
