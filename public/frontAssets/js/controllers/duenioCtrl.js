debFrontApp.controller("duenioCtrl", ["$scope", "$state", "toastr", "duenioService",
    function($scope, $state, toastr, duenioService) {

        $scope.listadoDuenios=[];
        $scope.duenio={};


        var getDuenios=function(){
                        console.log("ENTRO AL CONTROLLER DEL DUEÑO ");
            duenioService.getListadoDuenios()
                .success(function(response){
                    $scope.listadoDuenios=response;
                }).error(function(){
                console.log("Fallo traer los dueños");
            });
        };
        getDuenios();

        $scope.delete=function(duenioId){
            duenioService.deleteDuenio(duenioId)
                .success(function(){
                    console.log("Dueño borrado correctamente");
                    getDuenios();
                })
        };


        $scope.getDuenioById=function(duenioId){
            duenioService.getDuenioById(duenioId)
                .success(function(response){
                    $scope.duenio = response;
                }).error(function(){
                    console.log("Fallo traer dueño")
                getDuenios();
            })
        }


        $scope.save = function () {
            $scope.duenio.id =null;
            duenioService.save($scope.duenio)
                .success(function (response) {
                    console.log(response);
                    getDuenios();
                }).error(function () {
                console.log("Fallo al traer los dueños");
            });

        };

        $scope.update=function() {
            duenioService.update($scope.duenio)
                .success(function (response) {
                    console.log(response);
                    getDuenios();
                }).error(function () {
                console.log("Fallo updatear Dueño");
            });
        };

    }
]);