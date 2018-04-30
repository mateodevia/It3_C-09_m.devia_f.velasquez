(function (ng) {
    var mod = angular.module('RF8Mod', ['ui.router']);

    mod.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

            $stateProvider.state('rf8', {
                views: {
                    inputView: {
                        templateUrl: "RF8/rf8.input.html",
                        controller: "RF8Ctrl"
                    }
                }
            }).state('rf8Response', {

                params: {
                	id:null,
                	evento: null,
                	reservas: [],
                	servicios: null,
                	fechaInicio: null,
                	fechaFin: null,
                	tipoContrato: null,
                	numPersonasPorReserva: null,
                	clienteId: null
                },

                views: {
                    inputView: {
                        templateUrl: "RF8/rf8.input.html"
                    },
                    outputView: {
                        templateUrl: "RF8/rf8.output.html",
                        controller: "responseRF8Ctrl"
                    }
                }
            });
        }]);

})(window.angular);