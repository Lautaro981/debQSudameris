debFrontApp.controller('userDetailCtrl', ["$scope", "$state", "$stateParams",
    function($scope, $state, $stateParams) {
    	$scope.userId = $stateParams.userId;
    }
]);