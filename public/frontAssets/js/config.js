debFrontApp.config(function(toastrConfig, $stateProvider, $urlRouterProvider, $breadcrumbProvider) {
    	// Notificaciones
        angular.extend(toastrConfig, {
            positionClass: 'toast-bottom-right'
        });

        // Breadcrumbs
        $breadcrumbProvider.setOptions({
            templateUrl: '/assets/frontAssets/templates/breadcrumbs.html'
        });
        
        // Router
        var home = {
            name: 'home',
            url: '/',
            template: '<h1> Home template </h1>',
            ncyBreadcrumb: {
                label: 'Home'
            }
        }
        var notifications = {
            name: 'notifications',
            url: '/notifications',
            templateUrl: '/assets/views/notifications.html',
            controller: 'notificationCtrl',
            ncyBreadcrumb: {
                label: 'Notificaciones'
            }
        }
        var support = {
            name: 'support',
            url: '/support',
            templateUrl: '/assets/views/support.html',
            controller: 'supportCtrl',
            ncyBreadcrumb: {
                label: 'Soporte'
            }
        }
        var users = {
        	name: 'users',
        	url: '/users',
        	templateUrl: '/assets/views/users.html',
            controller: 'exampleCtrl',
            ncyBreadcrumb: {
                label: 'Usuarios'
            }
        }
        var newUser = {
            name: 'users.new',
            url: '/newuser',
            views: {
                "@" : {
                    template: '<h1> Nuevo Usuario </h1>'
                }
            },
            ncyBreadcrumb: {
                label: 'Nuevo'
            }
        }
        var viewUser = {
            name: 'users.detail',
            url: '/{userId}?from',
            views: {
                "@" : {
                    template: '<h1> Detalles de usuario {{userId}} </h2>',
                    controller: 'userDetailCtrl'
                }
            },
            ncyBreadcrumb: {
                label: 'User {{userId}}',
                parent: function($scope){
                    return $scope.from || 'users';
                }
            }
        }
        var editUser = {
            name: 'users.detail.edit',
            url: '/edit',
            views:{
                "@" : {
                    template: "<h1> Edición de usuario {{userId}}",
                    controller: 'userEditCtrl'
                }
            },
            ncyBreadcrumb:{
                label: 'Edición'
            }
        }
        // Registro los estados
        $stateProvider.state(home);
        $stateProvider.state(notifications);
        $stateProvider.state(users);
        $stateProvider.state(newUser);
        $stateProvider.state(viewUser);
        $stateProvider.state(editUser);
        $stateProvider.state(support);
        $urlRouterProvider.otherwise("/home");
});