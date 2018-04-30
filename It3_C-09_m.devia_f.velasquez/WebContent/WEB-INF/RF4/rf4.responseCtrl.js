(function (ng) {

    var mod = angular.module("RF4Mod");

    mod.controller("responseRF4Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            var parameter = JSON.stringify(input);

            console.log(input);

            $http.post("/It2_C-09_m.devia_f.velasquez/rest/reservas?token="+$state.params.cliente, parameter).then(function (response) {
                $scope.response = response.data;
                console.log($scope.response)
            });


        }]);
})(window.angular);