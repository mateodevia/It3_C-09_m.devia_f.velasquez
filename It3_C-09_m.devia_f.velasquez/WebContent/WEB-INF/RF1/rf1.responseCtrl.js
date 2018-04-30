(function(ng){
  
    var mod = angular.module("RF1Mod");

    mod.controller("rf1ResponseCtrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input = $state.params

            input.habHost = [];
            input.casas = [];
            input.apartamentos = [];

            var parameter = JSON.stringify(input);

           

            $http.post("/It2_C-09_m.devia_f.velasquez/rest/personasNaturales", parameter).then(function (response) {

                    $scope.response = response.data;
            })


        }]);
})(window.angular);