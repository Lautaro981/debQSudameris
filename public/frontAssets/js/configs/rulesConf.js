debFrontApp.config(["$stateProvider", function ($stateProvider) {

    var stateList = {
        name: 'rules',
        url: '/rules',
        views: {
            "@" : {
                templateUrl: '/assets/views/rules/list.html',
                controller: 'rulesListCtrl'
            }
        },
        ncyBreadcrumb: {
            label: 'Reglas'
        }
    };
    var stateNew = {
        name: 'rules.new',
        url: '/new',
        views: {
            "@" : {
                templateUrl: '/assets/views/rules/new.html',
                controller: 'rulesNewCtrl'
            }
        },
        ncyBreadcrumb: {
            label: 'Nueva'
        }
    };
    var stateShow = {
        name: 'rules.show',
        url: '/{rulesId}',
        views: {
            "@" : {
                templateUrl: '/assets/views/rules/show.html',
                controller: 'rulesShowCtrl'
            }
        },
        ncyBreadcrumb: {
            label: 'Reglas {{rulesId}}'
        }
    };
    var stateEdit = {
        name: 'rules.show.edit',
        url: '/edit',
        views: {
            "@" : {
                templateUrl: '/assets/views/rules/edit.html',
                controller: 'rulesEditCtrl'
            }
        },
        ncyBreadcrumb: {
            label: 'Editar'
        }
    };
    $stateProvider
        .state(stateList)
        .state(stateNew)
        .state(stateShow)
        .state(stateEdit);
}
]);
