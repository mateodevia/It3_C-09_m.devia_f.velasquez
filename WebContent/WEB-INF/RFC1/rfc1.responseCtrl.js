(function (ng) {

    var mod = angular.module("RFC1Mod");

    mod.controller("responseRFC1Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            var parameter = JSON.stringify(input);

            $http.get("/It2_C-09_m.devia_f.velasquez/rest/operadores/dineroanioactual?token=-1").then(function (response) {

                $scope.response = response.data;
            });


        }]);
})(window.angular);