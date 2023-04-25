debFrontApp.controller("gatoCtrl", ["$scope", "$state", "toastr", "gatoService",
    function($scope, $state, toastr, gatoService) {

        $scope.listadoGatos = [];
        $scope.gato = {};



        var getGatos = function () {
        console.log("ENTRO AL CONTROLLER DEL GATO ")
            gatoService.getListadoGatos()
                .success(function (response) {
                console.log("listado de gatos " + response)
                    $scope.listadoGatos = response;
                }).error(function () {
                console.log("Fallo traer los gatos");
            });

        };
        getGatos();

        $scope.delete = function (gatoId) {
            gatoService.deleteGato(gatoId)
                .success(function () {
                    console.log("Gato borrado correctamente");
                    getGatos();
                }).error(function () {
                console.log("Fallo traer los gatos");
                getGatos();
            });
        }

        $scope.getGatoById = function (gatoId) {
            gatoService.getGatoById(gatoId)
                .success(function (response) {
                    $scope.gato = response;
                }).error(function () {
                console.log("Fallo traer los gatos");
            });
        };

        $scope.save = function () {
            $scope.gato.id=null;
            gatoService.save($scope.gato)
                .success(function (response) {
                    console.log(response);
                    getGatos();
                }).error(function () {
                console.log("Fallo guardar gato");
            });

        };


        $scope.update=function() {
            gatoService.update($scope.gato)
                .success(function (response) {
                    console.log(response);
                    getGatos();
                }).error(function () {
                console.log("Fallo updatear gato");
                getGatos();
            });
        };

    }
    ]);