(function (ng) {

    var mod = angular.module("RFC7Mod");

    mod.controller("responseRFC7Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;


            $http.get("/It2_C-09_m.devia_f.velasquez/rest/reservas?unidadTiempo="+input.unidadTiempo+"&tipoAl="+input.tipoAl).then(function (response) {

                $scope.response = response.data;
            });


        }]);
})(window.angular);