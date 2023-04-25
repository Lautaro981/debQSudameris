debFrontApp.controller('notificationCtrl', ["$scope", "toastr",
    function($scope, toastr) {
        toastr.success('Ejemplo de notificación');
        $scope.newAlert = function(msg){
        	if(msg === 'ok'){
        		toastr.success('Mensaje de éxito', 'Titulo de mensaje');
        	}else if(msg === 'error'){
        		toastr.error('Mensaje de error', 'Titulo de mensaje');
        	}
        }
    }
]);