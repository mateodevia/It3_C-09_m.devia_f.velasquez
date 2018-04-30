(function (ng) {

    var mod = angular.module("RF3Mod");

    mod.controller("responseRF3Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            var parameter = JSON.stringify(input);

            $http.post("/It2_C-09_m.devia_f.velasquez/rest/clientes", parameter).then(function (response) {

                $scope.response = response.data;
            });


        }]);
})(window.angular);