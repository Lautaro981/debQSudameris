debFrontApp.config(["$stateProvider", function($stateProvider) {
        var stateList = {
            name: 'support',
            url: '/support',
            templateUrl: '/assets/views/support.html',
            controller: 'supportCtrl',
            ncyBreadcrumb: {
                label: 'Support'
            }
        }
        $stateProvider.state(stateList);
    }
]);