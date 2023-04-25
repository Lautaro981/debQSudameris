debFrontApp.config(["$stateProvider", function ($stateProvider) {

    var stateList = {
        name: 'qCustomerTypes',
        url: '/qCustomerTypes',
        views: {
            "@" : {
                templateUrl: '/assets/views/qCustomerTypes/list.html',
                controller: 'qCustomerTypesListCtrl'
            }
        },
        ncyBreadcrumb: {
            label: 'Tipos de cliente DebQ'
        }
    };
    var stateNew = {
        name: 'qCustomerTypes.new',
        url: '/new',
        views: {
            "@" : {
                templateUrl: '/assets/views/qCustomerTypes/new.html',
                controller: 'qCustomerTypesNewCtrl'
            }
        },
        ncyBreadcrumb: {
            label: 'Nuevo'
        }
    };
    var stateShow = {
        name: 'qCustomerTypes.show',
        url: '/{qCustomerTypesId}',
        views: {
            "@" : {
                templateUrl: '/assets/views/qCustomerTypes/show.html',
                controller: 'qCustomerTypesShowCtrl'
            }
        },
        ncyBreadcrumb: {
            label: 'Tipo de cliente DebQ {{qCustomerTypesId}}'
        }
    };
    var stateEdit = {
        name: 'qCustomerTypes.show.edit',
        url: '/edit',
        views: {
            "@" : {
                templateUrl: '/assets/views/qCustomerTypes/edit.html',
                controller: 'qCustomerTypesEditCtrl'
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
