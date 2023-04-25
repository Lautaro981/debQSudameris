debFrontApp.config(["$stateProvider", function ($stateProvider) {

    var stateList = {
        name: 'gato',
        url: '/gato',
        views: {
            "@" : {
                templateUrl: '/assets/views/gato.list.html',
                controller: 'gatoCtrl'
            }
        },
        ncyBreadcrumb: {
            label: 'Gatos'
        }
    };

    $stateProvider
        .state(stateList)

}
]);
