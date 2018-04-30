(function (ng) {

    var mod = angular.module("RF8Mod");

    mod.controller("responseRF8Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params;

            input.servicios = $state.params.servicios.split(",");
            
            var parameter = JSON.stringify(input);
            console.log(input);
            $http.put("/It2_C-09_m.devia_f.velasquez/rest/reservascolectivas?token="+$state.params.clienteId, parameter).then(function (response) {

                $scope.response = response.data;
                console.log(response.data)
            });


        }]);
})(window.angular);