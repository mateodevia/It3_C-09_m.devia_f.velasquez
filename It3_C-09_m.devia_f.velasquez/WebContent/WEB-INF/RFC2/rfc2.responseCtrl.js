(function (ng) {

    var mod = angular.module("RFC2Mod");

    mod.controller("responseRFC2Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params.num;

            var parameter = JSON.stringify(input);

            $http.get("/It2_C-09_m.devia_f.velasquez/rest/ofertasalojamiento?num="+input).then(function (response) {

                $scope.response = response.data;

                console.log(response.data);
            });


        }]);
})(window.angular);