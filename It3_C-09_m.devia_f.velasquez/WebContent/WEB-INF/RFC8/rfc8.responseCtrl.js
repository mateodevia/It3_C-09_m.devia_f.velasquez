(function (ng) {

    var mod = angular.module("RFC8Mod");

    mod.controller("responseRFC8Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params.idAl;


            $http.get("/It2_C-09_m.devia_f.velasquez/rest/clientes/clientesfrecuentes?idAl="+input).then(function (response) {

                $scope.response = response.data;
            });


        }]);
})(window.angular);