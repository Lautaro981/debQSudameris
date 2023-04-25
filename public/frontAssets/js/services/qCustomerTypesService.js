debFrontApp.service('qCustomerTypesService', ["$http", function($http){
    var base = '/api/qcustomertypes';

    this.label = 'Tipo de cliente DebQ';
    this.name = "qCustomerTypes";

    this.getCustomerTypes = function (){
        return $http.get(base);
    };
    this.getCustomerType = function (id){
        return $http.get(base + '/' + id);
    };
    this.saveCustomerType = function (businessSector){
        let data = angular.copy(businessSector);
        return $http.put(base +'/'+ data.id, data);
    };
    this.createCustomerType = function (customerType){
        var data = angular.copy(customerType);
        return $http.post(base, data);
    };
    this.deleteCustomerType = function (customerType){
        var data = angular.copy(customerType);
        return $http.delete(base +'/'+ data.id);
    };
}]);