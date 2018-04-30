(function (ng) {

    var mod = angular.module("RF6Mod");

    mod.controller("responseRF6Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            var parameter = JSON.stringify(input);

            $http.put("/It2_C-09_m.devia_f.velasquez/rest/ofertasalojamiento?token=1", parameter).then(function (response) {

                $scope.response = response.data;
                console.log($scope.response);
            });


        }]);
})(window.angular);