debFrontApp.controller("supportCtrl", ["$scope", "$state", "$http" ,"toastr",
    function($scope, $state, $http, toastr) {

        $scope.support = [];
        $scope.levels = ['ALL','TRACE','DEBUG','INFO','WARN','ERROR','OFF'];


        var errorFn = function (){
            toastr.error("Ha ocurrido un error en el servidor.", "Ops...");
        }

        $scope.init = function (){
        };

        $scope.downloadAllLogs = function() {
            var url= "/support/logs";
            var fileName = "logs.zip";
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            xhr.onload = function() {
                var a = document.createElement('a');
                a.href = window.URL.createObjectURL(xhr.response); // xhr.response is a blob
                a.download = fileName; // Set the file name.
                a.style.display = 'none';
                document.body.appendChild(a);
                a.click();
                if(this.status === 200){
                    $scope.$parent.$emit('newAlert', {time: 4000, type: 'alert-success', message: "Logs descargados correctamente"});
                }else{
                    $scope.$parent.$emit('newAlert', {time: 5000, type: 'alert-danger', message: "Error: " + response.message});
                }
            };
            xhr.open('GET', url);
            xhr.send();
        };

        $scope.downloadActiveLogs = function() {
            var url= "/support/activelogs";
            var fileName = "logs.zip";
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            xhr.onload = function() {
                var a = document.createElement('a');
                a.href = window.URL.createObjectURL(xhr.response); // xhr.response is a blob
                a.download = fileName; // Set the file name.
                a.style.display = 'none';
                document.body.appendChild(a);
                a.click();
                if(this.status === 200){
                    $scope.$parent.$emit('newAlert', {time: 4000, type: 'alert-success', message: "Logs descargados correctamente"});
                }else{
                    $scope.$parent.$emit('newAlert', {time: 5000, type: 'alert-danger', message: "error: " + response.message});
                }
            };
            xhr.open('GET', url);
            xhr.send();
        };



        $scope.getLoggers = function(){
            $http.get('/api/root/loggers').success(function(data) {
                $scope.logs = data;
            }).error(function(error){
                $scope.$parent.$emit('newAlert', {time: 5000, type: 'alert-danger', message: error.message});
            });
        };

        $scope.changeLevel = function(log){
            $http.put('/api/root/logger/'+log.name+'/' + log.level)
                .success(function(data){
                    $scope.$parent.$emit('newAlert', {time: 4000, type: 'alert-success', message: "nivel de log actualizado"});
                })
                .error(function(error){
                    $scope.$parent.$emit('newAlert', {time: 5000, type: 'alert-danger', message: error.message});
                });
        };

        $scope.getLoggers();
    }
]);