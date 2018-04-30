(function (ng) {

    var mod = angular.module("RF7Mod");

    mod.controller("responseRF7Ctrl", ["$scope", "$http", "$state", function ($scope, $http, $state) {

            var input ={};
            
            var servicios = $state.params.servicios.split(",");
            
            input.evento = $state.params.evento;
            input.servicios = servicios;
            input.fechaInicio = $state.params.fechaInicio;
            input.fechaFin = $state.params.fechaFin;
            input.tipoContrato = $state.params.tipoContrato;
            input.numPersonasPorReserva = $state.params.numPersonasPorReserva;
            input.clienteId = $state.params.clienteId;
            input.reservas = [];
            
            console.log(input);
            var parameter = JSON.stringify(input);

            $http.post("/It2_C-09_m.devia_f.velasquez/rest/reservascolectivas?token="+$state.params.clienteId+"&necesitadas="+$state.params.necesitadas+"&tipo="+$state.params.tipo, parameter).then(function (response) {
                $scope.response = response.data;
            });


        }]);
})(window.angular);