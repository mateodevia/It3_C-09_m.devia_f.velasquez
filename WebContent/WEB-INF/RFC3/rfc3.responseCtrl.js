(function (ng) {

    var mod = angular.module("RFC3Mod");

    mod.controller("responseRFC3Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            var parameter = JSON.stringify(input);

            $http.get("/It2_C-09_m.devia_f.velasquez/rest/alojamientos").then(function (response) {

                $scope.response = response.data;
            });


        }]);
})(window.angular);