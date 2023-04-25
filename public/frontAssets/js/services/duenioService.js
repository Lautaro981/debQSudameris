debFrontApp.service('duenioService', ["$http", function($http){

    urlDuenio='/duenios';
    this.label='Listado de duenios';
    this.name="duenio";

    this.getListadoDuenios=function(){
        return $http.get(urlDuenio);
    };

    this.deleteDuenio=function (id){
        return $http.delete(urlDuenio+ '/' +id)
    };

    this.getDuenioById=function(id){
        return $http.get(urlDuenio+'/'+id);
    };

    this.save=function(data){
        return $http.post(urlDuenio,data);
    };

    this.update=function(data){
        return $http.put(urlDuenio,data);
    };

}]);