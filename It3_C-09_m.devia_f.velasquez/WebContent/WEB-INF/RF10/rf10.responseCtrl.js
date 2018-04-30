(function(ng){
  
    var mod = angular.module("RF10Mod");

    mod.controller("rf10ResponseCtrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

           

            $http.put("/It2_C-09_m.devia_f.velasquez/rest/ofertasalojamiento/rehabilitar?idAl="+input.idAl+"&fechaCreacion="+input.fechaCreacion+"&token="+input.token+"&fechaRehabilitacion="+input.fechaRehabilitacion).then(function (response) {

                    $scope.response = response.data;
            })


        }]);
})(window.angular);