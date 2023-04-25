debFrontApp.controller("servicesListCtrl", ["$scope", "$state", "toastr", "servicesService",
    function($scope, $state, toastr, servicesService) {
        $scope.services = [];

        var errorFn = function (){
            toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
        }

        $scope.init = function (){
            servicesService.getServices().success(function (response){
                $scope.services = response;
                console.log("Succes");
                console.log(response);
            }).error(function (response){
                errorFn();
            });
        };

        $scope.save = function (service){
            servicesService.saveService(service).success(function (response){
                $scope.services.some(function(item) {
                    if (item.id == service.id) {
                        $scope.services[$scope.services.indexOf(item)] = response;
                        toastr.success('El servicio: ' + response.name + ' fue actualizado correctamente.', 'Ok');
                        return true;
                    }
                })
            }).error(function (response){
                console.log(response);
                errorFn();
            });
        };

        $scope.delete = function (customerType){
            servicesService.deleteCustomerType(customerType).success(function (response){
                toastr.success('El customer type fue eliminado correctamente.', 'Ok');
                $scope.init();
            }).error(function (response){
                toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
            });
        }
    }
]);