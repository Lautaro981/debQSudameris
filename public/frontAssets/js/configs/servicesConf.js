debFrontApp.config(["$stateProvider", function($stateProvider) {
    var stateList = {
        name: 'services',
        url: '/services',
        templateUrl: '/assets/views/services.list.html',
        controller: 'servicesListCtrl',
        ncyBreadcrumb: {
            label: 'Services'
        }
    }
    $stateProvider.state(stateList);
}
]);