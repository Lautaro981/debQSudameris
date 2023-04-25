debFrontApp.service('servicesService', ["$http", function($http){
    var base = '/api/service';
    this.getServices = function (){
        return $http.get('/api/services');
    };
    this.saveService = function (service){
        var data = angular.copy(service);
        return $http.put(base +'/'+ data.id, data, {method:"POST"});
    };
    this.getServiceByName = function (name){
        return $http.get('/api/service/'+name+'/name');
    };
}]);