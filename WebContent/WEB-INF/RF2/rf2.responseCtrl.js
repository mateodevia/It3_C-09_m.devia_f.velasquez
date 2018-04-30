(function (ng) {

    var mod = angular.module("RF2Mod");

    mod.controller("responseRF2Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            input.servicios = [];
            input.ofertas = [];

            var parameter = JSON.stringify(input);


            $http.post("/It2_C-09_m.devia_f.velasquez/rest/habshosts?token=" + input.operador, parameter).then(function (response) {

                $scope.response = response.data;
            });


        }]);
})(window.angular);