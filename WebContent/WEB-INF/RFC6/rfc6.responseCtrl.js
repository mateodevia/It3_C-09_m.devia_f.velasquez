(function (ng) {

    var mod = angular.module("RFC6Mod");

    mod.controller("responseRFC6Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params.id;

            console.log(input);
            $http.get("/It2_C-09_m.devia_f.velasquez/rest/clientes/uso?id="+input).then(function (response) {
            	
                $scope.response = response.data;
                console.log($scope.response);
            });


        }]);
})(window.angular);