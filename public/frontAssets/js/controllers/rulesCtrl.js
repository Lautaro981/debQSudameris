debFrontApp.controller("rulesListCtrl", ["$scope", "$state", "toastr", "rulesService",
    function($scope, $state, toastr, rulesService) {

        $scope.label = rulesService.label;
        $scope.name = rulesService.name;
        $scope.values = [];

        var errorFn = function (){
            toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
        };

        $scope.init = function (){
            rulesService.getRules().success(function (response){
                $scope.values = response;
            }).error(function (){
                errorFn();
            });
        };

        $scope.delete = function (rule){
            rulesService.deleteRule(rule).success(function (response){
                toastr.success('La regla fue eliminada correctamente.', 'Ok');
                $scope.init();
            }).error(function (response){
                toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
            });
        }
    }
]);

debFrontApp.controller("rulesNewCtrl", ["$scope", "$state", "$stateParams", "toastr", "rulesService", "qCustomerTypesService",
    function($scope, $state, $stateParams, toastr, rulesService, qCustomerTypesService) {

        $scope.new= {};
        $scope.label = rulesService.label;
        $scope.name = rulesService.name;

        $scope.qCustomerTypes = [];

        var errorFn = function (){
            toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
        };

        $scope.init = function (){
            qCustomerTypesService.getCustomerTypes().success(function (response){
                $scope.qCustomerTypes = response;
            }).error(function (){
                errorFn();
            });
        }

        isDuplicated = function (rules, newRule){
            sameAttributes = function (el) {
                return el.segment === newRule.segment;
            }
            return rules.some(el => sameAttributes(el));
        }

        $scope.save = function (){
            rulesService.getRules().success(function (response){
                $scope.rules = response;
                console.log(response);
                if(isDuplicated($scope.rules, $scope.new)){
                    toastr.error("Ya existe una regla para ese segmento", "Ops...");
                }

                else{
                    rulesService.createRule($scope.new).success(function (response){
                        toastr.success('La regla fue creada correctamente.', 'Ok');
                        $scope.new = {};
                        $state.go('rules');
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

debFrontApp.controller("rulesEditCtrl", ["$scope", "$state", "$stateParams", "toastr", "rulesService", "qCustomerTypesService",
    function($scope, $state, $stateParams, toastr, rulesService, qCustomerTypesService) {

        $scope.label = rulesService.label;
        $scope.name = rulesService.name;

        $scope.rulesId = $stateParams.rulesId;
        $scope.current = {};

        $scope.qCustomerTypes = [];

        var errorFn = function (){
            toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
        };

        $scope.init = function(){
            rulesService.getRule($scope.rulesId).success(function (response){
                $scope.current = response;
            }).error(function (response){
                errorFn();
                $state.go('rules');
            });

            qCustomerTypesService.getCustomerTypes().success(function (response){
                $scope.qCustomerTypes = response;
            }).error(function (){
                errorFn();
            });
        };

        isDuplicated = function (rules, newRule){
            sameAttributes = function (el) {
                return el.segment === newRule.segment;
            }
            return rules.some(el => sameAttributes(el));
        }

        $scope.save = function (){
            $scope.rules = rulesService.getRules().success(function (response){
                $scope.rules = response;

                if(isDuplicated($scope.rules, $scope.current)){
                    toastr.error("Ya existe una regla para ese segmento", "Ops...");
                }
                else{
                    rulesService.saveRule($scope.current).success(function (response) {
                        toastr.success('La regla fue guardada correctamente.', 'Ok');
                        $scope.current = {};
                        $state.go('rules');
                    }).error(function (response) {
                        errorFn();
                    });
                }
            }).error(function (){
                errorFn();
            });
        };

    }
]);

debFrontApp.controller("rulesShowCtrl", ["$scope", "$state", "$stateParams", "toastr", "rulesService",
    function($scope, $state, $stateParams, toastr, rulesService) {

        $scope.label = rulesService.label;
        $scope.name = rulesService.name;

        $scope.rulesId = $stateParams.rulesId;
        $scope.current = {};

        $scope.init = function (){
            rulesService.getRule($scope.rulesId).success(function (response){
                $scope.current = response;
            }).error(function (response){
                toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
                $state.go('rules');
            });
        }
    }
]);