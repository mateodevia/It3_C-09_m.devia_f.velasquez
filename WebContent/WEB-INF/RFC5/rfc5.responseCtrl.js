(function (ng) {

    var mod = angular.module("RFC5Mod");

    mod.controller("responseRFC5Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            var parameter = JSON.stringify(input);

            $http.get("/It2_C-09_m.devia_f.velasquez/rest/clientes/usoTipo").then(function (response) {

                $scope.response = response.data;
            });


        }]);
})(window.angular);