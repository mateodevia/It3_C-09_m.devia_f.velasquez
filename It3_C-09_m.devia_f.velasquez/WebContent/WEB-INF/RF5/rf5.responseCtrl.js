(function (ng) {

    var mod = angular.module("RF5Mod");

    mod.controller("responseRF5Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            var parameter = JSON.stringify(input);

            console.log(input);

            $http.put("/It2_C-09_m.devia_f.velasquez/rest/reservas?token="+$state.params.cliente, parameter).then(function (response) {
                $scope.response = response.data;
            });


        }]);
})(window.angular);