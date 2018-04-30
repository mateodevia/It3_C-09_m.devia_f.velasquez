(function(ng){
  
    var mod = angular.module("RF9Mod");

    mod.controller("rf9ResponseCtrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

           

            $http.put("/It2_C-09_m.devia_f.velasquez/rest/ofertasalojamiento/deshabilitar?idAl="+input.idAl+"&fechaCreacion="+input.fechaCreacion+"&token="+input.token).then(function (response) {

                    $scope.response = response.data;
            })


        }]);
})(window.angular);