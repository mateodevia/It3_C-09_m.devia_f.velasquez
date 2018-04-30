(function (ng) {

    var mod = angular.module("RFC4Mod");

    mod.controller("responseRFC4Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var str = $state.params.servicios;

            var servicios = str.split(",");
            
            str = "";
            
            console.log(servicios);
            
            var i;
            
            for(x in servicios){
            	console.log(x);
            	str = str + "&servicios=" +servicios[x];
            }
            
            console.log(str);
            
            $http.get("/It2_C-09_m.devia_f.velasquez/rest/alojamientos/rfc4?fechaInicio=" + $state.params.fechaInicio + "&fechaFin="+ $state.params.fechaFin + str).then(function (response) {
            	
                $scope.response = response.data;
                console.log($scope.response);
            });


        }]);
})(window.angular);