debFrontApp.service('rulesService', ["$http", function($http){
    var base = '/api/rules';
    this.label = 'Reglas';
    this.name = "rules";

    this.getRules = function (){
        return $http.get(base);
    };
    this.getRule = function (id){
        return $http.get(base + '/' + id);
    };
    this.createRule = function (rule){
        let data = angular.copy(rule);
        return $http.post(base, data);
    };
    this.saveRule = function (rule){
        let data = angular.copy(rule);
        return $http.put(base +'/'+ data.id, data);
    };
    this.deleteRule = function (rule){
        let data = angular.copy(rule);
        return $http.delete(base +'/'+ data.id);
    };

}]);