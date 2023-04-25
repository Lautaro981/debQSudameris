debFrontApp.config(["$stateProvider", function ($stateProvider) {

    var stateList = {
        name: 'duenio',
        url: '/duenio',
        views: {
            "@" : {
                templateUrl: '/assets/views/duenio.list.html',
                controller: 'duenioCtrl'
            }
        },
        ncyBreadcrumb: {
            label: 'Dueños'
        }
    };

    $stateProvider
        .state(stateList)

}
]);
