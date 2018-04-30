(function (ng) {

    var mod = angular.module("RFC9Mod");

    mod.controller("responseRFC9Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            var parameter = JSON.stringify(input);

            $http.get("/It2_C-09_m.devia_f.velasquez/rest/alojamientos/bajademanda").then(function (response) {

                $scope.response = response.data;
            });


        }]);
})(window.angular);