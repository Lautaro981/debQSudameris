debFrontApp.controller("qCustomerTypesListCtrl", ["$scope", "$state", "toastr", "qCustomerTypesService", "rulesService",
    function($scope, $state, toastr, qCustomerTypesService, rulesService) {

        $scope.label = qCustomerTypesService.label;
        $scope.name = qCustomerTypesService.name;
        $scope.values = [];

        var errorFn = function (){
            toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
        };

        $scope.init = function (){
            qCustomerTypesService.getCustomerTypes().success(function (response){
                $scope.values = response;
            }).error(function (){
                errorFn();
            });
        };

        let isInsideRule = function(rules, qCustomerType){
            return rules.some(r=>r.qCustomerType.id === qCustomerType.id);
        };

        $scope.delete = function (qCustomerType){
            let rules = [];
            rulesService.getRules().success(function (response){
                rules=response;
                if(isInsideRule(rules, qCustomerType)){
                    toastr.error("Este tipo de cliente se usa en alguna regla, primero borrelas.", "Ops...");
                }
                else{
                    qCustomerTypesService.deleteCustomerType(qCustomerType).success(function (response){
                        toastr.success('El tipo de cliente fue eliminado correctamente.', 'Ok');
                        $scope.init();
                    }).error(function (response){
                        toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
                    });
                }
            }).error(function (response){
                toastr.error("Ha ocurrido un error en el servidor obteniendo las rules.", "Ops...");
                return true;
            });
        }
    }
]);

debFrontApp.controller("qCustomerTypesNewCtrl", ["$scope", "$state", "$stateParams", "toastr", "qCustomerTypesService",
    function($scope, $state, $stateParams, toastr, qCustomerTypesService) {

        $scope.new= {};
        $scope.label = qCustomerTypesService.label;
        $scope.name = qCustomerTypesService.name;

        var existCustomerType = function (customerTypes, newCustomerType) {
            return customerTypes.some(elem => elem['id_q'] === newCustomerType['id_q']);
        };

        $scope.save = function (){
            $scope.qCustomerTypes = qCustomerTypesService.getCustomerTypes().success(function (response){
                $scope.qCustomerTypes = response;

                if(existCustomerType($scope.qCustomerTypes, $scope.new)){
                    toastr.error("Ya hay un tipo de cliente con ese id.", "Ops...");
                }
                else{
                    qCustomerTypesService.createCustomerType($scope.new).success(function (response){
                        toastr.success('El tipo de cliente fue creado correctamente.', 'Ok');
                        $scope.new = {};
                        $state.go('qCustomerTypes');
                    }).error(function (response){
                        toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
                    });
                }
            }).error(function (){
                toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
            });
        };

    }
]);

debFrontApp.controller("qCustomerTypesEditCtrl", ["$scope", "$state", "$stateParams", "toastr", "qCustomerTypesService",
    function($scope, $state, $stateParams, toastr, qCustomerTypesService) {

        $scope.label = qCustomerTypesService.label;
        $scope.name = qCustomerTypesService.name;

        $scope.qCustomerTypesId = $stateParams.qCustomerTypesId;
        $scope.current = {};

        $scope.init = function(){
            qCustomerTypesService.getCustomerType($scope.qCustomerTypesId).success(function (response){
                $scope.current = response;
            }).error(function (response){
                toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
                $state.go('qCustomerTypes');
            });
        };

        isDuplicated = function (qCustomerTypes, newqCustomerTypes, field){
            let filtered = qCustomerTypes.filter(el=>el.id !== newqCustomerTypes.id);
            return filtered.some(el=> el[field]===newqCustomerTypes[field]);
        }

        $scope.save = function (){
            $scope.qCustomerTypes = qCustomerTypesService.getCustomerTypes().success(function (response){
                $scope.qCustomerTypes = response;

                if(isDuplicated($scope.qCustomerTypes, $scope.current,"label")){
                    toastr.error("Ya existe un tipo de cliente con ese nombre", "Ops...");
                }
                else if(isDuplicated($scope.qCustomerTypes, $scope.current,"id_q")){
                    toastr.error("Ya existe un tipo de cliente con ese ID Q", "Ops...");
                }
                else{
                    qCustomerTypesService.saveCustomerType($scope.current).success(function (response) {
                        toastr.success('El tipo de cliente fue guardado correctamente.', 'Ok');
                        $scope.current = {};
                        $state.go('qCustomerTypes');
                    }).error(function (response) {
                        toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
                    });
                }
            }).error(function (){
                toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
            });
        };

    }
]);

debFrontApp.controller("qCustomerTypesShowCtrl", ["$scope", "$state", "$stateParams", "toastr", "qCustomerTypesService",
    function($scope, $state, $stateParams, toastr, qCustomerTypesService) {

        $scope.label = qCustomerTypesService.label;
        $scope.name = qCustomerTypesService.name;

        $scope.qCustomerTypesId = $stateParams.qCustomerTypesId;
        $scope.current = {};

        $scope.init = function (){
            qCustomerTypesService.getCustomerType($scope.qCustomerTypesId).success(function (response){
                $scope.current = response;
            }).error(function (response){
                toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
                $state.go('qCustomerTypes');
            });
        }
    }
]);