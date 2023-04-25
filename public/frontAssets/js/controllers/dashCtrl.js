debFrontApp.controller('dashCtrl', ["$scope","$http", "$state","profileMenuService",
    function($scope,$http,$state,profileMenuService) {
        $scope.logoPath = 'assets/frontAssets/images/logo.png';
        $scope.loginName = 'Administrador';
        $scope.branch = 'Root';
        $scope.profileMenu = {};

        $http.get('assets/menu.json').success(function(res){
            // El json se refactorea para adaptarlo al menu
            $scope.profileMenu = profileMenuService.refactorMenu(res);
        });
        
        //Cambia secciones principales
        $scope.changeState = function(state){
            $state.go(state.url);
        };
    }
]);