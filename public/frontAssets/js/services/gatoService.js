debFrontApp.service('gatoService', ["$http", function($http){

    urlGato='/gatos';
    this.label='Listado de gatos';
    this.name="gato";

    this.getListadoGatos=function(){
        return $http.get(urlGato);
    };

    this.deleteGato=function (id){
        return $http.delete(urlGato+ '/' +id)
    };

    this.getGatoById=function(id){
        return $http.get(urlGato+'/'+id);
    };

    this.save=function(data){
        return $http.post(urlGato,data);
    };

    this.update=function(data){
        return $http.put(urlGato,data);
    };


}]);